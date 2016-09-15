import os


# base path
basedir = os.path.abspath(os.path.dirname(__file__))

# database settings
SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.path.join(basedir, 'app.db')
SQLALCHEMY_TRACK_MODIFICATIONS = True
