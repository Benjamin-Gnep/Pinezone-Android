package com.example.pinezone.config;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pinezone.R;

public class ItemGroupMessage extends FrameLayout implements View.OnClickListener {

    private ConstraintLayout itemGroupLayout; //组合控件的布局
    private TextView titleTv; //标题
    private ImageView jtRightIv; //向右的箭头
    private ImageView iconIv;//icon
    private TextView messageNum;//消息数量


    public ItemGroupMessage(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public ItemGroupMessage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public ItemGroupMessage(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context,attrs);
    }

    //初始化View
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_message_layout, null);
        itemGroupLayout = (ConstraintLayout) view.findViewById(R.id.item_group_layout);
        titleTv = (TextView) view.findViewById(R.id.title_tv);
        jtRightIv = (ImageView) view.findViewById(R.id.jt_right_iv);
        iconIv = view.findViewById(R.id.icon_tv);
        messageNum = view.findViewById(R.id.message_iv);
        addView(view); //把自定义的这个组合控件的布局加入到当前FramLayout
    }
    /**
     * 初始化相关属性，引入相关属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        //标题的默认字体颜色
        int defaultTitleColor = context.getResources().getColor(R.color.gray);
        //输入框的默认字体颜色
        int defaultEdtColor = context.getResources().getColor(R.color.black);
        //输入框的默认的提示内容的字体颜色
        int defaultHintColor = context.getResources().getColor(R.color.middleGray);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemGroupMessage);
        String title = typedArray.getString(R.styleable.ItemGroupMessage_message_title);
        float paddingLeft = typedArray.getDimension(R.styleable.ItemGroupMessage_message_paddingLeft, 15);
        float paddingRight = typedArray.getDimension(R.styleable.ItemGroupMessage_message_paddingRight, 15);
        float paddingTop = typedArray.getDimension(R.styleable.ItemGroupMessage_message_paddingTop, 5);
        float paddingBottom = typedArray.getDimension(R.styleable.ItemGroupMessage_message_paddingTop, 5);
        float titleSize = typedArray.getDimension(R.styleable.ItemGroupMessage_message_title_size, 15);
        int titleColor = typedArray.getColor(R.styleable.ItemGroupMessage_message_title_color, defaultTitleColor);
        int iconId = typedArray.getResourceId(R.styleable.ItemGroupMessage_message_icon_id, 0);
        int messageCount = typedArray.getInteger(R.styleable.ItemGroupMessage_message_count, 0);
        //向右的箭头图标是否可见，默认可见
        boolean showJtIcon = typedArray.getBoolean(R.styleable.ItemGroup_jt_visible, true);
        boolean showJMessageIcon = typedArray.getBoolean(R.styleable.ItemGroupMessage_message_visible, true);
        typedArray.recycle();

        //设置数据
        //设置item的内边距
        itemGroupLayout.setPadding((int) paddingLeft, (int) paddingTop, (int) paddingRight, (int) paddingBottom);
        titleTv.setText(title);
        titleTv.setTextSize(titleSize);
        titleTv.setTextColor(titleColor);
        iconIv.setImageResource(iconId);
        messageNum.setText(String.valueOf(messageCount));
        messageNum.setVisibility(showJMessageIcon ? View.VISIBLE : View.GONE);  //设置向右的箭头图标是否可见
        jtRightIv.setVisibility(showJtIcon ? View.VISIBLE : View.GONE);  //设置向右的箭头图标是否可见
    }

    @Override
    public void onClick(View v) {

    }
}