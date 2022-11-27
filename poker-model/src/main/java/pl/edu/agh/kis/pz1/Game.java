package pl.edu.agh.kis.pz1;


import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

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
    private int currentPlayer;

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

    public Hand getHand(int player) {
        return players[player].getHand();
    }

    public int getDealerID() {
        return dealer;
    }

    public int getCurrentPlayerID() {
        return currentPlayer;
    }

    public void call(int playerID) {

    }

    public void raise(int playerID, int amount) {

    }

    public void fold(int playerID) {
        players[playerID].hasFolded = true;
    }

    public void check(int playerID) {

    }

    public ArrayList<Player> getPlayersInGame() {
        var playersInGame = new ArrayList<Player>();
        for (var player : players) {
            if (!player.hasFolded) {
                playersInGame.add(player);
            }
        }

        return playersInGame;
    }

    void endFirstRound() {
        for (var player : players) {
            player.hasFolded = false;
        }
    }

    void replaceCards(int playerID, ArrayList<Integer> cardIndexes) {

        players[playerID].getHand().removeCards(cardIndexes);
        players[playerID].getHand().addCards(deck.getFromTop(cardIndexes.size()));
    }

    public Player endSecondRound() {
        var bestHand = getPlayersInGame().get(0).getHand();
        var bestPlayer = players[0];

        for (var player : getPlayersInGame()) {
            if (player.getHand().compareTo(bestHand) > 0) {
                bestHand = player.getHand();
                bestPlayer = player;
            }
        }

        return bestPlayer;
    }


}
