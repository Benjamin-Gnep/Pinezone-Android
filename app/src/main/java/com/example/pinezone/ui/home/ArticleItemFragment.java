package com.example.pinezone.ui.home;

import android.content.Context;
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
import com.example.pinezone.ui.home.article.ArticleContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ArticleItemFragment extends Fragment {

    private List<ArticleContent.Article> articleList = new ArrayList<>();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ArticleItemFragment newInstance(int columnCount) {
        ArticleItemFragment fragment = new ArticleItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_item_list, container, false);
        initArticles();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            mColumnCount = 2;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager
                        (mColumnCount,StaggeredGridLayoutManager.VERTICAL));
            }
            recyclerView.setAdapter(new MyArticleItemRecyclerViewAdapter(articleList, mListener));
        }
        return view;
    }

    private void initArticles() {
        for(int i = 0;i < 3; i++){
            ArticleContent.Article coffee = new ArticleContent.Article("1",
                    "LuckinCoffee","detail","title",R.drawable.t5);
            articleList.add(coffee);
            ArticleContent.Article milktea = new ArticleContent.Article("2",
                    "ALittleMilkTea","detail","title",R.drawable.t1);
            articleList.add(milktea);
            ArticleContent.Article soup = new ArticleContent.Article("3",
                    "FujianLocalSoup","detail","title",R.drawable.t2);
            articleList.add(soup);
            ArticleContent.Article noodle = new ArticleContent.Article("4",
                    "PorkNoodle","detail","title",R.drawable.t3);
            articleList.add(noodle);
            ArticleContent.Article jiaozi = new ArticleContent.Article("5",
                    "ShaxianJiaozi","detail","title",R.drawable.t4);
            articleList.add(jiaozi);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ArticleContent.Article item);
    }
}
