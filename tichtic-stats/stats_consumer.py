from kafka import KafkaConsumer
import signal
import json
import logging
import database_client

from tenacity import retry, stop_after_attempt, wait_exponential

logging.basicConfig(level=logging.INFO)
log = logging.getLogger()

@retry(stop=stop_after_attempt(3), wait=wait_exponential(multiplier=1, min=4, max=10))
def process_message(message):
    try:
        incoming_data = message.value.decode('utf-8')
        log.info(f"Received message: {incoming_data}")
        event = json.loads(incoming_data)
        if 'shortCode' in event:
            log.info(f"Logging stat for shortCode: {event['shortCode']}")
            database_client.log_stat(event.get('shortCode'),event.get('originalUrl'),event.get('createdAt'))
        else:
            log.warning(f"Message does not contain 'shortCode': {incoming_data}")
    except Exception as e:
        log.exception(f"Error processing message: {e}")
        raise

def handle_shutdown(consumer):
    if consumer:
        try:
            consumer.close()
            log.info("Kafka consumer closed.")
        except Exception as e:
            log.exception(f"Error closing Kafka consumer: {e}")

def main():
    consumer = None
    try:
        consumer = KafkaConsumer(
            'tichtic-stats',
            bootstrap_servers=['localhost:9093'],
            auto_offset_reset='earliest',
            enable_auto_commit=True,
            group_id='tichtic-stats-consumer-group',
            max_poll_records=100,
            consumer_timeout_ms=10000
        )
        signal.signal(signal.SIGTERM, lambda signo, frame: handle_shutdown(consumer))
        signal.signal(signal.SIGINT, lambda signo, frame: handle_shutdown(consumer))

        log.info("Starting Kafka consumer...")
        while True:
            message_batch = consumer.poll(timeout_ms=1000)
            if message_batch:
                for tp, messages in message_batch.items():
                    for message in messages:
                        process_message(message)
    except Exception as e:
        log.exception(f"Failed to create Kafka consumer: {e}")
        if consumer:
            handle_shutdown(consumer)
        exit(1)

if __name__ == "__main__":
    main()
