from xmlrpc.client import Boolean
from utils import protocol,contants

def _login_service():
    accNum = input("Please enter your bank account number:")
    accName = input("Please enter your account name:")
    pw = input("Please enter your password:")
    succ, msg = protocol.sendRequest(contants.Method.CREATE_ACCOUNT,(accNum,accName,pw),(int,str))

    if succ:
        print(msg[1])
        if msg[0] == 1:
            return True, (accNum,accName,pw)
        else:            
            return False
    else:

        return False

def create_account_service():
    while True:
        accName = input("Please enter an account name:")
        pw = input("Please enter your password:")
        re_pw = input("Please re-enter your password:")
        if pw == re_pw:
            print(f"{contants.Currency.SGD.value} | Singapore dollar")
            print(f"{contants.Currency.MYR.value} | Malaysian ringgit")
            print(f"{contants.Currency.CNY.value} | Chinese yuan renminbi")            
            while True:
                try:
                    curreny_type_input = input(f"Please select initial curreny type:")
                    curreny_type = contants.Currency(curreny_type_input)
                except ValueError:
                    print("Invalid type! Please Try again.")
                    continue
                try:
                    curreny_amt_input = input(f"Please select initial ammount ({curreny_type.name}):")
                    curreny_amt = float(curreny_amt_input)
                    break
                except ValueError:
                    print("Invalid input! Please Try again.")
                    continue
            succ, msg = protocol.sendRequest(contants.Method.CREATE_ACCOUNT,(accName,pw,curreny_type.value,curreny_amt),(int,str))             
            if succ:
                if msg[0] == 1:
                    return True
                else:
                    print(msg[1])
                    return False
            else:

                return False
        else:
            print("Passwords does not match. Please re-enter all details.")


def close_account_service():
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()        
    confirm  = input("Are you sure you want to close current logged in account:")
    if confirm:
        succ, msg =protocol.sendRequest(contants.Method.CLOSE_ACCOUNT,user_cred)        
        if succ:
            print(msg[1])
            if msg[0] == 1:
                return True
            else:                
                return False
        else:

            return False

    else:
        return True
            
    

def deposite_service():
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  
    while True:
        try:
            curreny_type_input = input(f"Please enter deposite curreny type:")
            curreny_type = contants.Currency(curreny_type_input)
        except ValueError:
            print("Invalid type! Please Try again.")
            continue
        try:
            curreny_amt_input = input(f"Please select deposite amount ({curreny_type.name}):")
            curreny_amt = float(curreny_amt_input)
            break
        except ValueError:
            print("Invalid input! Please Try again.")
            continue
    succ, msg = protocol.sendRequest(contants.Method.DEPOSITE,(*user_cred,curreny_type.value,curreny_amt),(int,str)) 
    if succ:
        print(msg[1])
        if msg[0] == 1:
            return True
        else:                
            return False
    else:

        return False            



def withdraw_service():
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  
    while True:
        try:
            curreny_type_input = input(f"Please enter withdraw curreny type:")
            curreny_type = contants.Currency(curreny_type_input)
        except ValueError:
            print("Invalid type! Please Try again.")
            continue
        try:
            curreny_amt_input = input(f"Please select withdraw amount ({curreny_type.name}):")
            curreny_amt = float(curreny_amt_input)
            break
        except ValueError:
            print("Invalid input! Please Try again.")
            continue
    succ, msg = protocol.sendRequest(contants.Method.WITHDRAW,(*user_cred,curreny_type.value,curreny_amt),(int,str)) 
    if succ:
        print(msg[1])
        if msg[0] == 1:
            return True
        else:                
            return False
    else:

        return False   
    
def view_balance_service():
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  
    succ, msg =protocol.sendRequest(contants.Method.VIEW_BALANCE,user_cred)        
    if succ:
        print(msg[1])
        if msg[0] == 1:
            return True
        else:                
            return False
    else:

        return False


def currency_exchange_service():
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  
    while True:
        try:
            src_curreny_type_input = input(f"Please enter source curreny type:")
            src_curreny_type = contants.Currency(src_curreny_type_input)
            tar_curreny_type_input = input(f"Please enter target curreny type:")
            tar_curreny_type = contants.Currency(tar_curreny_type_input)
        except ValueError:
            print("Invalid type! Please Try again.")
            continue
        try:
            curreny_amt_input = input(f"Please select exchange amount ({src_curreny_type.name}):")
            curreny_amt = float(curreny_amt_input)
            break
        except ValueError:
            print("Invalid input! Please Try again.")
            continue
    succ, msg = protocol.sendRequest(contants.Method.CURRENCY_EXCHANGE,(*user_cred,src_curreny_type.value,tar_curreny_type.value,curreny_amt),(int,str)) 
    if succ:
        print(msg[1])
        if msg[0] == 1:
            return True
        else:                
            return False
    else:

        return False  
    

def monitor_service():
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  
    while True:
        try:
            intervalTime = input(f"Please enter monitor interval (in second):")
            print(f"Monitoring start for {intervalTime} secound(s)")
            intervalTime = int(intervalTime)
            break
        except ValueError:
            print("Invalid input! Please Try again.")
            continue
    succ = protocol.longRequest(contants.Method.MONITOR,intervalTime,(*user_cred,intervalTime,(int,str)))
    return succ
        




