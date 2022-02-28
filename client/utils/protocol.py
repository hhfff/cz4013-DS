# TO Do
#

from typing import Tuple
from utils.UDPhelper import UDPSocket
from utils.marshalling import Marshalling
from utils.contants import Method,Network

def setServerAddress(host,port):
    UDPSocket.setServerAddress(host,port)

def sendRequest(methodCode:Method, dataTuple:Tuple):
    marshalled_data = Marshalling.marshall((Network.Request.value,methodCode.value,*dataTuple))
    UDPSocket.send_msg(marshalled_data)


def waitForReply(methodCode:Method, paraTypeOrder:Tuple):
    bytearray_data = UDPSocket.listen_msg()
    list_data = Marshalling.unmarshall(bytearray_data,paraTypeOrder)
    # TODO check if reply mothod code identical
    # drop header
    return list_data[2:]

    