package com.example.calculator.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ICalculator {
    void run(InputStream is, OutputStream os) throws IOException;
}
