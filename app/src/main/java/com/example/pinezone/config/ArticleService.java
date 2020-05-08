package com.example.pinezone.config;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("user")
    Call<ResponseBody> getArticleList(@Query("id")int id);
    Call<ResponseBody> getArticle();
}
