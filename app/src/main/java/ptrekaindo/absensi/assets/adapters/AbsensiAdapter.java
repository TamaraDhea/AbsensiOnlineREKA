package ptrekaindo.absensi.assets.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.models.Absensi;

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.MyViewHolder> {
    private Context context;
    private List<Absensi> absensiList;

    public AbsensiAdapter(Context context, List<Absensi> absensiList) {
        this.context = context;
        this.absensiList = absensiList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absensi,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            Absensi data = absensiList.get(position);
            holder.tvTanggal.setText(data.getHari()+", "+data.getTgl());
            holder.tvJamMasuk.setText(data.getJamMasuk().getText());
            holder.tvJamPulang.setText(data.getJamPulang().getText());
            holder.tvLokasiMasuk.setText(data.getLokasiMasuk());
            holder.tvLokasiPulang.setText(data.getLokasiPulang());
        }catch (Exception e){
            e.printStackTrace();
            holder.setIsRecyclable(true);
        }
    }

    @Override
    public int getItemCount() {
        return (absensiList!=null)?absensiList.size():0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal,tvJamMasuk,tvLokasiMasuk,tvJamPulang,tvLokasiPulang;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvJamMasuk = itemView.findViewById(R.id.tv_jam_masuk);
            tvLokasiMasuk = itemView.findViewById(R.id.tv_lokasi_masuk);
            tvJamPulang = itemView.findViewById(R.id.tv_jam_pulang);
            tvLokasiPulang = itemView.findViewById(R.id.tv_lokasi_pulang);
        }
    }
}
