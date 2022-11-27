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
    private int lastToRaise = -1;

    private Round currentRound = Round.FIRST;

    public Game(int numberOfPlayers, int ante) {
        this.numberOfPlayers = numberOfPlayers;

        players = new Player[numberOfPlayers];

        deck = new Deck();

        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player();
            players[i].setGame(this);
            players[i].lastBet = ante;
            players[i].money = 1000;

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

    private void nextPlayer() {
        System.out.println("Before: " + currentPlayer);
        while (true) {
            currentPlayer = (currentPlayer + 1) % numberOfPlayers;

            if (!players[currentPlayer].hasFolded) {
                System.out.println("After: " + currentPlayer);
                return;
            }
        }
    }

    public void call(int playerID) {
        players[playerID].money -= (currentBet - players[playerID].lastBet);
        pot += (currentBet - players[playerID].lastBet);
        players[playerID].lastBet = currentBet;

        nextPlayer();
    }

    public void raise(int playerID, int amount) {
        players[playerID].money -= amount;
        pot += amount;
        currentBet += amount;
        players[playerID].lastBet = amount;
        lastToRaise = playerID;

        nextPlayer();
    }

    public void fold(int playerID) {
        players[playerID].hasFolded = true;

        nextPlayer();
    }

    public void check(int playerID) {
        nextPlayer();
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
        currentRound = Round.SECOND;
        lastToRaise = -1;
    }

    void replaceCards(int playerID, ArrayList<Integer> cardIndexes) {

        players[playerID].getHand().removeCards(cardIndexes);
        players[playerID].getHand().addCards(deck.getFromTop(cardIndexes.size()));
    }

    public int endSecondRound() {
        var bestHand = getPlayersInGame().get(0).getHand();
        var bestPlayerID = 0;

        for (int i = 1; i < getPlayersInGame().size(); i++) {
            var player = getPlayersInGame().get(i);
            var hand = player.getHand();

            if (hand.compareTo(bestHand) > 0) {
                bestHand = hand;
                bestPlayerID = i;
            }
        }

        players[bestPlayerID].money += pot;
        return bestPlayerID;
    }

    public boolean isRoundFinished() {
        if (getPlayersInGame().size() == 1) {
            return true;
        }

        if (lastToRaise == currentPlayer) {
            return true;
        }

        return false;
    }

    public int getBalance(int playerID) {
        return players[playerID].money;
    }


}
