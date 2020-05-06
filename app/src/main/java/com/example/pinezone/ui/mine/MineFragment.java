package com.example.pinezone.ui.mine;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pinezone.R;
import com.example.pinezone.article.Article;
import com.example.pinezone.article.ArticleAdapter;
import com.example.pinezone.config.LoadMoreOnScrollListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment {
    private static final String TAG = "MineFragment";

    private int page = 0;

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
    private RecyclerView articleRecyclerView;

    private TextView mineActionBar;

    public static MineFragment newInstance() {
        return new MineFragment();

    }

    @Override
    public void onResume() {
        super.onResume();
        final StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
        articleRecyclerView.setLayoutManager(layoutManager);
        final ArticleAdapter mineArticleAdapter = new ArticleAdapter(getMineArticle());
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
                        mineArticleAdapter.setMoreData(getMineArticle());
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(root);
        return root;
    }

    private List<Article> getMineArticle() {
        List<Article> articles = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Article coffee = new Article("瑞幸咖啡LuckinCoffee",R.drawable.t5);
            articles.add(coffee);
            Article milktea = new Article("一点点奶茶ALittleMilkTea",R.drawable.t1);
            articles.add(milktea);
            Article soup = new Article("福建小吃FujianLocalSoup",R.drawable.t2);
            articles.add(soup);
            Article noodle = new Article("猪肉面PorkNoodle",R.drawable.t3);
            articles.add(noodle);
            Article jiaozi = new Article("沙县饺子ShaxianJiaozi",R.drawable.t4);
            articles.add(jiaozi);
        }
        return articles;
    }

    private void initView(View root) {
        mineViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        scrollView = root.findViewById(R.id.mine_scroll);
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

        mineViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userName.setText(s);
            }
        });
        mineViewModel.getUserSign().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                userSign.setText(s);
            }
        });
        mineViewModel.getUserImage().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                userImage.setImageResource(integer);
            }
        });
        mineViewModel.getUserSex().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s.equals("男")){
                    userGradeSex.setSelected(true);
                }
                if(s.equals("女")){
                    userGradeSex.setSelected(false);
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
    }

}
