package com.cardclash.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private ArrayList<Card> drawPile;
    private ArrayList<Card> discardPile;

    public Deck(List<Card> cards) {
        // TODO
    }

    public void shuffle() {
        // TODO
    }

    public Card drawCard() {
        // TODO
        return null;
    }

    public List<Card> drawCard(int count) {
        // TODO
        return null;
    }

    public void discard(Card card) {
        // TODO
    }

    private void reshuffleDiscardPile() {
        // TODO
    }

    public int size() {
        // TODO
        return 0;
    }

    public int getDiscardSize() {
        // TODO
        return 0;
    }

    public boolean isEmpty() {
        // TODO
        return false;
    }
}
