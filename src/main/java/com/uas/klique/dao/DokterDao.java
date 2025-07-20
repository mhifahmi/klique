package com.uas.klique.dao;

import com.uas.klique.config.DatabaseConfig;
import com.uas.klique.model.Dokter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DokterDao {

    public List<Dokter> getAll() {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Dokter d = new Dokter();
                d.setId(rs.getInt("id"));
                d.setNama(rs.getString("nama"));
                d.setNik(rs.getString("nik"));
                d.setTanggalLahir(rs.getString("tanggal_lahir"));
                d.setAlamat(rs.getString("alamat"));
                d.setNoTelepon(rs.getString("no_telepon"));
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Dokter getById(int id) {
        String sql = "SELECT * FROM dokter WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Dokter d = new Dokter();
                d.setId(rs.getInt("id"));
                d.setNama(rs.getString("nama"));
                d.setNik(rs.getString("nik"));
                d.setTanggalLahir(rs.getString("tanggal_lahir"));
                d.setAlamat(rs.getString("alamat"));
                d.setNoTelepon(rs.getString("no_telepon"));
                return d;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> searchNamaDokter(String keyword) {
        List<String> result = new ArrayList<>();

        String sql = "SELECT nama FROM dokter WHERE nama LIKE ? LIMIT 10";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("nama"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<Integer, String> searchNamaDokterWithId(String keyword) {
        Map<Integer, String> result = new LinkedHashMap<>();
        String sql = "SELECT id, nama FROM dokter WHERE nama LIKE ? LIMIT 10";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.put(rs.getInt("id"), rs.getString("nama"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
