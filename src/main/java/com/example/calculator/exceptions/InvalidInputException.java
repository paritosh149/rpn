package com.example.calculator.exceptions;

public class InvalidInputException extends Throwable {
    public InvalidInputException(String inputPart) {
        super(inputPart);
    }
}
