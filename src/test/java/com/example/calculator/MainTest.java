package com.example.calculator;

import com.example.calculator.interfaces.ICalculator;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class MainTest
{
    ICalculator calculator = new CalculatorImpl();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeAll
    static void setup(){

    }

    @Test
    public void shouldAdd5And6() throws IOException {

        String a = "5 6 +\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 11\n");
    }

    @Test
    public void shouldThrowErrorAdd5And6() throws IOException {

        String a = "5 + 6\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "operator + (position: 3): insufficient parameters stack: 5\n");
    }

    @Test
    public void shouldStore5And6() throws IOException {

        String a = "5 6\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 5 6\n");
    }
        @Test
    public void shouldSqrt2() throws IOException {

        String a = "2 sqrt\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 1.4142135623\n");
    }

    @Test
    public void shouldSubtractMultipleClear() throws IOException {

        String a = "5 2 -\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 3\n");
        outputStream.reset();

        calculator.run(new ByteArrayInputStream("3 -".getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 0\n");
        outputStream.reset();

        calculator.run(new ByteArrayInputStream("clear".getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: \n");
    }

    @Test
    public void shouldAddMultipleUndo() throws IOException {

        String a = "5 4 3 2\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 5 4 3 2\n");
        outputStream.reset();

        calculator.run(new ByteArrayInputStream("undo undo *".getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 20\n");
        outputStream.reset();

        calculator.run(new ByteArrayInputStream("5 *".getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 100\n");
        outputStream.reset();

        calculator.run(new ByteArrayInputStream("undo".getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 20 5\n");
    }

    @Test
    public void shouldThrowErrorMultipleOps() throws IOException {

        String a = "1 2 3 * 5 + * * 6 5\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "operator * (position: 15): insufficient parameters stack: 11\n");
    }

    @Test
    public void shouldThrowErrorInvalidInput() throws IOException {

        String a = "5 1+\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "Invalid input found: 1+ stack: 5\n");
    }

    @Test
    public void shouldThrowErrorInvalidOp() throws IOException {

        String a = "-9 sqrt\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "Invalid operation attempted: Attempted square root of negative BigDecimal stack: -9\n");
    }

    @Test
    public void shouldExit() throws IOException {

        String a = "exit\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: \n");
    }

    @Test
    public void shouldDivide5By6() throws IOException {

        String a = "5 6 /\n";
        calculator.run(new ByteArrayInputStream(a.getBytes()), outputStream);
        assertEquals(outputStream.toString(),
                "stack: 0.8333333333\n");
    }
}
