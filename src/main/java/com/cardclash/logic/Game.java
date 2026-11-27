package com.cardclash.logic;

import com.cardclash.exception.InsufficientBandwidthException;
import com.cardclash.exception.InvalidMoveException;
import com.cardclash.model.Card;
import com.cardclash.model.CardResult;
import com.cardclash.model.ComputerPlayer;
import com.cardclash.model.HumanPlayer;
import com.cardclash.model.Player;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private HumanPlayer human;
    private ComputerPlayer computer;
    private Player currentPlayer;
    private Difficulty difficulty;
    private int turnNumber;
    private boolean gameOver;
    private Player winner;
    private List<String> combatLog;
    private List<GameEventListener> listeners;
    private Map<Integer, Integer> cardsPlayedByHuman;   // card id -> times played

    private static int totalGamesPlayed;

    public Game(HumanPlayer human, ComputerPlayer computer, Difficulty difficulty) {
       this.human = human;
this.computer = computer;
this.difficulty = difficulty;
this.combatLog = new ArrayList<>();
this.listeners = new ArrayList<>();
this.cardsPlayedByHuman = new HashMap<>();
    }

    public void startGame() {
        startGame(coinFlip());
    }

    /** Starts the game with the player selected by the setup screen. */
    public void startGame(Player startingPlayer) {
        if (startingPlayer != human && startingPlayer != computer) {
            throw new IllegalArgumentException("Starting player must belong to this game.");
        }

 human.drawStartingHand();
computer.drawStartingHand();

currentPlayer = startingPlayer;
turnNumber = 1;
gameOver = false;
winner = null;
totalGamesPlayed++;

addLog(currentPlayer.getName() + " goes first.");
startTurn();
    }

    private Player coinFlip() {
       
        return Math.random() < 0.5 ? human : computer;
    }

    public void startTurn() {
       if (gameOver) {
    return;
}

currentPlayer.startTurn();
addLog(currentPlayer.getName() + "'s turn starts.");
notifyTurnStarted();
if (currentPlayer.isSkipNextTurn()) {
    Player skippedPlayer = currentPlayer;
    currentPlayer.setSkipNextTurn(false);
    addLog(skippedPlayer.getName() + " skips their turn.");
    endTurn();
}
    }

    public void endTurn() {
        if (gameOver) {
    return;
}

currentPlayer = getOpponent(currentPlayer);
turnNumber++;
startTurn();
       
    }

    public CardResult playCard(Player player, Card card) throws InsufficientBandwidthException, InvalidMoveException {
       if (gameOver) {
    throw new InvalidMoveException("The game is over.");
}

if (player != currentPlayer) {
    throw new InvalidMoveException("It is not this player's turn.");
}
CardResult result = player.playCard(card, getOpponent(player));
addLog(result.getMessage());
notifyCardPlayed(player, card, result);
if (player == human) {
    int count = cardsPlayedByHuman.getOrDefault(card.getId(), 0);
    cardsPlayedByHuman.put(card.getId(), count + 1);
}

checkWinner();
return result;
        
    }

    /** Plays one computer card; returns false when it has nothing left to play. */
    public boolean runComputerStep() {
       
        if (gameOver || currentPlayer != computer) {
    return false;
}

Card card = computer.chooseNextCard(human);
if (card == null) {
    return false;
}

try {
    playCard(computer, card);
    return true;
} catch (InsufficientBandwidthException | InvalidMoveException e) {
    addLog("Computer could not play " + card.getName() + ": " + e.getMessage());
    return false;
}
    }

    public void checkWinner() {
       if (gameOver) {
    return;
}

if (!human.isAlive()) {
    winner = computer;
} else if (!computer.isAlive()) {
    winner = human;
}

if (winner != null) {
    gameOver = true;
    addLog(winner.getName() + " wins!");
    notifyGameOver();
}
    }

    public Player getOpponent(Player player) {
       
      if (player == human) {
    return computer;
}

if (player == computer) {
    return human;
}

throw new IllegalArgumentException("Player is not part of this game");
    }

    public void addListener(GameEventListener listener) {
       if (listener != null && !listeners.contains(listener)) {
    listeners.add(listener);
}
    }

    private void notifyTurnStarted() {
       for (GameEventListener listener : listeners) {
    listener.onTurnStarted(currentPlayer);
}
    }

    private void notifyCardPlayed(Player player, Card card, CardResult result) {
       for (GameEventListener listener : listeners) {
    listener.onCardPlayed(player, card, result);
}
    }

    private void notifyGameOver() {
       for (GameEventListener listener : listeners) {
    listener.onGameOver(winner);
}
    }

    private void addLog(String message) {
       combatLog.add(message);

for (GameEventListener listener : listeners) {
    listener.onLogMessage(message);
}
    }

    public HumanPlayer getHuman() { return human; }             
    public ComputerPlayer getComputer() { return computer; }       
    public Player getCurrentPlayer() { return currentPlayer; }          
    public int getTurnNumber() { return turnNumber; }                   
    public Player getWinner() { return winner; }                 
    public boolean isGameOver() { return gameOver; }              
    public Difficulty getDifficulty() { return difficulty; }         
    public List<String> getCombatLog() { return new ArrayList<>(combatLog); }        
    public Map<Integer, Integer> getCardsPlayedByHuman() { return new HashMap<>(cardsPlayedByHuman); }

    public static int getTotalGamesPlayed() {
       
        return totalGamesPlayed;
    }
}
