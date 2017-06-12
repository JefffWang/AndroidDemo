package com.example.sqlitedemo;

/**
 * Created by wangjinfa on 2017/6/3.
 */

public class Constant {

    public static final String CREATE_TABLE_SQL_V1 = "create table if not exists student(_id integer primary key," +
            "name text);";
    public static final String CREATE_TABLE_SQL_V2 = "create table if not exists student(_id integer primary key," +
            "name text , age int);";
    public static final String DROP_TABLE_SQL = "drop table if exists student";

    public static final String DB_NAME = "student_db";
//    public static int DB_VERSION = 1;
    public static final String TABLE_NAME = "student";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String NAME_PREFIX = "student";

    public static final String MAX_ID = "max_id";
    public static final String VERSION = "version";
    public static final String PREF_NAME = "db_pref";
}
