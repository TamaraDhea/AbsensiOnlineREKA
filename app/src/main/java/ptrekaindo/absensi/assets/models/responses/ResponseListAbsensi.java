package ptrekaindo.absensi.assets.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ptrekaindo.absensi.assets.models.Absensi;
import ptrekaindo.absensi.assets.models.Shift;
import ptrekaindo.absensi.assets.models.User;

public class ResponseListAbsensi {
    @SerializedName("karyawan")
    private User karyawan;
    @SerializedName("jam_kerja")
    private Shift jamKerja;
    @SerializedName("bulan")
    private String bulan;
    @SerializedName("tahun")
    private String tahun;
    @SerializedName("hari")
    private List<Absensi> data;

    public User getKaryawan() {
        return karyawan;
    }

    public Shift getJamKerja() {
        return jamKerja;
    }

    public String getBulan() {
        return bulan;
    }

    public String getTahun() {
        return tahun;
    }

    public List<Absensi> getData() {
        return data;
    }
}
