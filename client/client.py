import socket
import struct


 


serverAddressPort   = ("127.0.0.1", 54088)
bufferSize          = 128
# Create a UDP socket at client side
money=100

#print(money.to_bytes(4,'little'))
#print(money.to_bytes(4,'big')) 

bytesToSend= bytearray(struct.pack(">d", 100.56))  #double, big endian, !d network format
#bytesToSend=bytes('ABCD','utf-8') #string
UDPClientSocket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
# Send to server using created UDP socket
UDPClientSocket.sendto(bytesToSend, serverAddressPort)
#msgFromServer = UDPClientSocket.recvfrom(bufferSize)
#msg = "Message from Server {}".format(msgFromServer[0])
#print(msg)


