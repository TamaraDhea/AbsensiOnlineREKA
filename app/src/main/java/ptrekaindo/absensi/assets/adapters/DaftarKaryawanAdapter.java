package ptrekaindo.absensi.assets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ptrekaindo.absensi.R;
import ptrekaindo.absensi.absensi.DetailAbsenActivity;
import ptrekaindo.absensi.assets.models.User;

public class DaftarKaryawanAdapter extends RecyclerView.Adapter<DaftarKaryawanAdapter.MyViewHolder> {
    private Context context;
    private List<User> karyawanList;

    public DaftarKaryawanAdapter(Context context, List<User> karyawanList) {
        this.context = context;
        this.karyawanList = karyawanList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absensi_list_karyawan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User data = karyawanList.get(position);
        holder.tvNamaKaryawan.setText(data.getNama());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailAbsenActivity.class);
            intent.putExtra("id",data.getIdUser());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (karyawanList!=null)?karyawanList.size():0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaKaryawan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaKaryawan = itemView.findViewById(R.id.tv_nama_karyawan);
        }
    }
}
