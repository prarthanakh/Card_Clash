package com.cardclash.database;

import com.cardclash.exception.DatabaseException;
import com.cardclash.model.MatchRecord;
import java.util.List;
import java.util.Map;

public class MatchDAO {

    /** Returns the new match_id. */
    public int saveMatch(MatchRecord match) throws DatabaseException {
        // TODO
        return 0;
    }

    public void saveCardUsage(int matchId, Map<Integer, Integer> usage) throws DatabaseException {
        // TODO
    }

    /** Does both inside one transaction. */
    public void saveMatchWithUsage(MatchRecord match, Map<Integer, Integer> usage)
            throws DatabaseException {
        // TODO
    }

    public List<MatchRecord> getMatchesForPlayer(int playerId) throws DatabaseException {
        // TODO
        return null;
    }

    public int countMatches(int playerId) throws DatabaseException {
        // TODO
        return 0;
    }

    public String getMostPlayedCard(int playerId) throws DatabaseException {
        // TODO
        return null;
    }
}
