import java.net.InetAddress;
import java.time.LocalTime;

public class MonitorInfo {
	private int accountNum;
	private LocalTime expiretime;
	private InetAddress ip;
	private int port;
	
	public MonitorInfo(int accountNum,LocalTime expiretime,InetAddress ip,int port) {
		this.accountNum = accountNum;
		this.expiretime = expiretime;
		this.ip = ip;
		this.port = port;
	}
	
public int getAccountNum() {
	return this.accountNum;
}
public LocalTime getExpireTime() {
	return this.expiretime;
}
public void setExpireTime(LocalTime expiretime) {
	this.expiretime = expiretime;
}
public InetAddress getIP() {
	return this.ip;
}

public void setIP(InetAddress ip) {
	this.ip = ip;
}

public int getPort() {
	return this.port;
}
public void setport(int port) {
	this.port = port;
}
}
