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
        // TODO
    }

    public void startGame() {
        // TODO
    }

    private Player coinFlip() {
        // TODO
        return null;
    }

    public void startTurn() {
        // TODO
    }

    public void endTurn() {
        // TODO
    }

    public CardResult playCard(Player player, Card card)
            throws InsufficientBandwidthException, InvalidMoveException {
        // TODO
        return null;
    }

    /** Plays one computer card; returns false when it has nothing left to play. */
    public boolean runComputerStep() {
        // TODO
        return false;
    }

    public void checkWinner() {
        // TODO
    }

    public Player getOpponent(Player player) {
        // TODO
        return null;
    }

    public void addListener(GameEventListener listener) {
        // TODO
    }

    private void notifyTurnStarted() {
        // TODO
    }

    private void notifyCardPlayed(Player player, Card card, CardResult result) {
        // TODO
    }

    private void notifyGameOver() {
        // TODO
    }

    private void addLog(String message) {
        // TODO
    }

    public HumanPlayer getHuman() { return null; }              // TODO
    public ComputerPlayer getComputer() { return null; }        // TODO
    public Player getCurrentPlayer() { return null; }           // TODO
    public int getTurnNumber() { return 0; }                    // TODO
    public Player getWinner() { return null; }                  // TODO
    public boolean isGameOver() { return false; }               // TODO
    public Difficulty getDifficulty() { return null; }          // TODO
    public List<String> getCombatLog() { return null; }         // TODO
    public Map<Integer, Integer> getCardsPlayedByHuman() { return null; } // TODO

    public static int getTotalGamesPlayed() {
        // TODO
        return 0;
    }
}
