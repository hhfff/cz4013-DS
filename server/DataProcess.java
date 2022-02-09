import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataProcess {
    public static int length=4;
    /*public static byte[] intToBytes(final int data) {
        return new byte[]{
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) ((data >> 0) & 0xff),
        };
    }
    public static int bytesToInt(byte[] data) {
        if (data == null || data.length != 4) return 0x0;
        return (int)(
                (0xff & data[0]) << 24  |
                        (0xff & data[1]) << 16  |
                        (0xff & data[2]) << 8   |
                        (0xff & data[3]) << 0
        );
    }*/

    public static byte[] intToBytes(int data,ByteOrder byteOrder) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.putInt(data);
        return  byteBuffer.array();
    }
    public static int bytesToInt(byte[] buf,int start,ByteOrder byteOrder) {
        byte[] bytes = new byte[length];
        for(int i = 0; i < length; i++) {
            bytes[i] = buf[start+i];
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        return byteBuffer.getInt();
    }
    public static byte[] doubleToBytes(double data,ByteOrder byteOrder){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.putDouble(data);
        return  byteBuffer.array();
    }

    public static double bytesToDouble(byte[] buf,int start,ByteOrder byteOrder){
        byte[] bytes = new byte[length*2];
        for(int i = 0; i < length*2; i++) {
            bytes[i] = buf[start+i];
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        return byteBuffer.getDouble();
    }



    //string no need consider byte order
    public static byte[] stringToBytes(String str) {
        /*

        US-ASCII: Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
        ISO-8859-1: ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
        UTF-8: Eight-bit UCS Transformation Format
        UTF-16BE: Sixteen-bit UCS Transformation Format, big-endian byte order
        UTF-16LE: Sixteen-bit UCS Transformation Format, little-endian byte order
        UTF-16: Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark.
         */
        /*if(byteOrder==ByteOrder.LITTLE_ENDIAN){
            try {
                return str.getBytes("UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }else if(byteOrder==ByteOrder.BIG_ENDIAN){
            try {
                return str.getBytes("UTF-16BE");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }*/
        return str.getBytes(StandardCharsets.UTF_8);
    }
    public static String bytesToString(byte[] buf,int start,int numberOfByteToRead) {
        byte[] bytes = new byte[numberOfByteToRead];
        for(int i = 0; i < numberOfByteToRead; i++) {
            bytes[i] = buf[start+i];
        }
        printByteToHex(bytes);
        return new String(bytes, StandardCharsets.UTF_8);

    }

    /*public static byte[] doubletoBytes(double dblValue) {
        long data = Double.doubleToRawLongBits(dblValue);
        return new byte[]{
                (byte) ((data >> 56) & 0xff),
                (byte) ((data >> 48) & 0xff),
                (byte) ((data >> 40) & 0xff),
                (byte) ((data >> 32) & 0xff),
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) ((data >> 0) & 0xff),
        };
    }*/



    public static void printByteToHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb.toString());
    }

    // remove msg type and request id which is 8 byte + method id(4 byte) so start from 12 byte
    // passwd(4 byte, 4 char),currency type(int, 4 byte), init_amount(double), name_length(int 4 byte), name(string variable)
    public static void unmarshalCreateAccount(byte[] buf){
        int ptr=12;

        String passwd=bytesToString(buf,ptr,length);
        ptr+=length;
        System.out.println(passwd);

        int currencyType=bytesToInt(buf,ptr,ByteOrder.BIG_ENDIAN);
        ptr+=length;
        System.out.println(currencyType);


        double initAmount=bytesToDouble(buf,ptr,ByteOrder.BIG_ENDIAN);
        ptr+=length*2;
        System.out.println(initAmount);


        int nameLength=bytesToInt(buf,ptr,ByteOrder.BIG_ENDIAN);
        ptr+=length;
        System.out.println(nameLength);


        String name=bytesToString(buf,ptr,nameLength);
        System.out.println(name);



    }
    public static void unmarshalCreateAccount(){

    }

    public static byte[] marshal(Object ...obj){
        List<Byte> bytesList=new ArrayList<>();
        for(Object o :obj){
            if(o instanceof Integer){
                byte[] bytes=intToBytes((Integer)o,ByteOrder.BIG_ENDIAN);
                for(byte b :bytes) bytesList.add(b);
            }else if(o instanceof Double){
                byte[] bytes=doubleToBytes((Double)o,ByteOrder.BIG_ENDIAN);
                for(byte b :bytes) bytesList.add(b);
            }else  if(o instanceof  String){
                byte[] bytes=stringToBytes((String)o);
                for(byte b :bytes) bytesList.add(b);
            }else{
                throw new IllegalArgumentException();
            }
        }
        byte[] bytes=new byte[bytesList.size()];
        for(int i=0;i<bytesList.size();i++) bytes[i]= bytesList.get(i);
        return bytes;
    }







}
