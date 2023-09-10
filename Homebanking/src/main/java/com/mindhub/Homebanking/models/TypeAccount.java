package com.mindhub.Homebanking.models;

public enum TypeAccount {
    CURRENT_ACCOUNT("Current Account"),
    SAVING_ACCOUNT("Saving Account");

    private final String displayName;
    TypeAccount(String displayName) {
        this.displayName =displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
