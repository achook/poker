package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import static pl.edu.agh.kis.pz1.Card.*;


class CardTest {

    @Test
    void testEquals() {
        Card card1 = new Card(CardSuite.HEARTS, CardRank.ACE);
        Card card2 = new Card(CardSuite.HEARTS, CardRank.ACE);
        Card card3 = new Card(CardSuite.SPADES, CardRank.ACE);
        Card card4 = new Card(CardSuite.HEARTS, CardRank.TWO);

        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
        assertNotEquals(card1, card4);
    }

    @Test
    void testCompareTo() {
        assertTrue(new Card(CardSuite.HEARTS, CardRank.KING).compareTo(new Card(CardSuite.HEARTS, CardRank.QUEEN)) > 0);
        assertEquals(0, new Card(CardSuite.HEARTS, CardRank.ACE).compareTo(new Card(CardSuite.SPADES, CardRank.ACE)));
        assertTrue(new Card(CardSuite.HEARTS, CardRank.TWO).compareTo(new Card(CardSuite.HEARTS, CardRank.FOUR)) < 0);
    }

    @Test
    void testFromSCP() {
        assertEquals(Card.fromSCP("2H"), new Card(CardSuite.HEARTS, CardRank.TWO));
        assertEquals(Card.fromSCP("5H"), new Card(CardSuite.HEARTS, CardRank.FIVE));
        assertEquals(Card.fromSCP("QH"), new Card(CardSuite.HEARTS, CardRank.QUEEN));
        assertEquals(Card.fromSCP("KH"), new Card(CardSuite.HEARTS, CardRank.KING));
        assertEquals(Card.fromSCP("AH"), new Card(CardSuite.HEARTS, CardRank.ACE));
        assertEquals(Card.fromSCP("4D"), new Card(CardSuite.DIAMONDS, CardRank.FOUR));
        assertEquals(Card.fromSCP("6C"), new Card(CardSuite.CLUBS, CardRank.SIX));
        assertEquals(Card.fromSCP("AS"), new Card(CardSuite.SPADES, CardRank.ACE));
    }

    @Test
    void testToSCP() {

        assertEquals("2H", new Card(CardSuite.HEARTS, CardRank.TWO).toSCP());
        assertEquals("5H", new Card(CardSuite.HEARTS, CardRank.FIVE).toSCP());
        assertEquals("QH", new Card(CardSuite.HEARTS, CardRank.QUEEN).toSCP());
        assertEquals("KH", new Card(CardSuite.HEARTS, CardRank.KING).toSCP());
        assertEquals("AH", new Card(CardSuite.HEARTS, CardRank.ACE).toSCP());
        assertEquals("4D", new Card(CardSuite.DIAMONDS, CardRank.FOUR).toSCP());
        assertEquals("6C", new Card(CardSuite.CLUBS, CardRank.SIX).toSCP());
        assertEquals("AS", new Card(CardSuite.SPADES, CardRank.ACE).toSCP());

    }
}