package com.cardclash.logic;

import com.cardclash.model.Card;
import com.cardclash.model.CardResult;
import com.cardclash.model.Player;

public interface GameEventListener {
    void onTurnStarted(Player player);

    void onCardPlayed(Player player, Card card, CardResult result);

    void onLogMessage(String message);

    void onGameOver(Player winner);
}
