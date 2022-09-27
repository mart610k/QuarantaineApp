package com.example.quarantaine.Classes;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit client = null; // interface client, der bruges til http kald

    /*
    GetClient laver en http retrofit client, således det gør det nemere at lave http kald
    ved brug af HttpLoggingInterceptor, som bruges til at logge requests og responses fra http kald
    OkHttpClient bruges til at oprette disse request og response
    Bygger så denne client med en retrofit builder. hvor der defineres hvor API addressen er
    Hvilken Json converter der bruges
    oh hvilken httpclient der bruges.
     */
    static Retrofit GetClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Logger body data
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        client = new Retrofit.Builder()
                .baseUrl("https://quarantaine.baage-it.dk")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();
        return client;
    }
}
