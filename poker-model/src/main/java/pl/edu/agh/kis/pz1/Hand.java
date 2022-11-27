package pl.edu.agh.kis.pz1;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a player's hand.
 */
public class Hand implements Comparable<Hand> {
    public enum HandType {
        HIGH_CARD,
        PAIR,
        TWO_PAIRS,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }

    private ArrayList<Card> cards;
    private HandType type;

    private Card.CardRank higherRank = null;
    private Card.CardRank lowerRank = null;

    public Hand() {
        cards = new ArrayList<Card>();
        type = HandType.HIGH_CARD;
    }

    /**
     * Compares this hand with another hand h.
     * @param h Hand to compare with.
     * @return positive number if this hand is better than h, negative number if this hand is worse than h,
     * 0 if this hand is equal to h.
     */
    private int compareHighCard(Hand h) {
        for (int i = 4; i >= 0; i--) {
            int c = cards.get(i).compareTo(h.cards.get(i));
            if (c != 0) {
                return c;
            }
        }

        return 0;
    }

    /**
     * Compares significant higher rank of this hand with significant higher rank of h.
     * @param h Hand to compare with.
     * @return positive number if this hand is better than h (according to significant higher rank),
     * negative number if this hand is worse than h, 0 if this hand is equal to h.
     */
    private int compareHigherRank(Hand h) {
        return higherRank.compareTo(h.higherRank);
    }

    /**
     * Compares significant lower rank of this hand with significant lower rank of h.
     * @param h Hand to compare with.
     * @return positive number if this hand is better than h (according to significant lower rank),
     * negative number if this hand is worse than h, 0 if this hand is equal to h.
     */
    private int compareLowerRank(Hand h) {
        return lowerRank.compareTo(h.lowerRank);
    }

    /**
     * Compares this hand with another hand h.
     * @param h Hand to compare with.
     * @return positive number if this hand is better than h, negative number if this hand is worse than h,
     * 0 if this hand is equal to h.
     */
    @Override
    public int compareTo(Hand h) {

        var t1 = this.getType();
        var t2 = h.getType();
        var diff = t1.ordinal() - t2.ordinal();

        // DIFFERENT HANDS
        if (diff != 0) {
            return diff;
        }

        var c1 = this.getCards();
        var c2 = h.getCards();

        // TWO HIGH CARDS
        if (t1 == HandType.HIGH_CARD) {
            return compareHighCard(h);
        }

        // TWO PAIRS
        if (t1 == HandType.PAIR) {
            var r = compareHigherRank(h);
            if (r != 0) {
                return r;
            }

            return compareHighCard(h);
        }

        // TWO TWO PAIRS
        if (t1 == HandType.TWO_PAIRS) {
            // Compare ranks of the higher pair of each pair
            var r = compareHigherRank(h);
            if (r != 0) {
                return r;
            }

            // Compare ranks of the lower pair of each pair
            var r2 = compareLowerRank(h);
            if (r2 != 0) {
                return r2;
            }

            return compareHighCard(h);

        }

        // TWO THREE OF A KINDS
        if (t1 == HandType.THREE_OF_A_KIND) {
            var r = compareHigherRank(h);
            if (r != 0) {
                return r;
            }

            return compareHighCard(h);
        }

        // TWO STRAIGHTS
        if (t1 == HandType.STRAIGHT) {
            return compareHighCard(h);
        }

        // TWO FLUSHES
        if (t1 == HandType.FLUSH) {
            return compareHighCard(h);
        }

        // TWO FULL HOUSES
        if (t1 == HandType.FULL_HOUSE) {
            var r = compareHigherRank(h);
            if (r != 0) {
                return r;
            }

            return compareHighCard(h);
        }

        // TWO FOUR OF A KINDS
        if (t1 == HandType.FOUR_OF_A_KIND) {
            var r = compareHigherRank(h);
            if (r != 0) {
                return r;
            }

            return compareHighCard(h);
        }

        // TWO STRAIGHT FLUSHES
        if (t1 == HandType.STRAIGHT_FLUSH) {
            return compareHighCard(h);
        }


        return 0;

    }

    /**
     * Gets the cards in this hand.
     * @return cards in this hand.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Gets the type of this hand.
     * @return type of this hand.
     */
    public HandType getType() {
        return type;
    }

    /**
     * Removes i-th card from this hand.
     * @param i index of the card to remove.
     */
    public void removeCard(int i) {
        cards.remove(i);
    }

    public void removeCards(ArrayList<Integer> indices) {
        var toRemove = new ArrayList<Card>();
        for (var i : indices) {
            toRemove.add(cards.get(i));
        }

        cards.removeAll(toRemove);
    }

    /**
     * Adds a card to this hand and sorts it.
     * @param c card to add.
     */
    public void addCard(Card c) {
        cards.add(c);

        cards.sort(Collections.reverseOrder());

        if (isFull()) {
            analyzeCards();
        }
    }

    /**
     * Adds cards to this hand and sorts them.
     * @param c cards to add.
     */
    public void addCards(Card[] c) {
        Collections.addAll(cards, c);

        this.cards.sort(Collections.reverseOrder());

        if (isFull()) {
            analyzeCards();
        }
    }

    /**
     * Checks if this hand is full.
     * @return true if this hand is full, false otherwise.
     */
    public boolean isFull() {
        return (cards.size() == 5);
    }

