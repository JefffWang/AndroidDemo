package com.example.sqlitedemo;

/**
 * Created by wangjinfa on 2017/6/3.
 */

public class Student {
    long id;
    String name;
    int age;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
