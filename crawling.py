from urllib.request import urlopen
from bs4 import BeautifulSoup

url = "https://hangang.seoul.go.kr/"
html = urlopen(url)
source = html.read()
html.close()

soup = BeautifulSoup(source, "html.parser")
event_table = soup.find(class_="event_list")
event_list = event_table.find_all("li")

for event in event_list:
    place = event.find(class_="place")
    title = event.find("h5")
    date = event.find(class_="date")
    time = event.find(class_="time")
    print(place.get_text())
    print(title.get_text())
    print(date.get_text())
    print(time.get_text())
