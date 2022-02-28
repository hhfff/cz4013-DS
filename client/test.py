from utils import protocol,contants

protocol.setServerAddress("127.0.0.1",54088)
protocol.sendRequest(contants.Method.CREATE_ACCOUNT,("1234",1,1234.0,"Acc Name"))

# succ, msg = protocol.waitForReply(contants.Method.CREATE_ACCOUNT,(int,str))
# print(succ,msg)