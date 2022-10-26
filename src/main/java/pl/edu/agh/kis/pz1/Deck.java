package pl.edu.agh.kis.pz1;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    Deck() {
        cards = new ArrayList<Card>();

        for (Card.CardSuite suite: Card.CardSuite.values()) {
            for (Card.CardRank rank: Card.CardRank.values()) {
                cards.add(new Card(suite, rank));
            }
        }

        shuffle();
    }

    void shuffle() {
        Collections.shuffle(cards);
    }

    public Card[] getFromTop(int n) {
        var c = new Card[n];
        // TODO: Not enough cards in the deck - NULL?
        if (cardsLeft() > 0) {
            for (int i = 0; i < n; i++) {
                c[i] = cards.remove(0);
            }

            return c;
        }

        return null;
    }

    public Card getFromTop() {
        return getFromTop(1)[0];
    }

    public int cardsLeft() {
        return cards.size();
    }

    public boolean isFull() {
        return (cards.size() == 52);
    }

    public boolean isEmpty() {
        return (cards.size() == 0);
    }

    public void addCard(Card c) {
        cards.add(c);
    }
}
