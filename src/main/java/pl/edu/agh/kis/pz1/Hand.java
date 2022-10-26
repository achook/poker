package pl.edu.agh.kis.pz1;

import java.util.ArrayList;
import java.util.Collections;

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

    private Card.CardRank higherRank;
    private Card.CardRank lowerRank;

    Hand() {
        cards = new ArrayList<Card>();
        type = HandType.HIGH_CARD;
    }

    @Override
    public int compareTo(Hand h) {
        // TODO: Better comparison

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
            for (int i = 0; i < 5; i++) {
                diff = c1.get(i).compareTo(c2.get(i));

                if (diff != 0) {
                    return diff;
                }
            }
        }

        // TWO PAIRS

        return 0;

    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public HandType getType() {
        return type;
    }

    public void removeCard(int i) {
        cards.remove(i);
    }

    public void addCard(Card c) {
        cards.add(c);

        cards.sort(Collections.reverseOrder());

        if (isFull()) {
            analyzeCards();
        }
    }

    public boolean isFull() {
        return (cards.size() == 5);
    }

    public int size() {
        return cards.size();
    }

    // Checks if all cards are in a row
    private boolean checkIfStraight() {
        // TODO: Ace as lowest card
        for (int i = 0; i < 4; i++) {
            if (cards.get(i).getRank().ordinal() != cards.get(i + 1).getRank().ordinal() + 1) {
                return false;
            }
        }

        return true;
    }

    private boolean checkIfFlush() {
        for (int i = 0; i < 4; i++) {
            if (cards.get(i).getSuite() != cards.get(i + 1).getSuite()) {
                return false;
            }
        }

        return true;
    }

    private boolean checkIfSameRank(int i, int j) {
        return (cards.get(i).getRank() == cards.get(j).getRank());
    }

    private HandType analyzeCards() {
        var isStraight = checkIfStraight();
        var isFlush = checkIfFlush();

        // STRAIGHT / ROYAL FLUSH
        // Checks if all cards have the same suite and if they are in a row
        // If so, it's a straight flush
        if (isStraight && isFlush) {
            // Check for royal flush
            // If the highest card is an ace, it's a royal flush
            if (cards.get(0).getRank() == Card.CardRank.ACE) {
                return (type = HandType.ROYAL_FLUSH);
            }

            return (type = HandType.STRAIGHT_FLUSH);
        }

        // FOUR OF A KIND
        // Checks if the first and the fourth card are the same, or the second and the fifth
        // card are the same, it's a four of a kind
        if (checkIfSameRank(0, 3) || checkIfSameRank(1, 4)) {
            return (type = HandType.FOUR_OF_A_KIND);
        }

        // FULL HOUSE
        // Checks if the first and the third card are the same, and the fourth and the fifth
        // card are the same, or the first and the second card are the same, and the third
        // and the fifth card are the same
        if ((checkIfSameRank(0, 2) && checkIfSameRank(3, 4))
                || (checkIfSameRank(0, 1) && checkIfSameRank(2, 4))) {
            return (type = HandType.FULL_HOUSE);
        }

        // FLUSH
        // Checks if all cards have the same suite
        if (isFlush) {
            return (type = HandType.FLUSH);
        }

        // STRAIGHT
        // Checks if all cards are in a row
        if (isStraight) {
            return (type = HandType.STRAIGHT);
        }

        // THREE OF A KIND
        // Checks if the first and the third card are the same, or the second and the fourth
        // card are the same, or the third and the fifth card are the same
        if (checkIfSameRank(0, 2) || checkIfSameRank(1, 3) || checkIfSameRank(2, 4)) {
            return (type = HandType.THREE_OF_A_KIND);
        }

        // TWO PAIR
        // Checks if the first and the second card are the same, and the third and the
        // fourth card are the same, or the first and the second card are the same, and
        // the fourth and the fifth card are the same, or the second and the third card
        // are the same, and the fourth and the fifth card are the same
        if ((checkIfSameRank(0, 1) && checkIfSameRank(2, 3))
                || (checkIfSameRank(0, 1) && checkIfSameRank(3, 4))
                || (checkIfSameRank(1, 2) && checkIfSameRank(3, 4))) {
            return (type = HandType.TWO_PAIRS);
        }

        // ONE PAIR
        // Checks if the first and the second card are the same, or the second and the
        // third card are the same, or the third and the fourth card are the same, or the
        // fourth and the fifth card are the same
        if (checkIfSameRank(0, 1) || checkIfSameRank(1, 2)
                || checkIfSameRank(2, 3) || checkIfSameRank(3, 4)) {
            return (type = HandType.PAIR);
        }

        // Default value
        return (type = HandType.HIGH_CARD);
    }

    public void getFour() {};
    public void getThree() {};
    public void getTwoPair() {};
    public void getPair() {};

}
