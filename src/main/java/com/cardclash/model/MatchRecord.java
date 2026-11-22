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
       this.matchId = matchId;
this.playerId = playerId;
this.difficulty = difficulty;
this.result = result;
this.turns = turns;
this.finalIntegrity = finalIntegrity;
this.playedAt = playedAt;
    }

    public int getMatchId() { return matchId; }             
    public int getPlayerId() { return playerId; }            
    public Difficulty getDifficulty() { return difficulty; }
    public GameResult getResult() { return result; }    
    public int getTurns() { return turns; }               
    public int getFinalIntegrity() { return finalIntegrity; }      
    public String getPlayedAt() { return playedAt; }      
}
