package com.example;

import java.util.Stack;

public class TreeNode<T> {
    T value;
    Stack<TreeNode<T>> childNodes;

    public TreeNode(T value) {
        this.value = value;
    }
}
