package ptrekaindo.absensi.section;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.adapters.DivisiAdapter;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.Divisi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SectionFragment extends Fragment {

    private LinearLayout btnAdd;
    private RecyclerView rvSection;
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
        rootView = inflater.inflate(R.layout.fragment_section, container, false);
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
        apiServices.listDivisi().enqueue(new Callback<List<Divisi>>() {
            @Override
            public void onResponse(@NotNull Call<List<Divisi>> call, @NotNull Response<List<Divisi>> response) {
                try {
                    assert response.body() != null;
                    if (response.body().isEmpty()){
                        hiddenList();
                    }else{
                        mAdapter = new DivisiAdapter(context,response.body(), SectionFragment.this);
                        rvSection.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        rvSection.post(() -> {
                            showList();
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    hiddenList();
                    Toast.makeText(context, "Nampaknya terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Divisi>> call, @NotNull Throwable t) {
                hiddenList();
                Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listener() {
        swip.setOnRefreshListener(() -> {
            onAttach(context);
            swip.setRefreshing(false);
            loadData();
        });

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(context, FormSectionActivity.class));
        });
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
        rvSection = rootView.findViewById(R.id.rv_section);
        lytEmpty = rootView.findViewById(R.id.layout_empty);
        lytRv = rootView.findViewById(R.id.layout_rv);
        swip = rootView.findViewById(R.id.swip);
        rvSection.setHasFixedSize(true);
        rvSection.setLayoutManager(new LinearLayoutManager(context));
        skeleton = SkeletonLayoutUtils.applySkeleton(rvSection, R.layout.item_section, 8);
    }
}