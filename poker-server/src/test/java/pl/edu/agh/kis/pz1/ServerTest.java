package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void testStartServer() throws InternalServerException, IOException {
        Server.startServer(9898);
    }

    @Test
    void testStopServer() throws InternalServerException, IOException {
        Server.startServer(8989);
        Server.stopServer();

    }
}