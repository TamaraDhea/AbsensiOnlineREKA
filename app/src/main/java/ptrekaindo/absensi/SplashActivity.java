package ptrekaindo.absensi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import ptrekaindo.absensi.assets.helpers.IntroApps;
import ptrekaindo.absensi.assets.helpers.PrefManager;

public class SplashActivity extends AppCompatActivity {
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefManager = new PrefManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        int waktu_loading = 3000;
        new Handler().postDelayed(() -> {
            //setelah loading maka akan langsung berpindah ke home activity
            if (prefManager.checkIntroAccess()){
                if (prefManager.getIdUser().isEmpty()){
                    startActivity(new Intent(this, LoginActivity.class));
                }else{
                    startActivity(new Intent(this, MainActivity.class));
                }
            }else{
                startActivity(new Intent(this, IntroApps.class));
            }
            finish();
        }, waktu_loading);

    }
}