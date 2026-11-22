package com.cardclash.model;

/** What a card did when played. Used for the combat log (and later the UI). */
public class CardResult {

    private int damageDealt;
    private int healthRestored;
    private int firewallGained;
    private int bandwidthGained;
    private String message;

    public CardResult(String message) {
       this.message=message;
    }

    public int getDamageDealt()
     
    { return damageDealt; 

    }          
    public void setDamageDealt(int damageDealt)
     { 
        this.damageDealt=damageDealt;

     }    
    public int getHealthRestored()
     { 
        return healthRestored; 
    }     
    public void setHealthRestored(int healthRestored) {
        this.healthRestored=healthRestored;
     } 
    public int getFirewallGained()
     { 
        return firewallGained;
     }       
    public void setFirewallGained(int firewallGained) {
        this.firewallGained=firewallGained;
     } 
    public int getBandwidthGained()
     { 
        return bandwidthGained;
      }      
    public void setBandwidthGained(int bandwidthGained) { 
        this.bandwidthGained=bandwidthGained;
    } 
    public String getMessage()
     { 
        return message;
      }        
    public void setMessage(String message) {
        this.message=message;
     }         
}
