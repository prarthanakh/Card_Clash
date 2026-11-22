package com.cardclash.database;

import com.cardclash.exception.DatabaseException;
import com.cardclash.model.PlayerRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    public PlayerRecord getOrCreatePlayer(String username)
            throws DatabaseException {
        PlayerRecord player = findByUsername(username);
        if (player != null) {
            return player;
        }

        insertPlayer(username);
        return findByUsername(username);
    }

    public PlayerRecord findByUsername(String username)
            throws DatabaseException {
        String sql = """
            SELECT player_id, username, wins, losses
            FROM players WHERE username = ?
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readPlayer(result) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find player: " + username, e);
        }
    }

    public int insertPlayer(String username) throws DatabaseException {
        String insert = "INSERT INTO players(username) VALUES (?)";
        String select = "SELECT player_id FROM players WHERE username = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(insert)) {
            statement.setString(1, username);
            statement.executeUpdate();

            try (PreparedStatement query = connection.prepareStatement(select)) {
                query.setString(1, username);
                try (ResultSet result = query.executeQuery()) {
                    if (result.next()) {
                        return result.getInt("player_id");
                    }
                    throw new SQLException("Inserted player was not found");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not insert player: " + username, e);
        }
    }

    public void incrementWins(int playerId) throws DatabaseException {
        incrementCounter("wins", playerId);
    }

    public void incrementLosses(int playerId) throws DatabaseException {
        incrementCounter("losses", playerId);
    }

    public List<PlayerRecord> getTopPlayers() throws DatabaseException {
        return getTopPlayers(10);
    }

    public List<PlayerRecord> getTopPlayers(int limit) throws DatabaseException {
        List<PlayerRecord> players = new ArrayList<>();
        if (limit <= 0) {
            return players;
        }

        String sql = """
            SELECT player_id, username, wins, losses
            FROM players
            ORDER BY wins DESC, losses ASC, username ASC
            LIMIT ?
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    players.add(readPlayer(result));
                }
            }
            return players;
        } catch (SQLException e) {
            throw new DatabaseException("Could not load leaderboard", e);
        }
    }

    private void incrementCounter(String column, int playerId)
            throws DatabaseException {
        String sql = "UPDATE players SET " + column + " = " + column
                + " + 1 WHERE player_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update player record", e);
        }
    }

    private PlayerRecord readPlayer(ResultSet result) throws SQLException {
        return new PlayerRecord(
                result.getInt("player_id"),
                result.getString("username"),
                result.getInt("wins"),
                result.getInt("losses"));
    }
}
