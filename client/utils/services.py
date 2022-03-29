# from xmlrpc.client import Boolean, Server
from utils import protocol,contants
from utils.systemPrint import ErrorMsg, ServerReply
import getpass

def _login_service():
    '''Authentication Service'''
    while True:
        accNum = input("Please enter your bank account number:")
        if not accNum.isdigit():
            ErrorMsg("Account number has to be in digit")
            continue
        accName = input("Please enter your account name:")        
        pw = getpass.getpass("Please enter your password:")
        if len(pw)>4:
            ErrorMsg("Password cannot more than 4 characters. Please try again.")
            continue
        accNum = int(accNum)

        # server method parameters: password, account number, account name
        # expected server response data type: execution status (int), message (str)
        succ, msg = protocol.sendRequest(contants.Method.USER_VERIFICATION,(pw,accNum,accName),(int,str))

        if succ:
            ServerReply(msg[1])
            if msg[0] == 1:
                return True, (pw,accNum,accName)
            else:            
                return False,()
        else:

            return False,()

def create_account_service():
    '''open new account service'''
    while True:
        accName = input("Please enter an account name:")                
        pw = getpass.getpass("Please enter your password:")
        if len(pw)>4:
            ErrorMsg("Password cannot more than 4 characters. Please try again.")
            continue        
        re_pw = getpass.getpass("Please re-enter your password:")
        if pw == re_pw:            
            print(f"{contants.Currency.CNY.value} | Chinese yuan renminbi")
            print(f"{contants.Currency.MYR.value} | Malaysian ringgit")
            print(f"{contants.Currency.SGD.value} | Singapore dollar")
            
                        
            while True:
                try:
                    currency_type_input = input(f"Please select initial curreny type:")
                    currency_type = contants.Currency(int(currency_type_input))
                except ValueError:
                    ErrorMsg("Invalid type! Please Try again.")
                    continue
                try:
                    curreny_amt_input = input(f"Please select initial ammount ({currency_type.name}):")
                    curreny_amt = float(curreny_amt_input)
                    break
                except ValueError:
                    ErrorMsg("Invalid input! Please Try again.")
                    continue

            # server method parameters: password, currency account name, currency type, amount, account name
            # expected server response data type: execution status (int), message (str)
            succ, msg = protocol.sendRequest(contants.Method.CREATE_ACCOUNT,(pw,currency_type.value,curreny_amt,accName),(int,str))             
            if succ:
                ServerReply(msg[1])
                if msg[0] == 1:
                    return True
                else:                    
                    return False
            else:

                return False
        else:
            ErrorMsg("Passwords does not match. Please re-enter all details.")

def close_account_service():
    '''closs account service'''
    logged_in = False
    while not logged_in:
        # user_cred: password, account number, account name
        logged_in, user_cred = _login_service() 

    confirm  = input("Are you sure you want to close current logged in account: (Enter 'N' or 'n' to cancel)")
    if confirm.lower() not in ['N','n']:

        # server method parameters: password, account number, account name
        # expected server response data type: execution status (int), message (str)
        succ, msg =protocol.sendRequest(contants.Method.CLOSE_ACCOUNT,(*user_cred[:-1],user_cred[-1]),(int,str))                    
        if succ:
            ServerReply(msg[1])
            if msg[0] == 1:
                return True
            else:                
                return False
        else:

            return False
    else:
        return True
            
def deposite_service():
    '''Deposit money service'''
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  
    while True:
        try:
            print(f"{contants.Currency.CNY.value} | Chinese yuan renminbi")
            print(f"{contants.Currency.MYR.value} | Malaysian ringgit")
            print(f"{contants.Currency.SGD.value} | Singapore dollar")
            curreny_type_input = input(f"Please enter deposite curreny type:")
            curreny_type = contants.Currency(int(curreny_type_input))
        except ValueError:
            ErrorMsg("Invalid type! Please Try again.")
            continue
        try:
            curreny_amt_input = input(f"Please select deposite amount ({curreny_type.name}):")
            curreny_amt = float(curreny_amt_input)
            break
        except ValueError:
            ErrorMsg("Invalid input! Please Try again.")
            continue

    # server method parameters: password, account number, currency type, amount, account name
    # expected server response data type: execution status (int), message (str)
    succ, msg = protocol.sendRequest(contants.Method.DEPOSITE,(*user_cred[:-1],curreny_type.value,curreny_amt,user_cred[-1]),(int,str)) 
    if succ:
        ServerReply(msg[1])
        if msg[0] == 1:
            return True
        else:                
            return False
    else:

        return False            

