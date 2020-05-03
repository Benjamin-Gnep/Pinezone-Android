package com.example.pinezone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pinezone.article.ArticleListFragment;
import com.example.pinezone.ui.home.HomeFragment;
import com.example.pinezone.ui.mine.MineFragment;
import com.example.pinezone.ui.notifications.NotificationsFragment;
import com.example.pinezone.ui.star.StarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BasicActivity {
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_star, R.id.navigation_notifications,
                R.id.navigation_mine)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId())
                {
                    case  R.id.navigation_home:
                        HomeFragment homeFragment = new HomeFragment();
                        transaction.replace(R.id.nav_host_fragment,homeFragment);
                        transaction.commit();
                        break;

                    case R.id.navigation_star:
                        StarFragment starFragment = new StarFragment();
                        transaction.replace(R.id.nav_host_fragment,starFragment);
                        transaction.commit();
                        break;

                    case R.id.navigation_notifications:
                        NotificationsFragment notificationsFragment = new NotificationsFragment();
                        transaction.replace(R.id.nav_host_fragment,notificationsFragment);
                        transaction.commit();
                        break;

                    case R.id.navigation_mine:
                        MineFragment mineFragment = new MineFragment();
                        transaction.replace(R.id.nav_host_fragment,mineFragment);
                        transaction.commit();
                        break;
                }
                return false;
            }
        });
        ActivityCollector.addActivity(this);
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
        return getSharedPreferences("config",MODE_PRIVATE);
    }
}
