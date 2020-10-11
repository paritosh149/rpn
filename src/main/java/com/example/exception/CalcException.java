package com.example.exception;

import com.example.CalculatorMain;

public class CalcException extends RuntimeException{
    public CalcException(String message) {
        super(message);
    }
}
