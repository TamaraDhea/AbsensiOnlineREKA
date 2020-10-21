package ptrekaindo.absensi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import ptrekaindo.absensi.absensi.AbsenHarianActivity;
import ptrekaindo.absensi.absensi.AbsensiFragment;
import ptrekaindo.absensi.absensi.DetailAbsenActivity;
import ptrekaindo.absensi.assets.helpers.AppData;
import ptrekaindo.absensi.assets.helpers.PrefManager;
import ptrekaindo.absensi.dashboard.DashboardFragment;
import ptrekaindo.absensi.karyawan.KaryawanFragment;
import ptrekaindo.absensi.lokasi.LokasiFragment;
import ptrekaindo.absensi.profil.ProfilFragment;
import ptrekaindo.absensi.section.SectionFragment;
import ptrekaindo.absensi.shift.ShiftFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private PrefManager prefManager;
    private Context context;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        prefManager = new PrefManager(context);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        this.navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        this.navigationView.setNavigationItemSelectedListener(this);
        hideMenu();
        loadFragment(new DashboardFragment(),context.getResources().getString(R.string.dashboard));
    }

    private void hideMenu() {
        Menu navMenu = this.navigationView.getMenu();
        switch (prefManager.getLevelUser()){
            case AppData.MANAGER:
                navMenu.findItem(R.id.menu_absen).setVisible(false);
                navMenu.findItem(R.id.menu_absensiku).setVisible(false);
                break;
            case AppData.KARYAWAN:
                navMenu.findItem(R.id.menu_shift).setVisible(false);
//                navMenu.findItem(R.id.menu_lokasi).setVisible(false);
                navMenu.findItem(R.id.menu_section).setVisible(false);
                navMenu.findItem(R.id.menu_karyawan).setVisible(false);
                navMenu.findItem(R.id.menu_absensi).setVisible(false);
                break;
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    System.exit(0);
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(context, "Tekan sekali lagi untuk keluar dari aplikasi", Toast.LENGTH_LONG).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.menu_dashboard:
                loadFragment(new DashboardFragment(),context.getResources().getString(R.string.dashboard));
                break;
            case R.id.menu_profil:
                loadFragment(new ProfilFragment(),context.getResources().getString(R.string.profil));
                break;
            case R.id.menu_shift:
                loadFragment(new ShiftFragment(),context.getResources().getString(R.string.shift));
                break;
//            case R.id.menu_lokasi:
//                loadFragment(new LokasiFragment(),context.getResources().getString(R.string.lokasi));
//                break;
            case R.id.menu_section:
                loadFragment(new SectionFragment(),context.getResources().getString(R.string.section));
                break;
            case R.id.menu_karyawan:
                loadFragment(new KaryawanFragment(),context.getResources().getString(R.string.karyawan));
                break;
            case R.id.menu_absensi:
                loadFragment(new AbsensiFragment(),context.getResources().getString(R.string.absensi));
                break;
            case R.id.menu_absensiku:
                Intent absensiku = new Intent(context, DetailAbsenActivity.class);
                absensiku.putExtra("id",prefManager.getIdUser());
                startActivity(absensiku);
                break;
            case R.id.menu_absen:
                startActivity(new Intent(context, AbsenHarianActivity.class));
                break;
            case R.id.menu_logout:
                prefManager.clearAllData();
                prefManager.saveSPBoolean(true);
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment, String title){
        toolbar.setTitle(title);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fm.beginTransaction()
                .replace(R.id.frame, fragment, "HOME")
                .commit();
    }
}