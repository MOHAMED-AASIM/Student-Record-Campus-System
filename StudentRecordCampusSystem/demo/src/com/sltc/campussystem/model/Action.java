package com.sltc.campussystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents one recent action performed on the student records
 * (ADD, UPDATE, DELETE). Pushed onto the ActionStack so the system can
 * display a recent-actions / history log (Section 4, Requirement 3).
 */
public class Action {

    public enum Type { ADD, UPDATE, DELETE }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final Type type;
    private final StudentRecord snapshot;
    private final LocalDateTime timestamp;

    public Action(Type type, StudentRecord snapshot) {
        this.type = type;
        this.snapshot = snapshot;
        this.timestamp = LocalDateTime.now();
    }

    public Type getType() {
        return type;
    }

    public StudentRecord getSnapshot() {
        return snapshot;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-8s Student ID: %-10s Name: %-20s (%s)",
                timestamp.format(FMT), type, snapshot.getStudentId(), snapshot.getName(), timestamp.format(FMT));
    }
}
