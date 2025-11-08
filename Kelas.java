public class Kelas {
    String kodeKelas;
    String namaKelas;
    String pengajar;
    double harga;

    public Kelas(String kodeKelas, String namaKelas, String pengajar, double harga) {
        this.kodeKelas = kodeKelas;
        this.namaKelas = namaKelas;
        this.pengajar = pengajar;
        this.harga = harga;
    }

    public String getKodeKelas() {
        return kodeKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public String getPengajar() {
        return pengajar;
    }

    public double getHarga() {
        return harga;
    }
}