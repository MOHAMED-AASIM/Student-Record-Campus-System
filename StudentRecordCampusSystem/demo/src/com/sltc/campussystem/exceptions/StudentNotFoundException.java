package com.sltc.campussystem.exceptions;

/** Thrown when a lookup, update, or delete is attempted on a missing student ID. */
public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String studentId) {
        super("No student record found with ID '" + studentId + "'.");
    }
}
