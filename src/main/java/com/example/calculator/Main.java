package com.example.calculator;

import com.example.calculator.interfaces.ICalculator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ICalculator calculator = new CalculatorImpl();
        System.out.println("Calculator ready...");
        calculator.run(System.in, System.out);
    }
}
