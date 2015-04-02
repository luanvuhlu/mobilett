package com.bigbear.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableStudentResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableSubjectClassResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableSubjectStudyDayResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;
import com.bigbear.common.TimeCommon;
import com.bigbear.common.Validate;
import com.bigbear.db.StudentEtt;
import com.bigbear.db.SubjectClassEtt;
import com.bigbear.entity.Student;
import com.bigbear.entity.SubjectClass;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by luanvu on 4/1/15.
 */
public class TimeTableDao extends AbstractDao<TimeTable> implements TimeTableDaoInterface<TimeTable> {
    private static final String LOG_TAG = "StudentDao";

    public TimeTableDao(Context context) {
        super(context);
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
        try {
            open();
            ContentValues contentValues = toValue(entity);
            return getDb().insert(getTableName(), null, contentValues);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        } finally {
            close();
        }

    }

    @Override
    public long delete(long id) {
        try {
            open();
            return getDb().delete(getTableName(), getKeyIDName() + "=" + id, null);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        } finally {
            close();
        }

    }

    @Override
    public ContentValues toValue(TimeTable entity) {
        ContentValues value=new ContentValues();
        if(entity.getId()!=0) value.put("ID", entity.getId());
        value.put("STUDENT_ID", entity.getStudent().getId());
        value.put("SEMESTER", entity.getSemester());
        value.put("YEAR", entity.getYear());
        return value;
    }

    @Override
    public void setValue(Cursor rs, TimeTable entity) {
        try{
            entity.setId(rs.getLong(0));
            entity.setStudent(new Student(rs.getLong(1)));
            entity.setSemester(Validate.repNullCursor(2, rs));
            entity.setYear(Validate.repNullCursor(3, rs));
            entity.setCreatedDate(TimeCommon.parseDate(Validate.repNullCursor(4, rs), TimeCommon.DEFAULT_CURSOR_DATE));
        }catch(Exception e){
            Log.e(LOG_TAG, "ERROR: "+e.getMessage(), e);
        }
    }
    public TimeTable getEntityFromResponse(TimeTableTimeTableResponse res){
        StudentDao studentDao=new StudentDao();
        SubjectStudyClassDao subjectStudyClassDao=new SubjectStudyClassDao();
        SubjectClassDao subjectClassDao=new SubjectClassDao();
        TimeTable entity=new TimeTable();
        entity.setSemester(res.getSemester());
        entity.setYear(res.getYear());
        entity.setStudent(studentDao.getEntityFromResponse(res.getStudent()));
        for(TimeTableSubjectClassResponse subjectCLassResponse:res.getSubjectClass()){
            SubjectClass subjectClass=subjectClassDao.getEntityFromResponse(subjectCLassResponse);
            for(TimeTableSubjectStudyDayResponse dayResponse:subjectCLassResponse.getSubjectStudyDay()){
                SubjectStudyClass subjectStudyClass=subjectStudyClassDao.getEntityFromResponse(dayResponse);
                subjectStudyClass.setTimeTable(entity);
                subjectStudyClass.setSubjectClass(subjectClass);
                entity.getSubjectStudyClass().add(subjectStudyClass);
            }
        }
        return entity;
    }
}
