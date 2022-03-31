import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataProcess {

    public static int length=4;

    /**
     * This method convert integer to byte array
     * @param data in integer type
     * @param byteOrder in big endian or little endian
     * @return byte array for int type
     */
    public static byte[] intToBytes(int data,ByteOrder byteOrder) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.putInt(data);
        return  byteBuffer.array();
    }

    /**
     * This method convert byte array to integer type
     * @param buf array of bytes
     * @param start starting index of integer in the buffer
     * @param byteOrder in big endian or little endian
     * @return integer
     */
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

    /**
     * This method convert double to byte array
     * @param data in double type
     * @param byteOrder in big endian or little endian
     * @return  byte array for double type
     */

    public static byte[] doubleToBytes(double data,ByteOrder byteOrder){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.putDouble(data);
        return  byteBuffer.array();
    }

    /**
     * This method convert byte array to double type
     * @param buf array of bytes
     * @param start starting index of integer in the buffer
     * @param byteOrder in big endian or little endian
     * @return double type
     */
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
    /**
     * This method convert string to byte array in UTF-8 format
     * @param str String data
     * @return byte array for string
     */
    public static byte[] stringToBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * This method convert bytes to string
     * @param buf array of bytes
     * @param start starting index of string in the buffer
     * @param numberOfByteToRead number of characters to read
     * @return string data
     */
    public static String bytesToString(byte[] buf,int start,int numberOfByteToRead) {
        byte[] bytes = new byte[numberOfByteToRead];
        for(int i = 0; i < numberOfByteToRead; i++) {
            bytes[i] = buf[start+i];
        }
        return new String(bytes, StandardCharsets.UTF_8);

    }

    // remove msg type and request id which is 8 byte + method id(4 byte) so start from 12 byte
    // passwd(4 byte, 4 char),currency type(int, 4 byte), init_amount(double), name_length(int 4 byte), name(string variable)
    /**
     * This method unmarshal the create account service
     * @param buf array of bytes
     * @param startByte starting index of the content in the message, here use 12th bytes
     * @return hashmap contain parameter name as key with corresponding value
     * @throws Exception
     */
    public static HashMap<String,Object> unmarshalCreateAccount(byte[] buf,int startByte) throws Exception{
        HashMap<String,Object> hashMap=new HashMap<>();
        int passLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("password",bytesToString(buf,startByte,length));
        startByte+=length;

        hashMap.put("currencyType",Currency.values()[bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN)]);
        startByte+=length;

        hashMap.put("amt",bytesToDouble(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length*2;

        int nameLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;




        hashMap.put("name",bytesToString(buf,startByte,nameLength));

        return hashMap;
    }

    //passwd(4 byte, 4 char), acct number(int), name(string variable)
    /**
     * This method unmarshal the close account service
     * @param buf array of bytes
     * @param startByte starting index of the content in the message, here use 12th bytes
     * @return hashmap contain parameter name as key with corresponding value
     * @throws Exception
     */
    public static HashMap<String,Object> unmarshalCloseAccount(byte[] buf,int startByte) throws Exception{
        HashMap<String,Object> hashMap=new HashMap<>();
        int passLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("password",bytesToString(buf,startByte,length));
        startByte+=length;

        hashMap.put("acctNum",bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length;

        int nameLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("name",bytesToString(buf,startByte,nameLength));

        return hashMap;
    }

    //passwd(4 byte, 4 char), acct number(int), currency type(int),amt(double),name(string variable)
    /**
     * This method unmarshal the deposit service
     * @param buf array of bytes
     * @param startByte starting index of the content in the message, here use 12th bytes
     * @return hashmap contain parameter name as key with corresponding value
     * @throws Exception
     */
    public static HashMap<String,Object> unmarshalDeposit(byte[] buf, int startByte) throws Exception{
        HashMap<String,Object> hashMap=new HashMap<>();
        int passLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("password",bytesToString(buf,startByte,length));
        startByte+=length;

        hashMap.put("acctNum",bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length;

        hashMap.put("currencyType",Currency.values()[bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN)]);
        startByte+=length;

        hashMap.put("amt",bytesToDouble(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length*2;

        int nameLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("name",bytesToString(buf,startByte,nameLength));

        return hashMap;
    }

    //passwd(4 byte, 4 char), acct number(int), currency type(int),amt(double),name(string variable)
    /**
     * This method unmarshal the withdraw service
     * @param buf array of bytes
     * @param startByte starting index of the content in the message, here use 12th bytes
     * @return hashmap contain parameter name as key with corresponding value
     * @throws Exception
     */
    public static HashMap<String,Object> unmarshalWithdraw(byte[] buf,int startByte) throws Exception{
        HashMap<String,Object> hashMap=new HashMap<>();
        int passLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("password",bytesToString(buf,startByte,length));
        startByte+=length;

        hashMap.put("acctNum",bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length;

        hashMap.put("currencyType",Currency.values()[bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN)]);
        startByte+=length;

        hashMap.put("amt",bytesToDouble(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length*2;

        int nameLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("name",bytesToString(buf,startByte,nameLength));

        return hashMap;
    }

    //passwd(4 byte, 4 char), acct number(int),name(string variable)
    /**
     * This method unmarshal the view balance service
     * @param buf array of bytes
     * @param startByte starting index of the content in the message, here use 12th bytes
     * @return hashmap contain parameter name as key with corresponding value
     * @throws Exception
     */
    public static HashMap<String,Object> unmarshalViewBalance(byte[] buf,int startByte) throws Exception{
        HashMap<String,Object> hashMap=new HashMap<>();
        int passLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("password",bytesToString(buf,startByte,length));
        startByte+=length;

        hashMap.put("acctNum",bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length;

        int nameLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("name",bytesToString(buf,startByte,nameLength));

        return hashMap;
    }

    //passwd(4 byte, 4 char), acct number(int), currency from type(int),currency to type(int), amount(double),amt(double),name(string variable)
    /**
     * This method unmarshal the currency exchange service
     * @param buf array of bytes
     * @param startByte starting index of the content in the message, here use 12th bytes
     * @return hashmap contain parameter name as key with corresponding value
     * @throws Exception
     */
    public static HashMap<String,Object> unmarshalCurrencyExchange(byte[] buf,int startByte) throws Exception{
        HashMap<String,Object> hashMap=new HashMap<>();
        int passLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("password",bytesToString(buf,startByte,length));
        startByte+=length;

        hashMap.put("acctNum",bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length;

        hashMap.put("currencyFromType",Currency.values()[bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN)]);
        startByte+=length;
        hashMap.put("currencyToType",Currency.values()[bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN)]);
        startByte+=length;

        hashMap.put("amt",bytesToDouble(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length*2;

        int nameLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("name",bytesToString(buf,startByte,nameLength));
        return hashMap;
    }

    // interval in seconds
    /**
     * This method unmarshal the monitor service
     * @param buf array of bytes
     * @param startByte starting index of the content in the message, here use 12th bytes
     * @return hashmap contain parameter name as key with corresponding value
     * @throws Exception
     */
    public static HashMap<String,Object> unmarshalMonitor(byte[] buf,int startByte) throws Exception{
        HashMap<String,Object> hashMap=new HashMap<>();
        int passLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("password",bytesToString(buf,startByte,length));
        startByte+=length;

        hashMap.put("acctNum",bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length;

        hashMap.put("intervalTime",bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length;

        int nameLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("name",bytesToString(buf,startByte,nameLength));
        startByte+=nameLength;

        return hashMap;
    }

    // password, acctNum,name
    /**
     * This method unmarshal the user verification service
     * @param buf array of bytes
     * @param startByte starting index of the content in the message, here use 12th bytes
     * @return hashmap contain parameter name as key with corresponding value
     * @throws Exception
     */
    public static HashMap<String,Object> unmarshalUserVerification(byte[] buf,int startByte) throws Exception{
        HashMap<String,Object> hashMap=new HashMap<>();
        int passLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("password",bytesToString(buf,startByte,length));
        startByte+=length;

        hashMap.put("acctNum",bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN));
        startByte+=length;

        int nameLength=bytesToInt(buf,startByte,ByteOrder.BIG_ENDIAN);
        startByte+=length;

        hashMap.put("name",bytesToString(buf,startByte,nameLength));

        return hashMap;
    }


    /**
     * This method marshal the data to byte array
     * @param obj variable length param for integer, double and string type
     * @return byte array
     */
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
