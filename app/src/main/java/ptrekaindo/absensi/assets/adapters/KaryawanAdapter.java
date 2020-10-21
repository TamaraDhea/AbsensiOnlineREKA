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
import ptrekaindo.absensi.assets.models.User;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import ptrekaindo.absensi.karyawan.FormKaryawanActivity;
import ptrekaindo.absensi.karyawan.KaryawanFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanAdapter extends RecyclerView.Adapter<KaryawanAdapter.MyViewHolder> {
    private Context context;
    private List<User> karyawanList;
    private KaryawanFragment fragment;
    private ProgressDialog loadingDialog;
    private ApiServices apiServices = ApiUtils.getApiServices();

    public KaryawanAdapter(Context context, List<User> karyawanList, KaryawanFragment fragment) {
        this.context = context;
        this.karyawanList = karyawanList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_karyawan,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            User data = karyawanList.get(position);

            holder.tvNamaKaryawan.setText(data.getNama());
            holder.tvNamaDivisi.setText(data.getNamaDivisi());
            holder.tvEmail.setText(data.getEmail());
            holder.tvTelp.setText(data.getTelp());
            holder.tvShift.setText(data.getNamaShift());

            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, FormKaryawanActivity.class);
                intent.putExtra("data",data);
                context.startActivity(intent);
            });
            holder.btnDelete.setOnClickListener(v -> {
                loadingDialog = ProgressDialog.show(context, null, "harap tunggu...", true, false);
                apiServices.deleteKaryawan(data.getIdUser()).enqueue(new Callback<ResponseMessage>() {
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
            holder.setIsRecyclable(false);
        }

    }

    @Override
    public int getItemCount() {
        return (karyawanList!=null)?karyawanList.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaKaryawan,tvNamaDivisi,tvEmail,tvTelp,tvShift;
        ImageView btnEdit,btnDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaKaryawan = itemView.findViewById(R.id.tv_nama_karyawan);
            tvNamaDivisi = itemView.findViewById(R.id.tv_nama_divisi);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvTelp = itemView.findViewById(R.id.tv_telp);
            tvShift = itemView.findViewById(R.id.tv_shift);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
