from utils import protocol,contants

protocol.setServerAddress("",12345)
protocol.sendRequest(contants.Method.CREATE_ACCOUNT,("test Acc Name","123456"))

succ, msg = protocol.waitForReply(contants.Method.CREATE_ACCOUNT,(int,str))
print(succ,msg)