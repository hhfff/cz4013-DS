import java.net.InetAddress;
import java.time.LocalTime;

/**
 * @author 
 *Class to store information of the client who register for monitor update.
 */
public class MonitorInfo {
	private int accountNum;			// account number.
	private LocalTime expiretime;	//expire time for client finish monitor update.
	private InetAddress ip;			//IP address of the client.
	private int port;				//port number of the client.
	
	/**
	 * Constructor for MonitorInfo class
	 * @param accountNum	account number.
	 * @param expiretime	expire time for client finish monitor update.
	 * @param ip			IP address of the client.
	 * @param port			port number of the client.
	 */
	public MonitorInfo(int accountNum,LocalTime expiretime,InetAddress ip,int port) {
		this.accountNum = accountNum;
		this.expiretime = expiretime;
		this.ip = ip;
		this.port = port;
	}
	
/**
 * @return
 */
public int getAccountNum() {
	return this.accountNum;
}
/**
 * @return
 */
public LocalTime getExpireTime() {
	return this.expiretime;
}
/**
 * @param expiretime
 */
public void setExpireTime(LocalTime expiretime) {
	this.expiretime = expiretime;
}
/**
 * @return
 */
public InetAddress getIP() {
	return this.ip;
}

/**
 * @param ip
 */
public void setIP(InetAddress ip) {
	this.ip = ip;
}

/**
 * @return
 */
public int getPort() {
	return this.port;
}
/**
 * @param port
 */
public void setport(int port) {
	this.port = port;
}
}
