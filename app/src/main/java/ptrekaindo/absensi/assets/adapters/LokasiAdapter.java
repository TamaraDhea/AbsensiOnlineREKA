package ptrekaindo.absensi.assets.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.Lokasi;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import ptrekaindo.absensi.lokasi.FormLokasiActivity;
import ptrekaindo.absensi.lokasi.LokasiFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LokasiAdapter extends RecyclerView.Adapter<LokasiAdapter.MyViewHolder> {
    private Context context;
    private List<Lokasi> lokasiList;
    private LokasiFragment fragment;
    private ProgressDialog loadingDialog;
    private ApiServices apiServices = ApiUtils.getApiServices();

    public LokasiAdapter(Context context, List<Lokasi> lokasiList, LokasiFragment fragment) {
        this.context = context;
        this.lokasiList = lokasiList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lokasi,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try{
            Lokasi data = lokasiList.get(position);
            holder.tvLokasi.setText(data.getNamaLokasi());
            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, FormLokasiActivity.class);
                intent.putExtra("data",data);
                context.startActivity(intent);
            });
            holder.btnDelete.setOnClickListener(v -> {
                loadingDialog = ProgressDialog.show(context, null, "harap tunggu...", true, false);
                apiServices.deleteLokasi(data.getIdLokasi()).enqueue(new Callback<ResponseMessage>() {
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
        return (lokasiList!=null)?lokasiList.size():0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLokasi;
        ImageView btnEdit,btnDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLokasi = itemView.findViewById(R.id.tv_nama_lokasi);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
