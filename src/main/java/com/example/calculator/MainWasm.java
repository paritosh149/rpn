package com.example.calculator;

import com.example.calculator.exceptions.ExitException;
import com.example.calculator.interfaces.ICalculator;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MainWasm {
    public static void main(String[] args) throws IOException {
        // create a new Calculator instance
        ICalculator calculator = new CalculatorImpl();
        System.out.println("Wasm Calculator ready...");
        // start the instance
//        calculator.run(new ByteArrayInputStream("5 6 +".getBytes()), System.out);
        try {
            for (String inputLine :
                    args) {
                String result = calculator.processInputLine(inputLine);
                System.out.println(result);

            }
        } catch (ExitException e) {

        }
    }
}
