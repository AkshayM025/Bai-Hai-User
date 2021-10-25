package com.techno.baihai.api;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    private static APIInterface apiInterface = null;


    public static Retrofit getClient() {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().cache(null).addInterceptor(interceptor).build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(null)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)//okHttpClient)
                .build();


        return retrofit;
    }

    public static APIInterface loadInterface() {
        if (apiInterface == null) {
            apiInterface = APIClient.getClient().create(APIInterface.class);
        }
        return apiInterface;
    }

}