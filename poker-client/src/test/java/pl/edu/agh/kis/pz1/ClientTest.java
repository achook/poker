package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

        @Test
        void testStart() throws IOException, IOException, InterruptedException {
            int port = ThreadLocalRandom.current().nextInt(10000, 20000);

            Runnable r = () -> {
                try {
                    Server.playGame(port, 1, 50, 1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };

            new Thread(r).start();

            for (int i = 0; i < 10000000; i++) {
                i++;
            }

            Client.start(port);

            assertNotNull(Client.socketClient);
            assertTrue(Client.socketClient.isOpen());
            assertTrue(Client.socketClient.isBlocking());
            assertEquals(port, Client.socketClient.socket().getPort());
        }
}