from xmlrpc.client import Boolean
from utils import protocol,contants

def create_account_service():
    while True:
        accName = input("Please an account name:")
        pw = input("Please enter your password:")
        re_pw = input("Please re-enter your password:")
        if pw == re_pw:
            protocol.sendRequest(contants.Method.CREATE_ACCOUNT,(accName,pw))

            succ, msg = protocol.waitForReply(contants.Method.CREATE_ACCOUNT,(int,str))

            if succ is False:
                print(msg)
                return False
            else:
                return True
        else:
            print("Passwords does not match. Please re-enter all details.")


def close_account_service():
    while not login_service():
        pass
    while True:
        confirm  = input("Are you sure you want to close current logged in account:")
        if confirm:
            protocol.sendRequest(contants.Method.CLOSE_ACCOUNT,())

            succ, msg = protocol.waitForReply(contants.Method.CREATE_ACCOUNT,(int,str))

            if succ is False:
                print(msg)
                return False
            else:
                return True

        else:
            return True
            
    

def deposite_service():
    login_service()

def withdraw_service():
    login_service()
    
def view_balance_service():
    login_service()

def currency_exchange_service():
    login_service()

def monitor_service():
    login_service()
    while input()!="X":
        pass
        # T =2
        # listening SOCKET
        # REcieve
        

def login_service():
    accNum = input("Please enter your bank account number:")
    accName = input("Please enter your account name:")
    pw = input("Please enter your password:")
    protocol.sendRequest(contants.Method.CREATE_ACCOUNT,(accNum,accName,pw))

    succ, msg = protocol.waitForReply(contants.Method.CREATE_ACCOUNT,(int,str))
    
    if succ is False:
        print(msg)
        return False
    else:
        return True


