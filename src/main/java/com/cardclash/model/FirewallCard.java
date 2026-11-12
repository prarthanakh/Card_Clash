package com.cardclash.model;

public class FirewallCard extends Card {

    public FirewallCard(int id, String name, int cost, int firewallAmount, String description) {
        super(id, name, CardType.FIREWALL, cost, firewallAmount, description);
        // TODO
    }

    @Override
    public CardResult play(Player user, Player target) {
        // TODO
        return null;
    }

    public int getFirewallAmount() {
        // TODO
        return 0;
    }
}
