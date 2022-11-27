package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testConstructor() {
        var game = new Game(2, 100);

        assertEquals(2, game.players.length);
        assertEquals(200, game.pot);
        assertEquals(0, game.getDealerID());
        assertEquals(1, game.getCurrentPlayerID());
    }

    @Test
    void call() {
    }

    @Test
    void raise() {
    }

    @Test
    void fold() {
    }

    @Test
    void check() {
    }

    @Test
    void makeMove() {
    }

    @Test
    void testMakeMove() {
    }

    @Test
    void getPlayersInGame() {
    }

    @Test
    void endFirstRound() {
    }

    @Test
    void replaceCards() {
    }

    @Test
    void endSecondRound() {
    }

    @Test
    void isRoundFinished() {
    }


    @Test
    void isMoveLegal() {

    }
}