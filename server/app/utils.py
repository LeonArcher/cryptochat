from threading import Thread
from datetime import datetime, timedelta
import time

from app import db
from app.models import PackageModel
from config import REMOVE_PACKAGES_OLDER_THAN_HOURS, RUN_CLEANER_EVERY_SECONDS


class DatabaseCleaner(Thread):
    """
    Provide database automatic cleaning
    """
    def __init__(self, name='database_cleaner'):
        Thread.__init__(self, name=name)

    def run(self):
        """
        Run database cleaning procedure in a periodic loop
        :return: None
        """
        while True:
            current_time = datetime.utcnow()
            maximum_old_time = current_time - timedelta(hours=REMOVE_PACKAGES_OLDER_THAN_HOURS)

            old_packages = db.session.query(PackageModel).filter(PackageModel.sent_time < maximum_old_time)
            old_packages.delete()
            db.session.commit()

            time.sleep(RUN_CLEANER_EVERY_SECONDS)
