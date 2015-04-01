package com.bigbear.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.bigbear.entity.Student;

/**
 * Created by luanvu on 4/1/15.
 */
public interface StudentDaoInterface<T> {
    public Student getEntryById(long id) throws Exception;
    public Cursor getAllEntries ();
    public long save(Student student);
    public long delete(long id);
}
