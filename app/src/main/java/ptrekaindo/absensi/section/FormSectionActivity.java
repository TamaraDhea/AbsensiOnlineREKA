package ptrekaindo.absensi.section;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.Divisi;
import ptrekaindo.absensi.assets.models.User;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormSectionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_nama_divisi)
    EditText edtNamaDivisi;
    @BindView(R.id.btn_simpan)
    LinearLayout btnSimpan;
    private Context context;
    private Intent intentData;
    private Divisi data;
    private ProgressDialog loadingDialog;
    private ApiServices apiServices = ApiUtils.getApiServices();
    private Boolean editData = false;
    private String id_divisi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_section);
        ButterKnife.bind(this);
        context = this;
        intentData = getIntent();
        data = (Divisi) intentData.getSerializableExtra("data");
        //init toolbar
        if (data==null){
            toolbar.setTitle("Tambah Divisi");
        }else{
            toolbar.setTitle("Edit Divisi");
            loadData();
            editData = true;
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadData() {
        id_divisi = data.getIdDivisi();
        edtNamaDivisi.setText(data.getNamaDivisi());
    }

    @OnClick(R.id.btn_simpan)
    public void onViewClicked() {
        if (edtNamaDivisi.getText().toString().isEmpty()){
            edtNamaDivisi.setError("Harap diisi");
            edtNamaDivisi.requestFocus();
        }else{
            if (editData){
                editDataProcess();
            }else{
                tambahDataProcess();
            }
        }
    }

    private void editDataProcess() {
        loadingDialog = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        apiServices.editDivisi(
                id_divisi,
                edtNamaDivisi.getText().toString()
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

    private void tambahDataProcess() {
        loadingDialog = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        apiServices.addDivisi(edtNamaDivisi.getText().toString()).enqueue(new Callback<ResponseMessage>() {
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