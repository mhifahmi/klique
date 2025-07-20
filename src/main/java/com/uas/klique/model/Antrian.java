package com.uas.klique.model;

public class Antrian {
    private int id;
    private int nomorAntrian;
    private int pasienId;
    private int ruanganId;
    private int dokterId;
    private String status;
    private String tanggal;

    private String namaPasien; // dari JOIN
    private String namaDokter; // dari JOIN
    private String namaRuangan;
    private String pasienNik;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNomorAntrian() { return nomorAntrian; }
    public void setNomorAntrian(int nomorAntrian) { this.nomorAntrian = nomorAntrian; }

    public int getIdPasien() { return pasienId; }
    public void setIdPasien(int pasienId) { this.pasienId = pasienId; }

    public int getIdRuangan() { return ruanganId; }
    public void setIdRuangan(int ruanganId) { this.ruanganId = ruanganId; }

    public int getDokterId() { return dokterId; }
    public void setDokterId(int dokterId) { this.dokterId = dokterId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getNamaPasien() { return namaPasien; }
    public void setNamaPasien(String namaPasien) { this.namaPasien = namaPasien; }

    public String getNamaDokter() { return namaDokter; }
    public void setNamaDokter(String namaDokter) { this.namaDokter = namaDokter; }

    public int getPasienId() {
        return pasienId;
    }

    public void setPasienId(int pasienId) {
        this.pasienId = pasienId;
    }

    public int getRuanganId() {
        return ruanganId;
    }

    public void setRuanganId(int ruanganId) {
        this.ruanganId = ruanganId;
    }

    public String getNamaRuangan() {
        return namaRuangan;
    }

    public void setNamaRuangan(String namaRuangan) {
        this.namaRuangan = namaRuangan;
    }

    public String getPasienNik() {
        return pasienNik;
    }

    public void setPasienNik(String pasienNik) {
        this.pasienNik = pasienNik;
    }
}
