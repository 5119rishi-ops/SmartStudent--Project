import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection.java
 * Manages JDBC connection to MySQL database.
 * Update DB_URL, DB_USER, DB_PASSWORD to match your MySQL setup.
 */
public class DatabaseConnection {

    private static final String DB_URL      = "jdbc:mysql://localhost:3306/smartstudent?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";          // XAMPP default is empty

    private static Connection connection = null;

    /** Returns a singleton Connection instance. */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("[DB] Connected to MySQL successfully.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-java.jar to classpath.", e);
            }
        }
        return connection;
    }

    /** Closes the connection gracefully. */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DB] Connection closed.");
            } catch (SQLException e) {
                System.err.println("[DB] Error closing connection: " + e.getMessage());
            }
        }
    }
}
