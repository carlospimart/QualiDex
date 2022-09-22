#delete previously stored files
from flask import Flask
import os

app = Flask(__name__)

def delete():
        dir = r"../src/test/resources//"
        for f in os.listdir(dir):
                os.remove(os.path.join(dir, f))
