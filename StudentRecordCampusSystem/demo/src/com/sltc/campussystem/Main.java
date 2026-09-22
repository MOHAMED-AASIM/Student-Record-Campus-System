package com.sltc.campussystem;

import com.sltc.campussystem.exceptions.*;
import com.sltc.campussystem.model.Action;
import com.sltc.campussystem.model.ServiceRequest;
import com.sltc.campussystem.model.StudentRecord;
import com.sltc.campussystem.structures.*;
import com.sltc.campussystem.util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * CIT300 Data Structures and Algorithms - Graded Practical Assignment 1
 * University Student Record and Campus Route Management System.
 *
 * Ties together all six required data structures:
 *   - StudentLinkedList : primary storage for student records
 *   - ActionStack        : recent actions / undo-style history
 *   - ServiceQueue        : student service requests, FIFO
 *   - StudentAVLTree      : records organised/searchable by Student ID
 *   - StudentHashTable    : O(1)-average search by Student ID
 *   - CampusGraph          : campus locations and connections, BFS/DFS
 *
 * Whenever a student record is added, updated, or deleted, all three
 * student-record structures (linked list, AVL tree, hash table) are kept
 * in sync so every view of the data is consistent.
 */
public class Main {

    private final StudentLinkedList studentList = new StudentLinkedList();
    private final ActionStack actionStack = new ActionStack();
    private final ServiceQueue serviceQueue = new ServiceQueue();
    private final StudentAVLTree studentTree = new StudentAVLTree();
    private final StudentHashTable studentHashTable = new StudentHashTable();
    private final CampusGraph campusGraph = new CampusGraph();

