
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;


/**
 * This class will contain all the service that server provide to client.
 * @author 
 *
 */
public class AccountService {
    /**
     * 
     */
    private int accountNumber=0;	// account number increase 1 after each account created.
    private int currentclient=-1;		//use to indicate client who currently use the service.
    private ArrayList<Account> accountList = new ArrayList<Account>();		//list to store all account information.
    private ArrayList<MonitorInfo> monitorList = new ArrayList<MonitorInfo>();	//list use to store the client register for update monitor.

    public AccountService(){}
    
    /**
     * This method use for create a new account for client.
     * @param accountName   name of the account holder.
     * @param password		Account password
     * @param currency		Currency of initial balance
     * @param balance  		Amount of money in account
     * @param ip    		IP address of current client
     * @param port    		Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out. 
     * @throws Exception
     */
    public void createUserAccount(String accountName, String password, Currency currency, double balance,InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("create account\n name %s, passwd: %s, CurrencyType: %s, balance: %f",accountName,password,currency.toString(),balance));
        String replyMessage;
        accountNumber+=1;
        Map<Currency, Double> saving = new HashMap<Currency, Double>();
        saving.put(Currency.CNY, 0.0);
        saving.put(Currency.MYR, 0.0);
        saving.put(Currency.SGD, 0.0);
        saving.put(currency, saving.get(currency)+balance);
        Account newclient = new Account(accountNumber,accountName,password,saving);		//create a new account.
        accountList.add(newclient);														//add new account to account list.
        replyMessage="New account for "+newclient.getAccountName()+" has been created, Acoount number is: "+newclient.getAccountNum();
        serviceReply(1,replyMessage,ip,port,replyPacketList);			
        monitoruser(accountName+" has create a new account.",replyPacketList);
        //return replyMessage;
    }

    /**
     * This method use for client to closing their account.
     * @param accountNum	client's account number 	
     * @param accountName	name of the account holder.
     * @param password		Account password
     * @param ip    		IP address of current client
     * @param port    		Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out.
     * @throws Exception
     */
    public void closingUserAccount(int accountNum, String accountName, String password, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("Closing Account\nname %s, passwd: %s, acctNUM: %s,",accountName,password,accountNum));

        int i;
        //Account currentAccount= new Account();

        String replyMessage= "Your account has been close successfully";

//        i = clientVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentclient;
        // if condition will check client data again in case client calling service before it pass user verification service.
        if(i!=-1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {
            accountList.remove(i);			//remove client current account from account list.
            serviceReply(1,replyMessage,ip,port,replyPacketList);
            monitoruser(accountName+"has closing his account",replyPacketList);
        }
        else {
        	serviceReply(0,"Something wrong with client data",ip,port,replyPacketList);
        }


    }

    /**
     * This Method use for client to deposit money to their account.
     * @param accountNum	client's account number	client's account number
     * @param accountName	name of the account holder.
     * @param password		Account password.
     * @param currency		currency type client want to deposit.
     * @param amount		amount of money client want to deposit.
     * @param ip    		IP address of current client
     * @param port    		Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out.
     * @throws Exception
     */
    public void depositToAccount(int accountNum, String accountName, String password, Currency currency, double amount, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("deposite\nname %s, passwd: %s, acctNUM: %s,, CurrencyType: %s, balance: %f",accountName,password,accountNum,currency.toString(),amount));

        int i; double balance;
        //Account currentAccount= new Account();

        String replyMessage="";

//        i = clientVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentclient;
        if(i!=-1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {
            balance = (double) accountList.get(i).getSaving().get(currency)+amount;
            accountList.get(i).getSaving().put(currency, balance);
            replyMessage="Your new balance for "+currency.toString()+" is : "+balance;
            serviceReply(1,replyMessage,ip,port,replyPacketList);
            monitoruser(accountName+" deposit some money to account.",replyPacketList);
        }
        else {
        	serviceReply(0,"Something wrong with client data",ip,port,replyPacketList);
        }


    }

    /**
     * This method for client to withdraw money from their account.
     * @param accountNum	client's account number
     * @param accountName	name of the account holder.
     * @param password		Account password
     * @param currency		Currency type of the withdraw money 
     * @param amount		amount of money want to withdraw
     * @param ip    		IP address of current client
     * @param port    		Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out.
     * @throws Exception
     */
    public void withdrawFromAccount(int accountNum, String accountName, String password, Currency currency, double amount, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("withdraw\nname %s, passwd: %s, acctNUM: %s,, CurrencyType: %s, balance: %f",accountName,password,accountNum,currency.toString(),amount));

        int i; double balance;
        //Account currentAccount= new Account();

        String replyMessage;
