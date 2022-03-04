# TO Do
#

from typing import Tuple
from utils.UDPhelper import UDPSocket
from utils.marshalling import Marshalling
from utils.contants import Method,Network



def setServerAddress(host,port):
    UDPSocket.setServerAddress(host,port)

# add request id
def sendRequest(methodCode:Method, dataTuple:Tuple):
    UDPSocket.request_id +=1
    marshalled_data = Marshalling.marshall((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple))
    UDPSocket.send_msg(marshalled_data)



def waitForReply(methodCode:Method, paraTypeOrder:Tuple):
    bytearray_data = UDPSocket.listen_msg()
    list_data = Marshalling.unmarshall(bytearray_data,paraTypeOrder)


    # TODO check if reply mothod code identical
    # drop header
    return list_data[0],list_data[1:]

    