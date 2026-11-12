package com.cardclash.logic;

import com.cardclash.model.Card;
import com.cardclash.model.Player;

public interface Strategy {
    /** Returns null to end the turn. */
    Card chooseCard(Player self, Player opponent);

    String getName();
}
