
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private int accountNumber=0;
    private int currentUser=-1;
    private ArrayList<Account> accountList = new ArrayList<Account>();
    private ArrayList<MonitorInfo> monitorList = new ArrayList<MonitorInfo>();

    public AccountService(){}
    public void createUserAccount(String accountName, String password, Currency currency, double balance,InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("create acct\nname %s, passwd: %s, CurrencyType: %s, balance: %f",accountName,password,currency.toString(),balance));
        String replyMessage;
        accountNumber+=1;
        Map<Currency, Double> saving = new HashMap<Currency, Double>();
        saving.put(Currency.CNY, 0.0);
        saving.put(Currency.MYR, 0.0);
        saving.put(Currency.SGD, 0.0);
        saving.put(currency, saving.get(currency)+balance);
        Account newUser = new Account(accountNumber,accountName,password,saving);
        accountList.add(newUser);
        replyMessage="New account for "+newUser.getAccountName()+" has been created, Acoount number is: "+newUser.getAccountNum();
        serviceReply(1,replyMessage,ip,port,replyPacketList);
        monitorUser(accountName+" has create a new account.",replyPacketList);
        //return replyMessage;
    }

    public void closingUserAccount(int accountNum, String accountName, String password, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("closingAcct\nname %s, passwd: %s, acctNUM: %s,",accountName,password,accountNum));

        int i;
        //Account currentAccount= new Account();

        String replyMessage= "Your account has been close successfully";

//        i = userVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentUser;
        if(i!=-1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {
            accountList.remove(i);
            serviceReply(1,replyMessage,ip,port,replyPacketList);
            monitorUser(accountName+"has closing his account",replyPacketList);
        }
        else {
        	serviceReply(0,"Something wrong with User data",ip,port,replyPacketList);
        }


    }

    public void depositToAccount(int accountNum, String accountName, String password, Currency currency, double amount, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("deposite\nname %s, passwd: %s, acctNUM: %s,, CurrencyType: %s, balance: %f",accountName,password,accountNum,currency.toString(),amount));

        int i; double balance;
        //Account currentAccount= new Account();

        String replyMessage="";

//        i = userVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentUser;
        if(i!=-1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {
            balance = (double) accountList.get(i).getSaving().get(currency)+amount;
            accountList.get(i).getSaving().put(currency, balance);
            replyMessage="Your new balance for "+currency.toString()+" is : "+balance;
            serviceReply(1,replyMessage,ip,port,replyPacketList);
            monitorUser(accountName+" deposit some money to account.",replyPacketList);
        }
        else {
        	serviceReply(0,"Something wrong with User data",ip,port,replyPacketList);
        }


    }

    public void wthdrawFromAccount(int accountNum, String accountName, String password, Currency currency, double amount, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("withdraw\nname %s, passwd: %s, acctNUM: %s,, CurrencyType: %s, balance: %f",accountName,password,accountNum,currency.toString(),amount));

        int i; double balance;
        //Account currentAccount= new Account();

        String replyMessage;
//        i = userVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentUser;
        if(i!=-1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {

            if((double) accountList.get(i).getSaving().get(currency)<amount) {
            	replyMessage="Sorry, you don't have enough balance to withdraw "+currency.toString()+" : "+amount+".";
            	serviceReply(0,replyMessage,ip,port,replyPacketList);
            }
            else {
                balance = (double) accountList.get(i).getSaving().get(currency)-amount;
                accountList.get(i).getSaving().put(currency, balance);
                replyMessage="Your new balance for "+currency.toString()+" is : "+balance;
                serviceReply(1,replyMessage,ip,port,replyPacketList);
                monitorUser(accountName+" withdraw some money from account.",replyPacketList);
            }

        }
        else {
        	serviceReply(0,"Something wrong with User data",ip,port,replyPacketList);
        }

    }

    public void viewBalance(int accountNum, String accountName, String password, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("view balance\nname %s, passwd: %s, acctNUM: %s,",accountName,password,accountNum));

        int i;
        String balanceInfo;
        String replyMessage;
