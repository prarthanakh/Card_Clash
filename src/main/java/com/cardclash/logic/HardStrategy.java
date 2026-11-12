package com.cardclash.logic;

import com.cardclash.model.Card;
import com.cardclash.model.Player;
import com.cardclash.model.VirusCard;

public class HardStrategy extends BaseStrategy {

    private static final int LOW_INTEGRITY_THRESHOLD = 10;
    private static final int BIG_FIREWALL_THRESHOLD = 5;

    public HardStrategy() {
        // TODO
    }

    @Override
    public Card chooseCard(Player self, Player opponent) {
        // TODO
        return null;
    }

    @Override
    public String getName() {
        // TODO: "Rogue AI"
        return null;
    }

    private Card findKillingCard(Player self, Player opponent) {
        // TODO
        return null;
    }

    private Card findHealCard(Player self) {
        // TODO
        return null;
    }

    /** DDoS Attack or Brute Force. */
    private Card findDisruptionCard(Player self, Player opponent) {
        // TODO
        return null;
    }

    private Card findBestValueCard(Player self) {
        // TODO
        return null;
    }

    private int calculateDamage(Player self, VirusCard card) {
        // TODO: remember Brute Force doubles it
        return 0;
    }

    /** Power per Bandwidth. */
    private double calculateValue(Card card) {
        // TODO
        return 0.0;
    }
}
