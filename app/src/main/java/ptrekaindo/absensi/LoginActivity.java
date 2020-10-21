package ptrekaindo.absensi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import ptrekaindo.absensi.assets.helpers.PrefManager;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiServices;
import ptrekaindo.absensi.assets.helpers.retrofit.ApiUtils;
import ptrekaindo.absensi.assets.models.responses.ResponseLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.button_signin)
    Button btnSignin;

    Context context;
    ApiServices apiServices = ApiUtils.getApiServices();
    PrefManager prefManager;
    ProgressDialog dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        context = this;
        prefManager = new PrefManager(context);
        listener();
    }

    private void listener() {
        btnSignin.setOnClickListener(v -> {
            if (edtUsername.getText().toString().isEmpty()){
                edtUsername.setError("Harap diisi");
                edtUsername.requestFocus();
            }else if (edtPassword.getText().toString().isEmpty()){
                edtPassword.setError("Harap diisi");
                edtPassword.requestFocus();
            }else{
                edtPassword.setError(null);
                edtUsername.setError(null);
                dialogLoading = ProgressDialog.show(this, null, "harap tunggu...", true, false);
                apiServices.login(
                        edtUsername.getText().toString(),
                        edtPassword.getText().toString()
                ).enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseLogin> call, @NotNull Response<ResponseLogin> response) {
                        if (response.isSuccessful()){
                            try{
                                assert response.body() != null;
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                if (dialogLoading!=null && dialogLoading.isShowing()){
                                    dialogLoading.dismiss();
                                }
                                if (response.body().getStatus()){
                                    prefManager.setDataUser(response.body().getUser());
                                    Intent intent = new Intent(context,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(context, "Terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show();
                                if (dialogLoading!=null && dialogLoading.isShowing()){
                                    dialogLoading.dismiss();
                                }
                            }
                        }else{
                            Toast.makeText(context, "Gagal terhubung pada server", Toast.LENGTH_SHORT).show();
                            if (dialogLoading!=null && dialogLoading.isShowing()){
                                dialogLoading.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseLogin> call, @NotNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(context, "Tampaknya terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                        if (dialogLoading!=null && dialogLoading.isShowing()){
                            dialogLoading.dismiss();
                        }
                    }
                });
            }
        });
    }
}