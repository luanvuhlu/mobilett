package com.bigbear.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectClassResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;
import com.bigbear.adapter.HoursEntity;
import com.bigbear.adapter.ListHours;
import com.bigbear.common.TimeCommon;
import com.bigbear.common.Validate;

public class TimeTableEtt extends AbstractEntity implements Cloneable {
	  private String semester;
	  private StudentEtt student;
	  private List<SubjectClassEtt> subjectClass;
	  private String year;
	  private Date createdDate;
	  public static final String TABLE_NAME="TIMETABLE";
	private static final String LOG_TAG="TABLE TIMETABLE";
	public static final String[] COLUMNS={"ID", "STUDENT_ID", "SEMESTER", "YEAR", "CREATED_TIME"};
	public static final String CREATE_TABLE_SQL = "CREATE TABLE TIMETABLE ( ID"
		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ " STUDENT_ID INTEGER NOT NULL, "
		+ " SEMESTER TEXT, "
		+ " YEAR TEXT, "
		+ " CREATED_TIME REAL DEFAULT (datetime('now','localtime')), "
		+ " FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(ID)  ); ";
	private static TimeTableEtt instance=new TimeTableEtt();
	private TimeTableEtt() {
	}
	public static TimeTableEtt getInstance() {
		return instance;
	}
	private TimeTableEtt(long id) {
		super(id);
	}
	public static TimeTableEtt getInstance(long id){
		instance=new TimeTableEtt(id);
		return instance;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public static TimeTableEtt getInstance (TimeTableTimeTableResponse res, StudentEtt studentEtt) {
		instance.semester=res.getSemester();
		instance.year=res.getYear();
		instance.student=studentEtt;
		instance.subjectClass=new ArrayList<SubjectClassEtt>();
		for(TimeTableSubjectClassResponse s:res.getSubjectClass()){
			instance.subjectClass.add(new SubjectClassEtt(s, 0));
		}
		return instance;
	}
	public String getSemester() {
		return semester;
	}
	public StudentEtt getStudent() {
		return student;
	}
	public List<SubjectClassEtt> getSubjectClass() {
		return subjectClass;
	}
	public String getYear() {
		return year;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public void setStudent(StudentEtt student) {
		this.student = student;
	}
	public void setSubjectClass(List<SubjectClassEtt> subjectClass) {
		this.subjectClass = subjectClass;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	@Override
	public void setId(long id) {
		super.setId(id);
		if(subjectClass!=null){
			for(int i=0; i<subjectClass.size(); i++){
				subjectClass.get(i).setTimeTableId(id);
				subjectClass.set(i, subjectClass.get(i));
			}
		}
	}
	
	
//	"ID", "STUDENT_ID", "SEMESTER", "YEAR"
	public ContentValues toValue(boolean hasId) {
		ContentValues value=new ContentValues();
		if(hasId) value.put("ID", this.getId());
		value.put("STUDENT_ID", this.getStudent().getId());
		value.put("SEMESTER", this.getSemester());
		value.put("YEAR", this.getYear());
		return value;
	}
	

	public final String getTableName() {
		return TABLE_NAME;
	}
//	"ID", "STUDENT_ID", "SEMESTER", "YEAR"
	public final String getCreateTableQuery() {
		return CREATE_TABLE_SQL;
	}

	public final String[] getColumnNames() {
		return COLUMNS;
	}

//	"ID", "STUDENT_ID", "SEMESTER", "YEAR"
	public void setValue(Cursor rs) {
		try{
			this.setId(rs.getLong(0));
			this.setStudent(StudentEtt.getInstance(rs.getLong(1)));
			this.setSemester(Validate.repNullCursor(2, rs));
			this.setYear(Validate.repNullCursor(3, rs));
			this.setCreatedDate(TimeCommon.parseDate(Validate.repNullCursor(4, rs), TimeCommon.DEFAULT_CURSOR_DATE));
		}catch(Exception e){
			Log.e(LOG_TAG, "ERROR: "+e.getMessage(), e);
		}
	}
	public static List<TimeTableEtt> getAllEntries(TimeTableDBAdapter adapter) throws Exception{
		TimeTableEtt ett=null;
		 List<TimeTableEtt> etts=new ArrayList<TimeTableEtt>();
		 Cursor cs=adapter.getAllEntries(TABLE_NAME, COLUMNS);
		 if(cs ==null){
			 Log.d(LOG_TAG, "No row !");
			 return etts;
		 }
		 if(cs.moveToFirst()){
			 do{
				 ett=new TimeTableEtt();
				 ett.setValue(cs);
				 ett.setStudent((StudentEtt)adapter.getEntryById(ett.getStudent()));
				 etts.add(ett);
			 }while(cs.moveToNext());
		 }else{
			 Log.d(LOG_TAG, "Cursor empty !");
		 }
		 return etts;
	}
	public AbstractEntity setDetail(TimeTableDBAdapter adapter) throws Exception {
		this.setStudent((StudentEtt)getStudent().setDetail(adapter));
		
		if(subjectClass==null){
			setSubjectClass(SubjectClassEtt.getSubjectClassFromTimeTable(this.id, adapter));
		}
		if(subjectClass!=null){
			for(int i=0; i<subjectClass.size(); i++){
				subjectClass.set(i, (SubjectClassEtt)(subjectClass.get(i).setDetail(adapter)));
			}
		}
		return this;
	}
	public ListHours getSubjectStudyOnDate(Date d) throws Exception{
		if(	d==null){
			throw new NullPointerException("Time is not valid");
		}
		ListHours lsHoursAdapter=new ListHours();
		
//		List<HoursEntity> ls=new ArrayList<HoursEntity>();
		if(getSubjectClass()==null){
			throw new Exception("Subject Class List is null");
		}
		for(SubjectClassEtt sc:getSubjectClass()){
			if(sc==null){
				throw new Exception("Subject Class is null");
			}
			if(sc.getSubject()==null){
				throw new NullPointerException("Subject is null");
			}
			if(sc.getSubjectStudyDay()==null){
				throw new NullPointerException("Subject Study Day is null: "+sc.getId());
			}
			
			if(sc.getStartDate()==null || sc.getEndDate()==null){
				throw new NullPointerException("Start date and End date is null in ID: "+sc.getId());
			}
			if(TimeCommon.compareDate(d, sc.getStartDate()) == TimeCommon.LT ||
					TimeCommon.compareDate(sc.getEndDate(), d) == TimeCommon.LT){
				continue;
			}
			
			for(SubjectStudyClassEtt day:sc.getSubjectStudyDay()){
				if(!(TimeCommon.getDayOfWeek(d)+"").equals(day.getDayName())){
					continue;
				}
				lsHoursAdapter.add(new HoursEntity(day.getDayHours(), sc.getSubject().getSubjectName() , day.getDayLocations(),day.getClassType(), day.getId()+""));
			}
			
		}
		return lsHoursAdapter;
	}
	protected TimeTableEtt clone() throws CloneNotSupportedException {
		return (TimeTableEtt)super.clone();
	}
}
