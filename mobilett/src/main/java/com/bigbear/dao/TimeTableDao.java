package com.bigbear.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectClassResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableSubjectStudyDayResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;
import com.bigbear.common.TimeCommon;
import com.bigbear.common.Validate;
import com.bigbear.entity.Student;
import com.bigbear.entity.SubjectClass;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by luanvu on 4/1/15.
 */
public class TimeTableDao extends AbstractDao<TimeTable> implements TimeTableDaoInterface<TimeTable> {
    private static final String LOG_TAG = "TimeTableDao";

    public TimeTableDao(Context context) {
        super(context);
    }

    public TimeTableDao(Context context, SQLiteDatabase db) {
        super(context, db);
    }

    @Override
    public String getTableName() {
        return "TIMETABLE";
    }

    @Override
    public String getKeyIDName() {
        return "ID";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"ID", "STUDENT_ID", "SEMESTER", "YEAR", "CREATED_TIME"};
    }

    @Override
    public TimeTable getEntryById(long id) throws Exception {
        TimeTable s = new TimeTable();
        Cursor rs = getDb().query(getTableName(), getColumnNames(), getKeyIDName() + "=" + id,
                null, null, null, null);

        if (rs == null || rs.getCount() < 1 || !rs.moveToFirst()) {
            throw new Exception("Cannot found object: " + getTableName() + ": " + id);
        }
        setValue(rs, s);
        if (rs != null) rs.close();
        return s;
    }

    @Override
    public Cursor getAllEntries() {
        return getDb().query(getTableName(), getColumnNames(), null, null, null, null, null);
    }

    @Override
    public long save(TimeTable entity) {

        ContentValues contentValues = toValue(entity);
        long id = getDb().insert(getTableName(), null, contentValues);
        Log.d(LOG_TAG, "Saved TimeTable id: " + id);
        entity.setId(id);
        return id;

    }

    @Override
    public long delete(long id) {
        return getDb().delete(getTableName(), getKeyIDName() + "=" + id, null);
    }

    // TODO
    @Override
    public List<TimeTable> findAll() throws Exception {
        List<TimeTable> etts = new ArrayList<TimeTable>();
        Cursor cs = null;
        try {
            cs = getAllEntries();
            if (cs == null) {
                Log.d(LOG_TAG, "No row !");
                return etts;
            }
            if (cs.moveToFirst()) {
                do {
                    TimeTable ett = new TimeTable();
                    setValue(cs, ett);
                    etts.add(ett);
                } while (cs.moveToNext());
            } else {
                Log.d(LOG_TAG, "Cursor empty !");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cs != null) cs.close();
        }
        return etts;
    }

    // TODO
    @Override
    public TimeTable getNewest() throws Exception {
        Cursor rs = null;
        TimeTable ett = null;
        try {
            rs = getDb().query(getTableName(), getColumnNames(), null, null, null, null, "CREATED_TIME DESC", "1");
            ett=new TimeTable();
            if(rs!=null && rs.moveToFirst()) {
                setValue(rs, ett);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
        }
        return ett;
    }

    @Override
    public TimeTable findById(long id) throws Exception {
        Cursor rs = null;
        TimeTable timeTable = null;
        try {
            rs = getDb().query(getTableName(), getColumnNames(), getKeyIDName() + "=" + id, null, null, null, null);
            timeTable = new TimeTable();
            if(rs!=null && rs.moveToFirst()) {
                setValue(rs, timeTable);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
        }
        return timeTable;
    }

    @Override
    public ContentValues toValue(TimeTable entity) {
        ContentValues value = new ContentValues();
        if (entity.getId() != 0) value.put("ID", entity.getId());
        value.put("STUDENT_ID", entity.getStudent().getId());
        value.put("SEMESTER", entity.getSemester());
        value.put("YEAR", entity.getYear());
        return value;
    }

    @Override
    public void setValue(Cursor rs, TimeTable entity) throws Exception {
        try {
            if (rs == null) {
                Log.d(LOG_TAG, "Cursor timetable empty");
                return;
            }
            StudentDao studentDao = new StudentDao(getContext(), getDb());
            SubjectStudyClassDao subjectStudyClassDao = new SubjectStudyClassDao(getContext(), getDb());
            entity.setId(rs.getLong(rs.getColumnIndexOrThrow("ID")));
            entity.setStudent(studentDao.findById(rs.getLong(rs.getColumnIndexOrThrow("STUDENT_ID"))));
            entity.setSemester(Validate.repNullCursor(rs.getColumnIndexOrThrow("SEMESTER"), rs));
            entity.setYear(Validate.repNullCursor(rs.getColumnIndexOrThrow("YEAR"), rs));
            entity.setCreatedDate(TimeCommon.parseDate(Validate.repNullCursor(rs.getColumnIndexOrThrow("CREATED_TIME"), rs), TimeCommon.DEFAULT_CURSOR_DATE));
            entity.setSubjectStudyClass(subjectStudyClassDao.getFromTimeTable(entity));
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
            throw e;
        }
    }

    public TimeTable getEntityFromResponse(TimeTableTimeTableResponse res) {
        StudentDao studentDao = new StudentDao(getContext());
        SubjectStudyClassDao subjectStudyClassDao = new SubjectStudyClassDao(getContext());
        SubjectClassDao subjectClassDao = new SubjectClassDao(getContext());
        TimeTable entity = new TimeTable();
        entity.setSemester(res.getSemester());
        entity.setYear(res.getYear());
        entity.setStudent(studentDao.getEntityFromResponse(res.getStudent()));
        entity.setSubjectStudyClass(new HashSet<SubjectStudyClass>());
        for (TimeTableSubjectClassResponse subjectCLassResponse : res.getSubjectClass()) {
            SubjectClass subjectClass = subjectClassDao.getEntityFromResponse(subjectCLassResponse);
            for (TimeTableSubjectStudyDayResponse dayResponse : subjectCLassResponse.getSubjectStudyDay()) {
                SubjectStudyClass subjectStudyClass = subjectStudyClassDao.getEntityFromResponse(dayResponse);
                subjectStudyClass.setTimeTable(entity);
                subjectStudyClass.setSubjectClass(subjectClass);
                entity.getSubjectStudyClass().add(subjectStudyClass);
            }
        }
        return entity;
    }
}
