package com.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

	private static Connection conn;

	public static Connection getConn() {
		try {
			// Check if the connection is null or closed
			if (conn == null || conn.isClosed()) {
				// 1. Load the Driver
				// Note: For modern JDBC, this line is often not required but is good practice.
				Class.forName("org.mariadb.jdbc.Driver");

				// 2. Establish the Connection
				String url = "jdbc:mariadb://localhost:3306/airesumedb";
				String user = "root"; // Your database username
				String password = ""; // Your actual DB password

				conn = DriverManager.getConnection(url, user, password);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.err.println("Error connecting to the database: " + e.getMessage());
			// In a real application, you'd want more robust error handling
		}
		return conn;
	}
}