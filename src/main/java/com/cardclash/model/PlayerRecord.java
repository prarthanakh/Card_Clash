package com.cardclash.model;


public class PlayerRecord implements Comparable<PlayerRecord> {

    private int playerId;
    private String username;
    private int wins;
    private int losses;

    public PlayerRecord(int playerId, String username, int wins, int losses) {
        this.playerId = playerId;
        this.username = username;
        this.wins = wins;
        this.losses = losses;
    }

    public int getPlayerId() { return this.playerId; }
    public String getUsername() { return this.username; }
    public int getWins() { return this.wins; }
    public int getLosses() { return this.losses; }

    public double getWinRate() {
       int gamesPlayed = wins + losses;
return gamesPlayed == 0 ? 0.0 : (double) wins / gamesPlayed;
      
    }

    @Override
    public int compareTo(PlayerRecord other) {
      return Integer.compare(other.wins, this.wins);
    }

    @Override
    public String toString() {
       
       return username + " (Wins: " + wins + ", Losses: " + losses + ")";
    }
}
