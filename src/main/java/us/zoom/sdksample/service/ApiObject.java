package us.zoom.sdksample.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiObject {


    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://zoom.ksta.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}
