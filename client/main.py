
from utils import contants,protocol
from utils.contants import Method
import utils.services as Services
from utils.systemPrint import ErrorMsg, SystemMsg

def main():
    promptServerAddress()
    print("Connecting...")
    print("Welcome to NTU Distributed Banking System")
    selectService()
    


def promptServerAddress():
    print(f"Server Host: {contants.SERVER_HOST} Port: {contants.SERVER_PORT}")
    while True:
        ans = input("Do you want to change? (Y/N)").lower()
        if ans not in ['y','n']:
            ErrorMsg("Invalid Input! Please Try again.")
        else:
            if ans == 'y':
                serverHost = input("Enter Server Host:").lower()
                serverPort = int(input("Enter Server Port:").lower())  # you forget to convert string to int, port is integer type
                protocol.setServerAddress(serverHost,serverPort)
            else:
                protocol.setServerAddress(contants.SERVER_HOST,contants.SERVER_PORT)
            break


def selectService():
    while True:
        # Main Menu
        print(f"{Method.CREATE_ACCOUNT.value} | Open a new account")
        print(f"{Method.CLOSE_ACCOUNT.value} | Close account")
        print(f"{Method.DEPOSITE.value} | Deposit money to account")
        print(f"{Method.WITHDRAW.value} | Withdraw money from account")
        print(f"{Method.VIEW_BALANCE.value} | View account balance")
        print(f"{Method.CURRENCY_EXCHANGE.value} | Exchange account currency type")    
        print(f"{Method.MONITOR.value} | Monitor server")
        print(f"0 | Exit")
       
        # Get user input
        while True:            
            inputStr = input("Please select a service:")
            if int(inputStr) == 0:
                print("Thank you for using.")
                exit()
            try:
                inputStr = int(inputStr)
                opsID = Method(inputStr)
            except ValueError:
                ErrorMsg("Invalid input! Please Try again.")
                continue

            if opsID == Method.CREATE_ACCOUNT:
                status = Services.create_account_service()
            elif opsID == Method.CLOSE_ACCOUNT:
                status = Services.close_account_service()
            elif opsID == Method.DEPOSITE:
                status = Services.deposite_service()
            elif opsID == Method.WITHDRAW:
                status = Services.withdraw_service()
            elif opsID == Method.VIEW_BALANCE:
                status = Services.view_balance_service()
            elif opsID == Method.CURRENCY_EXCHANGE:
                status = Services.currency_exchange_service()
            elif opsID == Method.MONITOR:
                status = Services.monitor_service()
            if status:
                SystemMsg("Service completed")
            else:
                SystemMsg("Something wrong")
            break




if __name__ == "__main__":
    main()