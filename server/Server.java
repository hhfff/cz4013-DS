import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[128];
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
                socket.receive(datagramPacket);
                //System.out.println(data(buf));
                //DataProcess.printByteToHex(buf);

                //msg type(4 byte, 0 or 1), request id(from client), method type
                int msgType=DataProcess.bytesToInt(buf,0,ByteOrder.BIG_ENDIAN);
                int requestID=DataProcess.bytesToInt(buf,4,ByteOrder.BIG_ENDIAN);
                //todo  write error catch
                int method=DataProcess.bytesToInt(buf,8,ByteOrder.BIG_ENDIAN);

                //todo need to catch those argument order error?
                if(method==Method.CREATE_ACCOUNT.getValue()){
                    var data=DataProcess.unmarshalCreateAccount(buf,12);
                    accountService.createUserAccount(
                            (String)data.get("name"),
                            (String) data.get("password"),
                            (Currency) data.get("currencyType"),
                            (double) data.get("amt")
                    );
                }else if(method==Method.CLOSE_ACCOUNT.getValue()){
                    var data=DataProcess.unmarshalCloseAccount(buf,12);
                    accountService.closingUserAccount(
                        (int) data.get("acctNum"),
                        (String) data.get("name"),
                        (String) data.get("password")

                    );
                }else if(method==Method.DEPOSITE.getValue()){
                    var data=DataProcess.unmarshalDeposite(buf,12);
                    accountService.depositToAccount(
                            (int) data.get("acctNum"),
                            (String) data.get("name"),
                            (String) data.get("password"),
                            (Currency) data.get("currencyType"),
                            (double) data.get("amt")
                    );

                }else if(method==Method.WITHDRAW.getValue()){
                    var data=DataProcess.unmarshalWithdraw(buf,12);
                    accountService.wthdrawFromAccount(
                            (int) data.get("acctNum"),
                            (String) data.get("name"),
                            (String) data.get("password"),
                            (Currency) data.get("currencyType"),
                            (double) data.get("amt")
                    );
                }else if(method==Method.WITHDRAW.getValue()){
                    var data=DataProcess.unmarshalViewBalance(buf,12);
                    accountService.currencyExchange(
                            (int) data.get("acctNum"),
                            (String) data.get("name"),
                            (String) data.get("password"),
                            (Currency) data.get("currencyFromType"),
                            (Currency) data.get("currencyToType"),
                            (double) data.get("amt")

                    );
                }else if(method==Method.MONITOR.getValue()){
                    var data=DataProcess.unmarshalMonitor(buf,12);
                    int secs=(int) data.get("intervalTime");
                    System.out.println(secs);
                }else{
                    //todo write no such method reply
                }







            } catch (IOException e) {
                e.printStackTrace();
            }
            buf = new byte[128];
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




}

