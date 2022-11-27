package pl.edu.agh.kis.pz1;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import java.io.IOException;


/**
 * Represents the server of the poker game.
 */
public class Server {
    private static ServerSocketChannel serverSocketChannel;
    private static ServerSocket serverSocket;
    private static SelectionKey selectionKey;
    private static Selector selector;

    /**
     * Starts the server.
     * @param port server port
     * @throws IOException if an I/O error occurs
     */
    public static void startServer(int port) throws InternalServerException, IOException {
        var address = new InetSocketAddress(port);

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        selector = Selector.open();

        selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * Stops the server.
     * @throws InternalServerException if an error occurs
     */
    public static void stopServer() throws InternalServerException {
        try {
            serverSocket.close();
        } catch (Exception e) {
            throw new InternalServerException(e.toString());
        }
    }

    /**
     * Sends a message to the client.
     * @param message message to send
     * @param key client key
     * @throws IOException if an I/O error occurs
     */
    private static void send(String message, SelectionKey key) throws IOException {
        var buffer = ByteBuffer.allocate(256);
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();

        buffer.put(message.getBytes());
        buffer.flip();

        while (buffer.hasRemaining()) {
            client.write (buffer);
        }
    }

    /**
     * Sends a message to all clients.
     * @param message message to send
     * @param keys clients keys
     * @throws IOException if an I/O error occurs
     */
    private static void sendToAll(String message, Collection<SelectionKey> keys) throws IOException {
        for (SelectionKey key : keys) {
            send(message, key);
        }
    }

    /**
     * Receives a message from the client.
     * @param key client key
     * @return received message
     * @throws IOException if an I/O error occurs
     */
    private static String read(SelectionKey key) throws IOException {
        var buffer = ByteBuffer.allocate(256);
        SocketChannel client = (SocketChannel) key.channel();

        int numRead;
        while ((numRead = client.read(buffer)) > 0) {}

        if (numRead == -1) {
            // TODO: Handle user disconnect
        }

        return new String(buffer.array()).trim();
    }

    /**
     * Registers a socket channel with the selector.
     * @param selector selector
     * @param serverSocket server socket
     * @throws IOException if an I/O error occurs
     */
    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, client.validOps());
    }

    /**
     * Plays a single round.
     * @param players players
     * @param game game
     * @throws IOException if an I/O error occurs
     */
    private static void playARound(Map<Integer, SelectionKey> players, Game game) throws IOException {
        while (!game.isRoundFinished()) {
            // Check which players turn is it
            var currentPlayerID = game.getCurrentPlayerID();

            // Inform all the players
            for (var player : players.entrySet()) {
                send("TURN " + currentPlayerID, player.getValue());
            }

            // Wait for the current player to make a move
            String bet = "";
            while (!bet.contains("BET")) {
                bet = read(players.get(currentPlayerID));
            }

            var rawBetElements = bet.split(" ");
            var betType = rawBetElements[1];

            Game.Move move = null;
            int amount = -1;

            while (move == null) {
                if (betType.contains("FOLD")) {
                    move = Game.Move.FOLD;

                } else if (betType.contains("CALL")) {
                    move = Game.Move.CALL;

                } else if (betType.contains("RAISE")) {
                    move = Game.Move.RAISE;
                    amount = Integer.parseInt(rawBetElements[2]);

                } else if (betType.contains("CHECK")) {
                    move = Game.Move.CHECK;
                }

                if (!game.isMoveLegal(currentPlayerID, move) | move == null) {
                    send("ILLEGAL", players.get(currentPlayerID));
                }
            }

            game.makeMove(currentPlayerID, move, amount);

            // Send bet to other players
            for (var player : players.entrySet()) {
                if (player.getKey() != currentPlayerID) {
                    send("BET " + currentPlayerID + " " + betType, player.getValue());
                }
            }
        }
    }

    public static void main(String[] args) throws InternalServerException, IOException, InterruptedException {
        var port = Integer.parseInt(args[0]);
        var numberOfAllPlayers = Integer.parseInt(args[1]);

        startServer(port);

        // WAIT FOR PLAYERS
        var currentID = 0;
        Map<Integer, SelectionKey> players = new java.util.HashMap<>(Map.of());

        while (currentID < numberOfAllPlayers) {
            selector.select();

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isAcceptable()) {
                    register(selector, serverSocketChannel);
                }

                if (key.isWritable() && !players.containsValue(key)) {
                    send("ID " + currentID, key);
                    players.put(currentID, key);
                    currentID++;

                }
            }
        }
        TimeUnit.MILLISECONDS.sleep(100);

        var gameInProgress = true;
        while (gameInProgress) {

            for (var player : players.entrySet()) {
                send("BEGIN", player.getValue());
            }
            TimeUnit.MILLISECONDS.sleep(100);


            System.out.println(players);
            // GAME STARTS

            var startingMoney = 1000;
            var ante = 10;

            var game = new Game(numberOfAllPlayers, ante);

            // SEND ANTE
            for (var player : players.entrySet()) {
                send("ANTE " + ante, player.getValue());
            }
            TimeUnit.MILLISECONDS.sleep(100);

            // SEND STARTING MONEY
            for (var player : players.entrySet()) {
                send("MONEY " + startingMoney, player.getValue());
            }
            TimeUnit.MILLISECONDS.sleep(100);

            // SEND CARDS
            for (var player : players.entrySet()) {
                var cards = game.getHand(player.getKey()).getCards();

                for (var card : cards) {
                    send("CARD " + card.toSCP() + "\n", player.getValue());
                    TimeUnit.MILLISECONDS.sleep(50);
                }
            }

            // SEND DEALER ID
            for (var player : players.entrySet()) {
                send("DEALER " + game.getDealerID(), player.getValue());
            }
            TimeUnit.MILLISECONDS.sleep(100);


            playARound(players, game);

            // INFORM THAT ROUND IS FINISHED
            sendToAll("FINISHED 1", players.values());

            game.endFirstRound();

            // REPLACE CARDS
            var replacedForPlayers = 0;

            while (replacedForPlayers < numberOfAllPlayers) {
                for (var player : players.entrySet()) {
                    var cardsToReplace = read(player.getValue());
                    var indexes = new ArrayList<Integer>();

                    var rawIndexes = cardsToReplace.split(" ");
                    if (!rawIndexes[0].contains("REPLACE")) {
                        continue;
                    }

                    replacedForPlayers++;

                    for (var i = 1; i < rawIndexes.length; i++) {
                        indexes.add(Integer.parseInt(rawIndexes[i]));
                    }

                    game.replaceCards(player.getKey(), indexes);

                    for (var card : game.getHand(player.getKey()).getCards()) {
                        send("CARD " + card.toSCP() + "\n", player.getValue());
                        TimeUnit.MILLISECONDS.sleep(50);
                    }
                }
            }

            // SECOND ROUND
            playARound(players, game);


            sendToAll("FINISHED 2", players.values());
            TimeUnit.MILLISECONDS.sleep(100);

            var winner = game.endSecondRound();
            System.out.println("The winner is " + winner);

            // SEND WINNER
            sendToAll("WINNER " + winner, players.values());
            TimeUnit.MILLISECONDS.sleep(100);

            // SEND BALANCES
            for (var player : players.entrySet()) {
                send("BALANCE " + game.getBalance(player.getKey()), player.getValue());
            }
            TimeUnit.MILLISECONDS.sleep(100);

            // INFORM THAT GAME IS FINISHED
            for (var player : players.entrySet()) {
                send("END", player.getValue());
            }

            if (true) {
                game.prepareNewGame();
            } else {
                gameInProgress = false;
            }
        }

    }
}