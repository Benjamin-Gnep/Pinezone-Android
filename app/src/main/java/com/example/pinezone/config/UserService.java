package com.example.pinezone.config;

import android.database.Observable;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("user")
    Call<ResponseBody> getUser(@Query("id")int id);

    @GET("user/picture")
    Call<ResponseBody> getUserImage(@Query("uid")int uid);

    @GET("loginByPhone/{phone}")
    Call<ResponseBody> loginByPhone(@Path("phone")Long phone);
    @Multipart

    @PUT("edit/{id}")
    Call<ResponseBody> editUpload(@Path("id")int uid,
                                  @Part("name") RequestBody name,
                                  @Part("profile") RequestBody profile,
                                  @Part("sex") RequestBody sex);

    @POST("uploadUserImg")
    Call<ResponseBody> uploadUserImg(@Part("uid") RequestBody uid,
                                     @Part MultipartBody.Part img);
}
