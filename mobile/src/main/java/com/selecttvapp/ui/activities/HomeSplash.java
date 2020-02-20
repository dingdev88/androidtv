package com.selecttvapp.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.selecttvapp.R;
import com.selecttvapp.common.InternetConnection;

public class HomeSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_splash);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                if (!isDestroyed()) {
                    Intent intent = new Intent(HomeSplash.this, LoadingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.ani_in, R.anim.ani_out);
                    finish();
                }
            }
        }, 1500);

    }
}
