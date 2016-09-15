from flask_restful import Resource, fields
from flask_restful import marshal_with
from flask_restful import reqparse
from flask_restful import abort
from datetime import datetime

from app import db
from app.models import PackageModel

"""
SQLAlchemy REST API service for working with Packages - encrypted data containers.
"""

package_fields = {
    'id': fields.Integer,  # this field is useless for now (might be useful with more complex API
    'sender_id': fields.String,
    'receiver_id': fields.String,
    'data': fields.String,
    'sent_time': fields.DateTime,
}

parser_package = reqparse.RequestParser()

parser_package.add_argument('sender_id', type=str)
parser_package.add_argument('receiver_id', type=str)
parser_package.add_argument('data', type=str)


class PackageResource(Resource):
    """
    REST resource for package handling
    """
    @marshal_with(package_fields)
    def get(self, receiver_id):
        """
        Get all available packages for specified receiver
        :param receiver_id: string id of the receiver
        :return: list of packages
        """
        packages = PackageModel.query.filter(PackageModel.receiver_id == receiver_id)
        return packages, 200

    @marshal_with(package_fields)
    def post(self):
        """
        Post a new package to the server
        :return: posted package
        """
        parsed_args = parser_package.parse_args()

        if None in parsed_args.values():
            abort(400, message="incorrect header")

        package = PackageModel(
            sender_id=parsed_args['sender_id'],
            receiver_id=parsed_args['receiver_id'],
            data=parsed_args['data'],
            sent_time=datetime.utcnow()
        )

        db.session.add(package)
        db.session.commit()

        return package, 201
