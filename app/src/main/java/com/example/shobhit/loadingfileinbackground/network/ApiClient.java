package com.example.shobhit.loadingfileinbackground.network;

import retrofit2.Retrofit;

/**
 * Created by Shobhit on 02-04-2018.
 */

public class ApiClient {

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl("https://github.com/")
                .build();
    }
}
