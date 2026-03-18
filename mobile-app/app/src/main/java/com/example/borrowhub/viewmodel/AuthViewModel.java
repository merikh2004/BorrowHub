package com.example.borrowhub.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.borrowhub.repository.UserRepository;

public class AuthViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MediatorLiveData<Boolean> logoutResult = new MediatorLiveData<>();

    public AuthViewModel(Application application) {
        super(application);
        this.userRepository = new UserRepository(application);
    }

    public AuthViewModel(Application application, UserRepository userRepository) {
        super(application);
        this.userRepository = userRepository;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getLogoutResult() {
        return logoutResult;
    }

    public LiveData<Boolean> login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            errorMessage.setValue(getApplication().getString(com.example.borrowhub.R.string.error_empty_fields));
            return new MutableLiveData<>(false);
        }

        isLoading.setValue(true);
        LiveData<Boolean> repoResult = userRepository.login(username, password);

        // We wrap the repository result to handle loading state and error messages
        MediatorLiveData<Boolean> finalResult = new MediatorLiveData<>();
        
        finalResult.addSource(repoResult, success -> {
            isLoading.setValue(false);
            if (Boolean.FALSE.equals(success)) {
                errorMessage.setValue(getApplication().getString(com.example.borrowhub.R.string.login_failed));
            }
            finalResult.setValue(success);
            finalResult.removeSource(repoResult);
        });

        return finalResult;
    }

    public boolean hasActiveSession() {
        return userRepository.hasActiveSession();
    }

    public void logout() {
        isLoading.setValue(true);
        LiveData<Boolean> repoResult = userRepository.logout();
        logoutResult.addSource(repoResult, success -> {
            isLoading.setValue(false);
            logoutResult.setValue(Boolean.TRUE.equals(success));
            logoutResult.removeSource(repoResult);
        });
    }

    public void clearLogoutResult() {
        logoutResult.setValue(null);
    }
}
