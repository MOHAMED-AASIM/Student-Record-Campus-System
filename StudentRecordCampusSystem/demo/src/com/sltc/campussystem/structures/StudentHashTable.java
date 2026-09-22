package com.sltc.campussystem.structures;

import com.sltc.campussystem.model.StudentRecord;

/**
 * A hand-written hash table (separate chaining) providing efficient
 * average O(1) search by Student ID (Section 4, Requirement 6).
 *
 * The table resizes (doubles) once the load factor exceeds 0.75, so
 * performance stays close to O(1) as the number of records grows.
 */
public class StudentHashTable {

    private static class Entry {
        String key;
        StudentRecord value;
        Entry next;

        Entry(String key, StudentRecord value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry[] buckets;
    private int capacity;
    private int size;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    public StudentHashTable() {
        this(16);
    }

    public StudentHashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        this.buckets = new Entry[capacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    /** Simple, deterministic hash based on the ID's character codes. */
    private int hash(String key) {
        int hashValue = 0;
        for (int i = 0; i < key.length(); i++) {
            hashValue = (hashValue * 31 + key.charAt(i));
        }
        int index = hashValue % capacity;
        return index < 0 ? index + capacity : index;
    }

    /** Inserts or overwrites the mapping for a Student ID. O(1) average. */
    public void put(String studentId, StudentRecord record) {
        if ((double) (size + 1) / capacity > LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        int index = hash(studentId);
        Entry current = buckets[index];
        while (current != null) {
            if (current.key.equalsIgnoreCase(studentId)) {
                current.value = record; // update existing
                return;
            }
            current = current.next;
        }
        Entry newEntry = new Entry(studentId, record);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
    }

    /** Retrieves a record by Student ID. O(1) average. */
    public StudentRecord get(String studentId) {
        int index = hash(studentId);
        Entry current = buckets[index];
        while (current != null) {
            if (current.key.equalsIgnoreCase(studentId)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /** Removes the mapping for a Student ID, if present. O(1) average. */
    public boolean remove(String studentId) {
        int index = hash(studentId);
        Entry current = buckets[index];
        Entry previous = null;
        while (current != null) {
            if (current.key.equalsIgnoreCase(studentId)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    /** Doubles table capacity and re-hashes every existing entry. */
    private void resize() {
        Entry[] oldBuckets = buckets;
        capacity *= 2;
        buckets = new Entry[capacity];
        int oldSize = size;
        size = 0;
        for (Entry head : oldBuckets) {
            Entry current = head;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
        size = oldSize;
    }

    /** Returns diagnostic info useful for demonstrating hashing behaviour. */
    public String diagnostics() {
        int usedBuckets = 0;
        int maxChain = 0;
        for (Entry head : buckets) {
            if (head != null) {
                usedBuckets++;
            }
            int chainLen = 0;
            Entry current = head;
            while (current != null) {
                chainLen++;
                current = current.next;
            }
            maxChain = Math.max(maxChain, chainLen);
        }
        return String.format("Capacity: %d | Entries: %d | Used buckets: %d | Longest chain: %d",
                capacity, size, usedBuckets, maxChain);
    }
}
