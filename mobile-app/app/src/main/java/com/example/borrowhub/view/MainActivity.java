package com.example.borrowhub.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.borrowhub.R;
import com.example.borrowhub.databinding.ActivityMainBinding;
import com.example.borrowhub.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize View Binding BEFORE setContentView
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Apply Window Insets using the binding object
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.textViewTitle.setText("Welcome to BorrowHub\n(MVVM + Room Architecture Pattern)");

        // TAMA NA CONVENTION: 
        // Ang View (Activity) ay inuutusan lang ang ViewModel.
        // Kunwari ito ay nanggaling sa pag-click ng user sa isang "Save" button.
        viewModel.addExample("Test Item", "Ito ay test lamang galing sa ViewModel logic");

        // Mag-observe tayo sa database, kapag may nadagdag, mag-uupdate ang text
        viewModel.getAllExamples().observe(this, examples -> {
            if (binding.textViewMessage != null && examples != null) {
                binding.textViewMessage.setText("Database Connected!\nBilang ng items sa DB: " + examples.size());
            }
        });
    }
}
