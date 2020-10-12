package com.example.calculator.operations;

import com.example.calculator.objects.ManagedInput;
import com.example.calculator.objects.TreeNode;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.EmptyStackException;
import java.util.Stack;

public class Operation {

    public static void processOperation(ManagedInput managedInput, Stack<TreeNode<BigDecimal>> calcHeap, MathContext mathContext) throws InsufficientResourcesException {
        switch (managedInput.operator) {
            case PLUS:
                operateOnTwo(managedInput, calcHeap, mathContext, SimpleOperation::add);
                break;
            case MINUS:
                operateOnTwo(managedInput, calcHeap, mathContext, SimpleOperation::subtract);
                break;
            case MULTIPLY:
                operateOnTwo(managedInput, calcHeap, mathContext, SimpleOperation::multiply);
                break;
            case DIVIDE:
                operateOnTwo(managedInput, calcHeap, mathContext, SimpleOperation::divide);
                break;
            case SQRT:
                operateOnOne(managedInput, calcHeap, mathContext, SimpleOperation::sqrt);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + managedInput.operator);
        }
    }

    private static void operateOnTwo(ManagedInput managedInput, Stack<TreeNode<BigDecimal>> calcHeap, MathContext mathContext, OperationOnTwo operationOnTwo) throws InsufficientResourcesException {
        TreeNode<BigDecimal> lastOperand = null;
        TreeNode<BigDecimal> secondLastOperand = null;
        try {
                // pop the last operand
                lastOperand = calcHeap.pop();
            try {
                // pop the seconds last operand
                secondLastOperand = calcHeap.pop();
            } catch (EmptyStackException e) {
                if (lastOperand != null) {
                    calcHeap.push(lastOperand);
                }
                throw e;
            }

            // invoke the operation and get the result
            BigDecimal result = operationOnTwo
                    .operation(lastOperand.value, secondLastOperand.value, mathContext);

            TreeNode<BigDecimal> combinedNode = new TreeNode<>(result);
            combinedNode.childNodes = new Stack<>();
            combinedNode.childNodes.push(secondLastOperand);
            combinedNode.childNodes.push(lastOperand);
            calcHeap.push(combinedNode);
        } catch (EmptyStackException e) {
            throw new InsufficientResourcesException(managedInput.operator.toString());
        }
    }

    private static void operateOnOne(ManagedInput managedInput, Stack<TreeNode<BigDecimal>> calcHeap, MathContext mathContext, OperationOnOne operationOnOne) throws InsufficientResourcesException {
        TreeNode<BigDecimal> lastOperand1 = null;
        try {
            lastOperand1 = calcHeap.pop();
            BigDecimal result1 = operationOnOne.operation(lastOperand1.value, mathContext);
            TreeNode<BigDecimal> combinedResult1 = new TreeNode<>(result1);
            combinedResult1.childNodes = new Stack<>();
            combinedResult1.childNodes.push(lastOperand1);
            calcHeap.push(combinedResult1);
        } catch (EmptyStackException e) {
            throw new InsufficientResourcesException(managedInput.operator.toString());
        } catch (ArithmeticException e) {
            if (lastOperand1 != null)
                calcHeap.push(lastOperand1);
            throw e;
        }
    }

    @FunctionalInterface
    public interface OperationOnTwo {
        BigDecimal operation(BigDecimal a, BigDecimal b, MathContext mathContext);
    }

    @FunctionalInterface
    public interface OperationOnOne {
        BigDecimal operation(BigDecimal a, MathContext mathContext);
    }
}
