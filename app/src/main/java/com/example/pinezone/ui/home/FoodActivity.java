package com.example.pinezone.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;

import java.util.ArrayList;
import java.util.List;

public class FoodActivity extends BasicActivity {

    private List<ThingsItem> list = new ArrayList<>();

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
    }

    private void initButton() {
        ThingsItem mushroom = new ThingsItem("蘑菇","减少20%体力消耗",R.drawable.ic_achievement);
        list.add(mushroom);
    }
}
