import glob
import os
from flask import Flask


app = Flask(__name__)

def Rename():
    path = r"../src/test/resources//"
    # search pdf files
    pattern = path + "*.pdf"

    # List of the files that match the pattern
    result = glob.glob(pattern)

    # Iterating the list with the count
    count = 1
    for file_name in result:
        old_name = file_name
        new_name = path + 'File_' + str(count) + ".pdf"
        os.rename(old_name, new_name)
        count = count + 1

    # printing all pdf files
    res = glob.glob(path + "file" + "*.pdf")
    for name in res:
        print(name)
