package com.example.borrowhub.viewmodel;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
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

public class AuthViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Application mockApplication;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private Observer<Boolean> mockIsLoadingObserver;

    @Mock
    private Observer<String> mockErrorObserver;

    @Mock
    private Observer<Boolean> mockLoginResultObserver;

    private AuthViewModel authViewModel;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        when(mockApplication.getString(com.example.borrowhub.R.string.error_empty_fields))
                .thenReturn("Username and password cannot be empty");
        when(mockApplication.getString(com.example.borrowhub.R.string.login_failed))
                .thenReturn("Invalid Credentials: Username or password is incorrect.");

        authViewModel = new AuthViewModel(mockApplication, mockUserRepository);

        authViewModel.getIsLoading().observeForever(mockIsLoadingObserver);
        authViewModel.getErrorMessage().observeForever(mockErrorObserver);

        // Clear the initial onChanged(false) call from observeForever
        clearInvocations(mockIsLoadingObserver);
    }

    @Test
    public void login_emptyUsername_setsErrorMessage() {
        authViewModel.login("", "password").observeForever(mockLoginResultObserver);

        verify(mockErrorObserver).onChanged("Username and password cannot be empty");
        verify(mockLoginResultObserver).onChanged(false);
    }

    @Test
    public void login_emptyPassword_setsErrorMessage() {
        authViewModel.login("username", "").observeForever(mockLoginResultObserver);

        verify(mockErrorObserver).onChanged("Username and password cannot be empty");
        verify(mockLoginResultObserver).onChanged(false);
    }

    @Test
    public void login_validCredentials_success() {
        MutableLiveData<Boolean> repoResult = new MutableLiveData<>();
        when(mockUserRepository.login(anyString(), anyString())).thenReturn(repoResult);

        authViewModel.login("user", "pass").observeForever(mockLoginResultObserver);

        verify(mockIsLoadingObserver).onChanged(true);
        
        repoResult.setValue(true);
        
        verify(mockIsLoadingObserver).onChanged(false);
        verify(mockLoginResultObserver).onChanged(true);
    }

    @Test
    public void login_invalidCredentials_failureAndErrorMessage() {
        MutableLiveData<Boolean> repoResult = new MutableLiveData<>();
        when(mockUserRepository.login(anyString(), anyString())).thenReturn(repoResult);

        authViewModel.login("user", "wrongpass").observeForever(mockLoginResultObserver);

        verify(mockIsLoadingObserver).onChanged(true);
        
        repoResult.setValue(false);
        
        verify(mockIsLoadingObserver).onChanged(false);
        verify(mockErrorObserver).onChanged("Invalid Credentials: Username or password is incorrect.");
        verify(mockLoginResultObserver).onChanged(false);
    }
}
