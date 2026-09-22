package com.sltc.campussystem.exceptions;

/** Thrown when attempting to add a campus location that already exists in the graph. */
public class DuplicateLocationException extends Exception {
    public DuplicateLocationException(String location) {
        super("Campus location '" + location + "' already exists.");
    }
}
