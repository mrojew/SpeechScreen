import sys
import time
from PyQt5.uic import loadUi
from PyQt5 import QtWidgets
from PyQt5.QtWidgets import QDialog, QApplication, QWidget
from PyQt5.Qt import Qt
from PyQt5.QtCore import QTimer,QDateTime

from calendar_main import CalendarWindow
from weather_main import WeatherWindow
from info_main import InfoWindow
from qrcode_main import QRCodeWindow


import paho.mqtt.subscribe as subscribe

def on_message(client, userdata, message):
	return str(message.payload.decode("utf-8"))

def change_screen():
    msg = subscribe.simple("test/status", hostname="10.42.0.66")
    screen_name = msg.payload.decode("utf-8").lower()
    print(screen_name)
    if screen_name == "weather":
        widget.setCurrentIndex(1)
    if screen_name == "calendar":
        widget.setCurrentIndex(2)
    if screen_name == "connect":
        widget.setCurrentIndex(0)
    if screen_name == "information":
        widget.setCurrentIndex(3)
    else :
        widget.setCurrentIndex(widget.currentIndex())

app = QApplication(sys.argv)
widget = QtWidgets.QStackedWidget()

screen0 = QRCodeWindow()
screen1 = WeatherWindow()
screen2 = CalendarWindow()
screen3 = InfoWindow()
widget.addWidget(screen0)
widget.addWidget(screen1)
widget.addWidget(screen2)
widget.addWidget(screen3)

widget.setFixedWidth(1920)
widget.setFixedHeight(1080)
widget.show()


timer = QTimer()
timer.timeout.connect(change_screen)
timer.start(100)

ix = 0
widget.setCurrentIndex(0)


try:
    sys.exit(app.exec_())
except:
    print("Exiting")
