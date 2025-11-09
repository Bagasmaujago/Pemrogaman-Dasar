public class Siswa {
    private String idSiswa, namaSiswa, noTelepon;

    public Siswa(String idSiswa, String namaSiswa, String noTelepon) {
        this.idSiswa = idSiswa;
        this.namaSiswa = namaSiswa;
        this.noTelepon = noTelepon;
    }

    public String getIdSiswa() { return idSiswa; }
    public String getNamaSiswa() { return namaSiswa; }
    public String getNoTelepon() { return noTelepon; }
}