from PyQt5 import QtWidgets as qtw
from PyQt5 import QtCore as qtc
from PyQt5 import QtGui as qtg

from calendarr import Ui_Form

class CalendarWindow(qtw.QWidget):

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        self.ui = Ui_Form()
        self.ui.setupUi(self)

        #self.showFullScreen()


if __name__ == '__main__':
    app = qtw.QApplication([])

    widget = CalendarWindow()
    widget.show()

    app.exec_()