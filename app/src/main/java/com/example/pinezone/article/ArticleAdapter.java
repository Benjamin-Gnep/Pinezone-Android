package com.example.pinezone.article;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinezone.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articleList;

    public ArticleAdapter(List<Article> articles){
            articleList = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_article_item,parent,false);
        final ViewHolder holder = new ViewHolder(view,parent);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.articleTitle.setText(article.getTitle());
        holder.articleImage.setImageResource(article.getImage());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView articleImage;
        TextView articleTitle;

        public ViewHolder(View view,ViewGroup group){
            super(view);
            articleTitle = view.findViewById(R.id.article_title);
            articleImage = view.findViewById(R.id.article_image);
            Resources resources = group.getResources();
            //获取屏幕数据
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            //获取屏幕宽高，单位是像素
            int widthPixels = displayMetrics.widthPixels/2;
            ViewGroup.LayoutParams lp = articleImage.getLayoutParams();
            lp.width = widthPixels;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
           articleImage.setLayoutParams(lp);
        }
    }



}
