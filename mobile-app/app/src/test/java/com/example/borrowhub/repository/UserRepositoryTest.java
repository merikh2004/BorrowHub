package com.example.borrowhub.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.borrowhub.data.local.SessionManager;
import com.example.borrowhub.data.local.dao.UserDao;
import com.example.borrowhub.data.local.entity.User;
import com.example.borrowhub.data.remote.api.ApiService;
import com.example.borrowhub.data.remote.dto.LoginRequestDTO;
import com.example.borrowhub.data.remote.dto.LoginResponseDTO;
import com.example.borrowhub.data.remote.dto.UserDTO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ApiService mockApiService;
    @Mock
    private SessionManager mockSessionManager;
    @Mock
    private UserDao mockUserDao;
    @Mock
    private Call<LoginResponseDTO> mockCall;
    @Mock
    private Observer<Boolean> mockObserver;
    @Mock
    private Call<Void> mockLogoutCall;

    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userRepository = new UserRepository(mockApiService, mockSessionManager, mockUserDao);
    }

    @Test
    public void login_success_savesTokenAndUser() {
        // Arrange
        LoginResponseDTO mockResponse = new LoginResponseDTO();
        LoginResponseDTO.Data mockData = new LoginResponseDTO.Data();
        mockData.setToken("fake_token");
        UserDTO userDto = new UserDTO();
        userDto.setId(1);
        userDto.setName("Test User");
        userDto.setUsername("testuser");
        userDto.setRole("student");
        mockData.setUser(userDto);
        mockResponse.setData(mockData);

        when(mockApiService.login(any(LoginRequestDTO.class))).thenReturn(mockCall);

        // Act
        LiveData<Boolean> result = userRepository.login("testuser", "password");
        result.observeForever(mockObserver);

        // Capture callback
        ArgumentCaptor<Callback<LoginResponseDTO>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(captor.capture());

        // Simulate success response
        captor.getValue().onResponse(mockCall, Response.success(mockResponse));

        // Assert
        verify(mockSessionManager).saveAuthToken("Bearer fake_token");
        
        // Wait a bit for the executor service to run
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        verify(mockUserDao).insertUser(any(User.class));
        verify(mockObserver).onChanged(true);
    }

    @Test
    public void logout_clearsSessionAndDeletesUser() {
        // Arrange
        when(mockSessionManager.getAuthToken()).thenReturn("Bearer fake_token");
        when(mockApiService.logout("Bearer fake_token")).thenReturn(mockLogoutCall);

        // Act
        LiveData<Boolean> result = userRepository.logout();
        result.observeForever(mockObserver);

        ArgumentCaptor<Callback<Void>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockLogoutCall).enqueue(captor.capture());
        captor.getValue().onResponse(mockLogoutCall, Response.success(null));

        // Assert
        verify(mockSessionManager).clearSession();
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        verify(mockUserDao).deleteAll();
        verify(mockApiService).logout("Bearer fake_token");
        verify(mockObserver).onChanged(true);
        result.removeObserver(mockObserver);
    }
}
