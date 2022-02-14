from enum import Enum
from client.utils.services import *
class Method(Enum):
    CREATE_ACCOUNT = (1,"Open a new account",create_account_service)
    CLOSE_ACCOUNT = (2, "Close account",close_account_service)
    DEPOSITE = (3,"Deposit money to account",deposite_service)
    WITHDRAW = (4,"Withdraw money from account",withdraw_service)
    VIEW_BALANCE = (5,"View account balance",view_balance_service)
    CURRENCY_EXCHANGE = (6,"Exchange account currency type",currency_exchange_service)
    MONITOR = (7,"Monitor server",monitor_service)
    # make enum support multiple attributes: https://stackoverflow.com/questions/12680080/python-enums-with-attributes 
    def __new__(cls, v, description, f):
        entry = object.__new__(cls) 
        entry.v = entry._value_ = v  # set the value, and the extra attribute
        entry.description = description
        entry.f = f
        return entry


print(Method.CLOSE_ACCOUNT.v)
