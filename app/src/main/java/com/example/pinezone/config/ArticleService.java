package com.example.pinezone.config;

import com.example.pinezone.article.Article;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("article-list")
    Call<List<Article>> getArticleList(@Query("cid")int cid,
                                       @Query("page")int page,
                                       @Query("pageSize")int pageSize);

    @GET("userArticle-list")
    Call<List<Article>> getUserArticleList(@Query("uid")int uid,
                                           @Query("page")int page,
                                           @Query("pageSize")int pageSize);

    @GET("article")
    Call<Article> getArticle(@Query("aid")int aid,
                             @Query("uid")int uid);

    @Multipart
    @POST("article")
    Call<ResponseBody> publishArticle(@Part("uid") RequestBody uid,
                                      @Part("cid") RequestBody cid,
                                      @Part("title") RequestBody title,
                                      @Part("content") RequestBody content,
                                      @Part List<MultipartBody.Part> imgs);
}
