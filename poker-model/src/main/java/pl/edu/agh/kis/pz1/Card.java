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

    /**
     * Returns a card from a valid SCP string.
     * @param scp SCP encoded card
     * @return card
     */
    static Card fromSCP(String scp) {
        var r = scp.charAt(0);
        var s = scp.charAt(1);

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suite == card.suite && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Map.entry(suite, rank).hashCode();
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

    /**
     * Returns an SCP representation of a card.
     * @return An SCP representation of a card.
     */
    public String toSCP() {
        String s;

        switch (rank) {
            case TWO -> s = "2";
            case THREE -> s = "3";
            case FOUR -> s = "4";
            case FIVE -> s = "5";
            case SIX -> s = "6";
            case SEVEN -> s = "7";
            case EIGHT -> s = "8";
            case NINE -> s = "9";
            case TEN -> s = "T";
            case JACK -> s = "J";
            case QUEEN -> s = "Q";
            case KING -> s = "K";
            case ACE -> s = "A";
            default -> throw new IllegalStateException("Unexpected value: " + rank);
        }

        switch (suite) {
            case HEARTS -> s += "H";
            case DIAMONDS -> s += "D";
            case CLUBS -> s += "C";
            case SPADES -> s += "S";
            default -> throw new IllegalStateException("Unexpected value: " + suite);
        }

        return s;
    }
}