package com.bigbear.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


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
    private static final String CREAT_TABLE_SUBJECT_CLASS_SQL = "CREATE TABLE SUBJECT_CLASS ( ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " SUBJECT_ID INTEGER NOT NULL, "
            + " THEORY_CLASS TEXT NOT NULL, "
            + " SEMINAR_CLASS TEXT, "
            + " START_DATE DATE, "
            + " END_DATE DATE, "
            + " FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT(ID)";
    private static final String CREATE_TABLE_STUDENT_SQL="CREATE TABLE STUDENT ( "
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " CODE TEXT NOT NULL, "
            + " NAME TEXT NOT NULL, "
            + " STUDENT_CLASS TEXT NOT NULL "
            + "  ); ";
    private static final String CREATE_TABLE_SUBJECT_SQL = "CREATE TABLE SUBJECT ( "
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " SUBJECT_CODE TEXT NOT NULL, "
            + " SUBJECT_NAME TEXT NOT NULL, "
            + " COURSE_CREDIT INTEGER NOT NULL, "
            + " SPECIALITY TEXT , "
            + " SUBJECT_SHORT_NAME TEXT ); ";
    private static final String CREATE_TABLE_SUBJECT_STUDY_CLASS_SQL = "CREATE TABLE SUBJECT_STUDY_CLASS ( ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " SUBJECT_CLASS_ID INTEGER NOT NULL, "
            + " TIMETABLE_ID INTEGER NOT NULL, "
            + " CLASS_TYPE TEXT NOT NULL, "
            + " DAY_NAME TEXT NOT NULL, "
            + " DAY_HOURS TEXT NOT NULL, "
            + " DAY_LOCATION TEXT NOT NULL , "
            + " FOREIGN KEY (SUBJECT_CLASS_ID) REFERENCES SUBJECT_CLASS(ID) ); "
            + " FOREIGN KEY (TIMETABLE_ID) REFERENCES TIMETABLE(ID) ); ";
    private static final String CREATE_TABLE_TIMETABLE_SQL = "CREATE TABLE TIMETABLE ( ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " STUDENT_ID INTEGER NOT NULL, "
            + " SEMESTER TEXT, "
            + " YEAR TEXT, "
            + " CREATED_TIME REAL DEFAULT (datetime('now','localtime')), "
            + " FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(ID)  ); ";
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
            db.execSQL(CREATE_TABLE_STUDENT_SQL);
            db.execSQL(CREATE_TABLE_SUBJECT_SQL);
            db.execSQL(CREAT_TABLE_SUBJECT_CLASS_SQL);
            db.execSQL(CREATE_TABLE_SUBJECT_STUDY_CLASS_SQL);
            db.execSQL(CREATE_TABLE_TIMETABLE_SQL);

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