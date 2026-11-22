package com.cardclash.model;

public class FirewallCard extends Card {

    public FirewallCard(int id, String name, int cost, int firewallAmount, String description) {
        super(id, name, CardType.FIREWALL, cost, firewallAmount, description);
       
    }

    @Override
    public CardResult play(Player user, Player target) {
       int amount=getFirewallAmount();
       user.addFirewall(amount);
       CardResult result=new CardResult(user.getName() + "gained" + amount + "Firewall.");
       result.setFirewallGained(amount);
       return result;
    }

    public int getFirewallAmount() {
        return getPower();
    }
}
