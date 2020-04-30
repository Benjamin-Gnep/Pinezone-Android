package com.example.pinezone.ui.star;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pinezone.R;

public class StarFragment extends Fragment {

    private StarViewModel starViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        starViewModel =
                ViewModelProviders.of(this).get(StarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_star, container, false);
        final TextView textView = root.findViewById(R.id.text_star);
        starViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
