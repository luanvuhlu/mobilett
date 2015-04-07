package com.bigbear.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectClassResponse;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.Subject;
import com.bigbear.entity.SubjectClass;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by luanvu on 4/1/15.
 */
public class SubjectClassDao extends AbstractDao<SubjectClass> implements SubjectClassDaoInterface<SubjectClass> {
    private static final String LOG_TAG = "SubjectClassDao";

    public SubjectClassDao(Context context) {
        super(context);
    }
    public SubjectClassDao() {

    }

    public SubjectClassDao(Context context, SQLiteDatabase db) {
        super(context, db);
    }

    @Override
    public String getTableName() {
        return "SUBJECT_CLASS";
    }

    @Override
    public String getKeyIDName() {
        return "ID";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"ID", "SUBJECT_ID", "THEORY_CLASS", "SEMINAR_CLASS", "START_DATE", "END_DATE"};
    }

    @Override
    public SubjectClass getEntryById(long id) throws Exception {
        SubjectClass s = new SubjectClass();
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
    public long save(SubjectClass entity) {
        try {
            ContentValues contentValues = toValue(entity);
            long id=getDb().insert(getTableName(), null, contentValues);
            entity.setId(id);
            Log.d(LOG_TAG, "Saved Subject_Class id: "+id);
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
    public ContentValues toValue(SubjectClass entity) {
        ContentValues value = new ContentValues();
        if (entity.getId() != 0) value.put("ID", entity.getId());
        value.put("SUBJECT_ID", entity.getSubject().getId());
        value.put("THEORY_CLASS", entity.getTheoryClass());
        value.put("SEMINAR_CLASS", entity.getSeminarClass());
        try {
            value.put("START_DATE", TimeCommon.formatDate(entity.getStartDate()));
            value.put("END_DATE", TimeCommon.formatDate(entity.getEndDate()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return value;
    }

    @Override
    public void setValue(Cursor rs, SubjectClass entity) {
        try {
            SubjectDao subjectDao=new SubjectDao(getContext());
            entity.setId(rs.getLong(0));
            entity.setSubject(subjectDao.findById(rs.getLong(1)));
            entity.setTheoryClass(rs.getString(2));
            entity.setSeminarClass(rs.getString(3));
            entity.setStartDate(TimeCommon.parseDate(rs.getString(4)));
            entity.setEndDate(TimeCommon.parseDate(rs.getString(5)));
        } catch (Exception e) {
            Log.d("SUBJECT_CLASS TABLE", "Set value error: " + e.getMessage());
        }
    }
    public SubjectClass getEntityFromResponse(TimeTableSubjectClassResponse res){
        SubjectDao subjectDao=new SubjectDao();
        SubjectClass entity=new SubjectClass();
        entity.setEndDate(TimeCommon.toDate(res.getEndDate()));
        entity.setStartDate(TimeCommon.toDate(res.getStartDate()));
        entity.setTheoryClass(res.getTheoryClass());
        entity.setSeminarClass(res.getSeminarClass());
        entity.setSubject(subjectDao.getEntityFromResponse(res.getSubject()));
        return entity;
    }
    @Override
    public SubjectClass findById(long id) {
        Cursor rs = getDb().query(getTableName(), getColumnNames(), getKeyIDName()+"="+id, null, null, null, null);
        SubjectClass entity=new SubjectClass();
        setValue(rs, entity);
        return entity;
    }
}
