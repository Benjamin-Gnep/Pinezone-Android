package com.example.pinezone.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.pinezone.ActivityCollector;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.article.ArticleListFragment;
import com.example.pinezone.config.ArticleConstant;
import com.example.pinezone.squirrel.BagActivity;
import com.example.pinezone.squirrel.FoodActivity;
import com.example.pinezone.squirrel.PetInfoActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private long exitTime = 0;

    private HomeViewModel homeViewModel;



    private Button canteenButton;
    private Button playButton;
    private Button storeButton;
    private Button studyButton;
    private Button gymButton;
    private Button petInfoButton;
    private Button bagButton;
    private Button foodButton;
    private TextView canteenButtonTextView;
    private TextView playButtonTextView;
    private TextView storeButtonTextView;
    private TextView studyButtonTextView;
    private TextView gymButtonTextView;
    private TextView petInfoButtonTextView;
    private TextView bagButtonTextView;
    private TextView foodButtonTextView;


    private ImageButton achievementButton;
    private ImageButton searchButton;
    private TextView homeText;
    private TextView selectionText;
    private ImageView homePet;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;



    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if((System.currentTimeMillis()-exitTime) > 2000){
                        Toast.makeText(getContext(), "再一次返回退出程序", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        ActivityCollector.finishAll();
                        System.exit(0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        setPet();
        return root;
    }

    private void setPet() {
        Date time = new Date(System.currentTimeMillis());
        Log.e("TAG", dateToString(time) );
        if(pref.getString("date",null) == null){
            editor.putString("date",dateToString(time));
            editor.putInt("mode",0);
            editor.apply();
        }
        String timeIndex;
        timeIndex = pref.getString("date",null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date timeStore = format.parse(timeIndex);
            Log.e("TAG", dateToString(timeStore) );
            if(time.after(timeStore) || timeStore.equals(time)){
                Random rand = new Random();
                chooseMode(rand.nextInt(4));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void chooseMode(int nextInt) {
        Random rand = new Random();
        Date newDate;
        int i;
        switch (nextInt){
            case 0:
                editor.putInt("mode",0);
                homePet.setImageResource(R.drawable.home_pet_leave);
                //18000000 7200000
                i = rand.nextInt(18000000);
                while(i < 7200000){
                    i = rand.nextInt(18000000);
                }
                newDate = new Date(System.currentTimeMillis() + i);
                editor.putString("date",dateToString(newDate));
                editor.apply();
                homeText.setText("松鼠出去旅游了还没回家，再等等吧");
                break;
            case 1:
                editor.putInt("mode",1);
                homePet.setImageResource(R.drawable.home_pet_look);
                //18000000 7200000
                i = rand.nextInt(30000);//30s
                while(i < 20000){//20s
                    i = rand.nextInt(30000);
                }
                newDate = new Date(System.currentTimeMillis() + i);
                editor.putString("date",dateToString(newDate));
                editor.apply();
                homeText.setText("欸，你好像挺好看的");
                break;
            case 2:
                editor.putInt("mode",2);
                homePet.setImageResource(R.drawable.home_pet_sleep);
                i = rand.nextInt(7200000);
                while(i < 3600000){
                    i = rand.nextInt(7200000);
                }
                newDate = new Date(System.currentTimeMillis() + i);
                editor.putString("date",dateToString(newDate));
                editor.apply();
                homeText.setText("好困呐，我先睡一会哇");
                break;
            case 3:
                editor.putInt("mode",3);
                homePet.setImageResource(R.drawable.home_pet_write);
                i = rand.nextInt(7200000);
                while(i < 3600000){
                    i = rand.nextInt(7200000);
                }
                newDate = new Date(System.currentTimeMillis() + i);
                editor.putString("date",dateToString(newDate));
                editor.apply();
                homeText.setText("昨天碰到了小青蛙，我要给他写封信");
                break;
        }
        editor.apply();
    }

    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);
        return ctime;
     }

    private void initView(View root) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        canteenButton = root.findViewById(R.id.home_canteen);
        storeButton = root.findViewById(R.id.home_store);
        playButton = root.findViewById(R.id.home_play);
        studyButton = root.findViewById(R.id.home_study);
        gymButton = root.findViewById(R.id.home_gym);
        homeText = root.findViewById(R.id.text_home);
        selectionText = root.findViewById(R.id.text_selection);
        homePet = root.findViewById(R.id.home_pet);
        achievementButton = root.findViewById(R.id.home_achievement);
        searchButton = root.findViewById(R.id.home_search);
        petInfoButton = root.findViewById(R.id.home_pet_info);
        foodButton = root.findViewById(R.id.home_food);
        bagButton = root.findViewById(R.id.home_bag);

        canteenButtonTextView = root.findViewById(R.id.text_canteen);
        storeButtonTextView = root.findViewById(R.id.text_store);
        playButtonTextView = root.findViewById(R.id.text_play);
        studyButtonTextView = root.findViewById(R.id.text_study);
        gymButtonTextView = root.findViewById(R.id.text_gym);
        petInfoButtonTextView = root.findViewById(R.id.text_pet_info);
        foodButtonTextView = root.findViewById(R.id.text_food);
        bagButtonTextView = root.findViewById(R.id.text_bag);

        selectionText.setText("> 今天打算去哪里逛逛呢 <");

        pref = getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = pref.edit();
        canteenButton.setOnClickListener(this);
        storeButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        studyButton.setOnClickListener(this);
        gymButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        achievementButton.setOnClickListener(this);
        homePet.setOnClickListener(this);
        petInfoButton.setOnClickListener(this);
        bagButton.setOnClickListener(this);
        foodButton.setOnClickListener(this);
        petInfoButton.setVisibility(View.INVISIBLE);
        bagButton.setVisibility(View.INVISIBLE);
        foodButton.setVisibility(View.INVISIBLE);
        petInfoButtonTextView.setVisibility(View.INVISIBLE);
        foodButtonTextView.setVisibility(View.INVISIBLE);
        bagButtonTextView.setVisibility(View.INVISIBLE);

        try{
            int mode = pref.getInt("mode",0);
            switch (mode){
                case 0:
                    homePet.setImageResource(R.drawable.home_pet_leave);
                    homeText.setText("松鼠出去旅游了还没回家，再等等吧");
                    break;
                case 1:
                    homePet.setImageResource(R.drawable.home_pet_look);
                    homeText.setText("欸，你好像挺好看的");
                    break;
                case 2:
                    homePet.setImageResource(R.drawable.home_pet_sleep);
                    homeText.setText("好困呐，我先睡一会哇");
                    break;
                case 3:
                    homePet.setImageResource(R.drawable.home_pet_write);
                    homeText.setText("昨天碰到了小青蛙，我要给他写封信");
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        final MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        ArticleListFragment articleListFragment;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.home_canteen:
                articleListFragment = ArticleListFragment.newInstance(ArticleConstant.TYPE_CANTEEN);
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment,articleListFragment);
                transaction.commit();
                break;
            case R.id.home_store:
                articleListFragment = ArticleListFragment.newInstance(ArticleConstant.TYPE_STORE);
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment,articleListFragment);
                transaction.commit();
                break;
            case R.id.home_play:
                articleListFragment = ArticleListFragment.newInstance(ArticleConstant.TYPE_PLAY);
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment,articleListFragment);
                transaction.commit();
                break;
            case R.id.home_study:
                articleListFragment = ArticleListFragment.newInstance(ArticleConstant.TYPE_STUDY);
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment,articleListFragment);
                transaction.commit();
                break;
            case R.id.home_gym:
                articleListFragment = ArticleListFragment.newInstance(ArticleConstant.TYPE_GYM);
                transaction.addToBackStack(null);
                transaction.replace(R.id.nav_host_fragment,articleListFragment);
                transaction.commit();
                break;
            case R.id.home_search:
                Toast.makeText(getContext(),"搜索功能暂未开放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_achievement:
                Toast.makeText(getContext(),"成就功能暂未开放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_pet:
                if(!homePet.isSelected()){
                    homePet.setSelected(true);
                    canteenButton.setVisibility(View.INVISIBLE);
                    storeButton.setVisibility(View.INVISIBLE);
                    playButton.setVisibility(View.INVISIBLE);
                    studyButton.setVisibility(View.INVISIBLE);
                    gymButton.setVisibility(View.INVISIBLE);
                    petInfoButton.setVisibility(View.VISIBLE);
                    bagButton.setVisibility(View.VISIBLE);
                    foodButton.setVisibility(View.VISIBLE);
                    canteenButtonTextView.setVisibility(View.INVISIBLE);
                    storeButtonTextView.setVisibility(View.INVISIBLE);
                    playButtonTextView.setVisibility(View.INVISIBLE);
                    studyButtonTextView.setVisibility(View.INVISIBLE);
                    gymButtonTextView.setVisibility(View.INVISIBLE);
                    petInfoButtonTextView.setVisibility(View.VISIBLE);
                    bagButtonTextView.setVisibility(View.VISIBLE);
                    foodButtonTextView.setVisibility(View.VISIBLE);
                    selectionText.setText("> 给你的松鼠准备一下行李吧 <");
                }else {
                    homePet.setSelected(false);
                    canteenButton.setVisibility(View.VISIBLE);
                    storeButton.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.VISIBLE);
                    studyButton.setVisibility(View.VISIBLE);
                    gymButton.setVisibility(View.VISIBLE);
                    petInfoButton.setVisibility(View.INVISIBLE);
                    bagButton.setVisibility(View.INVISIBLE);
                    foodButton.setVisibility(View.INVISIBLE);
                    canteenButtonTextView.setVisibility(View.VISIBLE);
                    storeButtonTextView.setVisibility(View.VISIBLE);
                    playButtonTextView.setVisibility(View.VISIBLE);
                    studyButtonTextView.setVisibility(View.VISIBLE);
                    gymButtonTextView.setVisibility(View.VISIBLE);
                    petInfoButtonTextView.setVisibility(View.INVISIBLE);
                    bagButtonTextView.setVisibility(View.INVISIBLE);
                    foodButtonTextView.setVisibility(View.INVISIBLE);
                    selectionText.setText("> 今天打算去哪里逛逛呢 <");
                }
                break;
            case R.id.home_food:
                Intent intent = new Intent(getContext(), FoodActivity.class);
                startActivity(intent);
                break;
            case R.id.home_bag:
                intent = new Intent(getContext(), BagActivity.class);
                startActivity(intent);
                break;
            case R.id.home_pet_info:
                intent = new Intent(getContext(), PetInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
