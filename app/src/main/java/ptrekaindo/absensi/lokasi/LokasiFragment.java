package ptrekaindo.absensi.lokasi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.adapters.LokasiAdapter;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.Lokasi;
import ptrekaindo.absensi.karyawan.FormKaryawanActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LokasiFragment extends Fragment {

    private LinearLayout btnAdd;
    private RecyclerView rvLokasi;
    private LinearLayout lytEmpty,lytRv;
    private SwipeRefreshLayout swip;
    private Skeleton skeleton;
    private Context context;
    private RecyclerView.Adapter mAdapter;
    private ApiServices apiServices = ApiUtils.getApiServices();
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lokasi, container, false);
        initComponent();
        listener();
        hiddenList();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        showList();
        skeleton.showSkeleton();
        apiServices.listLokasi().enqueue(new Callback<List<Lokasi>>() {
            @Override
            public void onResponse(@NotNull Call<List<Lokasi>> call, @NotNull Response<List<Lokasi>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().isEmpty()){
                        hiddenList();
                    }else{
                        mAdapter = new LokasiAdapter(context,response.body(),LokasiFragment.this);
                        rvLokasi.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        rvLokasi.post(()-> showList());
                    }
                }else {
                    hiddenList();
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Lokasi>> call, @NotNull Throwable t) {
                t.printStackTrace();
                hiddenList();
                Toast.makeText(context, "Nampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listener() {
        swip.setOnRefreshListener(() -> {
            onAttach(context);
            swip.setRefreshing(false);
            loadData();
        });

        btnAdd.setOnClickListener(v -> startActivity(new Intent(context, FormLokasiActivity.class)));
    }

    private void showList() {
        lytEmpty.setVisibility(View.GONE);
        lytRv.setVisibility(View.VISIBLE);
    }

    private void hiddenList() {
        lytEmpty.setVisibility(View.VISIBLE);
        lytRv.setVisibility(View.GONE);
    }

    private void initComponent() {
        btnAdd = rootView.findViewById(R.id.btn_add);
        rvLokasi = rootView.findViewById(R.id.rv_lokasi);
        lytEmpty = rootView.findViewById(R.id.layout_empty);
        lytRv = rootView.findViewById(R.id.layout_rv);
        swip = rootView.findViewById(R.id.swip);
        rvLokasi.setHasFixedSize(true);
        rvLokasi.setLayoutManager(new LinearLayoutManager(context));
        skeleton = SkeletonLayoutUtils.applySkeleton(rvLokasi, R.layout.item_lokasi, 4);
    }
}