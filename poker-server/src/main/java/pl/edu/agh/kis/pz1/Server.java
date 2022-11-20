package pl.edu.agh.kis.pz1;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private void startServer(int port) throws InternalServerException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            throw new InternalServerException(e.toString());
        }
    }

    private void stopServer() throws InternalServerException {
        try {
            serverSocket.close();
        } catch (Exception e) {
            throw new InternalServerException(e.toString());
        }
    }

    public static void main(String[] args) {
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
    }
}