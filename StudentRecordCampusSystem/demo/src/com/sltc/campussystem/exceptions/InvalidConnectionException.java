package com.sltc.campussystem.exceptions;

/** Thrown for invalid connection/road operations (duplicate edge, self-loop, missing edge, etc.). */
public class InvalidConnectionException extends Exception {
    public InvalidConnectionException(String message) {
        super(message);
    }
}
