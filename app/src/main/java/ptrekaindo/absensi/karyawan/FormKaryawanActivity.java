package ptrekaindo.absensi.karyawan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.faltenreich.skeletonlayout.Skeleton;

import org.angmarch.views.NiceSpinner;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.Divisi;
import ptrekaindo.absensi.assets.models.Shift;
import ptrekaindo.absensi.assets.models.User;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormKaryawanActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_nik)
    EditText edtNik;
    @BindView(R.id.edt_nama_lengkap)
    EditText edtNamaLengkap;
    @BindView(R.id.edt_no_telp)
    EditText edtNoTelp;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.sp_divisi)
    NiceSpinner spDivisi;
    @BindView(R.id.sp_shift)
    NiceSpinner spShift;
    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_simpan)
    LinearLayout btnSimpan;
    @BindView(R.id.skeletonLayout)
    Skeleton skeleton;

    List<String> datasetDivisi = new ArrayList<>();
    List<String> datasetIdDivisi = new ArrayList<>();
    List<String> datasetShift = new ArrayList<>();
    List<String> datasetIdShift = new ArrayList<>();

    private ApiServices apiServices = ApiUtils.getApiServices();
    private Context context;
    private String idDivisi, idShift, idKaryawan;
    private Boolean editData = false;
    private ProgressDialog loadingDialog;
    private User data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_karyawan);
        ButterKnife.bind(this);
        skeleton.showSkeleton();
        context = this;
        Intent intentData = getIntent();
        data = (User) intentData.getSerializableExtra("data");
        loadDataDivisi();

        if (data==null){
            toolbar.setTitle("Tambah Karyawan");
        }else{
            toolbar.setTitle("Edit Karyawan");
            loadUpdateData();
            editData = true;
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadUpdateData() {
        idKaryawan = data.getIdUser();
        edtNik.setText(data.getNik());
        edtNamaLengkap.setText(data.getNama());
        edtNoTelp.setText(data.getTelp());
        edtEmail.setText(data.getEmail());
        for (int i = 0; i<datasetIdShift.size() ;i++) {
            if (datasetIdShift.get(i).equals(data.getShiftId())){
                spShift.setSelectedIndex(i);
                spShift.setSelected(true);
                break;
            }
        }
        for (int i = 0; i<datasetIdDivisi.size() ;i++) {
            if (datasetIdDivisi.get(i).equals(data.getDivisi())){
                spDivisi.setSelectedIndex(i);

                Log.e("BABI", "selected id: "+spDivisi.getSelectedIndex() );
                Log.e("BABI", "id dipisi: "+data.getDivisi() );
                Log.e("BABI", "index dipisi: "+i );
//                spDivisi.
                break;
            }
        }
        Log.e("KUDA", "ap: "+data.getNamaShift() );
        Log.e("KUDA", "total dataset: "+datasetShift.size() );
        Log.e("KUDA", "index dataset: "+datasetShift.indexOf(data.getNamaShift() ));


        edtUsername.setText(data.getUsername());
    }

    private void setupSpinner() {
        spDivisi.attachDataSource(datasetDivisi);
        spShift.attachDataSource(datasetShift);
        spDivisi.setOnSpinnerItemSelectedListener((parent, view, position, id) -> idDivisi = datasetIdDivisi.get(position));
        spShift.setOnSpinnerItemSelectedListener((parent, view, position, id) -> idShift = datasetIdShift.get(position));
        skeleton.showOriginal();
        if (data!=null){
            try {
                int postionShift = datasetIdShift.indexOf(data.getShiftId());
                int positionDivisi = datasetIdDivisi.indexOf(data.getDivisi());
                spShift.setSelectedIndex(postionShift);
                spDivisi.setSelectedIndex(positionDivisi);
                idDivisi = datasetIdDivisi.get(positionDivisi);
                idShift = datasetIdShift.get(postionShift);
            }catch (Exception ignored){}
        }

    }


    private void loadDataDivisi() {
        apiServices.listDivisi().enqueue(new Callback<List<Divisi>>() {
            @Override
            public void onResponse(@NotNull Call<List<Divisi>> call, @NotNull Response<List<Divisi>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        datasetDivisi.add(response.body().get(i).getNamaDivisi());
                        datasetIdDivisi.add(response.body().get(i).getIdDivisi());
                    }
                    loadDataShift();
                } else {
                    skeleton.showOriginal();
                    Toast.makeText(context, "Gagal mengambil data divisi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Divisi>> call, @NotNull Throwable t) {
                t.printStackTrace();
                skeleton.showOriginal();
                Toast.makeText(context, "Tampaknya terjadi kesalaan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataShift() {
        apiServices.listShift().enqueue(new Callback<List<Shift>>() {
            @Override
            public void onResponse(@NotNull Call<List<Shift>> call, @NotNull Response<List<Shift>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    for (int i = 0; i < response.body().size(); i++) {
                        datasetIdShift.add(response.body().get(i).getIdShift());
                        datasetShift.add(response.body().get(i).getNamaShift());
                    }

                    setupSpinner();
                } else {
                    skeleton.showOriginal();
                    Toast.makeText(context, "Gagal mengambil data divisi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Shift>> call, @NotNull Throwable t) {
                t.printStackTrace();
                skeleton.showOriginal();
                Toast.makeText(context, "Tampaknya terjadi kesalaan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_simpan)
    public void onViewClicked() {
        if (edtNik.getText().toString().isEmpty()){
            edtNik.setError("Harap diisi");
            edtNik.requestFocus();
        }else if (edtNamaLengkap.getText().toString().isEmpty()){
            edtNamaLengkap.setError("Harap diisi");
            edtNamaLengkap.requestFocus();
        }else if (edtNoTelp.getText().toString().isEmpty()){
            edtNoTelp.setError("Harap diisi");
            edtNoTelp.requestFocus();
        }else if (edtEmail.getText().toString().isEmpty()){
            edtEmail.setError("Harap diisi");
            edtEmail.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
            edtEmail.setError("Email tidak sesuai");
            edtEmail.requestFocus();
        }else if (TextUtils.isEmpty(idDivisi)){
            Toast.makeText(context, "Divisi harus dipilih", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(idShift)){
            Toast.makeText(context, "Shift harus dipilih", Toast.LENGTH_SHORT).show();
        }else if (edtUsername.getText().toString().isEmpty()){
            edtUsername.setError("Harap diisi");
            edtUsername.requestFocus();
        }else if (edtPassword.getText().toString().isEmpty() && (!editData)){
            edtPassword.setError("Harap diisi");
            edtPassword.requestFocus();
        }else{
            if (editData){
                editDataProcess();
            }else{
                tambahdataProcess();
            }
        }
    }

    private void editDataProcess() {
        loadingDialog = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        apiServices.editKaryawan(
                idKaryawan,
                edtNik.getText().toString(),
                edtNamaLengkap.getText().toString(),
                edtNoTelp.getText().toString(),
                idDivisi,
                edtEmail.getText().toString(),
                idShift,
                edtUsername.getText().toString(),
                edtPassword.getText().toString()
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

    private void tambahdataProcess() {
        loadingDialog = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        apiServices.addKaryawan(
                edtNik.getText().toString(),
                edtNamaLengkap.getText().toString(),
                edtNoTelp.getText().toString(),
                idDivisi,
                edtEmail.getText().toString(),
                idShift,
                edtUsername.getText().toString(),
                edtPassword.getText().toString()
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