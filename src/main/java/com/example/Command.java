package com.example;

import com.example.exception.ExitException;

import java.util.Stack;

public class Command<T> {

    public void exit() throws ExitException {
        throw new ExitException();
    }

    public void clear(Stack heap) {
        heap.clear();
    }

    public void undo(Stack<TreeNode<T>> heap) {
        if (!heap.isEmpty()) {
            TreeNode<T> item = heap.pop();
            if (item.childNodes != null) {
                TreeNode<T> lastItem = item.childNodes.pop();
                if (!item.childNodes.isEmpty()) {
                    TreeNode<T> secondLastItem = item.childNodes.pop();
                    heap.push(secondLastItem);
                }
                heap.push(lastItem);
            }
        }
    }

}
