package com.uas.klique.dao;

import com.uas.klique.config.DatabaseConfig;
import com.uas.klique.model.Pasien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PasienDao {

    public List<String> searchNamaPasien(String keyword) {
        List<String> result = new ArrayList<>();

        String sql = "SELECT nama FROM pasien WHERE nama LIKE ? LIMIT 10";

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

    public Map<Integer, String> searchNamaPasienWithId(String keyword) {
        Map<Integer, String> result = new LinkedHashMap<>();
        String sql = "SELECT id, nama FROM pasien WHERE nama LIKE ? LIMIT 10";

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

    public List<Pasien> getAll() {
        List<Pasien> list = new ArrayList<>();

        String sql = "SELECT * FROM pasien";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pasien p = new Pasien();
                p.setId(rs.getInt("id"));
                p.setNama(rs.getString("nama"));
                p.setNik(rs.getString("nik"));
                p.setTanggalLahir(rs.getString("tanggal_lahir"));
                p.setJenisKelamin(rs.getString("jenis_kelamin"));
                p.setAlamat(rs.getString("alamat"));
                p.setNoTelepon(rs.getString("no_telepon"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(Pasien pasien) {
        String sql = "INSERT INTO pasien (nama, nik, alamat, tanggal_lahir, jenis_kelamin, no_telepon) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pasien.getNama());
            stmt.setString(2, pasien.getNik());
            stmt.setString(3, pasien.getAlamat());
            stmt.setString(4, pasien.getTanggalLahir());
            stmt.setString(5, pasien.getJenisKelamin());
            stmt.setString(6, pasien.getNoTelepon());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(Pasien pasien) {
        String sql = "UPDATE pasien SET nama = ?, nik = ?, alamat = ?, tanggal_lahir = ?, jenis_kelamin = ?, no_telepon = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pasien.getNama());
            stmt.setString(2, pasien.getNik());
            stmt.setString(3, pasien.getAlamat());
            stmt.setString(4, pasien.getTanggalLahir());
            stmt.setString(5, pasien.getJenisKelamin());
            stmt.setString(6, pasien.getNoTelepon());
            stmt.setInt(7, pasien.getId());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM pasien WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
