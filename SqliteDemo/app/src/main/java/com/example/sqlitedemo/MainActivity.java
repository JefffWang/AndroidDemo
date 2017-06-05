package com.example.sqlitedemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private MySqliteOpenHelper sqliteOpenHelper;
    private SQLiteDatabase db;

    private MyAdapter adapter;

    private AppCompatButton addBtn;
    private AppCompatButton upgradeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        sqliteOpenHelper = new MySqliteOpenHelper(this, "student", 2);
        sqliteOpenHelper.setOnDbOpenListener(new MySqliteOpenHelper.OnDbOpenListener() {
            @Override
            public void onOpen(SQLiteDatabase db) {
                List<Student> students = sqliteOpenHelper.queryAll(db);
                adapter = new MyAdapter(MainActivity.this, students);
                listView.setAdapter(adapter);
            }
        });
        db = sqliteOpenHelper.getWritableDatabase();

        addBtn = (AppCompatButton) findViewById(R.id.add);
        upgradeBtn = (AppCompatButton) findViewById(R.id.upgrade);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
                int suffix = sp.getInt(Constant.MAX_ID, 4);
                sqliteOpenHelper.addRow(db, ++suffix);
                sp.edit().putInt(Constant.MAX_ID, suffix).commit();
                List<Student> students = sqliteOpenHelper.queryAll(db);
                adapter = new MyAdapter(MainActivity.this, students);
                listView.setAdapter(adapter);
            }
        });

        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        List<Student> students;
        public MyAdapter(Context context, List<Student> students) {
            this.context = context;
            this.students = students;
        }

        @Override
        public int getCount() {
            if (students == null || students.isEmpty()) {
                return 0;
            }
            return students.size();
        }

        @Override
        public Object getItem(int i) {
            return students.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item, null);
            }
            final AppCompatEditText nameEt = (AppCompatEditText) view.findViewById(R.id.name);
            final AppCompatEditText ageEt = (AppCompatEditText) view.findViewById(R.id.age);
            final AppCompatTextView modifyTv = (AppCompatTextView) view.findViewById(R.id.modify);
            AppCompatTextView delTv = (AppCompatTextView) view.findViewById(R.id.delete);

            Student student = students.get(pos);
            nameEt.setText(student.name);
            if (student.getAge() != 0) {
                ageEt.setVisibility(View.VISIBLE);
                ageEt.setText(student.getAge());
            } else {
                ageEt.setVisibility(View.GONE);
            }

            nameEt.setEnabled(false);
            ageEt.setEnabled(false);
            modifyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (modifyTv.getText().equals(getString(R.string.modify))) {
                        modifyTv.setText(R.string.ok);
                        nameEt.setEnabled(true);
                        ageEt.setEnabled(true);
                    } else {
                        modifyTv.setText(R.string.modify);
                        nameEt.setEnabled(false);
                        ageEt.setEnabled(false);
                    }
                }
            });

            delTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return view;
        }
    }
}
