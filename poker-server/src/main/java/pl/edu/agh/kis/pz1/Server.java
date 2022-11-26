package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Server {
    private static ServerSocketChannel serverSocketChannel;
    private static ServerSocket serverSocket;
    private static SelectionKey selectionKey;
    private static Selector selector;
    private static ByteBuffer buffer;

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

    private static void readAndAnswer(String message, SelectionKey key)
            throws IOException, InterruptedException {

        buffer = ByteBuffer.allocate(256);
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();

        while ((client.read(buffer)) > 0) {}

        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();

        while (buffer.hasRemaining()) {
            client.write (buffer);
        }
    }

    private static void send(String message, SelectionKey key) throws IOException {
        buffer = ByteBuffer.allocate(256);
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();

        buffer.put(message.getBytes());
        buffer.flip();

        while (buffer.hasRemaining()) {
            client.write (buffer);
        }
    }

    private static String read(SelectionKey key) throws IOException {
        buffer = ByteBuffer.allocate(256);
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();

        while (client.read(buffer) > 0) {}

        return new String(buffer.array()).trim();
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
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

                if (key.isAcceptable()) {
                    register(selector, serverSocketChannel);
                }

                if (key.isReadable()) {
                    readAndAnswer("ID " + currentID, key);
                    players.put(currentID, key);
                    currentID++;
                }

                iter.remove();
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

        // SEND CARDS
        for (var player : players.entrySet()) {
            var cards = game.getHand(player.getKey()).getCards();

            for (var card : cards) {
                send("CARD " + card.toSCP() + "\n", player.getValue());
            }
        }

        // SEND DEALER ID
        for (var player : players.entrySet()) {
            send("DEALER " + game.getDealerID(), player.getValue());
        }

    }
}