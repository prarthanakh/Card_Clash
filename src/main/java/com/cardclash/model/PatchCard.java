package com.cardclash.model;

public class PatchCard extends Card {

    public PatchCard(int id, String name, int cost, int healAmount, String description) {
        super(id, name, CardType.PATCH, cost, healAmount, description);
        // TODO
    }

    @Override
    public CardResult play(Player user, Player target) {
        // TODO
        return null;
    }

    public int getHealAmount() {
        // TODO
        return 0;
    }
}
