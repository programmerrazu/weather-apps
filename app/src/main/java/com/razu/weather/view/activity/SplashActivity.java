package com.razu.weather.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.razu.weather.Apps;
import com.razu.weather.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Apps.redirect(SplashActivity.this, MainActivity.class);
    }
}