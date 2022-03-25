import java.nio.ByteOrder;
import java.util.Arrays;



public class Main {
    public static void main(String[] args) {
       /* System.out.print("========int========");
        testInt();
        System.out.println("========double========");
        testDouble();
        System.out.println("========start server========");
        testStr();
        System.out.println("========start server========");*/


        System.out.println("========start server========");
        /*Server server=new Server();
        server.createUserAccount("Tom","password",Server.Currency.CNY,500.00);
        server.depositToAccount(1, "Tom", "password", Server.Currency.SGD, 1000.13);
        server.WithdrawFromAccount(1, "Tom", "password", Server.Currency.SGD, 200.0);
        server.currencyExchange(1, "Tom", "password", Server.Currency.SGD,Server.Currency.MYR ,200.0);
        server.createUserAccount("David","password",Server.Currency.CNY,500.00);
        
        // server.accountList.add(newUser);
        // server.accountList.size();
         System.out.println(server.accountList.size());
         server.closingUserAccount(2,"David" ,"password" );
         System.out.println(server.accountList.size());
          System.out.println(server.accountList.get(0).accountName);
          System.out.println(server.accountList.get(0).accountNum);
          System.out.println(server.accountList.get(0).passwd);
          System.out.println(server.accountList.get(0).saving);*/

        Server server=new Server();
        server.start();

    }



}