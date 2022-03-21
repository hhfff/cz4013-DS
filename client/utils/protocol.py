# TO Do
#
import time
# from tkinter import E
from typing import Tuple
from utils.UDPhelper import UDPSocket,TimeoutError
from utils.marshalling import Marshalling
from utils.contants import Method,Network,PacketLossProbability,timeout
from random import random # generate number [0,1]
from utils.systemPrint import ErrorMsg, ServerReply, SystemMsg


def setServerAddress(host,port):
    UDPSocket.setServerAddress(host,port)

# add request id
def sendRequest(methodCode:Method, dataTuple:Tuple,receiveParaTypeOder:Tuple):
    try:        
        UDPSocket.request_id +=1
        marshalled_data = Marshalling.marshall((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple))   
        # print("Msg to server")
        # print("-------------------------------------------")
        # print((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple)) 
        # print("-------------------------------------------")
        while True:
            UDPSocket.set_timeout(timeout)
            # simulate packet loss with certain probability
            if (random() >= PacketLossProbability) :
                UDPSocket.send_msg(marshalled_data)            
            else:
                ErrorMsg("Packet Loss")
            try:                
                reply_data = _waitForReply(methodCode,receiveParaTypeOder) 
                return True,reply_data[1:]                     
            except TimeoutError:
                ErrorMsg(f"No message from server after {timeout} seconds. Packet will be resended!")


        # frist two item reply id , status
    
    except Exception as e:
        print(str(e))
        return False,()    


def _waitForReply(methodCode:Method, paraTypeOrder:Tuple):
    bytearray_data = UDPSocket.recv_msg()    
    list_data = Marshalling.unmarshall(bytearray_data,(int,*paraTypeOrder))
    # print("Raw msg from server")
    # print("-------------------------------------------")
    # print(list_data)
    # print("-------------------------------------------")
    return list_data

def longRequest(methodCode:Method, intervalTime,dataTuple:Tuple,receiveParaTypeOder:Tuple):
    try:
        UDPSocket.request_id +=1
        marshalled_data = Marshalling.marshall((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple))
        UDPSocket.send_msg(marshalled_data)

        end_time = time.time() + intervalTime

        # Stop the listening if server never send msg within interval time
        UDPSocket.set_timeout(intervalTime)

        while True:
            
            
            reply_data = _waitForReply(methodCode,receiveParaTypeOder)
            end = time.time()            
            ServerReply(reply_data[2])    
            if reply_data[1] == 0:
                return False     

            # update timeout 
            UDPSocket.set_timeout(end_time -end)
    except TimeoutError:
        SystemMsg("Your Subscription expired.")
        return True
    except Exception as e:
        print(str(e))
        return False

    