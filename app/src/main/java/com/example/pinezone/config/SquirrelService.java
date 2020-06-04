package com.example.pinezone.config;

import com.example.pinezone.squirrel.Squirrel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SquirrelService {
    @GET("squirrel")
    Call<Squirrel> getSquirrel(@Query("uid") int uid);

    @Multipart
    @POST("squirrel/food")
    Call<ResponseBody> setFood(@Part("uid") RequestBody uid,
                               @Part("food") RequestBody food);

    @Multipart
    @POST("squirrel/achievement")
    Call<ResponseBody> setAchievement(@Part("uid") RequestBody uid,
                                      @Part("achievement") RequestBody achievement);

    @Multipart
    @POST("squirrel/companion")
    Call<ResponseBody> setCompanion(@Part("uid") RequestBody uid,
                                    @Part("companion") RequestBody companion);

    @Multipart
    @POST("squirrel/pinecone")
    Call<ResponseBody> setPinecone(@Part("uid") RequestBody uid,
                                   @Part("pinecone") RequestBody pinecone);

    @Multipart
    @POST("squirrel/hp")
    Call<ResponseBody> setHp(@Part("uid") RequestBody uid,
                             @Part("hp") RequestBody hp);

    @GET("squirrel/travel")
    Call<TravelResponse> travel(@Query("hp") int hp,
                              @Query("achievement") float achievement,
                              @Query("companion") float companion);
}
