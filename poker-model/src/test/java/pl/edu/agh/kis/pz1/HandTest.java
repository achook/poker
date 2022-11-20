package pl.edu.agh.kis.pz1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class HandTest {
    @Test
    public void compareDifferentHandTypes1() {
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

}
