import requests
import json
from datetime import datetime

from weather import Ui_Form

from PyQt5 import QtWidgets as qtw
from PyQt5 import QtCore as qtc
from PyQt5 import QtGui as qtg

class WeatherWindow(qtw.QWidget):
    
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        self.days = ("Poniedziałek","Wtorek","Środa","Czwartek","Piątek","Sobota","Niedziela")

        self.ui = Ui_Form()
        self.ui.setupUi(self)

        #self.showFullScreen()

        self.setWeather()
     
    def setWeather(self):
        
        try:
            weather = requests.get("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Pozna%C5%84?unitGroup=metric&key=3Z9N76X35VMQH6Z2H362ERTFF&contentType=json")
            weather = weather.json()
        except:
            print("API Error: no response")
        
        
        img1 = self.ui.imageWeather1
        self.ui.day1.setText(self.days[datetime.today().weekday()])
        
        img2 = self.ui.imageWeather2
        self.ui.day2.setText(self.days[datetime.today().weekday()+1])
        
        img3 = self.ui.imageWeather3
        self.ui.day3.setText(self.days[datetime.today().weekday()+2])
        
        img1.setScaledContents(True)
        img1.setPixmap(qtg.QPixmap("assets/" + weather["days"][0]["icon"] + ".png"))
        
        img2.setScaledContents(True)
        img2.setPixmap(qtg.QPixmap("assets/" + weather["days"][0]["icon"] + ".png"))

        img3.setScaledContents(True)
        img3.setPixmap(qtg.QPixmap("assets/" + weather["days"][0]["icon"] + ".png"))

    
        self.ui.infoWeather1.setText(str(weather["days"][0]["temp"]))
        self.ui.infoWeather2.setText(str(weather["days"][1]["tempmax"]))
        self.ui.infoWeather3.setText(str(weather["days"][2]["tempmax"]))

    


        
        
        



if __name__ == '__main__':
    app = qtw.QApplication([])

    widget = WeatherWindow()
    widget.show()

    app.exec_()