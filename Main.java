import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static ArrayList<Kelas> daftarKelasTersedia = new ArrayList<>();
    static ArrayList<Siswa> databaseSiswa = new ArrayList<>();
    static ArrayList<Transaksi> riwayatTransaksi = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        tambahDataDummy();
        int pilihan = 0;

        System.out.println("=== SISTEM MANAJEMEN KURSUS XYZ ===");
        System.out.println("Selamat Datang, Admin!\n");

        do {
            System.out.println(" ============MENU UTAMA============");
            System.out.println(" 1. Lihat Daftar Kelas yang Tersedia");
            System.out.println(" 2. Registrasi Siswa Baru           ");
            System.out.println(" 3. Proses Pembayaran               ");
            System.out.println(" 4. Pendaftaran Kelas (untuk Siswa) ");
            System.out.println(" 5. Lihat Riwayat Transaksi         ");
            System.out.println(" 6. Lihat Daftar Semua Siswa        ");
            System.out.println(" 7. Lihat Daftar Siswa per Kelas    ");
            System.out.println(" 8. Keluar                          ");
            System.out.print("   Pilih menu (1-8): ");

            try {
                pilihan = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid! Masukkan angka 1-8.");
                pilihan = 0;
            } finally {
                scanner.nextLine();
            }

            switch (pilihan) {
                case 1 -> tampilkanDaftarKelas();
                case 2 -> registrasiSiswaBaru();
                case 3 -> prosesPembayaran();
                case 4 -> pendaftaranKelas();
                case 5 -> lihatRiwayatTransaksi();
                case 6 -> lihatDaftarSemuaSiswa();
                case 7 -> lihatDaftarSiswaPerKelas();
                case 8 -> System.out.println("\nTerima kasih telah menggunakan sistem ini. Sampai jumpa!");
                default -> {
                    if (pilihan != 0) System.out.println("Pilihan tidak valid. Silakan pilih 1-8.");
                }
            }
            System.out.println();
        } while (pilihan != 8);

        scanner.close();
    }
    //menampilkan daftar kelas
    public static void tampilkanDaftarKelas() {
        System.out.println("\n=== DAFTAR KELAS TERSEDIA ===");
        if (daftarKelasTersedia.isEmpty()) {
            System.out.println("Belum ada kelas tersedia.");
            return;
        }
        System.out.println("─────────────────────────────────────────────────────────────────");
        System.out.printf("| %-8s | %-25s | %-15s | %-12s |\n", "Kode", "Nama Kelas", "Pengajar", "Harga");
        System.out.println("─────────────────────────────────────────────────────────────────");
        for (Kelas k : daftarKelasTersedia) {
            System.out.printf("| %-8s | %-25s | %-15s | Rp%,-9.0f |\n",
                    k.getKodeKelas(), k.getNamaKelas(), k.getPengajar(), k.getHarga());
        }
        System.out.println("─────────────────────────────────────────────────────────────────");
    }
    //Membuat form registrasi siswa baru
    public static Siswa registrasiSiswaBaru() {
        System.out.println("\n=== REGISTRASI SISWA BARU ===");
        System.out.print("Masukkan Nama Siswa: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan No. Telepon: ");
        String noTelp = scanner.nextLine();

        String idSiswa = "S-" + String.format("%03d", databaseSiswa.size() + 1);
        Siswa siswaBaru = new Siswa(idSiswa, nama, noTelp);
        databaseSiswa.add(siswaBaru);

        System.out.println("Siswa berhasil ditambahkan!");
        System.out.println("ID Siswa: " + idSiswa + " | Nama: " + nama);
        return siswaBaru;
    }
    //Proses pembayaran untuk transaksi yang belum lunas
    public static void prosesPembayaran() {
        System.out.println("\n=== PROSES PEMBAYARAN ===");
        if (riwayatTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi yang perlu dibayar.");
            return;
        }

        System.out.print("Masukkan ID Transaksi: ");
        String idTransaksi = scanner.nextLine();
        Transaksi trx = findTransaksiById(idTransaksi);

        if (trx == null) {
            System.out.println("Transaksi tidak ditemukan.");
            return;
        }
        if (trx.getStatusBayar().equals("LUNAS")) {
            System.out.println("Transaksi ini sudah lunas.");
            return;
        }

        Siswa siswa = findSiswaById(trx.getIdSiswa());
        Kelas kelas = findKelasByKode(trx.getKodeKelas());

        System.out.println("─── DETAIL TAGIHAN ───");
        System.out.println("ID Transaksi: " + trx.getIdTransaksi());
        System.out.println("Siswa       : " + (siswa != null ? siswa.getNamaSiswa() : "N/A"));
        System.out.println("Kelas       : " + (kelas != null ? kelas.getNamaKelas() : "N/A"));
        System.out.printf("Total Bayar : Rp%,.0f\n", trx.getTotalBayar());

        double jumlahBayar = 0;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.print("Masukkan Jumlah Uang (Rp): ");
                jumlahBayar = scanner.nextDouble();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        }

        if (jumlahBayar < trx.getTotalBayar()) {
            System.out.println("Uang tidak cukup!");
        } else {
            double kembalian = jumlahBayar - trx.getTotalBayar();
            trx.setStatusBayar("LUNAS");
            System.out.println("\n=== STRUK PEMBAYARAN ===");
            System.out.println("PEMBAYARAN BERHASIL!");
            System.out.printf("Total Tagihan: Rp%,.0f\n", trx.getTotalBayar());
            System.out.printf("Uang Dibayar : Rp%,.0f\n", jumlahBayar);
            System.out.printf("Kembalian    : Rp%,.0f\n", kembalian);
        }
    }
    //Proses pendaftaran kelas untuk siswa
    public static void pendaftaranKelas() {
        System.out.println("\n=== PENDAFTARAN KELAS ===");
        System.out.print("Masukkan ID Siswa (atau 'baru'): ");
        String input = scanner.nextLine();

        Siswa siswa = input.equalsIgnoreCase("baru") ? registrasiSiswaBaru() : findSiswaById(input);
        if (siswa == null) {
            System.out.println("Siswa tidak ditemukan. Registrasi baru...");
            siswa = registrasiSiswaBaru();
        }

        System.out.println("Siswa: " + siswa.getNamaSiswa() + " (ID: " + siswa.getIdSiswa() + ")");
        tampilkanDaftarKelas();

        System.out.print("Pilih Kode Kelas: ");
        String kodeKelas = scanner.nextLine();
        Kelas kelas = findKelasByKode(kodeKelas);

        if (kelas == null) {
            System.out.println("Kode kelas tidak valid.");
            return;
        }

        kelas.tambahSiswa(siswa);

        String idTrx = "T-" + String.format("%03d", riwayatTransaksi.size() + 1);
        Transaksi trx = new Transaksi(idTrx, siswa.getIdSiswa(), kelas.getKodeKelas(), kelas.getHarga(), "BELUM LUNAS");
        riwayatTransaksi.add(trx);

        System.out.println("Pendaftaran berhasil!");
        System.out.println("Kelas: " + kelas.getNamaKelas());
        System.out.println("Total Tagihan: Rp" + String.format("%,.0f", kelas.getHarga()));
        System.out.println("ID Transaksi: " + idTrx);
    }
    //Menampilkan riwayat transaksi
    public static void lihatRiwayatTransaksi() {
        System.out.println("\n=== RIWAYAT TRANSAKSI ===");
        if (riwayatTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }
        System.out.println("─────────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("| %-10s | %-15s | %-20s | %-13s | %-12s |\n", "ID Trx", "Nama Siswa", "Kelas", "Total Bayar", "Status");
        System.out.println("─────────────────────────────────────────────────────────────────────────────────────────────");
        for (Transaksi t : riwayatTransaksi) {
            Siswa s = findSiswaById(t.getIdSiswa());
            Kelas k = findKelasByKode(t.getKodeKelas());
            System.out.printf("| %-10s | %-15s | %-20s | Rp%,-10.0f | %-12s |\n",
                    t.getIdTransaksi(),
                    s != null ? s.getNamaSiswa() : "N/A",
                    k != null ? k.getNamaKelas() : "N/A",
                    t.getTotalBayar(),
                    t.getStatusBayar());
        }
        System.out.println("─────────────────────────────────────────────────────────────────────────────────────────────");
    }
    //Menampilkan daftar semua siswa
    public static void lihatDaftarSemuaSiswa() {
        System.out.println("\n=== DAFTAR SEMUA SISWA ===");
        if (databaseSiswa.isEmpty()) {
            System.out.println("Belum ada siswa terdaftar.");
            return;
        }
        System.out.println("─────────────────────────────────────────────────");
        System.out.printf("| %-10s | %-20s | %-15s |\n", "ID Siswa", "Nama", "No. Telepon");
        System.out.println("─────────────────────────────────────────────────");
        for (Siswa s : databaseSiswa) {
            System.out.printf("| %-10s | %-20s | %-15s |\n", s.getIdSiswa(), s.getNamaSiswa(), s.getNoTelepon());
        }
        System.out.println("─────────────────────────────────────────────────");
    }
    //Menampilkan daftar siswa per kelas
    public static void lihatDaftarSiswaPerKelas() {
        System.out.println("\n=== DAFTAR SISWA PER KELAS ===");
        if (daftarKelasTersedia.isEmpty()) {
            System.out.println("Belum ada kelas tersedia.");
            return;
        }
        tampilkanDaftarKelas();
        System.out.print("Masukkan Kode Kelas: ");
        String kode = scanner.nextLine().trim();
        Kelas kelas = findKelasByKode(kode);

        if (kelas == null) {
            System.out.println("Kelas tidak ditemukan.");
            return;
        }

        System.out.println("\nSiswa di kelas: " + kelas.getNamaKelas());
        if (kelas.getDaftarSiswa().isEmpty()) {
            System.out.println("Belum ada siswa yang terdaftar.");
        } else {
            System.out.println("─────────────────────────────────────────────────");
            System.out.printf("| %-10s | %-20s | %-15s |\n", "ID", "Nama", "Telepon");
            System.out.println("─────────────────────────────────────────────────");
            for (Siswa s : kelas.getDaftarSiswa()) {
                System.out.printf("| %-10s | %-20s | %-15s |\n", s.getIdSiswa(), s.getNamaSiswa(), s.getNoTelepon());
            }
            System.out.println("─────────────────────────────────────────────────");
        }
    }
    //Untuk data awal(dummy)
    private static void tambahDataDummy() {
        // === 6 KELAS ===
        Kelas[] kelasList = {
            new Kelas("MAT-01", "Matematika SMA Kelas 12", "Bapak Budi", 500_000),
            new Kelas("FIS-01", "Fisika SMA Intensif", "Ibu Ani", 550_000),
            new Kelas("BIG-01", "Bahasa Inggris TOEFL", "Mr. John", 450_000),
            new Kelas("KIM-01", "Kimia SMA Olimpiade", "Bapak Eko", 520_000),
            new Kelas("BIO-01", "Biologi SMA UTBK", "Ibu Rina", 480_000),
            new Kelas("INF-01", "Informatika Python", "Bapak Diki", 600_000)
        };
    
        for (Kelas k : kelasList) {
            daftarKelasTersedia.add(k);
        }
    
        // === 60 SISWA (10 per kelas) ===
        String[] namaDepan = {"Andi", "Budi", "Cici", "Dedi", "Eka", "Fani", "Galih", "Hani", "Ika", "Joko"};
        String[] namaBelakang = {"Saputra", "Wijaya", "Lestari", "Pratama", "Sari", "Nugroho", "Rahayu", "Kusuma", "Putri", "Santoso"};
        int siswaCounter = 1;
    
        for (int i = 0; i < 6; i++) { // 6 kelas
            Kelas kelas = kelasList[i];
            for (int j = 0; j < 10; j++) { // 10 siswa per kelas
                String id = "S-" + String.format("%03d", siswaCounter++);
                String nama = namaDepan[j] + " " + namaBelakang[(i + j) % 10];
                String telp = "08" + String.format("%09d", (long)(Math.random() * 1_000_000_000));
    
                Siswa siswa = new Siswa(id, nama, telp);
                databaseSiswa.add(siswa);
                kelas.tambahSiswa(siswa); // Daftarkan ke kelas
    
                // Buat transaksi otomatis
                String status = (j % 3 == 0) ? "LUNAS" : "BELUM LUNAS"; // 1/3 LUNAS
                String idTrx = "T-" + String.format("%03d", riwayatTransaksi.size() + 1);
                Transaksi trx = new Transaksi(idTrx, id, kelas.getKodeKelas(), kelas.getHarga(), status);
                riwayatTransaksi.add(trx);
            }
        }
    
        System.out.println("Data dummy berhasil dimuat: 6 kelas, 60 siswa, 60 transaksi.");
    }
    //Helper methods
    private static Siswa findSiswaById(String id) {
        for (Siswa s : databaseSiswa) {
            if (s.getIdSiswa().equalsIgnoreCase(id)) return s;
        }
        return null;
    }

    private static Kelas findKelasByKode(String kode) {
        for (Kelas k : daftarKelasTersedia) {
            if (k.getKodeKelas().equalsIgnoreCase(kode)) return k;
        }
        return null;
    }

    private static Transaksi findTransaksiById(String id) {
        for (Transaksi t : riwayatTransaksi) {
            if (t.getIdTransaksi().equalsIgnoreCase(id)) return t;
        }
        return null;
    }
}