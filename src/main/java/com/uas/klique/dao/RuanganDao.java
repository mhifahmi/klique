package com.uas.klique.dao;

import com.uas.klique.config.DatabaseConfig;
import com.uas.klique.model.Ruangan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RuanganDao {
    public Ruangan getById(int id) {
        String sql = "SELECT * FROM ruangan WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Ruangan r = new Ruangan();
                r.setId(rs.getInt("id"));
                r.setNamaRuangan(rs.getString("nama_ruangan"));
                r.setStatus(rs.getString("status"));
                r.setIdDokter(rs.getInt("dokter_id"));
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ruangan> getAllRuangan() {
        List<Ruangan> result = new ArrayList<>();
        String sql = """
            SELECT r.id, r.nama_ruangan, r.status, d.nama AS nama_dokter
            FROM ruangan r
            LEFT JOIN dokter d ON r.dokter_id = d.id
            ORDER BY r.id ASC
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ruangan r = new Ruangan();
                r.setId(rs.getInt("id"));
                r.setNamaRuangan(rs.getString("nama_ruangan"));
                r.setStatus(rs.getString("status"));
                r.setNamaDokter(rs.getString("nama_dokter"));
                result.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean adaRuanganTersedia() {
        String sql = "SELECT 1 FROM ruangan WHERE status = 'Tersedia' LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next(); // true jika ada baris
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Ruangan getRuanganTersedia() {
        String sql = "SELECT * FROM ruangan WHERE status = 'Tersedia' ORDER BY id ASC LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Ruangan r = new Ruangan();
                r.setId(rs.getInt("id"));
                r.setNamaRuangan(rs.getString("nama_ruangan"));
                r.setStatus(rs.getString("status"));
                r.setIdDokter(rs.getInt("dokter_id"));
                return r;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateStatusRuangan(int idRuangan, String status) {
        String sql = "UPDATE ruangan SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idRuangan);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Ruangan> getAllWithDokter() {
        List<Ruangan> list = new ArrayList<>();
        String sql = """
        SELECT r.*, d.nama AS nama_dokter
        FROM ruangan r
        LEFT JOIN dokter d ON r.dokter_id = d.id
    """;

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ruangan r = new Ruangan();
                r.setId(rs.getInt("id"));
                r.setNamaRuangan(rs.getString("nama_ruangan"));
                r.setStatus(rs.getString("status"));
                r.setIdDokter(rs.getInt("dokter_id"));
                r.setNamaDokter(rs.getString("nama_dokter"));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean update(Ruangan ruangan) {
        String sql = "UPDATE ruangan SET dokter_id = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ruangan.getIdDokter());
            stmt.setString(2, ruangan.getStatus());
            stmt.setInt(3, ruangan.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM ruangan";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM ruangan WHERE status = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
