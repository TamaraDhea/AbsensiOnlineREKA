package ptrekaindo.absensi.assets.models;

import com.google.gson.annotations.SerializedName;

public class JamAbsensi {
    @SerializedName("status")
    private String status;
    @SerializedName("text")
    private String text;

    public String getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }
}
