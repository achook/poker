package pl.edu.agh.kis.pz1;

import java.util.Map;

/**
 * Represents a single card.
 * Can represent a card from a full deck (without joker).
 */
public class Card implements Comparable<Card> {
    /**
     * Represents a suite of a card.
     */
    public enum CardSuite {
        HEARTS, DIAMONDS, CLUBS, SPADES;
    }

    /**
     * Represents a rank of a card.
     */
    public enum CardRank {
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE
    }

    private final CardSuite suite;
    private final CardRank rank;

    public Card(CardSuite s, CardRank r) {
        suite = s;
        rank = r;
    }

    static Card fromSCP(String scp) {
        var s = scp.charAt(1);
        var r = scp.charAt(0);

        CardSuite suite;
        CardRank rank;

        switch (s) {
            case 'H' -> suite = CardSuite.HEARTS;
            case 'D' -> suite = CardSuite.DIAMONDS;
            case 'C' -> suite = CardSuite.CLUBS;
            case 'S' -> suite = CardSuite.SPADES;
            default -> throw new IllegalArgumentException("Invalid suite: " + s);
        }

        switch (r) {
            case '2' -> rank = CardRank.TWO;
            case '3' -> rank = CardRank.THREE;
            case '4' -> rank = CardRank.FOUR;
            case '5' -> rank = CardRank.FIVE;
            case '6' -> rank = CardRank.SIX;
            case '7' -> rank = CardRank.SEVEN;
            case '8' -> rank = CardRank.EIGHT;
            case '9' -> rank = CardRank.NINE;
            case 'T' -> rank = CardRank.TEN;
            case 'J' -> rank = CardRank.JACK;
            case 'Q' -> rank = CardRank.QUEEN;
            case 'K' -> rank = CardRank.KING;
            case 'A' -> rank = CardRank.ACE;
            default -> throw new IllegalArgumentException("Invalid rank: " + r);
        }

        return new Card(suite, rank);
    }

    @Override
    public int compareTo(Card c) {
        return rank.compareTo(c.rank);
    }

    /**
     * Returns a suite of a card.
     * @return A suite of a card.
     */
    public CardSuite getSuite() {
        return suite;
    }

    /**
     * Returns a rank of a card.
     * @return A rank of a card.
     */
    public CardRank getRank() {
        return rank;
    }

    /**
     * Returns textual representation of a card - rank and suite.
     * @return A string representation of a card.
     */
    public String getName() {
        return getRank().name() + " OF " + getSuite().name();
    }

    @Override
    public String toString() {
        return getName();
    }

    public String toSCP() {
        Map<CardRank, String> rankMap = Map.ofEntries(
                Map.entry(CardRank.TWO, "2"),
                Map.entry(CardRank.THREE, "3"),
                Map.entry(CardRank.FOUR, "4"),
                Map.entry(CardRank.FIVE, "5"),
                Map.entry(CardRank.SIX, "6"),
                Map.entry(CardRank.SEVEN, "7"),
                Map.entry(CardRank.EIGHT, "8"),
                Map.entry(CardRank.NINE, "9"),
                Map.entry(CardRank.TEN, "T"),
                Map.entry(CardRank.JACK, "J"),
                Map.entry(CardRank.QUEEN, "Q"),
                Map.entry(CardRank.KING, "K"),
                Map.entry(CardRank.ACE, "A")
        );

        Map<CardSuite, String> suiteMap = Map.ofEntries(
                Map.entry(CardSuite.HEARTS, "H"),
                Map.entry(CardSuite.DIAMONDS, "D"),
                Map.entry(CardSuite.CLUBS, "C"),
                Map.entry(CardSuite.SPADES, "S")
        );

        return rankMap.get(rank) + suiteMap.get(suite);
    }
}