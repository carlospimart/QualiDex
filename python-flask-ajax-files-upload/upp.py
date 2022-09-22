from flask import Flask

UPLOAD_FOLDER = 'C:/uploads'

upp = Flask(__name__)
upp.secret_key = "secret key"
upp.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
upp.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024