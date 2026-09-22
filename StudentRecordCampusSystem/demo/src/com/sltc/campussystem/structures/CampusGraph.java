package com.sltc.campussystem.structures;

import com.sltc.campussystem.exceptions.DuplicateLocationException;
import com.sltc.campussystem.exceptions.InvalidConnectionException;
import com.sltc.campussystem.exceptions.LocationNotFoundException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An undirected graph representing the campus network: locations are
 * vertices, roads/paths are edges (Section 6, Graph Component).
 *
 * Represented as an adjacency list (a map from location name to the set
 * of its directly connected neighbours). Supports add/remove of both
 * locations and connections, neighbour display, and both BFS and DFS
 * traversal (Section 4, Requirements 7-11).
 */
public class CampusGraph {

    // LinkedHashMap/LinkedHashSet are used purely as ordered containers for
    // the adjacency list; the graph algorithm itself (BFS/DFS, add/remove
    // vertex/edge) is implemented from scratch below.
    private final Map<String, Set<String>> adjacency = new LinkedHashMap<>();

    public int locationCount() {
        return adjacency.size();
    }

    public boolean hasLocation(String location) {
        return adjacency.containsKey(normalize(location));
    }

    public List<String> locations() {
        return new ArrayList<>(adjacency.keySet());
    }

    public List<String> networkLines() {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : adjacency.entrySet()) {
            String neighbours = entry.getValue().isEmpty()
                    ? "(no connections)"
                    : String.join(", ", entry.getValue());
            lines.add(entry.getKey() + "  ->  " + neighbours);
        }
        return lines;
    }

    private String normalize(String location) {
        return location.trim();
    }

    /** Adds a new campus location (vertex) with no connections yet. */
    public void addLocation(String location) throws DuplicateLocationException {
        String key = normalize(location);
        if (adjacency.containsKey(key)) {
            throw new DuplicateLocationException(key);
        }
        adjacency.put(key, new LinkedHashSet<>());
    }

    /** Removes a campus location and every connection/edge referencing it. */
    public void removeLocation(String location) throws LocationNotFoundException {
        String key = normalize(location);
        if (!adjacency.containsKey(key)) {
            throw new LocationNotFoundException(key);
        }
        adjacency.remove(key);
        for (Set<String> neighbours : adjacency.values()) {
            neighbours.remove(key);
        }
    }

    /** Adds an undirected connection/road between two existing locations. */
    public void addConnection(String from, String to) throws LocationNotFoundException, InvalidConnectionException {
        String a = normalize(from);
        String b = normalize(to);
        if (!adjacency.containsKey(a)) {
            throw new LocationNotFoundException(a);
        }
        if (!adjacency.containsKey(b)) {
            throw new LocationNotFoundException(b);
        }
        if (a.equalsIgnoreCase(b)) {
            throw new InvalidConnectionException("A location cannot be connected to itself.");
        }
        if (adjacency.get(a).contains(b)) {
            throw new InvalidConnectionException("A connection between '" + a + "' and '" + b + "' already exists.");
        }
        adjacency.get(a).add(b);
        adjacency.get(b).add(a);
    }

    /** Removes the undirected connection/road between two locations. */
    public void removeConnection(String from, String to) throws LocationNotFoundException, InvalidConnectionException {
        String a = normalize(from);
        String b = normalize(to);
        if (!adjacency.containsKey(a)) {
            throw new LocationNotFoundException(a);
        }
        if (!adjacency.containsKey(b)) {
            throw new LocationNotFoundException(b);
        }
        if (!adjacency.get(a).contains(b)) {
            throw new InvalidConnectionException("No existing connection between '" + a + "' and '" + b + "'.");
        }
        adjacency.get(a).remove(b);
        adjacency.get(b).remove(a);
    }

    /** Prints every location together with its directly connected neighbours. */
    public void displayNetwork() {
        if (adjacency.isEmpty()) {
            System.out.println("No campus locations have been added yet.");
            return;
        }
        System.out.println("Campus Network (location -> connected locations):");
        System.out.println("-".repeat(70));
        for (Map.Entry<String, Set<String>> entry : adjacency.entrySet()) {
            String neighbours = entry.getValue().isEmpty() ? "(no connections)" : String.join(", ", entry.getValue());
            System.out.println(entry.getKey() + "  ->  " + neighbours);
        }
    }

    /** Breadth-first traversal starting from the given location. */
    public List<String> bfs(String start) throws LocationNotFoundException {
        String startKey = normalize(start);
        if (!adjacency.containsKey(startKey)) {
            throw new LocationNotFoundException(startKey);
        }
        List<String> order = new ArrayList<>();
        Set<String> visited = new LinkedHashSet<>();
        Deque<String> queue = new ArrayDeque<>();

        queue.add(startKey);
        visited.add(startKey);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            order.add(current);
            for (String neighbour : adjacency.get(current)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }
        return order;
    }

    /** Depth-first traversal starting from the given location (iterative, uses an explicit stack). */
    public List<String> dfs(String start) throws LocationNotFoundException {
        String startKey = normalize(start);
        if (!adjacency.containsKey(startKey)) {
            throw new LocationNotFoundException(startKey);
        }
        List<String> order = new ArrayList<>();
        Set<String> visited = new LinkedHashSet<>();
        Deque<String> stack = new ArrayDeque<>();

        stack.push(startKey);
        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            order.add(current);
            // Push neighbours in reverse so traversal order matches natural adjacency order
            List<String> neighbours = new ArrayList<>(adjacency.get(current));
            for (int i = neighbours.size() - 1; i >= 0; i--) {
                if (!visited.contains(neighbours.get(i))) {
                    stack.push(neighbours.get(i));
                }
            }
        }
        return order;
    }
}
