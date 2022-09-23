package com.example.quarantaine.Classes.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class RegisterUserDTO {
    @SerializedName("Username")
    @Expose
    private String username;

    @SerializedName("Password")
    @Expose
    private String password;

    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("Name")
    @Expose
    private String name;



    public RegisterUserDTO(String username, String password, String name, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}