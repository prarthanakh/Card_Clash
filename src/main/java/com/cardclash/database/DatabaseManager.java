package com.cardclash.database;

import com.cardclash.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:cardclash.db";

    private DatabaseManager() {
    }

    public static Connection getConnection() throws DatabaseException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Could not connect to the database", e);
        }
    }

    public static void initializeDatabase() throws DatabaseException {
        try (Connection connection = getConnection()) {
            createTables(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Could not initialize the database", e);
        }

        new CardDAO().seedDefaultCards();
    }

    private static void createTables(Connection connection)
            throws DatabaseException {
        String[] sql = {
            """
            CREATE TABLE IF NOT EXISTS cards (
                card_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                type TEXT NOT NULL CHECK (type IN
                    ('VIRUS','FIREWALL','PATCH','EXPLOIT')),
                cost INTEGER NOT NULL CHECK (cost BETWEEN 0 AND 10),
                power INTEGER NOT NULL DEFAULT 0 CHECK (power >= 0),
                effect TEXT CHECK (effect IN
                    ('OVERCLOCK','BRUTE_FORCE','DDOS')),
                copies INTEGER NOT NULL DEFAULT 1 CHECK (copies >= 1),
                description TEXT
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS players (
                player_id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                wins INTEGER NOT NULL DEFAULT 0,
                losses INTEGER NOT NULL DEFAULT 0,
                created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS matches (
                match_id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_id INTEGER NOT NULL,
                difficulty TEXT NOT NULL CHECK (difficulty IN ('EASY','HARD')),
                result TEXT NOT NULL CHECK (result IN ('WIN','LOSS')),
                turns INTEGER NOT NULL,
                final_integrity INTEGER NOT NULL,
                played_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (player_id) REFERENCES players(player_id)
                    ON DELETE CASCADE
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS match_card_usage (
                match_id INTEGER NOT NULL,
                card_id INTEGER NOT NULL,
                times_played INTEGER NOT NULL CHECK (times_played >= 1),
                PRIMARY KEY (match_id, card_id),
                FOREIGN KEY (match_id) REFERENCES matches(match_id)
                    ON DELETE CASCADE,
                FOREIGN KEY (card_id) REFERENCES cards(card_id)
            )
            """,
            "CREATE INDEX IF NOT EXISTS idx_matches_player ON matches(player_id)"
        };

        try (Statement statement = connection.createStatement()) {
            for (String query : sql) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not create database tables", e);
        }
    }
}
