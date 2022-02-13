package com.example.mobileshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class IntroductoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_introductory);


        Thread td = new Thread(){

                public void run(){
                    try {
//                        sleep(5600);
                        sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        Intent intent = new Intent( IntroductoryActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

        };td.start();
    }
}