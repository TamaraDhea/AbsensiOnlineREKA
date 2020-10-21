package ptrekaindo.absensi.absensi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.helpers.PrefManager;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.responses.ResponseCheckAbsen;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsenHarianActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_nama_karyawan)
    TextView tvNamaKaryawan;
    @BindView(R.id.tv_nama_divisi)
    TextView tvNamaDivisi;
    @BindView(R.id.tv_nama_shift)
    TextView tvNamaShift;
    @BindView(R.id.tv_absensi_hari)
    TextView tvAbsensiHari;
    @BindView(R.id.cv_info)
    CardView cvInfo;
    @BindView(R.id.btn_masuk)
    Button btnMasuk;
    @BindView(R.id.btn_pulang)
    Button btnPulang;
    @BindView(R.id.lyt_btn)
    LinearLayout lytBtn;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    private Context context;
    private ProgressDialog loading;
    private ApiServices apiServices = ApiUtils.getApiServices();
    private PrefManager prefManager;
    private String lat, lng;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_harian);
        ButterKnife.bind(this);
        context = this;
        toolbar.setTitle("Absen Hari ini");
        requestLocationPermission();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        loading = new ProgressDialog(context);
        loading.setTitle(null);
        loading.setMessage("Memproses absensi");
        loading.setCancelable(false);
        prefManager = new PrefManager(context);
        prepareAbsensi();
    }

    private void prepareAbsensi() {
        loading.setMessage("Memuat data...");
        loading.show();
        apiServices.checkAbsen(
                prefManager.getIdUser()
        ).enqueue(new Callback<ResponseCheckAbsen>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<ResponseCheckAbsen> call, @NotNull Response<ResponseCheckAbsen> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        tvAbsensiHari.setText("Absensi Pada Tanggal : " + response.body().getTanggal());
                        tvNamaKaryawan.setText(response.body().getDataUser().getNama());
                        tvNamaDivisi.setText(response.body().getDataUser().getNamaDivisi());
                        tvNamaShift.setText(response.body().getDataUser().getNamaShift());
                        if (response.body().getWeekend()){
                            lytBtn.setVisibility(View.GONE);
                            tvInfo.setText("Tidak ada absen untuk akhir pekan");
                            tvInfo.setVisibility(View.VISIBLE);
                        }else{
                            if (response.body().getAbsenMasuk() && response.body().getAbsenPulang()) {
                                lytBtn.setVisibility(View.GONE);
                                tvInfo.setText("Anda telah melakukan absensi hari ini");
                                tvInfo.setVisibility(View.VISIBLE);
                            }else {
                                lytBtn.setVisibility(View.VISIBLE);
                                tvInfo.setVisibility(View.GONE);
                                if (response.body().getAbsenMasuk()){
                                    btnMasuk.setEnabled(false);
                                    btnMasuk.setBackground(context.getDrawable(R.drawable.bg_disable));
                                    btnPulang.setEnabled(true);
                                }else{
                                    btnMasuk.setEnabled(true);
                                    btnPulang.setEnabled(false);
                                    btnPulang.setBackground(context.getDrawable(R.drawable.bg_disable));
                                }
                            }
                        }
                        loading.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(context, "Terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseCheckAbsen> call, @NotNull Throwable t) {
                t.printStackTrace();
                loading.dismiss();
                Toast.makeText(context, "Tampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(MY_PERMISSIONS_REQUEST_LOCATION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            getLocationUser();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", MY_PERMISSIONS_REQUEST_LOCATION, perms);
        }
    }

    private void getLocationUser() {
        // GET CURRENT LOCATION
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            mFusedLocation.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    lat = String.valueOf(location.getLatitude());
                    lng = String.valueOf(location.getLongitude());
                }
            });
        }
    }

    @OnClick({R.id.btn_masuk, R.id.btn_pulang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
            case R.id.btn_pulang:
                getLocationUser();
                ImagePicker.Companion.with(this)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .crop()
                        .cameraOnly()
                        .start();
                break;
        }
    }

    private void absenProcess(String imgBase64) {
        loading.setMessage("Memproses absensi");
        loading.show();
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)){
            Toast.makeText(context, "Gagal mengambil lokasi pengguna, harap pastikan GPS/lokasi telah dinyalakan", Toast.LENGTH_LONG).show();
            loading.dismiss();
        }else{
            apiServices.absenHarian(
                    prefManager.getIdUser(),
                    lat,
                    lng,
                    imgBase64
            ).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(@NotNull Call<ResponseMessage> call, @NotNull Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        try {
                            assert response.body() != null;
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                            if (response.body().getStatus()) {
                                Intent intent = new Intent(context, DetailAbsenActivity.class);
                                intent.putExtra("id", prefManager.getIdUser());
                                context.startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading.dismiss();
                            Toast.makeText(context, "Terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseMessage> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    loading.dismiss();
                    Toast.makeText(context, "Tampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                assert data != null;
                absenProcess(convertImageBase64(data.getData()));
                break;
            case ImagePicker.RESULT_ERROR:
                Toast.makeText(this, "Nampaknya kesalahan mengambil gambar", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Batal Absen", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertImageBase64(Uri selectedfile) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedfile);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}