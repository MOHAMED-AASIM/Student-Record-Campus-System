package com.sltc.campussystem.structures;

import com.sltc.campussystem.model.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * A hand-written, linked-node-based stack (LIFO) that records recent
 * add/update/delete actions performed on student records
 * (Section 4, Requirement 3).
 */
public class ActionStack {

    private static class Node {
        Action data;
        Node next;

        Node(Action data) {
            this.data = data;
        }
    }

    private Node top;
    private int size;

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    /** Pushes a new action onto the top of the stack. O(1). */
    public void push(Action action) {
        Node node = new Node(action);
        node.next = top;
        top = node;
        size++;
    }

    /** Removes and returns the most recent action, or null if the stack is empty. O(1). */
    public Action pop() {
        if (isEmpty()) {
            return null;
        }
        Action data = top.data;
        top = top.next;
        size--;
        return data;
    }

    /** Returns the most recent action without removing it, or null if empty. O(1). */
    public Action peek() {
        return isEmpty() ? null : top.data;
    }

    public List<Action> toList() {
        List<Action> result = new ArrayList<>();
        Node current = top;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    /** Prints all recorded actions from most recent to oldest, without modifying the stack. */
    public void displayAll() {
        if (isEmpty()) {
            System.out.println("No recent actions recorded yet.");
            return;
        }
        System.out.println("Recent Actions (most recent first):");
        System.out.println("-".repeat(70));
        Node current = top;
        int count = 1;
        while (current != null) {
            System.out.println(count++ + ". " + current.data);
            current = current.next;
        }
    }
}
