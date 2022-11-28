package pl.edu.agh.kis.pz1;

import java.nio.channels.SelectionKey;

public class Player {
    int money;
    private Hand hand;
    boolean hasFolded;
    int lastBet;
    int id;

    SelectionKey selectionKey;

    private Game game;

    public Player(SelectionKey selectionKey, int index, Game game) {
        this.selectionKey = selectionKey;
        this.game = game;
        this.id = index;
    }


    public int getID() {
        return id;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

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
