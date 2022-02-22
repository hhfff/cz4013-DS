import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server{
    private DatagramSocket socket;
    private boolean running;
    private final int BUFFER_SIZE=128;
    private byte[] buf = new byte[BUFFER_SIZE];
    private DatagramPacket datagramPacket = null;
    private AccountService accountService;

    public Server(){
        try {
            socket = new DatagramSocket(54088);
            accountService=new AccountService();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        while(true){
            datagramPacket = new DatagramPacket(buf, buf.length);
            try {
                //testing code
                int messsageType=0;
                int requestITempD=0;
                int methodTemp=1;//1-7
                String pass="pass";
                int currFromType=0;
                int currToType;
                double money=1000.0;
                String name="haskel";
                int acctNUm=1;

                buf=DataProcess.marshal(messsageType,requestITempD,methodTemp,pass,currFromType,money,name.length(),name);


                processData(buf,datagramPacket.getAddress(),datagramPacket.getPort());

                socket.receive(datagramPacket);
                //System.out.println(data(buf));
                //DataProcess.printByteToHex(buf);



            } catch (IOException e) {
                e.printStackTrace();
            }
            buf = new byte[BUFFER_SIZE];
        }

    }
    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
    private void processData(byte[] buf, InetAddress ip, int port) throws IOException{
        //msg type(4 byte, 0 or 1), request id(from client), method type
        int msgType=DataProcess.bytesToInt(buf,0,ByteOrder.BIG_ENDIAN);
        System.out.println("msgtype:  "+msgType);
        int requestID=DataProcess.bytesToInt(buf,4,ByteOrder.BIG_ENDIAN);
        System.out.println("reqID:  "+requestID);
        //todo  write error catch
        int method=DataProcess.bytesToInt(buf,8,ByteOrder.BIG_ENDIAN);

        //todo need to catch those argument order error?
        if(method==Method.CREATE_ACCOUNT.getValue()){
            var data=DataProcess.unmarshalCreateAccount(buf,12);
            accountService.createUserAccount(
                    (String)data.get("name"),
                    (String) data.get("password"),
                    (Currency) data.get("currencyType"),
                    (double) data.get("amt"),
                    ip,
                    port
            );
        }else if(method==Method.CLOSE_ACCOUNT.getValue()){
            var data=DataProcess.unmarshalCloseAccount(buf,12);
            accountService.closingUserAccount(
                    (int) data.get("acctNum"),
                    (String) data.get("name"),
                    (String) data.get("password"),
                    ip,
                    port

            );
        }else if(method==Method.DEPOSITE.getValue()){
            var data=DataProcess.unmarshalDeposite(buf,12);
            accountService.depositToAccount(
                    (int) data.get("acctNum"),
                    (String) data.get("name"),
                    (String) data.get("password"),
                    (Currency) data.get("currencyType"),
                    (double) data.get("amt"),
                    ip,
                    port
            );

        }else if(method==Method.WITHDRAW.getValue()){
            var data=DataProcess.unmarshalWithdraw(buf,12);
            accountService.wthdrawFromAccount(
                    (int) data.get("acctNum"),
                    (String) data.get("name"),
                    (String) data.get("password"),
                    (Currency) data.get("currencyType"),
                    (double) data.get("amt"),
                    ip,
                    port
            );
        }else if(method==Method.CURRENCY_EXCHANGE.getValue()){
            var data=DataProcess.unmarshalViewBalance(buf,12);
            accountService.currencyExchange(
                    (int) data.get("acctNum"),
                    (String) data.get("name"),
                    (String) data.get("password"),
                    (Currency) data.get("currencyFromType"),
                    (Currency) data.get("currencyToType"),
                    (double) data.get("amt"),
                    ip,
                    port

            );
        }else if(method==Method.MONITOR.getValue()){
            var data=DataProcess.unmarshalMonitor(buf,12);
            int secs=(int) data.get("intervalTime");
            System.out.println(secs);
        }else{
            //todo write no such method reply
        }


    }




}

