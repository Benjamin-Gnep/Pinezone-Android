package com.example.pinezone.article;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
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
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.ArticleConstant;
import com.example.pinezone.config.ArticleService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";
    
    private static final String ARG_ARTICLE_TYPE = "articleType";

    private RecyclerView articleRecyclerView;
    private RefreshLayout refreshLayout;
    private ArticleAdapterPro articleAdapter;

    private String articleType = null;
    private View view;
    private static int requestPage = 1;
    private static int currentPage = 1;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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

        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setDragRate((float) 0.6);
        refreshLayout.setHeaderMaxDragRate((float) 1.6);
        refreshLayout.autoRefreshAnimationOnly();
        refreshLayout.finishRefresh(2000);
        refreshLayout.setEnableAutoLoadMore(true);
        articleRecyclerView = view.findViewById(R.id.article_recycler_view);
        final StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //解决瀑布流错乱，顶部留白
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        ((DefaultItemAnimator) articleRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        ((SimpleItemAnimator) articleRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        articleRecyclerView.getItemAnimator().setChangeDuration(0);
        articleRecyclerView.setHasFixedSize(true);

        articleRecyclerView.setLayoutManager(layoutManager);

        articleAdapter = new ArticleAdapterPro(getContext(),new ArrayList<Article>());
        articleAdapter.setAnimationEnable(true);
        articleAdapter.addChildClickViewIds(R.id.article_image,R.id.like_button,
                R.id.article_author_image,R.id.article_author_name,R.id.article_title,
                R.id.article_id);
        articleAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
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

        articleRecyclerView.requestLayout();
        articleRecyclerView.setAdapter(articleAdapter);
        articleRecyclerView.setItemAnimator(null);

        currentPage = 1;
        requestPage = 1;
        getArticleList(requestPage);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                requestPage = 1;
                currentPage = 1;
                getArticleList(requestPage);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                if(requestPage == currentPage){
                    requestPage++;
                    getArticleList(requestPage);
                    currentPage++;
                }
            }
        });

        //解决顶部留白问题，必写
        articleRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int[] first = new int[2];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });

        return view;
    }

    private void getArticleList(final int page){
        final List<Article> articleList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        final ArticleService articleService = retrofit.create(ArticleService.class);

        Call<List<Article>> call;

        switch (articleType){
            case ArticleConstant.TYPE_CANTEEN:
                call = articleService.getArticleList(1,page,10);break;
            case ArticleConstant.TYPE_STORE:
                call = articleService.getArticleList(2,page,10);break;
            case ArticleConstant.TYPE_PLAY:
                call = articleService.getArticleList(3,page,10);break;
            case ArticleConstant.TYPE_STUDY:
                call = articleService.getArticleList(4,page,10);break;
            case ArticleConstant.TYPE_GYM:
                call = articleService.getArticleList(5,page,10);break;
            default:
                throw new IllegalStateException("Unexpected value: " + articleType);
        }
        
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                assert response.body() != null;
                if(response.body().size() != 0){
                    Log.e(TAG, response.body().toString() );
                    articleList.addAll(response.body());
                    if(page == 1){
                        articleAdapter.refresh(articleList);
                    }else{
                        articleAdapter.loadMore(articleList);
                    }
                    articleRecyclerView.requestLayout();
                } else {
                    try{
                        Toast.makeText(getActivity(),"没有更多了",Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString() );
            }
        });
    }
}
