from PyQt5 import QtWidgets as qtw
from PyQt5 import QtCore as qtc
from PyQt5 import QtGui as qtg

from qrcode import Ui_Form

class QRCodeWindow(qtw.QWidget):

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        self.ui = Ui_Form()
        self.ui.setupUi(self)

        self.setQrcode()
        #self.showFullScreen()

    def setQrcode(self):
        qr_img = self.ui.qrcode
        qr_img.setPixmap(qtg.QPixmap("assets/qrcode.png"))


if __name__ == '__main__':
    app = qtw.QApplication([])

    widget = QRCodeWindow()
    widget.show()

    app.exec_()