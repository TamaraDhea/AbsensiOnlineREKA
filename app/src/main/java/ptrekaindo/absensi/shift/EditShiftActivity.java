package ptrekaindo.absensi.shift;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.Shift;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditShiftActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_nama_shift)
    EditText edtNamaShift;
    @BindView(R.id.edt_jam_mulai)
    EditText edtJamMulai;
    @BindView(R.id.edt_jam_selesai)
    EditText edtJamSelesai;
    @BindView(R.id.btn_simpan)
    LinearLayout btnSimpan;
    private Context context;
    MyTimePickerDialog timePickerMulai,timePickerSelesai;
    private ProgressDialog loadingDialog;
    private ApiServices apiServices = ApiUtils.getApiServices();
    private String id;
    private Intent intentData;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shift);
        ButterKnife.bind(this);
        context = this;
        Calendar now = Calendar.getInstance();
        //init toolbar
        toolbar.setTitle("Edit Shift");
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        timePickerMulai = new MyTimePickerDialog(context, (view1, hourOfDay, minute, seconds) -> edtJamMulai.setText(String.format("%02d", hourOfDay) +
                ":" + String.format("%02d", minute) +
                ":" + String.format("%02d", seconds)
        ), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
        timePickerSelesai = new MyTimePickerDialog(context, (view1, hourOfDay, minute, seconds) -> edtJamSelesai.setText(String.format("%02d", hourOfDay) +
                ":" + String.format("%02d", minute) +
                ":" + String.format("%02d", seconds)
        ), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
        loadData();
    }

    private void loadData() {
        intentData = getIntent();
        Shift data = (Shift) intentData.getSerializableExtra("data");
        assert data != null;
        edtNamaShift.setText(data.getNamaShift());
        edtJamMulai.setText(data.getJamMulai());
        edtJamSelesai.setText(data.getJamSelesai());
        id = data.getIdShift();
    }

    @OnClick({R.id.edt_jam_mulai, R.id.edt_jam_selesai, R.id.btn_simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edt_jam_mulai:
                timePickerMulai.show();
                break;
            case R.id.edt_jam_selesai:
                timePickerSelesai.show();
                break;
            case R.id.btn_simpan:
                if (edtNamaShift.getText().toString().isEmpty()){
                    edtNamaShift.setError("Harap diisi!");
                    edtNamaShift.requestFocus();
                }else if (edtJamMulai.getText().toString().isEmpty()){
                    edtJamMulai.setError("Harap diisi");
                }else if (edtJamSelesai.getText().toString().isEmpty()){
                    edtJamSelesai.setError("Harap diisi");
                }else{
                    simpanData();
                }
                break;
        }
    }

    private void simpanData() {
        loadingDialog = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        apiServices.editShift(
                id,
                edtNamaShift.getText().toString(),
                edtJamMulai.getText().toString(),
                edtJamSelesai.getText().toString()
        ).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NotNull Call<ResponseMessage> call, @NotNull Response<ResponseMessage> response) {
                if (response.isSuccessful()){
                    try{
                        assert response.body() != null;
                        if (loadingDialog!=null && loadingDialog.isShowing()){
                            loadingDialog.dismiss();
                        }
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getStatus()){
                            finish();
                        }
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}