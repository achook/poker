package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import pl.edu.agh.kis.pz1.Player;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testConstructor() {
        Game game = new Game(20);
        assertNotNull(game);
    }

    @Test
    void call() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.raise(1, 30);
        game.call(0);

        assertEquals(950, player1.money);
        assertEquals(950, player2.money);

        assertEquals(100, game.getPot());
    }

    @Test
    void raise() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.raise(1, 50);

        assertEquals(930, player2.money);
        assertEquals(90, game.getPot());
    }

    @Test
    void fold() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.raise(0, 30);
        game.fold(1);

        assertEquals(980, player2.money);
        assertEquals(70, game.getPot());
    }

    @Test
    void check() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.check(1);

        assertEquals(980, player2.money);
        assertEquals(40, game.getPot());
    }

    @Test
    void testMoveCall() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.makeMove(1, Game.Move.RAISE, 30);
        game.makeMove(0, Game.Move.CALL);

        assertEquals(950, player1.money);
        assertEquals(950, player2.money);

        assertEquals(100, game.getPot());
    }

    @Test
    void testMakeMoveRaise() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.makeMove(1, Game.Move.RAISE, 50);

        assertEquals(930, player2.money);
        assertEquals(90, game.getPot());
    }

    @Test
    void testMakeMoveFold() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.makeMove(0, Game.Move.RAISE, 30);
        game.makeMove(1, Game.Move.FOLD);

        assertEquals(980, player2.money);
        assertEquals(70, game.getPot());
    }

    @Test
    void testMoveCheck() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.makeMove(1, Game.Move.CHECK, 0);

        assertEquals(980, player2.money);
        assertEquals(40, game.getPot());
    }

    @Test
    void getPlayersInGame() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        ArrayList<Player> playersList = new ArrayList<Player>(of(player1, player2));

        assertEquals(playersList, game.getPlayersInGame());
    }

    @Test
    void endFirstRound() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.endFirstRound();

        assertEquals(2, game.getPlayersInGame().size());
        assertEquals(40, game.getPot());
    }

    @Test
    void endSecondRound() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        var winningHand = new Hand();
        winningHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.ACE));
        winningHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.KING));
        winningHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        winningHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.JACK));
        winningHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.TEN));

        var losingHand = new Hand();
        losingHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.ACE));
        losingHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.KING));
        losingHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        losingHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.JACK));
        losingHand.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.NINE));

        player1.setHand(winningHand);
        player2.setHand(losingHand);

        var winner = game.endSecondRound();

        assertEquals(0, winner);
    }

    @Test
    void replaceCards() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        var player1CardsBefore = player1.getHand().getCards().clone();
        game.replaceCards(0, new ArrayList<Integer>(of(0, 1, 2, 3, 4)));
        var player1CardsAfter = player1.getHand().getCards();

        assertNotEquals(player1CardsBefore, player1CardsAfter);
    }

    @Test
    void testIsMoveLegal() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.raise(1, 30);

        assertTrue(game.isMoveLegal(0, Game.Move.CALL));
        assertFalse(game.isMoveLegal(0, Game.Move.CHECK));
        assertTrue(game.isMoveLegal(0, Game.Move.FOLD));
        assertTrue(game.isMoveLegal(0, Game.Move.RAISE));
    }

    @Test
    void testPrepareNewGame() {
        Game game = new Game(20);
        Player player1 = new Player(null, 0, game);
        Player player2 = new Player(null, 1, game);
        Player player3 = new Player(null, 2, game);

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        game.raise(1, 30);
        game.fold(2);
        game.call(0);

        game.prepareNewGame();

        var players = game.getPlayersInGame();
        assertEquals(3, players.size());

        assertEquals(0, game.currentBet);
        assertEquals(0, game.getPot());
        assertEquals(-1, game.lastPlayerIndexToRaise);

    }
}