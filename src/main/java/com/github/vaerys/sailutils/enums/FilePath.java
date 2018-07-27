package com.github.vaerys.sailutils.enums;

public enum FilePath {
    DIR_STORAGE("Storage/"),
    DIR_BACKUP(DIR_STORAGE + "Backups/"),
    FILE_TOKEN(DIR_STORAGE + "Token.txt");

    private String name;

    FilePath(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
