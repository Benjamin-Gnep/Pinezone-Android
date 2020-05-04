package com.example.pinezone.article;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pinezone.ActivityCollector;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.LoadMoreOnScrollListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleListFragment extends Fragment {
    private static final String ARG_ARTICLE_TYPE = "articleType";
    private static final String ARG_PARAM2 = "param2";

    private String articleType;
    private View view;
    private int page = 0;

    public ArticleListFragment() {
        // 需要空的构造函数
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param articleType Parameter 1.
     * @return A new instance of fragment ArticleListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleListFragment newInstance(String articleType) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString("ARG_ARTICLE_TYPE",articleType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //设置按返回键退出当前fragment，因为HomeFragment中写了退出软件的语句所以要进行覆盖
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    return false;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleType =  getArguments().getString(ARG_ARTICLE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article_list, container, false);
        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setDragRate((float) 0.6);
        refreshLayout.setHeaderMaxDragRate((float) 1.6);
        refreshLayout.autoRefreshAnimationOnly();//自动刷新，只显示动画不执行刷新
        refreshLayout.finishRefresh(1200);//延迟2000毫秒后结束刷新

        final RecyclerView articleRecyclerView = view.findViewById(R.id.article_recycler_view);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        articleRecyclerView.setLayoutManager(layoutManager);
        final ArticleAdapter articleAdapter = new ArticleAdapter(getArticleList());
        articleRecyclerView.setAdapter(articleAdapter);


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                articleAdapter.refresh(getArticleList());
                page = 0;
            }
        });


        articleRecyclerView.addOnScrollListener(new LoadMoreOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (currentPage > page) {
                    page++;
                    articleAdapter.setMoreData(getArticleList());
                }
            }
        });
        return view;
    }

    public List<Article> getArticleList(){
        List<Article> articleList = new ArrayList<>();
        for(int i = 0;i < 3; i++){
            Article coffee = new Article("瑞幸咖啡LuckinCoffee",R.drawable.t5);
            articleList.add(coffee);
            Article milktea = new Article("一点点奶茶ALittleMilkTea",R.drawable.t1);
            articleList.add(milktea);
            Article soup = new Article("福建小吃FujianLocalSoup",R.drawable.t2);
            articleList.add(soup);
            Article noodle = new Article("猪肉面PorkNoodle",R.drawable.t3);
            articleList.add(noodle);
            Article jiaozi = new Article("沙县饺子ShaxianJiaozi",R.drawable.t4);
            articleList.add(jiaozi);
        }
        return articleList;
    }


}
