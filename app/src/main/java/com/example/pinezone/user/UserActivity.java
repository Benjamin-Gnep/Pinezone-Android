package com.example.pinezone.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.article.Article;
import com.example.pinezone.article.ArticleAdapterPro;
import com.example.pinezone.article.ArticleDetailActivity;
import com.example.pinezone.config.ArticleService;
import com.example.pinezone.ui.mine.MineFragment;
import com.example.pinezone.ui.mine.MineInfo;
import com.example.pinezone.ui.mine.MineViewModel;
import com.example.pinezone.ui.setting.SettingsActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "MineFragment";

    private int page = 1;

    private int uid;
    private ImageButton setting;
    private ImageButton QRcode;
    private Button followBotton;

    private MineViewModel mineViewModel;
    private TextView userName;
    private TextView userSign;
    private ImageView userImage;
    private Button userGradeSex;
    private Button userLocation;
    private TextView userFans;
    private TextView userFollow;
    private TextView userArticle;

    private NestedScrollView scrollView;
    private RefreshLayout refreshLayout;
    private RecyclerView articleRecyclerView;

    private TextView mineActionBar;
    private ArticleAdapterPro mineArticleAdapter;

    public static void StartActivity(Context context, int uid){
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("uid",uid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid",0);
        initView();
        final StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
        articleRecyclerView.setLayoutManager(layoutManager);
        mineArticleAdapter = new ArticleAdapterPro(UserActivity.this,new ArrayList<Article>());

        page = 1;
        getMineArticle(page);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                page = 1;
                getMineArticle(page);
            }
        });

        mineArticleAdapter.setAnimationEnable(true);
        mineArticleAdapter.addChildClickViewIds(R.id.article_image,R.id.like_button,
                R.id.article_author_image,R.id.article_author_name,R.id.article_title,
                R.id.article_id);
        mineArticleAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter,
                                         @NonNull View view, int position) {
                switch (view.getId()){
                    case R.id.article_image:
                    case R.id.article_title:
                        ConstraintLayout layout = (ConstraintLayout) view.getParent();
                        TextView textView = layout.findViewById(R.id.article_id);
                        Long aid = Long.parseLong(textView.getText().toString());
                        int uid = MainActivity.getUid();
                        ArticleDetailActivity.StartActivity(UserActivity.this,aid,uid);
                        break;
                }
            }
        });
        articleRecyclerView.setAdapter(mineArticleAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mineActionBar.setVisibility(View.VISIBLE);
                    float alpha = 0;
                    if (scrollY > 800) {
                        alpha = 1;
                    } else {
                        alpha = scrollY / (800 * 1.0f);
                    }
                    mineActionBar.setAlpha(alpha);

                    //判断scrollview是否到达最底端，然后加载下一页
                    View onlyChild = scrollView.getChildAt(0);
                    if (onlyChild.getHeight() <= scrollY + scrollView.getHeight()) {   // 如果满足就是到底部了
                        page++;
                        getMineArticle(page);
                    }
                }
            });
        }
    }

    private void getMineArticle(final int page) {
        final List<Article> articleList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        final ArticleService articleService = retrofit.create(ArticleService.class);

        Call<List<Article>> call = articleService.getUserArticleList(uid, page,10);


        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                assert response.body() != null;
                if(response.body().size() != 0){
                    Log.e(TAG, response.body().toString() );
                    articleList.addAll(response.body());
                    if(page == 1){
                        mineArticleAdapter.refresh(articleList);
                    }else{
                        mineArticleAdapter.loadMore(articleList);
                    }
                    articleRecyclerView.requestLayout();
                } else {
                }
            }
            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString() );
            }
        });
    }

    private void initView() {
        mineViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        scrollView = findViewById(R.id.mine_scroll);
        refreshLayout = findViewById(R.id.mine_refresh);
        mineActionBar = findViewById(R.id.mine_action_bar);
        mineActionBar.setVisibility(View.INVISIBLE);
        articleRecyclerView = findViewById(R.id.mine_article_list);
        articleRecyclerView.setNestedScrollingEnabled(false);
        articleRecyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        articleRecyclerView.setFocusable(false);

        userName = findViewById(R.id.mine_user_name);
        userSign = findViewById(R.id.mine_user_sign);
        userImage = findViewById(R.id.mine_user_image);
        userGradeSex = findViewById(R.id.sex_grade_tag);
        userLocation = findViewById(R.id.location_tag);
        userFans = findViewById(R.id.fans);
        userFollow = findViewById(R.id.follow);
        userArticle = findViewById(R.id.article);

        setting = findViewById(R.id.mine_setting);
        QRcode = findViewById(R.id.mine_QR);
        followBotton = findViewById(R.id.edit_info_button);

        followBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MineInfo.class);
                startActivity(intent);
            }
        });
    }
}
