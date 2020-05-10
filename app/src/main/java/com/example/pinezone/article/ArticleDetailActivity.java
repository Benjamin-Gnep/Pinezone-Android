package com.example.pinezone.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.ArticleService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleDetailActivity extends BasicActivity {
    private static String ARTICLE_ID = "articleId";
    private static String USER_ID = "userId";

    private List<String> detailImageGroup;
    private BaseQuickAdapter<String, BaseViewHolder> mBaseQuickAdapter;

    private int articleId;
    private int userId;
    private ImageView detailAuthorImage;
    private TextView detailAuthorName;
    private TextView detailAuthorDescribe;
    private TextView detailTitle;
    private TextView detailContent;
    private TextView detailDate;
    private RecyclerView detailImageView;
    private Button detailLikeButton;
    private Button detailStarButton;
    private Button detailCommentButton;
//    private BannerPagerView detailImageView;

    public static void StartActivity(Context context, int articleId, int userId){
        Intent intent = new Intent(context,ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_ID,articleId);
        intent.putExtra(USER_ID,userId);
        context.startActivity(intent);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadArticle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        Intent intent = getIntent();
        if(intent != null){
            articleId = intent.getIntExtra(ARTICLE_ID,0);
            userId = intent.getIntExtra(USER_ID,0);
        }else {
            Toast.makeText(ArticleDetailActivity.this,"获取文章错误"
                    ,Toast.LENGTH_SHORT).show();
        }
        initView();
    }

    private void loadArticle() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        final ArticleService articleService = retrofit.create(ArticleService.class);
        Call<Article> call = articleService.getArticle(articleId, userId);
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(@NonNull Call<Article> call, @NonNull final Response<Article> response) {
                Log.e("TAG", response.body().toString() );
                final Article article = response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        detailAuthorName.setText(article.getUsername());
                        detailAuthorDescribe.setText(article.getDescribe());
                        detailTitle.setText(article.getTitle());
                        detailContent.setText(article.getContent());
                        detailDate.setText("编辑于 " + article.getDatetime());
                        Glide.with(ArticleDetailActivity.this)
                                .load(article.getUimg()).into(detailAuthorImage);
                        for(int i = 0 ; i < article.getAimg().size();i++){
                            detailImageGroup.add(article.getAimg().get(i).path);
                            Log.e("TAG",article.getAimg().get(i).path );
                        }
                        detailLikeButton.setText(String.valueOf(article.getLikenum()));
                        detailStarButton.setText(String.valueOf(article.getStarnum()));
                        detailCommentButton.setText(String.valueOf(article.getCommentnum()));
                        setAdapter();

                    }
                });


            }
            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    private void initView() {


        detailImageGroup = new ArrayList<>();

        //detailImageGroup = getImageGroup();
//        for(int i = 0; i < 2;i++){
//            detailImageGroup.add(R.drawable.t5);
//            detailImageGroup.add(R.drawable.t1);
//        }

        detailAuthorImage = findViewById(R.id.detail_author_image);
        detailAuthorName = findViewById(R.id.detail_author_name);
        detailAuthorDescribe = findViewById(R.id.detail_author_describe);
        detailContent = findViewById(R.id.detail_content);
        detailTitle = findViewById(R.id.detail_title);
        detailDate = findViewById(R.id.detail_date);
        detailCommentButton = findViewById(R.id.detail_comment_button);
        detailLikeButton = findViewById(R.id.detail_like_button);
        detailStarButton = findViewById(R.id.detail_star_button);

        detailImageView = findViewById(R.id.detail_image_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        detailImageView.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(detailImageView);
    }

    void setAdapter(){
        mBaseQuickAdapter= new BaseQuickAdapter<String, BaseViewHolder>
                (R.layout.fragment_detail_image_item,detailImageGroup) {
            @Override
            protected void convert(@NotNull BaseViewHolder helper, String s) {
                ImageView imageView =  helper.getView(R.id.img);
                Log.e("TAG", s );
                Glide.with(ArticleDetailActivity.this)
                        .load(s).into(imageView);

//                Resources resources = getContext().getResources();
//                //获取屏幕数据
//                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//                //获取屏幕宽高，单位是像素
//                int widthPixels = displayMetrics.widthPixels;
//                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
//                lp.width = widthPixels;
//                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                if(lp.height > 2 * lp.width){
//                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    lp.height = 2*lp.width;
//                }
//                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        };
        detailImageView.setAdapter(mBaseQuickAdapter);
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