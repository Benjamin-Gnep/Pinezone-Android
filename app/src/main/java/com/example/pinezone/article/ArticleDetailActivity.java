package com.example.pinezone.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.pinezone.BasicActivity;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.comment.Comment;
import com.example.pinezone.comment.CommentAdapter;
import com.example.pinezone.config.ArticleConstant;
import com.example.pinezone.config.ArticleService;
import com.example.pinezone.config.SquirrelService;
import com.example.pinezone.squirrel.ListAdapter;
import com.example.pinezone.squirrel.Squirrel;
import com.example.pinezone.ui.publish.PublishActivityCollector;
import com.example.pinezone.user.UserActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleDetailActivity extends BasicActivity {
    private static String ARTICLE_ID = "articleId";
    private static String USER_ID = "userId";

    private List<String> detailImageGroup;
    private BaseQuickAdapter<String, BaseViewHolder> mBaseQuickAdapter;
    private String authorImageSrc;
    private Long articleId;
    private int userId;
    private int authorId;
    private ImageView detailAuthorImage;
    private TextView detailAuthorName;
    private TextView detailAuthorDescribe;
    private TextView detailTitle;
    private TextView detailContent;
    private TextView detailDate;
    private RecyclerView detailImageView;
    private Button detailLikeButton;
    private Button detailStarButton;
    private Button detailCommentButton;
    private Button detailSubscribeButton;
    private Button detailDeleteButton;
    private Button detailUpdateButton;
    private Button addCommentButton;
    private Button reportButton;
    //评论列表逻辑
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private TextView commentNum;
    private NestedScrollView scrollView;
    private RefreshLayout refreshLayout;

    private static int requestPage = 1;
    private static int currentPage = 1;
//    private BannerPagerView detailImageView;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://111.230.173.4:8081/v1/")
            .addConverterFactory(GsonConverterFactory.create()) //添加Gson
            .build();

    public static void StartActivity(Context context, Long articleId, int userId){
        Intent intent = new Intent(context,ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_ID,articleId);
        intent.putExtra(USER_ID,userId);
        context.startActivity(intent);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadArticle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        Intent intent = getIntent();
        if(intent != null){
            articleId = intent.getLongExtra(ARTICLE_ID,0);
            userId = intent.getIntExtra(USER_ID,0);
        }else {
            Toast.makeText(ArticleDetailActivity.this,"获取文章错误"
                    ,Toast.LENGTH_SHORT).show();
        }
        initView();

    }

    private void loadArticle() {
        final ArticleService articleService = retrofit.create(ArticleService.class);
        Call<Article> call = articleService.getArticle(articleId, userId);
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(@NonNull Call<Article> call, @NonNull final Response<Article> response) {
                Log.e("TAG", response.body().toString() );
                final Article article = response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            authorId = article.getUid();
                            detailAuthorName.setText(article.getUsername());
                            detailAuthorDescribe.setText(article.getDescribe());
                            detailTitle.setText(article.getTitle());
                            detailContent.setText(article.getContent());
                            detailDate.setText("编辑于 " + article.getDatetime());
                            Glide.with(ArticleDetailActivity.this)
                                    .load(article.getUimg()).into(detailAuthorImage);
                            authorImageSrc = article.getUimg();
                            for(int i = 0 ; i < article.getAimg().size();i++){
                                detailImageGroup.add(article.getAimg().get(i).path);
                                Log.e("TAG",article.getAimg().get(i).path );
                            }
                            detailLikeButton.setText(String.valueOf(article.getLikenum()));
                            detailStarButton.setText(String.valueOf(article.getStarnum()));
                            detailCommentButton.setText(String.valueOf(article.getCommentnum()));
                            commentNum.setText("共有" + article.getCommentnum() + "条评论");
                            if(article.getIslike() == 1){
                                detailLikeButton.setSelected(true);
                            }
                            if(article.getIsStar() == 1){
                                detailStarButton.setSelected(true);
                            }
                            if(article.getUid() == MainActivity.getUid()){
                                detailSubscribeButton.setVisibility(View.INVISIBLE);
                                detailDeleteButton.setVisibility(View.VISIBLE);
                                detailUpdateButton.setVisibility(View.VISIBLE);
                            }else {
                                detailSubscribeButton.setVisibility(View.VISIBLE);
                                detailDeleteButton.setVisibility(View.INVISIBLE);
                                detailUpdateButton.setVisibility(View.INVISIBLE);
                            }
                            boolean isAuthor = false;
                            if(authorId == MainActivity.getUid()){
                                isAuthor = true;
                            }
                            commentAdapter.couldDelete(isAuthor);
                            setAdapter();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });


            }
            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
    }

    private void initView() {


        detailImageGroup = new ArrayList<>();

        detailAuthorImage = findViewById(R.id.detail_author_image);
        detailAuthorName = findViewById(R.id.detail_author_name);
        detailAuthorDescribe = findViewById(R.id.detail_author_describe);
        detailContent = findViewById(R.id.detail_content);
        detailTitle = findViewById(R.id.detail_title);
        detailDate = findViewById(R.id.detail_date);
        detailCommentButton = findViewById(R.id.detail_comment_button);
        detailLikeButton = findViewById(R.id.detail_like_button);
        detailStarButton = findViewById(R.id.detail_star_button);
        detailUpdateButton = findViewById(R.id.detail_update);
        detailDeleteButton = findViewById(R.id.detail_delete);
        detailSubscribeButton = findViewById(R.id.detail_subscribe);
        addCommentButton = findViewById(R.id.detail_add_comment);
        commentRecyclerView = findViewById(R.id.comment_recycler_view);
        scrollView = findViewById(R.id.detail_scroll_view);
        refreshLayout = findViewById(R.id.detail_refresh);
        commentNum = findViewById(R.id.comment_num_text);
        reportButton = findViewById(R.id.detail_report);

        detailImageView = findViewById(R.id.detail_image_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        detailImageView.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(detailImageView);

        //修改按钮逻辑
        detailUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //关注按钮逻辑
        detailSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ArticleDetailActivity.this,"关注功能暂未开发",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //删除按钮逻辑
        detailDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ArticleDetailActivity.this).
                        setMessage("确定删除这篇文章吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final ArticleService articleService =
                                        retrofit.create(ArticleService.class);
                                Call<ResponseBody> call;
                                call = articleService.deleteArticle(articleId);
                                call.enqueue(new Callback<ResponseBody>(){
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if(response.body() == null){
                                            Log.e("TAG", response.toString() );
                                            Toast.makeText(ArticleDetailActivity.this,"参数错误",
                                                    Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(ArticleDetailActivity.this,"删除成功",
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(ArticleDetailActivity.this,"网络错误",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });

        //点赞图标逻辑
        detailLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailLikeButton.setClickable(false);
                if(!detailLikeButton.isSelected()){
                    detailLikeButton.setSelected(true);
                    detailLikeButton.setText(String.valueOf(
                            Integer.parseInt(detailLikeButton.getText().toString())+1));
                    final ArticleService articleService = retrofit.create(ArticleService.class);
                    RequestBody uidBody = RequestBody.create
                            (MediaType.parse("multipart/form-data"),String.valueOf(MainActivity.getUid()));
                    RequestBody cidBody = RequestBody.create
                            (MediaType.parse("multipart/form-data"),String.valueOf(articleId));
                    Call<ResponseBody> call = articleService.like(uidBody,cidBody);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            if(response.body()!=null){
                                Log.e("点赞回执", response.body().toString() );
                                detailLikeButton.setClickable(true);
                            }else{
                                Toast.makeText(ArticleDetailActivity.this,"参数错误",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call,
                                              @NonNull Throwable t) {
                            Toast.makeText(ArticleDetailActivity.this,"网络错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    detailLikeButton.setSelected(false);
                    final ArticleService articleService = retrofit.create(ArticleService.class);
                    detailLikeButton.setText(String.valueOf(
                            Integer.parseInt(detailLikeButton.getText().toString())-1));

                    Call<ResponseBody> call = articleService.unlike(MainActivity.getUid(),
                            (long) articleId);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.body()!=null){
                                Log.e("点赞回执", response.body().toString() );
                                Squirrel.addPinecone(ArticleDetailActivity.this,2);
                                detailLikeButton.setClickable(true);
                            }else{
                                Toast.makeText(ArticleDetailActivity.this,"参数错误",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ArticleDetailActivity.this,"网络错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //收藏按钮逻辑
        detailStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailStarButton.setClickable(false);
                if(!detailStarButton.isSelected()){
                    detailStarButton.setSelected(true);
                    detailStarButton.setText(String.valueOf(
                            Integer.parseInt(detailStarButton.getText().toString())+1));
                    final ArticleService articleService = retrofit.create(ArticleService.class);
                    RequestBody uidBody = RequestBody.create
                            (MediaType.parse("multipart/form-data"),String.valueOf(MainActivity.getUid()));
                    RequestBody cidBody = RequestBody.create
                            (MediaType.parse("multipart/form-data"),String.valueOf(articleId));
                    Call<ResponseBody> call = articleService.star(uidBody,cidBody);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.body()!=null){
                                Log.e("收藏回执", response.body().toString() );
                                detailStarButton.setClickable(true);
                            }else{
                                Toast.makeText(ArticleDetailActivity.this,"参数错误",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ArticleDetailActivity.this,"网络错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    detailStarButton.setSelected(false);
                    final ArticleService articleService = retrofit.create(ArticleService.class);
                    detailStarButton.setText(String.valueOf(
                            Integer.parseInt(detailStarButton.getText().toString())-1));

                    Call<ResponseBody> call = articleService.unstar(MainActivity.getUid(),
                            (long) articleId);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.body()!=null){
                                Log.e("收藏回执", response.body().toString() );
                                Squirrel.addPinecone(ArticleDetailActivity.this,2);
                                detailStarButton.setClickable(true);
                            }else{
                                Toast.makeText(ArticleDetailActivity.this,"参数错误",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ArticleDetailActivity.this,"网络错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleDetailActivity.this);

                builder.setTitle("请选择举报内容");

                final String items[] = {
                        "发布与主题不相关的内容",
                        "营销或剽窃内容",
                        "淫秽图片或不当言论",
                        "恶意或虚假宣传",
                        "其他原因"};

                final boolean [] checkedItems ={false,false,false,false,false};

                builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });

                //把选中的挑选出来
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        //把选中的条目的数据取出来
                        for (int i = 0; i <checkedItems.length ; i++) {
                            //判断下选中的
                            if(checkedItems[i]){
                                String report = items[i];
                                sb.append(report+"");
                            }
                            //Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_LONG).show();
                            //2.然后把对话框关闭
                            dialog.dismiss();
                        }
                        final ArticleService articleService = retrofit.create(ArticleService.class);
                        RequestBody uidBody = RequestBody.create
                                (MediaType.parse("multipart/form-data"),String.valueOf(MainActivity.getUid()));
                        RequestBody aidBody = RequestBody.create
                                (MediaType.parse("multipart/form-data"),String.valueOf(articleId));
                        RequestBody contentBody = RequestBody.create
                                (MediaType.parse("multipart/form-data"),sb.toString());
                        Call<ResponseBody> call = articleService.report(uidBody,aidBody,contentBody);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.body()!=null){
                                    Log.e("举报回执", response.body().toString() );
                                    Toast.makeText(getApplicationContext(),
                                            "举报成功，请等待管理员审核"
                                            ,Toast.LENGTH_SHORT).show();
                                    detailStarButton.setClickable(true);
                                }else{
                                    Toast.makeText(ArticleDetailActivity.this,"参数错误",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(ArticleDetailActivity.this,"网络错误",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                //一样要show
                builder.show();
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "click");
                showPopupComment();
            }
        });

        detailAuthorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.StartActivity(ArticleDetailActivity.this,authorId,authorImageSrc);
            }
        });
        detailAuthorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.StartActivity(ArticleDetailActivity.this,authorId,authorImageSrc);
            }
        });

        //评论列表部分
        commentAdapter = new CommentAdapter(ArticleDetailActivity.this,
                new ArrayList<Comment>());
        commentAdapter.setAnimationEnable(true);
        commentAdapter.addChildClickViewIds(R.id.comment_delete);
        commentAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter,
                                         @NonNull View view, int position) {
                switch (view.getId()){
                    case R.id.comment_delete:
                        ConstraintLayout layout = (ConstraintLayout) view.getParent();
                        TextView commentId = layout.findViewById(R.id.comment_id);
                        new AlertDialog.Builder(ArticleDetailActivity.this).
                                setMessage("确定删除这条评论吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final ArticleService articleService =
                                                retrofit.create(ArticleService.class);
                                        Call<ResponseBody> call;
                                        call = articleService.deleteComment(
                                                Long.parseLong(commentId.getText().toString()));
                                        call.enqueue(new Callback<ResponseBody>(){
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if(response.body() == null){
                                                    Log.e("TAG", response.toString() );
                                                    Toast.makeText(ArticleDetailActivity.this,"参数错误",
                                                            Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(ArticleDetailActivity.this,"删除成功",
                                                            Toast.LENGTH_SHORT).show();
                                                    currentPage = 1;
                                                    requestPage = 1;
                                                    getCommentList(requestPage);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(ArticleDetailActivity.this,"网络错误",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        break;
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(commentAdapter);
        currentPage = 1;
        requestPage = 1;
        getCommentList(requestPage);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                loadArticle();
                requestPage = 1;
                getCommentList(requestPage);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //判断scrollview是否到达最底端，然后加载下一页
                    View onlyChild = scrollView.getChildAt(0);
                    if (onlyChild.getHeight() <= scrollY + scrollView.getHeight()) {   // 如果满足就是到底部了
                        if(requestPage == currentPage){
                            requestPage++;
                            getCommentList(requestPage);
                        }
                    }
                }
            });
        }
    }


    private void getCommentList(final int page){
        final List<Comment> commentList = new ArrayList<>();

        final ArticleService articleService = retrofit.create(ArticleService.class);

        Call<List<Comment>> call;

        call = articleService.getCommentList(articleId,page,10);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.body() != null && response.body().size() != 0){
                    Log.e("TAG", response.body().toString() );
                    commentList.addAll(response.body());
                    if(page == 1){
                        commentAdapter.refresh(commentList);
                        currentPage = 1;
                    }else{
                        commentAdapter.loadMore(commentList);
                        currentPage++;
                    }
                    commentRecyclerView.requestLayout();
                } else {
                    Log.e("TAG", response.toString() );
                }
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("TAG", t.toString() );
            }
        });
    }

    void setAdapter(){
        mBaseQuickAdapter= new BaseQuickAdapter<String, BaseViewHolder>
                (R.layout.fragment_detail_image_item,detailImageGroup) {
            @Override
            protected void convert(@NotNull BaseViewHolder helper, String s) {
                ImageView imageView =  helper.getView(R.id.img);
                Log.e("TAG", s );
                Glide.with(ArticleDetailActivity.this)
                        .load(s).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        };
        detailImageView.setAdapter(mBaseQuickAdapter);
    }




    //评论部分逻辑
    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private RelativeLayout rl_input_container;
    private InputMethodManager mInputManager;
    @SuppressLint("WrongConstant")
    private void showPopupComment(){
        if (popupView == null){
            //加载评论框的资源文件
            popupView = LayoutInflater.from(ArticleDetailActivity.this)
                    .inflate(R.layout.comment_popupwindow, null);
        }
        if (popupWindow == null){
            popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, false);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        inputComment.requestFocus();
        inputComment.setFocusable(true);
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        btn_submit = (Button) popupView.findViewById(R.id.btn_confirm);
        rl_input_container = (RelativeLayout)popupView.findViewById(R.id.rl_input_container);
        //利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run()
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(inputComment, 0);
            }

        }, 500);
        //popupWindow的常规设置，设置点击外部事件，背景色
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    popupWindow.dismiss();
                }
                return false;
            }
        });
        // 设置弹出窗体需要软键盘，放在setSoftInputMode之前
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow的显示位置，这里应该是显示在底部，即Bottom
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        popupWindow.update();

        //设置监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onDismiss() {
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘
            }
        });
        //外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getWindowToken()!=null){
                    mInputManager.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);//强制隐藏键盘
                }
                popupWindow.dismiss();
            }
        });
        //评论框内的发送按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nInputContentText = inputComment.getText().toString().trim();
                if (nInputContentText == null || "".equals(nInputContentText)) {
//                        showToastMsgShort("请输入评论内容");
                    try{
                        Toast.makeText(ArticleDetailActivity.this
                                ,"请输入评论内容",Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return;
                }
                publishComment(nInputContentText);
                mInputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                popupWindow.dismiss();
            }

            private void publishComment(String nInputContentText) {
                final ArticleService articleService = retrofit.create(ArticleService.class);

                RequestBody uidBody = RequestBody.create
                        (MediaType.parse("multipart/form-data"),String.valueOf(MainActivity.getUid()));
                RequestBody aidBody = RequestBody.create
                        (MediaType.parse("multipart/form-data"),String.valueOf(articleId));
                RequestBody contentBody = RequestBody.create
                        (MediaType.parse("multipart/form-data"),nInputContentText);
                Call<ResponseBody> call = articleService.publishComment(uidBody,aidBody,contentBody);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if(response.body()==null){
                            Toast.makeText(ArticleDetailActivity.this,"参数错误",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ArticleDetailActivity.this,"评论成功",
                                    Toast.LENGTH_SHORT).show();
                            //评论增加松果
                            Squirrel.addPinecone(ArticleDetailActivity.this,5);
                            currentPage = 1;
                            requestPage = 1;
                            getCommentList(requestPage);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ArticleDetailActivity.this,"网络错误",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
