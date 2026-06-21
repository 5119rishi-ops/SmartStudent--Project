/**
 * Student.java
 * Model class representing a Student record.
 */
public class Student {

    private int    id;
    private String name;
    private String rollNo;
    private String department;
    private String email;
    private String phone;
    private double marks;

    // ── Constructors ────────────────────────────────────────────────────────────

    public Student() {}

    /** Constructor used when reading from database (includes id). */
    public Student(int id, String name, String rollNo, String department,
                   String email, String phone, double marks) {
        this.id         = id;
        this.name       = name;
        this.rollNo     = rollNo;
        this.department = department;
        this.email      = email;
        this.phone      = phone;
        this.marks      = marks;
    }

    /** Constructor used when creating a new student (no id yet). */
    public Student(String name, String rollNo, String department,
                   String email, String phone, double marks) {
        this(0, name, rollNo, department, email, phone, marks);
    }

    // ── Getters & Setters ────────────────────────────────────────────────────────

    public int    getId()          { return id; }
    public String getName()        { return name; }
    public String getRollNo()      { return rollNo; }
    public String getDepartment()  { return department; }
    public String getEmail()       { return email; }
    public String getPhone()       { return phone; }
    public double getMarks()       { return marks; }

    public void setId(int id)                  { this.id = id; }
    public void setName(String name)           { this.name = name; }
    public void setRollNo(String rollNo)       { this.rollNo = rollNo; }
    public void setDepartment(String dept)     { this.department = dept; }
    public void setEmail(String email)         { this.email = email; }
    public void setPhone(String phone)         { this.phone = phone; }
    public void setMarks(double marks)         { this.marks = marks; }

    // ── Grade helper ─────────────────────────────────────────────────────────────

    public String getGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s', rollNo='%s', dept='%s', marks=%.1f, grade='%s'}",
                id, name, rollNo, department, marks, getGrade());
    }
}
