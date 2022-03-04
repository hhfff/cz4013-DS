from utils import protocol,contants

protocol.setServerAddress("192.168.2.2",12345)
protocol.sendRequest(contants.Method.CREATE_ACCOUNT,("1234",1,1234.0,"Acc Name"))

# succ, msg = protocol.waitForReply(contants.Method.CREATE_ACCOUNT,(int,str))
# print(succ,msg)
