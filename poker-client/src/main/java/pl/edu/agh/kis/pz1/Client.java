package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Represents a poker client.
 */
public class Client {
    private static SocketChannel client;

    /**
     * Connects to the server.
     * @param port server port
     * @throws IOException if an I/O error occurs
     */
    public static void start(int port) throws IOException {
        client = SocketChannel.open(new InetSocketAddress("localhost", port));
        // client.configureBlocking(false);
    }

    /**
     * Sends a message to the server.
     * @param message message to send
     * @throws IOException if an I/O error occurs
     */
    public static void send(String message) throws IOException {
        var buffer = ByteBuffer.wrap(message.getBytes());
        client.write(buffer);
    }

    /**
     * Receives a message from the server.
     * @return received message
     * @throws IOException if an I/O error occurs
     */
    public static String get() throws IOException {
        var buffer = ByteBuffer.allocate(256);
        client.read(buffer);
        return new String(buffer.array()).trim();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        var port = Integer.parseInt(args[0]);
        start(port);

        // GET ID
        String idString = get();
        int id = Utilities.getArgument(idString);

        // GET BEGIN
        String beginString = get();

        // GET ANTE
        String anteString = get();
        int ante = Utilities.getArgument(anteString);
        System.out.println("Ante: " + ante);

        // GET MAX
        String startMoneyString = get();
        int startMoney = Utilities.getArgument(startMoneyString);
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
        int dealer = Utilities.getArgument(dealerString);
        System.out.println("Dealer: " + dealer);

        String incoming = "";
        while (!incoming.contains("FINISHED 1")) {
            incoming = get();

            if (incoming.contains("TURN")) {
                var whoseTurn = Utilities.getArgument(incoming);

                if (whoseTurn == id) {
                    System.out.println("YOUR TURN");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String move = reader.readLine();

                    send("BET " + move);
                }
            }

            if (incoming.contains("BET")) {
                var betMaker = Utilities.getArgument(incoming);
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
                var whoseTurn = Utilities.getArgument(incoming);

                if (whoseTurn == id) {
                    System.out.println("YOUR TURN");

                    reader = new BufferedReader(new InputStreamReader(System.in));
                    String move = reader.readLine();

                    send("BET " + move);
                }
            }

            if (incoming.contains("BET")) {
                var betMaker = Utilities.getArgument(incoming);
                var betType = incoming.split(" ")[2];

                System.out.println("Player " + betMaker + " " + betType);
            }
        }

        // GET WINNER
        String winnerString = get();
        int winner = Utilities.getArgument(winnerString);
        System.out.println("Winner: " + winner);

        // GET MONEY
        String moneyString = get();
        int money = Utilities.getArgument(moneyString);
        System.out.println("Money: " + money);

        // GET END
        String endString = get();

    }

}