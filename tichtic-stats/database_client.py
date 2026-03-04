import os
import dotenv
import logging
from datetime import datetime
from sqlalchemy import create_engine, Column, Integer, String, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

dotenv.load_dotenv()
_DATABASE_URL = f'postgresql+psycopg2://{os.environ['DB_USR']}:{os.environ['DB_PWD']}@{os.environ['DB_HOST']}:{os.environ['DB_PORT']}/{os.environ['DB_NAME']}'

engine = create_engine(_DATABASE_URL)
Base = declarative_base()
logging.basicConfig(level=logging.INFO)
log = logging.getLogger()
class TichticStat(Base):
    __tablename__ = 'stats'
    short_code = Column(String, primary_key=True, index=True)
    hits = Column(Integer, index=True)
    last_accessed = Column(DateTime)
    created_at = Column(DateTime)
    original_url = Column(String)

Session = sessionmaker(bind=engine)
session = Session()

def log_stat(short_code: str, original_url: str, created_at: str):
    log.info(f"Logging stat for shortCode: {short_code}, originalUrl: {original_url}, createdAt: {created_at}")
    stat = session.query(TichticStat).filter(TichticStat.short_code == short_code).first()
    if stat:
        log.info(f"Stat found for shortCode: {short_code}, incrementing hits.")
        stat.hits += 1
        stat.last_accessed = datetime.now()
    else:
        log.info(f"No stat found for shortCode: {short_code}, creating new entry.")
        stat = TichticStat(short_code=short_code, hits=1, last_accessed=datetime.utcnow(),
                            created_at=datetime.fromisoformat(created_at), original_url=original_url)
        session.add(stat)
    session.commit()
    session.close()
