package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server {
    private static ServerSocketChannel serverSocketChannel;
    private static ServerSocket serverSocket;
    private static SelectionKey selectionKey;
    private static Selector selector;

    public static void startServer(int port) throws InternalServerException, IOException {
        var address = new InetSocketAddress(port);

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        selector = Selector.open();

        selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void stopServer() throws InternalServerException {
        try {
            serverSocket.close();
        } catch (Exception e) {
            throw new InternalServerException(e.toString());
        }
    }

    private static String readAndAnswer(String message, SelectionKey key)
            throws IOException, InterruptedException {

        var buffer = ByteBuffer.allocate(256);
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();

        while ((client.read(buffer)) > 0) {}
        buffer.flip();

        var s = new String(buffer.array()).trim();

        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();

        while (buffer.hasRemaining()) {
            client.write (buffer);
        }

        return s;
    }

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

    private static String read(SelectionKey key) throws IOException {
        var buffer = ByteBuffer.allocate(256);
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();

        int numRead;

        while ((numRead = client.read(buffer)) > 0) {}
        if (numRead == -1) {
            System.out.println("Client disconnected");
        }

        return new String(buffer.array()).trim();
    }

    private static String forceRead(SelectionKey key) throws IOException {
        var buffer = ByteBuffer.allocate(256);
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();

        System.out.println("Waiting for message...");

        while (client.read(buffer) == 0) {
        }

        System.out.println("Read: " + new String(buffer.array()).trim());

        while (client.read(buffer) > 0) {}

        return new String(buffer.array()).trim();
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);

        client.register(selector, client.validOps());
    }

    public static void main(String[] args) throws InternalServerException, IOException, InterruptedException {

        startServer(8090);

        var maxPlayers = 2;

        // WAIT FOR PLAYERS
        var currentID = 0;
        Map<Integer, SelectionKey> players = new java.util.HashMap<>(Map.of());

        while (currentID < maxPlayers) {
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


        System.out.println(players);
        // GAME STARTS

        var startingMoney = 1000;
        var ante = 10;

        var game = new Game(maxPlayers, ante);

        // SEND STARTING MONEY
        for (var player : players.entrySet()) {
            send("START " + startingMoney, player.getValue());
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

        // GET BETS
        var bets = new java.util.HashMap<Integer, String>(Map.of());

        while (game.getPlayersInGame().size() > 1) {
            while (bets.size() < game.getPlayersInGame().size()) {
                for (var player : players.entrySet()) {
                    if (player.getValue().isWritable()) {
                        var bet = read(player.getValue());
                        if (!bet.equals("")) {
                            bets.put(player.getKey(), bet);
                        }
                    }
                }
            }

            System.out.println(bets);

            for (var bet : bets.entrySet()) {
                if (bet.getValue().contains("FOLD")) {
                    game.fold(bet.getKey());
                    continue;
                }

                if (bet.getValue().contains("CALL")) {
                    game.call(bet.getKey());
                }

                if (bet.getValue().contains("CHECK")) {
                    game.check(bet.getKey());
                }

                if (bet.getValue().contains("RAISE")) {
                    var amount = Integer.parseInt(bet.getValue().split(" ")[1]);
                    game.raise(bet.getKey(), amount);
                }
            }

            System.out.println(game.getPlayersInGame().size());

        }

        game.endFirstRound();

        // REPLACE CARDS
        for (var player : players.entrySet()) {
            var cardsToReplace = read(player.getValue());
            var indexes = new ArrayList<Integer>();

            for (var cardIndex: cardsToReplace.split(" ")) {
                indexes.add(Integer.parseInt(cardIndex));
            }

            game.replaceCards(player.getKey(), indexes);

            for (var card : game.getHand(player.getKey()).getCards()) {
                send("CARD " + card.toSCP() + "\n", player.getValue());
            }
        }

        // GET BETS
        while (game.getPlayersInGame().size() > 1) {
            for (var player: players.entrySet()) {
                if (!game.getPlayersInGame().contains(player.getKey())) {
                    continue;
                }

                var bet = read(player.getValue());

                if (bet.contains("FOLD")) {
                    game.fold(player.getKey());
                }

                if (bet.contains("CALL")) {
                    game.call(player.getKey());
                }

                if (bet.contains("CHECK")) {
                    game.check(player.getKey());
                }

                if (bet.contains("RAISE")) {
                    var amount = Integer.parseInt(bet.split(" ")[1]);
                    game.raise(player.getKey(), amount);
                }
            }
        }

        var winner = game.endSecondRound();

        // SEND WINNER
        for (var player : players.entrySet()) {
            send("WINNER " + winner, player.getValue());
        }


    }
}