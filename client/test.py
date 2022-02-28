from utils import protocol,contants
protocol.setServerAddress("","")
protocol.sendRequest(contants.Method.CREATE_ACCOUNT,("test Acc Name","123456"))