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
import java.util.Set;

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

    private static String readAndAnswer(String message, SelectionKey key)
            throws IOException {

        var buffer = ByteBuffer.allocate(256);

        SocketChannel client = (SocketChannel) key.channel();

        client.read(buffer);
        String response = new String(buffer.array()).trim();
        buffer.clear();

        buffer.put(message.getBytes());
        client.write(buffer);
        buffer.clear();

        return response;
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws InternalServerException, IOException {
        Deck d = new Deck();
        Hand h = new Hand();

        for (int i = 0; i < 5; i++) {
            Card c = d.getFromTop();
            h.addCard(c);

        }

        ArrayList<Card> cs = h.getCards();

        for (int i = 0; i < 5; i++) {
            System.out.println(cs.get(i).getName());
        }

        startServer(8090);

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {

                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    register(selector, serverSocketChannel);
                }

                if (key.isReadable()) {
                    readAndAnswer("Read", key);
                }
                iter.remove();
            }
        }
    }
}