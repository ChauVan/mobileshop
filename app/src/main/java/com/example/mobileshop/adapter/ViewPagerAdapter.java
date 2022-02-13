package com.example.mobileshop.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mobileshop.LoginActivity;
import com.example.mobileshop.MainActivity;
import com.example.mobileshop.fragment.ChatFragment;
import com.example.mobileshop.fragment.FavoriteFragment;
import com.example.mobileshop.fragment.HomeFragment;
import com.example.mobileshop.fragment.NotificationsFragment;
import com.example.mobileshop.fragment.PersonFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    private SharedPreferences preferences;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoriteFragment();
            case 2:
                return new NotificationsFragment();
            case 3:
                return new ChatFragment();
            case 4:
                return new PersonFragment();
            default:
                return new HomeFragment();

        }

    }

    @Override
    public int getCount() {
        return 5;
    }





}
