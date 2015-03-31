package com.bigbear.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectStudyDayResponse;

public class SubjectStudyClassEtt extends AbstractEntity {
	private String classType;
	private String dayName;
	private String dayHours;
	private String dayLocations;
	private long subjectClassId;
	private static SubjectStudyClassEtt instance=new SubjectStudyClassEtt();
	public static final String TABLE_NAME="SUBJECT_STUDY_CLASS";
	public static final String[] COLUMNS={"ID", "SUBJECT_CLASS_ID", "CLASS_TYPE", "DAY_NAME", "DAY_HOURS", "DAY_LOCATION"};
	public static final String CREATE_TABLE_SQL = "CREATE TABLE SUBJECT_STUDY_CLASS ( ID"
		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ " SUBJECT_CLASS_ID INTEGER NOT NULL, "
		+ " CLASS_TYPE TEXT NOT NULL, "
		+ " DAY_NAME TEXT NOT NULL, "
		+ " DAY_HOURS TEXT NOT NULL, "
		+ " DAY_LOCATION TEXT NOT NULL , "
		+ " FOREIGN KEY (SUBJECT_CLASS_ID) REFERENCES SUBJECT_CLASS(ID) ); ";
	private static final String LOG_TAG = null;
	public SubjectStudyClassEtt(TimeTableSubjectStudyDayResponse d, long subjectClassId) {
		this.classType=d.getClassType();
		this.dayHours=d.getDayHours();
		this.dayLocations=d.getDayLocation();
		this.dayName=d.getDayName();
		this.subjectClassId=subjectClassId;
	}
	@SuppressWarnings("unused")
	private static SubjectStudyClassEtt getInstance() {
		return instance;
	}
	public static SubjectStudyClassEtt getInstance(TimeTableSubjectStudyDayResponse d, long subjectClassId) {
		instance.id=0;
		instance.classType=d.getClassType();
		instance.dayHours=d.getDayHours();
		instance.dayLocations=d.getDayLocation();
		instance.dayName=d.getDayName();
		instance.subjectClassId=subjectClassId;
		return instance;
	}
	
	public static SubjectStudyClassEtt getInstance(long id) {
		instance = new SubjectStudyClassEtt(id);
		return instance;
	}
	private SubjectStudyClassEtt() {
		super();
	}
	private SubjectStudyClassEtt(long id2) {
		super(id2);
	}
	public String getClassType() {
		return classType;
	}
	public String getDayHours() {
		return dayHours;
	}
	public String getDayLocations() {
		return dayLocations;
	}
	public String getDayName() {
		return dayName;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public long getSubjectClassId() {
		return subjectClassId;
	}
	public void setSubjectClassId(long subjectClassId) {
		this.subjectClassId = subjectClassId;
	}
	public void setDayHours(String dayHours) {
		this.dayHours = dayHours;
	}
	public void setDayLocations(String dayLocations) {
		this.dayLocations = dayLocations;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	
	public ContentValues toValue(boolean hasId) {
		ContentValues value=new ContentValues();
		if(hasId) value.put("ID", this.getId());
		value.put("SUBJECT_CLASS_ID", this.getSubjectClassId());
		value.put("CLASS_TYPE", this.getClassType());
		value.put("DAY_NAME", this.getDayName());
		value.put("DAY_HOURS", this.getDayHours());
		value.put("DAY_LOCATION", this.getDayLocations());
		return value;
	}


	public final String getTableName() {
		return TABLE_NAME;
	}

	public final String getCreateTableQuery() {
		return CREATE_TABLE_SQL;
	}

	public final String[] getColumnNames() {
		return COLUMNS;
	}


	public void setValue(Cursor rs) {
		this.setId(rs.getLong(0));
		this.setSubjectClassId(rs.getLong(1));
		this.setClassType(rs.getString(2));
		this.setDayName(rs.getString(3));
		this.setDayHours(rs.getString(4));
		this.setDayLocations(rs.getString(5));
	}
	
public static List<SubjectStudyClassEtt> getSubjectStudyClassFromTimeTable(long id, TimeTableDBAdapter adapter) throws Exception{
	List<SubjectStudyClassEtt> etts=new ArrayList<SubjectStudyClassEtt>();
	SubjectStudyClassEtt ett=new SubjectStudyClassEtt();
	ett.setId(id);
	ett.setWhereQuery("SUBJECT_CLASS_ID="+id);
	Cursor cs=adapter.getEntry(ett);
	if(cs ==null){
		 Log.d(LOG_TAG, "No row !");
		 return etts;
	 }
	 if(cs.moveToFirst()){
		 do{
			 ett=new SubjectStudyClassEtt();
			 ett.setValue(cs);
			 etts.add(ett);
		 }while(cs.moveToNext());
	 }else{
		 Log.d(LOG_TAG, "Cursor empty !");
	 }
	return etts;
}

}
