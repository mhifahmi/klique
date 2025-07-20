CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nama TEXT NOT NULL,
    role TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS pasien (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nama TEXT NOT NULL,
    nik TEXT NOT NULL UNIQUE,
    tanggal_lahir TEXT,
    jenis_kelamin TEXT,
    alamat TEXT,
    no_telepon TEXT
);

CREATE TABLE IF NOT EXISTS dokter (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nama TEXT NOT NULL,
    nik TEXT,
    tanggal_lahir TEXT,
    alamat TEXT,
    no_telepon TEXT
);

CREATE TABLE IF NOT EXISTS ruangan (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nama_ruangan TEXT NOT NULL UNIQUE,
    dokter_id INTEGER,
    status TEXT,
    FOREIGN KEY (dokter_id) REFERENCES dokter(id)
);

CREATE TABLE IF NOT EXISTS antrian (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nomor_antrian INTEGER,
    pasien_id INTEGER,
    ruangan_id INTEGER,
    dokter_id INTEGER,
    status TEXT,
    tanggal TEXT,
    FOREIGN KEY (pasien_id) REFERENCES pasien(id),
    FOREIGN KEY (ruangan_id) REFERENCES ruangan(id),
    FOREIGN KEY (dokter_id) REFERENCES dokter(id)
);

-- seed user
INSERT OR IGNORE INTO users (username, password, nama, role)
VALUES ('admin', 'admin123', 'Thor Finn Affianto', 'Staff');

-- seed pasien
INSERT OR IGNORE INTO pasien (nama, nik, tanggal_lahir, jenis_kelamin, alamat, no_telepon) VALUES
('Budi Santoso', '3173020101000001', '1990-01-01', 'Laki-laki', 'Jl. Melati No. 10', '081234567891'),
('Siti Aminah', '3173020101000002', '1992-02-14', 'Perempuan', 'Jl. Mawar No. 5', '081234567892'),
('Andi Wijaya', '3173020101000003', '1985-05-20', 'Laki-laki', 'Jl. Kenanga No. 7', '081234567893'),
('Lina Marlina', '3173020101000004', '1997-08-10', 'Perempuan', 'Jl. Dahlia No. 11', '081234567894'),
('Rahmat Hidayat', '3173020101000005', '1993-12-25', 'Laki-laki', 'Jl. Anggrek No. 2', '081234567895');

-- seed dokter
INSERT OR IGNORE INTO dokter (nama, nik, tanggal_lahir, alamat, no_telepon) VALUES
('dr. Ahmad Fadil', '3173020202000001', '1975-03-12', 'Jl. Cemara No. 3', '082112345678'),
('dr. Nisa Rahma', '3173020202000002', '1980-07-21', 'Jl. Cempaka No. 6', '082112345679'),
('dr. Beni Gunawan', '3173020202000003', '1988-11-30', 'Jl. Flamboyan No. 8', '082112345680'),
('dr. Rina Ayu', '3173020202000004', '1990-06-15', 'Jl. Teratai No. 9', '082112345681'),
('dr. Yusuf Ali', '3173020202000005', '1982-01-05', 'Jl. Bougenville No. 4', '082112345682');

-- seed ruangan
INSERT OR IGNORE INTO ruangan (nama_ruangan, dokter_id, status) VALUES
('K - 1', 1, 'Tersedia'),
('K - 2', 2, 'Tersedia'),
('K - 3', 3, 'Istirahat');

-- seed antrian
INSERT OR IGNORE INTO antrian (nomor_antrian, pasien_id, ruangan_id, dokter_id, status, tanggal) VALUES
(1, 1, 1, NULL, 'Menunggu', '2025-07-17'),
(2, 2, 2, NULL, 'Dilayani', '2025-07-17'),
(3, 3, 3, NULL, 'Menunggu', '2025-07-17'),
(4, 4, 1, 2, 'Selesai', '2025-07-17'),
(5, 5, 2, NULL, 'Menunggu', '2025-07-17');
