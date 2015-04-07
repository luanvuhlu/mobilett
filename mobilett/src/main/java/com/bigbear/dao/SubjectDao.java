package com.bigbear.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectResponse;
import com.bigbear.common.Validate;
import com.bigbear.entity.Subject;
import com.bigbear.entity.SubjectClass;

/**
 * Created by luanvu on 4/1/15.
 */
public class SubjectDao extends AbstractDao<Subject> implements SubjectDaoInterface<Subject> {
    private static final String LOG_TAG = "SubjectDao";

    public SubjectDao(Context context) {
        super(context);
    }
    public SubjectDao() {
    }
    public SubjectDao(Context context, SQLiteDatabase sql) {
        super(context, sql);
    }
    @Override
    public String getTableName() {
        return "SUBJECT";
    }

    @Override
    public String getKeyIDName() {
        return "ID";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"ID","SUBJECT_CODE", "SUBJECT_NAME", "COURSE_CREDIT", "SPECIALITY",   "SUBJECT_SHORT_NAME"};
    }

    @Override
    public Subject getEntryById(long id) throws Exception {
        Subject s = new Subject();
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
    public long save(Subject entity) {
        try {
            ContentValues contentValues = toValue(entity);
            long id=getDb().insert(getTableName(), null, contentValues);
            entity.setId(id);
            Log.d(LOG_TAG, "Saved Subject id: "+id);
            return id;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public long delete(long id) {
        try {
            return getDb().delete(getTableName(), getKeyIDName() + "=" + id, null);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public ContentValues toValue(Subject entity) {
        ContentValues value=new ContentValues();
        if(entity.getId()!=0) value.put("ID", entity.getId());
        value.put("SUBJECT_CODE", entity.getSubjectCode());
        value.put("SUBJECT_NAME", entity.getSubjectName());
        value.put("COURSE_CREDIT", entity.getCourseCredit());
        value.put("SPECIALITY", entity.getSpeciality());
        value.put("SUBJECT_SHORT_NAME", entity.getSubjectShortName());
        return value;
    }

    @Override
    public void setValue(Cursor rs, Subject entity) {
        try{
            if(rs ==null || !rs.moveToFirst()){
                Log.d(LOG_TAG, "Cursor subject empty");
                return;
            }
            entity.setId(rs.getLong(0));
            entity.setSubjectCode(Validate.repNullCursor(1, rs));
            entity.setSubjectName(Validate.repNullCursor(2, rs));
            entity.setCourseCredit(Validate.repNullIntCursor(3, rs));
            entity.setSpeciality(Validate.repNullCursor(4, rs));
            entity.setSubjectShortName(Validate.repNullCursor(5, rs));
        }catch(Exception e){
            Log.e(LOG_TAG, "Seting value has some errors: "+e.getMessage(), e);
        }
    }
    public Subject getEntityFromResponse(TimeTableSubjectResponse res){
        Subject entity=new Subject();
        entity.setCourseCredit(res.getCourseCredit()==null?0:res.getCourseCredit().intValue());
        entity.setSpeciality(res.getSpeciality());
        entity.setSubjectCode(res.getSubjectCode());
        entity.setSubjectName(res.getSubjectName());
        entity.setSubjectShortName(res.getSubjectShortName());
        return entity;
    }
    @Override
    public Subject findById(long id) {
        Cursor rs = getDb().query(getTableName(), getColumnNames(), getKeyIDName()+"="+id, null, null, null, null);
        Subject entity=new Subject();
        setValue(rs, entity);
        return entity;
    }
}
