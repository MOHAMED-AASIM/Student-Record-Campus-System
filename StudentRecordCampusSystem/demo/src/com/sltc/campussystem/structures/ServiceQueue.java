package com.sltc.campussystem.structures;

import com.sltc.campussystem.model.ServiceRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * A hand-written, linked-node-based queue (FIFO) that manages student
 * service requests in order of arrival (Section 4, Requirement 4).
 */
public class ServiceQueue {

    private static class Node {
        ServiceRequest data;
        Node next;

        Node(ServiceRequest data) {
            this.data = data;
        }
    }

    private Node front;
    private Node rear;
    private int size;

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }

    /** Adds a new request to the rear of the queue. O(1). */
    public void enqueue(ServiceRequest request) {
        Node node = new Node(request);
        if (rear == null) {
            front = node;
            rear = node;
        } else {
            rear.next = node;
            rear = node;
        }
        size++;
    }

    /** Removes and returns the request at the front of the queue, or null if empty. O(1). */
    public ServiceRequest dequeue() {
        if (isEmpty()) {
            return null;
        }
        ServiceRequest data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    /** Returns the request at the front without removing it, or null if empty. O(1). */
    public ServiceRequest peek() {
        return isEmpty() ? null : front.data;
    }

    public List<ServiceRequest> toList() {
        List<ServiceRequest> result = new ArrayList<>();
        Node current = front;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    /** Prints all pending requests in arrival order, without modifying the queue. */
    public void displayAll() {
        if (isEmpty()) {
            System.out.println("No pending service requests.");
            return;
        }
        System.out.println("Pending Service Requests (front to rear):");
        System.out.println("-".repeat(70));
        Node current = front;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
        System.out.println("Total pending: " + size);
    }
}
