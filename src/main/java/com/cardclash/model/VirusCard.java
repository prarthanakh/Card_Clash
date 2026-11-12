package com.cardclash.model;

public class VirusCard extends Card {

    public VirusCard(int id, String name, int cost, int damage, String description) {
        super(id, name, CardType.VIRUS, cost, damage, description);
        // TODO
    }

    @Override
    public CardResult play(Player user, Player target) {
        // TODO
        return null;
    }

    public int getDamage() {
        // TODO
        return 0;
    }
}
