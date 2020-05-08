package com.example.pinezone.article;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailActivity extends BasicActivity {
    private static String ARTICLE_ID = "articleId";

    private List<Integer> detailImageGroup;
    private BaseQuickAdapter<Integer, BaseViewHolder> mBaseQuickAdapter;

    private int articleId;
    private ImageView detailAuthorImage;
    private TextView detailAuthorName;
    private TextView detailAuthorDescribe;
    private TextView detailTitle;
    private TextView detailContent;
    private TextView detailDate;
    private RecyclerView detailImageView;
//    private BannerPagerView detailImageView;

    public static void StartActivity(Context context, int articleId){
        Intent intent = new Intent(context,ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_ID,articleId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        Intent intent = getIntent();
        if(intent != null){
            articleId = intent.getIntExtra(ARTICLE_ID,0);
        }else {
            Toast.makeText(ArticleDetailActivity.this,"获取文章错误"
                    ,Toast.LENGTH_SHORT).show();
        }
        initView();
    }

    private void initView() {


        detailImageGroup = new ArrayList<>();

        getArticleJson(articleId);
        //detailImageGroup = getImageGroup();
        for(int i = 0; i < 2;i++){
            detailImageGroup.add(R.drawable.t5);
            detailImageGroup.add(R.drawable.t1);
        }

        detailImageView = findViewById(R.id.detail_image_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        detailImageView.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(detailImageView);

//        BannerLayoutManager bannerLayoutManager = new BannerLayoutManager(this,200);
//        detailImageView.setLayoutManager(bannerLayoutManager);
//        ImageAdapter imageAdapter = new ImageAdapter(detailImageGroup);
//        detailImageView.setAdapter(imageAdapter);
//        WormIndicatorView indicatorView = findViewById(R.id.detail_indicator);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            indicatorView.setBanner(detailImageView,detailImageGroup.size());
//        }

        mBaseQuickAdapter=new BaseQuickAdapter<Integer, BaseViewHolder>
                (R.layout.fragment_detail_image_item,detailImageGroup) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                helper.setImageResource(R.id.img,item);
            }
        };
        detailImageView.setAdapter(mBaseQuickAdapter);
    }

    private void getArticleJson(int articleId) {
    }
}
//
//class ImageAdapter extends BannerAdapter<Integer,ImageAdapter.ViewHolder>{
//    private List<Integer> imageList;
//
//    public ImageAdapter(List<Integer> imageList){
//        this.imageList = imageList;
//    }
//
//
//    static class ViewHolder extends  RecyclerView.ViewHolder{
//        ImageView image;
//
//        public ViewHolder(@NonNull View view) {
//            super(view);
//            image = view.findViewById(R.id.img);
//        }
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.fragment_detail_image_item,parent,false);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        int imgSource = imageList.get(position);
//        holder.image.setImageResource(imgSource);
//    }
//
//}