package com.example.quarantaine.Classes;

import android.os.SystemClock;

import com.example.quarantaine.DTO.RegisterUserDTO;
import com.example.quarantaine.Interfaces.IRegisterUserRequestHandler;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceController {

    private static boolean created = false; // Bool der fortæller om brugeren er registreret eller ej

    // Sender Post data til API, via vores RequestHandler's Call metode. og sætter Created bool til true hvis API svare tilbage positivt
    public static boolean  RegisterUser(RegisterUserDTO newUser) {

        IRegisterUserRequestHandler requestHandler = ApiClient.GetClient().create(IRegisterUserRequestHandler.class);
        Call<JsonObject> call = requestHandler.PostData(newUser);


         call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        created = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
           //     Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return created;
    }
}
