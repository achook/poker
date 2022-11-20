package pl.edu.agh.kis.pz1;


/**
 * Represents the current state of the game of poker.
 */
public class Game {
    public enum Move {
        FOLD,
        CHECK,
        CALL,
        RAISE
    }

    public enum Round {
        FIRST,
        SECOND
    }

    private Player[] players;
    private int dealer;

    private int numberOfPlayers;

    private int pot;
    private int currentBet;
    private Deck deck;

    private boolean canCheck = true;

    private Round currentRound = Round.FIRST;

    public Game(int numberOfPlayers, int ante) {
        this.numberOfPlayers = numberOfPlayers;

        players = new Player[numberOfPlayers];

        deck = new Deck();

        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player();
            players[i].setGame(this);

            var h = new Hand();
            h.addCards(deck.getFromTop(5));

            players[i].setHand(h);
        }

        dealer = 0;

        pot = ante * numberOfPlayers;

    }
}
