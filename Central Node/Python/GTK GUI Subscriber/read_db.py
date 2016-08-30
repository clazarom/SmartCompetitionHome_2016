#!/usr/bin/python
import MySQLdb

def read_last_sample(table_name):
    db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                         user="syncComp",         # your username
                         passwd="startCount1ng",  # your password
                         db="Compdb")        # name of the data base

    # you must create a Cursor object. It will let
    # you execute all the queries you need
    cur = db.cursor()

   
    # Use all the SQL you like
    cur.execute("SELECT * FROM "+str(table_name))

    # print all the first cell of all the rows
    all_rows = cur.fetchall()
    for row in all_rows:
        print row[0]

    last_row = cur.fetchlast()
    db.close()

    return last_row