    private final Scanner scanner = new Scanner(System.in);
    private final InputValidator input = new InputValidator(scanner);
    private int nextRequestId = 1;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        printBanner();
        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readMenuChoice("Enter your choice (1-16): ", 1, 16);
            System.out.println();
            switch (choice) {
                case 1 -> addStudentRecord();
                case 2 -> updateStudentRecord();
                case 3 -> deleteStudentRecord();
                case 4 -> studentList.displayAll();
                case 5 -> addServiceRequest();
                case 6 -> processNextServiceRequest();
                case 7 -> actionStack.displayAll();
                case 8 -> studentTree.displayInOrder();
                case 9 -> searchStudentUsingHashing();
                case 10 -> addCampusLocation();
                case 11 -> removeCampusLocation();
                case 12 -> addCampusConnection();
                case 13 -> removeCampusConnection();
                case 14 -> campusGraph.displayNetwork();
                case 15 -> traverseCampus();
                case 16 -> running = false;
                default -> System.out.println("Unexpected choice.");
            }
            if (running) {
                System.out.println();
                System.out.println("-".repeat(70));
                System.out.println();
            }
        }
        System.out.println("Exiting. Goodbye!");
        scanner.close();
    }

    // ---------------------------------------------------------------
    // Student record operations (Menu 1-4, 8-9)
    // ---------------------------------------------------------------

    private void addStudentRecord() {
        System.out.println("--- Add Student Record ---");
        String id = input.readNonBlank("Student ID: ");

        if (studentHashTable.get(id) != null) {
            System.out.println("Error: " + new DuplicateStudentIdException(id).getMessage());
            return;
        }

        String name = input.readNonBlank("Name: ");
        String programme = input.readNonBlank("Programme: ");
        double marks = input.readMarks("Marks (0-100): ");

        StudentRecord record = new StudentRecord(id, name, programme, marks);
        studentList.add(record);
        studentTree.insert(record);
        studentHashTable.put(id, record);
        actionStack.push(new Action(Action.Type.ADD, record.copy()));

        System.out.println("Student record added successfully.");
    }

    private void updateStudentRecord() {
        System.out.println("--- Update Student Record ---");
        String id = input.readNonBlank("Student ID to update: ");

        StudentRecord existing = studentHashTable.get(id);
        if (existing == null) {
            System.out.println("Error: " + new StudentNotFoundException(id).getMessage());
            return;
        }

        System.out.println("Current record: " + existing);
        System.out.println("Leave a field blank to keep its current value.");
        String name = input.readOptional("New Name: ");
        String programme = input.readOptional("New Programme: ");
        Double marks = input.readOptionalMarks("New Marks (0-100): ");

        studentList.update(id, name, programme, marks);
        // AVL tree and hash table hold the same StudentRecord object references
        // as the linked list, so their data is already updated in place.
        actionStack.push(new Action(Action.Type.UPDATE, existing.copy()));

        System.out.println("Student record updated successfully.");
    }

    private void deleteStudentRecord() {
        System.out.println("--- Delete Student Record ---");
        String id = input.readNonBlank("Student ID to delete: ");

        StudentRecord removed = studentList.delete(id);
        if (removed == null) {
            System.out.println("Error: " + new StudentNotFoundException(id).getMessage());
            return;
        }
        studentTree.delete(id);
        studentHashTable.remove(id);
        actionStack.push(new Action(Action.Type.DELETE, removed.copy()));

        System.out.println("Student record deleted successfully.");
    }

    private void searchStudentUsingHashing() {
        System.out.println("--- Search Student using Hashing ---");
        String id = input.readNonBlank("Student ID to search: ");
        StudentRecord record = studentHashTable.get(id);
        if (record == null) {
            System.out.println("Error: " + new StudentNotFoundException(id).getMessage());
            return;
        }
        System.out.println(StudentRecord.tableHeader());
        System.out.println(record);
        System.out.println(studentHashTable.diagnostics());
    }

    // ---------------------------------------------------------------
    // Service request queue operations (Menu 5-6)
    // ---------------------------------------------------------------

    private void addServiceRequest() {
        System.out.println("--- Add Service Request ---");
        String studentId = input.readNonBlank("Student ID: ");
        String description = input.readNonBlank("Request description: ");
        ServiceRequest request = new ServiceRequest(nextRequestId++, studentId, description);
        serviceQueue.enqueue(request);
        System.out.println("Service request added to the queue: " + request);
    }

    private void processNextServiceRequest() {
        System.out.println("--- Process Next Service Request ---");
        ServiceRequest request = serviceQueue.dequeue();
        if (request == null) {
            System.out.println("There are no pending service requests to process.");
            return;
        }
        System.out.println("Processed: " + request);
    }

    // ---------------------------------------------------------------
    // Campus graph operations (Menu 10-15)
    // ---------------------------------------------------------------

    private void addCampusLocation() {
        System.out.println("--- Add Campus Location ---");
        String location = input.readNonBlank("Location name: ");
        try {
            campusGraph.addLocation(location);
            System.out.println("Location added successfully.");
        } catch (DuplicateLocationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeCampusLocation() {
        System.out.println("--- Remove Campus Location ---");
        String location = input.readNonBlank("Location name: ");
        try {
            campusGraph.removeLocation(location);
            System.out.println("Location and its connections removed successfully.");
        } catch (LocationNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addCampusConnection() {
        System.out.println("--- Add Campus Connection/Road ---");
        String from = input.readNonBlank("From location: ");
        String to = input.readNonBlank("To location: ");
        try {
            campusGraph.addConnection(from, to);
            System.out.println("Connection added successfully.");
        } catch (LocationNotFoundException | InvalidConnectionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeCampusConnection() {
        System.out.println("--- Remove Campus Connection/Road ---");
        String from = input.readNonBlank("From location: ");
        String to = input.readNonBlank("To location: ");
        try {
            campusGraph.removeConnection(from, to);
            System.out.println("Connection removed successfully.");
        } catch (LocationNotFoundException | InvalidConnectionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void traverseCampus() {
        System.out.println("--- Traverse Campus Locations ---");
        if (campusGraph.locationCount() == 0) {
            System.out.println("No campus locations have been added yet.");
            return;
        }
        String start = input.readNonBlank("Start location: ");
        int mode = input.readMenuChoice("Choose traversal - 1) BFS  2) DFS : ", 1, 2);
        try {
            List<String> order = (mode == 1) ? campusGraph.bfs(start) : campusGraph.dfs(start);
            System.out.println((mode == 1 ? "BFS" : "DFS") + " order from '" + start + "': "
                    + String.join(" -> ", order));
        } catch (LocationNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Console UI helpers
    // ---------------------------------------------------------------

    private void printBanner() {
        System.out.println("=".repeat(70));
        System.out.println(" CIT300 - University Student Record and Campus Route Management System");
        System.out.println("=".repeat(70));
    }

    private void printMenu() {
        System.out.println("""
                 1.  Add Student Record
                 2.  Update Student Record
                 3.  Delete Student Record
                 4.  Display All Records using Linked List
                 5.  Add Service Request to Queue
                 6.  Process Next Service Request
                 7.  Display Recent Actions using Stack
                 8.  Display Students using BST/AVL
                 9.  Search Student using Hashing
                 10. Add Campus Location
                 11. Remove Campus Location
                 12. Add Campus Connection/Road
                 13. Remove Campus Connection/Road
                 14. Display Campus Connections
                 15. Traverse Campus Locations using BFS or DFS
                 16. Exit
                """);
    }
}
