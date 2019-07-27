package com.csc4360.beertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        //setting a delay for the splash screen to call the main activity page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashScreen.this.startActivities();
                splashScreen.this.finish();
            }
        },4750);
    }

    //method to open the main activity page
    private void startActivities() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
