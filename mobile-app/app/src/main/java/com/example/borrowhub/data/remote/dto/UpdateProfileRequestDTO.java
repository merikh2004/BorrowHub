package com.example.borrowhub.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequestDTO {
    @SerializedName("name")
    private final String name;

    @SerializedName("username")
    private final String username;

    public UpdateProfileRequestDTO(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
