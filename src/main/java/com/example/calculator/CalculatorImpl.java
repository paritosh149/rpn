package com.example.calculator;

import com.example.calculator.commands.Command;
import com.example.calculator.constants.Constants;
import com.example.calculator.exceptions.ExitException;
import com.example.calculator.interfaces.ICalculator;
import com.example.calculator.objects.*;
import com.example.calculator.exceptions.CalcException;
import com.example.calculator.exceptions.InvalidInputException;
import com.example.calculator.operations.Operation;

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

public class CalculatorImpl implements ICalculator {

    final String displaySeparatorChar;
    final String inputSeparatorChar;
    final int operationPrecision;
    final int displayPrecision;
    final public MathContext mathContext;

    final Command<BigDecimal> command;

    final Stack<TreeNode<BigDecimal>> calcStorage;
    final HashMap<String, OperatorType> OperatorsMap;
    final HashMap<String, CommandType> CommandsMap;

    public CalculatorImpl() {
        this(Constants.DISPLAY_SEPARATOR_CHAR,
                Constants.INPUT_SEPARATOR_CHAR,
                Constants.OPERATION_PRECISION,
                Constants.DISPLAY_PRECISION);
    }
    public CalculatorImpl(String displaySeparatorChar,
                          String inputSeparatorChar,
                          int operationPrecision,
                          int displayPrecision) {

        this.displaySeparatorChar = displaySeparatorChar;
        this.inputSeparatorChar = inputSeparatorChar;
        this.operationPrecision = operationPrecision;
        this.displayPrecision = displayPrecision;
        this.mathContext = new MathContext(operationPrecision, RoundingMode.HALF_UP);

        command = new Command<>();

        calcStorage = new Stack<>();
        OperatorsMap = new HashMap<>();
        CommandsMap = new HashMap<>();

        OperatorsMap.put(OperatorType.PLUS.toString(), OperatorType.PLUS);
        OperatorsMap.put(OperatorType.MINUS.toString(), OperatorType.MINUS);
        OperatorsMap.put(OperatorType.MULTIPLY.toString(), OperatorType.MULTIPLY);
        OperatorsMap.put(OperatorType.DIVIDE.toString(), OperatorType.DIVIDE);
        OperatorsMap.put(OperatorType.SQRT.toString(), OperatorType.SQRT);

        CommandsMap.put(CommandType.EXIT.toString(), CommandType.EXIT);
        CommandsMap.put(CommandType.UNDO.toString(), CommandType.UNDO);
        CommandsMap.put(CommandType.CLEAR.toString(), CommandType.CLEAR);

    }

    public void run() throws IOException {
        this.run(System.in, System.out);
    }

    public void run(InputStream inputStream, OutputStream outputStream) throws IOException {
        final Scanner scanner = new Scanner(inputStream);
        final Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        boolean isRunning = true;
        // keep accepting input lines until exit command is issued
        while (isRunning && scanner.hasNextLine()) {
            try {
                // accept user inputs in a single line
                String inputLine = scanner.nextLine().toLowerCase().trim();
                try {
                    this.processInputLine(inputLine);
                }catch (CalcException e) {
                    writer.write(e.getMessage());
                } finally {
                    // print the calc stack
                    writer.write("stack: " + this + "\n");
                    writer.flush();
                }
            } catch (ExitException e) {
                // stop accepting the input line
                isRunning = false;
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
        ManagedInput managedInput = this.prepareInputPart(inputPart);
        switch (managedInput.type) {
            case INVALID:
                throw new InvalidInputException(inputPart);
            case COMMAND:
                processCommand(managedInput);
                return;
            case NUMBER:
                processNumber(managedInput);
                break;
            case OPERATION:
                processOperation(managedInput);
                break;
        }
    }

    private void processOperation(ManagedInput managedInput) throws InsufficientResourcesException {
        Operation.processOperation(managedInput, calcStorage, mathContext);
    }

    private void processNumber(ManagedInput managedInput) {
        calcStorage.push(new TreeNode<>(managedInput.value));
    }

    private void processCommand(ManagedInput managedInput) throws ExitException {
        switch (managedInput.command) {
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

    private ManagedInput prepareInputPart(String inputPart) {
        ManagedInput managedInput = new ManagedInput();
        // check if it is a operator
        managedInput.operator = OperatorsMap.get(inputPart);
        if (managedInput.operator != null) {
            managedInput.type = ManagedInputType.OPERATION;
            return managedInput;
        }
        // check is it is a command
        managedInput.command = CommandsMap.get(inputPart);
        if (managedInput.command != null) {
            managedInput.type = ManagedInputType.COMMAND;
            return managedInput;
        }
        // check if it is a number
        try {
            managedInput.value = new BigDecimal(inputPart);
            managedInput.type = ManagedInputType.NUMBER;
            return managedInput;
        } catch (NumberFormatException e) {
            // Do nothing
        }
        // input part is not understood yet
        managedInput.type = ManagedInputType.INVALID;
        return managedInput;
    }

    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(displaySeparatorChar);
        for (TreeNode<BigDecimal> item : calcStorage) {
            sj.add(
                item.value
                    .setScale(displayPrecision, RoundingMode.FLOOR)
                    .stripTrailingZeros()
                    .toPlainString()
            );
        }
        return sj.toString();
    }
}