import os


# base path
basedir = os.path.abspath(os.path.dirname(__file__))

# database settings
SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.path.join(basedir, 'app.db')
SQLALCHEMY_TRACK_MODIFICATIONS = True

# history settings
REMOVE_PACKAGES_OLDER_THAN_HOURS = 24
RUN_CLEANER_EVERY_SECONDS = 3600
