package com.bigbear.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectStudyDayResponse;
import com.bigbear.entity.SubjectClass;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by luanvu on 4/1/15.
 */
public class SubjectStudyClassDao extends AbstractDao<SubjectStudyClass> implements SubjectStudyClassDaoInterface<SubjectStudyClass> {
    private static final String LOG_TAG = "SubjectStudyClassDao";

    public SubjectStudyClassDao(Context context) {
        super(context);
    }
    public SubjectStudyClassDao() {
    }

    public SubjectStudyClassDao(Context context, SQLiteDatabase db) {
        super(context, db);
    }

    @Override
    public String getTableName() {
        return "SUBJECT_STUDY_CLASS";
    }

    @Override
    public String getKeyIDName() {
        return "ID";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"ID", "TIMETABLE_ID", "SUBJECT_CLASS_ID", "CLASS_TYPE", "DAY_NAME", "DAY_HOURS", "DAY_LOCATION"};
    }

    @Override
    public SubjectStudyClass getEntryById(long id) throws Exception {
        SubjectStudyClass s = null;
        Cursor rs=null;
        try {
            rs = getDb().query(getTableName(), getColumnNames(), getKeyIDName() + "=" + id,
                    null, null, null, null);
            s = new SubjectStudyClass();
            if (rs == null || rs.getCount() < 1 || !rs.moveToFirst()) {
                throw new Exception("Cannot found object: " + getTableName() + ": " + id);
            }
            if(rs!=null && rs.moveToFirst()) {
                setValue(rs, s);
            }
        }catch (Exception e){
            throw e;
        }finally {
            if (rs != null) rs.close();
        }
        return s;
    }

    @Override
    public Cursor getAllEntries() {
        return getDb().query(getTableName(), getColumnNames(), null, null, null, null, null);
    }

    @Override
    public long save(SubjectStudyClass entity) {
        try {
            ContentValues contentValues = toValue(entity);
            long id= getDb().insert(getTableName(), null, contentValues);
            entity.setId(id);
            Log.d(LOG_TAG, "Saved Subject_Study_Class id: "+id);
            return id;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public long delete(long id) {
            return getDb().delete(getTableName(), getKeyIDName() + "=" + id, null);

    }

    @Override
    public Set<SubjectStudyClass> getFromTimeTable(TimeTable timeTable) throws Exception {
        Set<SubjectStudyClass> etts=new HashSet<>();
        Cursor cs=null;
        try {
            cs = getDb().query(getTableName(), getColumnNames(), "TIMETABLE_ID = ?", new String[]{timeTable.getId() + ""}, null, null, null);
            if (cs == null) {
                Log.d(LOG_TAG, "No row !");
                return etts;
            }
            Log.d(LOG_TAG, "Cursor count: " + cs.getCount());
            if (cs.moveToFirst()) {
                do {
                    SubjectStudyClass ett = new SubjectStudyClass();
                    setValue(cs, ett);
                    ett.setTimeTable(timeTable);
                    etts.add(ett);
                } while (cs.moveToNext());
            } else {
                Log.d(LOG_TAG, "Cursor empty !");
            }
        }catch (Exception e){
            throw e;
        }finally {
            if(cs!=null)cs.close();
        }
        return etts;
    }

    @Override
    public ContentValues toValue(SubjectStudyClass entity) {
        ContentValues value=new ContentValues();
        if(entity.getId()!=0) value.put("ID", entity.getId());
        value.put("TIMETABLE_ID", entity.getTimeTable().getId());
        value.put("SUBJECT_CLASS_ID", entity.getSubjectClass().getId());
        value.put("CLASS_TYPE", entity.getClassType());
        value.put("DAY_NAME", entity.getDayName());
        value.put("DAY_HOURS", entity.getDayHours());
        value.put("DAY_LOCATION", entity.getDayLocations());
        return value;
    }

    @Override
    public void setValue(Cursor rs, SubjectStudyClass entity) throws Exception {
        try{
            if(rs ==null){
                Log.d(LOG_TAG, "Cursor subject study class empty");
                return;
            }
            SubjectClassDao subjectClassDao=new SubjectClassDao(getContext(), getDb());
            entity.setId(rs.getLong(0));
            entity.setTimeTable(new TimeTable(rs.getLong(1)));
            entity.setSubjectClass(subjectClassDao.findById(rs.getLong(2)));
            entity.setClassType(rs.getString(3));
            entity.setDayName(rs.getString(4));
            entity.setDayHours(rs.getString(5));
            entity.setDayLocations(rs.getString(6));
        }catch(Exception e){
            Log.e(LOG_TAG, "Seting value has some errors: "+e.getMessage(), e);
            throw  e;
        }
    }
    public SubjectStudyClass getEntityFromResponse(TimeTableSubjectStudyDayResponse res){
        SubjectStudyClass entity=new SubjectStudyClass();
        entity.setClassType(res.getClassType());
        entity.setDayHours(res.getDayHours());
        entity.setDayLocations(res.getDayLocation());
        entity.setDayName(res.getDayName());
        return entity;
    }
}
