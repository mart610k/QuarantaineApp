package com.example.quarantaine.Interfaces;

import com.example.quarantaine.Classes.DTO.RegisterUserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IRegisterUserRequestHandler {

    @POST("/register")
    @Headers("Content-Type: application/json")
    Call<String> PostData(@Body RegisterUserDTO registerUserDTO);
}
