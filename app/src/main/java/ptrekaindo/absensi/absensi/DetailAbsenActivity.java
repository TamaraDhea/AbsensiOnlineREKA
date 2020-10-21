package ptrekaindo.absensi.absensi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;

import org.angmarch.views.NiceSpinner;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.adapters.AbsensiAdapter;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.User;
import ptrekaindo.absensi.assets.models.responses.ResponseListAbsensi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAbsenActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sp_bulan)
    NiceSpinner spBulan;
    @BindView(R.id.sp_tahun)
    NiceSpinner spTahun;
    @BindView(R.id.tv_nama_karyawan)
    TextView tvNamaKaryawan;
    @BindView(R.id.tv_nama_divisi)
    TextView tvNamaDivisi;
    @BindView(R.id.tv_nama_shift)
    TextView tvNamaShift;
    @BindView(R.id.rv_absensi)
    RecyclerView rvAbsensi;
    @BindView(R.id.skeletonLayout)
    Skeleton skeletonLayout;
    Skeleton skeleton;
    @BindView(R.id.tv_absensi_bulan)
    TextView tvAbsensiBulan;
    @BindView(R.id.lyt_no_data)
    LinearLayout lytNoData;
    @BindView(R.id.btn_tampilkan)
    Button btnTampilkan;
    private RecyclerView.Adapter mAdapter;
    private ApiServices apiServices = ApiUtils.getApiServices();
    private Context context;
    private Intent intentData;
    private String idUser;
    private Integer currentMonth, currentYear;
    String[] bulanNama, bulanAngka, tahun;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_absen);
        ButterKnife.bind(this);
        context = this;
        intentData = getIntent();
        rvAbsensi.setHasFixedSize(true);
        rvAbsensi.setLayoutManager(new LinearLayoutManager(context));
        toolbar.setTitle("Detail Absensi");
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        loading = new ProgressDialog(context);
        loading.setTitle(null);
        loading.setMessage("Harap tunggu...");
        loading.setCancelable(false);

        prepareData();
    }

    private void prepareData() {
        loading.setMessage("Memuat data karyawan...");
        loading.show();
        lytNoData.setVisibility(View.GONE);
        Calendar instance = Calendar.getInstance();
        currentMonth = instance.get(Calendar.MONTH) + 1;
        currentYear = instance.get(Calendar.YEAR);

        bulanAngka = context.getResources().getStringArray(R.array.bulan_angka);
        bulanNama = context.getResources().getStringArray(R.array.bulan_nama);
        tahun = context.getResources().getStringArray(R.array.tahun);

        apiServices.getUserData(intentData.getStringExtra("id")).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    idUser = response.body().getIdUser();
                    tvNamaDivisi.setText(response.body().getNamaDivisi());
                    tvNamaKaryawan.setText(response.body().getNama());
                    tvNamaShift.setText(response.body().getNamaShift());
                    loading.dismiss();
                    loadDataAbsensi(
                            idUser,
                            String.valueOf(currentMonth),
                            String.valueOf(currentYear)
                    );
                } else {
                    loading.dismiss();
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                loading.dismiss();
                t.printStackTrace();
                Toast.makeText(context, "Nampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataAbsensi(String id_user, String bulan, String tahun) {
        loading.setMessage("Memuat data absensi...");
        loading.show();
         apiServices.listAbsensi(
                id_user,
                bulan,
                tahun
        ).enqueue(new Callback<ResponseListAbsensi>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<ResponseListAbsensi> call, @NotNull Response<ResponseListAbsensi> response) {
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        spBulan.setSelectedIndex(Integer.parseInt(response.body().getBulan()) - 1);
                        tvAbsensiBulan.setText("Absensi Bulan : " + bulanNama[Integer.parseInt(response.body().getBulan()) - 1]);
                        mAdapter = new AbsensiAdapter(context, response.body().getData());
                        rvAbsensi.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        if (response.body().getData().isEmpty()) {
                            lytNoData.setVisibility(View.VISIBLE);
                        } else
                            lytNoData.setVisibility(View.GONE);
                        loading.dismiss();
                    } catch (Exception e) {
                        lytNoData.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(context, "Nampaknya terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    lytNoData.setVisibility(View.VISIBLE);
                    loading.dismiss();
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseListAbsensi> call, @NotNull Throwable t) {
                loading.dismiss();
                lytNoData.setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(context, "Nampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_tampilkan)
    public void onViewClicked() {
        loadDataAbsensi(
                idUser,
                bulanAngka[spBulan.getSelectedIndex()],
                tahun[spTahun.getSelectedIndex()]
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}