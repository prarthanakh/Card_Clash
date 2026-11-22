package com.cardclash.logic;

public enum Difficulty {
    EASY("Easy"),
    HARD("Hard");

    private String label;

    private Difficulty(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

    

