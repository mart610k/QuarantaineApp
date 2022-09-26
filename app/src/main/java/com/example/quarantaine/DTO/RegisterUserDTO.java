package com.example.quarantaine.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


// Class til Registrer bruger objected
// @SerializedName - annotering der definere at feltet skal json serialiseres med det gevet navn
// @Expose - Annotering der definere om feltet skal være exposed til Json Serialisering eller Deserialisering - virker kun med Gson.
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