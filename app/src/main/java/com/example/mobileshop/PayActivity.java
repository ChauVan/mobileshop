package com.example.mobileshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PayActivity extends AppCompatActivity {

    private Button btnPayAgree;
    private ImageButton imgbtnPayActivityLBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        btnPayAgree = findViewById(R.id.btnPayAgree);
        imgbtnPayActivityLBack = findViewById(R.id.imgbtnPayActivityLBack);

        btnPayAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgbtnPayActivityLBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}