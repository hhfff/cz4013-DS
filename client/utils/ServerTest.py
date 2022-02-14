import socket
sock = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)

sock.bind(('127.0.0.1',54088))

while True:
    data,addr = sock.recvfrom(218)
    print(str(data))
    message= "Helo I am UDP Server".encode('utf-8')
    sock.sendto(message,addr)

