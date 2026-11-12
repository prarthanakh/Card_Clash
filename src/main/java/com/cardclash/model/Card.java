package com.cardclash.model;

public abstract class Card implements Comparable<Card> {

    private int id;
    private String name;
    private CardType type;
    private int cost;
    private int power;
    private String description;

    public Card(int id, String name, CardType type, int cost, int power, String description) {
        // TODO
    }

    /** Each subclass decides what the card does. */
    public abstract CardResult play(Player user, Player target);

    public boolean isAffordableFor(Player player) {
        // TODO
        return false;
    }

    @Override
    public int compareTo(Card other) {
        // TODO: by cost, then name
        return 0;
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }

    public int getId() { return 0; }              // TODO
    public String getName() { return null; }      // TODO
    public CardType getType() { return null; }    // TODO
    public int getCost() { return 0; }            // TODO
    public int getPower() { return 0; }           // TODO
    public String getDescription() { return null; } // TODO
}
