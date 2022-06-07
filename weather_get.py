import requests
import json

weather = requests.get("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Pozna%C5%84?unitGroup=metric&key=3Z9N76X35VMQH6Z2H362ERTFF&contentType=json")


weather = weather.json()

print(weather)