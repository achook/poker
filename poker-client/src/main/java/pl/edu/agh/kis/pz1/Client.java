package pl.edu.agh.kis.pz1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        String idString = get();
        int id = Integer.parseInt(idString.split(" ")[1]);

        // GET BEGIN
        String beginString = get();

        // GET ANTE
        String anteString = get();
        int ante = Integer.parseInt(anteString.split(" ")[1]);
        System.out.println("Ante: " + ante);

        // GET MAX
        String startMoneyString = get();
        int startMoney = Integer.parseInt(startMoneyString.split(" ")[1]);
        System.out.println("Start money: " + startMoney);

        var h = new Hand();

        // GET 5 CARDS
        for (int i = 0; i < 5; i++) {
            String cartString = get();
            var c = Card.fromSCP(cartString.split(" ")[1]);
            h.addCard(c);
        }

        System.out.println("Hand: " + h);

        // GET DEALER
        String dealerString = get();
        int dealer = Integer.parseInt(dealerString.split(" ")[1]);
        System.out.println("Dealer: " + dealer);

        String incoming = "";
        while (!incoming.contains("FINISHED 1")) {
            incoming = get();

            if (incoming.contains("TURN")) {
                var whoseTurn = Integer.parseInt(incoming.split(" ")[1]);

                if (whoseTurn == id) {
                    System.out.println("YOUR TURN");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String move = reader.readLine();

                    send("BET " + move);
                }
            }

            if (incoming.contains("BET")) {
                var betMaker = Integer.parseInt(incoming.split(" ")[1]);
                var betType = incoming.split(" ")[2];

                System.out.println("Player " + betMaker + " " + betType);
            }
        }

        // REPLACE CARDS
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String toReplace = reader.readLine();

        send("REPLACE " + toReplace);

        h = new Hand();

        // GET 5 CARDS
        for (int i = 0; i < 5; i++) {
            String cartString = get();
            var c = Card.fromSCP(cartString.split(" ")[1]);
            h.addCard(c);
        }

        System.out.println("Hand: " + h);

        while (!incoming.contains("FINISHED 2")) {
            incoming = get();

            if (incoming.contains("TURN")) {
                var whoseTurn = Integer.parseInt(incoming.split(" ")[1]);

                if (whoseTurn == id) {
                    System.out.println("YOUR TURN");

                    reader = new BufferedReader(new InputStreamReader(System.in));
                    String move = reader.readLine();

                    send("BET " + move);
                }
            }

            if (incoming.contains("BET")) {
                var betMaker = Integer.parseInt(incoming.split(" ")[1]);
                var betType = incoming.split(" ")[2];

                System.out.println("Player " + betMaker + " " + betType);
            }
        }

    }

}