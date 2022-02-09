import java.nio.ByteOrder;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.print("========int========");
        testInt();
        System.out.println("========double========");
        testDouble();
        System.out.println("========start server========");
        testStr();
        System.out.println("========start server========");

        int methodName=1;
        String pass="pass";
        int currType=1;
        double money=3.0;
        String name="haskelliii";

        byte[] bytes=DataProcess.marshal(pass,currType,money,name.length(),name);

        byte[] buffer=new byte[128];
        if(bytes.length<=buffer.length-12){
            for(int i=12;i<bytes.length+12;i++){
                buffer[i]=bytes[i-12];
            }
        }else{
            System.out.println("out of buffer bound");
        }
        DataProcess.unmarshalCreateAccount(buffer);

        //Server server=new Server();
        //server.start();

    }

   private static void testInt(){
        System.out.println("little endian");
        byte[] intbyte=DataProcess.intToBytes(1000, ByteOrder.LITTLE_ENDIAN);
        DataProcess.printByteToHex(intbyte);
        int i= DataProcess.bytesToInt(intbyte,0,ByteOrder.LITTLE_ENDIAN);
        System.out.println(i);

        System.out.println("big endian");
        intbyte=DataProcess.intToBytes(1000, ByteOrder.BIG_ENDIAN);
        DataProcess.printByteToHex(intbyte);
        i= DataProcess.bytesToInt(intbyte,0,ByteOrder.BIG_ENDIAN);
        System.out.println(i);
    }
    private static void testDouble(){
        System.out.println("little endian");
        byte[] intbyte=DataProcess.doubleToBytes(100.5,ByteOrder.LITTLE_ENDIAN);
        DataProcess.printByteToHex(intbyte);
        double i= DataProcess.bytesToDouble(intbyte,0,ByteOrder.LITTLE_ENDIAN);
        System.out.println(i);

        System.out.println("big endian");
        intbyte=DataProcess.doubleToBytes(100.5, ByteOrder.BIG_ENDIAN);
        DataProcess.printByteToHex(intbyte);
        i= DataProcess.bytesToDouble(intbyte,0,ByteOrder.BIG_ENDIAN);
        System.out.println(i);
    }
    private static void testStr(){
        byte[] intbyte=DataProcess.stringToBytes("ABCD");
        //DataProcess.printByteToHex(intbyte);
        String i= DataProcess.bytesToString(intbyte,0,intbyte.length);
        System.out.println(i);

    }

}