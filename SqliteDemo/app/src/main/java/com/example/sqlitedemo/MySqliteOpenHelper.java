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

    public static final String TAG = MySqliteOpenHelper.class.getName();
    private Context context;

    public MySqliteOpenHelper(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constant.CREATE_TABLE_SQL_V1);

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            sqLiteDatabase.execSQL("ALTER TABLE " + Constant.TABLE_NAME + " ADD COLUMN age INTEGER DEFAULT 20");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
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

    public List<Student> queryAll2() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME, null, null, null, null, null, null, null);
        List<Student> students = new ArrayList<>();
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(Constant.COLUMN_NAME);
            int idIndex = cursor.getColumnIndex(Constant.COLUMN_ID);
            long id = cursor.getLong(idIndex);
            String name = cursor.getString(nameIndex);
            Student student = new Student();
            student.setId(id);
            student.setName(name);

            if (db.getVersion() == 2) {
                int ageIndex = cursor.getColumnIndex(Constant.COLUMN_AGE);
                int age = cursor.getInt(ageIndex);
                student.setAge(age);
            }

            students.add(student);
        }
        db.close();
        return students;
    }

    public List<Student> queryByName(String query) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME, null, Constant.COLUMN_NAME + " LIKE '%"+query+"%'", null, null, null, null);
        List<Student> students = new ArrayList<>();
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(Constant.COLUMN_NAME);
            int idIndex = cursor.getColumnIndex(Constant.COLUMN_ID);
            long id = cursor.getLong(idIndex);
            String name = cursor.getString(nameIndex);
            Student student = new Student();
            student.setId(id);
            student.setName(name);

            if (db.getVersion() == 2) {
                int ageIndex = cursor.getColumnIndex(Constant.COLUMN_AGE);
                int age = cursor.getInt(ageIndex);
                student.setAge(age);
            }

            students.add(student);
        }
        db.close();
        return students;
    }
    
    public void addRow(SQLiteDatabase db, int suffix) {
        ContentValues student = new ContentValues();
        student.put(Constant.COLUMN_NAME, Constant.NAME_PREFIX + suffix);
        db.insert(Constant.TABLE_NAME, null, student);
    }

    public void insert(int suffix) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_NAME, Constant.NAME_PREFIX + suffix);
        db.insert(Constant.TABLE_NAME, null, values);
        db.close();
    }

    public void update(Student student) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(Constant.TABLE_NAME, null, Constant.COLUMN_ID + "=?", new String[]{student.id + ""}, null, null, null);
        if (cursor == null || !cursor.moveToNext()) {
            Log.d(TAG, "没有找到要更新的记录");
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return ;
        }

        long id = cursor.getLong(cursor.getColumnIndex(Constant.COLUMN_ID));
        ContentValues values = new ContentValues();
        values.put(Constant.COLUMN_ID, student.id);
        values.put(Constant.COLUMN_NAME, student.name);
        if (db.getVersion() > 1) {
            values.put(Constant.COLUMN_AGE, student.age);
        }
        db.update(Constant.TABLE_NAME, values, Constant.COLUMN_ID+"=?", new String[]{id+""});
        db.close();
    }


    public void delete(Student student) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constant.TABLE_NAME, Constant.COLUMN_ID + "=?" , new String[]{student.id+""});
    }


}