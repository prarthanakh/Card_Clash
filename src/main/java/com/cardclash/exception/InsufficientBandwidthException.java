package com.cardclash.exception;

/** Thrown by Player.spendBandwidth when the player cannot afford a card. */
public class InsufficientBandwidthException extends Exception {

    private int required;
    private int available;

    public InsufficientBandwidthException(int required, int available) {
        // TODO
    }

    public int getRequired() {
        // TODO
        return 0;
    }

    public int getAvailable() {
        // TODO
        return 0;
    }

    @Override
    public String getMessage() {
        // TODO
        return null;
    }
}
