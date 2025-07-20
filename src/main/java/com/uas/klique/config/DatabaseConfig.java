package com.uas.klique.config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String DB_PATH = "database/klique.db";

    public static Connection getConnection() throws SQLException {
        File file = new File(DB_PATH);
        // Buat folder data jika belum ada
        file.getParentFile().mkdirs();
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }
}
