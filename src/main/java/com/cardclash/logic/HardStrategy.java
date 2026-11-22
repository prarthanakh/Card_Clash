package com.cardclash.logic;

import java.util.List;

import com.cardclash.model.Card;
import com.cardclash.model.Player;
import com.cardclash.model.VirusCard;
import com.cardclash.model.PatchCard;
import com.cardclash.model.ExploitCard;
import com.cardclash.model.ExploitEffect;
public class HardStrategy extends BaseStrategy {

    private static final int LOW_INTEGRITY_THRESHOLD = 10;
    private static final int BIG_FIREWALL_THRESHOLD = 5;

    public HardStrategy() {
      
    }

    @Override
    public Card chooseCard(Player self, Player opponent) {
      
       if (getAffordableCards(self).isEmpty()) {
    return null;
}

Card card = findKillingCard(self, opponent);
if (card != null) {
    return card;
}

if (self.getIntegrity() <= LOW_INTEGRITY_THRESHOLD) {
    card = findHealCard(self);
    if (card != null) {
        return card;
    }
}

if (opponent.getFirewall() >= BIG_FIREWALL_THRESHOLD) {
    card = findDisruptionCard(self, opponent);
    if (card != null) {
        return card;
    }
}

return findBestValueCard(self);
    }

    @Override
    public String getName() {
      
       return "Rogue AI";
    }

    private Card findKillingCard(Player self, Player opponent) {
      
     for (Card card : getAffordableCards(self)) {
    if (card instanceof VirusCard) {
        VirusCard virus = (VirusCard) card;
        int damage = calculateDamage(self, virus);

        if (damage >= opponent.getFirewall() + opponent.getIntegrity()) {
            return virus;
        }
    }
}

return null;
    }

    private Card findHealCard(Player self) {
      
        PatchCard best = null;

for (Card card : getAffordableCards(self)) {
    if (card instanceof PatchCard) {
        PatchCard patch = (PatchCard) card;

        if (best == null || patch.getHealAmount() > best.getHealAmount()) {
            best = patch;
        }
    }
}

return best;
    }

    /** DDoS Attack or Brute Force. */
    private Card findDisruptionCard(Player self, Player opponent) {
      
       boolean hasVirus = false;
for (Card card : self.getHand().getCards()) {
    if (card instanceof VirusCard) {
        hasVirus = true;
        break;
    }
}
List<Card> affordable = getAffordableCards(self);

for (Card card : affordable) {
    if (card instanceof ExploitCard
            && ((ExploitCard) card).getEffect() == ExploitEffect.DDOS) {
        return card;
    }
}

if (hasVirus) {
    for (Card card : affordable) {
        if (card instanceof ExploitCard
                && ((ExploitCard) card).getEffect() == ExploitEffect.BRUTE_FORCE) {
            return card;
        }
    }
}

return null;
    }

    private Card findBestValueCard(Player self) {
      Card best = null;

for (Card card : getAffordableCards(self)) {
    if (best == null) {
        best = card;
        continue;
    }

    int valueOrder = Double.compare(calculateValue(card), calculateValue(best));
    if (valueOrder > 0 || (valueOrder == 0 && card.compareTo(best) < 0)) {
        best = card;
    }
}

return best;
    }

    private int calculateDamage(Player self, VirusCard card) {
    
       int damage = card.getDamage();

if (self.isDoubleNextVirus()) {
    damage *= 2;
}

return damage;
    }

    /** Power per Bandwidth. */
    private double calculateValue(Card card) {
      
       if (card.getCost() == 0) {
    return Double.POSITIVE_INFINITY;
}

return (double) card.getPower() / card.getCost();
    }
}
