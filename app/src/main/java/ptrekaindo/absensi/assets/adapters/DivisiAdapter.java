package ptrekaindo.absensi.assets.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.Divisi;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import ptrekaindo.absensi.section.FormSectionActivity;
import ptrekaindo.absensi.section.SectionFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivisiAdapter extends RecyclerView.Adapter<DivisiAdapter.MyViewHolder> {
    private Context context;
    private List<Divisi> divisiList;
    private SectionFragment fragment;
    private ProgressDialog loadingDialog;
    private ApiServices apiServices = ApiUtils.getApiServices();

    public DivisiAdapter(Context context, List<Divisi> divisiList, SectionFragment fragment) {
        this.context = context;
        this.divisiList = divisiList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try{
            Divisi data = divisiList.get(position);
            holder.tvNamaSection.setText(data.getNamaDivisi());
            holder.btnEdit.setOnClickListener(v -> {
                Intent i = new Intent(context, FormSectionActivity.class);
                i.putExtra("data",data);
                context.startActivity(i);
            });

            holder.btnHapus.setOnClickListener(v -> {
                loadingDialog = ProgressDialog.show(context, null, "harap tunggu...", true, false);
                apiServices.deleteDivisi(data.getIdDivisi()).enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseMessage> call, @NotNull Response<ResponseMessage> response) {
                        if (response.isSuccessful()){
                            try{
                                assert response.body() != null;
                                if (loadingDialog!=null && loadingDialog.isShowing()){
                                    loadingDialog.dismiss();
                                }
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                fragment.onResume();
                            }catch (Exception e){
                                e.printStackTrace();
                                if (loadingDialog!=null && loadingDialog.isShowing()){
                                    loadingDialog.dismiss();
                                }
                                Toast.makeText(context, "Terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            if (loadingDialog!=null && loadingDialog.isShowing()){
                                loadingDialog.dismiss();
                            }
                            Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseMessage> call, @NotNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(context, "Tampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                        if (loadingDialog!=null && loadingDialog.isShowing()){
                            loadingDialog.dismiss();
                        }
                    }
                });
            });
        }catch (Exception e){
            e.printStackTrace();
            holder.setIsRecyclable(true);
        }
    }

    @Override
    public int getItemCount() {
        return (divisiList!=null)?divisiList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNamaSection;
        private ImageView btnEdit,btnHapus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaSection = itemView.findViewById(R.id.tv_nama_section);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnHapus = itemView.findViewById(R.id.btn_delete);
        }
    }
}
