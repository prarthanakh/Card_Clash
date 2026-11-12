package com.cardclash.database;

import com.cardclash.exception.DatabaseException;
import java.sql.Connection;

/** Not needed for the console evaluation: build this in layer 3. */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:cardclash.db";

    public static Connection getConnection() throws DatabaseException {
        // TODO: remember PRAGMA foreign_keys = ON
        return null;
    }

    public static void initializeDatabase() throws DatabaseException {
        // TODO
    }

    private static void createTables(Connection connection) {
        // TODO
    }
}
