import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Server{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[128];
    private DatagramPacket datagramPacket = null;

    public Server(){
        try {
            socket = new DatagramSocket(54088);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        while(true){
            datagramPacket = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(datagramPacket);
                System.out.println(data(buf));
                DataProcess.printByteToHex(buf);
                /* test int
                byte[] temp=new byte[4];
                for(int i=0;i<4;i++){
                    temp[i]=buf[i];
                }
                int i=DataProcess.bytesToInt(temp, ByteOrder.BIG_ENDIAN);
                System.out.print(i);*/

                /* test double   */
                 byte[] temp=new byte[8];
                for(int i=0;i<8;i++){
                    temp[i]=buf[i];
                }
                // i=DataProcess.bytesToDouble(temp, ByteOrder.BIG_ENDIAN);
                //System.out.print(i);




                //send ABCD in utf-8
                //String i=DataProcess.bytesToString(buf,0,4);
                //System.out.print(i);




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

