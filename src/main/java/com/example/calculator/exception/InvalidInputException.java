package com.example.calculator.exception;

public class InvalidInputException extends Throwable {
    public InvalidInputException(String inputPart) {
        super(inputPart);
    }
}
