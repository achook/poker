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

    static ServerSocketChannel serverSocketChannel;
    static ServerSocket serverSocket;
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

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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

    static Game.Move checkBetType(String betType) {
        if (betType.contains("CHECK")) {
            return Game.Move.CHECK;
        } else if (betType.contains("CALL")) {
            return Game.Move.CALL;
        } else if (betType.contains("RAISE")) {
            return Game.Move.RAISE;
        } else if (betType.contains("FOLD")) {
            return Game.Move.FOLD;
        } else {
            return null;
        }
    }

    /**
     * Sends a message to all clients.
     * @param message message to send
     * @param keys clients keys
     * @throws IOException if an I/O error occurs
     */
    private static void sendToAll(String message, Collection<Player> keys) throws IOException {
        for (Player key : keys) {
            send(message, key.getSelectionKey());
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
    private static boolean playARound(Map<Integer, Player> players, Game game) throws IOException, InterruptedException {
        while (!game.isRoundFinished()) {
            // Check which players turn is it
            var currentPlayerID = game.getCurrentPlayerID();

            // Inform all the players
            sendToAll("TURN " + currentPlayerID, players.values());

            // Wait for the current player to make a move
            String bet = "";
            while (!bet.contains("BET")) {
                bet = read(players.get(currentPlayerID).getSelectionKey());
            }

            var rawBetElements = bet.split(" ");
            var betType = rawBetElements[1];

            Game.Move move = null;
            int amount = -1;

            while (move == null) {
                move = checkBetType(betType);
                if (move == Game.Move.RAISE) {
                    amount = Integer.parseInt(rawBetElements[2]);
                }

                if (!game.isMoveLegal(currentPlayerID, move) || move == null) {
                    send("ILLEGAL", players.get(currentPlayerID).getSelectionKey());
                }
            }

            game.makeMove(currentPlayerID, move, amount);

            // Send bet to other players
            for (var player : players.entrySet()) {
                if (player.getKey() != currentPlayerID) {
                    if (betType.contains("RAISE")) {
                        send("BET " + currentPlayerID + " " + betType + " " + amount, player.getValue().getSelectionKey());
                    } else {
                        send("BET " + currentPlayerID + " " + betType, player.getValue().getSelectionKey());
                    }
                }
            }

            if (game.getPlayersInGame().size() == 1) {
                var winner = game.getPlayersInGame().get(0);
                game.endByFold();

                for (var player : players.entrySet()) {
                    send("FINISHED WINNER " + winner.id, player.getValue().getSelectionKey());
                }

                return false;
            }

            TimeUnit.MILLISECONDS.sleep(100);
        }

        return true;
    }

    public static void main(String[] args) throws InternalServerException, IOException, InterruptedException {
        var port = Integer.parseInt(args[0]);
        var numberOfAllPlayers = Integer.parseInt(args[1]);
        var startingMoney = Integer.parseInt(args[2]);
        var ante = Integer.parseInt(args[3]);


        playGame(port, numberOfAllPlayers, ante, startingMoney);

    }

    static void playGame(int port, int numberOfAllPlayers, int ante, int startingMoney)
            throws InternalServerException, IOException, InterruptedException {
        startServer(port);

        var game = new Game(ante);

        // WAIT FOR PLAYERS
        var currentID = 0;
        Map<Integer, Player> players = new HashMap<>(Map.of());

        getPlayers(numberOfAllPlayers, game, currentID, players);
        TimeUnit.MILLISECONDS.sleep(100);


        // GAME STARTS

        // SEND ANTE
        for (var player : players.entrySet()) {
            send("ANTE " + ante, player.getValue().getSelectionKey());
        }
        TimeUnit.MILLISECONDS.sleep(100);

        // SEND STARTING MONEY
        for (var player : players.entrySet()) {
            send("MONEY " + startingMoney, player.getValue().getSelectionKey());
        }
        TimeUnit.MILLISECONDS.sleep(100);

        var gameInProgress = true;
        while (gameInProgress) {

            // SEND CARDS
            for (var player : players.entrySet()) {
                var cards = game.getHand(player.getKey()).getCards();

                for (var card : cards) {
                    send("CARD " + card.toSCP() + "\n", player.getValue().getSelectionKey());
                    TimeUnit.MILLISECONDS.sleep(50);
                }
            }

            // SEND DEALER ID
            for (var player : players.entrySet()) {
                send("DEALER " + game.getDealerID(), player.getValue().getSelectionKey());
            }
            TimeUnit.MILLISECONDS.sleep(100);


            var playSecondRound = playARound(players, game);

            if (playSecondRound) {

                // INFORM THAT ROUND IS FINISHED
                sendToAll("FINISHED", players.values());

                game.endFirstRound();

                // REPLACE CARDS
                var replacedForPlayers = 0;
                while (replacedForPlayers < game.getPlayersInGame().size()) {
                    for (var player : players.entrySet()) {
                        var cardsToReplace = read(player.getValue().getSelectionKey());
                        var indexes = new ArrayList<Integer>();

                        var rawIndexes = cardsToReplace.split(" ");
                        if (!rawIndexes[0].contains("REPLACE")) {
                            continue;
                        }

                        for (var i = 1; i < rawIndexes.length; i++) {
                            indexes.add(Integer.parseInt(rawIndexes[i]));
                        }

                        game.replaceCards(player.getKey(), indexes);

                        for (var card : game.getHand(player.getKey()).getCards()) {
                            send("CARD " + card.toSCP() + "\n", player.getValue().getSelectionKey());
                            TimeUnit.MILLISECONDS.sleep(50);
                        }


                        replacedForPlayers++;
                    }
                }
                TimeUnit.MILLISECONDS.sleep(100);

                // SECOND ROUND
                playARound(players, game);



                sendToAll("FINISHED", players.values());
                TimeUnit.MILLISECONDS.sleep(100);

                var winner = game.endSecondRound();

                // SEND WINNER
                sendToAll("WINNER " + winner, players.values());
            }
            TimeUnit.MILLISECONDS.sleep(100);


            // SEND BALANCES
            for (var player : players.entrySet()) {
                send("BALANCE " + game.getBalance(player.getKey()), player.getValue().getSelectionKey());
            }
            TimeUnit.MILLISECONDS.sleep(100);


            var gotResponseFrom = 0;
            while (gotResponseFrom < numberOfAllPlayers) {
                for (var player : players.entrySet()) {
                    var response = read(player.getValue().getSelectionKey());
                    if (response.contains("CONTINUE")) {
                        gotResponseFrom++;
                    }

                    if (response.contains("QUIT")) {
                        game.players.remove(player.getValue().getID());
                    }
                }
            }
                game.prepareNewGame();

            if (game.getPlayersInGame().size() < 2) {
                gameInProgress = false;
            }
        }
    }

    private static void getPlayers(int numberOfAllPlayers, Game game, int currentID, Map<Integer, Player> players) throws IOException {
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

                if (key.isWritable()) {
                    var isAlreadyRegistered = false;

                    for (var player : players.entrySet()) {
                        if (player.getValue().getSelectionKey() == key) {
                            isAlreadyRegistered = true;
                            break;
                        }
                    }

                    if (!isAlreadyRegistered) {
                        var p = new Player(key, currentID, game);
                        game.addPlayer(p);
                        players.put(currentID, p);
                        send("ID " + currentID, key);
                        currentID++;
                    }

                }
            }
        }
    }
}