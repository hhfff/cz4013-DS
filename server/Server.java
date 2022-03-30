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
import java.util.Queue;
import java.util.Scanner;

public class Server{
    private DatagramSocket socket;
    private boolean running;
    private final int BUFFER_SIZE=512;						//buffer size
    private byte[] buf = new byte[BUFFER_SIZE];				//buffer use to store date from packet
    private DatagramPacket datagramPacket = null;
    private AccountService accountService;
    private ArrayList<DatagramPacket> replyPacketList;		//Array list use to store packet for sent to client  
    private static int serverPort=8888;					//service port number
    private double packetChance = 0.6;						//The probability that a packet is sent successfully.
    private int select;
    //maybe requestId with ArrayList is better, but since is small app, can just loop the list
    private ArrayList<History> histories;

    /**
     * initialize
     */
    public Server(){
        histories=new ArrayList<>();
        replyPacketList=new ArrayList<>();
        try {
            socket = new DatagramSocket(54088);
            accountService=new AccountService();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * start loop the server and dispatch received data to processData method, the data cleaned after every loop
     */
    public void start(){
       Scanner userSelect = new Scanner(System.in); 
       System.out.println("Please select Invocation Semantics.");
       System.out.println("0. At-Least-Once");
       System.out.println("1. At-Most-Once\n");
       select = userSelect.nextInt();
       while(select!=0 && select !=1) {
    	   System.out.println("Sorry, You have enter a invalid input. Plase try again.");
    	   System.out.println("Please select Invocation Semantics.");
           System.out.println("0. At-Least-Once");
           System.out.println("1. At-Most-Once");
           select = userSelect.nextInt();
       }
       if(select == 0) {
    	   System.out.println("Server Running with At-Least-Once Invocation Semantics.");
       }
       else if(select == 1) {
    	   System.out.println("Server Running with At-Most-Once Invocation Semantics.\n");
       }
    	while(true){
            datagramPacket = new DatagramPacket(buf, buf.length);
            try {


                socket.receive(datagramPacket);
                buf = datagramPacket.getData();
                processData(buf,datagramPacket.getAddress(), datagramPacket.getPort());

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


    /**
     * send packet in packet list to client, use chance to simulate packet lost
     */
    private void sendPacket(){
        double chance;
    	for(DatagramPacket packet:replyPacketList){
            try {
                //System.out.println("packet sent: "+packet.getAddress()+"  port: "+packet.getPort());
                chance=Math.random();		//generate a random number from 0.0 to 1.0. 								
               // System.out.println("send out success chance is: "+chance);
                
                if(chance<=packetChance) {	//only when random number smaller than packetChance then the packet will send out.  
                	System.out.println("packet sent: "+packet.getAddress()+"  port: "+packet.getPort()+"  packet sent success");
                	socket.send(packet);
                }
                else {
                	System.out.println("packet sent: "+packet.getAddress()+"  port: "+packet.getPort()+"  packet loss");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       replyPacketList.clear();
    }

    
    /**
     * This method use to process service call send from client.
     * @param buf	byte array data extract from UDP packet.
     * @param ip	IP address of current client
     * @param port
     * @throws IOException
     */
    private void processData(byte[] buf, InetAddress ip, int port) throws IOException{

        int msgType=DataProcess.bytesToInt(buf,0,ByteOrder.BIG_ENDIAN);
        int requestID=DataProcess.bytesToInt(buf,4,ByteOrder.BIG_ENDIAN);
        int method=DataProcess.bytesToInt(buf,8,ByteOrder.BIG_ENDIAN);	//extract and unmarshal method id from byte array.
        System.out.println("ip: "+ip.toString()+" port: "+port+" msgType: "+msgType + " request id: "+requestID +" Service: "+Method.getmethod(method));
        if(select==1) {
        //checking history
        for(History history: histories){
            if(history.getRequestID()==requestID && history.getIpAddress().equals(ip) && history.getPort()==port){
                //found in history, just reply
                //socket.send(new DatagramPacket(data,data.length,ip,port));
                replyPacketList.add(history.getReplyPacket());
                System.out.println("found in history");
                sendPacket();
                return ;
            }
        }
        }
        //String msg=null;
        //todo  write error catch
        
        // call method based on method id.
        try{
            //todo need to catch those argument order error?
            if(method==Method.CREATE_ACCOUNT.getValue()){
                var data=DataProcess.unmarshalCreateAccount(buf,12);
                accountService.createUserAccount(
                        (String)data.get("name"),
                        (String) data.get("password"),
                        (Currency) data.get("currencyType"),
                        (double) data.get("amt"),
                        ip,
                        port,
                        replyPacketList
                );
            }else if(method==Method.CLOSE_ACCOUNT.getValue()){
                var data=DataProcess.unmarshalCloseAccount(buf,12);
                accountService.closingUserAccount(
                        (int) data.get("acctNum"),
                        (String) data.get("name"),
                        (String) data.get("password"),
                        ip,
                        port,
                        replyPacketList

                );
            }else if(method==Method.DEPOSITE.getValue()){
                var data=DataProcess.unmarshalDeposit(buf,12);
                accountService.depositToAccount(
                        (int) data.get("acctNum"),
                        (String) data.get("name"),
                        (String) data.get("password"),
                        (Currency) data.get("currencyType"),
                        (double) data.get("amt"),
                        ip,
                        port,
                        replyPacketList
                );

            }else if(method==Method.WITHDRAW.getValue()){
                var data=DataProcess.unmarshalWithdraw(buf,12);
                accountService.withdrawFromAccount(
                        (int) data.get("acctNum"),
                        (String) data.get("name"),
                        (String) data.get("password"),
                        (Currency) data.get("currencyType"),
                        (double) data.get("amt"),
                        ip,
                        port,
                        replyPacketList
                );
            }else if(method==Method.VIEW_BALANCE.getValue()){
                var data=DataProcess.unmarshalViewBalance(buf,12);
                accountService.viewBalance(
                        (int) data.get("acctNum"),
                        (String) data.get("name"),
                        (String) data.get("password"),
                        ip,
                        port,
                        replyPacketList
                );


            }
            else if(method==Method.CURRENCY_EXCHANGE.getValue()){
                var data=DataProcess.unmarshalCurrencyExchange(buf,12);
                accountService.currencyExchange(
                        (int) data.get("acctNum"),
                        (String) data.get("name"),
                        (String) data.get("password"),
                        (Currency) data.get("currencyFromType"),
                        (Currency) data.get("currencyToType"),
                        (double) data.get("amt"),
                        ip,
                        port,
                        replyPacketList

                );
            }else if(method==Method.MONITOR.getValue()){
                var data=DataProcess.unmarshalMonitor(buf,12);
                accountService.registerMonitorUpdate(
                        (int) data.get("acctNum"),
                        (String) data.get("name"),
                        (String) data.get("password"),
                        (int) data.get("intervalTime"),
                        ip,
                        port,
                        replyPacketList
                );

            }else if(method==Method.USER_VERIFICATION.getValue()){
                var data=DataProcess.unmarshalUserVerification(buf,12);
                accountService.userVerification(
                        (int) data.get("acctNum"),
                        (String) data.get("name"),
                        (String) data.get("password"),
                        ip,
                        port,
                        replyPacketList
                );


            }else{

                //todo write no such method reply
            }
            //if success means first item in array list is message
            if(!replyPacketList.isEmpty()) {
            	histories.add(new History(requestID,port,ip,replyPacketList.get(0)));
            	if(histories.size()>10) {
            		histories.remove(0);
            	}
            }
            else {
                String msg="error in server process";
                byte[] data=DataProcess.marshal(requestID,0,msg.length(),msg);
                //socket.send(new DatagramPacket(data,data.length,ip,port));
                replyPacketList.add(new DatagramPacket(data,data.length,ip,port));
            }

        }catch (Exception e){
            e.printStackTrace();
            String msg="Error on request param";
            byte[] data=DataProcess.marshal(requestID,0,msg.length(),msg);
            //socket.send(new DatagramPacket(data,data.length,ip,port));
            replyPacketList.add(new DatagramPacket(data,data.length,ip,port));
        }
        sendPacket();
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int serverPort) {
        Server.serverPort = serverPort;
    }
}

