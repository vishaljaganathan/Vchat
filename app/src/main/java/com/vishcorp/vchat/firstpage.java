package com.vishcorp.vchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class firstpage extends AppCompatActivity {
    private static int first_TIMER=3000;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R. layout .activity_firstpage);

        getWindow(). setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) ;
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {

                Intent intent=new Intent(firstpage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        },first_TIMER);}}