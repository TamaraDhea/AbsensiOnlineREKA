package ptrekaindo.absensi.assets.models;

import com.google.gson.annotations.SerializedName;

public class Absensi {
    @SerializedName("hari")
    private String hari;
    @SerializedName("tgl")
    private String tgl;
    @SerializedName("weekend")
    private Boolean weekend;
    @SerializedName("jam_masuk")
    private JamAbsensi jamMasuk;
    @SerializedName("lokasi_masuk")
    private String lokasiMasuk;
    @SerializedName("jam_pulang")
    private JamAbsensi jamPulang;
    @SerializedName("lokasi_pulang")
    private String lokasiPulang;

    public String getHari() {
        return hari;
    }

    public String getTgl() {
        return tgl;
    }

    public Boolean getWeekend() {
        return weekend;
    }

    public JamAbsensi getJamMasuk() {
        return jamMasuk;
    }

    public String getLokasiMasuk() {
        return lokasiMasuk;
    }

    public JamAbsensi getJamPulang() {
        return jamPulang;
    }

    public String getLokasiPulang() {
        return lokasiPulang;
    }
}
