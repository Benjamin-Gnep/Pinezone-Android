package com.example.pinezone.comment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.article.Article;

import java.util.List;


public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    private Context mContext;
    private List<Comment> commentList;
    private boolean couldDelete;

    public CommentAdapter(Context context, List<Comment> data) {
        super(R.layout.fragment_comment_item, data);
        commentList = data;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment comment) {
        helper.setText(R.id.comment_id,String.valueOf(comment.getComid()))
                .setText(R.id.comment_content,comment.getContent())
            .setText(R.id.comment_author_name,comment.getUsername())
            .setText(R.id.comment_date,comment.getDatetime());

        ImageView userImage = helper.findView(R.id.comment_author_image);
        Button commentDelete = helper.findView(R.id.comment_delete);
        if(couldDelete || comment.getUid() == MainActivity.getUid()){
            commentDelete.setVisibility(View.VISIBLE);
        }
        Glide.with((getContext())).load(comment.getUimg()).into(userImage);
    }

    public void refresh(List<Comment> list) {
        commentList.clear();
        notifyDataSetChanged();
        int currentSize = commentList.size();
        int insertSize = list.size();;
        commentList.addAll(list);
        notifyItemRangeInserted(currentSize,insertSize);
    }

    public void loadMore(List<Comment> list) {
        int currentSize = commentList.size();
        int insertSize = list.size();
        commentList.addAll(list);
        notifyItemRangeInserted(currentSize,insertSize);
    }

    public void couldDelete(boolean isAuthor) {
        couldDelete = isAuthor;
    }
}
