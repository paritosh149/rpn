package com.example.calculator.interfaces;

import com.example.calculator.exceptions.ExitException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ICalculator {
    void run(InputStream is, OutputStream os) throws IOException;
    String processInputLine(String input) throws ExitException;
    void run() throws IOException;
}
