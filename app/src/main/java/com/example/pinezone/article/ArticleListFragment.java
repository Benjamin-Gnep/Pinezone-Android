package com.example.pinezone.article;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pinezone.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleListFragment extends Fragment {

    private View view;

    private static final String ARG_ARTICLE_TYPE = "articleType";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String articleType;
    private String mParam2;

    public ArticleListFragment() {
        // 需要空的构造函数
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param articleList Parameter 1.
     * @return A new instance of fragment ArticleListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleListFragment newInstance(List<Article> articleList) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_ARTICLE_TYPE",(Serializable)articleList);
        fragment.setArguments(args);
        return fragment;
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
        RecyclerView articleRecyclerView = view.findViewById(R.id.article_recycler_view);
        articleRecyclerView.setLayoutManager
                (new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        ArticleAdapter articleAdapter = new ArticleAdapter(getArticleList());
        articleRecyclerView.setAdapter(articleAdapter);
        return view;
    }

    public List<Article> getArticleList(){
        List<Article> articleList = new ArrayList<>();
        for(int i = 0;i < 3; i++){
            Article coffee = new Article("LuckinCoffee",R.drawable.t5);
            articleList.add(coffee);
            Article milktea = new Article("ALittleMilkTea",R.drawable.t1);
            articleList.add(milktea);
            Article soup = new Article("FujianLocalSoup",R.drawable.t2);
            articleList.add(soup);
            Article noodle = new Article("PorkNoodle",R.drawable.t3);
            articleList.add(noodle);
            Article jiaozi = new Article("ShaxianJiaozi",R.drawable.t4);
            articleList.add(jiaozi);
        }
        return articleList;
    }

}
