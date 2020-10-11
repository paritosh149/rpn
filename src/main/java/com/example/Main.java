package com.example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws IOException {
        CalculatorMain calculator = new CalculatorMain();
        System.out.println("Calculator ready...");
        calculator.run(System.in, System.out);
    }
}