//        i = userVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentUser;
        if(i != -1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {
            balanceInfo= accountList.get(i).getSaving().toString().substring(1,accountList.get(i).getSaving().toString().length()-1);
            replyMessage = "Your balance under account : "+accountNum+"is \n"+balanceInfo;
            serviceReply(1,replyMessage,ip,port,replyPacketList);
            monitorUser(accountName+" check his account.",replyPacketList);
        }
        else {
        	serviceReply(0,"Something wrong with User data",ip,port,replyPacketList);
        }

    }

    public void currencyExchange(int accountNum, String accountName, String password, Currency fromCurrency, Currency toCurrency, double amount, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList ) throws Exception {
        System.out.println(String.format("curr exchange\nname %s, passwd: %s, acctNUM: %s,, FromCurrencyType: %s, toCurrencyType: %s,balance: %f",accountName,password,accountNum,fromCurrency.toString(),toCurrency.toString(),amount));

        int i;
        double newAmount;
        String balanceInfo;
        String replyMessage;
//        i = userVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentUser;
        if(i!=-1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {
        	if((double)accountList.get(i).getSaving().get(fromCurrency) >= amount) {
            switch(fromCurrency) {
                case CNY: if(toCurrency== Currency.MYR) {
                    newAmount = amount/1.5;
                }
                else {
                    newAmount = amount/5.0;
                }
                    break;
                case MYR: if(toCurrency== Currency.CNY) {
                    newAmount = amount*1.5;
                }
                else {
                    newAmount = amount/3.0;
                }
                    break;
                case SGD: if(toCurrency== Currency.CNY) {
                    newAmount = amount*5.0;
                }
                else {
                    newAmount = amount*3.0;
                }
                    break;
                default: 	newAmount = 0;
                    break;
            }
            accountList.get(i).getSaving().put(fromCurrency, (double)accountList.get(i).getSaving().get(fromCurrency)-amount);
            accountList.get(i).getSaving().put(toCurrency, (double)accountList.get(i).getSaving().get(toCurrency)+newAmount);
            balanceInfo= accountList.get(i).getSaving().toString().substring(1,accountList.get(i).getSaving().toString().length()-1);
            replyMessage = "Your new balance under account : "+accountNum+" is \n"+balanceInfo;
            
            serviceReply(1,replyMessage,ip,port,replyPacketList);
            monitorUser(accountName+"has exchange currency from "+fromCurrency.toString()+" to "+toCurrency.toString(),replyPacketList);

        }
        else {
        	replyMessage ="Sorry, you don't have enough "+fromCurrency+" to convert to"+toCurrency;
        	serviceReply(0,replyMessage,ip,port,replyPacketList);
        }
        }
        else {
        	serviceReply(0,"Something wrong with User data",ip,port,replyPacketList);
        }
        

    }
    public void registerMonitorUpdate(int accountNum, String accountName, String password, int interval, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
    	System.out.println(String.format("acc num %d , acct name %s, passwd: %s acc list size: %d, ",accountNum,accountName,password,accountList.size()));
    	int i,j;
    	String replyMessage;
    	LocalTime time = LocalTime.now().plusSeconds(interval);
    	MonitorInfo monitorAccount= new MonitorInfo(accountNum,time,ip,port);
    	i= currentUser;
    	if(i!=-1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {
	//    	if(monitorList.isEmpty()) {
	//    		monitorList.add(monitorAccount);
	//    	}
	//    	else{
	//    		for(j=0;i<monitorList.size();j++) {
	//    			if(monitorList.get(j).getAccountNum()==accountNum) {
	//    				monitorList.get(j).setExpireTime(time);
	//    				monitorList.get(j).setIP(ip);
	//    				monitorList.get(j).setport(port);
	//    				break;
	//    			}
	//    		}
	//    		
	//    		if(i==monitorList.size()) {
	//    			monitorList.add(monitorAccount);
	//    		}
	//    	}
    		monitorList.add(monitorAccount);
	    	replyMessage= "You have success register for monitor update";
	    	serviceReply(1,replyMessage,ip,port,replyPacketList);
	    	monitorUser(accountName+"has register for monitor update.",replyPacketList);
    	}
        else {
        	serviceReply(0,"Something wrong with User data",ip,port,replyPacketList);
        }
    	
    	
    	
    
    }
    
    public int userVerification(int accountNum, String accountName, String password,InetAddress ip,int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("acc num %d create acct name %s, passwd: %s acc list size: %d",accountNum,accountName,password,accountList.size()));

        int i;
        String wrongAccountNum = "Sorry, you have enter a invalid account number";
        String wrongAccountName = "Sorry, you have enter a wrong account name";
        String wrongPassword= "Sorry, you have enter the wrong password";
        String userPassed = "User verification success";

        for(i=0;i<accountList.size();i++) {
            if(accountList.get(i).getAccountNum()==accountNum) {
                //currentAccount=accountList.get(i);
                if(!accountList.get(i).getAccountName().equals(accountName)) {
                    serviceReply(0,wrongAccountName,ip,port,replyPacketList);
                    return -1;
                }
                else if(!accountList.get(i).getPasswd().equals(password) ){
                    serviceReply(0,wrongPassword,ip,port,replyPacketList);
                    return -1;
                }
                else {
                    currentUser = i;
                    serviceReply(1,userPassed,ip,port,replyPacketList);
                    return i;
                }
            }
        }
        serviceReply(0,wrongAccountNum,ip,port,replyPacketList);
        return -1;

    }
    
    
    public void serviceReply(int status,String message, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
    	//DatagramSocket socket = new DatagramSocket(Server.getServerPort());
    	System.out.println(message);
    	//byte[] replyHead=DataProcess.intToBytes(1, ByteOrder.BIG_ENDIAN);
    	//byte[] replyResult=DataProcess
    	//byte[] replybuf=DataProcess.stringToBytes(message);
        byte[] replybuf=DataProcess.marshal(1,status,message.length(),message);
    	
    	DatagramPacket reply = new DatagramPacket(replybuf,replybuf.length,ip, 
				port);
		//socket.send(reply);
		replyPacketList.add(reply);
    }

    public void monitorUser(String message, ArrayList<DatagramPacket> replyPacketList)  throws Exception{
        System.out.println(message);
        System.out.println("monitor list size: "+monitorList.size());
        LocalTime time = LocalTime.now();
        //DatagramSocket socket=new DatagramSocket(Server.getServerPort());
        //byte[] updateHead=DataProcess.intToBytes(1, ByteOrder.BIG_ENDIAN);
        //byte[] updatebuf=DataProcess.stringToBytes(message);

        //reply id, status, message length, message
        byte[] updatebuf=DataProcess.marshal(1,1,message.length(),message);
        DatagramPacket update = new DatagramPacket(updatebuf,updatebuf.length);
        if(!monitorList.isEmpty()) {
        	for(int i=0; i<monitorList.size();i++) {
        		if(monitorList.get(i).getExpireTime().compareTo(time)>=0) {
        			update.setAddress(monitorList.get(i).getIP());
        			update.setPort(monitorList.get(i).getPort());
        			//socket.send(update);
        			replyPacketList.add(update);
        			System.out.println("reply List size: "+replyPacketList.size());
        		}
        		else {
        			monitorList.remove(i);
        			i--;
        		}
        	}
        }
        
    }



    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<Account> accountList) {
        this.accountList = accountList;
    }
}
