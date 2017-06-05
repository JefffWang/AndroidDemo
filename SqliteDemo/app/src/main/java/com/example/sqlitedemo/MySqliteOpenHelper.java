package com.example.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinfa on 2017/6/3.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper{

    private String databaseName;

    private OnDbOpenListener listener;

    private int maxId = 0;

    private Context context;

    public MySqliteOpenHelper(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
        this.databaseName = databaseName;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constant.CREATE_TABLE_SQL_V1);
        Log.e("wjf", "on create");
        addRow(sqLiteDatabase,1);
        addRow(sqLiteDatabase,2);
        addRow(sqLiteDatabase,3);
        addRow(sqLiteDatabase,4);

        SharedPreferences sp = context.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(Constant.MAX_ID, 4).commit();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);


        if (listener != null) {
            listener.onOpen(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public int getVersion(SQLiteDatabase db) {
        return db.getVersion();
    }

    public List<Student> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(Constant.TABLE_NAME, null, null, null, null, null, null, null);
        List<Student> students = new ArrayList<>();
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(Constant.COLUMN_NAME);
            String name = cursor.getString(nameIndex);
            Student student = new Student();
            student.setName(name);
            students.add(student);
        }
        return students;
    }
    
    public void addRow(SQLiteDatabase db, int suffix) {
        ContentValues student = new ContentValues();
        student.put(Constant.COLUMN_NAME, Constant.NAME_PREFIX + suffix);
        db.insert(Constant.TABLE_NAME, null, student);
    }


    interface OnDbOpenListener {
        public void onOpen(SQLiteDatabase db);
    }

    public void setOnDbOpenListener(OnDbOpenListener listener) {
        this.listener = listener;
    }

}
