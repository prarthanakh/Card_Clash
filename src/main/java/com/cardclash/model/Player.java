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
        // TODO
    }

    public Player(String name, Deck deck, int startingIntegrity) {
        // TODO
    }

    public void startTurn() {
        // TODO: maxBandwidth +1 (cap 10), refill, clear firewall, draw 1
    }

    public void endTurn() {
        // TODO
    }

    public void drawStartingHand() {
        // TODO
    }

    public void drawCard() {
        // TODO
    }

    public void drawCard(int count) {
        // TODO
    }

    public CardResult playCard(Card card, Player opponent)
            throws InsufficientBandwidthException, InvalidMoveException {
        // TODO
        return null;
    }

    public void spendBandwidth(int amount) throws InsufficientBandwidthException {
        // TODO
    }

    public void gainBandwidth(int amount) {
        // TODO
    }

    /** Returns the Integrity actually lost. */
    public int takeDamage(int amount) {
        // TODO
        return 0;
    }

    /** Returns the Integrity actually lost. */
    public int takeDamage(int amount, boolean ignoreFirewall) {
        // TODO
        return 0;
    }

    public void heal(int amount) {
        // TODO
    }

    public void addFirewall(int amount) {
        // TODO
    }

    public void clearFirewall() {
        // TODO
    }

    public boolean isAlive() {
        // TODO
        return false;
    }

    public void setDoubleNextVirus(boolean value) { }   // TODO
    public boolean isDoubleNextVirus() { return false; } // TODO
    public void setSkipNextTurn(boolean value) { }      // TODO
    public boolean isSkipNextTurn() { return false; }   // TODO

    public String getName() { return null; }            // TODO
    public int getIntegrity() { return 0; }             // TODO
    public int getFirewall() { return 0; }              // TODO
    public int getBandwidth() { return 0; }             // TODO
    public int getMaxBandwidth() { return 0; }          // TODO
    public Hand getHand() { return null; }              // TODO
    public Deck getDeck() { return null; }              // TODO

    @Override
    public String toString() {
        // TODO
        return null;
    }

    // takeTurn(Game game) comes from Playable: define it in the subclasses.
}
