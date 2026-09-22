# University Student Record and Campus Route Management System

**Module:** CIT300 – Data Structures and Algorithms
**Assessment:** Graded Practical Assignment 1 (Week 10)
**Contribution:** 10% of the final module grade
**Coverage:** Weeks 1–9 – Linear Data Structures, Trees, Hashing, and Graphs

---

## 1. Group Member Details

> Fill in every row before submission. All members' names, student IDs,
> responsibilities and individual contributions **must** be accurate and
> complete — the assignment brief states marks may be deducted otherwise.

| # | Name | Student ID | Assigned Responsibility | Individual Contribution |
|---|------|-----------|--------------------------|---------------------------|
 *e.g. Linked list & student-record management* | *describe what was built/tested* |
| 2 | | | *e.g. Stack and queue implementation* | |
| 3 | | | *e.g. AVL tree and hashing/search* | |
| 4 | | | *e.g. Graph, campus locations, BFS/DFS* | |

If the group has fewer than four members, combine responsibilities among
the available members — every component listed below, including the
graph component, is still implemented in full.

---

## 2. Project Structure

```
StudentRecordCampusSystem/
├── README.md
├── sources.txt                      (javac source list, for convenience)
└── src/com/sltc/campussystem/
    ├── Main.java                    Menu-driven console entry point
    ├── model/
    │   ├── StudentRecord.java       Student ID, Name, Programme, Marks
    │   ├── ServiceRequest.java      Queue payload
    │   └── Action.java              Stack payload (ADD/UPDATE/DELETE history)
    ├── structures/
    │   ├── StudentLinkedList.java   Custom singly linked list (record storage)
    │   ├── ActionStack.java         Custom stack (recent actions / history)
    │   ├── ServiceQueue.java        Custom queue (service requests, FIFO)
    │   ├── StudentAVLTree.java      Custom self-balancing AVL tree (by Student ID)
    │   ├── StudentHashTable.java    Custom hash table, separate chaining (search by ID)
    │   └── CampusGraph.java         Adjacency-list graph, BFS & DFS traversal
    ├── exceptions/                  Custom checked exceptions for validation
    └── util/
        └── InputValidator.java      Console input reading & validation helpers
```

## 3. Requirement-to-Component Mapping

| Requirement (Section 4) | Implemented in |
|---|---|
| 1. Student ID, Name, Programme, Marks | `model/StudentRecord.java` |
| 2. Linked list storage | `structures/StudentLinkedList.java` |
| 3. Stack – recent actions / history | `structures/ActionStack.java` |
| 4. Queue – service requests | `structures/ServiceQueue.java` |
| 5. BST/AVL tree by Student ID | `structures/StudentAVLTree.java` |
| 6. Hashing for ID search | `structures/StudentHashTable.java` |
| 7–11. Graph, adjacency list, add/remove, display, BFS/DFS | `structures/CampusGraph.java` |
| 12. Add/update/delete/search/display for students | `Main.java` (menu options 1–4, 9) |
| 13–14. Menu-driven UI, input validation, error handling | `Main.java`, `util/InputValidator.java`, `exceptions/*` |

All six required data structures (linked list, stack, queue, AVL tree,
hash table, graph) are implemented **from scratch** using plain node
classes — no `java.util.LinkedList`, `Stack`, `Queue`, `TreeMap`, or
`HashMap` is used for these components. `CampusGraph` uses a
`LinkedHashMap`/`LinkedHashSet` purely as an ordered container for the
adjacency list; the graph algorithms themselves (add/remove vertex,
add/remove edge, BFS, DFS) are hand-written.

When a student record is added, updated, or deleted, the linked list,
AVL tree, and hash table are all kept in sync, so every menu view
(list, tree, hash search) reflects the same consistent data.

## 4. How to Build and Run

Requires a Java 17+ JDK.

The project also includes a JavaFX interface. With Maven and Java 17+ installed,
run the GUI from the `StudentRecordCampusSystem/` directory with:

```bash
mvn javafx:run
```

The original console interface remains available with the commands below.

```bash
# From the StudentRecordCampusSystem/ directory:
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
java -cp bin com.sltc.campussystem.Main
```

## 5. Menu Overview

```
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
```

## 6. Validation & Error Handling

- Blank/empty required fields are rejected and re-prompted (`InputValidator`).
- Marks are validated to be numeric and within 0–100.
- Menu choices are validated to be integers within the valid range.
- Duplicate Student IDs, missing Student IDs, duplicate campus locations,
  missing locations, self-loop connections, and duplicate/missing
  connections all raise dedicated checked exceptions
  (`exceptions/` package) and are reported to the user without crashing
  the application.

## 7. Before You Submit — Checklist

This checklist mirrors Section 10 of the assignment brief:

- [ ] Complete project implemented, including all data structures and graph functionality.
- [ ] All group members' names and student IDs are correct (Section 1 above).
- [ ] Responsibilities and individual contributions are documented (Section 1 above).
- [ ] GitHub repository and demo video are complete.
- [ ] If using Google Drive, the complete project is uploaded.
- [ ] Google Drive link is copied correctly into the Notepad (.txt) file.
- [ ] `asanka.r@sltc.ac.lk` has Editor access.
- [ ] `kaushika.w@sltc.ac.lk` has Editor access.
- [ ] Google Drive permissions are checked before LMS submission.
- [ ] Submission completed through the designated LMS link before the deadline (**29th September**).

**Reminder:** an email sent after the deadline does not make a late
submission valid, even if it includes files, links, or permissions.
