import java.net.InetAddress;

public class History {
    private int requestID;
    private int port;
    private InetAddress ipAddress;
    private String replyMessage;

    public History(int requestID, int port, InetAddress ipAddress, String replyMessage) {
        this.requestID = requestID;
        this.port = port;
        this.ipAddress = ipAddress;
        this.replyMessage = replyMessage;
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

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }
}
