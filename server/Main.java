import java.nio.ByteOrder;
import java.util.Arrays;



public class Main {
    public static void main(String[] args) {
        System.out.println("========start server========");
        Server server=new Server();
        server.start(args);

    }

}