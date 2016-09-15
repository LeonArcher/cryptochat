from app import db


class PackageModel(db.Model):
    """
    The model contains sender/receiver information, encrypted string encoded data and the server sent time
    """
    __tablename__ = 'PackageModel'

    id = db.Column(db.Integer, primary_key=True, unique=True)
    sender_id = db.Column(db.String)
    receiver_id = db.Column(db.String)
    data = db.Column(db.String)
    sent_time = db.Column(db.DateTime)

    def __repr__(self):
        return 'Message ID: {} From: {} To: {} At: {}'.format(
            self.id,
            self.sender_id,
            self.receiver_id,
            self.sent_time
        )
