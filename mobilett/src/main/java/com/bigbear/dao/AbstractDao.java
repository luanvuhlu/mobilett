package com.bigbear.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bigbear.entity.Student;

import java.text.ParseException;

/**
 * Created by luanvu on 4/1/15.
 */
public abstract class AbstractDao<T> {
    private static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 1;
    private DBHelper helper;
    private Context context;
    private SQLiteDatabase db;
    public static final int WRITE_MODE=1;
    public static final int READ_MODE=0;

    protected AbstractDao(Context context, SQLiteDatabase db) {
        this.context = context;
        this.db = db;
    }

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

    /**
     * Open database
     * @throws SQLException
     */
    private void openWrite() throws SQLException {
        db=helper.getWritableDatabase();
    }
    /**
     * Open database
     * @throws SQLException
     */
    private void openRead() throws SQLException {
        db=helper.getReadableDatabase();
    }
    /**
     * Open database
     * @throws SQLException
     */
    public void open(int mode) throws SQLException {
        if(mode==WRITE_MODE){
            openWrite();
        }else{
            openRead();
        }
    }

    /**
     * Close database
     */
    public void close(){
        if(db!=null)
            db.close();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * Chuyển entity sang dạng {@link android.content.ContentValues} để thao tác với database
     * @param entity
     * @return Trả về {@link android.content.ContentValues}
     */
    public abstract ContentValues toValue(T entity);
    public abstract String getTableName();
    public abstract String getKeyIDName();
    public abstract String[] getColumnNames();

    /**
     * Fill dữ liệu vào entity từ {@link android.database.Cursor} lấy từ database
     * @param rs con trỏ từ database
     * @param entity cũ sau khi được set giá trị
     */
    public abstract void setValue(Cursor rs, T entity) throws Exception;
}
