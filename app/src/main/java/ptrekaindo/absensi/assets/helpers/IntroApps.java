package ptrekaindo.absensi.assets.helpers;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.model.SliderPage;

import org.jetbrains.annotations.Nullable;

import ptrekaindo.absensi.LoginActivity;
import ptrekaindo.absensi.MainActivity;
import ptrekaindo.absensi.R;

public class IntroApps extends AppIntro2 {

    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);

        SliderPage sliderPageCamera = new SliderPage();
        sliderPageCamera.setTitle("Akses Kamera");
        sliderPageCamera.setTitleColor(Color.parseColor("#000000"));
        sliderPageCamera.setDescription("Izinkan Aplikasi untuk mengakses kamera");
        sliderPageCamera.setDescriptionColor(Color.parseColor("#000000"));
        sliderPageCamera.setImageDrawable(R.drawable.ic_photo);
        sliderPageCamera.setBackgroundColor(Color.parseColor("#ffffff"));

        SliderPage sliderPageFile = new SliderPage();
        sliderPageFile.setTitle("Akses Berkas Gambar");
        sliderPageFile.setTitleColor(Color.parseColor("#000000"));
        sliderPageFile.setDescription("Izinkan Aplikasi untuk menyimpan dan mengambil berkas gambar ");
        sliderPageFile.setDescriptionColor(Color.parseColor("#000000"));
        sliderPageFile.setImageDrawable(R.drawable.ic_folder);
        sliderPageFile.setBackgroundColor(Color.parseColor("#ffffff"));

        SliderPage sliderPageLocation = new SliderPage();
        sliderPageLocation.setTitle("Akses Lokasi");
        sliderPageLocation.setTitleColor(Color.parseColor("#000000"));
        sliderPageLocation.setDescription("Izinkan Aplikasi untuk mengakses lokasi");
        sliderPageLocation.setDescriptionColor(Color.parseColor("#000000"));
        sliderPageLocation.setImageDrawable(R.drawable.ic_map);
        sliderPageLocation.setBackgroundColor(Color.parseColor("#ffffff"));

        addSlide(AppIntroFragment.newInstance(sliderPageCamera));
        addSlide(AppIntroFragment.newInstance(sliderPageFile));
        addSlide(AppIntroFragment.newInstance(sliderPageLocation));

        askForPermissions(new String[]{Manifest.permission.CAMERA}, 1); // OR
        askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2); // OR
        askForPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 3); // OR

        setBarColor(getResources().getColor(R.color.white));
        setIndicatorColor(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimary));

        // Hide Skip/Done button.
        setSkipButtonEnabled(false);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        prefManager.saveSPBoolean(true);
        if (prefManager.getLevelUser().isEmpty()){
            startActivity(new Intent(IntroApps.this, LoginActivity.class));
        }else{
            startActivity(new Intent(IntroApps.this, MainActivity.class));
            finish();
        }
    }
}
