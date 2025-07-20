package com.uas.klique.model;

public class Ruangan {
    private int id;
    private String namaRuangan;
    private String status;
    private int dokterId;

    private String namaDokter; // dari JOIN

    public Ruangan() {
    }

    public Ruangan(int id, String namaRuangan, String status, int dokterId) {
        this.id = id;
        this.namaRuangan = namaRuangan;
        this.status = status;
        this.dokterId = dokterId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaRuangan() {
        return namaRuangan;
    }

    public void setNamaRuangan(String namaRuangan) {
        this.namaRuangan = namaRuangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdDokter() {
        return dokterId;
    }

    public void setIdDokter(int dokterId) {
        this.dokterId = dokterId;
    }

    public String getNamaDokter() {
        return namaDokter;
    }

    public void setNamaDokter(String namaDokter) {
        this.namaDokter = namaDokter;
    }
}
