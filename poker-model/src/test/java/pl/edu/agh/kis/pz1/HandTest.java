package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class HandTest {
    @Test
    public void compareDifferentHandTypes() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.ACE));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.QUEEN));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.JACK));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TEN));

        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TWO));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.THREE));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.FOUR));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.FIVE));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.SIX));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void compareDifferentHandTypes2() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.ACE));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.QUEEN));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.JACK));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TEN));

        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.KING));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.ACE));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.KING));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.ACE));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.FIVE));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void compareSameHandTypes1() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.NINE));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.NINE));

        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.THREE));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.THREE));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.QUEEN));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void compareSameHandTypes2() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.TEN));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.FIVE));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.FOUR));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.TWO));

        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.TEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.FIVE));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.THREE));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TWO));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void compareTwoTwoPairs() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.FIVE));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.FIVE));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.TWO));

        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.FOUR));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.FOUR));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TWO));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void compareTwoThreeOfAKinds() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.FIVE));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.TWO));

        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.FOUR));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TWO));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void testTwoFourOfAKinds() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.TWO));

        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TWO));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void compareTwoFlushes() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TEN));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.FIVE));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.FOUR));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TWO));

        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.TEN));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.FIVE));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.THREE));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.TWO));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void compareTwoStraights() {
        Hand h1 = new Hand();
        Hand h2 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.QUEEN));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.JACK));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.TEN));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.NINE));

        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.QUEEN));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.JACK));
        h2.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.TEN));
        h2.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.NINE));
        h2.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.EIGHT));

        assertTrue(h1.compareTo(h2) > 0);
    }

    @Test
    public void testAnalyzeCards1() {
        Hand h1 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.NINE));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.NINE));

        assertEquals(Hand.HandType.FULL_HOUSE, h1.getType());
    }

    @Test
    public void testAnalyzeCards2() {
        Hand h1 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.ACE));
        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.NINE));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.NINE));

        assertEquals(Hand.HandType.TWO_PAIRS, h1.getType());
    }

    @Test
    public void testAnalyzeCards3() {
        Hand h1 = new Hand();

        h1.addCard(new Card(Card.CardSuite.HEARTS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.CLUBS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.DIAMONDS, Card.CardRank.KING));
        h1.addCard(new Card(Card.CardSuite.SPADES, Card.CardRank.TEN));

        assertEquals(Hand.HandType.FOUR_OF_A_KIND, h1.getType());
    }


}
