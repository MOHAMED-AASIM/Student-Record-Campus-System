package com.sltc.campussystem.exceptions;

/** Thrown when a referenced campus location does not exist in the graph. */
public class LocationNotFoundException extends Exception {
    public LocationNotFoundException(String location) {
        super("Campus location '" + location + "' was not found.");
    }
}
