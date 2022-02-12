import socket
class UDPSocket:    
    # default UDP setting         
    client_socket = socket.socket(socket.AF_INET,socket.SOCK_DGRAM  ) 
    socket_bufer =  128

    @classmethod
    def setServerAddress(cls,host, port):
        cls.serverAddress = (host, port)

    @classmethod
    def send_msg(cls, msg: bytes):
        cls.client_socket.sendto(msg, cls.serverAddress)

    @classmethod
    def listen_msg(cls):

        while True:
            data, addr = cls.client_socket.recvfrom(cls.socket_bufer) 
            return data, addr                       

if __name__ == '__main__':
    UDPSocket.setServerAddress('127.0.0.1',54088)
    UDPSocket.send_msg(msg=b'test')
