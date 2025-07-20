package com.uas.klique.util;

import java.sql.*;

public class DbUtil {
    private static final String DB_URL = "jdbc:sqlite:database/klinik.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Inisialisasi saat aplikasi pertama kali jalan
    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Buat tabel user_login
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS user_login (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL
                );
            """);

            // Buat tabel pasien
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pasien (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama TEXT NOT NULL,
                    nik TEXT NOT NULL UNIQUE,
                    tanggal_lahir TEXT,
                    jenis_kelamin TEXT,
                    alamat TEXT,
                    no_telepon TEXT
                );
            """);

            // Buat tabel dokter
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS dokter (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama TEXT NOT NULL,
                    nik TEXT,
                    tanggal_lahir TEXT,
                    alamat TEXT,
                    no_telepon TEXT
                );
            """);

            // Buat tabel ruangan
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS ruangan (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama_ruangan TEXT NOT NULL UNIQUE,
                    id_dokter INTEGER,
                    status TEXT,
                    FOREIGN KEY (id_dokter) REFERENCES dokter(id)
                );
            """);

            // Buat tabel antrian
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS antrian (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nomor_antrian INTEGER,
                    id_pasien INTEGER,
                    id_ruangan INTEGER,
                    status TEXT,
                    tanggal TEXT,
                    FOREIGN KEY (id_pasien) REFERENCES pasien(id),
                    FOREIGN KEY (id_ruangan) REFERENCES ruangan(id)
                );
            """);

            // Seeder: hanya jika user belum ada
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM user_login;");
            if (rs.next() && rs.getInt("total") == 0) {
                stmt.executeUpdate("""
                    INSERT INTO user_login (username, password) VALUES ('admin', 'admin123');
                """);
                System.out.println("✅ User default admin ditambahkan");
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal inisialisasi database:");
            e.printStackTrace();
        }
    }
}
