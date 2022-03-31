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
    '''set server address

    Args:
        server ip (str): IP address of server
        server port (int): Port number of server        
    ''' 
    UDPSocket.setServerAddress(host,port)

def sendRequest(methodCode:Method, dataTuple:Tuple,receiveParaTypeOder:Tuple):
    '''send request to server
    
    Args:
        methodCode (Method): Method enumerated type
        dataTuplle (Tuple): A tuple of request data
        receiveParaTypeOder (Tuple): A tuple of expected data type order from responer

    Returns:
        bool: False if request is unsuccessfully sent True otherwise
        list: A List of received data from server without reply ID
    '''
    try:        
        # increase request id of every new request 
        UDPSocket.request_id +=1

        # append message type, request id, service type in front of request content before marshalling
        marshalled_data = Marshalling.marshall((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple))   
        while True:
            UDPSocket.set_timeout(timeout)

            # simulate packet loss with certain probability
            if (random() >= PacketLossProbability) :
                UDPSocket.send_msg(marshalled_data)            
            else:
                ErrorMsg("Packet Loss")
            try:                
                reply_data = _waitForReply(methodCode,receiveParaTypeOder) 

                # 1st element message type is truncated
                return True,reply_data[1:]                     
            except TimeoutError:
                ErrorMsg(f"No message from server after {timeout} seconds. Packet will be resended!")

        
    
    except Exception as e:
        print(str(e))
        return False,()    


def _waitForReply(methodCode:Method, paraTypeOrder:Tuple):
    '''wait for reply from server
    Args:
        methodCode (Method): Method enumerated type
        paraTypeOrder (Tuple): A tuple of expected data type order from responer

    Returns:
        list: A List of received data from server
    '''
    bytearray_data = UDPSocket.recv_msg()

    # append message type (int) to list of expected server response data type after unmershalling receive data
    list_data = Marshalling.unmarshall(bytearray_data,(int,*paraTypeOrder))
    return list_data

def longRequest(methodCode:Method, intervalTime,dataTuple:Tuple,receiveParaTypeOder:Tuple):
    '''long request which will close after user defined interval dedicated for monitoring service
    Args:
        methodCode (Method): Method enumerated type
        intervalTime (int): Moniter time interval
        dataTuple (Tuple): A tuple of request data
        paraTypeOrder (Tuple): A tuple of expected data type order from responer

    Returns:
        bool: False if monitoring fails True otherwise
    '''
    try:
        # increase request id of every new request 
        UDPSocket.request_id +=1
        # append message type, request id, service type in front of request content before marshalling
        marshalled_data = Marshalling.marshall((Network.Request.value,UDPSocket.request_id,methodCode.value,*dataTuple))
        UDPSocket.send_msg(marshalled_data)

        end_time = time.time() + intervalTime

        # Stop the listening if server never send msg within interval time
        UDPSocket.set_timeout(intervalTime)

        while True:
                    
            reply_data = _waitForReply(methodCode,receiveParaTypeOder)
            end = time.time()            
            ServerReply(reply_data[2]) 

            # break loop if server side turn off monitoring   
            if reply_data[1] == 0:
                return False     

            # update timeout to remaining time interval to stop the listening if server never send msg 
            # within this remaining time interval
            UDPSocket.set_timeout(end_time -end)
    except TimeoutError:
        SystemMsg("Your Subscription expired.")
        return True
    except Exception as e:
        # return fail execution if error is not timeout error
        print(str(e))
        return False

    