package com.example.pinezone.ui.home;

import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pinezone.R;
import com.example.pinezone.ui.home.ArticleItemFragment.OnListFragmentInteractionListener;
import com.example.pinezone.ui.home.article.ArticleContent;

import java.util.List;


public class MyArticleItemRecyclerViewAdapter extends RecyclerView.Adapter<MyArticleItemRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MyArticleItemRecyclerVi";

    private final List<ArticleContent.Article> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyArticleItemRecyclerViewAdapter(List<ArticleContent.Article> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_article_item, parent, false);
        return new ViewHolder(view,parent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mImage.setImageResource(mValues.get(position).getImageId());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {

                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mImage;
        public ArticleContent.Article mItem;

        public ViewHolder(View view,ViewGroup group) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mImage = (ImageView) view.findViewById(R.id.article_image);
            Resources resources = group.getResources();
            //获取屏幕数据
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            //获取屏幕宽高，单位是像素
            int widthPixels = displayMetrics.widthPixels/2;
            ViewGroup.LayoutParams lp = mImage.getLayoutParams();
            lp.width = widthPixels;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mImage.setLayoutParams(lp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
