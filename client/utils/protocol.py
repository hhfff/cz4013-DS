# TO Do
#
import time
from tkinter import E
from typing import Tuple
from utils.UDPhelper import UDPSocket
from utils.marshalling import Marshalling
from utils.contants import Method,Network



def setServerAddress(host,port):
    UDPSocket.setServerAddress(host,port)

# add request id
def sendRequest(methodCode:Method, dataTuple:Tuple,receiveParaTypeOder:Tuple):
    try:
        UDPSocket.request_id +=1
        marshalled_data = Marshalling.marshall((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple))   
        print("Msg to server")
        print("-------------------------------------------")
        print((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple)) 
        print("-------------------------------------------")
        
        UDPSocket.send_msg(marshalled_data)
        # TODO
        # try 
        reply_data = _waitForReply(methodCode,receiveParaTypeOder)
        time.sleep(1)


        # frist two item reply id , status
    except Exception as e:
        print(str(e))
        return False,()
    return True,reply_data[1:]


def _waitForReply(methodCode:Method, paraTypeOrder:Tuple):
    bytearray_data = UDPSocket.listen_msg()
    
    list_data = Marshalling.unmarshall(bytearray_data,(int,*paraTypeOrder))
    print("Raw msg from server")
    print("-------------------------------------------")
    print(list_data)
    print("-------------------------------------------")
    return list_data

def longRequest(methodCode:Method, intervalTime,receiveParaTypeOder:Tuple):
    try:
        UDPSocket.request_id +=1
        marshalled_data = Marshalling.marshall((Network.Request.value,UDPSocket.request_id,methodCode.value,intervalTime))
        UDPSocket.send_msg(marshalled_data)
        timeout = time.time() + intervalTime   # interval + now
        while True:
            reply_data = _waitForReply(methodCode,receiveParaTypeOder)
            print("Server:",reply_data[2])
            if time.time() > timeout:
                return True
    except:
        return False




    