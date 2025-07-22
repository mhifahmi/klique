# Klique - Aplikasi Sistem Antrian Klinik

**Klique** adalah sebuah aplikasi desktop berbasis JavaFX yang dirancang untuk membantu klinik kecil atau praktik mandiri dokter dalam mengelola antrian pasien dengan efisien, ringan, dan real-time.

---

## Studi Kasus

Banyak klinik kecil mengalami kesulitan dalam mengatur antrian pasien, terutama saat jumlah kunjungan tinggi. Ketidakjelasan urutan antrian dapat menimbulkan ketidakpuasan dan menyulitkan pencatatan kunjungan pasien.

**Solusi:**
Aplikasi Klique menawarkan fitur utama seperti:
- Pendaftaran pasien.
- Manajemen antrian otomatis.
- Pemantauan status ruangan secara real-time.
- Penyimpanan data lokal menggunakan SQLite agar ringan dan offline-ready.

---

## Fitur Utama

### Menu Awal
- Pemilihan mode: **Pengunjung** atau **Staff**.

### Tampilan Pengunjung (Monitor Ruang Tunggu)
- Melihat daftar antrian yang sedang menunggu.
- Mengetahui nomor antrian yang sedang dilayani.
- Melihat status ruangan (Tersedia / Konsultasi / Istirahat).
- Mengetahui nama dokter yang bertugas di masing-masing ruangan.

### Tampilan Staff (Setelah Login)
#### Dashboard Utama
- Total pasien hari ini.
- Jumlah pasien dalam antrian.
- Pasien yang sudah dipanggil.
- Antrian yang terlewat.
- Ruangan yang tersedia.
- Nomor antrian terakhir yang dipanggil.

#### Manajemen Antrian
- Daftar antrian aktif.
- Tombol aksi: **Panggil Selanjutnya**, **Panggil Terlewat**, **Konsultasi**, **Selesaikan**, **Panggil Ulang**.
- Tambah pasien baru ke antrian.

#### Manajemen Pasien
- Tambah pasien baru.
- Cari pasien berdasarkan nama/NIK.
- Tabel pasien dengan tombol: **Detail**, **Edit**, **Hapus**.

#### Manajemen Ruangan
- Tambah dokter baru.
- Tabel ruangan dan dokter aktif.
- Ubah status ruangan dan dokter yang bertugas.

#### Riwayat Antrian
- Filter berdasarkan tanggal.
- Search bar berdasarkan nama pasien.
- Tabel riwayat kunjungan.

---

## Teknologi yang Digunakan

- **Java 21**
- **JavaFX 23**
- **FXML**
- **CSS**
- **SQLite**
- **Maven**

---

## Cara Clone & Jalankan Aplikasi

### 1. Clone Repository
```bash
git clone https://github.com/username/klique.git
cd klique
```
### 2. Buka di IDE (IntelliJ / VS Code / NetBeans)
Pastikan sudah menginstal Java 21 dan Maven.
Import sebagai Project Maven.
### 3. Jalankan Aplikasi
Jalankan `KliqueApplication.java` sebagai Java Application. UI akan muncul dengan tampilan awal login / mode pengunjung.

---

### Catatan
- Aplikasi berjalan sepenuhnya lokal tanpa koneksi internet.
- Password tidak di-hash (karena ini versi prototipe).
- Username: admin, password: admin123