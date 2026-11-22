package com.cardclash.model;

public class VirusCard extends Card {

    public VirusCard(int id, String name, int cost, int damage, String description) {
        super(id, name, CardType.VIRUS, cost, damage, description);
        
    }

    @Override
    public CardResult play(Player user, Player target) {
        int damage=getDamage();
        if(user.isDoubleNextVirus()){
            damage*=2;
            user.setDoubleNextVirus(false);
        }
        int actualDamage=target.takeDamage(damage);
        CardResult result=new CardResult(getName() + "dealt" + actualDamage + "damage");
        result.setDamageDealt(actualDamage);
       return result;
    }

    public int getDamage() {
       
        return getPower();
    }
}