def withdraw_service():
    '''Withdraw money service'''
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  
    while True:
        try:
            print(f"{contants.Currency.CNY.value} | Chinese yuan renminbi")
            print(f"{contants.Currency.MYR.value} | Malaysian ringgit")
            print(f"{contants.Currency.SGD.value} | Singapore dollar")
            curreny_type_input = input(f"Please enter withdraw curreny type:")
            curreny_type = contants.Currency(int(curreny_type_input))
        except ValueError:
            ErrorMsg("Invalid type! Please Try again.")
            continue
        try:
            curreny_amt_input = input(f"Please select withdraw amount ({curreny_type.name}):")
            curreny_amt = float(curreny_amt_input)
            break
        except ValueError:
            ErrorMsg("Invalid input! Please Try again.")
            continue
    
    # server method parameters: password, account number, currency type, amount, account name
    # expected server response data type: execution status (int), message (str)
    succ, msg = protocol.sendRequest(contants.Method.WITHDRAW,(*user_cred[:-1],curreny_type.value,curreny_amt,user_cred[-1]),(int,str)) 
    if succ:
        ServerReply(msg[1])
        if msg[0] == 1:
            return True
        else:                
            return False
    else:

        return False   

def view_balance_service():
    '''view account balance service '''
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  

    # server method parameters: password, account number, account name
    # expected server response data type: execution status (int), message (str)
    succ, msg =protocol.sendRequest(contants.Method.VIEW_BALANCE,user_cred,(int,str))      
    if succ:
        ServerReply(msg[1])
        if msg[0] == 1:
            return True
        else:                
            return False
    else:

        return False

def currency_exchange_service():
    '''Currency exchange service'''
    logged_in = False
    while not logged_in:
        logged_in, user_cred = _login_service()  
    while True:
        try:
            print(f"{contants.Currency.CNY.value} | Chinese yuan renminbi")
            print(f"{contants.Currency.MYR.value} | Malaysian ringgit")
            print(f"{contants.Currency.SGD.value} | Singapore dollar")
            src_curreny_type_input = input(f"Please enter source curreny type:")
            src_curreny_type = contants.Currency(int(src_curreny_type_input))
            tar_curreny_type_input = input(f"Please enter target curreny type:")
            tar_curreny_type = contants.Currency(int(tar_curreny_type_input))
        except ValueError:
            ErrorMsg("Invalid type! Please Try again.")
            continue
        try:
            curreny_amt_input = input(f"Please select exchange amount ({src_curreny_type.name}):")
            curreny_amt = float(curreny_amt_input)
            break
        except ValueError:
            ErrorMsg("Invalid input! Please Try again.")
            continue

    # server method parameters: password, account number, scource currency type, target currency type,
    #                           exchange amount, account name
    # expected server response data type: execution status (int), message (str)
    succ, msg = protocol.sendRequest(contants.Method.CURRENCY_EXCHANGE,(*user_cred[:-1],src_curreny_type.value,tar_curreny_type.value,curreny_amt,user_cred[-1]),(int,str)) 
    if succ:
        ServerReply(msg[1])
        if msg[0] == 1:
            return True
        else:                
            return False
    else:

        return False  
    
def monitor_service():
    '''Monitoring service'''
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
            ErrorMsg("Invalid input! Please Try again.")
            continue

    # server method parameters: password, account number, monitor interval, account name
    # expected server response data type: execution status (int), message (str)
    succ = protocol.longRequest(contants.Method.MONITOR,intervalTime,(*user_cred[:-1],intervalTime,user_cred[-1]),(int,str))
    return succ
        




