package com.cardclash.model;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private ArrayList<Card> cards;

    public Hand() {
        cards=new ArrayList<>();
    }

    public boolean addCard(Card card) {
     if(cards.size()>=Player.HAND_LIMIT){
        return false;
     }
        cards.add(card);
        return true;
    }

    public void removeCard(Card card) {
       cards.remove(card);
    }

    public boolean contains(Card card) {
        
        return cards.contains(card);
    }

    public Card getCardAt(int index) {
        
        return cards.get(index);
    }

    public List<Card> getCards() {
       
       return new ArrayList<>(cards);
    }

    public int size() {
        return cards.size();
    }

    public boolean isFull() {
        
        return cards.size()>=Player.HAND_LIMIT;
    }

    public void clear() {
        cards.clear();
    }
}

