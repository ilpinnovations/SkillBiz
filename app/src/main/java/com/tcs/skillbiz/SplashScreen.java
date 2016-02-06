package com.tcs.skillbiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends Activity {

    public static boolean firstTime=false;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView textView = (TextView)findViewById(R.id.splashText);

        Typeface type = Typeface.createFromAsset(getAssets(), "romeo.ttf");
        textView.setTypeface(type);

        Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        textView.startAnimation(animationFadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        },3000);
    }



}
