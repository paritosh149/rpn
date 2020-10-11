package com.example;

public enum OperatorType {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    SQRT("sqrt");

    private final String value;

    OperatorType(String value) {
        this.value = value.toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
