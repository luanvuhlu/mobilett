package com.bigbear.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectResponse;
import com.bigbear.common.Validate;

public class SubjectEtt extends AbstractEntity {
	private int courseCredit;
	  private String speciality;
	  private String subjectCode;
	  private String subjectName;
	  private String subjectShortName;
	public static final String TABLE_NAME="SUBJECT";
	public static final String[] COLUMNS={"ID","SUBJECT_CODE", "SUBJECT_NAME", "COURSE_CREDIT", "SPECIALITY",   "SUBJECT_SHORT_NAME"};
	public static final String CREATE_TABLE_SQL = "CREATE TABLE SUBJECT ( "
		+ "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ " SUBJECT_CODE TEXT NOT NULL, "
		+ " SUBJECT_NAME TEXT NOT NULL, "
		+ " COURSE_CREDIT INTEGER NOT NULL, "
		+ " SPECIALITY TEXT , "
		+ " SUBJECT_SHORT_NAME TEXT ); ";
	private static final String LOG_TAG="TABLE SUBJECT";
	private static SubjectEtt instance=new SubjectEtt();
	public static SubjectEtt getInstance() {
		return instance;
	}
	public SubjectEtt (TimeTableSubjectResponse res) {
		this.courseCredit=res.getCourseCredit()==null?0:res.getCourseCredit().intValue();
		this.speciality=res.getSpeciality();
		this.subjectCode=res.getSubjectCode();
		this.subjectName=res.getSubjectName();
		this.subjectShortName=res.getSubjectShortName();
	}
	public static SubjectEtt getInstance(TimeTableSubjectResponse res) {
		instance.courseCredit=res.getCourseCredit()==null?0:res.getCourseCredit().intValue();
		instance.speciality=res.getSpeciality();
		instance.subjectCode=res.getSubjectCode();
		instance.subjectName=res.getSubjectName();
		instance.subjectShortName=res.getSubjectShortName();
		return instance;
	}
	private SubjectEtt() {
		super();
	}
public SubjectEtt(long id) {
		super(id);
	}
	public int getCourseCredit()
	  {
	    return this.courseCredit;
	  }
	public String getSpeciality()
	  {
	    return this.speciality;
	  }
	public String getSubjectCode()
	  {
	    return this.subjectCode;
	  }
	public String getSubjectName()
	  {
	    return this.subjectName;
	  }
	public String getSubjectShortName()
	  {
	    return this.subjectShortName;
	  }
	public void setCourseCredit(int courseCredit) {
		this.courseCredit = courseCredit;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public void setSubjectShortName(String subjectShortName) {
		this.subjectShortName = subjectShortName;
	}
	
	

	public final String getTableName() {
		return TABLE_NAME;
	}
//	"ID","SUBJECT_CODE", "SUBJECT_NAME", "COURSE_CREDIT", "SPECIALITY",   "SUBJECT_SHORT_NAME"
	public final String getCreateTableQuery() {
		return CREATE_TABLE_SQL;
	}

	public final String[] getColumnNames() {
		return COLUMNS;
	}

	public void setValue(Cursor rs) {
		try{
		this.setId(rs.getLong(0));
		this.setSubjectCode(Validate.repNullCursor(1, rs));
		this.setSubjectName(Validate.repNullCursor(2, rs));
		this.setCourseCredit(Validate.repNullIntCursor(3, rs));
		this.setSpeciality(Validate.repNullCursor(4, rs));
		this.setSubjectShortName(Validate.repNullCursor(5, rs));
		}catch(Exception e){
			Log.e(LOG_TAG, "Seting value has some errors: "+e.getMessage(), e);
		}
	}
	public ContentValues toValue(boolean hasId) {
		ContentValues value=new ContentValues();
		if(hasId) value.put("ID", this.getId());
		value.put("SUBJECT_CODE", this.getSubjectCode());
		value.put("SUBJECT_NAME", this.getSubjectName());
		value.put("COURSE_CREDIT", this.getCourseCredit());
		value.put("SPECIALITY", this.getSpeciality());
		value.put("SUBJECT_SHORT_NAME", this.getSubjectShortName());
		return value;
	}
	public List<StudentEtt> getAllEntries(TimeTableDBAdapter adapter){
		 List<StudentEtt> etts=new ArrayList<StudentEtt>();
		 Cursor cs=adapter.getAllEntries(TABLE_NAME, COLUMNS);
		 if(cs ==null){
			 Log.d(LOG_TAG, "No row !");
			 return etts;
		 }
		 try{
		 if(cs.moveToFirst()){
			 do{
				 setValue(cs);
			 }while(cs.moveToNext());
		 }else{
			 Log.d(LOG_TAG, "Cursor empty !");
		 }
		 }catch (Exception e) {
			 Log.e(LOG_TAG, "Error: "+e.getMessage(), e);
		}finally{
			if(cs!=null) cs.close();
		}
		 return etts;
	}
public AbstractEntity setDetail(TimeTableDBAdapter adapter) throws Exception {
		return adapter.getEntryById(this);
	}
}
