package com.sltc.campussystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a student service request placed in the FIFO queue
 * (Section 4, Requirement 4).
 */
public class ServiceRequest {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final int requestId;
    private final String studentId;
    private final String description;
    private final LocalDateTime timestamp;

    public ServiceRequest(int requestId, String studentId, String description) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public int getRequestId() {
        return requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Req#%-4d | Student: %-10s | %-30s | %s",
                requestId, studentId, description, timestamp.format(FMT));
    }
}
