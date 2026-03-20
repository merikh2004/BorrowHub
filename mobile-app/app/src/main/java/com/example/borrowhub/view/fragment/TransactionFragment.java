package com.example.borrowhub.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.borrowhub.R;
import com.example.borrowhub.databinding.FragmentTransactionBinding;
import com.example.borrowhub.viewmodel.TransactionViewModel;
import com.google.android.material.tabs.TabLayout;

public class TransactionFragment extends Fragment {

    private static final String TAG_BORROW = "borrow_fragment";

    private FragmentTransactionBinding binding;
    private TransactionViewModel viewModel;
    private BorrowItemFragment borrowFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        setupTabs();
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.transaction_tab_borrow));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.transaction_tab_return));

        // Re-use an existing fragment instance after configuration change, or create a new one
        borrowFragment = (BorrowItemFragment) getChildFragmentManager()
                .findFragmentByTag(TAG_BORROW);
        if (borrowFragment == null) {
            borrowFragment = new BorrowItemFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .add(binding.fragmentContainerBorrow.getId(), borrowFragment, TAG_BORROW)
                    .commit();
        }

        binding.fragmentContainerBorrow.setVisibility(View.VISIBLE);
        binding.layoutReturnTab.setVisibility(View.GONE);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showBorrowTab();
                } else {
                    showReturnTab();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void showBorrowTab() {
        binding.fragmentContainerBorrow.setVisibility(View.VISIBLE);
        binding.layoutReturnTab.setVisibility(View.GONE);
    }

    private void showReturnTab() {
        binding.fragmentContainerBorrow.setVisibility(View.GONE);
        binding.layoutReturnTab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}