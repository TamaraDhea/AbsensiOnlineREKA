package ptrekaindo.absensi.profil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import ptrekaindo.absensi.R;
import ptrekaindo.absensi.assets.helpers.AppData;
import ptrekaindo.absensi.assets.helpers.PrefManager;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.User;
import ptrekaindo.absensi.assets.models.responses.ResponseMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    private EditText edtNik,edtNamaLengkap,edtNoTelp,edtEmail,edtUsername,edtPassword;
    private LinearLayout btnSimpan;
    private ApiServices apiServices = ApiUtils.getApiServices();
    private Context context;
    private PrefManager prefManager;
    private String password;
    private ProgressDialog loading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);
        edtNik = rootView.findViewById(R.id.edt_nik);
        edtNamaLengkap = rootView.findViewById(R.id.edt_nama_lengkap);
        edtNoTelp = rootView.findViewById(R.id.edt_no_telp);
        edtEmail = rootView.findViewById(R.id.edt_email);
        edtUsername = rootView.findViewById(R.id.edt_username);
        edtPassword = rootView.findViewById(R.id.edt_password);
        btnSimpan = rootView.findViewById(R.id.btn_simpan);
        prefManager = new PrefManager(context);
        loading = new ProgressDialog(context);
        loading.setTitle(null);
        loading.setMessage("Harap tunggu...");
        loading.setCancelable(false);
        listener();

        return rootView;
    }

    private void listener() {
        btnSimpan.setOnClickListener(v -> {
            if (edtNik.getText().toString().isEmpty() && prefManager.getLevelUser().equals(AppData.KARYAWAN)){
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
            }else if (edtUsername.getText().toString().isEmpty()){
                edtUsername.setError("Harap diisi");
                edtUsername.requestFocus();
            }else{
                if (edtPassword.getText().toString().isEmpty()){
                    password = null;
                }else{
                    password = edtPassword.getText().toString();
                }
                simpanData();
            }
        });
    }

    private void simpanData() {
        loading.show();
        apiServices.editUser(
                prefManager.getIdUser(),
                edtNik.getText().toString(),
                edtNamaLengkap.getText().toString(),
                edtNoTelp.getText().toString(),
                edtEmail.getText().toString(),
                edtUsername.getText().toString(),
                password
        ).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(@NotNull Call<ResponseMessage> call, @NotNull Response<ResponseMessage> response) {
                if (response.isSuccessful()){
                    try {
                        assert response.body() != null;
                        loading.dismiss();
                        if (response.body().getStatus()){
                            onResume();
                        }
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(context, "Nampaknya terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loading.dismiss();
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseMessage> call, @NotNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Nampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        prepareData();
    }

    private void prepareData() {
        loading.show();
        apiServices.getUserData(
                prefManager.getIdUser()
        ).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful()){
                    try{
                        assert response.body() != null;
                        edtNik.setText(response.body().getNik());
                        edtNamaLengkap.setText(response.body().getNama());
                        edtNoTelp.setText(response.body().getTelp());
                        edtEmail.setText(response.body().getEmail());
                        edtUsername.setText(response.body().getUsername());
                        loading.dismiss();
                    }catch (Exception e){
                        loading.dismiss();
                        e.printStackTrace();
                        Toast.makeText(context, "Nampaknya terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loading.dismiss();
                    Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                loading.dismiss();
                t.printStackTrace();
                Toast.makeText(context, "Nampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}