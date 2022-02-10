import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
		
	public static void main(String[] args) {
        
    	
    	System.out.print("========int========");
        testInt();
        System.out.println("========double========");
        testDouble();
        System.out.println("========start server========");
        testStr();

        System.out.println("========start server========");

        Server server=new Server();
       // server.start();
        
        
      server.createUserAccount("Tom","password",Server.Currency.CNY,500.00);
      // server.accountList.add(newUser);
      // server.accountList.size();
       System.out.println(server.accountList.size());
        System.out.println(server.accountList.get(0).accountName);
        System.out.println(server.accountList.get(0).accountNum);
        System.out.println(server.accountList.get(0).passwd);
        System.out.println(server.accountList.get(0).saving);
    }

	
   private static void testInt(){
        System.out.println("little endian");
        byte[] intbyte=DataProcess.intToBytes(1000, ByteOrder.LITTLE_ENDIAN);
        DataProcess.printByteToHex(intbyte);
        int i= DataProcess.bytesToInt(intbyte,ByteOrder.LITTLE_ENDIAN);
        System.out.println(i);

        System.out.println("big endian");
        intbyte=DataProcess.intToBytes(1000, ByteOrder.BIG_ENDIAN);
        DataProcess.printByteToHex(intbyte);
        i= DataProcess.bytesToInt(intbyte,ByteOrder.BIG_ENDIAN);
        System.out.println(i);
    }
    private static void testDouble(){
        System.out.println("little endian");
        byte[] intbyte=DataProcess.doubleToBytes(100.5,ByteOrder.LITTLE_ENDIAN);
        DataProcess.printByteToHex(intbyte);
        double i= DataProcess.bytesToDouble(intbyte,ByteOrder.LITTLE_ENDIAN);
        System.out.println(i);

        System.out.println("big endian");
        intbyte=DataProcess.doubleToBytes(100.5, ByteOrder.BIG_ENDIAN);
        DataProcess.printByteToHex(intbyte);
        i= DataProcess.bytesToDouble(intbyte,ByteOrder.BIG_ENDIAN);
        System.out.println(i);
    }
    private static void testStr(){
        byte[] intbyte=DataProcess.stringToBytes("ABCD");
        DataProcess.printByteToHex(intbyte);
        String i= DataProcess.bytesToString(intbyte,0,intbyte.length);
        System.out.println(i);

    }
    

}