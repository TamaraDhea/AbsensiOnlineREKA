package ptrekaindo.absensi.assets.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Lokasi implements Serializable {
    @SerializedName("id_lokasi")
    private String idLokasi;
    @SerializedName("nama_lokasi")
    private String namaLokasi;
    @SerializedName("lng")
    private String longtitude;
    @SerializedName("lat")
    private String latitude;
    @SerializedName("radius")
    private String radius;

    public String getIdLokasi() {
        return idLokasi;
    }

    public void setIdLokasi(String idLokasi) {
        this.idLokasi = idLokasi;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }
}
