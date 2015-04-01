package com.bigbear.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bigbear.db.StudentEtt;
import com.bigbear.db.SubjectClassEtt;
import com.bigbear.db.SubjectEtt;
import com.bigbear.db.SubjectStudyClassEtt;
import com.bigbear.db.TimeTableEtt;

/**
 * Created by luanvu on 4/1/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "DBHelper";
    private static final String[] DATABASE_TABLES=
            {
                    "SUBJECT",
                    "STUDENT",
                    "TIMETABLE",
                    "SUBJECT_CLASS",
                    "SUBJECT_STUDY_CLASS"
            };
    private static final String DATABASE_NAME="TIMETABLE_DB";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(TimeTableEtt.CREATE_TABLE_SQL);
            db.execSQL(StudentEtt.CREATE_TABLE_SQL);
            db.execSQL(SubjectEtt.CREATE_TABLE_SQL);
            db.execSQL(SubjectClassEtt.CREAT_TABLE_SQL);
            db.execSQL(SubjectStudyClassEtt.CREATE_TABLE_SQL);
        }catch(SQLException e){
            Log.e(LOG_TAG, "Create table error: " + e.getMessage(), e);
            throw e;
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " +
                oldVersion + " to " +
                newVersion +
                ", which will destroy all old data");
        for(String str:DATABASE_TABLES){
            db.execSQL(" DROP TABLE IF EXISTS " + str+"; ");
        }
        onCreate(db);
    }
}