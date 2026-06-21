import java.util.List;

/**
 * AdminService.java
 * Business logic layer — bridges UI and DAO.
 * All validation happens here so DAO stays clean.
 */
public class AdminService {

    private final StudentDAO dao = new StudentDAO();

    // ── AUTH ─────────────────────────────────────────────────────────────────────

    /**
     * Validates admin credentials.
     * First checks MySQL admins table; falls back to static credentials.
     */
    public boolean login(String username, String password) {
        // Try DB auth first
        try {
            return dao.validateAdmin(username, password);
        } catch (Exception e) {
            // Fallback: static credentials if DB unavailable
            return "admin".equals(username) && "admin123".equals(password);
        }
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────────

    public String addStudent(String name, String rollNo, String dept,
                             String email, String phone, double marks) {
        if (name == null || name.trim().isEmpty())   return "❌ Name cannot be empty.";
        if (rollNo == null || rollNo.trim().isEmpty()) return "❌ Roll No cannot be empty.";
        if (dept == null || dept.trim().isEmpty())   return "❌ Department cannot be empty.";
        if (marks < 0 || marks > 100)                return "❌ Marks must be between 0 and 100.";

        // Check duplicate roll no
        if (dao.getStudentByRollNo(rollNo.trim()) != null)
            return "❌ Roll No '" + rollNo + "' already exists.";

        Student s = new Student(name.trim(), rollNo.trim(), dept.trim(),
                email == null ? "" : email.trim(),
                phone == null ? "" : phone.trim(), marks);

        return dao.addStudent(s)
                ? "✅ Student '" + name + "' added successfully."
                : "❌ Failed to add student. Check DB connection.";
    }

    public List<Student> getAllStudents() {
        return dao.getAllStudents();
    }

    public Student findByRollNo(String rollNo) {
        return dao.getStudentByRollNo(rollNo);
    }

    public String updateStudent(String rollNo, String name, String dept,
                                String email, String phone, double marks) {
        Student existing = dao.getStudentByRollNo(rollNo);
        if (existing == null)
            return "❌ No student found with Roll No: " + rollNo;
        if (marks < 0 || marks > 100)
            return "❌ Marks must be between 0 and 100.";

        existing.setName(name.trim());
        existing.setDepartment(dept.trim());
        existing.setEmail(email == null ? "" : email.trim());
        existing.setPhone(phone == null ? "" : phone.trim());
        existing.setMarks(marks);

        return dao.updateStudent(existing)
                ? "✅ Student '" + rollNo + "' updated successfully."
                : "❌ Update failed. Check DB connection.";
    }

    public String deleteStudent(String rollNo) {
        if (dao.getStudentByRollNo(rollNo) == null)
            return "❌ No student found with Roll No: " + rollNo;
        return dao.deleteStudent(rollNo)
                ? "✅ Student '" + rollNo + "' deleted successfully."
                : "❌ Deletion failed. Check DB connection.";
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────────

    public List<Student> searchByName(String name)         { return dao.searchByName(name); }
    public List<Student> searchByDept(String dept)         { return dao.searchByDepartment(dept); }
    public List<Student> searchByMarks(double min, double max) { return dao.searchByMarksRange(min, max); }

    // ── STATISTICS ────────────────────────────────────────────────────────────────

    public int           totalStudents()    { return dao.getTotalStudents(); }
    public Student       topScorer()        { return dao.getTopScorer(); }
    public Student       lowestScorer()     { return dao.getLowestScorer(); }
    public List<String>  deptWiseCount()    { return dao.getDepartmentWiseCount(); }

    // ── EXPORT ────────────────────────────────────────────────────────────────────

    public String exportCSV() { return dao.exportToCSV(); }
}
