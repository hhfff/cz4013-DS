import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class DataProcess {
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
    public static int bytesToInt(byte[] data,ByteOrder byteOrder) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.put(data);
        byteBuffer.flip();
        return byteBuffer.getInt();
    }
    public static byte[] doubleToBytes(double data,ByteOrder byteOrder){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.putDouble(data);
        return  byteBuffer.array();
    }

    public static double bytesToDouble(byte[] doubleBytes,ByteOrder byteOrder){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES);
        byteBuffer.order(byteOrder);
        byteBuffer.put(doubleBytes);
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
    public static String bytesToString(byte[] data,int start,int end) {
        byte[] bytes = new byte[end - start];
        for(int i = start; i < end; i++) {
            bytes[i-start] = (data[i]);
        }
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







}
