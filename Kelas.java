import java.util.ArrayList;

public class Kelas {
    private String kodeKelas, namaKelas, pengajar;
    private double harga;
    private ArrayList<Siswa> daftarSiswa = new ArrayList<>();

    public Kelas(String kodeKelas, String namaKelas, String pengajar, double harga) {
        this.kodeKelas = kodeKelas;
        this.namaKelas = namaKelas;
        this.pengajar = pengajar;
        this.harga = harga;
    }

    public String getKodeKelas() { return kodeKelas; }
    public String getNamaKelas() { return namaKelas; }
    public String getPengajar() { return pengajar; }
    public double getHarga() { return harga; }
    public ArrayList<Siswa> getDaftarSiswa() { return daftarSiswa; }

    public void tambahSiswa(Siswa siswa) {
        if (!daftarSiswa.contains(siswa)) {
            daftarSiswa.add(siswa);
        }
    }
}