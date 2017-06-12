package com.example.sqlitedemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private MySqliteOpenHelper sqliteOpenHelper;

    private MyAdapter adapter;

    private AppCompatButton addBtn;
    private AppCompatButton upgradeBtn;

    private AppCompatEditText queryEt;
    private AppCompatButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        SharedPreferences sp = getSharedPreferences(Constant.PREF_NAME, MODE_PRIVATE);
        int dbVersion = sp.getInt(Constant.VERSION, 1);

        sqliteOpenHelper = new MySqliteOpenHelper(this, Constant.DB_NAME, dbVersion);
        List<Student> students = sqliteOpenHelper.queryAll2();
        adapter = new MyAdapter(MainActivity.this, students);
        listView.setAdapter(adapter);

        addBtn = (AppCompatButton) findViewById(R.id.add);
        upgradeBtn = (AppCompatButton) findViewById(R.id.upgrade);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
                int suffix = sp.getInt(Constant.MAX_ID, 4);
                sqliteOpenHelper.insert(++suffix);
                sp.edit().putInt(Constant.MAX_ID, suffix).commit();
                List<Student> students = sqliteOpenHelper.queryAll2();
                adapter = new MyAdapter(MainActivity.this, students);
                listView.setAdapter(adapter);
            }
        });

        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(Constant.PREF_NAME, MODE_PRIVATE);
                int dbVersion = sp.getInt(Constant.VERSION, 1);
                if (dbVersion == 2) {
                    Toast.makeText(MainActivity.this, "已经是最新版本了", Toast.LENGTH_SHORT).show();
                    return;
                }
                sqliteOpenHelper = new MySqliteOpenHelper(MainActivity.this, Constant.DB_NAME, 2);
                sp.edit().putInt(Constant.VERSION, 2).commit();
                // 要调用打开数据库，才会真正升级
                SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
                db.close();

                List<Student> students = sqliteOpenHelper.queryAll2();
                adapter = new MyAdapter(MainActivity.this, students);
                listView.setAdapter(adapter);
            }
        });

        queryEt = (AppCompatEditText) findViewById(R.id.edit_query);
        queryEt.addTextChangedListener(textWatcher);

        searchBtn = (AppCompatButton) findViewById(R.id.btn_query);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = queryEt.getText().toString();
                if (TextUtils.isEmpty(query)) {
                    return;
                }
                upgradeBtn.setVisibility(View.GONE);
                addBtn.setVisibility(View.GONE);

                List<Student> students = sqliteOpenHelper.queryByName(query);
                adapter = new MyAdapter(MainActivity.this, students);
                listView.setAdapter(adapter);
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(charSequence)) {
                List<Student> students = sqliteOpenHelper.queryAll2();
                adapter = new MyAdapter(MainActivity.this, students);
                listView.setAdapter(adapter);
                upgradeBtn.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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
                view = new ItemLayout(context);
            }
            final AppCompatTextView nameTv = (AppCompatTextView) view.findViewById(R.id.name);
            final AppCompatTextView ageTv = (AppCompatTextView) view.findViewById(R.id.age);
            final AppCompatTextView modifyTv = (AppCompatTextView) view.findViewById(R.id.modify);
            AppCompatTextView delTv = (AppCompatTextView) view.findViewById(R.id.delete);

            final Student student = students.get(pos);
            nameTv.setText(student.name);
            if (student.getAge() != 0) {
                ageTv.setVisibility(View.VISIBLE);
                ageTv.setText(student.getAge()+"");
            } else {
                ageTv.setVisibility(View.GONE);
            }

            modifyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModifyDialog dialog = new ModifyDialog(context, student, ageTv.getVisibility() == View.VISIBLE);
                    dialog.setOnUpdateListener(new ModifyDialog.OnUpdateListener() {
                        @Override
                        public void onUpdate() {
                            List<Student> students = sqliteOpenHelper.queryAll2();
                            adapter = new MyAdapter(MainActivity.this, students);
                            listView.setAdapter(adapter);
                        }
                    });
                    dialog.show();
                }
            });

            delTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sqliteOpenHelper.delete(student);
                    List<Student> students = sqliteOpenHelper.queryAll2();
                    adapter = new MyAdapter(MainActivity.this, students);
                    listView.setAdapter(adapter);
                }
            });

            return view;
        }
    }
}
