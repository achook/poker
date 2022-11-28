package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import pl.edu.agh.kis.pz1.Utilities;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

        @Test
        void testStart() throws InternalServerException, IOException {
            int port = ThreadLocalRandom.current().nextInt(10000, 20000);

            Server.startServer(port);
            assertNotNull(Server.serverSocket);
            assertNotNull(Server.serverSocketChannel);

            assertEquals(port, Server.serverSocket.getLocalPort());
            assertTrue(Server.serverSocketChannel.isOpen());
            assertFalse(Server.serverSocketChannel.isBlocking());

            Server.stopServer();
        }

        @Test
        void testCheckBetType() {
            assertEquals(Game.Move.CHECK, Server.checkBetType("CHECK 2"));
            assertEquals(Game.Move.CALL, Server.checkBetType("CALL 1"));
            assertEquals(Game.Move.RAISE, Server.checkBetType("RAISE 3 220"));
            assertEquals(Game.Move.FOLD, Server.checkBetType("FOLD 1"));
            assertNull(Server.checkBetType("UGABUGA 2"));
        }

        @Test
        void testStop() throws InternalServerException, IOException {
            int port = ThreadLocalRandom.current().nextInt(10000, 20000);

            Server.startServer(port);
            Server.stopServer();
            assertFalse(Server.serverSocketChannel.isOpen());
        }

        @Test
        void testGame() throws IOException {
            var port = ThreadLocalRandom.current().nextInt(10000, 20000);
            var numberOfAllPlayers = 2;

            Runnable r = () -> {
                try {
                    Server.playGame(port, numberOfAllPlayers);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };

            new Thread(r).start();

            var socketClient1 = SocketChannel.open(new InetSocketAddress("localhost", port));
            var socketClient2 = SocketChannel.open(new InetSocketAddress("localhost", port));


            var buffer1 = ByteBuffer.allocate(256);
            socketClient1.read(buffer1);
            var stringID1 = new String(buffer1.array()).trim();
            var id1 = Utilities.getArgument(stringID1);


            var buffer2 = ByteBuffer.allocate(256);
            socketClient2.read(buffer2);
            var stringID2 = new String(buffer2.array()).trim();
            var id2 = Utilities.getArgument(stringID2);


            assertEquals(0, id1);
            assertEquals(1, id2);

            buffer1.clear(); buffer2.clear();

            socketClient1.read(buffer1);
            var anteString1 = new String(buffer1.array()).trim();
            var ante1 = Utilities.getArgument(anteString1);

            socketClient2.read(buffer2);
            var anteString2 = new String(buffer2.array()).trim();
            var ante2 = Utilities.getArgument(anteString2);

            assertEquals(ante1, ante2);

            buffer1.clear(); buffer2.clear();

            socketClient1.read(buffer1);
            var moneyString1 = new String(buffer1.array()).trim();
            var money1 = Utilities.getArgument(moneyString1);

            socketClient2.read(buffer2);
            var moneyString2 = new String(buffer2.array()).trim();
            var money2 = Utilities.getArgument(moneyString2);

            assertEquals(money1, money2);

            socketClient1.close();
            socketClient2.close();
        }
}