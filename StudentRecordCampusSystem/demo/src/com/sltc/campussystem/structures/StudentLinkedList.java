package com.sltc.campussystem.structures;

import com.sltc.campussystem.model.StudentRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * A hand-written singly linked list used as the primary storage structure
 * for student records (Section 4, Requirement 2).
 *
 * Supports add, update, delete, search, and full display, all implemented
 * from first principles (no java.util.LinkedList).
 */
public class StudentLinkedList {

    /** Internal node holding one student record and a link to the next node. */
    private static class Node {
        StudentRecord data;
        Node next;

        Node(StudentRecord data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Appends a new student record to the end of the list. O(1). */
    public void add(StudentRecord record) {
        Node node = new Node(record);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /** Linear search by student ID. Returns null if not present. O(n). */
    public StudentRecord search(String studentId) {
        Node current = head;
        while (current != null) {
            if (current.data.getStudentId().equalsIgnoreCase(studentId)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Updates an existing record in place (name, programme, marks).
     * Returns true if the record was found and updated.
     */
    public boolean update(String studentId, String name, String programme, Double marks) {
        StudentRecord record = search(studentId);
        if (record == null) {
            return false;
        }
        if (name != null && !name.isBlank()) {
            record.setName(name);
        }
        if (programme != null && !programme.isBlank()) {
            record.setProgramme(programme);
        }
        if (marks != null) {
            record.setMarks(marks);
        }
        return true;
    }

    /** Removes and returns the record with the given ID, or null if absent. O(n). */
    public StudentRecord delete(String studentId) {
        Node current = head;
        Node previous = null;
        while (current != null) {
            if (current.data.getStudentId().equalsIgnoreCase(studentId)) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                if (current == tail) {
                    tail = previous;
                }
                size--;
                return current.data;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    /** Returns all records as a List, in insertion order, for display or iteration. */
    public List<StudentRecord> toList() {
        List<StudentRecord> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    /** Prints every record in the list in a formatted table. */
    public void displayAll() {
        if (isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
        System.out.println(StudentRecord.tableHeader());
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
        System.out.println("Total records: " + size);
    }
}
