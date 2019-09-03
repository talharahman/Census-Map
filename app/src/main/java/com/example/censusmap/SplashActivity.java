package com.example.censusmap;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_DISPLAY_LENGTH = 3000;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       handler = new Handler();

       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent intent = new Intent(SplashActivity.this.getApplicationContext(), MainActivity.class);
               SplashActivity.this.startActivity(intent);
               SplashActivity.this.finish();

           }
       }, SPLASH_DISPLAY_LENGTH);

    }
}
