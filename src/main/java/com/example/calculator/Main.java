package com.example.calculator;

import com.example.calculator.interfaces.ICalculator;
import com.example.calculator.objects.CommandType;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // create a new Calculator instance
        ICalculator calculator = new CalculatorImpl();
        System.out.println("Calculator ready...");
        // start the instance
        calculator.run();
    }
}
