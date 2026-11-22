package com.cardclash.database;

import com.cardclash.exception.DatabaseException;
import com.cardclash.logic.Difficulty;
import com.cardclash.logic.GameResult;
import com.cardclash.model.MatchRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class MatchDAO {
    public int saveMatch(MatchRecord match) throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            return insertMatch(connection, match);
        } catch (SQLException e) {
            throw new DatabaseException("Could not save match", e);
        }
    }

    public void saveCardUsage(int matchId, Map<Integer, Integer> usage)
            throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            insertUsage(connection, matchId, usage);
        } catch (SQLException e) {
            throw new DatabaseException("Could not save card usage", e);
        }
    }

    public void saveMatchWithUsage(MatchRecord match, Map<Integer, Integer> usage)
            throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int matchId = insertMatch(connection, match);
                insertUsage(connection, matchId, usage);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not save match and card usage", e);
        }
    }

    public List<MatchRecord> getMatchesForPlayer(int playerId)
            throws DatabaseException {
        String sql = """
            SELECT match_id, player_id, difficulty, result, turns,
                   final_integrity, played_at
            FROM matches
            WHERE player_id = ?
            ORDER BY played_at DESC
            """;
        List<MatchRecord> matches = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    matches.add(readMatch(result));
                }
            }
            return matches;
        } catch (SQLException e) {
            throw new DatabaseException("Could not load player matches", e);
        }
    }

    public int countMatches(int playerId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM matches WHERE player_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not count player matches", e);
        }
    }

    public String getMostPlayedCard(int playerId) throws DatabaseException {
        String sql = """
            SELECT c.name
            FROM matches m
            JOIN match_card_usage u ON u.match_id = m.match_id
            JOIN cards c ON c.card_id = u.card_id
            WHERE m.player_id = ?
            GROUP BY c.card_id, c.name
            ORDER BY SUM(u.times_played) DESC, c.name ASC
            LIMIT 1
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getString("name") : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find most played card", e);
        }
    }

    private int insertMatch(Connection connection, MatchRecord match)
            throws SQLException {
        String sql = """
            INSERT INTO matches
                (player_id, difficulty, result, turns, final_integrity, played_at)
            VALUES (?, ?, ?, ?, ?, COALESCE(?, CURRENT_TIMESTAMP))
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, match.getPlayerId());
            statement.setString(2, match.getDifficulty().name());
            statement.setString(3, match.getResult().name());
            statement.setInt(4, match.getTurns());
            statement.setInt(5, match.getFinalIntegrity());
            statement.setString(6, match.getPlayedAt());
            statement.executeUpdate();
        }

        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT last_insert_rowid()")) {
            if (result.next()) {
                return result.getInt(1);
            }
            throw new SQLException("Could not get inserted match id");
        }
    }

    private void insertUsage(Connection connection, int matchId,
                             Map<Integer, Integer> usage) throws SQLException {
        String sql = """
            INSERT INTO match_card_usage(match_id, card_id, times_played)
            VALUES (?, ?, ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Map.Entry<Integer, Integer> entry : usage.entrySet()) {
                if (entry.getValue() <= 0) {
                    continue;
                }
                statement.setInt(1, matchId);
                statement.setInt(2, entry.getKey());
                statement.setInt(3, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private MatchRecord readMatch(ResultSet result) throws SQLException {
        return new MatchRecord(
                result.getInt("match_id"),
                result.getInt("player_id"),
                Difficulty.valueOf(result.getString("difficulty")),
                GameResult.valueOf(result.getString("result")),
                result.getInt("turns"),
                result.getInt("final_integrity"),
                result.getString("played_at"));
    }
}
