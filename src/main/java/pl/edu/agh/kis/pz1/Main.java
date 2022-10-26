package pl.edu.agh.kis.pz1;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Deck d = new Deck();
        Hand h = new Hand();

        for (int i = 0; i < 5; i++) {
            Card c = d.getFromTop();
            h.addCard(c);

        }

        ArrayList<Card> cs = h.getCards();

        for (int i = 0; i < 5; i++) {
            System.out.println(cs.get(i).getName());
        }
    }
}
