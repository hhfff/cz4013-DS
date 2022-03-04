import socket
class UDPSocket:    
    # default UDP setting         
    client_socket = socket.socket(socket.AF_INET,socket.SOCK_DGRAM  ) 
    client_socket.bind(('0.0.0.0',12345))
    socket_bufer =  128
    request_id = 0
    # TODO
    # 1. add request id
    # 2. cache = []
    # 3. 
    @classmethod
    def setServerAddress(cls,host ='127.0.0.1', port= 54088):
        cls.serverAddress = (host, port)

    @classmethod
    def send_msg(cls, msg: bytearray):
        cls.client_socket.sendto(msg, cls.serverAddress)
        # start timer
        # listen_msg() # 

    @classmethod
    def listen_msg(cls):
        while True:
            data, addr = cls.client_socket.recvfrom(cls.socket_bufer) 
            return data, addr                       

if __name__ == '__main__':
    UDPSocket.setServerAddress('127.0.0.1',54088)
    UDPSocket.send_msg(msg=b'test')
    msg,_ = UDPSocket.listen_msg()
    print(type(msg),_)
