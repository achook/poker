package pl.edu.agh.kis.pz1;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a deck of cards.
 */
public class Deck {
    ArrayList<Card> cards;

    /**
     * Creates a new full deck of 52 cards and shuffles it.
     */
    public Deck() {
        cards = new ArrayList<Card>();

        for (Card.CardSuite suite: Card.CardSuite.values()) {
            for (Card.CardRank rank: Card.CardRank.values()) {
                cards.add(new Card(suite, rank));
            }
        }

        shuffle();
    }

    /**
     * Randomly shuffles the deck.
     */
    void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Returns n cards from the top of the deck.
     * @param n Number of cards to return.
     * @return n cards.
     */
    public Card[] getFromTop(int n) {
        var c = new Card[n];
        // TODO: Not enough cards in the deck - NULL?
        if (cardsLeft() > 0) {
            for (int i = 0; i < n; i++) {
                c[i] = cards.remove(0);
            }
        }

        return c;
    }

    /**
     * Returns a single card from the top of the deck.
     * @return A single card.
     */
    public Card getFromTop() {
        return getFromTop(1)[0];
    }

    /**
     * Returns a number of cards left in the deck.
     * @return A number of cards left.
     */
    public int cardsLeft() {
        return cards.size();
    }

    /**
     * Checks if the deck is full (has 52 cards).
     * @return true if the deck is full, false otherwise.
     */
    public boolean isFull() {
        return (cards.size() == 52);
    }

    /**
     * Checks if the deck is empty.
     * @return true if the deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return (cards.size() == 0);
    }

    /**
     * Adds a card to the deck.
     * @param c A card to add.
     */
    public void addCard(Card c) {
        cards.add(c);
    }
}
