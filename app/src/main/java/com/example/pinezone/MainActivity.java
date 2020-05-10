package com.example.pinezone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.pinezone.ui.home.HomeFragment;
import com.example.pinezone.ui.mine.MineFragment;
import com.example.pinezone.ui.notifications.NotificationsFragment;
import com.example.pinezone.ui.publish.PublishType;
import com.example.pinezone.ui.star.StarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BasicActivity {
    private static int uid;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button publishButton = findViewById(R.id.home_publish_button);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        switch (item.getItemId())
                        {
                            case  R.id.navigation_home:
                                navView.getMenu().getItem(0).setChecked(true);
                                HomeFragment homeFragment = new HomeFragment();
                                transaction.replace(R.id.nav_host_fragment,homeFragment);
                                transaction.commit();
                                break;

                            case R.id.navigation_star:
                                navView.getMenu().getItem(1).setChecked(true);
                                StarFragment starFragment = new StarFragment();
                                transaction.replace(R.id.nav_host_fragment,starFragment);
                                transaction.commit();
                                break;

                            case R.id.navigation_notifications:
                                navView.getMenu().getItem(3).setChecked(true);
                                NotificationsFragment notificationsFragment = new NotificationsFragment();
                                transaction.replace(R.id.nav_host_fragment,notificationsFragment);
                                transaction.commit();
                                break;

                            case R.id.navigation_mine:
                                MineFragment mineFragment = new MineFragment();
                                navView.getMenu().getItem(4).setChecked(true);
                                transaction.replace(R.id.nav_host_fragment,mineFragment);
                                transaction.commit();
                                break;
                        }
                        return false;
                    }
                });
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PublishType.class);
                startActivity(intent);
            }
        });
        ActivityCollector.addActivity(this);
        initUid();
    }

    private void initUid() {
        SharedPreferences sp = getSharedPreferences("setting",MODE_PRIVATE);
        uid = sp.getInt("id",0);
    }

    public static int getUid() {
        return uid;
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//            if((System.currentTimeMillis()-exitTime) > 2000){
//                Toast.makeText(getApplicationContext(), "再一次返回退出程序", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                ActivityCollector.finishAll();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences("setting",MODE_PRIVATE);
    }
}
