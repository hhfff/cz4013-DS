# TO Do
#

from typing import Tuple
from utils.UDPhelper import UDPSocket
from utils.marshalling import Marshalling
from utils.contants import Method,Network

request_id = 0
reply_id = 0

def setServerAddress(host,port):
    UDPSocket.setServerAddress(host,port)

# add request id
def sendRequest(methodCode:Method, dataTuple:Tuple):
    # request_id += 1
    marshalled_data = Marshalling.marshall((3,1,methodCode.value,*dataTuple))
    UDPSocket.send_msg(marshalled_data)



def waitForReply(methodCode:Method, paraTypeOrder:Tuple):
    bytearray_data = UDPSocket.listen_msg()
    list_data = Marshalling.unmarshall(bytearray_data,paraTypeOrder)
    # TODO check if reply mothod code identical
    # drop header
    return list_data[2:]

    