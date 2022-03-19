import java.util.*;

/**
 * @author LONG
 *
 */
public class Account {
	private int accountNum;
	private String accountName;
	private String passwd;

	private Map saving;
	public Account() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param accountNum
	 * @param accountName
	 * @param passwd
	 * @param saving
	 */
	public Account(int accountNum, String accountName, String passwd, Map saving) {
		 this.accountNum= accountNum;
		 this.accountName= accountName;
		 this.passwd = passwd;
		 this.saving = saving;
	 }

	public int getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Map getSaving() {
		return saving;
	}

	public void setSaving(Map saving) {
		this.saving = saving;
	}
}
