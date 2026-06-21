import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO.java
 * Data Access Object — all SQL operations for the students table.
 */
public class StudentDAO {

    // ── CREATE ───────────────────────────────────────────────────────────────────

    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students (name, roll_no, department, email, phone, marks) VALUES (?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getRollNo());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setDouble(6, s.getMarks());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] addStudent error: " + e.getMessage());
            return false;
        }
    }

    // ── READ ─────────────────────────────────────────────────────────────────────

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY roll_no";
        try (Connection con = DatabaseConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[DAO] getAllStudents error: " + e.getMessage());
        }
        return list;
    }

    public Student getStudentByRollNo(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] getStudentByRollNo error: " + e.getMessage());
        }
        return null;
    }

    public List<Student> searchByName(String name) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] searchByName error: " + e.getMessage());
        }
        return list;
    }

    public List<Student> searchByDepartment(String dept) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE department LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + dept + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] searchByDepartment error: " + e.getMessage());
        }
        return list;
    }

    public List<Student> searchByMarksRange(double min, double max) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE marks BETWEEN ? AND ? ORDER BY marks DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] searchByMarksRange error: " + e.getMessage());
        }
        return list;
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────────

    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET name=?, department=?, email=?, phone=?, marks=? WHERE roll_no=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getDepartment());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setDouble(5, s.getMarks());
            ps.setString(6, s.getRollNo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] updateStudent error: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────────

    public boolean deleteStudent(String rollNo) {
        String sql = "DELETE FROM students WHERE roll_no = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rollNo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DAO] deleteStudent error: " + e.getMessage());
            return false;
        }
    }

    // ── STATISTICS ────────────────────────────────────────────────────────────────

    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection con = DatabaseConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[DAO] getTotalStudents error: " + e.getMessage());
        }
        return 0;
    }

    public Student getTopScorer() {
        String sql = "SELECT * FROM students WHERE marks = (SELECT MAX(marks) FROM students) LIMIT 1";
        try (Connection con = DatabaseConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[DAO] getTopScorer error: " + e.getMessage());
        }
        return null;
    }

    public Student getLowestScorer() {
        String sql = "SELECT * FROM students WHERE marks = (SELECT MIN(marks) FROM students) LIMIT 1";
        try (Connection con = DatabaseConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[DAO] getLowestScorer error: " + e.getMessage());
        }
        return null;
    }

    /** Returns dept → count mapping as formatted string list. */
    public List<String> getDepartmentWiseCount() {
        List<String> result = new ArrayList<>();
        String sql = "SELECT department, COUNT(*) as cnt FROM students GROUP BY department ORDER BY cnt DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement  st  = con.createStatement();
             ResultSet  rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(String.format("%-20s : %d students", rs.getString("department"), rs.getInt("cnt")));
            }
        } catch (SQLException e) {
            System.err.println("[DAO] getDepartmentWiseCount error: " + e.getMessage());
        }
        return result;
    }

    // ── EXPORT ───────────────────────────────────────────────────────────────────

    /** Returns CSV string of all students. */
    public String exportToCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Name,Roll No,Department,Email,Phone,Marks,Grade\n");
        for (Student s : getAllStudents()) {
            sb.append(String.format("%d,\"%s\",%s,%s,%s,%s,%.1f,%s\n",
                    s.getId(), s.getName(), s.getRollNo(), s.getDepartment(),
                    s.getEmail(), s.getPhone(), s.getMarks(), s.getGrade()));
        }
        return sb.toString();
    }

    // ── ADMIN AUTH ───────────────────────────────────────────────────────────────

    public boolean validateAdmin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[DAO] validateAdmin error: " + e.getMessage());
            return false;
        }
    }

    // ── HELPER ───────────────────────────────────────────────────────────────────

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("roll_no"),
                rs.getString("department"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getDouble("marks")
        );
    }
}
