package com.sofac.fxmharmony.server.retrofit;

import com.sofac.fxmharmony.server.type.ServerRequest;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Part;

import static com.sofac.fxmharmony.Constants.PART_CONTROLLER;
import static com.sofac.fxmharmony.Constants.PART_UPLOAD_FILES;

/**
 * Created by Maxim on 03.08.2017.
 */

public interface ServiceRetrofit {

    @POST(PART_CONTROLLER)
    Call<ResponseBody> getData(@Body ServerRequest serverRequest);

    @Multipart
    @POST(PART_UPLOAD_FILES)
    Call<ResponseBody> uploadFiles(@Part("name") RequestBody fuck, @Part ArrayList<MultipartBody.Part> file);
}