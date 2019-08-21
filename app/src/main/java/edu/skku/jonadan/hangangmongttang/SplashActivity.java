package edu.skku.jonadan.hangangmongttang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

    public static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext= this;

        ParkInfoCrawler.start();    // start loading
    }

    public void loaded() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
