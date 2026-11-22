package com.cardclash.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Deck {

    private ArrayList<Card> drawPile;
    private ArrayList<Card> discardPile;

    public Deck(List<Card> cards) {
        drawPile=new ArrayList<>(cards);
        discardPile=new ArrayList<>();
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(drawPile);
    }

    public Card drawCard() {
        if(drawPile.isEmpty()){
            reshuffleDiscardPile();
        }
        if(drawPile.isEmpty()){
            return null;
        }
        return drawPile.remove(drawPile.size()-1);
    }

    public List<Card> drawCard(int count) {
       List<Card> drawn=new ArrayList<>();
       for(int i=0;i<count;i++){
        Card card=drawCard();
        if(card==null){
            break;
        }
        drawn.add(card);
    }
    return drawn;
        }

    public void discard(Card card) {
        discardPile.add(card);
    }

    private void reshuffleDiscardPile() {
        drawPile.addAll(discardPile);
        discardPile.clear();
        shuffle();
    }

    public int size() {
        return drawPile.size() + discardPile.size();
    }

    public int getDiscardSize() {
        return discardPile.size();
    }

    public boolean isEmpty() {
        return drawPile.isEmpty() && discardPile.isEmpty();
    }
}
