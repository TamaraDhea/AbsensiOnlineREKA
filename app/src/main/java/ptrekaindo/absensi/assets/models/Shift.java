package ptrekaindo.absensi.assets.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shift implements Serializable {
    @SerializedName("id_shift")
    private String idShift;
    @SerializedName("nama_shift")
    private String namaShift;
    @SerializedName("jam_mulai")
    private String jamMulai;
    @SerializedName("jam_selesai")
    private String jamSelesai;

    public String getIdShift() {
        return idShift;
    }

    public void setIdShift(String idShift) {
        this.idShift = idShift;
    }

    public String getNamaShift() {
        return namaShift;
    }

    public void setNamaShift(String namaShift) {
        this.namaShift = namaShift;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(String jamSelesai) {
        this.jamSelesai = jamSelesai;
    }
}
