package com.bigbear.db;


import com.appspot.hlutimetable.timetable.model.TimeTableStudentResponse;
import com.bigbear.common.Validate;

import android.content.ContentValues;
import android.database.Cursor;

public class StudentEtt extends AbstractEntity {
	private String code;
	  private String name;
	  private String studentClass;
	  public static final String TABLE_NAME="STUDENT";
	public static final String[] COLUMNS={"ID","CODE", "NAME", "STUDENT_CLASS"};
	public static final String CREATE_TABLE_SQL="CREATE TABLE STUDENT ( "
			+ "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ " CODE TEXT NOT NULL, "
			+ " NAME TEXT NOT NULL, "
			+ " STUDENT_CLASS TEXT NOT NULL "
			+ "  ); ";
	private static StudentEtt instance=new StudentEtt();
	public StudentEtt() {
		super();
	}
	public static StudentEtt getInstance() {
		return instance;
	}
	public static StudentEtt getInstance(long id) {
		instance=new StudentEtt(id);
		return instance;
	}
	public static StudentEtt getInstance(TimeTableStudentResponse response) {
		instance.id=0;
		instance.code=response.getCode();
		instance.name=response.getName();
		instance.studentClass=response.getStudentClass();
		return instance;
	}
	private StudentEtt(long id) {
		super(id);
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getStudentClass() {
		return studentClass;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
//	"ID","CODE", "NAME", "STUDENT_CLASS"
	public String getCreateTableQuery() {
		return CREATE_TABLE_SQL;
	}

	public String[] getColumnNames() {
		return COLUMNS;
	}


//	"ID","CODE", "NAME", "STUDENT_CLASS"
	public void setValue(Cursor rs) {
		try{
			this.setId(rs.getLong(0));
			this.setCode(Validate.repNullCursor(1, rs));
			this.setName(Validate.repNullCursor(2, rs));
			this.setStudentClass(Validate.repNullCursor(3, rs));
		}catch(Exception e){
			// TODO
		}
	}
//	"ID","CODE", "NAME", "STUDENT_CLASS"
	public ContentValues toValue(boolean hasId) {
		ContentValues value=new ContentValues();
		if(hasId) value.put("ID", this.getId());
		value.put("CODE", this.getCode());
		value.put("NAME", this.getName());
		value.put("STUDENT_CLASS", this.getStudentClass());
		return value;
	}
public AbstractEntity setDetail(TimeTableDBAdapter adapter) throws Exception {
		return adapter.getEntryById(this);
	}
}
