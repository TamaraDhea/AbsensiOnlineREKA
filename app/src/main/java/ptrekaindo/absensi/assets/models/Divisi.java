package ptrekaindo.absensi.assets.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Divisi implements Serializable {
    @SerializedName("id_divisi")
    private String idDivisi;
    @SerializedName("nama_divisi")
    private String namaDivisi;

    public String getIdDivisi() {
        return idDivisi;
    }

    public void setIdDivisi(String idDivisi) {
        this.idDivisi = idDivisi;
    }

    public String getNamaDivisi() {
        return namaDivisi;
    }

    public void setNamaDivisi(String namaDivisi) {
        this.namaDivisi = namaDivisi;
    }
}
