package pl.edu.agh.kis.pz1;

public class Player {
    private String name;
    int money;
    private Hand hand;
    boolean hasFolded;
    private boolean currentBet;
    int lastBet;

    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }
}
