package com.example.calculator.objects;

import java.util.Stack;

public class TreeNode<T> {
    final public T value;
    public Stack<TreeNode<T>> childNodes;

    public TreeNode(T value) {
        this.value = value;
    }
}
