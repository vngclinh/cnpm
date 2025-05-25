package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    public static Connection con;

    public DAO() {
        if (con == null) {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=garagemanagement;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "123";
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

            try {
                Class.forName(driver);
                con = DriverManager.getConnection(url, user, password);
                System.out.println("connected");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Ví dụ dùng thử
    public static void main(String[] args) {
        new DAO(); // tạo kết nối
    }
}
