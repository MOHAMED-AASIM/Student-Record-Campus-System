package com.sltc.campussystem.exceptions;

/** Thrown when attempting to add a student record whose ID already exists. */
public class DuplicateStudentIdException extends Exception {
    public DuplicateStudentIdException(String studentId) {
        super("A student record with ID '" + studentId + "' already exists.");
    }
}
