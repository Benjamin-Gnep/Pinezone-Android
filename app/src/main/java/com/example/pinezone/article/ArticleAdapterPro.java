package com.example.pinezone.article;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.pinezone.R;

import java.util.List;


public class ArticleAdapterPro extends BaseQuickAdapter<Article, BaseViewHolder> {

    private Context mContext;
    private List<Article> articleList;

    public ArticleAdapterPro(Context context, List<Article> data) {
        super(R.layout.fragment_article_item, data);
        articleList = data;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Article article) {
        helper.setText(R.id.article_title,article.getTitle())
                .setText(R.id.article_author_name,article.getUsername())
                .setText(R.id.like_num,String.valueOf(article.getLikenum()))
                .setText(R.id.article_id,String.valueOf(article.getAid()));

        ImageView userImage = helper.findView(R.id.article_author_image);
        ImageView articleImage = helper.findView(R.id.article_image);
        Glide.with((getContext())).load(article.getAimg().get(0).path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.d("Wain","加载失败 errorMsg:"+(e!=null?e.getMessage():"null"));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        Log.d("Wain","成功  Drawable Name:"+resource.getClass().getCanonicalName());
                        return false;
                    }
                })
                .error(R.drawable.default_background)
                .into(articleImage);
        Glide.with((getContext())).load(article.getUimg()).into(userImage);


        Resources resources = mContext.getResources();
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

    public void refresh(List<Article> list) {
        articleList.clear();
        notifyDataSetChanged();
        int currentSize = articleList.size();
        int insertSize = list.size();;
        articleList.addAll(list);
        notifyItemRangeInserted(currentSize,insertSize);
    }

    public void loadMore(List<Article> list) {
        int currentSize = articleList.size();
        int insertSize = list.size();
        articleList.addAll(list);
        notifyItemRangeInserted(currentSize,insertSize);
    }
}
