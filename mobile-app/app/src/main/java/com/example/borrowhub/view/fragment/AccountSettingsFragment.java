package com.example.borrowhub.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.borrowhub.databinding.FragmentAccountSettingsBinding;
import com.example.borrowhub.viewmodel.AccountSettingsViewModel;

public class AccountSettingsFragment extends Fragment {
    private FragmentAccountSettingsBinding binding;
    private AccountSettingsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AccountSettingsViewModel.class);

        setupActions();
        observeViewModel();
    }

    private void setupActions() {
        binding.btnSaveChanges.setOnClickListener(v -> viewModel.updateProfile(
                asString(binding.etFullName.getText()),
                asString(binding.etUsername.getText())
        ));

        binding.btnUpdatePassword.setOnClickListener(v -> viewModel.changePassword(
                asString(binding.etCurrentPassword.getText()),
                asString(binding.etNewPassword.getText()),
                asString(binding.etConfirmNewPassword.getText())
        ));
    }

    private void observeViewModel() {
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            binding.etFullName.setText(user.getName());
            binding.etUsername.setText(user.getUsername());
            binding.etRole.setText(user.getRole());
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                binding.progressBar.setVisibility(Boolean.TRUE.equals(isLoading) ? View.VISIBLE : View.GONE));

        viewModel.getOperationSuccess().observe(getViewLifecycleOwner(), message -> {
            if (message == null || message.isEmpty()) {
                return;
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            binding.etCurrentPassword.setText("");
            binding.etNewPassword.setText("");
            binding.etConfirmNewPassword.setText("");
            viewModel.clearOperationStates();
        });

        viewModel.getOperationError().observe(getViewLifecycleOwner(), message -> {
            if (message == null || message.isEmpty()) {
                return;
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            viewModel.clearOperationStates();
        });
    }

    private String asString(CharSequence value) {
        return value == null ? "" : value.toString().trim();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
