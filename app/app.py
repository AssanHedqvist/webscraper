import mysql.connector
import os
from dotenv import load_dotenv
from mysql.connector import Error

load_dotenv() 

db_host = os.getenv("DB_HOST")
db_port = int(os.getenv("DB_PORT"))
db_name = os.getenv("DB_NAME")
db_user = os.getenv("DB_USER")
db_pass = os.getenv("DB_PASS")
try:
    mydb = mysql.connector.connect(
    host=db_host,
    port=db_port,
    user=db_user,
    password=db_pass,
    database=db_name
    )
    if mydb.is_connected():
        print("Connected to MYSQL")
        
except Error as e:
        print("Error", e)

cursor = mydb.cursor()

query = ("SELECT name FROM test")

cursor.execute(query)
rows = cursor.fetchall()
print(rows)
