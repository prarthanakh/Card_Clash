package com.cardclash.model;

import java.util.ArrayList;
import java.util.List;


public class CardFactory {

    public static Card createCard(CardData data) {
       String type=data.getType().trim().toUpperCase();
       switch(type){
              case "VIRUS":
                 return new VirusCard(data.getId(),data.getName(),data.getCost(),data.getPower(),data.getDescription());
                case "FIREWALL":
                 return new FirewallCard(data.getId(),data.getName(),data.getCost(),data.getPower(),data.getDescription());
                case "EXPLOIT":
                 ExploitEffect effect=parseEffect(data.getEffect());
                 return new ExploitCard(data.getId(),data.getName(),data.getCost(),effect,data.getDescription());
                case "PATCH":
                 return new PatchCard(data.getId(), data.getName(), data.getCost(),data.getPower(), data.getDescription());
                 default:
                 throw new IllegalArgumentException("Unknown card type: " + data.getType());
       }
    }

    public static List<Card> buildToolkit(List<CardData> library) {
       List<Card> toolkit = new ArrayList<>();

for (CardData data : library) {
    for (int i = 0; i < data.getCopies(); i++) {
        toolkit.add(createCard(data));
    }
}

return toolkit;
    }

    private static ExploitEffect parseEffect(String text) {

        String normalized = text.trim().toUpperCase().replace(' ', '_');

            switch (normalized) {
            case "OVERCLOCK":
                return ExploitEffect.OVERCLOCK;
            case "BRUTE_FORCE":
                return ExploitEffect.BRUTE_FORCE;
            case "DDOS":
            case "DDOS_ATTACK":
                return ExploitEffect.DDOS;
            default:
                throw new IllegalArgumentException("Unknown exploit effect: " + text);
        }
    }
}
