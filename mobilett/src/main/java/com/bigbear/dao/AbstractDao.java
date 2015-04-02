package com.bigbear.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bigbear.entity.Student;

/**
 * Created by luanvu on 4/1/15.
 */
public abstract class AbstractDao<T> {
    private static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 1;
    private DBHelper helper;
    private Context context;
    private SQLiteDatabase db;

    protected AbstractDao() {
    }

    public AbstractDao(Context context) {
        this.context=context;
        helper=new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper getHelper() {
        return helper;
    }

    public Context getContext() {
        return context;
    }
    public void open() throws SQLException {
        db=helper.getWritableDatabase();
    }
    public void close(){
        if(db!=null)
            db.close();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public abstract ContentValues toValue(T entity);
    public abstract String getTableName();
    public abstract String getKeyIDName();
    public abstract String[] getColumnNames();
    public abstract void setValue(Cursor rs, T entity);
}
