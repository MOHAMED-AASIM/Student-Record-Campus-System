package com.sltc.campussystem.util;

import java.util.Scanner;

/**
 * Centralised input-reading and validation helpers used by Main, so the
 * console menu can handle invalid input, empty fields, and out-of-range
 * marks gracefully (Section 4, Requirement 13-14).
 */
public class InputValidator {

    private final Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Reads a required (non-blank) line of text, re-prompting until valid. */
    public String readNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("  Input cannot be empty. Please try again.");
        }
    }

    /** Reads a line of text, allowing it to be blank (used for optional update fields). */
    public String readOptional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /** Reads marks, re-prompting until a valid number in [0, 100] is entered. */
    public double readMarks(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double marks = Double.parseDouble(line);
                if (marks < 0 || marks > 100) {
                    System.out.println("  Marks must be between 0 and 100. Please try again.");
                    continue;
                }
                return marks;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number. Please enter marks as a number (e.g., 75.5).");
            }
        }
    }

    /**
     * Reads marks for an update, allowing a blank entry to mean "leave unchanged".
     * Returns null when the user leaves the field blank.
     */
    public Double readOptionalMarks(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                return null;
            }
            try {
                double marks = Double.parseDouble(line);
                if (marks < 0 || marks > 100) {
                    System.out.println("  Marks must be between 0 and 100. Please try again.");
                    continue;
                }
                return marks;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number. Please enter marks as a number, or leave blank to keep unchanged.");
            }
        }
    }

    /** Reads a menu choice as an integer within [min, max], re-prompting on invalid input. */
    public int readMenuChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice < min || choice > max) {
                    System.out.println("  Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a valid menu number.");
            }
        }
    }
}
