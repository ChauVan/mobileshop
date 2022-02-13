package com.example.mobileshop.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mobileshop.CartActivity;
import com.example.mobileshop.LoginActivity;
import com.example.mobileshop.R;
import com.example.mobileshop.adapter.HomeProductAdapter;
import com.example.mobileshop.adapter.NotifyAdapter;

public class NotificationsFragment extends Fragment {

    private RecyclerView fragNotify;
    private NotifyAdapter notifyAdapter;
    private ImageButton imgbtnHomeActivityCart;
    public NotificationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        fragNotify = view.findViewById(R.id.fragNotify);
        imgbtnHomeActivityCart = view.findViewById(R.id.imgbtnHomeActivityCart);

        fragNotify.setHasFixedSize(true);
        notifyAdapter = new NotifyAdapter();
        fragNotify.setLayoutManager(new GridLayoutManager(getContext(), 1));
        fragNotify.setAdapter(notifyAdapter);

        imgbtnHomeActivityCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}