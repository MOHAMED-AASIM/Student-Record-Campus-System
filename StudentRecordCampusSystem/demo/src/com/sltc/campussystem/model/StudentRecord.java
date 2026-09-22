package com.sltc.campussystem.model;

/**
 * Represents a single university student record.
 * Holds Student ID, Name, Programme, and Marks as required by the
 * assignment specification (Section 4, Requirement 1).
 */
public class StudentRecord {

    private String studentId;
    private String name;
    private String programme;
    private double marks;

    public StudentRecord(String studentId, String name, String programme, double marks) {
        this.studentId = studentId;
        this.name = name;
        this.programme = programme;
        this.marks = marks;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    /**
     * Returns a shallow copy of this record. Used when storing snapshots
     * on the Action stack so undo/history entries are not affected by
     * later in-place mutation of the live record.
     */
    public StudentRecord copy() {
        return new StudentRecord(studentId, name, programme, marks);
    }

    @Override
    public String toString() {
        return String.format("%-12s %-22s %-25s %6.2f", studentId, name, programme, marks);
    }

    public static String tableHeader() {
        return String.format("%-12s %-22s %-25s %6s", "ID", "Name", "Programme", "Marks")
                + "\n" + "-".repeat(70);
    }
}
