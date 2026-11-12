package com.cardclash.logic;

import com.cardclash.model.Card;
import com.cardclash.model.Player;
import java.util.List;

/** Shared code for both difficulty levels. chooseCard and getName stay abstract. */
public abstract class BaseStrategy implements Strategy {

    protected List<Card> getAffordableCards(Player self) {
        // TODO
        return null;
    }
}
