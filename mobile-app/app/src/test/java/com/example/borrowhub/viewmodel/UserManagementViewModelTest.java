package com.example.borrowhub.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.borrowhub.data.local.entity.User;
import com.example.borrowhub.repository.UserRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

public class UserManagementViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Application application;

    @Mock
    private UserRepository userRepository;

    private final MutableLiveData<java.util.List<User>> usersLiveData = new MutableLiveData<>();

    private UserManagementViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userRepository.getAllUsers()).thenReturn(usersLiveData);
        viewModel = new UserManagementViewModel(application, userRepository);

        usersLiveData.setValue(Arrays.asList(
                new User(1, "Administrator", "admin", "admin"),
                new User(2, "Staff User", "staff1", "staff")
        ));
    }

    @Test
    public void deleteUser_protectedAdmin_doesNotCallRepositoryAndPostsError() {
        User protectedAdmin = new User(1, "Administrator", "admin", "admin");

        viewModel.deleteUser(protectedAdmin);

        verify(userRepository, never()).deleteUser(anyInt());
        assertEquals("Cannot delete the admin user", viewModel.getOperationError().getValue());
    }

    @Test
    public void resetPasswordToDefault_callsRepositoryWithDefaultPassword() {
        User staff = new User(2, "Staff User", "staff1", "staff");
        MutableLiveData<UserRepository.Result<Void>> resetResult = new MutableLiveData<>();
        when(userRepository.resetPassword(eq(2), eq("123"), eq("123"))).thenReturn(resetResult);

        viewModel.resetPasswordToDefault(staff);
        verify(userRepository).resetPassword(2, "123", "123");

        resetResult.setValue(new UserRepository.Result<>(null, null));
        assertEquals(Boolean.TRUE, viewModel.getOperationSuccess().getValue());
    }
}
