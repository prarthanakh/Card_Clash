package com.cardclash.model;

public class PatchCard extends Card {

    public PatchCard(int id, String name, int cost, int healAmount, String description) {
        super(id, name, CardType.PATCH, cost, healAmount, description);
        
    }

    @Override
    public CardResult play(Player user, Player target) {
       int before=user.getIntegrity();
       user.heal(getHealAmount());
       int restored=user.getIntegrity()-before;
       CardResult result=new CardResult(user.getName() + "restored" + restored + "Integrity.");
       result.setHealthRestored(restored);
       return result;
    }

    public int getHealAmount() {
    
        return getPower();
    }
}
