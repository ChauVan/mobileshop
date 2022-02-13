package com.example.mobileshop.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileshop.R;
import com.example.mobileshop.adapter.ChatAdapter;
import com.example.mobileshop.adapter.NotifyAdapter;

public class ChatFragment extends Fragment {

    private RecyclerView rclFragChat;
    private ChatAdapter chatAdapter;
    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        rclFragChat = view.findViewById(R.id.rclFragChat);
        rclFragChat.setHasFixedSize(true);
        chatAdapter = new ChatAdapter();
        rclFragChat.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rclFragChat.setAdapter(chatAdapter);
        return view;
    }


}