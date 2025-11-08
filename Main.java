import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    static ArrayList<Kelas> daftarKelasTersedia = new ArrayList<>();
    static ArrayList<Siswa> databaseSiswa = new ArrayList<>();
    static ArrayList<Transaksi> riwayatTransaksi = new ArrayList<>();
    static ArrayList<DaftarSiswa> DataSiswa = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);

  
    public static void main(String[] args) {
        tambahDataDummy();

        int pilihan = 0;
        System.out.println("=== SISTEM MANAJEMEN KURSUS XYZ ===");
        System.out.println("Selamat Datang, Admin!");

        // Menggunakan perulangan do-while untuk menu utama
        do {
            // Menampilkan menu sesuai revisi urutan dari Anda
            System.out.println("\nMenu Utama:");
            System.out.println("1. Lihat Daftar Kelas yang Tersedia");
            System.out.println("2. Registrasi Siswa Baru");
            System.out.println("3. Proses Pembayaran");
            System.out.println("4. Pendaftaran Kelas (untuk Siswa)");
            System.out.println("5. Lihat Riwayat Transaksi");
            System.out.println("6. Lihat daftar siswa");
            System.out.println("7. Keluar");
            System.out.print("Pilih menu (1-7): ");

            try {
                pilihan = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid, masukkan angka 1-7.");
                pilihan = 0; // Reset pilihan
            } finally {
                scanner.nextLine(); // Membersihkan buffer scanner
            }

            // Menggunakan percabangan switch-case untuk navigasi menu
            switch (pilihan) {
                case 1:
                    tampilkanDaftarKelas();
                    break;
                case 2:
                    registrasiSiswaBaru();
                    break;
                case 3:
                    prosesPembayaran();
                    break;
                case 4:
                    pendaftaranKelas();
                    break;
                case 5:
                    lihatRiwayatTransaksi();
                    break;
                case 6:
                    System.out.println("\n--- Daftar Siswa ---");
                    for (DaftarSiswa ds : DataSiswa) {
                        System.out.printf("ID: %s, Nama: %s, No. Telepon: %s\n",
                                ds.getIdSiswa(), ds.getNamaSiswa(), ds.getNoTelepon());
                    }
                    break;
                case 7:
                    System.out.println("Terima kasih telah menggunakan sistem ini.");
                    break;
                default:
                    if (pilihan != 0) {
                        System.out.println("Pilihan tidak valid. Silakan pilih 1-7.");
                    }
            }
        } while (pilihan != 7);

        scanner.close();
    }

    
     // Method 1: Menampilkan semua kelas yang tersedia
    public static void tampilkanDaftarKelas() {
        System.out.println("\n--- Daftar Kelas Tersedia ---");
        if (daftarKelasTersedia.isEmpty()) {
            System.out.println("Belum ada kelas yang tersedia.");
            return;
        }

        System.out.println("-----------------------------------------------------------------");
        System.out.printf("| %-7s | %-20s | %-15s | %-10s |\n", "Kode", "Nama Kelas", "Pengajar", "Harga");
        System.out.println("-----------------------------------------------------------------");       
        // Menggunakan perulangan for
        for (Kelas k : daftarKelasTersedia) {
            System.out.printf("| %-7s | %-20s | %-15s | Rp%-9.0f |\n",
                    k.getKodeKelas(), k.getNamaKelas(), k.getPengajar(), k.getHarga());
        }
        System.out.println("-----------------------------------------------------------------");
    }

    /**
     * Method 2: Registrasi siswa baru
     * @return Objek Siswa yang baru dibuat 
     */
    public static Siswa registrasiSiswaBaru() {
        System.out.println("\n--- Registrasi Siswa Baru ---");
        System.out.print("Masukkan Nama Siswa: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan No. Telepon (Contoh: 0812...): ");
        String noTelp = scanner.nextLine();

        // Membuat ID Siswa otomatis 
        String idSiswa = "S-" + String.format("%03d", databaseSiswa.size() + 1);

        // Membuat objek Siswa baru dan menambahkannya ke database
        Siswa siswaBaru = new Siswa(idSiswa, nama, noTelp);
        databaseSiswa.add(siswaBaru);

        System.out.println("Siswa baru berhasil ditambahkan!");
        System.out.println("ID Siswa: " + idSiswa);
        System.out.println("Nama: " + nama);
        return siswaBaru;
    }

    // Method 3: Proses Pembayaran
    public static void prosesPembayaran() {
        System.out.println("\n--- Proses Pembayaran ---");
        if (riwayatTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi yang perlu dibayar.");
            return;
        }

        System.out.print("Masukkan ID Transaksi yang akan dibayar: ");
        String idTransaksi = scanner.nextLine();

        // Mencari transaksi berdasarkan ID 
        Transaksi trx = findTransaksiById(idTransaksi);

        // Validasi percabangan if 
        if (trx == null) {
            System.out.println("Transaksi dengan ID " + idTransaksi + " tidak ditemukan.");
            return;
        }

        // Cek status bayar
        if (trx.getStatusBayar().equals("LUNAS")) {
            System.out.println("Transaksi ini sudah lunas.");
            return;
        }

        // Jika ditemukan dan belum lunas
        Siswa siswa = findSiswaById(trx.getIdSiswa());
        Kelas kelas = findKelasByKode(trx.getKodeKelas());

        System.out.println("--- Detail Tagihan ---");
        System.out.println("ID Transaksi: " + trx.getIdTransaksi());
        System.out.println("Nama Siswa  : " + (siswa != null ? siswa.getNamaSiswa() : "N/A"));
        System.out.println("Nama Kelas  : " + (kelas != null ? kelas.getNamaKelas() : "N/A"));
        System.out.printf("Total Tagihan: Rp%.0f\n", trx.getTotalBayar());

        double jumlahUangBayar = 0;
        boolean inputValid = false;
        // Perulangan while untuk validasi input pembayaran
        while (!inputValid) {
            try {
                System.out.print("Masukkan Jumlah Uang Bayar: Rp");
                jumlahUangBayar = scanner.nextDouble();
                scanner.nextLine(); // clear buffer
                inputValid = true;
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid, masukkan angka.");
                scanner.nextLine(); // clear buffer
            }
        }

        // Logika bisnis pembayaran 
        if (jumlahUangBayar < trx.getTotalBayar()) {
            System.out.println("Uang tidak cukup untuk melakukan pembayaran.");
        } else {
            double kembalian = jumlahUangBayar - trx.getTotalBayar();
            trx.setStatusBayar("LUNAS"); // Mengubah status bayar
            System.out.println("\n--- Struk Pembayaran ---");
            System.out.println("PEMBAYARAN BERHASIL (LUNAS)");
            System.out.printf("Total Bayar : Rp%.0f\n", trx.getTotalBayar());
            System.out.printf("Uang Dibayar: Rp%.0f\n", jumlahUangBayar);
            System.out.printf("Kembalian   : Rp%.0f\n", kembalian);
        }
    }


    // Method 4: Pendaftaran Kelas

    public static void pendaftaranKelas() {
        System.out.println("\n--- Pendaftaran Kelas untuk Siswa ---");
        System.out.print("Masukkan ID Siswa (atau ketik 'baru' untuk registrasi): ");
        String inputId = scanner.nextLine();
        
        Siswa siswa;

        if (inputId.equalsIgnoreCase("baru")) {
            siswa = registrasiSiswaBaru();
        } else {
            siswa = findSiswaById(inputId);
        }

        // Jika siswa masih tidak ditemukan
        if (siswa == null) {
            System.out.println("Siswa tidak ditemukan. Memulai registrasi siswa baru...");
            siswa = registrasiSiswaBaru(); 
        }

        // Jika siswa ditemukan / berhasil dibuat
        System.out.println("Siswa ditemukan: " + siswa.getNamaSiswa() + " (ID: " + siswa.getIdSiswa() + ")");
        
        // Menampilkan daftar kelas
        tampilkanDaftarKelas();
        
        System.out.print("Masukkan Kode Kelas yang ingin didaftar: ");
        String kodeKelas = scanner.nextLine();

        // Validasi kode kelas 
        Kelas kelas = findKelasByKode(kodeKelas);
        if (kelas == null) {
            System.out.println("Kode kelas tidak valid. Pendaftaran dibatalkan.");
            return;
        }

        // Membuat ID Transaksi otomatis 
        String idTransaksi = "T-" + String.format("%03d", riwayatTransaksi.size() + 1);
        double totalBayar = kelas.getHarga();

        // Membuat objek Transaksi baru
        Transaksi trx = new Transaksi(idTransaksi, siswa.getIdSiswa(), kelas.getKodeKelas(), totalBayar, "BELUM LUNAS");
        riwayatTransaksi.add(trx); 

        System.out.println("Pendaftaran kelas berhasil!");
        System.out.println("Siswa: " + siswa.getNamaSiswa());
        System.out.println("Kelas: " + kelas.getNamaKelas());
        System.out.println("Total Tagihan: Rp" + totalBayar);
        System.out.println("Status: BELUM LUNAS");
        System.out.println("ID Transaksi Anda: " + idTransaksi);
    }

    /**
     * Method 5: Lihat Riwayat Transaksi
     */
    public static void lihatRiwayatTransaksi() {
        System.out.println("\n--- Riwayat Transaksi ---");
        if (riwayatTransaksi.isEmpty()) {
            System.out.println("Belum ada riwayat transaksi.");
            return;
        }
        
        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-20s | %-12s | %-12s |\n", "ID Transaksi", "Nama Siswa", "Nama Kelas", "Total Bayar", "Status");
        System.out.println("-----------------------------------------------------------------------------------------------");

        // Menggunakan perulangan for 
        for (Transaksi trx : riwayatTransaksi) {
            //  mencari nama agar laporan mudah dibaca 
            Siswa siswa = findSiswaById(trx.getIdSiswa());
            Kelas kelas = findKelasByKode(trx.getKodeKelas());

            String namaSiswa = (siswa != null) ? siswa.getNamaSiswa() : "N/A";
            String namaKelas = (kelas != null) ? kelas.getNamaKelas() : "N/A";

            System.out.printf("| %-10s | %-15s | %-20s | Rp%-11.0f | %-12s |\n",
                    trx.getIdTransaksi(), namaSiswa, namaKelas, trx.getTotalBayar(), trx.getStatusBayar());
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
    }
    //Method 6: Lihat daftar siswa
    public static void lihatDaftarSiswa() {
        System.out.println("\n--- Daftar Siswa ---");
        if (DataSiswa.isEmpty()) {
            System.out.println("Belum ada siswa terdaftar.");
            return;
        }

        System.out.println("---------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-15s |\n", "ID Siswa", "Nama Siswa", "No. Telepon");
        System.out.println("---------------------------------------------");

        for (DaftarSiswa ds : DataSiswa) {
            System.out.printf("| %-10s | %-20s | %-15s |\n",
                    ds.getIdSiswa(), ds.getNamaSiswa(), ds.getNoTelepon());
        }
        System.out.println("---------------------------------------------");
    }

    // --- HELPER METHODS (Untuk kerapian kode & modularitas [cite: 4]) ---

    /**
     * Menambahkan data dummy kelas
     * Sesuai rancangan [cite: 12]
     */
    private static void tambahDataDummy() {
        daftarKelasTersedia.add(new Kelas("MAT-01", "Matematika SMA", "Bapak Budi", 500000.0));
        daftarKelasTersedia.add(new Kelas("FIS-01", "Fisika SMA", "Ibu Ani", 550000.0));
        daftarKelasTersedia.add(new Kelas("BIG-01", "Bahasa Inggris", "Mr. John", 450000.0));
        daftarKelasTersedia.add(new Kelas("KIM-01", "Kimia SMA", "Bapak Eko", 520000.0));
    }

    /**
     * Helper untuk mencari Siswa berdasarkan ID
     * Digunakan di pendaftaranKelas() dan lihatRiwayatTransaksi() [cite: 19]
     */
    private static Siswa findSiswaById(String idSiswa) {
        for (Siswa s : databaseSiswa) { // Perulangan for [cite: 6]
            if (s.getIdSiswa().equals(idSiswa)) { // Percabangan if [cite: 5]
                return s;
            }
        }
        return null;
    }

    /**
     * Helper untuk mencari Kelas berdasarkan Kode
     * Digunakan di pendaftaranKelas() dan lihatRiwayatTransaksi() [cite: 22]
     */
    private static Kelas findKelasByKode(String kodeKelas) {
        for (Kelas k : daftarKelasTersedia) {
            if (k.getKodeKelas().equalsIgnoreCase(kodeKelas)) {
                return k;
            }
        }
        return null;
    }

    /**
     * Helper untuk mencari Transaksi berdasarkan ID
     * Digunakan di prosesPembayaran() [cite: 25]
     */
    private static Transaksi findTransaksiById(String idTransaksi) {
        for (Transaksi t : riwayatTransaksi) {
            if (t.getIdTransaksi().equalsIgnoreCase(idTransaksi)) {
                return t;
            }
        }
        return null;
    }
    private
}