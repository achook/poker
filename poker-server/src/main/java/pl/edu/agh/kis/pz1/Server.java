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
            // Client disconnected
        }

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
        TimeUnit.MILLISECONDS.sleep(100);

        for (var player : players.entrySet()) {
            send("BEGIN", player.getValue());
        }
        TimeUnit.MILLISECONDS.sleep(100);


        System.out.println(players);
        // GAME STARTS

        var startingMoney = 1000;
        var ante = 10;

        var game = new Game(maxPlayers, ante);

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


        while (!game.isRoundFinished()) {
            // Check which players turn is it
            var curentPlayerID = game.getCurrentPlayerID();

            // Inform all the players
            for (var player : players.entrySet()) {
                send("TURN " + curentPlayerID, player.getValue());
            }

            // Wait for the current player to make a move
            String bet = "";
            while (!bet.contains("BET")) {
                bet = read(players.get(curentPlayerID));
            }

            System.out.println(bet);

            var rawBetElements = bet.split(" ");
            var betType = rawBetElements[1];


            if (betType.contains("FOLD")) {
                System.out.println("Fold " + curentPlayerID);
                game.fold(curentPlayerID);

            } else if (betType.contains("CALL")) {
                System.out.println("Call " + curentPlayerID);
                game.call(curentPlayerID);

            } else if (betType.contains("RAISE")) {
                var amount = Integer.parseInt(rawBetElements[2]);
                System.out.println("Raise " + amount + " " + curentPlayerID);
                game.raise(curentPlayerID, amount);

            } else if (betType.contains("CHECK")) {
                System.out.println("Check " + curentPlayerID);
                game.check(curentPlayerID);

            } else {
                // Handle error
            }

            // Send bet to other players
            for (var player : players.entrySet()) {
                if (player.getKey() != curentPlayerID) {
                    send("BET " + curentPlayerID + " " + betType, player.getValue());
                }
            }
        }

        System.out.println("WE FINISHED");

        // INFORM THAT ROUND IS FINISHED
        for (var player : players.entrySet()) {
            send("FINISHED 1", player.getValue());
        }

        game.endFirstRound();

        // REPLACE CARDS
        var replacedForPlayers = 0;

        while (replacedForPlayers < maxPlayers) {
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
        while (!game.isRoundFinished()) {
            // Check which players turn is it
            var curentPlayerID = game.getCurrentPlayerID();

            // Inform all the players
            for (var player : players.entrySet()) {
                send("TURN " + curentPlayerID, player.getValue());
            }

            // Wait for the current player to make a move
            String bet = "";
            while (!bet.contains("BET")) {
                bet = read(players.get(curentPlayerID));
            }

            System.out.println(bet);

            var rawBetElements = bet.split(" ");
            var betType = rawBetElements[1];


            if (betType.contains("FOLD")) {
                System.out.println("Fold " + curentPlayerID);
                game.fold(curentPlayerID);

            } else if (betType.contains("CALL")) {
                System.out.println("Call " + curentPlayerID);
                game.call(curentPlayerID);

            } else if (betType.contains("RAISE")) {
                var amount = Integer.parseInt(rawBetElements[2]);
                System.out.println("Raise " + amount + " " + curentPlayerID);
                game.raise(curentPlayerID, amount);

            } else if (betType.contains("CHECK")) {
                System.out.println("Check " + curentPlayerID);
                game.check(curentPlayerID);

            } else {
                // Handle error
            }

            // Send bet to other players
            for (var player : players.entrySet()) {
                if (player.getKey() != curentPlayerID) {
                    send("BET " + curentPlayerID + " " + betType, player.getValue());
                }
            }
        }


        for (var player : players.entrySet()) {
            send("FINISHED 2", player.getValue());
        }

        var winner = game.endSecondRound();

        System.out.println("WINNER IS " + winner);

        // SEND WINNER
        for (var player : players.entrySet()) {
            send("WINNER " + winner, player.getValue());
        }

        // SEND BALANCES
        for (var player : players.entrySet()) {
            send("BALANCE " + game.getBalance(player.getKey()), player.getValue());
        }

        // INFORM THAT GAME IS FINISHED
        for (var player : players.entrySet()) {
            send("END", player.getValue());
        }

    }
}