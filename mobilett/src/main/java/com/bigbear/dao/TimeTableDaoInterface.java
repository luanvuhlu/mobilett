package com.bigbear.dao;

import android.database.Cursor;

import com.bigbear.adapter.HoursEntity;
import com.bigbear.adapter.ListHours;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.Student;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luanvu on 4/1/15.
 */
public interface TimeTableDaoInterface<T> {
    public T getEntryById(long id) throws Exception;
    public Cursor getAllEntries();
    public long save(T entity);
    public long delete(long id);
    /**
     *
     * @return Trả về tất cả thời khóa biểu {@link com.bigbear.entity.TimeTable }
     */
    public List<TimeTable> findAll();
    /**
     *
     * @return Trả về thời khóa biểu mới được import {@link com.bigbear.entity.TimeTable }
     */
    public TimeTable getNewest();
    /**
     *
     * @param id
     * @return Trả về Thời khóa biểu theo id
     */
    public TimeTable findById(long id);
}
