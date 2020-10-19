package com.example.calculator.operations;

import java.math.BigDecimal;
import java.math.MathContext;

public class SimpleOperation {

    public static BigDecimal sqrt(BigDecimal a, MathContext mathContext) {
        try {
            return a.sqrt(mathContext);
        } catch(NoSuchMethodError e) {
            return new BigDecimal(Math.sqrt(a.doubleValue()));
        }
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b, MathContext mathContext) {
        return b.add(a, mathContext);
    }
    public static BigDecimal subtract(BigDecimal a, BigDecimal b, MathContext mathContext) {
        return b.subtract(a, mathContext);
    }
    public static BigDecimal multiply(BigDecimal a, BigDecimal b, MathContext mathContext) {
        return b.multiply(a, mathContext);
    }
    public static BigDecimal divide(BigDecimal a, BigDecimal b, MathContext mathContext) {
        return b.divide(a, mathContext);
    }
}
