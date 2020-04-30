package com.example.pinezone.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.pinezone.ActivityCollector;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.ui.login.SplashActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    private Button logoutButton;
    private TextView homeText;
    private TextView selectionText;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        logoutButton = root.findViewById(R.id.logout);
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

        final MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        pref = activity.getSharedPreferences();
        editor = pref.edit();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = activity.getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                ArticleItemFragment articleItemFragment = new ArticleItemFragment();
//                transaction.replace(R.id.nav_host_fragment,articleItemFragment);
//                transaction.commit();
                editor.clear();
                editor.apply();
                ActivityCollector.finishAll();
            }
        });
    }
}
