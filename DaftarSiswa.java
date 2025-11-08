public class DaftarSiswa {
    String idSiswa;
    String namaSiswa;
    String noTelepon;

    public DaftarSiswa(String idSiswa, String namaSiswa, String noTelepon) {
        this.idSiswa = idSiswa;
        this.namaSiswa = namaSiswa;
        this.noTelepon = noTelepon;
    }
    public String getIdSiswa() {
        return idSiswa;
    }
    public String getNamaSiswa() {
        return namaSiswa;
    }
    public String getNoTelepon() {
        return noTelepon;
    }
}