//        i = clientVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentclient;
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
                monitoruser(accountName+" withdraw some money from account.",replyPacketList);
            }

        }
        else {
        	serviceReply(0,"Something wrong with client data",ip,port,replyPacketList);
        }

    }

    /**
     * @param accountNum	client's account number
     * @param accountName	name of the account holder.
     * @param password		Account password
     * @param ip    		IP address of current client
     * @param port    		Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out.
     * @throws Exception
     */
    public void viewBalance(int accountNum, String accountName, String password, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("view balance\nname %s, passwd: %s, acctNUM: %s,",accountName,password,accountNum));

        int i;
        String balanceInfo;
        String replyMessage;
//        i = clientVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentclient;
        if(i != -1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {
            balanceInfo= accountList.get(i).getSaving().toString().substring(1,accountList.get(i).getSaving().toString().length()-1);
            replyMessage = "Your balance under account : "+accountNum+" is \n"+balanceInfo;
            serviceReply(1,replyMessage,ip,port,replyPacketList);
            monitoruser(accountName+" check his account.",replyPacketList);
        }
        else {
        	serviceReply(0,"Something wrong with client data",ip,port,replyPacketList);
        }

    }

    /**
     * 
     * @param accountNum	client's account number
     * @param accountName	name of the account holder.
     * @param password		Account password
     * @param fromCurrency	currency want to exchange from
     * @param toCurrency	currency 
     * @param amount
     * @param ip    IP address of current client
     * @param port    Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out.
     * @throws Exception
     */
    public void currencyExchange(int accountNum, String accountName, String password, Currency fromCurrency, Currency toCurrency, double amount, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList ) throws Exception {
        System.out.println(String.format("currency exchange\nname %s, passwd: %s, acctNUM: %s,, FromCurrencyType: %s, toCurrencyType: %s,balance: %f",accountName,password,accountNum,fromCurrency.toString(),toCurrency.toString(),amount));

        int i;
        double newAmount;
        String balanceInfo;
        String replyMessage;
//        i = clientVerification(accountNum,accountName,password, ip, port,replyPacketList);
        i = currentclient;
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
            monitoruser(accountName+" has exchange currency from "+fromCurrency.toString()+" to "+toCurrency.toString(),replyPacketList);

        }
        else {
        	replyMessage ="Sorry, you don't have enough "+fromCurrency+" to convert to "+toCurrency;
        	serviceReply(0,replyMessage,ip,port,replyPacketList);
        }
        }
        else {
        	serviceReply(0,"Something wrong with client data",ip,port,replyPacketList);
        }
        

    }

    /**
     * This method use for client register for  monitor update.
     * @param accountNum	client's account number
     * @param accountName	name of the account holder.
     * @param password	Account password
     * @param interval		time period of client monitor update
     * @param ip    IP address of current client
     * @param port    Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out.
     * @throws Exception
     */
    public void registerMonitorUpdate(int accountNum, String accountName, String password, int interval, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
    	System.out.println(String.format("acc num %d , acct name %s, passwd: %s acc list size: %d, ",accountNum,accountName,password,accountList.size()));
    	int i,j;
    	String replyMessage;
    	LocalTime time = LocalTime.now().plusSeconds(interval);
    	MonitorInfo monitorAccount= new MonitorInfo(accountNum,time,ip,port);
    	i= currentclient;
    	if(i!=-1 && accountList.get(i).getAccountNum()==accountNum && accountList.get(i).getAccountName().equals(accountName) && accountList.get(i).getPasswd().equals(password)) {

    		monitorList.add(monitorAccount);
    		
	    	replyMessage= "You have success register for monitor update";
	    	serviceReply(1,replyMessage,ip,port,replyPacketList);
	    	monitoruser(accountName+"has register for monitor update.",replyPacketList);
    	}
        else {
        	serviceReply(0,"Something wrong with client data",ip,port,replyPacketList);
        }
    	
    	
    	
    
    }
    
    /**
     * This method use to verify client information 
     * @param accountNum	client's account number
     * @param accountName	name of the account holder.
     * @param password	Account password
     * @param ip    IP address of current client
     * @param port    Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out.
     * @return 	return index in account list for current client, if client verification fail return -1.
     * @throws Exception
     */
    public int userVerification(int accountNum, String accountName, String password,InetAddress ip,int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
        System.out.println(String.format("acc num %d create acct name %s, passwd: %s acc list size: %d",accountNum,accountName,password,accountList.size()));

        int i;
        String wrongAccountNum = "Sorry, you have enter a invalid account number";
        String wrongAccountName = "Sorry, you have enter a wrong account name";
        String wrongPassword= "Sorry, you have enter the wrong password";
        String clientPassed = "client verification success";

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
                    currentclient = i;
                    serviceReply(1,clientPassed,ip,port,replyPacketList);
                    return i;
                }
            }
        }
        serviceReply(0,wrongAccountNum,ip,port,replyPacketList);
        return -1;

    }
    
    
    /**
     * This method use for prepare reply packet to reply client after receive a service call.
     * @param status	message type, if service call success status will be 1 else will be 0.
     * @param message    Reply message that client will receive after calling a service.
     * @param ip    IP address of current client
     * @param port    Port number that current client used 
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out. 
     * @throws Exception
     */
    public void serviceReply(int status,String message, InetAddress ip, int port, ArrayList<DatagramPacket> replyPacketList) throws Exception {
    	
    	
    	System.out.println(message);
    	
        byte[] replybuf=DataProcess.marshal(1,status,message.length(),message);	//marshal message
    	
    	DatagramPacket reply = new DatagramPacket(replybuf,replybuf.length,ip,port);	//add message into datagram packet
    	
		replyPacketList.add(reply);			//add datagram packet in to replay packet List.
		
    }

    /**
     * This method will prepare data packet to send update to user who register for monitor update.
     * @param message	message use to send to other client who register for Monitor.
     * @param replyPacketList	DatagramPacket list use to store DatagramPacket that need to send out.
     * @throws Exception
     */
    public void monitoruser(String message, ArrayList<DatagramPacket> replyPacketList)  throws Exception{
        System.out.println(message);
//        System.out.println("monitor list size: "+monitorList.size());
//        for(int k=0;k<monitorList.size();k++) {			
//			System.out.println("monitor List:");
//			System.out.print(monitorList.get(k).getAccountNum());
//			System.out.print("   "+monitorList.get(k).getExpireTime());
//			System.out.println("     "+monitorList.get(k).getIP());
//		}
        LocalTime time = LocalTime.now();

        byte[] updatebuf=DataProcess.marshal(1,1,message.length(),message);	//marshal message
       
        if(!monitorList.isEmpty()) {
        	for(int i=0; i<monitorList.size();i++) {	
        		if(monitorList.get(i).getExpireTime().compareTo(time)>=0) {	//loop through the monitor list to find client that still monitor update
        			DatagramPacket update = new DatagramPacket(updatebuf,updatebuf.length);  //add message into datagram packet
        			//System.out.println("packet for account: "+monitorList.get(i).getAccountNum()+"has add to packet list. IP: "+monitorList.get(i).getIP());
        			update.setAddress(monitorList.get(i).getIP());	
        			update.setPort(monitorList.get(i).getPort());
        			//System.out.println("IP for update packet:"+update.getAddress());
        			
        			replyPacketList.add(update);	//add datagram packet in to replay packet List.
        			//System.out.println("reply List size: "+replyPacketList.size());
        		}
        		else {
        			monitorList.remove(i);	//If client monitor interval has finish, remove from monitor list.
        			i--;
        		}
        	}
        }
        
    }



    /**
     * @return
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNum	client's account number
     */
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return
     */
    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    /**
     * @param accountList
     */
    public void setAccountList(ArrayList<Account> accountList) {
        this.accountList = accountList;
    }
}
