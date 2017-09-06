package com.sofac.fxmharmony.server.retrofit;

import com.sofac.fxmharmony.server.type.ServerRequest;
import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;

import static com.sofac.fxmharmony.Constants.PART_CONTROLLER;

/**
 * Created by Maxim on 03.08.2017.
 */

public interface ServiceRetrofit {

    @POST(PART_CONTROLLER)
    Call<ResponseBody> getData(@Body ServerRequest serverRequest);
}