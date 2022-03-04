# TO Do
#

from typing import Tuple
from utils.UDPhelper import UDPSocket
from utils.marshalling import Marshalling
from utils.contants import Method,Network



def setServerAddress(host,port):
    UDPSocket.setServerAddress(host,port)

# add request id
def sendRequest(methodCode:Method, dataTuple:Tuple,receiveParaTypeOder:Tuple):
    UDPSocket.request_id +=1
    marshalled_data = Marshalling.marshall((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple))
    UDPSocket.send_msg(marshalled_data)
    reply_data = _waitForReply(methodCode,receiveParaTypeOder)
    return True,reply_data[1:]


def _waitForReply(methodCode:Method, paraTypeOrder:Tuple):
    bytearray_data = UDPSocket.listen_msg()
    # frist two item reply id , status
    list_data = Marshalling.unmarshall(bytearray_data,(int,int,*paraTypeOrder))

    return list_data

    