package test.soprasteria.mock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection1 {
	
	private Connection dbConnection;
	
	public void getDBConnection() throws ClassNotFoundException, SQLException {		
		Class.forName("com.mysql.jdbc.Driver");
		dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:6666/jcg", "root", "password");		
	}
	
	public int executeQuery(String query) throws ClassNotFoundException, SQLException {
		System.out.println(query);
		return dbConnection.createStatement().executeUpdate(query);
	}

}
