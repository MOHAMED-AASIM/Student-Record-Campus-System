package com.sltc.campussystem.structures;

import com.sltc.campussystem.model.StudentRecord;

/**
 * A hand-written, self-balancing AVL tree that organizes student records
 * by Student ID (Section 4, Requirement 5).
 *
 * Standard AVL insert/delete with height-balancing rotations (LL, RR, LR,
 * RL) keeps the tree balanced so lookups stay O(log n) even as records
 * are added and removed.
 */
public class StudentAVLTree {

    private static class Node {
        StudentRecord data;
        Node left, right;
        int height;

        Node(StudentRecord data) {
            this.data = data;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;
        x.right = y;
        y.left = t2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;
        y.left = x;
        x.right = t2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node rebalance(Node node) {
        updateHeight(node);
        int balance = balanceFactor(node);

        // Left heavy
        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left); // LR case
            }
            return rotateRight(node); // LL case
        }
        // Right heavy
        if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right); // RL case
            }
            return rotateLeft(node); // RR case
        }
        return node;
    }

    /** Inserts a record keyed by Student ID, rebalancing as needed. O(log n). */
    public void insert(StudentRecord record) {
        root = insertRec(root, record);
    }

    private Node insertRec(Node node, StudentRecord record) {
        if (node == null) {
            size++;
            return new Node(record);
        }
        int cmp = record.getStudentId().compareToIgnoreCase(node.data.getStudentId());
        if (cmp < 0) {
            node.left = insertRec(node.left, record);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, record);
        } else {
            // Duplicate key: refresh the stored reference (should not normally happen
            // since Main checks for duplicate IDs before inserting).
            node.data = record;
            return node;
        }
        return rebalance(node);
    }

    /** Searches for a record by Student ID. O(log n). */
    public StudentRecord search(String studentId) {
        Node current = root;
        while (current != null) {
            int cmp = studentId.compareToIgnoreCase(current.data.getStudentId());
            if (cmp == 0) {
                return current.data;
            }
            current = cmp < 0 ? current.left : current.right;
        }
        return null;
    }

    /** Deletes a record by Student ID, rebalancing as needed. O(log n). */
    public boolean delete(String studentId) {
        int before = size;
        root = deleteRec(root, studentId);
        return size < before;
    }

    private Node deleteRec(Node node, String studentId) {
        if (node == null) {
            return null;
        }
        int cmp = studentId.compareToIgnoreCase(node.data.getStudentId());
        if (cmp < 0) {
            node.left = deleteRec(node.left, studentId);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, studentId);
        } else {
            size--;
            if (node.left == null || node.right == null) {
                return (node.left != null) ? node.left : node.right;
            }
            // Two children: replace with in-order successor (smallest in right subtree)
            Node successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            node.data = successor.data;
            size++; // compensate: the recursive delete below will decrement again
            node.right = deleteRec(node.right, successor.data.getStudentId());
        }
        return node == null ? null : rebalance(node);
    }

    /** Prints all records in ascending Student ID order (in-order traversal). */
    public void displayInOrder() {
        if (isEmpty()) {
            System.out.println("No student records available in the tree.");
            return;
        }
        System.out.println(StudentRecord.tableHeader());
        inOrderRec(root);
        System.out.println("Total records: " + size + " | Tree height: " + height(root));
    }

    private void inOrderRec(Node node) {
        if (node == null) {
            return;
        }
        inOrderRec(node.left);
        System.out.println(node.data);
        inOrderRec(node.right);
    }
}
