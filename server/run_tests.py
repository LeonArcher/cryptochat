import os
import requests
import json
from app import app as app_test
from app import db as db_test
import unittest
import tempfile
import threading
import time
import base64

SUCCESS = 200
INCORRECT_HEADER = 400
NOT_FOUND = 404
ADDED = 201


class FlaskTest(unittest.TestCase):
    """
    Main testing class
    """
    def setUp(self):
        self.db_fd, app_test.config['DATABASE'] = tempfile.mkstemp()
        app_test.config['TESTING'] = True
        self.app = app_test.test_client()

        with app_test.app_context():
            db_test.drop_all()
            db_test.create_all()

        def run_server():
            app_test.run(debug=False)

        self.thread = threading.Thread(target=run_server, name='MainThread', args=())
        self.thread.daemon = True
        self.thread.start()

        time.sleep(3)  # time should be enough to start the server

    def tearDown(self):
        os.close(self.db_fd)
        os.unlink(app_test.config['DATABASE'])

    def test_packages_handle(self):
        url = 'http://127.0.0.1:5000/api/packages/alex42'

        response = requests.get(url)

        status_code = response.status_code
        self.assertEqual(status_code, SUCCESS)
        self.assertFalse(json.loads(response.text))

        url = 'http://127.0.0.1:5000/api/packages/'
        headers = {'Content-Type': 'application/json'}
        data = json.dumps({
            'sender_id': 'peter84',
            'receiver_id': 'alex42',
            'data': str(base64.b64encode(b'test_bytes_string'))
        })

        response = requests.post(url, headers=headers, data=data)
        status_code = response.status_code

        self.assertEqual(status_code, ADDED)

        url = 'http://127.0.0.1:5000/api/packages/alex42'

        response = requests.get(url)

        status_code = response.status_code
        self.assertEqual(status_code, SUCCESS)

        data = json.loads(response.text)

        self.assertEqual(data[0]['sender_id'], 'peter84')
        self.assertEqual(data[0]['receiver_id'], 'alex42')
        self.assertEqual(data[0]['data'], str(base64.b64encode(b'test_bytes_string')))


if __name__ == '__main__':
    unittest.main()
