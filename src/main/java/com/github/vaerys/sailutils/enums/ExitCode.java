package com.github.vaerys.sailutils.enums;

public enum ExitCode {
    STOP(0),
    RESTART(1);

    private int value;

    ExitCode(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "";
    }

    public int get() {
        return value;
    }
}
