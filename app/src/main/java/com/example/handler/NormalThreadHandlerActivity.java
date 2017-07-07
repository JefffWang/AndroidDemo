package com.example.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;


public class NormalThreadHandlerActivity extends AppCompatActivity {

    private static final String TAG = NormalThreadHandlerActivity.class.getSimpleName();

    private AppCompatButton printLogBtn;

    private NormalThread normalThread;
    private AppCompatTextView descriptionTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        initViews();

        normalThread = new NormalThread();
        normalThread.start();
    }


    private void initViews() {
        printLogBtn = (AppCompatButton) findViewById(R.id.notify_stop);
        printLogBtn.setText(R.string.other_thread_handler);

        descriptionTv = (AppCompatTextView) findViewById(R.id.description);
        descriptionTv.setText(R.string.normal_thread_activity_description);

        printLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (normalThread.handler != null) {
                    normalThread.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "log printed from normal thread");
                        }
                    });
                }
            }
        });
    }

    private static class NormalThread extends Thread {

        public Handler handler;

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            handler = new Handler();
            Looper.loop();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // quit looper
        normalThread.handler.getLooper().quitSafely();
    }
}
