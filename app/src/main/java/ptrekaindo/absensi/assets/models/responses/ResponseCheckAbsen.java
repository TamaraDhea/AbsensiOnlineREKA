package ptrekaindo.absensi.assets.models.responses;

import com.google.gson.annotations.SerializedName;

import ptrekaindo.absensi.assets.models.User;

public class ResponseCheckAbsen {
    @SerializedName("weekend")
    private Boolean weekend;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("absen_masuk")
    private Boolean absenMasuk;
    @SerializedName("absen_pulang")
    private Boolean absenPulang;
    @SerializedName("karyawan")
    private User dataUser;

    public String getTanggal() {
        return tanggal;
    }

    public Boolean getWeekend() {
        return weekend;
    }

    public Boolean getAbsenMasuk() {
        return absenMasuk;
    }

    public Boolean getAbsenPulang() {
        return absenPulang;
    }

    public User getDataUser() {
        return dataUser;
    }
}
