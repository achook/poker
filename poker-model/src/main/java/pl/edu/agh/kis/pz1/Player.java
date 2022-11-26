package pl.edu.agh.kis.pz1;

public class Player {
    private String name;
    private int money;
    private Hand hand;
    private boolean hasFolded;
    private boolean currentBet;

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
