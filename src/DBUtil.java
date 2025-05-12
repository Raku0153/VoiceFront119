import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/emergency_system";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // 결과가 있으면 로그인 성공
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
