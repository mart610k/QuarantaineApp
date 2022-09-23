package com.example.quarantaine.Classes;

import android.app.Activity;
import android.widget.Toast;

import com.example.quarantaine.Classes.DTO.RegisterUserDTO;
import com.example.quarantaine.Interfaces.IRegisterUserRequestHandler;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceController {

    private static boolean created = false;
    public static boolean RegisterUser(RegisterUserDTO newUser) {

        IRegisterUserRequestHandler requestHandler = ApiClient.GetClient().create(IRegisterUserRequestHandler.class);
        Call<String> call = requestHandler.PostData(newUser);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String result = response.body();
                    created = true;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Response with Failure exception

            }
        });

        return created;

    }


}
