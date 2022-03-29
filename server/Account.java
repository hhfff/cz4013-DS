import java.util.*;

/**
 * Class use to store account data
 * @author LONG
 *
 */
public class Account {
	private int accountNum;				//accout number
	private String accountName;			//account holder name
	private String passwd;				//account password

	private Map saving;					//current and amount pair for balance in currency type : amount
	public Account() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor for Account class
	 * @param accountNum account number
	 * @param accountName account holder name
	 * @param passwd	password for account
	 * @param saving	amount and currency pair for balance
	 */
	public Account(int accountNum, String accountName, String passwd, Map saving) {
		 this.accountNum= accountNum;
		 this.accountName= accountName;
		 this.passwd = passwd;
		 this.saving = saving;
	 }

	/**
	 * 
	 * @return
	 */
	public int getAccountNum() {
		return accountNum;
	}


	/**
	 * @return
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @param passwd
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * @return
	 */
	public Map getSaving() {
		return saving;
	}

	/**
	 * @param saving
	 */
	public void setSaving(Map saving) {
		this.saving = saving;
	}
}
