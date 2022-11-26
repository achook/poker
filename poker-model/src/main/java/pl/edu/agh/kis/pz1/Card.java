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