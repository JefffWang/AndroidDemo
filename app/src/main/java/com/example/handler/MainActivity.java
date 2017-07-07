package com.example.handler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void toUiThreadHandlerActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainThreadHandlerActivity.class);
        startActivity(intent);
    }


    public void toOtherThreadHandlerActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(this, NormalThreadHandlerActivity.class);
        startActivity(intent);
    }
}
