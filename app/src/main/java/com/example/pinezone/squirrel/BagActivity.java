package com.example.pinezone.squirrel;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;

import java.util.ArrayList;
import java.util.List;

public class BagActivity extends BasicActivity {

    public static ProgressBar progressBar;
    private List<ThingsItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        initButton();
        RecyclerView recyclerView = findViewById(R.id.bag_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ListAdapter adapter = new ListAdapter(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        ConstraintLayout actionBarView = findViewById(R.id.action_bar);
        TextView textView = (TextView) actionBarView.findViewById(R.id.action_bar_title);
        textView.setText("背包");
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initButton() {
        ThingsItem guibeizhu = new ThingsItem("龟背竹","增加宠物20%成就机率",
                R.drawable.ic_guibeizhu,5,2,1.2);
        list.add(guibeizhu);
        ThingsItem jinqiancao = new ThingsItem("金钱草","增加宠物25%遭遇伙伴机率",
                R.drawable.ic_jinqiancao,8,3,1.25);
        list.add(jinqiancao);
        ThingsItem luhui = new ThingsItem("芦荟","增加宠物40%成就机率",
                R.drawable.ic_luhui,10,2,1.4);
        list.add(luhui);
        ThingsItem wannianqing = new ThingsItem("万年青","增加40%遭遇伙伴机率",
                R.drawable.ic_wannianqing,12,3,1.4);
        list.add(wannianqing);
        ThingsItem xianrenzhang = new ThingsItem("仙人掌","增加50%成就机率",
                R.drawable.ic_xianrenzhang,18,2,1.5);
        list.add(xianrenzhang);
        ThingsItem xianrenqiu = new ThingsItem("仙人球","增加50%遭遇伙伴机率",
                R.drawable.ic_xianrenqiu,18,3,1.5);
        list.add(xianrenqiu);
        ThingsItem ziluolan = new ThingsItem("紫罗兰","增加80%遭遇伙伴机率",
                R.drawable.ic_ziluolan,20,3,1.8);
        list.add(ziluolan);

    }
}
