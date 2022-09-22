from flask import Flask
import subprocess
app = Flask(__name__)
subprocess.call([r'..\BatchFile\testNGBatchFile2.bat'])
