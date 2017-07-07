package com.example.handler;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;


public class MainThreadHandlerActivity extends AppCompatActivity {

    private AppCompatTextView countTv;
    private AppCompatTextView descriptionTv;

    private AppCompatButton countStopBtn;

    private boolean stopCount = false;

    private Handler handler;

    int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        initViews();

        handler = new Handler();

        startCountThread();
    }



    private void initViews() {

        countTv = (AppCompatTextView) findViewById(R.id.count);
        descriptionTv = (AppCompatTextView) findViewById(R.id.description);
        countStopBtn = (AppCompatButton) findViewById(R.id.notify_stop);

        descriptionTv.setText(R.string.ui_thread_activity_description);

        countStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCount = true;
            }
        });
    }


    private void startCountThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stopCount) {
                    count++;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            countTv.setText("" + count);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCount = true;
    }
}
