from flask import Flask
from flask_restful import Api
from flask_sqlalchemy import SQLAlchemy

from config import SQLALCHEMY_DATABASE_URI, SQLALCHEMY_TRACK_MODIFICATIONS


app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = SQLALCHEMY_DATABASE_URI
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = SQLALCHEMY_TRACK_MODIFICATIONS
db = SQLAlchemy(app)


from app.resources import PackageResource


api = Api(app)

api.add_resource(PackageResource, '/api/packages/', endpoint='packages')
api.add_resource(PackageResource, '/api/packages/<string:receiver_id>', endpoint='package')
