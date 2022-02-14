# Reference
# https://github.com/lolfuljames/Distributed-Systems-FacilityBooking/blob/main/src/client/Client.java
# https://github.com/felixputera/cz4013-flight-info/blob/master/client/main.py
# https://github.com/Brabalawuka/CZ4013-Distributed-System-Project/blob/6ebd7086109d0a82f44da1ac7006aa2085e62372/cz4013_client/utils/styled_printing.py#L34
from utils import contants,protocol
from utils.contants import Method

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
            print("Invalid Input! Please Try again.")
        else:
            if ans == 'y':
                serverHost = input("Enter Server Host:").lower()
                serverPort = input("Enter Server Port:").lower()
                protocol.setServerAddress(serverHost,serverPort)
            else:
                protocol.setServerAddress(contants.SERVER_HOST,contants.SERVER_HOST)
            break

def selectService():
    print(f"{Method.CREATE_ACCOUNT.value} | {Method.CREATE_ACCOUNT.description}")
    print(f"{Method.CLOSE_ACCOUNT.value} | {Method.CLOSE_ACCOUNT.description}")
    print(f"{Method.DEPOSITE.value} | {Method.DEPOSITE.description}")
    print(f"{Method.WITHDRAW.value} | {Method.WITHDRAW.description}")
    print(f"{Method.VIEW_BALANCE.value} | {Method.VIEW_BALANCE.description}")
    print(f"{Method.CURRENCY_EXCHANGE.value} | {Method.CURRENCY_EXCHANGE.description}")    
    print(f"{Method.MONITOR.value} | {Method.MONITOR.description}")
    while True:
        inputStr = input("Please select a service:")
        try:
            inputStr = int(inputStr)
            opsID = contants.Method(inputStr)
        except ValueError:
            print("Invalid input! Please Try again.")
            continue
        method_mapper = {1 : Method.CREATE_ACCOUNT.f,
           2 : Method.CLOSE_ACCOUNT.f,
           3 : Method.DEPOSITE.f,
           4 : Method.WITHDRAW.f,
           5 : Method.VIEW_BALANCE.f,
           6 : Method.CURRENCY_EXCHANGE.f,
           7 : Method.MONITOR.f,           
        }
        method_mapper[opsID.value]()


if __name__ == "__main__":
    main()