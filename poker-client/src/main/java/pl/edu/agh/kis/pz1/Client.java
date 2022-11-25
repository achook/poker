package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.io.Serial;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class Client {
    private static SocketChannel client;
    private static ByteBuffer buffer;

    public static void start() throws IOException {
        client = SocketChannel.open(new InetSocketAddress("localhost", 8090));
        buffer = ByteBuffer.allocate(256);
    }

    public static String sendMessage(String msg) throws IOException {
        buffer = ByteBuffer.wrap(msg.getBytes());

        client.write(buffer);
        buffer.clear();

        client.read(buffer);
        String response = new String(buffer.array()).trim();

        buffer.clear();

        return response;
    }

    public static void main(String[] args) throws InterruptedException {
        start();

        for (int i = 0; i < 10; i++) {
            sendMessage(i+"");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}