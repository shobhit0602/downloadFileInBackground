package com.example.shobhit.loadingfileinbackground.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

/**
 * Created by Shobhit on 02-04-2018.
 */

public interface ApiInterface {

    @GET("gameplay3d/GamePlay/archive/master.zip")
    @Streaming
    Call<ResponseBody> downloadFile();
}
