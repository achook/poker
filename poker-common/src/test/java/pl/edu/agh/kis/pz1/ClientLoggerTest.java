package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ClientLoggerTest {
    ClientLogger clientLogger;
    LogHandler logHandler;

    static class LogHandler extends Handler {
        Level lastLevel = Level.FINEST;
        String lastMessage = "";

        public Level checkLevel() {
            return lastLevel;
        }

        public String checkMessage() {
            return lastMessage;
        }

        public void publish(LogRecord record) {
            lastMessage = record.getMessage();
            lastLevel = record.getLevel();
        }

        public void close(){}
        public void flush(){}
    }

    @BeforeEach
    void setUp() {
        clientLogger = new ClientLogger();
        logHandler = new LogHandler();
        logHandler.setLevel(Level.ALL);
        clientLogger.logger.getUseParentHandlers();
        clientLogger.logger.addHandler(logHandler);
        clientLogger.logger.setLevel(Level.ALL);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testLog() {
        clientLogger.log("test");
        assertEquals("test", logHandler.checkMessage());
    }

    @Test
    void testLogln() {
        clientLogger.logln("test");
        assertEquals("test\n", logHandler.checkMessage());
    }

    @Test
    void testDisplayHand() {
        var hand = new Hand();
        hand.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.ACE));
        hand.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        hand.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.QUEEN));
        hand.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.JACK));
        hand.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TEN));

        clientLogger.displayHand(hand);
        assertEquals("""
                        [ACE OF HEARTS, KING OF HEARTS, QUEEN OF HEARTS, JACK OF HEARTS, TEN OF HEARTS]
                        """, logHandler.checkMessage());

    }

    @Test
    void testDisplayID() {
        clientLogger.displayID(1);
        assertEquals("Your player number is 1\n", logHandler.checkMessage());
    }

    @Test
    void displayAnte() {
        clientLogger.displayAnte(1);
        assertEquals("Ante is $1\n", logHandler.checkMessage());
    }

    @Test
    void testAnnounceTurn() {
        clientLogger.announceTurn();
        assertEquals("It's your turn\n", logHandler.checkMessage());
    }

    @Test
    void announceWinner() {
        clientLogger.announceWinner(1);
        assertEquals("Player 1 won\n", logHandler.checkMessage());
    }

    @Test
    void askForCardsToDiscard() {
        clientLogger.askForCardsToDiscard();
        assertEquals("Which cards do you want to discard? (0/1/2/3/4)\n", logHandler.checkMessage());
    }

    @Test
    void displayMoney() {
        clientLogger.displayMoney(1);
        assertEquals("You have $1\n", logHandler.checkMessage());
    }

    @Test
    void announceDealer() {
        clientLogger.announceDealer(1);
        assertEquals("Player 1 is the dealer\n", logHandler.checkMessage());
    }


    @Test
    void askForContinue() {
        clientLogger.askForContinue();
        assertEquals("Do you want to continue? (y/n)\n", logHandler.checkMessage());
    }

    @Test
    void announceBet() {
        clientLogger.announceBet("BET 1 CHECK");
        assertEquals("Player 1 checked\n", logHandler.checkMessage());
    }
}