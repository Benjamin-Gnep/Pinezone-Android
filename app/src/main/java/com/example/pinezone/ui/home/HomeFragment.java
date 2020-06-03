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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pinezone.ActivityCollector;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.article.ArticleListFragment;
import com.example.pinezone.config.ArticleConstant;

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
                homeText.setText("松鼠好像还没回家，再等等吧");
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
                homeText.setText("你好像挺好看的");
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
                homeText.setText("好困呐，我先睡一会");
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
                homeText.setText("我要给我的伙伴写封信");
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
        homeViewModel.getSelectionText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                selectionText.setText(s);
            }
        });

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

        try{
            int mode = pref.getInt("mode",0);
            switch (mode){
                case 0:
                    homePet.setImageResource(R.drawable.home_pet_leave);
                    homeText.setText("松鼠好像还没回家，再等等吧");
                    break;
                case 1:
                    homePet.setImageResource(R.drawable.home_pet_look);
                    homeText.setText("你好像挺好看的");
                    break;
                case 2:
                    homePet.setImageResource(R.drawable.home_pet_sleep);
                    homeText.setText("好困呐，我先睡一会");
                    break;
                case 3:
                    homePet.setImageResource(R.drawable.home_pet_write);
                    homeText.setText("我要给我的伙伴写封信");
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
                Toast.makeText(getContext(),"松鼠便当功能待开发",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
