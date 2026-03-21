package com.example.borrowhub.viewmodel;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.borrowhub.repository.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountSettingsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Application mockApplication;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private Observer<Boolean> mockLoadingObserver;
    @Mock
    private Observer<String> mockErrorObserver;
    @Mock
    private Observer<String> mockSuccessObserver;

    private AccountSettingsViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        MutableLiveData<com.example.borrowhub.data.local.entity.User> userLiveData = new MutableLiveData<>();
        when(mockUserRepository.getUser()).thenReturn(userLiveData);

        when(mockApplication.getString(com.example.borrowhub.R.string.account_settings_error_fields_required))
                .thenReturn("Please fill in all required fields.");
        when(mockApplication.getString(com.example.borrowhub.R.string.account_settings_password_mismatch))
                .thenReturn("New password and confirmation do not match.");
        when(mockApplication.getString(com.example.borrowhub.R.string.account_settings_profile_updated))
                .thenReturn("Profile updated successfully.");
        when(mockApplication.getString(com.example.borrowhub.R.string.account_settings_profile_update_failed))
                .thenReturn("Unable to update profile. Please try again.");
        when(mockApplication.getString(com.example.borrowhub.R.string.account_settings_password_updated))
                .thenReturn("Password updated successfully.");
        when(mockApplication.getString(com.example.borrowhub.R.string.account_settings_password_update_failed))
                .thenReturn("Unable to update password. Please try again.");

        viewModel = new AccountSettingsViewModel(mockApplication, mockUserRepository);
        viewModel.getIsLoading().observeForever(mockLoadingObserver);
        viewModel.getOperationError().observeForever(mockErrorObserver);
        viewModel.getOperationSuccess().observeForever(mockSuccessObserver);

        // Reset observers to ignore initial LiveData values
        org.mockito.Mockito.reset(mockLoadingObserver, mockErrorObserver, mockSuccessObserver);
    }

    @Test
    public void updateProfile_withEmptyField_emitsValidationError() {
        viewModel.updateProfile("", "user");
        verify(mockErrorObserver).onChanged("Please fill in all required fields.");
    }

    @Test
    public void changePassword_withMismatch_emitsValidationError() {
        viewModel.changePassword("old", "new1", "new2");
        verify(mockErrorObserver).onChanged("New password and confirmation do not match.");
    }

    @Test
    public void updateProfile_success_emitsSuccess() {
        MutableLiveData<Boolean> repoResult = new MutableLiveData<>();
        when(mockUserRepository.updateProfile(anyString(), anyString())).thenReturn(repoResult);

        viewModel.updateProfile("Name", "user");
        verify(mockLoadingObserver).onChanged(true);

        repoResult.setValue(true);

        verify(mockLoadingObserver).onChanged(false);
        verify(mockSuccessObserver).onChanged("Profile updated successfully.");
    }

    @Test
    public void changePassword_failure_emitsError() {
        MutableLiveData<Boolean> repoResult = new MutableLiveData<>();
        when(mockUserRepository.changePassword(anyString(), anyString(), anyString())).thenReturn(repoResult);

        viewModel.changePassword("old", "new", "new");
        verify(mockLoadingObserver).onChanged(true);

        repoResult.setValue(false);

        verify(mockLoadingObserver).onChanged(false);
        verify(mockErrorObserver).onChanged("Unable to update password. Please try again.");
    }
}
