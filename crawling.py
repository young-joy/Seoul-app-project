import pymysql
import datetime

from urllib.request import urlopen
from bs4 import BeautifulSoup

db = pymysql.connect(host="localhost",
                     user="seoul",
                     passwd="qwer1234",
                     db="seoul_database",
                     charset="utf8")

cur = db.cursor()

url = "https://hangang.seoul.go.kr/"
html = urlopen(url)
source = html.read()
html.close()

soup = BeautifulSoup(source, "html.parser")
event_table = soup.find(class_="event_list")
event_list = event_table.find_all("li")

cur.execute("TRUNCATE event")
for event in event_list:
    place = event.find(class_="place")
    title = event.find("h5")
    date = event.find(class_="date")
    time = event.find(class_="time")
    query = ("INSERT INTO event (title, location, date, time)" 
              + " VALUES ('" 
              + title.string + "', '" 
              + place.string + "', '" 
              + date.string + "', '" 
              + time.string + "')")
    cur.execute(query)
db.commit()
db.close()
print(datetime.datetime.now())
