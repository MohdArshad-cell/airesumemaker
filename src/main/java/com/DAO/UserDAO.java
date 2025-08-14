package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.entity.User;

public class UserDAO {

    private Connection conn;

    // Constructor to receive the database connection
    public UserDAO(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * Inserts a new user into the database.
     * @param user The User object containing the user's details.
     * @return true if the user was added successfully, false otherwise.
     */
    public boolean addUser(User user) {
        boolean f = false;
        try {
            // SQL query to insert a new user
            String sql = "INSERT INTO users(full_name, email, password_hash) VALUES(?, ?, ?)";

            // Create a PreparedStatement to safely execute the query
            PreparedStatement ps = conn.prepareStatement(sql);

            // Set the values from the user object
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());

            // Execute the query
            int i = ps.executeUpdate();

            // Check if the query was successful (1 row affected)
            if (i == 1) {
                f = true;
            }

        } catch (SQLException e) {
            // Print the stack trace for debugging purposes
            e.printStackTrace();
        }
        return f;
    }
 // Method to authenticate a user by email and password
    public User getUserByEmailAndPassword(String email, String passwordHash) {
        User user = null;
        try {
            String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, passwordHash);

            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash")); // For completeness
                user.setRegistrationDate(rs.getTimestamp("registration_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}