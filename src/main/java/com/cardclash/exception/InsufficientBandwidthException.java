package com.cardclash.exception;

/** Thrown by Player.spendBandwidth when the player cannot afford a card. */
public class InsufficientBandwidthException extends Exception {

    private int required;
    private int available;

    public InsufficientBandwidthException(int required, int available) {
       this.required = required;
this.available = available;
    }

    public int getRequired() {
        // TODO
        return required;
    }

    public int getAvailable() {
        // TODO
        return available;
    }

    @Override
    public String getMessage() {
        // TODO
        return "Insufficient bandwidth: required " + required + ", available " + available;
    }
}
