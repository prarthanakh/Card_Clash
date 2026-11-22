package com.cardclash.database;

import com.cardclash.exception.DatabaseException;
import com.cardclash.model.CardData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    public List<CardData> getAllCards() throws DatabaseException {
        String sql = """
            SELECT card_id, name, type, cost, power, effect, copies, description
            FROM cards ORDER BY card_id
            """;
        List<CardData> cards = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                cards.add(readCard(result));
            }
            return cards;
        } catch (SQLException e) {
            throw new DatabaseException("Could not load cards", e);
        }
    }

    public CardData getCardByName(String name) throws DatabaseException {
        String sql = """
            SELECT card_id, name, type, cost, power, effect, copies, description
            FROM cards WHERE name = ?
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readCard(result) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not find card: " + name, e);
        }
    }

    public void insertCard(CardData card) throws DatabaseException {
        String sql = """
            INSERT INTO cards(name, type, cost, power, effect, copies, description)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, card.getName());
            statement.setString(2, card.getType());
            statement.setInt(3, card.getCost());
            statement.setInt(4, card.getPower());
            statement.setString(5, card.getEffect());
            statement.setInt(6, card.getCopies());
            statement.setString(7, card.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not insert card: " + card.getName(), e);
        }
    }

    public boolean isEmpty() throws DatabaseException {
        String sql = "SELECT 1 FROM cards LIMIT 1";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            return !result.next();
        } catch (SQLException e) {
            throw new DatabaseException("Could not check card table", e);
        }
    }

    public void seedDefaultCards() throws DatabaseException {
        if (!isEmpty()) {
            return;
        }

        List<CardData> defaults = new ArrayList<>();
        defaults.add(new CardData(0, "Ping Flood", "VIRUS", 1, 3,
                null, 3, "Deal 3 damage"));
        defaults.add(new CardData(0, "SQL Injection", "VIRUS", 3, 7,
                null, 3, "Deal 7 damage"));
        defaults.add(new CardData(0, "Zero-Day Exploit", "VIRUS", 5, 12,
                null, 2, "Deal 12 damage"));
        defaults.add(new CardData(0, "Basic Firewall", "FIREWALL", 1, 3,
                null, 3, "Gain 3 Firewall"));
        defaults.add(new CardData(0, "Encryption", "FIREWALL", 3, 8,
                null, 2, "Gain 8 Firewall"));
        defaults.add(new CardData(0, "Hotfix", "PATCH", 2, 5,
                null, 3, "Heal 5 Integrity"));
        defaults.add(new CardData(0, "System Restore", "PATCH", 4, 10,
                null, 2, "Heal 10 Integrity"));
        defaults.add(new CardData(0, "Overclock", "EXPLOIT", 0, 0,
                "OVERCLOCK", 2, "Gain 2 Bandwidth this turn"));
        defaults.add(new CardData(0, "Brute Force", "EXPLOIT", 2, 0,
                "BRUTE_FORCE", 2, "Double your next Virus damage"));
        defaults.add(new CardData(0, "DDoS Attack", "EXPLOIT", 3, 0,
                "DDOS", 2, "Opponent skips their next turn"));

        for (CardData card : defaults) {
            insertCard(card);
        }
    }

    private CardData readCard(ResultSet result) throws SQLException {
        return new CardData(
                result.getInt("card_id"),
                result.getString("name"),
                result.getString("type"),
                result.getInt("cost"),
                result.getInt("power"),
                result.getString("effect"),
                result.getInt("copies"),
                result.getString("description"));
    }
}
