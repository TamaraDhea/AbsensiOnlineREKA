package ptrekaindo.absensi.absensi;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.adapters.DaftarKaryawanAdapter;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarKaryawanActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_daftar_karyawan)
    RecyclerView rvDaftarKaryawan;
    @BindView(R.id.layout_rv)
    LinearLayout lytRv;
    @BindView(R.id.layout_empty)
    LinearLayout lytEmpty;
    @BindView(R.id.swip)
    SwipeRefreshLayout swip;

    private Context context;
    private RecyclerView.Adapter mAdapter;
    private Skeleton skeleton;
    private final ApiServices apiServices = ApiUtils.getApiServices();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_karyawan);
        ButterKnife.bind(this);
        context = this;
        rvDaftarKaryawan.setHasFixedSize(true);
        rvDaftarKaryawan.setLayoutManager(new LinearLayoutManager(context));
        skeleton = SkeletonLayoutUtils.applySkeleton(rvDaftarKaryawan, R.layout.item_absensi_list_karyawan, 4);
        prepareData();
        listener();
        hiddenList();
    }

    private void listener() {
        swip.setOnRefreshListener(() -> {
            swip.setRefreshing(false);
            prepareData();
        });
    }

    private void prepareData() {
        showList();
        skeleton.showSkeleton();
        apiServices.listKaryawanAbsensi().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NotNull Call<List<User>> call, @NotNull Response<List<User>> response) {
                if (response.isSuccessful()){
                    try{
                        assert response.body() != null;
                        if (response.body().isEmpty()){
                            hiddenList();
                        }else{
                            mAdapter = new DaftarKaryawanAdapter(context,response.body());
                            rvDaftarKaryawan.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            rvDaftarKaryawan.post(()->showList());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        hiddenList();
                        Toast.makeText(context, "Nampaknya terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    hiddenList();
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<User>> call, @NotNull Throwable t) {
                t.printStackTrace();
                hiddenList();
                Toast.makeText(context, "Nampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showList() {
        lytEmpty.setVisibility(View.GONE);
        lytRv.setVisibility(View.VISIBLE);
    }

    private void hiddenList() {
        lytEmpty.setVisibility(View.VISIBLE);
        lytRv.setVisibility(View.GONE);
    }
}