import java.net.DatagramPacket;
import java.net.InetAddress;


/**
 * The history class store the client information such as ID, ip address, port and packet used to reply the client
 */
public class History {
    private int requestID;
    private int port;
    private InetAddress ipAddress;
    //private String replyMessage;
    private DatagramPacket replyPacket;

    public History(int requestID, int port, InetAddress ipAddress, DatagramPacket replyPacket) {
        this.requestID = requestID;
        this.port = port;
        this.ipAddress = ipAddress;
        this.replyPacket=replyPacket;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public DatagramPacket getReplyPacket() {
        return replyPacket;
    }

    public void setReplyPacket(DatagramPacket replyPacket) {
        this.replyPacket = replyPacket;
    }
}
