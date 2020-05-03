package com.example.pinezone.config;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class LoadMoreOnScrollListener extends RecyclerView.OnScrollListener {
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private int currentPage = 1;

    private static final String TAG = "LoadMoreOnScrollListene";
    /**
     * 已经加载出来的item个数
     */
    private int totalItemCount;

    private int lastVisibleItemPosition;


    public LoadMoreOnScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.staggeredGridLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // item总数
        totalItemCount = staggeredGridLayoutManager.getItemCount();

        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];

        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
        lastVisibleItemPosition = findMax(lastPositions);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        staggeredGridLayoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
        int currentScrollState = newState;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 &&
                currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                (lastVisibleItemPosition) >= totalItemCount - 1)) {
            Log.i(TAG, "onScrollStateChanged: ...");
            try {
                onLoadMore(currentPage);//请求更多数据
                currentPage++;
            } catch (LoadFailException e) {
                e.printStackTrace();
                Toast.makeText(recyclerView.getContext(),"请求错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public abstract void onLoadMore(int currentPage) throws LoadFailException;
}
