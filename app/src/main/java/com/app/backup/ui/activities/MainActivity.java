package com.app.backup.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.backup.R;
import com.app.backup.databinding.ActivityMainBinding;
import com.app.backup.utils.PermissionsUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Inject
    PermissionsUtil permissionsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        setupNavigation();
        checkAndRequestPermissions();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment == null) return;

        navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                R.id.dashboardFragment,
                R.id.backupFragment,
                R.id.restoreFragment,
                R.id.historyFragment,
                R.id.settingsFragment
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    private void checkAndRequestPermissions() {
        List<String> missing = permissionsUtil.getMissingBackupPermissions(this);
        if (!missing.isEmpty()) {
            showPermissionRationale(missing);
        }
    }

    private void showPermissionRationale(List<String> permissions) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Permissions Required")
                .setMessage("BackupVault needs access to your Contacts and SMS to create backups. " +
                            "Your data stays on your device and is encrypted.")
                .setPositiveButton("Grant Access", (dialog, which) ->
                        permissionsUtil.requestAllBackupPermissions(this))
                .setNegativeButton("Not Now", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsUtil.REQUEST_ALL) {
            boolean allGranted = permissionsUtil.areAllGranted(grantResults);
            if (!allGranted) {
                Snackbar.make(binding.getRoot(),
                        "Some permissions denied. Backup may be limited.",
                        Snackbar.LENGTH_SHORT)
                        .setAction("Settings", v -> startActivity(
                                permissionsUtil.getAppSettingsIntent(this)))
                        .show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    public void navigateToBackup() {
        navController.navigate(R.id.backupFragment);
    }

    public void navigateToRestore() {
        navController.navigate(R.id.restoreFragment);
    }
}
