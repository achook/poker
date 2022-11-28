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
        Deck deck = new Deck();
        var topCard = deck.cards.get(0);

        assertEquals(topCard, deck.getFromTop());
    }

    @Test
    void cardsLeft() {
        Deck deck = new Deck();
        assertEquals(52, deck.cardsLeft());


        deck.getFromTop();
        assertEquals(51, deck.cardsLeft());
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
        var cardToAdd = new Card(Card.CardSuite.HEARTS, Card.CardRank.ACE);

        Deck deck = new Deck();
        deck.cards.clear();

        deck.addCard(cardToAdd);
        assertEquals(1, deck.cardsLeft());
        assertEquals(cardToAdd, deck.getFromTop());
    }

    public static class NonExistentPlayerException extends Exception {
        public NonExistentPlayerException(String message) {
            super(message);
        }
    }
}