package com.cardclash.database;

import com.cardclash.exception.DatabaseException;
import com.cardclash.model.PlayerRecord;
import java.util.List;

public class PlayerDAO {

    public PlayerRecord getOrCreatePlayer(String username) throws DatabaseException {
        // TODO
        return null;
    }

    public PlayerRecord findByUsername(String username) throws DatabaseException {
        // TODO
        return null;
    }

    public int insertPlayer(String username) throws DatabaseException {
        // TODO
        return 0;
    }

    public void incrementWins(int playerId) throws DatabaseException {
        // TODO
    }

    public void incrementLosses(int playerId) throws DatabaseException {
        // TODO
    }

    public List<PlayerRecord> getTopPlayers() throws DatabaseException {
        // TODO
        return null;
    }

    public List<PlayerRecord> getTopPlayers(int limit) throws DatabaseException {
        // TODO
        return null;
    }
}
