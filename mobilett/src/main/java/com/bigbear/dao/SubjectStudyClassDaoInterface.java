package com.bigbear.dao;

import android.database.Cursor;

import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;

import java.util.Set;

/**
 * Created by luanvu on 4/1/15.
 */
public interface SubjectStudyClassDaoInterface<T> {
    public T getEntryById(long id) throws Exception;
    public Cursor getAllEntries();
    public long save(T entity);
    public long delete(long id);
    public Set<T> getFromTimeTable(TimeTable timeTable);
}
