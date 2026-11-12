package com.cardclash.model;

/** One row of the cards table, before it becomes a real Card object. */
public class CardData {

    private int id;
    private String name;
    private String type;
    private int cost;
    private int power;
    private String effect;
    private int copies;
    private String description;

    public CardData(int id, String name, String type, int cost, int power,
                    String effect, int copies, String description) {
        // TODO
    }

    public int getId() { return 0; }               // TODO
    public String getName() { return null; }       // TODO
    public String getType() { return null; }       // TODO
    public int getCost() { return 0; }             // TODO
    public int getPower() { return 0; }            // TODO
    public String getEffect() { return null; }     // TODO
    public int getCopies() { return 0; }           // TODO
    public String getDescription() { return null; } // TODO
}
