package com.example.pinezone.config;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("user")
    Call<ResponseBody> getUser(@Query("id")int id);

    @GET("user/picture")
    Call<ResponseBody> getUserImage(@Query("uid")int uid);

    @GET("loginByPhone/{phone}")
    Call<ResponseBody> loginByPhone(@Path("phone")Long phone);
}
