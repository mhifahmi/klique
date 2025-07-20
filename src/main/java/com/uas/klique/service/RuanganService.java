package com.uas.klique.service;

import com.uas.klique.dao.RuanganDao;
import com.uas.klique.model.Ruangan;

import java.util.List;

public class RuanganService {
    private final RuanganDao ruanganDao = new RuanganDao();

    public List<Ruangan> getAllRuangan() {
        return ruanganDao.getAllRuangan();
    }
}
