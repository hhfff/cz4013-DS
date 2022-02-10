import java.util.*;

public class Account {
		 int accountNum;
		 String accountName;
		 String passwd;
		 
		 Map saving;
		 
		 public Account() {
			// TODO Auto-generated constructor stub
		}
		 public Account(int accountNum, String accountName, String passwd, Map saving) {
			 this.accountNum= accountNum;
			 this.accountName= accountName;
			 this.passwd = passwd;
			 this.saving = saving;
		 }

		
}
