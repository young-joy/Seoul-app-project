package edu.skku.jonadan.hangangmongttang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SplashTime = 2000;    // change to 0 if skipping is necessary.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        animation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity(new Intent(getApplication(), MainActivity.class));
                SplashActivity.this.finish();
            }
        }, SplashTime);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    private void animation() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        ImageView img= (ImageView) findViewById(R.id.splash_logo);
        img.setAnimation(anim);
    }

}
