package ptrekaindo.absensi.assets.helpers.retrofit;

import java.util.List;

import ptrekaindo.absensi.assets.models.Divisi;
import ptrekaindo.absensi.assets.models.Lokasi;
import ptrekaindo.absensi.assets.models.Shift;
import ptrekaindo.absensi.assets.models.User;
import ptrekaindo.absensi.assets.models.responses.ResponseCheckAbsen;
import ptrekaindo.absensi.assets.models.responses.ResponseListAbsensi;
import ptrekaindo.absensi.assets.models.responses.ResponseLogin;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {
    @FormUrlEncoded
    @POST("auth")
    Call<ResponseLogin> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("shift/all")
    Call<List<Shift>> listShift();

    @FormUrlEncoded
    @POST("shift/add")
    Call<ResponseMessage> addShift(
            @Field("nama_shift") String nama_shift,
            @Field("jam_mulai") String jam_mulai,
            @Field("jam_selesai") String jam_selesai
    );

    @FormUrlEncoded
    @POST("shift/edit")
    Call<ResponseMessage> editShift(
            @Field("id_shift") String id_shift,
            @Field("nama_shift") String nama_shift,
            @Field("jam_mulai") String jam_mulai,
            @Field("jam_selesai") String jam_selesai
    );

    @FormUrlEncoded
    @POST("shift/delete")
    Call<ResponseMessage> deleteShift(
            @Field("id") String id_shift
    );

    @GET("karyawan/all")
    Call<List<User>> listKaryawan();

    @FormUrlEncoded
    @POST("karyawan/add")
    Call<ResponseMessage> addKaryawan(
            @Field("nik") String nik,
            @Field("nama") String  nama,
            @Field("telp") String  telp,
            @Field("divisi") String  divisi,
            @Field("email") String  email,
            @Field("id_shift") String  id_shift,
            @Field("username") String  username,
            @Field("password") String  password
    );

    @FormUrlEncoded
    @POST("karyawan/edit")
    Call<ResponseMessage> editKaryawan(
            @Field("id_user") String id_karyawan,
            @Field("nik") String nik,
            @Field("nama") String  nama,
            @Field("telp") String  telp,
            @Field("divisi") String  divisi,
            @Field("email") String  email,
            @Field("id_shift") String  id_shift,
            @Field("username") String  username,
            @Field("password") String  password

    );

    @FormUrlEncoded
    @POST("karyawan/delete")
    Call<ResponseMessage> deleteKaryawan(
            @Field("id") String id
    );

    @GET("divisi/all")
    Call<List<Divisi>> listDivisi();

    @FormUrlEncoded
    @POST("divisi/add")
    Call<ResponseMessage> addDivisi(
            @Field("nama_divisi") String nama_divisi
    );

    @FormUrlEncoded
    @POST("divisi/edit")
    Call<ResponseMessage> editDivisi(
            @Field("id_divisi") String id_divisi,
            @Field("nama_divisi") String nama_divisi
    );

    @FormUrlEncoded
    @POST("divisi/delete")
    Call<ResponseMessage> deleteDivisi(
            @Field("id") String id
    );

    @GET("lokasi/all")
    Call<List<Lokasi>> listLokasi();

    @FormUrlEncoded
    @POST("lokasi/add")
    Call<ResponseMessage> addLokasi(
            @Field("nama_lokasi") String nama_lokasi,
            @Field("lng") String lng,
            @Field("lat") String lat,
            @Field("radius") String radius
    );

    @FormUrlEncoded
    @POST("lokasi/edit")
    Call<ResponseMessage> editLokasi(
            @Field("id_lokasi") String id_lokasi,
            @Field("nama_lokasi") String nama_lokasi,
            @Field("lng") String lng,
            @Field("lat") String lat,
            @Field("radius") String radius
    );

    @FormUrlEncoded
    @POST("lokasi/delete")
    Call<ResponseMessage> deleteLokasi(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("profil")
    Call<User> getUserData(
            @Field("id_user") String id_user
    );

    @FormUrlEncoded
    @POST("profil/edit")
    Call<ResponseMessage> editUser(
            @Field("id_user") String id_user,
            @Field("nik") String nik,
            @Field("nama") String  nama,
            @Field("telp") String  telp,
            @Field("email") String  email,
            @Field("username") String  username,
            @Field("password") String  password
    );

    @GET("absensi/karyawan")
    Call<List<User>> listKaryawanAbsensi();

    @GET("absensi/detail")
    Call<ResponseListAbsensi> listAbsensi(
            @Query("id_user") String id_user,
            @Query("bulan") String bulan,
            @Query("tahun") String tahun
    );

    @FormUrlEncoded
    @POST("absensi/absen")
    Call<ResponseMessage> absenHarian(
            @Field("id_user") String idUser,
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("userfile") String foto_base64
    );

    @FormUrlEncoded
    @POST("absensi/check")
    Call<ResponseCheckAbsen> checkAbsen(
            @Field("id_user") String id_user
    );
}

