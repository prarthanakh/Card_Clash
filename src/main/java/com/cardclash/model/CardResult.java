package com.cardclash.model;

/** What a card did when played. Used for the combat log (and later the UI). */
public class CardResult {

    private int damageDealt;
    private int healthRestored;
    private int firewallGained;
    private int bandwidthGained;
    private String message;

    public CardResult(String message) {
        // TODO
    }

    public int getDamageDealt() { return 0; }          // TODO
    public void setDamageDealt(int damageDealt) { }    // TODO
    public int getHealthRestored() { return 0; }       // TODO
    public void setHealthRestored(int healthRestored) { } // TODO
    public int getFirewallGained() { return 0; }       // TODO
    public void setFirewallGained(int firewallGained) { } // TODO
    public int getBandwidthGained() { return 0; }      // TODO
    public void setBandwidthGained(int bandwidthGained) { } // TODO
    public String getMessage() { return null; }        // TODO
    public void setMessage(String message) { }         // TODO
}
