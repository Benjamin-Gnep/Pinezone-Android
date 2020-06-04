package com.example.pinezone.squirrel;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends BasicActivity {

    private List<ThingsItem> list = new ArrayList<>();
    public static ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        initButton();
        RecyclerView recyclerView = findViewById(R.id.food_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ListAdapter adapter = new ListAdapter(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        ConstraintLayout actionBarView = findViewById(R.id.action_bar);
        TextView textView = (TextView) actionBarView.findViewById(R.id.action_bar_title);
        textView.setText("便当");
    }

    private void initButton() {
        ThingsItem hazelnut = new ThingsItem("松子","减少5%体力消耗",
                R.drawable.ic_hazelnut,10,1,1.05);
        list.add(hazelnut);
        ThingsItem cashew = new ThingsItem("腰果","减少8%体力消耗",
                R.drawable.ic_cashew,15,1,1.08);
        list.add(cashew);
        ThingsItem cookie = new ThingsItem("曲奇","减少10%体力消耗",
                R.drawable.ic_cookie,20,1,1.1);
        list.add(cookie);
        ThingsItem hawaii = new ThingsItem("夏威夷果","减少15%体力消耗",
                R.drawable.ic_hawaii,25,1,1.15);
        list.add(hawaii);
        ThingsItem mushroom = new ThingsItem("蘑菇","减少20%体力消耗",
                R.drawable.ic_mushroom,30,1,1.2);
        list.add(mushroom);
        ThingsItem seed = new ThingsItem("瓜子","减少30%体力消耗",
                R.drawable.ic_seed,40,1,1.3);
        list.add(seed);
        ThingsItem badam = new ThingsItem("巴旦木","减少35%体力消耗",
                R.drawable.ic_badam,50,1,1.35);
        list.add(badam);

    }
}
