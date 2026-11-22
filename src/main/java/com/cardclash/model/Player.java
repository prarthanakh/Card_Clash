package com.cardclash.model;

import com.cardclash.exception.InsufficientBandwidthException;
import com.cardclash.exception.InvalidMoveException;
import com.cardclash.logic.Playable;

/**
 * Abstract: it implements Playable but leaves takeTurn(Game) for
 * HumanPlayer and ComputerPlayer to define.
 */
public abstract class Player implements Playable {

    public static final int MAX_INTEGRITY = 30;
    public static final int MAX_BANDWIDTH = 10;
    public static final int HAND_LIMIT = 7;
    public static final int STARTING_HAND_SIZE = 5;

    private String name;
    private int integrity;
    private int firewall;
    private int bandwidth;
    private int maxBandwidth;
    private boolean doubleNextVirus;   // set by Brute Force
    private boolean skipNextTurn;      // set by DDoS Attack
    private Deck deck;
    private Hand hand;

    public Player(String name, Deck deck) {
        this(name, deck, MAX_INTEGRITY);

    }

    public Player(String name, Deck deck, int startingIntegrity) {
        this.name=name;
        this.deck=deck;
        this.integrity=startingIntegrity;
        this.hand=new Hand();

    }

    public void startTurn() {
       
        if(maxBandwidth<MAX_BANDWIDTH)
          {  maxBandwidth++;
            
        }
        bandwidth=maxBandwidth;
        clearFirewall();
        drawCard();
        
    }

    public void endTurn() {
      bandwidth=0;
    }

    public void drawStartingHand() {
      drawCard(STARTING_HAND_SIZE);
    }

    public void drawCard() {
        Card card = deck.drawCard();
        if(card==null){
            return;
        }
        if(hand.size() < HAND_LIMIT) {
           hand.addCard(card);}
           else
              {
                deck.discard(card);
        }
    }

    public void drawCard(int count) {
       for(int i=0;i<count;i++)
       {
        drawCard();
       }
    }

    public CardResult playCard(Card card, Player opponent)
            throws InsufficientBandwidthException, InvalidMoveException {
     if (!hand.contains(card)) {
    throw new InvalidMoveException("Card is not in your hand");
}
spendBandwidth(card.getCost());
CardResult result = card.play(this, opponent);
hand.removeCard(card);
deck.discard(card);
        return result;
    }

    public void spendBandwidth(int amount) throws InsufficientBandwidthException {
        if (amount > bandwidth) {
            throw new InsufficientBandwidthException(amount, bandwidth);
        }
        bandwidth -= amount;
    }

    public void gainBandwidth(int amount) {
        bandwidth += amount;
        if (bandwidth > MAX_BANDWIDTH) {
            bandwidth = MAX_BANDWIDTH;
        }
    }

    /** Returns the Integrity actually lost. */
    public int takeDamage(int amount) {
        
        return takeDamage(amount, false);
    }

    /** Returns the Integrity actually lost. */
    public int takeDamage(int amount, boolean ignoreFirewall) {
      int damage = Math.max(0, amount);

if (!ignoreFirewall) {
    int absorbed = Math.min(firewall, damage);
    firewall -= absorbed;
    damage -= absorbed;
}

int oldIntegrity = integrity;
integrity = Math.max(0, integrity - damage);

return oldIntegrity - integrity;
    }

    public void heal(int amount) {
       integrity = Math.min(MAX_INTEGRITY, integrity + amount);
    }

    public void addFirewall(int amount) {
        firewall += amount;
    }

    public void clearFirewall() {
        firewall = 0;
    }

    public boolean isAlive() {
        return integrity > 0;
    }

    public void setDoubleNextVirus(boolean value) {
        doubleNextVirus = value;
     }   
    public boolean isDoubleNextVirus() { return doubleNextVirus; } 
    public void setSkipNextTurn(boolean value) {
        skipNextTurn = value;
     }      
    public boolean isSkipNextTurn() { return skipNextTurn; }   

    public String getName() { return name; }            
    public int getIntegrity() { return integrity; }             
    public int getFirewall() { return firewall; }              
    public int getBandwidth() { return bandwidth; }             
    public int getMaxBandwidth() { return maxBandwidth; }          
    public Hand getHand() { return hand; }              
    public Deck getDeck() { return deck; }             

    @Override
    public String toString() {
       return name + " - Integrity: " + integrity + " , Firewall: " + firewall  + " , Bandwidth: " + bandwidth + "/" + maxBandwidth;
    }
}
       