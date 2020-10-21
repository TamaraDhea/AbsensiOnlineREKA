package ptrekaindo.absensi.assets.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import ptrekaindo.absensi.assets.models.User;

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public static final String PREF_NAME = "data_absensi";
    public static final String SP_INTRO = "spIntro";

    public static final String  ID_USER = "IDUSER";
    public static final String NIK = "nik";
    public static final String NAMA = "nama";
    public static final String TELP= "telp";
    public static final String EMAIL="email";
    public static final String FOTO = "foto";
    public static final String DIVISI="divisi";
    public static final String SHIFTID="shift_id";
    public static final String NAMA_SHIFT="nama_shift";
    public static final String USERNAME="username";
    public static final String LEVEL="level";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveSPBoolean(boolean status) {
        editor.putBoolean(SP_INTRO,status);
        editor.apply();
    }

    public boolean checkIntroAccess(){
        return pref.getBoolean(SP_INTRO,false);
    }

    public void setDataUser(User user){
        editor.putString(ID_USER,user.getIdUser());
        editor.putString(NIK,user.getNik());
        editor.putString(NAMA,user.getNama());
        editor.putString(TELP,user.getTelp());
        editor.putString(EMAIL,user.getEmail());
        editor.putString(FOTO,user.getFoto());
        editor.putString(DIVISI,user.getDivisi());
        editor.putString(SHIFTID,user.getShiftId());
        editor.putString(NAMA_SHIFT,user.getNamaShift());
        editor.putString(USERNAME,user.getUsername());
        editor.putString(LEVEL,user.getLevel());
        editor.commit();
    }

    public String getLevelUser(){
        return pref.getString(LEVEL,"");
    }

    public String getIdUser(){
        return pref.getString(ID_USER,"");
    }

    public void clearAllData() {
        editor.clear().commit();
    }


}
