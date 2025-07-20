package com.uas.klique.dao;

import com.uas.klique.config.DatabaseConfig;
import com.uas.klique.model.Antrian;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntrianDao {

    public List<Antrian> getAntrianHariIni() {
        List<Antrian> result = new ArrayList<>();

        String sql = """
                SELECT\s
                    a.id, a.nomor_antrian, a.pasien_id, a.ruangan_id, a.status, a.tanggal,
                    p.nama AS nama_pasien,
                    d.nama AS nama_dokter
                FROM antrian a
                JOIN pasien p ON a.pasien_id = p.id
                LEFT JOIN ruangan r ON a.ruangan_id = r.id
                LEFT JOIN dokter d ON r.dokter_id = d.id
                WHERE a.tanggal = DATE('now')
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Antrian a = new Antrian();
                a.setId(rs.getInt("id"));
                a.setNomorAntrian(rs.getInt("nomor_antrian"));
                a.setIdPasien(rs.getInt("pasien_id"));
                a.setIdRuangan(rs.getInt("ruangan_id"));
                a.setStatus(rs.getString("status"));
                a.setTanggal(rs.getString("tanggal"));
                a.setNamaPasien(rs.getString("nama_pasien"));
                a.setNamaDokter(rs.getString("nama_dokter"));
                result.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Ambil antrian hari ini" + result.size() + " baris dari SQLite");
        return result;
    }

    public List<Antrian> getPasienAntrianHariIni() {
        List<Antrian> result = new ArrayList<>();

        String sql = """
            SELECT
                a.id, a.nomor_antrian, a.pasien_id, a.ruangan_id, a.status, a.tanggal,
                p.nama AS nama_pasien,
                d.nama AS nama_dokter
            FROM antrian a
            JOIN pasien p ON a.pasien_id = p.id
            LEFT JOIN ruangan r ON a.ruangan_id = r.id
            LEFT JOIN dokter d ON r.dokter_id = d.id
            WHERE a.tanggal = DATE('now')
              AND a.status IN ('Menunggu', 'Terlewat')
            ORDER BY 
                CASE a.status 
                    WHEN 'Terlewat' THEN 0
                    WHEN 'Menunggu' THEN 1
                    ELSE 2
                END,
                a.nomor_antrian ASC
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Antrian a = new Antrian();
                a.setId(rs.getInt("id"));
                a.setNomorAntrian(rs.getInt("nomor_antrian"));
                a.setIdPasien(rs.getInt("pasien_id"));
                a.setIdRuangan(rs.getInt("ruangan_id"));
                a.setStatus(rs.getString("status"));
                a.setTanggal(rs.getString("tanggal"));
                a.setNamaPasien(rs.getString("nama_pasien"));
                a.setNamaDokter(rs.getString("nama_dokter"));
                result.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Ambil antrian hari ini" + result.size() + " baris dari SQLite");
        return result;
    }

    public int getNextNomorAntrianHariIni() {
        String sql = "SELECT MAX(nomor_antrian) FROM antrian WHERE tanggal = DATE('now')";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void simpanAntrianBaru(Antrian a) {
        String sql = """
        INSERT INTO antrian(nomor_antrian, pasien_id, ruangan_id, status, tanggal)
        VALUES (?, ?, ?, ?, ?)
    """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getNomorAntrian());
            stmt.setInt(2, a.getIdPasien());
            if (a.getIdRuangan() != 0) {
                stmt.setInt(3, a.getIdRuangan());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, a.getStatus());
            stmt.setString(5, a.getTanggal());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(int idAntrian, String statusBaru) {
        String sql = "UPDATE antrian SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statusBaru);
            stmt.setInt(2, idAntrian);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTerlewat(int idAntrian) {
        String sql = "UPDATE antrian SET status = ?, ruangan_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Terlewat");
            stmt.setNull(2, java.sql.Types.INTEGER);
            stmt.setInt(3, idAntrian);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatusDanRuangan(int idAntrian, String status, int idRuangan) {
        String sql = "UPDATE antrian SET status = ?, ruangan_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idRuangan);
            stmt.setInt(3, idAntrian);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countByStatusAndTanggal(String status, String tanggal) {
        String sql = "SELECT COUNT(*) FROM antrian WHERE status = ? AND tanggal = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, tanggal);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMaxNomorAntrianByTanggal(String tanggal) {
        String sql = "SELECT MAX(nomor_antrian) FROM antrian WHERE tanggal = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tanggal);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int countTotalPasienHariIni(String tanggal) {
        String sql = "SELECT COUNT(DISTINCT pasien_id) FROM antrian WHERE tanggal = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tanggal);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateDokterId(int idAntrian, int dokterId) {
        String sql = "UPDATE antrian SET dokter_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dokterId);
            stmt.setInt(2, idAntrian);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Antrian> getRiwayatByTanggal(String tanggal) {
        List<Antrian> result = new ArrayList<>();
        String sql = """
        SELECT
            a.nomor_antrian, a.tanggal, a.status,
            p.nama AS nama_pasien, p.nik,
            d.nama AS nama_dokter
        FROM antrian a
        JOIN pasien p ON a.pasien_id = p.id
        LEFT JOIN dokter d ON a.dokter_id = d.id
        WHERE a.tanggal = ?
        ORDER BY a.nomor_antrian
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tanggal);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Antrian a = new Antrian();
                a.setNomorAntrian(rs.getInt("nomor_antrian"));
                a.setTanggal(rs.getString("tanggal"));
                a.setStatus(rs.getString("status"));
                a.setNamaPasien(rs.getString("nama_pasien"));
                a.setPasienNik(rs.getString("nik"));
                a.setNamaDokter(rs.getString("nama_dokter"));
                result.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Antrian> getRiwayatByTanggalRange(String start, String end) {
        List<Antrian> result = new ArrayList<>();
        String sql = """
        SELECT
            a.nomor_antrian, a.tanggal, a.status,
            p.nama AS nama_pasien, p.nik,
            d.nama AS nama_dokter
        FROM antrian a
        JOIN pasien p ON a.pasien_id = p.id
        LEFT JOIN dokter d ON a.dokter_id = d.id
        WHERE a.tanggal BETWEEN ? AND ?
        ORDER BY a.tanggal DESC, a.nomor_antrian ASC
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, start);
            stmt.setString(2, end);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Antrian a = new Antrian();
                a.setNomorAntrian(rs.getInt("nomor_antrian"));
                a.setTanggal(rs.getString("tanggal"));
                a.setStatus(rs.getString("status"));
                a.setNamaPasien(rs.getString("nama_pasien"));
                a.setPasienNik(rs.getString("nik"));
                a.setNamaDokter(rs.getString("nama_dokter"));
                result.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Antrian> getSemuaRiwayat() {
        List<Antrian> result = new ArrayList<>();
        String sql = """
        SELECT
            a.nomor_antrian, a.tanggal, a.status,
            p.nama AS nama_pasien, p.nik,
            d.nama AS nama_dokter
        FROM antrian a
        JOIN pasien p ON a.pasien_id = p.id
        LEFT JOIN dokter d ON a.dokter_id = d.id
        ORDER BY a.tanggal DESC, a.nomor_antrian ASC
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Antrian a = new Antrian();
                a.setNomorAntrian(rs.getInt("nomor_antrian"));
                a.setTanggal(rs.getString("tanggal"));
                a.setStatus(rs.getString("status"));
                a.setNamaPasien(rs.getString("nama_pasien"));
                a.setPasienNik(rs.getString("nik"));
                a.setNamaDokter(rs.getString("nama_dokter"));
                result.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
