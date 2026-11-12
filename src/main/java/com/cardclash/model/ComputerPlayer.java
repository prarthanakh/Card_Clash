package com.cardclash.model;

import com.cardclash.logic.Game;
import com.cardclash.logic.Strategy;

public class ComputerPlayer extends Player {

    private Strategy strategy;

    public ComputerPlayer(String name, Deck deck, Strategy strategy) {
        super(name, deck);
        // TODO
    }

    @Override
    public void takeTurn(Game game) {
        // TODO
    }

    /** Returns null when there is nothing left worth playing. */
    public Card chooseNextCard(Player opponent) {
        // TODO
        return null;
    }

    public Strategy getStrategy() {
        // TODO
        return null;
    }

    public void setStrategy(Strategy strategy) {
        // TODO
    }
}
