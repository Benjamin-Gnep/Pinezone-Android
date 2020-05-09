package com.example.pinezone.config;

import com.example.pinezone.article.Article;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("article-list")
    Call<List<Article>> getArticleList(@Query("cid")int cid,
                                       @Query("page")int page,
                                       @Query("pageSize")int pageSize);

    Call<ResponseBody> getArticle();
}