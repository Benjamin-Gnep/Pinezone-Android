package com.example.pinezone.article;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

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
                .setImageResource(R.id.article_image,article.getImage());

        ImageView articleImage = helper.getView(R.id.article_image);
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

    public  void refresh(List<Article> articleList) {
        this.articleList.clear();
        this.articleList.addAll(articleList);
        notifyDataSetChanged();
    }
}
