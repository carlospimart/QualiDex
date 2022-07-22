import os
from flask import Flask

app = Flask(__name__)


path = "../ResultOutput"
path = os.path.realpath(path)
os.startfile(path)
