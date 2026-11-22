package com.cardclash.logic;

import com.cardclash.model.Card;
import com.cardclash.model.Player;

import java.util.List;
import java.util.Random;

public class EasyStrategy extends BaseStrategy {

    private Random random;

    public EasyStrategy() {
       random = new Random();
    }

    @Override
    public Card chooseCard(Player self, Player opponent) {
       List<Card> affordable = getAffordableCards(self);

if (affordable.isEmpty()) {
    return null;
}

return affordable.get(random.nextInt(affordable.size()));
        
    }

    @Override
    public String getName() {
       
       return "Script Kiddie";
    }
}
