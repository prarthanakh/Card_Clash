package com.cardclash.model;

import com.cardclash.logic.Game;

public class HumanPlayer extends Player {

    private int databaseId;

    public HumanPlayer(String name, Deck deck) {
        super(name, deck);
        
    }

    @Override
    public void takeTurn(Game game) {
        // Human actions are handled by the console or UI.
        
    }

    public int getDatabaseId() {
      
        return databaseId;
    }

    public void setDatabaseId(int id) {
        this.databaseId = id;
    }
}
