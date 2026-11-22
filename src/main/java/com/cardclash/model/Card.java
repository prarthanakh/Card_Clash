package com.cardclash.model;

public abstract class Card implements Comparable<Card> {

    private int id;
    private String name;
    private CardType type;
    private int cost;
    private int power;
    private String description;

    public Card(int id, String name, CardType type, int cost, int power, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.power = power;
        this.description = description;
    }

    /** Each subclass decides what the card does. */
    public abstract CardResult play(Player user, Player target);

    public boolean isAffordableFor(Player player) {
        return player != null && player.getBandwidth() >= cost;
    }

    @Override
    public int compareTo(Card other) {
        int costOrder = Integer.compare(cost, other.cost);
        return costOrder != 0 ? costOrder : name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + " (" + type + ") - Cost: " + cost + ", Power: " + power
                + " - " + description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public CardType getType() { return type; }
    public int getCost() { return cost; }
    public int getPower() { return power; }
    public String getDescription() { return description; }
}
