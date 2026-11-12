package com.cardclash.model;

/** One row of the players table, used by the leaderboard. */
public class PlayerRecord implements Comparable<PlayerRecord> {

    private int playerId;
    private String username;
    private int wins;
    private int losses;

    public PlayerRecord(int playerId, String username, int wins, int losses) {
        // TODO
    }

    public int getPlayerId() { return 0; }        // TODO
    public String getUsername() { return null; }  // TODO
    public int getWins() { return 0; }            // TODO
    public int getLosses() { return 0; }          // TODO

    public double getWinRate() {
        // TODO
        return 0.0;
    }

    @Override
    public int compareTo(PlayerRecord other) {
        // TODO: more wins first
        return 0;
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }
}
