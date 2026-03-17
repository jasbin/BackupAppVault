package com.app.backup.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.app.backup.databinding.ActivityDriveAccountBinding;
import com.app.backup.viewmodel.DriveViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.services.drive.DriveScopes;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DriveAccountActivity extends AppCompatActivity {

    private ActivityDriveAccountBinding binding;
    private DriveViewModel driveViewModel;
    private GoogleSignInClient googleSignInClient;

    private static final String PREF_DRIVE_ACCOUNT = "drive_account";

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleSignInResult(task);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriveAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Google Drive");
        }

        driveViewModel = new ViewModelProvider(this).get(DriveViewModel.class);
        setupGoogleSignIn();
        checkExistingSignIn();
        setupUI();
        observeViewModel();
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void checkExistingSignIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            driveViewModel.connectDrive(account.getEmail());
            showConnectedState(account.getEmail(), account.getDisplayName());
        } else {
            showDisconnectedState();
        }
    }

    private void setupUI() {
        binding.btnSignIn.setOnClickListener(v -> signInLauncher.launch(googleSignInClient.getSignInIntent()));
        binding.btnSignOut.setOnClickListener(v -> signOut());
        binding.btnRefresh.setOnClickListener(v -> driveViewModel.loadDriveFiles());
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                // Save account
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().putString(PREF_DRIVE_ACCOUNT, account.getEmail()).apply();

                driveViewModel.connectDrive(account.getEmail());
                showConnectedState(account.getEmail(), account.getDisplayName());
            }
        } catch (ApiException e) {
            Snackbar.make(binding.getRoot(),
                    "Sign in failed: " + e.getStatusCode(), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().remove(PREF_DRIVE_ACCOUNT).apply();
            driveViewModel.disconnectDrive();
            showDisconnectedState();
        });
    }

    private void showConnectedState(String email, String name) {
        binding.layoutSignedIn.setVisibility(View.VISIBLE);
        binding.layoutSignedOut.setVisibility(View.GONE);
        binding.tvAccountName.setText(name != null ? name : email);
        binding.tvAccountEmail.setText(email);
    }

    private void showDisconnectedState() {
        binding.layoutSignedIn.setVisibility(View.GONE);
        binding.layoutSignedOut.setVisibility(View.VISIBLE);
    }

    private void observeViewModel() {
        driveViewModel.getDriveFiles().observe(this, files -> {
            if (files == null) return;
            binding.tvDriveFileCount.setText(files.size() + " backup(s) on Drive");
        });

        driveViewModel.isLoading().observe(this, loading ->
                binding.progressDrive.setVisibility(loading ? View.VISIBLE : View.GONE));

        driveViewModel.getDriveEvent().observe(this, event -> {
            if (event == null) return;
            if (event.type == DriveViewModel.DriveEvent.Type.ERROR) {
                Snackbar.make(binding.getRoot(),
                        "Drive error: " + event.message, Snackbar.LENGTH_SHORT).show();
            } else if (event.type == DriveViewModel.DriveEvent.Type.FILE_DELETED) {
                Snackbar.make(binding.getRoot(), "File deleted from Drive", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
