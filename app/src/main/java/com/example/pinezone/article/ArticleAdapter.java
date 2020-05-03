package com.example.pinezone.article;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        holder.authorImage.setImageResource(R.drawable.user);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void setMoreData(List<Article> articleList) {
        for(Article article:articleList){
            this.articleList.add(article);
        }
    }

    public void refresh(List<Article> articleList) {
        this.articleList.clear();
        this.articleList = articleList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView articleImage;
        TextView articleTitle;
        ImageView authorImage;
        TextView authorName;
        Button likeButton;

        public ViewHolder(View view,ViewGroup group){
            super(view);
            articleTitle = view.findViewById(R.id.article_title);
            articleImage = view.findViewById(R.id.article_image);
            authorImage = view.findViewById(R.id.article_author_image);
            authorName = view.findViewById(R.id.article_author_name);
            likeButton = view.findViewById(R.id.like_button);


            Resources resources = group.getResources();
            //获取屏幕数据
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            //获取屏幕宽高，单位是像素
            int widthPixels = displayMetrics.widthPixels/40*19;
            ViewGroup.LayoutParams lp = articleImage.getLayoutParams();
            lp.width = widthPixels;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            if(lp.height > 2 * lp.width){
                articleImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                lp.height = 2*lp.width;
            }
            articleImage.setLayoutParams(lp);
        }
    }



}
