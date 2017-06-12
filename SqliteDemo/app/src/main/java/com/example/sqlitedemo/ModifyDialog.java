package com.example.sqlitedemo;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wangjinfa on 2017/6/10.
 */

public class ModifyDialog extends AppCompatDialog{

    private boolean containAge = false;

    private AppCompatEditText nameEt;
    private AppCompatEditText ageEt;

    private Student student;

    private OnUpdateListener onUpdateListener;

    public ModifyDialog(Context context, Student student, boolean containAge) {
        this(context);
        this.containAge = containAge;
        this.student = student;
        init();
    }

    public ModifyDialog(Context context) {
        super(context);
    }

    private void init() {
        setTitle(R.string.modify);
        setContentView(R.layout. layout_modify);
        nameEt = (AppCompatEditText) findViewById(R.id.et_name);
        nameEt.setText(student.name);

        ageEt = (AppCompatEditText) findViewById(R.id.et_age);
        final LinearLayout ageContainer = (LinearLayout) findViewById(R.id.et_age_container);
        if (containAge) {
            ageContainer.setVisibility(View.VISIBLE);
            ageEt.setText(student.age + "");
        } else {
            ageContainer.setVisibility(View.GONE);
        }

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student.name = nameEt.getText().toString();
                if (containAge) {
                    student.age = Integer.parseInt(ageEt.getText().toString());
                }
                SharedPreferences sp = getContext().getSharedPreferences(Constant.PREF_NAME, MODE_PRIVATE);
                int dbVersion = sp.getInt(Constant.VERSION, 1);
                new MySqliteOpenHelper(getContext(), Constant.DB_NAME, dbVersion).update(student);
                if (onUpdateListener != null) {
                    onUpdateListener.onUpdate();
                }
                dismiss();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }



    interface OnUpdateListener {
        public void onUpdate();
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }
}