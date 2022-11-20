package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @RepeatedTest(100)
    void shuffle() {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        deck1.shuffle();
        deck2.shuffle();

        assertNotEquals(deck1.cards, deck2.cards);
    }

    @Test
    void getFromTop() {
    }

    @Test
    void testGetFromTop() {
    }

    @Test
    void cardsLeft() {
    }

    @Test
    void isFull() {
        Deck deck = new Deck();
        assertTrue(deck.isFull());
    }

    @Test
    void isEmpty() {
        Deck deck = new Deck();
        deck.cards.clear();
        assertTrue(deck.isEmpty());
    }

    @Test
    void addCard() {
    }

    public static class NonExistentPlayerException extends Exception {
        public NonExistentPlayerException(String message) {
            super(message);
        }
    }
}