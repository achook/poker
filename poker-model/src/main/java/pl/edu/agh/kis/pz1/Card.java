package pl.edu.agh.kis.pz1;

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
}