    /**
     * Gets the number of cards in this hand.
     * @return number of cards in this hand.
     */
    public int size() {
        return cards.size();
    }


    /**
     * Checks if the cards in this hand are straight (in order).
     * @return true if the cards in this hand are straight, false otherwise.
     */
    private boolean checkIfStraight() {
        // TODO: Ace as lowest card
        for (int i = 0; i < 4; i++) {
            if (cards.get(i).getRank().ordinal() != cards.get(i + 1).getRank().ordinal() + 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the cards in this hand are flush (same suit).
     * @return true if the cards in this hand are flush, false otherwise.
     */
    private boolean checkIfFlush() {
        for (int i = 0; i < 4; i++) {
            if (cards.get(i).getSuite() != cards.get(i + 1).getSuite()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the i-th and j-th cards in this hand are the same rank.
     * @param i index of the first card.
     * @param j index of the second card.
     * @return true if the i-th and j-th cards in this hand are the same rank, false otherwise.
     */
    private boolean checkIfSameRank(int i, int j) {
        return (cards.get(i).getRank() == cards.get(j).getRank());
    }

    /**
     * Analyzes the cards in this hand and sets the type and significant ranks.
     */
    private void analyzeCards() {
        var isStraight = checkIfStraight();
        var isFlush = checkIfFlush();
        higherRank = null;
        lowerRank = null;

        // STRAIGHT / ROYAL FLUSH
        // Checks if all cards have the same suite and if they are in a row
        // If so, it's a straight flush
        if (isStraight && isFlush) {
            // Check for royal flush
            // If the highest card is an ace, it's a royal flush
            if (cards.get(0).getRank() == Card.CardRank.ACE) {
                type = HandType.ROYAL_FLUSH;
                return;
            }

            type = HandType.STRAIGHT_FLUSH;
            return;
        }

        // FOUR OF A KIND
        // Checks if the first and the fourth card are the same, or the second and the fifth
        // card are the same, it's a four of a kind
        if (checkIfSameRank(0, 3) || checkIfSameRank(1, 4)) {
            higherRank = cards.get(2).getRank();
            lowerRank = null;
            type = HandType.FOUR_OF_A_KIND;
            return;
        }

        // FULL HOUSE
        // Checks if the first and the third card are the same, and the fourth and the fifth
        // card are the same, or the first and the second card are the same, and the third
        // and the fifth card are the same
        if ((checkIfSameRank(0, 2) && checkIfSameRank(3, 4))
                || (checkIfSameRank(0, 1) && checkIfSameRank(2, 4))) {
            higherRank = cards.get(2).getRank();
            lowerRank = null;
            type = HandType.FULL_HOUSE;
            return;
        }


        // FLUSH
        // Checks if all cards have the same suite
        if (isFlush) {
            higherRank = null;
            lowerRank = null;
            type = HandType.FLUSH;
            return;
        }

        // STRAIGHT
        // Checks if all cards are in a row
        if (isStraight) {
            higherRank = null;
            lowerRank = null;
            type = HandType.STRAIGHT;
            return;
        }

        // THREE OF A KIND
        // Checks if the first and the third card are the same, or the second and the fourth
        // card are the same, or the third and the fifth card are the same
        if (checkIfSameRank(0, 2) || checkIfSameRank(1, 3) || checkIfSameRank(2, 4)) {
            higherRank = cards.get(2).getRank();
            lowerRank = null;
            type = HandType.THREE_OF_A_KIND;
            return;
        }

        // TWO PAIR
        // Checks if the first and the second card are the same, and the third and the
        // fourth card are the same, or the first and the second card are the same, and
        // the fourth and the fifth card are the same, or the second and the third card
        // are the same, and the fourth and the fifth card are the same
        if (checkIfSameRank(0, 1) && checkIfSameRank(2, 3)) {
            higherRank = cards.get(0).getRank();
            lowerRank = cards.get(2).getRank();
            type = HandType.TWO_PAIRS;
            return;
        } else if (checkIfSameRank(0, 1) && checkIfSameRank(3, 4)) {
            higherRank = cards.get(0).getRank();
            lowerRank = cards.get(3).getRank();
            type = HandType.TWO_PAIRS;
            return;
        } else if (checkIfSameRank(1, 2) && checkIfSameRank(3, 4)) {
            higherRank = cards.get(1).getRank();
            lowerRank = cards.get(3).getRank();
            type = HandType.TWO_PAIRS;
            return;
        }


        // ONE PAIR
        // Checks if the first and the second card are the same, or the second and the
        // third card are the same, or the third and the fourth card are the same, or the
        // fourth and the fifth card are the same
        if (checkIfSameRank(0, 1) || checkIfSameRank(1, 2) ) {
            higherRank = cards.get(1).getRank();
            type = HandType.PAIR;
            return;
        } else if (checkIfSameRank(2, 3) || checkIfSameRank(3, 4)) {
            higherRank = cards.get(3).getRank();
            type = HandType.PAIR;
            return;
        }

        // Default value
        type = HandType.HIGH_CARD;
    }

    public Card.CardRank getHigherRank() {
        return higherRank;
    }

    public HandType getHandType() {
        return type;
    }

    public Card.CardRank getLowerRank() {
        return lowerRank;
    }

}
