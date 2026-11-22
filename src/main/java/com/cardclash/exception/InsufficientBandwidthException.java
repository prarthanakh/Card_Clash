package com.cardclash.exception;

public class InsufficientBandwidthException extends Exception {
    private final int required;
    private final int available;

    public InsufficientBandwidthException(int required, int available) {
        this.required = required;
        this.available = available;
    }

    public int getRequired() {
        return required;
    }

    public int getAvailable() {
        return available;
    }

    @Override
    public String getMessage() {
        return "Insufficient bandwidth: required " + required
                + ", available " + available;
    }
}
