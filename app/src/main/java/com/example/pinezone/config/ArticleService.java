package com.example.pinezone.config;

import com.example.pinezone.article.Article;
import com.example.pinezone.comment.Comment;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<Article> getArticle(@Query("aid")Long aid,
                             @Query("uid")int uid);

    @Multipart
    @POST("article")
    Call<ResponseBody> publishArticle(@Part("uid") RequestBody uid,
                                      @Part("cid") RequestBody cid,
                                      @Part("title") RequestBody title,
                                      @Part("content") RequestBody content,
                                      @Part List<MultipartBody.Part> imgs);

    @Multipart
    @POST("like")
    Call<ResponseBody> like(@Part("uid") RequestBody uid,
                            @Part("aid") RequestBody aid);

//    @Multipart
//    @DELETE("like")
//    Call<ResponseBody> unlike(@Part("uid") RequestBody uid,
//                              @Part("aid") RequestBody aid);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @HTTP(method = "DELETE", path = "like", hasBody = true)
    @FormUrlEncoded
    Call<ResponseBody> unlike(@Field("uid") int uid,
                              @Field("aid") Long aid);

    @Multipart
    @POST("star")
    Call<ResponseBody> star(@Part("uid") RequestBody uid,
                            @Part("aid") RequestBody aid);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @HTTP(method = "DELETE", path = "star", hasBody = true)
    @FormUrlEncoded
    Call<ResponseBody> unstar(@Field("uid") int uid,
                              @Field("aid") Long aid);

    @GET("userStar-list")
    Call<List<Article>> getUserStarArticleList(@Query("uid")int uid,
                                               @Query("page")int page,
                                               @Query("pageSize")int pageSize);

    @GET("comment-list")
    Call<List<Comment>> getCommentList(@Query("aid")Long aid,
                                       @Query("page")int page,
                                       @Query("num")int pageSize);

    @Multipart
    @POST("comment")
    Call<ResponseBody> publishComment(@Part("uid") RequestBody uid,
                                      @Part("aid") RequestBody aid,
                                      @Part("content") RequestBody content);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @HTTP(method = "DELETE", path = "comment", hasBody = true)
    @FormUrlEncoded
    Call<ResponseBody> deleteComment(@Field("comid") Long comid);

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @HTTP(method = "DELETE", path = "article", hasBody = true)
    @FormUrlEncoded
    Call<ResponseBody> deleteArticle(@Field("aid") Long aid);

    @Multipart
    @POST("report")
    Call<ResponseBody> report(@Part("uid") RequestBody uid,
                              @Part("aid") RequestBody aid,
                              @Part("content") RequestBody content);

    @Multipart
    @PUT("article")
    Call<ResponseBody> updateArticle(@Part("aid") RequestBody aid,
                                     @Part List<MultipartBody.Part> imgs,
                                     @Part("title") RequestBody title,
                                     @Part("content") RequestBody content);
}
