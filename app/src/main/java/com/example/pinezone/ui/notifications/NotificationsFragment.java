package com.example.pinezone.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pinezone.R;

public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";
    private NotificationsViewModel notificationsViewModel;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ConstraintLayout actionBarView = view.findViewById(R.id.action_bar);
        if(actionBarView == null){
            Log.e(TAG, "no exist");
        }
        TextView textView = (TextView) actionBarView.findViewById(R.id.nobutton_bar_title);
        textView.setText("我的消息");
        return view;
    }


}
