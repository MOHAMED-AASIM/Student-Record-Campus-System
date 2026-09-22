# University Student Record and Campus Route Management System

**Module:** CIT300 – Data Structures and Algorithms
**Assessment:** Graded Practical Assignment 1 (Week 10)
**Contribution:** 10% of the final module grade
**Coverage:** Weeks 1–9 – Linear Data Structures, Trees, Hashing, and Graphs

---

## 1. Group Member Details

> ⚠️ Fill in every row before submission — this table is graded directly
> (Section 1 of the brief: missing/incomplete details may lose marks).

| # | Name | Student ID | Assigned Responsibility | Individual Contribution |
|---|------|-----------|--------------------------|---------------------------|
| 1 | | | Linked list & student-record management | |
| 2 | | | Stack and queue implementation | |
| 3 | | | AVL tree and hashing/search functionality | |
| 4 | | | Graph, campus locations, BFS/DFS traversal | |

If the group has fewer than four members, combine responsibilities among
the available members — every component below, including the graph
component, is still implemented in full; none may be omitted.

---

## 2. Project Structure

```
StudentRecordCampusSystem/
└── demo/
    ├── pom.xml                          Maven build file (JavaFX GUI + compiler config)
    ├── src/com/sltc/campussystem/
    │   ├── Main.java                    Menu-driven CONSOLE entry point (this is what the assignment grades)
    │   ├── CampusApplication.java       Optional JavaFX desktop GUI over the same logic (bonus, not required)
    │   ├── model/
    │   │   ├── StudentRecord.java       Student ID, Name, Programme, Marks
    │   │   ├── ServiceRequest.java      Queue payload
    │   │   └── Action.java              Stack payload (ADD/UPDATE/DELETE history)
    │   ├── structures/
    │   │   ├── StudentLinkedList.java   Custom singly linked list (student record storage)
    │   │   ├── ActionStack.java         Custom stack (recent actions / history)
    │   │   ├── ServiceQueue.java        Custom queue (service requests, FIFO)
    │   │   ├── StudentAVLTree.java      Custom self-balancing AVL tree (ordered by Student ID)
    │   │   ├── StudentHashTable.java    Custom hash table, separate chaining (search by ID)
    │   │   └── CampusGraph.java         Adjacency-list graph, BFS & DFS traversal
    │   ├── exceptions/                  Custom checked exceptions for validation errors
    │   └── util/
    │       └── InputValidator.java      Console input reading & validation helpers
    └── target/                          Build output (generated — safe to delete/ignore)
```

## 3. Requirement-to-Component Mapping

| Assignment Requirement (Section 4) | Implemented in |
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

## 4. How to Build and Run (Console App — this is what's graded)

Requires a **JDK 17 or later** (any recent JDK works — no Maven needed
for the console app).

```bash
# From the demo/ directory:
find src -name "*.java" ! -name "CampusApplication.java" > sources.txt
javac -d bin @sources.txt
java -cp bin com.sltc.campussystem.Main
```

> `CampusApplication.java` is excluded here because it's the optional
> JavaFX GUI (see below) — leaving it out lets the console app compile
> with just a plain JDK and no extra downloads.

This has been compiled and test-run end-to-end (every menu option,
1–16) with no errors.

### Optional: JavaFX GUI

The project also includes an optional JavaFX desktop GUI
(`CampusApplication.java`) wrapping the same underlying logic. It needs
Maven and internet access to download JavaFX 21:

```bash
# From the demo/ directory, with Maven and JDK 17+ on the PATH:
mvn javafx:run
```

The console app above is the primary deliverable and does not depend on
the GUI or on Maven at all.

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


