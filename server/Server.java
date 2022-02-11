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
    static enum Currency{
    	CNY,
    	MYR,
    	SGD
    
    }
	private int accountNumber=0;
	ArrayList<Account> accountList = new ArrayList<Account>();
	
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


    public void createUserAccount(String accountName, String password, Currency currency, double balance) {
    	accountNumber+=1;
    	Map<Currency, Double> saving = new HashMap<Currency, Double>();
    	saving.put(Currency.CNY, 0.0);
    	saving.put(Currency.MYR, 0.0);
    	saving.put(Currency.SGD, 0.0);
    	saving.put(currency, saving.get(currency)+balance);
    	Account newUser = new Account(accountNumber,accountName,password,saving);
    	accountList.add(newUser);
    	
    	serviceReply("New account for "+newUser.accountName+" has been created, Acoount number is: "+newUser.accountNum);
    	updateUser(accountName+" has create a new account.");
    }
    
    public void closingUserAccount(int accountNum, String accountName, String password) {
		int i;
    	//Account currentAccount= new Account();

		String CancelSuccess= "Your account has been close successfully";
		
		i = userVerification(accountNum,accountName,password);
		
		if(i!=-1) {
			accountList.remove(i);
			serviceReply(CancelSuccess);
			updateUser(accountName+"has closing his account");
		}
    	
    	
    }
    
    public void depositToAccount(int accountNum, String accountName, String password, Currency currency, double amount) {
    	int i; double balance;
    	//Account currentAccount= new Account();

		String depositSuccess="";
		
		i = userVerification(accountNum,accountName,password);
		if(i!=-1) {
			balance = (double) accountList.get(i).saving.get(currency)+amount;
			accountList.get(i).saving.put(currency, balance);
			depositSuccess="Your new balance for "+currency.toString()+" is : "+balance;
			serviceReply(depositSuccess);
			updateUser(accountName+" deposit some money to account.");
		}
			
    
    	
    }

    public void WithdrawFromAccount(int accountNum, String accountName, String password, Currency currency, double amount) {
    	int i; double balance;
    	//Account currentAccount= new Account();

		String balanceNotEnoug= "Sorry, you don't have enough balance to withdraw "+currency.toString()+" : "+amount+".";
		String depositSuccess="";
		i = userVerification(accountNum,accountName,password);
		if(i!=-1) {
			
		if((double) accountList.get(i).saving.get(currency)<amount) {
			serviceReply(balanceNotEnoug);
		}
		else {
			balance = (double) accountList.get(i).saving.get(currency)-amount;
			accountList.get(i).saving.put(currency, balance);
			depositSuccess="Your new balance for "+currency.toString()+" is : "+balance;
			serviceReply(depositSuccess);
			updateUser(accountName+" withdraw some money from account.");
		}
		
		}
    	
    }

    public void viewBalance(int accountNum, String accountName, String password) {
    	int i;
    	String balanceInfo; 
    	i = userVerification(accountNum,accountName,password);
    	if(i != -1) {
    		balanceInfo= accountList.get(i).saving.toString();
    		serviceReply("Your balance under account : "+accountNum+"is \n"+balanceInfo.substring(1,balanceInfo.length()-1));
    		updateUser(accountName+" check his account.");
    	}
    	
    }
    
    public void currencyExchange(int accountNum, String accountName, String password, Currency currency1,  Currency currency2, double amount ) {
    	int i;
    	double newAmount; 
    	String balanceInfo;
    	i = userVerification(accountNum,accountName,password);
    	if((double)accountList.get(i).saving.get(currency1) >= amount) {
    		switch(currency1) {
    			case CNY: if(currency2== Currency.MYR) {
    						newAmount = amount/1.5;
    					  }
    					  else {
    						newAmount = amount/5.0;
    					  }
    					  break;
    			case MYR: if(currency2== Currency.CNY) {
							newAmount = amount*1.5;
						  }
    					  else {
    						newAmount = amount/3.0;
			  			  }
						  break;
    			case SGD: if(currency2== Currency.CNY) {
							newAmount = amount*5.0;
						  }
    					  else {
    						newAmount = amount*3.0;
    					  }
						  break;
			    default: 	newAmount = 0;
			    		  break;
    		}
    		accountList.get(i).saving.put(currency1, (double)accountList.get(i).saving.get(currency1)-amount);
    		accountList.get(i).saving.put(currency2, (double)accountList.get(i).saving.get(currency2)+newAmount);
    		balanceInfo= accountList.get(i).saving.toString();
    		serviceReply("Your new balance under account : "+accountNum+" is \n"+balanceInfo.substring(1,balanceInfo.length()-1));
    		updateUser(accountName);
    		
    	}
    	else {
    		serviceReply("Sorry, you don't have enough "+currency1+" to convert to"+currency2);
    	}
    	
    }
    public void serviceReply(String message) {
    	System.out.println(message);
    	
    }
    
    public void updateUser(String message) {
    	System.out.println(message);
    }
    
    private int userVerification(int accountNum, String accountName, String password) {
    	int i;
    	String wrongAccountNum = "Sorry, you have enter a invalid account number";
		String wrongAccountName = "Sorry, you have enter a wrong account number";
		String wrongPassword= "Sorry, you have enter the wrong password";
		for(i=0;i<accountList.size();i++) {
			if(accountList.get(i).accountNum==accountNum) {
				//currentAccount=accountList.get(i);
				break;
			}
		}
		if(i==accountList.size()) {
			serviceReply(wrongAccountNum);
			return -1;
		}
		else if(accountList.get(i).accountName != accountName) {
			serviceReply(wrongAccountName);
			return -1;
		}
		else if(accountList.get(i).passwd != password) {
			serviceReply(wrongPassword);
			return -1;
		}
		else {
			return i;
		}
		
    }

}

