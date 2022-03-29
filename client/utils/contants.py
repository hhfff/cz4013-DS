from enum import Enum
SERVER_HOST = '127.0.0.1' #'192.168.2.2'
SERVER_PORT = 54088
SOCKET_BUFFER = 4096

PacketLossProbability = 0.1
timeout = 5

class Method(Enum):
    CREATE_ACCOUNT = 1
    CLOSE_ACCOUNT = 2
    DEPOSITE = 3
    WITHDRAW = 4
    VIEW_BALANCE = 5
    CURRENCY_EXCHANGE = 6
    MONITOR = 7
    USER_VERIFICATION = 8    

class Currency(Enum):
    CNY = 0
    MYR = 1
    SGD = 2

class Network(Enum):
    Request = 0
    Reply = 1

