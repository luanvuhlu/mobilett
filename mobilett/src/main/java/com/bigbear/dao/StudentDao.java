package com.bigbear.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableStudentResponse;
import com.bigbear.common.Validate;
import com.bigbear.entity.Student;
import com.bigbear.entity.TimeTable;

/**
 * Created by luanvu on 4/1/15.
 */
public class StudentDao extends AbstractDao<Student> implements StudentDaoInterface<Student> {
    private static final String LOG_TAG = "StudentDao";

    public StudentDao() {
    }

    public StudentDao(Context context, SQLiteDatabase db) {
        super(context, db);
    }

    public StudentDao(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return "STUDENT";
    }

    @Override
    public String getKeyIDName() {
        return "ID";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{"ID", "CODE", "NAME", "STUDENT_CLASS"};
    }

    @Override
    public Student getEntryById(long id) throws Exception {
        Student s = new Student();
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
    public long save(Student student) {
        try {
            ContentValues contentValues = toValue(student);
            Log.d(LOG_TAG, "DB is null: "+(getDb()==null));
            long id=getDb().insert(getTableName(), null, contentValues);
            student.setId(id);
            Log.d(LOG_TAG, "Saved Student id: "+id);
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
    public ContentValues toValue(Student entity) {
        ContentValues value = new ContentValues();
        if (entity.getId() != 0) value.put("ID", entity.getId());
        value.put("CODE", entity.getCode());
        value.put("NAME", entity.getName());
        value.put("STUDENT_CLASS", entity.getStudentClass());
        return value;
    }

    @Override
    public void setValue(Cursor rs, Student entity) {
        try {
            if(rs ==null || !rs.moveToFirst()){
                Log.d(LOG_TAG, "Cursor student empty");
                return;
            }
            Log.d(LOG_TAG, "Student id: "+rs.getLong(rs.getColumnIndexOrThrow("ID")));
            entity.setId(rs.getLong(rs.getColumnIndexOrThrow("ID")));
            entity.setCode(Validate.repNullCursor(rs.getColumnIndexOrThrow("CODE"), rs));
            entity.setName(Validate.repNullCursor(rs.getColumnIndexOrThrow("NAME"), rs));
            entity.setStudentClass(Validate.repNullCursor(rs.getColumnIndexOrThrow("STUDENT_CLASS"), rs));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }
    public Student getEntityFromResponse(TimeTableStudentResponse response){
        Student student=new Student();
        student.setCode(response.getCode());
        student.setName(response.getName());
        student.setStudentClass(response.getStudentClass());
        return student;
    }
    @Override
    public Student findById(long id) {
        Cursor rs = getDb().query(getTableName(), getColumnNames(), getKeyIDName()+"="+id, null, null, null, null);
        Log.d(LOG_TAG, "ID: "+id+" - Cursor: "+rs.toString()+" - Count: "+rs.getCount());
        Student entity=new Student();
        setValue(rs, entity);
        return entity;
    }
}
