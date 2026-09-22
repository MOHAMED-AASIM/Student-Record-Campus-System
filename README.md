# University Student Record and Campus Route Management System

**Module:** CIT300 – Data Structures and Algorithms
**Assessment:** Graded Practical Assignment 1 (Week 10)
**Contribution:** 10% of the final module grade
**Coverage:** Weeks 1–9 – Linear Data Structures, Trees, Hashing, and Graphs

---

## 1. Project Structure

```text
StudentRecordCampusSystem/
└── demo/
    ├── pom.xml
    ├── src/com/sltc/campussystem/
    │   ├── Main.java
    │   ├── CampusApplication.java
    │   ├── model/
    │   │   ├── StudentRecord.java
    │   │   ├── ServiceRequest.java
    │   │   └── Action.java
    │   ├── structures/
    │   │   ├── StudentLinkedList.java
    │   │   ├── ActionStack.java
    │   │   ├── ServiceQueue.java
    │   │   ├── StudentAVLTree.java
    │   │   ├── StudentHashTable.java
    │   │   └── CampusGraph.java
    │   ├── exceptions/
    │   └── util/
    │       └── InputValidator.java
    └── target/
```

---

## 2. Requirement-to-Component Mapping

| Assignment Requirement                              | Implemented In                      |
| --------------------------------------------------- | ----------------------------------- |
| Student ID, Name, Programme, Marks                  | `model/StudentRecord.java`          |
| Linked list storage                                 | `structures/StudentLinkedList.java` |
| Stack – recent actions/history                      | `structures/ActionStack.java`       |
| Queue – service requests                            | `structures/ServiceQueue.java`      |
| BST/AVL tree by Student ID                          | `structures/StudentAVLTree.java`    |
| Hashing for ID search                               | `structures/StudentHashTable.java`  |
| Graph, adjacency list, add/remove, display, BFS/DFS | `structures/CampusGraph.java`       |
| Student operations                                  | `Main.java`                         |
| Menu-driven UI and validation                       | `Main.java`, `InputValidator.java`  |
| Error handling                                      | `exceptions/`                       |

---

## 3. Data Structures

The system implements the required data structures from scratch:

* Singly Linked List
* Stack
* Queue
* AVL Tree
* Hash Table using Separate Chaining
* Graph using an Adjacency List
* BFS Traversal
* DFS Traversal

The main student record is synchronized across the linked list, AVL tree, and hash table.

---

## 4. Validation and Error Handling

The application provides validation and error handling for:

* Blank or empty required fields
* Invalid student marks
* Marks outside the range 0–100
* Invalid menu choices
* Duplicate Student IDs
* Missing Student IDs
* Duplicate campus locations
* Missing campus locations
* Self-loop connections
* Duplicate campus connections
* Missing campus connections

Errors are handled without crashing the application.

---

## 5. How to Build and Run

### Requirements

The console application requires:

* JDK 17 or later
* Visual Studio Code
* Java Extension Pack for VS Code

Maven and JavaFX are **not required** for the graded console application.

### Step 1 – Check Java

Open the VS Code terminal:

```powershell
java -version
```

```powershell
javac -version
```

Both commands should display Java 17 or a later version.

### Step 2 – Open the `demo` folder

Open the `demo` folder in VS Code.

The folder should contain:

```text
pom.xml
src
```

### Step 3 – Generate the Java source list

Run:

```powershell
Get-ChildItem -Recurse -Filter *.java |
Where-Object { $_.Name -ne "CampusApplication.java" } |
ForEach-Object { $_.FullName } |
Set-Content sources.txt
```

### Step 4 – Create the build folder

```powershell
New-Item -ItemType Directory -Force bin
```

### Step 5 – Compile

```powershell
javac -d bin @sources.txt
```

### Step 6 – Run

```powershell
java -cp bin com.sltc.campussystem.Main
```

---

## 6. Menu

```text
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

---

## 7. Optional JavaFX GUI

The project also contains an optional JavaFX GUI through:

```text
CampusApplication.java
```

The GUI is not required for the graded console application.

If the GUI is required, Maven and JavaFX must be installed/configured.

Run:

```powershell
mvn javafx:run
```

---

## 8. Submission Checklist

Before submission, verify:

* Complete project is implemented.
* All required data structures are included.
* Graph functionality is implemented.
* Student management functions work correctly.
* Validation and error handling work correctly.
* GitHub repository is complete.
* Demo video is complete.
* Google Drive files are uploaded if required.
* Google Drive link is included in the required `.txt` file.
* Required staff members have Editor access.
* Google Drive permissions have been checked.
* Submission is completed through the designated LMS before the deadline.

**Submission deadline: 29 September**

An email sent after the deadline does not replace the required LMS submission.

