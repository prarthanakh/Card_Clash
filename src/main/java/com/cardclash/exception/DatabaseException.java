package com.cardclash.exception;

/** Wraps SQLException so no SQL types leak out of the database package. */
public class DatabaseException extends Exception {

    public DatabaseException(String message, Throwable cause) {
        // TODO
    }
}
