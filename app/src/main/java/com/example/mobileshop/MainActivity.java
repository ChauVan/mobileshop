package com.example.mobileshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.mobileshop.Object.Login.LoginUser;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Others.GetNewToken;
import com.example.mobileshop.adapter.ViewPagerAdapter;
import com.example.mobileshop.fragment.ChatFragment;
import com.example.mobileshop.fragment.FavoriteFragment;
import com.example.mobileshop.fragment.HomeFragment;
import com.example.mobileshop.fragment.NotificationsFragment;
import com.example.mobileshop.fragment.PersonFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private AHBottomNavigation ahBottomNavigation;
    private AHBottomNavigationViewPager ahBottomNavigationViewPager;
    private ViewPagerAdapter adapter;
    private boolean doubleBackToExitPressedOnce = false;
    private SharedPreferences preferences;
    private String user, pass;
    private LoginUser loginUser;
    private DataToken dataToken;
    private GetNewToken getNewToken;
    private BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = MainActivity.this.getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        user = preferences.getString("user", "");
        long expires = preferences.getLong("expires", 0);
//        pass = preferences.getString("pass", "");

        dataToken = new DataToken(MainActivity.this);

//        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor1 = settings.edit();
//        editor1.putLong("expires", System.currentTimeMillis() -1); //24 * 3600000
//        editor1.apply();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        int code = intent.getIntExtra("code", 0);
        if(expires < System.currentTimeMillis() || user.equals("")){
            if(code != 1) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }
        loadFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            if (item.getItemId() != R.id.navigation_Home) {
                AskLogin();
            }

            if (!user.equals("")) {
                switch (item.getItemId()) {
                    case R.id.navigation_Home:
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_Like:
                        fragment = new FavoriteFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_Notify:
                        fragment = new NotificationsFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_Mess:
                        fragment = new ChatFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_Profile:
                        fragment = new PersonFragment();
                        loadFragment(fragment);
                        return true;
                }
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void AskLogin() {
        if (user.equals("")) {
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            b.setTitle("Xác nhận");
            b.setMessage("Đăng nhập để tiếp tục");
            b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });

            b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    navigation.setSelectedItemId(R.id.navigation_Home);
                }
            });

            AlertDialog al = b.create();
            al.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Trả về lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;


            }
        }, 2000);
    }


}