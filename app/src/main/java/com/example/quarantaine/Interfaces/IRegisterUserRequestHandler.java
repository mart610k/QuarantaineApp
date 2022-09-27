package com.example.quarantaine.Interfaces;

import com.example.quarantaine.DTO.RegisterUserDTO;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

// Interface der definere Http Kald
public interface IRegisterUserRequestHandler {

    @POST("api/register")
    @Headers("Content-Type: application/json")
    Call<JsonObject> PostData(@Body RegisterUserDTO registerUserDTO);
}
