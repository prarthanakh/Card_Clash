package com.cardclash.model;

import com.cardclash.logic.Difficulty;
import com.cardclash.logic.GameResult;

/** One row of the matches table. */
public class MatchRecord {

    private int matchId;
    private int playerId;
    private Difficulty difficulty;
    private GameResult result;
    private int turns;
    private int finalIntegrity;
    private String playedAt;

    public MatchRecord(int matchId, int playerId, Difficulty difficulty, GameResult result,
                       int turns, int finalIntegrity, String playedAt) {
        // TODO
    }

    public int getMatchId() { return 0; }              // TODO
    public int getPlayerId() { return 0; }             // TODO
    public Difficulty getDifficulty() { return null; } // TODO
    public GameResult getResult() { return null; }     // TODO
    public int getTurns() { return 0; }                // TODO
    public int getFinalIntegrity() { return 0; }       // TODO
    public String getPlayedAt() { return null; }       // TODO
}
