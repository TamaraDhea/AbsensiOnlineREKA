package ptrekaindo.absensi.assets.adapters;

import android.annotation.SuppressLint;
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
import ptrekaindo.absensi.assets.models.Shift;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import ptrekaindo.absensi.shift.EditShiftActivity;
import ptrekaindo.absensi.shift.ShiftFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.MyViewHolder> {

    private Context context;
    private List<Shift> shiftList;
    private ApiServices apiServices = ApiUtils.getApiServices();
    private ProgressDialog loadingDialog;
    private ShiftFragment fragment;

    public ShiftAdapter(Context context, List<Shift> shiftList, ShiftFragment fragment) {
        this.context = context;
        this.shiftList = shiftList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try{
            Shift data = shiftList.get(position);
            holder.tvNamaShift.setText(data.getNamaShift());
            holder.tvJamMulai.setText(": "+data.getJamMulai());
            holder.tvJamSelesai.setText(": "+data.getJamSelesai());
            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditShiftActivity.class);
                intent.putExtra("data",data);
                context.startActivity(intent);
            });
            holder.btnDelete.setOnClickListener(v -> {
                loadingDialog = ProgressDialog.show(context, null, "harap tunggu...", true, false);
                apiServices.deleteShift(data.getIdShift()).enqueue(new Callback<ResponseMessage>() {
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
        return (shiftList!=null)?shiftList.size():0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaShift,tvJamMulai,tvJamSelesai;
        ImageView btnEdit,btnDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaShift = itemView.findViewById(R.id.tv_nama_shift);
            tvJamMulai = itemView.findViewById(R.id.tv_jam_mulai);
            tvJamSelesai = itemView.findViewById(R.id.tv_jam_selesai);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
