import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * UI.java
 * Console-based User Interface for SmartStudent.
 * Handles all user interaction; delegates to AdminService.
 */
public class UI {

    private static final String APP_NAME    = "SmartStudent v1.0";
    private static final String LINE_SINGLE = repeatChar('─', 72);
    private static final String LINE_DOUBLE = repeatChar('═', 72);

    private final AdminService service = new AdminService();
    private final Scanner      scanner = new Scanner(System.in);
    private boolean            loggedIn = false;

    // ── ENTRY POINT ──────────────────────────────────────────────────────────────

    public void start() {
        printBanner();
        if (loginScreen()) {
            loggedIn = true;
            mainMenu();
        }
        System.out.println("\n Thank you for using " + APP_NAME + ". Goodbye!");
        DatabaseConnection.closeConnection();
        scanner.close();
    }

    // ── LOGIN ────────────────────────────────────────────────────────────────────

    private boolean loginScreen() {
        System.out.println(LINE_DOUBLE);
        System.out.println(center("ADMIN LOGIN", 72));
        System.out.println(LINE_DOUBLE);

        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Username : ");
            String username = scanner.nextLine().trim();
            System.out.print("Password : ");
            String password = scanner.nextLine().trim();

            if (service.login(username, password)) {
                System.out.println("\n Login successful! Welcome, " + username + ".\n");
                return true;
            } else {
                attempts++;
                System.out.println("Invalid credentials. Attempts remaining: " + (3 - attempts));
            }
        }
        System.out.println("\n Too many failed attempts. Access denied.");
        return false;
    }

    // ── MAIN MENU ────────────────────────────────────────────────────────────────

    private void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" + LINE_DOUBLE);
            System.out.println(center(APP_NAME + " — Main Menu", 72));
            System.out.println(LINE_DOUBLE);
            System.out.println("1.Add New Student");
            System.out.println("2.View All Students");
            System.out.println("3.Update Student");
            System.out.println("4.Delete Student");
            System.out.println("5.Search Students");
            System.out.println("6.Statistics");
            System.out.println("7.Export to CSV");
            System.out.println("0.Logout");
            System.out.println(LINE_SINGLE);
            System.out.print("  Enter choice: ");

            String choice = scanner.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1": addStudentMenu(); break;
                case "2": viewAllStudents();break;
                case "3": updateStudentMenu();break;
                case "4": deleteStudentMenu();break;
                case "5": searchMenu();break;
                case "6": statsMenu();break;
                case "7": exportCSV();break;
                case "0": running = false;break;
                default:  System.out.println("Invalid choice. Please enter 0–7.");
            }
        }
    }

    // ── ADD STUDENT ──────────────────────────────────────────────────────────────

    private void addStudentMenu() {
        System.out.println(LINE_SINGLE);
        System.out.println(center("ADD NEW STUDENT", 72));
        System.out.println(LINE_SINGLE);

        System.out.print("Full Name:"); String name  = scanner.nextLine().trim();
        System.out.print("Roll No:"); String rollNo = scanner.nextLine().trim();
        System.out.print("Department:"); String dept  = scanner.nextLine().trim();
        System.out.print("Email:"); String email = scanner.nextLine().trim();
        System.out.print("Phone:"); String phone = scanner.nextLine().trim();
        System.out.print("Marks (0-100):");
        double marks = readDouble();

        String result = service.addStudent(name, rollNo, dept, email, phone, marks);
        System.out.println("\n  " + result);
    }

    // ── VIEW ALL ─────────────────────────────────────────────────────────────────

    private void viewAllStudents() {
        List<Student> students = service.getAllStudents();
        printStudentTable(students, "ALL STUDENTS");
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────────

    private void updateStudentMenu() {
        System.out.println(LINE_SINGLE);
        System.out.println(center("UPDATE STUDENT", 72));
        System.out.println(LINE_SINGLE);

        System.out.print("  Enter Roll No to update: ");
        String rollNo = scanner.nextLine().trim();

        Student s = service.findByRollNo(rollNo);
        if (s == null) {
            System.out.println(" No student found with Roll No: " + rollNo);
            return;
        }

        System.out.println("  Current record: " + s);
        System.out.println("  (Press ENTER to keep existing value)\n");

        System.out.print("  Full Name   [" + s.getName() + "]: "); String name  = scanner.nextLine().trim();
        System.out.print("  Department  [" + s.getDepartment() + "]: "); String dept  = scanner.nextLine().trim();
        System.out.print("  Email       [" + s.getEmail() + "]: "); String email = scanner.nextLine().trim();
        System.out.print("  Phone       [" + s.getPhone() + "]: "); String phone = scanner.nextLine().trim();
        System.out.print("  Marks       [" + s.getMarks() + "]: "); String marksInput = scanner.nextLine().trim();

        // Use existing if blank
        if (name.isEmpty())  name  = s.getName();
        if (dept.isEmpty())  dept  = s.getDepartment();
        if (email.isEmpty()) email = s.getEmail();
        if (phone.isEmpty()) phone = s.getPhone();
        double marks = marksInput.isEmpty() ? s.getMarks() : parseDouble(marksInput, s.getMarks());

        String result = service.updateStudent(rollNo, name, dept, email, phone, marks);
        System.out.println("\n  " + result);
    }

    // ── DELETE ───────────────────────────────────────────────────────────────────

    private void deleteStudentMenu() {
        System.out.println(LINE_SINGLE);
        System.out.println(center("DELETE STUDENT", 72));
        System.out.println(LINE_SINGLE);

        System.out.print("  Enter Roll No to delete: ");
        String rollNo = scanner.nextLine().trim();

        Student s = service.findByRollNo(rollNo);
        if (s == null) {
            System.out.println("   No student found with Roll No: " + rollNo);
            return;
        }

        System.out.println("  Found: " + s);
        System.out.print("    Are you sure you want to delete? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            System.out.println("\n  " + service.deleteStudent(rollNo));
        } else {
            System.out.println("  Deletion cancelled.");
        }
    }

    // ── SEARCH MENU ──────────────────────────────────────────────────────────────

    private void searchMenu() {
        System.out.println(LINE_SINGLE);
        System.out.println(center("SEARCH STUDENTS", 72));
        System.out.println(LINE_SINGLE);
        System.out.println("1. Search by Roll Number");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Department");
        System.out.println("4. Search by Marks Range");
        System.out.print("Enter choice:");
        String choice = scanner.nextLine().trim();
        System.out.println();

        switch (choice) {
            case "1": {
                System.out.print("  Roll No: ");
                Student s = service.findByRollNo(scanner.nextLine().trim());
                if (s == null) {
                    System.out.println("  No student found.");
                } else {
                    List<Student> single = new java.util.ArrayList<>();
                    single.add(s);
                    printStudentTable(single, "SEARCH RESULT");
                }
                break;
            }
            case "2": {
                System.out.print("  Name (partial OK): ");
                List<Student> res = service.searchByName(scanner.nextLine().trim());
                printStudentTable(res, "SEARCH BY NAME");
                break;
            }
            case "3": {
                System.out.print("  Department (partial OK): ");
                List<Student> res = service.searchByDept(scanner.nextLine().trim());
                printStudentTable(res, "SEARCH BY DEPARTMENT");
                break;
            }
            case "4": {
                System.out.print("  Minimum marks: "); double min = readDouble();
                System.out.print("  Maximum marks: "); double max = readDouble();
                List<Student> res = service.searchByMarks(min, max);
                printStudentTable(res, String.format("MARKS RANGE [%.1f – %.1f]", min, max));
                break;
            }
            default: System.out.println(" Invalid choice.");
        }
    }

    // ── STATISTICS ───────────────────────────────────────────────────────────────

    private void statsMenu() {
        System.out.println(LINE_SINGLE);
        System.out.println(center("STATISTICS", 72));
        System.out.println(LINE_SINGLE);

        System.out.println("  Total Students  : " + service.totalStudents());

        Student top = service.topScorer();
        if (top != null)
            System.out.printf("  Highest Marks   : %.1f  (%s — %s)%n", top.getMarks(), top.getName(), top.getRollNo());

        Student low = service.lowestScorer();
        if (low != null)
            System.out.printf("  Lowest  Marks   : %.1f  (%s — %s)%n", low.getMarks(), low.getName(), low.getRollNo());

        System.out.println("\n  Department-wise Student Count:");
        System.out.println("  " + LINE_SINGLE.substring(0, 40));
        for (String line : service.deptWiseCount())
            System.out.println("    " + line);
    }

    // ── EXPORT CSV ───────────────────────────────────────────────────────────────

    private void exportCSV() {
        String filename = "students_export.csv";
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(service.exportCSV());
            System.out.println(" Exported to '" + filename + "' successfully.");
        } catch (IOException e) {
            System.out.println(" Export failed: " + e.getMessage());
        }
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────────

    /** Renders a list of students in a formatted table. */
    private void printStudentTable(List<Student> students, String title) {
        System.out.println(LINE_SINGLE);
        System.out.println(center(title + " (" + students.size() + " record" + (students.size() != 1 ? "s" : "") + ")", 72));
        System.out.println(LINE_SINGLE);

        if (students.isEmpty()) {
            System.out.println("  No records found.");
            return;
        }

        String fmt = "  %-5s %-20s %-10s %-18s %-8s %-6s %s%n";
        System.out.printf(fmt, "ID", "Name", "Roll No", "Department", "Phone", "Marks", "Grade");
        System.out.println("  " + repeatChar('─', 70));
        for (Student s : students) {
            System.out.printf(fmt,
                    s.getId(),
                    truncate(s.getName(), 19),
                    s.getRollNo(),
                    truncate(s.getDepartment(), 17),
                    s.getPhone(),
                    String.format("%.1f", s.getMarks()),
                    s.getGrade());
        }
        System.out.println("  " + repeatChar('─', 70));
    }

    private double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(" Please enter a valid number: ");
            }
        }
    }

    private double parseDouble(String s, double fallback) {
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return fallback; }
    }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static String center(String text, int width) {
        int padding = (width - text.length()) / 2;
        return repeatChar(' ', Math.max(0, padding)) + text;
    }

    /** Java 8-compatible replacement for String.repeat() (added in Java 11). */
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }

    private void printBanner() {
        System.out.println("\n" + LINE_DOUBLE);
        System.out.println(center("╔══════════════════════════════╗", 72));
        System.out.println(center("║    S M A R T S T U D E N T   ║", 72));
        System.out.println(center("║  Student Management System   ║", 72));
        System.out.println(center("╚══════════════════════════════╝", 72));
        System.out.println(center("Java + JDBC + MySQL", 72));
        System.out.println(LINE_DOUBLE + "\n");
    }
}
