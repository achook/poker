package pl.edu.agh.kis.pz1;

public class Card implements Comparable<Card> {
    public enum CardSuite {
        HEARTS, TILES, CLOVERS, PIKES;
    }

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

    private CardSuite suite;
    private CardRank rank;

    Card(CardSuite s, CardRank r) {
        suite = s;
        rank = r;
    }

    @Override
    public int compareTo(Card c) {
        return rank.compareTo(c.rank);
    }

    public CardSuite getSuite() {
        return suite;
    }

    public CardRank getRank() {
        return rank;
    }

    public String getName() {
        return getRank().name() + " OF " + getSuite().name();
    }
}
