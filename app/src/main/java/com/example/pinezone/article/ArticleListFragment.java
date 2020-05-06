package com.example.pinezone.article;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.pinezone.ActivityCollector;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.ArticleConstant;
import com.example.pinezone.config.LoadMoreOnScrollListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";
    
    private static final String ARG_ARTICLE_TYPE = "articleType";
    private static final String ARG_PARAM2 = "param2";

    private String articleType = null;
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
        args.putString(ARG_ARTICLE_TYPE,articleType);
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
        ConstraintLayout actionBarView = view.findViewById(R.id.action_bar);
        TextView textView = (TextView) actionBarView.findViewById(R.id.action_bar_title);
        switch (articleType){
            case ArticleConstant.TYPE_CANTEEN:
                textView.setText("食堂");break;
            case ArticleConstant.TYPE_STORE:
                textView.setText("探店");break;
            case ArticleConstant.TYPE_PLAY:
                textView.setText("玩吧");break;
            case ArticleConstant.TYPE_STUDY:
                textView.setText("自习室");break;
            case ArticleConstant.TYPE_GYM:
                textView.setText("健身房");break;
        }

        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setDragRate((float) 0.6);
        refreshLayout.setHeaderMaxDragRate((float) 1.6);
        refreshLayout.autoRefreshAnimationOnly();
        refreshLayout.finishRefresh(2000);

        final RecyclerView articleRecyclerView = view.findViewById(R.id.article_recycler_view);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        articleRecyclerView.setLayoutManager(layoutManager);
        final ArticleAdapterPro articleAdapter = new ArticleAdapterPro(getContext(),getArticleList());
        articleAdapter.addChildClickViewIds(R.id.article_image,R.id.like_button,
                R.id.article_author_image,R.id.article_author_name,R.id.article_title);
        articleAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter,
                                         @NonNull View view, int position) {
                switch (view.getId()){
                    case R.id.article_image:
                        Intent intent = new Intent(getActivity(),ArticleDetailActivity.class);
                        getActivity().startActivity(intent);
                        break;
                }
            }
        });

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
                    articleAdapter.addData(getArticleList());
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
