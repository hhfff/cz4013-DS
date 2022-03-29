import socket
class UDPSocket:    
    # default UDP setting         
    client_socket = socket.socket(socket.AF_INET,socket.SOCK_DGRAM  ) 
    socket_bufer =  128
    request_id = 0 

    @classmethod
    def setServerAddress(cls,host ='127.0.0.1', port= 54088):
        '''set server address''' 
        cls.serverAddress = (host, port)

    @classmethod
    def send_msg(cls, msg: bytearray):
        '''send request packet to server address''' 
        cls.client_socket.sendto(msg, cls.serverAddress)

    @classmethod
    def recv_msg(cls):   
        '''listen respond packet from serve, reject any packet that is not from server address'''  
        try:
            data, addr = cls.client_socket.recvfrom(cls.socket_bufer) 
            if addr == cls.serverAddress:   
                return data     
            else:
                raise Exception(f'Unexpected Message From {addr} Detected! Something wrong!')
        except socket.timeout:
            raise TimeoutError('Udp socket timeout!')


    @classmethod
    def set_timeout(cls,time):        
        cls.client_socket.settimeout(time)

class TimeoutError(Exception):
    """Raised when the UDP socket timeout"""
    pass


# code for test purpose
if __name__ == '__main__':
    UDPSocket.setServerAddress('127.0.0.1',54088)
    UDPSocket.send_msg(msg=b'test')
    msg,_ = UDPSocket.listen_msg()
    print(type(msg),_)
