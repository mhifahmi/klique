package com.uas.klique.service;

import java.sql.*;

public class AuthService {
    private Connection conn;

    public AuthService() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/com/uas/klique/db/klinik.db");
    }

    public boolean login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM user_login WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next(); // true jika ada hasil
    }
}
