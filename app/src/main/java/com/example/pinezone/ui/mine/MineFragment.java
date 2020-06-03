package com.example.pinezone.ui.mine;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.article.Article;
import com.example.pinezone.article.ArticleAdapterPro;
import com.example.pinezone.article.ArticleDetailActivity;
import com.example.pinezone.config.ArticleService;
import com.example.pinezone.ui.setting.SettingsActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MineFragment extends Fragment {
    private static final String TAG = "MineFragment";

    private int page = 1;

    private ImageButton setting;
    private ImageButton QRcode;
    private Button editInfo;

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

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(root);
        final StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
        articleRecyclerView.setLayoutManager(layoutManager);
        mineArticleAdapter = new ArticleAdapterPro(getContext(),new ArrayList<Article>());

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
                        ArticleDetailActivity.StartActivity(getActivity(),aid,uid);
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

        return root;
    }

    private void getMineArticle(final int page) {
        final List<Article> articleList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        final ArticleService articleService = retrofit.create(ArticleService.class);

        int uid = mineViewModel.getUserId().getValue();
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

    private void initView(View root) {
        mineViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        scrollView = root.findViewById(R.id.mine_scroll);
        refreshLayout = root.findViewById(R.id.mine_refresh);
        mineActionBar = root.findViewById(R.id.mine_action_bar);
        mineActionBar.setVisibility(View.INVISIBLE);
        articleRecyclerView = root.findViewById(R.id.mine_article_list);
        articleRecyclerView.setNestedScrollingEnabled(false);
        articleRecyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        articleRecyclerView.setFocusable(false);

        userName = root.findViewById(R.id.mine_user_name);
        userSign = root.findViewById(R.id.mine_user_sign);
        userImage = root.findViewById(R.id.mine_user_image);
        userGradeSex = root.findViewById(R.id.sex_grade_tag);
        userLocation = root.findViewById(R.id.location_tag);
        userFans = root.findViewById(R.id.fans);
        userFollow = root.findViewById(R.id.follow);
        userArticle = root.findViewById(R.id.article);

        setting = root.findViewById(R.id.mine_setting);
        QRcode = root.findViewById(R.id.mine_QR);
        editInfo = root.findViewById(R.id.edit_info_button);

        mineViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userName.setText(s);
                mineActionBar.setText(s);
            }
        });
        mineViewModel.getUserSign().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userSign.setText(s);
            }
        });
        mineViewModel.getUserImage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Glide.with(requireContext()).load(s).into(userImage);
                }
            }
        });
        mineViewModel.getUserSex().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                assert s != null;
                if(s.equals("男")){
                    userGradeSex.setSelected(true);
                }
                if(s.equals("女")){
                    userGradeSex.setSelected(false);
                }else {
                    userGradeSex.setSelected(true);
                }
            }
        });
        mineViewModel.getUserGrade().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userGradeSex.setText(s);
            }
        });
        mineViewModel.getUserFans().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userFans.setText(s);
            }
        });
        mineViewModel.getUserFollows().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userFollow.setText(s);
            }
        });
        mineViewModel.getUserLocation().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userLocation.setText(s);
            }
        });

        mineViewModel.getUserArticle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userArticle.setText(s);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MineInfo.class);
                startActivity(intent);
            }
        });
    }
}
