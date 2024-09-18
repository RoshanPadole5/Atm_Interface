package Hosp.HSM;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection11() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

	public static Connection getConnection1() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}
}
