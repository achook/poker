package pl.edu.agh.kis.pz1;

import java.util.ArrayList;

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

    ArrayList<Player> players;
    private int numberOfPlayers;

    private Deck deck;

    private int currentPlayerIndex;
    private int dealerIndex;


    int pot;
    int ante;
    int currentBet;

    private boolean canCheck = true;
    int lastPlayerIndexToRaise = -1;


    public Game(int ante) {
        this.numberOfPlayers = 0;

        players = new ArrayList<Player>();

        deck = new Deck();
        this.ante = ante;

        dealerIndex = 0;
        currentPlayerIndex = 1;

    }

    public void addPlayer(Player p) {
        numberOfPlayers++;
        p.setGame(this);
        p.lastBet = this.ante;
        p.money = 1000-ante;


        var h = new Hand();
        h.addCards(deck.getFromTop(5));

        p.setHand(h);

        players.add(p);

        pot += ante;

        currentBet = ante;
    }

    public Hand getHand(int player) {
        return players.get(player).getHand();
    }

    public int getDealerID() {
        return dealerIndex;
    }

    public int getCurrentPlayerID() {
        return currentPlayerIndex;
    }

    /**
     * Calculates which player is next to play.
     * Updates currentPlayer field.
     */
    private void nextPlayer() {
        while (true) {
            currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;

            if (!players.get(currentPlayerIndex).hasFolded) {
                return;
            }
        }
    }

    /**
     * Calls
     * @param playerID ID of the player who is calling
     */
    public void call(int playerID) {
        players.get(playerID).money -= currentBet - players.get(playerID).lastBet;
        pot += currentBet - players.get(playerID).lastBet;

        players.get(playerID).lastBet = currentBet;
        nextPlayer();
    }

    /**
     * Raises the bet by the given amount.
     * @param playerID ID of the player who is raising.
     * @param amount Amount by which the bet is raised.
     */
    public void raise(int playerID, int amount) {
        players.get(playerID).money -= amount;
        pot += amount;

        currentBet += amount;
        players.get(playerID).lastBet = amount;

        lastPlayerIndexToRaise = playerID;
        canCheck = false;

        nextPlayer();
    }

    /**
     * Folds the player's hand.
     * @param playerID ID of the player who is folding.
     */
    public void fold(int playerID) {
        players.get(playerID).hasFolded = true;

        nextPlayer();
    }

    /**
     * Checks
     * @param playerID ID of the player who is checking.
     */
    public void check(int playerID) {
        nextPlayer();
    }

    public void makeMove(int playerID, Move move) {
        switch (move) {
            case FOLD -> fold(playerID);
            case CHECK -> check(playerID);
            case CALL -> call(playerID);
            default -> throw new IllegalStateException("Unexpected value: " + move);
        }
    }

    public void makeMove(int playerID, Move move, int amount) {
        switch (move) {
            case RAISE -> raise(playerID, amount);
            default -> makeMove(playerID, move);
        }
    }

    /**
     * Gets the players who are still in the game.
     * @return List of players who are still in the game.
     */
    public ArrayList<Player> getPlayersInGame() {
        var playersInGame = new ArrayList<Player>();
        for (var player : players) {
            if (!player.hasFolded) {
                playersInGame.add(player);
            }
        }

        return playersInGame;
    }

    /**
     * Ends the first round of betting.
     * Prepares the game for the second round.
     */
    void endFirstRound() {
        lastPlayerIndexToRaise = -1;
        currentPlayerIndex = dealerIndex;
        nextPlayer();
    }

    /**
     * Replaces the cards of the given player.
     * @param playerID ID of the player who is replacing the cards.
     * @param cardIndexes Indexes of the cards to be replaced.
     */
    void replaceCards(int playerID, ArrayList<Integer> cardIndexes) {

        players.get(playerID).getHand().removeCards(cardIndexes);
        players.get(playerID).getHand().addCards(deck.getFromTop(cardIndexes.size()));
    }

    /**
     * Ends the second round of betting.
     * Prepares the game for the showdown.
     */
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

        players.get(bestPlayerID).money += pot;
        return bestPlayerID;
    }

    /**
     * Checks if the game is over.
     * @return True if the game is over, false otherwise.
     */
    public boolean isRoundFinished() {
        if (getPlayersInGame().size() == 1) {
            return true;
        }

        if (lastPlayerIndexToRaise == currentPlayerIndex) {
            return true;
        }

        return false;
    }

    public int getBalance(int playerID) {
        return players.get(playerID).money;
    }

    /**
     * Checks if the given move is valid for the given player at the moment the method is called.
     * @param playerID ID of the player who is making the move.
     * @param move Move to be checked.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isMoveLegal(int playerID, Move move) {
        if (playerID != currentPlayerIndex) {
            return false;
        }

        switch (move) {
            case FOLD -> {
                return true;
            }
            case CHECK -> {
                return canCheck;
            }
            case CALL -> {
                return players.get(playerID).money >= currentBet - players.get(playerID).lastBet;
            }
            case RAISE -> {
                return players.get(playerID).money >= currentBet - players.get(playerID).lastBet + 1;
            }
        }

        return false;
    }

    public void prepareNewGame() {
        deck = new Deck();

        for (var player : players) {
            player.hasFolded = false;
            player.lastBet = 0;
        }

        dealerIndex = (dealerIndex + 1) % numberOfPlayers;
        currentPlayerIndex = dealerIndex;
        nextPlayer();

        currentBet = 0;
        pot = 0;

        canCheck = true;
        lastPlayerIndexToRaise = -1;
    }

    public int getPot() {
        return pot;
    }
}
