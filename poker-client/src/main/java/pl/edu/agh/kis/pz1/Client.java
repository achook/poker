package pl.edu.agh.kis.pz1;

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
    static SocketChannel socketClient;
    private static final ClientLogger logger = new ClientLogger();

    /**
     * Connects to the server.
     * @param port server port
     * @throws IOException if an I/O error occurs
     */
    public static void start(int port) throws IOException {
        socketClient = SocketChannel.open(new InetSocketAddress("localhost", port));
    }

    /**
     * Sends a message to the server.
     * @param message message to send
     * @throws IOException if an I/O error occurs
     */
    public static void send(String message) throws IOException {
        var buffer = ByteBuffer.wrap(message.getBytes());
        socketClient.write(buffer);
    }

    /**
     * Receives a message from the server.
     * @return received message
     * @throws IOException if an I/O error occurs
     */
    public static String get() throws IOException {
        var buffer = ByteBuffer.allocate(256);
        socketClient.read(buffer);
        return new String(buffer.array()).trim();
    }

    public static void main(String[] args) throws IOException {
        var port = Integer.parseInt(args[0]);
        playGame(port);

    }

    private static void playGame(int port) throws IOException {
        start(port);

        // GET ID
        String idString = get();
        int clientID = Utilities.getArgument(idString);
        logger.displayID(clientID);

        // GET ANTE
        String anteString = get();
        int ante = Utilities.getArgument(anteString);
        logger.displayAnte(ante);

        // GET MAX
        String startMoneyString = get();
        int startMoney = Utilities.getArgument(startMoneyString);
        logger.displayMoney(startMoney);

        while (true) {

            var h = new Hand();

            // GET 5 CARDS
            for (int i = 0; i < 5; i++) {
                String cartString = get();
                var c = Card.fromSCP(cartString.split(" ")[1]);
                h.addCard(c);
            }

            logger.displayHand(h);

            // GET DEALER
            String dealerString = get();
            int dealerID = Utilities.getArgument(dealerString);
            logger.announceDealer(dealerID);

            String incoming = "";
            while (!incoming.contains("FINISHED 1")) {
                incoming = get();

                if (incoming.contains("TURN")) {
                    var whoseTurn = Utilities.getArgument(incoming);

                    if (whoseTurn == clientID) {
                        logger.announceTurn();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        String move = reader.readLine();

                        send("BET " + move);
                    }
                }

                if (incoming.contains("BET")) {
                    logger.announceBet(incoming);
                }
            }

            // REPLACE CARDS
            logger.askForCardsToDiscard();

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

            logger.announceTurn();

            while (!incoming.contains("FINISHED 2")) {
                incoming = get();

                if (incoming.contains("TURN")) {
                    var whoseTurn = Utilities.getArgument(incoming);

                    if (whoseTurn == clientID) {
                        logger.announceTurn();

                        reader = new BufferedReader(new InputStreamReader(System.in));
                        String move = reader.readLine();

                        send("BET " + move);
                    }
                }

                if (incoming.contains("BET")) {
                    logger.announceBet(incoming);
                }
            }

            // GET WINNER
            String winnerString = get();
            int winner = Utilities.getArgument(winnerString);
            logger.announceWinner(winner);

            // GET MONEY
            String moneyString = get();
            int money = Utilities.getArgument(moneyString);
            logger.displayMoney(money);

            // GET END
            logger.askForContinue();
            reader = new BufferedReader(new InputStreamReader(System.in));
            String move = reader.readLine();

            if (move.equals("y")) {
                send("CONTINUE");
            } else {
                send("QUIT");
                break;
            }
        }
    }

}