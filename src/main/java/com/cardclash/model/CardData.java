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

    public CardData(int id, String name, String type, int cost, int power,String effect, int copies, String description) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.cost = cost;
            this.power = power;
            this.effect = effect;
            this.copies = copies;
            this.description = description;
    }

    public int getId() { return id; }              
    public String getName() { return name; }      
    public String getType() { return type; }      
    public int getCost() { return cost; }            
    public int getPower() { return power; }           
    public String getEffect() { return effect; }    
    public int getCopies() { return copies; }          
    public String getDescription() { return description; }
}
