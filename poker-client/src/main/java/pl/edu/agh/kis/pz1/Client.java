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
        buffer = ByteBuffer.allocate(256);
    }

    public static String sendAndGet(String msg) throws IOException {
        var buffer = ByteBuffer.wrap(msg.getBytes());
        client.write(buffer);

        var buffer2 = ByteBuffer.allocate(256);
        client.read(buffer2);
        String response = new String(buffer2.array()).trim();

        return response;
    }

    public static void send(String msg) throws IOException {
        var buffer = ByteBuffer.wrap(msg.getBytes());
        client.write(buffer);
    }

    public static String get() throws IOException {
        var buffer2 = ByteBuffer.allocate(256);
        client.read(buffer2);
        return new String(buffer2.array()).trim();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        start();

        // GET ID
        String id = sendAndGet("BEGIN");

        System.out.println(id);

        // GET MAX
        String start = get();

        System.out.println(start);

        // GET 5 CARDS
        for (int i = 0; i < 5; i++) {
            String card = get();
            System.out.println(card);
        }

        while (true) {}
    }
}