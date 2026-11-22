package com.cardclash.model;

import com.cardclash.logic.Game;
import com.cardclash.logic.Strategy;

public class ComputerPlayer extends Player {

    private Strategy strategy;

    public ComputerPlayer(String name, Deck deck, Strategy strategy) {
        super(name, deck);
       this.strategy = strategy;
       
    }

    @Override
    public void takeTurn(Game game) {
        while (game.runComputerStep()) {
    // Keep taking steps while a card is played.
}
       
    }

    public Card chooseNextCard(Player opponent) {
       
        return strategy.chooseCard(this, opponent);
    }

    public Strategy getStrategy() {
       
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;

    }
}
