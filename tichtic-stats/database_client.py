import os
from datetime import datetime
from sqlalchemy import create_engine, Column, Integer, String, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker


_DATABASE_URL = f'postgresql+psycopg2://{os.environ['DB_USR']}:{os.environ['DB_PWD']}@{os.environ['DB_HOST']}:{os.environ['DB_PORT']}/{os.environ['DB_NAME']}'

engine = create_engine(_DATABASE_URL)
Base = declarative_base()

class TichticStat(Base):
    __tablename__ = 'stats'
    short_code = Column(String, primary_key=True, index=True)
    hits = Column(Integer, index=True)
    last_accessed = Column(DateTime)

Session = sessionmaker(bind=engine)
session = Session()

def log_stat(short_code: str):
    stat = session.query(TichticStat).filter(TichticStat.short_code == short_code).first()
    if stat:
        stat.hits += 1
        stat.last_accessed = datetime.now()
    else:
        stat = TichticStat(short_code=short_code, hits=1, last_accessed=datetime.utcnow())
        session.add(stat)
    session.commit()
    session.close()
