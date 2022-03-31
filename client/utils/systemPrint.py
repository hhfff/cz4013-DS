def ServerReply(msg:str):
    '''Wrapper to print out server reply
    Args:
        msg (str): Message to be displayed
    '''
    print("From Server: " + msg)


def ErrorMsg(msg:str):
    '''Wrapper to print out error message
    Args:
        msg (str): Message to be displayed
    '''
    print("Error: "+msg)

def SystemMsg(msg:str):
    '''Wrapper to print out system message
    Args:
        msg (str): Message to be displayed
    '''
    print("System: "+msg)