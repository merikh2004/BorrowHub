package com.example.borrowhub.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequestDTO {
    @SerializedName("current_password")
    private final String currentPassword;

    @SerializedName("new_password")
    private final String newPassword;

    @SerializedName("new_password_confirmation")
    private final String newPasswordConfirmation;

    public ChangePasswordRequestDTO(String currentPassword, String newPassword, String newPasswordConfirmation) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }
}
