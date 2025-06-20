package Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnect {
    private static MySQLConnect instance;
    private static final String URL = "jdbc:mysql://localhost:3306/todolist";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Đổi nếu có mật khẩu
    public static MySQLConnect getInstance() {
        if (instance == null) {
            instance = new MySQLConnect();
        }
        return instance;
    }


    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
