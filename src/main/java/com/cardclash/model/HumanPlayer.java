package com.cardclash.model;

import com.cardclash.logic.Game;

public class HumanPlayer extends Player {

    private int databaseId;

    public HumanPlayer(String name, Deck deck) {
        super(name, deck);
        // TODO
    }

    @Override
    public void takeTurn(Game game) {
        // TODO
    }

    public int getDatabaseId() {
        // TODO
        return 0;
    }

    public void setDatabaseId(int id) {
        // TODO
    }
}
