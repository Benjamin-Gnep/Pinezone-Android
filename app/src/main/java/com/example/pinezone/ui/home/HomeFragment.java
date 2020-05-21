package com.example.pinezone.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class HomeFragment extends Fragment implements View.OnClickListener {
    private long exitTime = 0;

    private HomeViewModel homeViewModel;



    private Button canteenButton;
    private Button playButton;
    private Button storeButton;
    private Button studyButton;
    private Button gymButton;
    private TextView homeText;
    private TextView selectionText;
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
        return root;
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
        homeViewModel.getHomeText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                homeText.setText(s);
            }
        });
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
        }
    }
}
