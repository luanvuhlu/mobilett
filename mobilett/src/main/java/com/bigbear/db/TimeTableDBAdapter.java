package com.bigbear.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimeTableDBAdapter {
	private static final String DATABASE_NAME = "timetable.db";
	private static final int DATABASE_VERSION = 1;
	public static final String KEY_ID="_id";
	public static final String KEY_RANDOM_KEY="random_key";
	public static final int NAME_COLUMN = 1;
	private static final String LOG_TAG = "TIMETABLE_DB_ADAPTER";
	private static final String[] DATABASE_TABLES=
		{
		"SUBJECT", 
		"STUDENT", 
		"TIMETABLE", 
		"SUBJECT_CLASS", 
		"SUBJECT_STUDY_CLASS"
		};
	
	private SQLiteDatabase db;
	private Context context;
	private DBHelper helper;
	public TimeTableDBAdapter(Context context) {
		this.context=context;
		helper=new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
	public TimeTableDBAdapter open() throws SQLException{
		db=helper.getWritableDatabase();
		return this;
	}
	public void close(){
		if(db!=null)
			db.close();
	}
	public long insertEntry(AbstractEntity _myObject) throws Exception {
		ContentValues contentValues = _myObject.toValue(false);
		return db.insert(_myObject.getTableName(), null, contentValues);
	}
	public boolean removeEntry(AbstractEntity _myObject) {
		return db.delete(_myObject.getTableName(), _myObject.getKeyIDName() +"=" + _myObject.getId(), null) > 0;
	}
	public Cursor getAllEntries (String name, String[] cols) {
		return db.query(name,cols,
		null, null, null, null, null);
	}
	
	
	public Cursor getEntry(AbstractEntity _myObject) throws Exception {
		return db.query(_myObject.getTableName(), _myObject.getColumnNames(), _myObject.getWhereQuery(),
				null, null, null, _myObject.getOrder());
	}
	public AbstractEntity getEntryById(AbstractEntity _myObject) throws Exception {
		if(_myObject==null){
			throw new NullPointerException();
		}
		Cursor rs = db.query(_myObject.getTableName(), _myObject.getColumnNames(), _myObject.getKeyIDName()+"="+_myObject.getId(),
				null, null, null, null);
		
		if(rs==null || rs.getCount() < 1 || !rs.moveToFirst()){
			throw new Exception("Cannot found object: "+_myObject.getTableName()+": "+_myObject.getId());
		}
		_myObject.setValue(rs);
		if(rs!=null) rs.close();
		return _myObject;
	}
	public AbstractEntity getEntryNewest(AbstractEntity _myObject) throws Exception {
		if(_myObject.getOrder()==null || "".equals(_myObject.getOrder())){
			throw new Exception("Order query must be set");
		}
		Cursor rs = getEntry(_myObject);
		if(rs==null || rs.getCount() < 1 || !rs.moveToFirst()){
			throw new Exception("Cannot found object");
		}
		_myObject.setValue(rs);
		if(rs!=null) rs.close();
		return _myObject;
	}
	
	public int updateEntry(AbstractEntity _myObject) throws Exception {
		String where = _myObject.getKeyIDName() + "=" + _myObject.getId();
		ContentValues contentValues = _myObject.toValue(true);
		return db.update(_myObject.getTableName(), contentValues, where, null);
		}
	
	private static class DBHelper extends SQLiteOpenHelper{
		
		public DBHelper(Context context, String name, CursorFactory factory,
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
				Log.e(LOG_TAG, "Create table error: "+e.getMessage(), e);
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
}
