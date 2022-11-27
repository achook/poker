package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.io.Serial;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Client {
    private static SocketChannel client;
    private static ByteBuffer buffer;

    public static void start() throws IOException {
        client = SocketChannel.open(new InetSocketAddress("localhost", 8090));
        // client.configureBlocking(false);
        buffer = ByteBuffer.allocate(256);
    }

    public static String sendAndGet(String msg) throws IOException {
        var buffer = ByteBuffer.wrap(msg.getBytes());
        client.write(buffer);
        buffer.clear();

        var buffer2 = ByteBuffer.allocate(256);
        client.read(buffer2);
        String response = new String(buffer2.array()).trim();
        buffer2.clear();

        return response;
    }

    public static void send(String msg) throws IOException {
        var buffer = ByteBuffer.wrap(msg.getBytes());
        client.write(buffer);
        buffer.clear();
    }

    public static String get() throws IOException {
        var buffer2 = ByteBuffer.allocate(256);
        client.read(buffer2);
        var s = new String(buffer2.array()).trim();
        buffer2.clear();
        return s;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        start();

        // GET ID
        String id = get();
        System.out.println("ID");
        System.out.println(id);

        // GET MAX
        String start = get();
        System.out.println("START");
        System.out.println(start);

        // GET 5 CARDS
        for (int i = 0; i < 5; i++) {
            String card = get();
            System.out.println("CARD");
            System.out.println(card);
        }



        // GET DEALER
        String dealer = get();
        System.out.println("DEALER");
        System.out.println(dealer);

        TimeUnit.SECONDS.sleep(2);

        send("FOLD");
        System.out.println("SENT");

        while (true) {}
    }

}