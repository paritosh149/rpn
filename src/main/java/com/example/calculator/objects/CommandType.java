package com.example.calculator.objects;

public enum CommandType {
    EXIT("exit"),
    UNDO("undo"),
    CLEAR("clear");


    private final String value;

    CommandType(String value) {
        this.value = value.toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }

}
