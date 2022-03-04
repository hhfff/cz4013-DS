from enum import Enum
SERVER_HOST = '127.0.0.1'
SERVER_PORT = 2345
SOCKET_BUFFER = 4096

# class Method(Enum):
#     CREATE_ACCOUNT = (1,"Open a new account",create_account_service)
#     CLOSE_ACCOUNT = (2, "Close account",close_account_service)
#     DEPOSITE = (3,"Deposit money to account",deposite_service)
#     WITHDRAW = (4,"Withdraw money from account",withdraw_service)
#     VIEW_BALANCE = (5,"View account balance",view_balance_service)
#     CURRENCY_EXCHANGE = (6,"Exchange account currency type",currency_exchange_service)
#     MONITOR = (7,"Monitor server",monitor_service)
#     # make enum support multiple attributes: https://stackoverflow.com/questions/12680080/python-enums-with-attributes 
#     def __new__(cls, val, description, f):
#         entry = object.__new__(cls) 
#         entry.val = entry._value_ = val  # set the value, and the extra attribute
#         entry.description = description
#         entry.f = f
#         return entry

class Method(Enum):
    CREATE_ACCOUNT = 1
    CLOSE_ACCOUNT = 2
    DEPOSITE = 3
    WITHDRAW = 4
    VIEW_BALANCE = 5
    CURRENCY_EXCHANGE = 6
    MONITOR = 7
    # make enum support multiple attributes: https://stackoverflow.com/questions/12680080/python-enums-with-attributes 

class Currency(Enum):
    CNY = 1
    MYR = 2
    SGD = 3

class Network(Enum):
    Request = 0
    Reply = 1