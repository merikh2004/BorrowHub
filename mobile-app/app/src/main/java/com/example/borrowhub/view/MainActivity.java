package com.example.borrowhub.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.borrowhub.R;
import com.example.borrowhub.databinding.ActivityMainBinding;
import com.example.borrowhub.viewmodel.AuthViewModel;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        setupObservers();

        // Setup Navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        }

        // Setup TopAppBar menu clicks
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_account_settings) {
                Toast.makeText(this, "Account Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_user_management) {
                Toast.makeText(this, "User Management Clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_student_management) {
                Toast.makeText(this, "Student Management Clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.action_logout) {
                authViewModel.logout();
                return true;
            }
            return false;
        });
    }

    private void setupObservers() {
        authViewModel.getLogoutResult().observe(this, isSuccess -> {
            boolean handled = false;
            if (Boolean.TRUE.equals(isSuccess)) {
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                handled = true;
            } else if (Boolean.FALSE.equals(isSuccess)) {
                Toast.makeText(this, R.string.logout_failed, Toast.LENGTH_SHORT).show();
                handled = true;
            }

            if (handled) {
                authViewModel.clearLogoutResult();
            }
        });
    }
}
