public class Transaksi {
    String idTransaksi;
    String idSiswa;
    String kodeKelas;
    double totalBayar;
    String statusBayar; 

    public Transaksi(String idTransaksi, String idSiswa, String kodeKelas, double totalBayar, String statusBayar) {
        this.idTransaksi = idTransaksi;
        this.idSiswa = idSiswa;
        this.kodeKelas = kodeKelas;
        this.totalBayar = totalBayar;
        this.statusBayar = statusBayar;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getIdSiswa() {
        return idSiswa;
    }

    public String getKodeKelas() {
        return kodeKelas;
    }

    public double getTotalBayar() {
        return totalBayar;
    }

    public String getStatusBayar() {
        return statusBayar;
    }

    public void setStatusBayar(String statusBayar) {
        this.statusBayar = statusBayar;
    }
}