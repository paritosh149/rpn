package com.example;

import com.example.exception.CalcException;
import com.example.exception.ExitException;
import com.example.exception.InvalidInputException;

import javax.naming.InsufficientResourcesException;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringJoiner;

public class CalculatorMain {

    String displaySeparatorChar;
    String inputSeparatorChar;
    int operationPrecision;
    int displayPrecision;
    public MathContext mathContext;

    final Command<BigDecimal> command = new Command<>();

    Stack<TreeNode<BigDecimal>> calcStorage = new Stack<>();
    HashMap<String, OperatorType> OperatorsMap = new HashMap<>();
    HashMap<String, CommandType> CommandsMap = new HashMap<>();

    public CalculatorMain() {
        this(" ", " ", 15, 10);
    }
    public CalculatorMain(String displaySeparatorChar,
                          String inputSeparatorChar,
                          int operationPrecision,
                          int displayPrecision) {
        this.displaySeparatorChar = displaySeparatorChar;
        this.inputSeparatorChar = inputSeparatorChar;
        this.operationPrecision = operationPrecision;
        this.displayPrecision = displayPrecision;
        this.mathContext = new MathContext(operationPrecision, RoundingMode.HALF_UP);
        OperatorsMap.put("+", OperatorType.PLUS);
        OperatorsMap.put("-", OperatorType.MINUS);
        OperatorsMap.put("*", OperatorType.MULTIPLY);
        OperatorsMap.put("/", OperatorType.DIVIDE);
        OperatorsMap.put("sqrt", OperatorType.SQRT);

        CommandsMap.put("exit", CommandType.EXIT);
        CommandsMap.put("undo", CommandType.UNDO);
        CommandsMap.put("clear", CommandType.CLEAR);
    }

    public void run(InputStream inputStream, OutputStream outputStream) throws IOException {
        Scanner scanner = new Scanner(inputStream);
        Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        boolean isRunning = true;
        // keep accepting input lines until exit command is issued
        while (isRunning && scanner.hasNextLine()) {
            // accept user inputs in a single line
            String inputLine = scanner.nextLine();
            try {
                this.processInputLine(inputLine);
                // print the calc stack
            } catch (ExitException e) {
                // stop accepting the input line
                isRunning = false;
            }catch (CalcException e) {
                writer.write(e.getMessage());
            } finally {
                writer.write("stack: " + this);
                writer.write("\n");
                writer.flush();
            }
        }
    }

    private void processInputLine(String inputLine) throws ExitException {
        String[] inputParts = inputLine.split(inputSeparatorChar);
        final int separatorLength = inputSeparatorChar.length();
        int position = 1; // maintain position to help form InsufficientResourcesException message
        for (String inputPart : inputParts) {
            try {
                // process each part in input line
                this.processInputPart(inputPart);
                // move the position marker ahead of current inputPart
                position += inputPart.length() + separatorLength;
            } catch (InsufficientResourcesException e) {
                throw new CalcException("operator " + e.getMessage() + " (position: " + position + "): insufficient parameters ");
            } catch (InvalidInputException e) {
                throw new CalcException("Invalid input found: " + e.getMessage() + " ");
            } catch (ArithmeticException e) {
                throw new CalcException("Invalid operation attempted: " + e.getMessage() + " ");
            }
        }
    }

    private void processInputPart(String inputPart) throws ExitException, InvalidInputException, InsufficientResourcesException {
        InputData inputData = this.prepareInputPart(inputPart);
        switch (inputData.type) {
            case INVALID:
                throw new InvalidInputException(inputPart);
            case COMMAND:
                processCommand(inputData);
                return;
            case NUMBER:
                processNumber(inputData);
                break;
            case OPERATION:
                processOperation(inputData);
                break;
        }
    }

    private void processOperation(InputData inputData) throws InsufficientResourcesException {
        Operation.processOperation(inputData, calcStorage, mathContext);
    }

    private void processNumber(InputData inputData) {
        calcStorage.push(new TreeNode<>(inputData.value));
    }

    private void processCommand(InputData inputData) throws ExitException {
        switch (inputData.command) {
            case EXIT:
                command.exit();
            case UNDO:
                command.undo(calcStorage);
                break;
            case CLEAR:
                command.clear(calcStorage);
                break;
        }
    }

    private InputData prepareInputPart(String inputPart) {
        InputData inputData = new InputData();
        // check if it is a operator
        inputData.operator = OperatorsMap.get(inputPart);
        if (inputData.operator != null) {
            inputData.type = InputDataType.OPERATION;
            return inputData;
        }
        // check is it is a command
        inputData.command = CommandsMap.get(inputPart);
        if (inputData.command != null) {
            inputData.type = InputDataType.COMMAND;
            return inputData;
        }
        // check if it is a number
        try {
            inputData.value = new BigDecimal(inputPart);
            inputData.type = InputDataType.NUMBER;
            return inputData;
        } catch (NumberFormatException e) {
            // Do nothing
        }
        // input part is not understood yet
        inputData.type = InputDataType.INVALID;
        return inputData;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(displaySeparatorChar);
        for (TreeNode<BigDecimal> item : calcStorage) {
            sj.add(
                item.value
                    // TODO store the rounded value for future
                    .setScale(displayPrecision, RoundingMode.FLOOR)
                    .stripTrailingZeros()
                    .toPlainString()
            );
        }
        return sj.toString();
    }
}