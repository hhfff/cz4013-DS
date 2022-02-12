import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
public class AccountService {
    private int accountNumber=0;
    private ArrayList<Account> accountList = new ArrayList<Account>();



    public AccountService(){}
    public void createUserAccount(String accountName, String password, Currency currency, double balance) {

        System.out.println(String.format("name %s, passwd: %s, CurrencyType: %s, balance: %f",accountName,password,currency.toString(),balance));
        accountNumber+=1;
        Map<Currency, Double> saving = new HashMap<Currency, Double>();
        saving.put(Currency.CNY, 0.0);
        saving.put(Currency.MYR, 0.0);
        saving.put(Currency.SGD, 0.0);
        saving.put(currency, saving.get(currency)+balance);
        Account newUser = new Account(accountNumber,accountName,password,saving);
        accountList.add(newUser);

        serviceReply("New account for "+newUser.getAccountName()+" has been created, Acoount number is: "+newUser.getAccountNum());
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
            balance = (double) accountList.get(i).getSaving().get(currency)+amount;
            accountList.get(i).getSaving().put(currency, balance);
            depositSuccess="Your new balance for "+currency.toString()+" is : "+balance;
            serviceReply(depositSuccess);
            updateUser(accountName+" deposit some money to account.");
        }



    }

    public void wthdrawFromAccount(int accountNum, String accountName, String password, Currency currency, double amount) {
        int i; double balance;
        //Account currentAccount= new Account();

        String balanceNotEnoug= "Sorry, you don't have enough balance to withdraw "+currency.toString()+" : "+amount+".";
        String depositSuccess="";
        i = userVerification(accountNum,accountName,password);
        if(i!=-1) {

            if((double) accountList.get(i).getSaving().get(currency)<amount) {
                serviceReply(balanceNotEnoug);
            }
            else {
                balance = (double) accountList.get(i).getSaving().get(currency)-amount;
                accountList.get(i).getSaving().put(currency, balance);
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
            balanceInfo= accountList.get(i).getSaving().toString();
            serviceReply("Your balance under account : "+accountNum+"is \n"+balanceInfo.substring(1,balanceInfo.length()-1));
            updateUser(accountName+" check his account.");
        }

    }

    public void currencyExchange(int accountNum, String accountName, String password, Currency fromCurrency, Currency toCurrency, double amount ) {
        int i;
        double newAmount;
        String balanceInfo;
        i = userVerification(accountNum,accountName,password);
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
            balanceInfo= accountList.get(i).getSaving().toString();
            serviceReply("Your new balance under account : "+accountNum+" is \n"+balanceInfo.substring(1,balanceInfo.length()-1));
            updateUser(accountName);

        }
        else {
            serviceReply("Sorry, you don't have enough "+fromCurrency+" to convert to"+toCurrency);
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
            if(accountList.get(i).getAccountNum()==accountNum) {
                //currentAccount=accountList.get(i);
                break;
            }
        }
        if(i==accountList.size()) {
            serviceReply(wrongAccountNum);
            return -1;
        }
        else if(accountList.get(i).getAccountName() != accountName) {
            serviceReply(wrongAccountName);
            return -1;
        }
        else if(accountList.get(i).getPasswd() != password) {
            serviceReply(wrongPassword);
            return -1;
        }
        else {
            return i;
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
