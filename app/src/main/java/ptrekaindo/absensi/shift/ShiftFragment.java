package ptrekaindo.absensi.shift;

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
import ptrekaindo.absensi.assets.adapters.ShiftAdapter;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.Shift;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShiftFragment extends Fragment {


    private LinearLayout btnAdd;
    private RecyclerView rvShift;
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
        rootView =  inflater.inflate(R.layout.fragment_shift, container, false);
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

    private void listener() {
        swip.setOnRefreshListener(() -> {
            onAttach(context);
            swip.setRefreshing(false);
            loadData();
        });
        
        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(context,TambahShiftActivity.class));
        });
    }

    private void loadData() {
        showList();
        skeleton.showSkeleton();
        apiServices.listShift().enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NotNull Call<List<Shift>> call, @NotNull Response<List<Shift>> response) {
                if (response.isSuccessful()){
                    try{
                        assert response.body() != null;
                        if (response.body().isEmpty()){
                            hiddenList();
                        }else{
                            mAdapter = new ShiftAdapter(context,response.body(),ShiftFragment.this);
                            rvShift.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            rvShift.post(() -> {
                                showList();
                            });
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        hiddenList();
                        Toast.makeText(context, "Nampaknya terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    hiddenList();
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Shift>> call, @NotNull Throwable t) {
                t.printStackTrace();
                hiddenList();
                Toast.makeText(context, "Nampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }
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
        rvShift = rootView.findViewById(R.id.rv_shift);
        lytEmpty = rootView.findViewById(R.id.layout_empty);
        lytRv = rootView.findViewById(R.id.layout_rv);
        swip = rootView.findViewById(R.id.swip);
        rvShift.setHasFixedSize(true);
        rvShift.setLayoutManager(new LinearLayoutManager(context));
        skeleton = SkeletonLayoutUtils.applySkeleton(rvShift, R.layout.item_shift, 4);
    }
}