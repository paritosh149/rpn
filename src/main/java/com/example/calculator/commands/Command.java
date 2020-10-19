package com.example.calculator.commands;

import com.example.calculator.exceptions.ExitException;
import com.example.calculator.objects.TreeNode;

import java.util.Stack;

public class Command<T> {

    public void exit() throws ExitException {
        throw new ExitException();
    }

    public void clear(Stack calcStack) {
        calcStack.clear();
    }

    public void undo(Stack<TreeNode<T>> calcStack) {
        if (!calcStack.isEmpty()) {
            final TreeNode<T> item = calcStack.pop();
            if (item.childNodes != null) {
                final TreeNode<T> lastItem = item.childNodes.pop();
                if (!item.childNodes.isEmpty()) {
                    final TreeNode<T> secondLastItem = item.childNodes.pop();
                    calcStack.push(secondLastItem);
                }
                calcStack.push(lastItem);
            }
        }
    }
}
