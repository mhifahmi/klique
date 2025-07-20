package com.uas.klique.service;

import com.uas.klique.dao.AntrianDao;
import com.uas.klique.model.Antrian;

import java.util.List;

public class AntrianService {
    private final AntrianDao antrianDao = new AntrianDao();

    public List<Antrian> getAntrianHariIni() {
        return antrianDao.getAntrianHariIni();
    }

    public List<Antrian> getMenungguAntrianHariIni() {
        return antrianDao.getPasienAntrianHariIni();
    }

    public Antrian getAntrianSelanjutnya() {
        return getAntrianHariIni().stream()
                .filter(a -> "Menunggu".equalsIgnoreCase(a.getStatus()))
                .min((a1, a2) -> Integer.compare(a1.getNomorAntrian(), a2.getNomorAntrian()))
                .orElse(null);
    }

    public long countByStatus(String status) {
        return getAntrianHariIni().stream()
                .filter(a -> a.getStatus().equalsIgnoreCase(status))
                .count();
    }

    public int getNextNomorAntrianHariIni(){
        return  antrianDao.getNextNomorAntrianHariIni();
    }

    public void simpanAntrianBaru(Antrian antrian){
        antrianDao.simpanAntrianBaru(antrian);
    }

    public void updateStatus(int idAntrian, String statusBaru) {
        antrianDao.updateStatus(idAntrian, statusBaru);
    }

    public void updateTerlewat(int idAntrian) {
        antrianDao.updateTerlewat(idAntrian);
    }

    public void updateStatusDanRuangan(int idAntrian, String status, int idRuangan) {
        antrianDao.updateStatusDanRuangan(idAntrian, status, idRuangan);
    }

    public void updateDokterId(int idAntrian, int idDokter) {
        antrianDao.updateDokterId(idAntrian, idDokter);
    }


}
