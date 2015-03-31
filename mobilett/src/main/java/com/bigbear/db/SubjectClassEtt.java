package com.bigbear.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectClassResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableSubjectStudyDayResponse;
import com.bigbear.common.TimeCommon;


public class SubjectClassEtt extends AbstractEntity {
	  private Date endDate;
	  private String seminarClass;
	  private Date startDate;
	  private SubjectEtt subject;
	  private List<SubjectStudyClassEtt> subjectStudyDay;
	  private String theoryClass;
	  private long timeTableId;
	  private static SubjectClassEtt instance=new SubjectClassEtt();
	public static final String TABLE_NAME="SUBJECT_CLASS";
	  public static final String[] COLUMNS={"ID", "TIMETABLE_ID", "SUBJECT_ID", "THEORY_CLASS", "SEMINAR_CLASS", "START_DATE", "END_DATE"};
	public static final String CREAT_TABLE_SQL = "CREATE TABLE SUBJECT_CLASS ( ID"
		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ " SUBJECT_ID INTEGER NOT NULL, "
		+ " TIMETABLE_ID INTEGER NOT NULL, "
		+ " THEORY_CLASS TEXT NOT NULL, "
		+ " SEMINAR_CLASS TEXT, "
		+ " START_DATE DATE, "
		+ " END_DATE DATE, "
		+ " FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT(ID), "
		+ " FOREIGN KEY (TIMETABLE_ID) REFERENCES TIMETABLE(ID)  ); ";
	private static final String LOG_TAG = "SUBJECT_CLASS_ETT";
	
	public SubjectClassEtt(TimeTableSubjectClassResponse res, long timeTableId) {
		this.endDate=TimeCommon.toDate(res.getEndDate());
		this.startDate=TimeCommon.toDate(res.getStartDate());
		this.theoryClass=res.getTheoryClass();
		this.seminarClass=res.getSeminarClass();
		this.subject=new SubjectEtt(res.getSubject());
		this.timeTableId=timeTableId;
		subjectStudyDay=new ArrayList<SubjectStudyClassEtt>();
		for(TimeTableSubjectStudyDayResponse d:res.getSubjectStudyDay()){
			subjectStudyDay.add(new SubjectStudyClassEtt(d, this.id));
		}
	}
	public static SubjectClassEtt getInstance(TimeTableSubjectClassResponse res, long timeTableId) {
		instance.endDate=TimeCommon.toDate(res.getEndDate());
		instance.startDate=TimeCommon.toDate(res.getStartDate());
		instance.theoryClass=res.getTheoryClass();
		instance.seminarClass=res.getSeminarClass();
		instance.subject=SubjectEtt.getInstance(res.getSubject());
		instance.timeTableId=timeTableId;
		instance.subjectStudyDay=new ArrayList<SubjectStudyClassEtt>();
		for(TimeTableSubjectStudyDayResponse d:res.getSubjectStudyDay()){
			instance.subjectStudyDay.add(SubjectStudyClassEtt.getInstance(d, 0));
		}
		return instance;
	}
	private SubjectClassEtt(long id){
		this.id=id;
	}
	public static SubjectClassEtt getInstance() {
		return instance;
	}
	public static SubjectClassEtt getInstance(long id) {
		return new SubjectClassEtt(id);
	}
	public SubjectClassEtt() {
		super();
	}
	public Date getEndDate() {
		return endDate;
	}
	public long getTimeTableId() {
		return timeTableId;
	}
	public void setTimeTableId(long timeTableId) {
		this.timeTableId = timeTableId;
	}
	
	public String getSeminarClass() {
		return seminarClass;
	}
	public Date getStartDate() {
		return startDate;
	}
	public SubjectEtt getSubject() {
		return subject;
	}
	public List<SubjectStudyClassEtt> getSubjectStudyDay() {
		return subjectStudyDay;
	}
	public String getTheoryClass() {
		return theoryClass;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setSeminarClass(String seminarClass) {
		this.seminarClass = seminarClass;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setSubject(SubjectEtt subject) {
		this.subject = subject;
	}
	public void setSubjectStudyDay(List<SubjectStudyClassEtt> subjectStudyDay) {
		this.subjectStudyDay = subjectStudyDay;
	}
	public void setTheoryClass(String theoryClass) {
		this.theoryClass = theoryClass;
	}
	@Override
	public void setId(long id) {
		super.setId(id);
		if(subjectStudyDay!=null){
			if(subjectStudyDay!=null){
				for(int i=0; i<subjectStudyDay.size(); i++){
					subjectStudyDay.get(i).setSubjectClassId(id);
					subjectStudyDay.set(i, subjectStudyDay.get(i));
				}
			}
		}
	}
	
	
	public ContentValues toValue(boolean hasId) throws Exception {
		ContentValues value=new ContentValues();
		if(hasId) value.put("ID", this.getId());
		value.put("SUBJECT_ID", this.getSubject().getId());
		value.put("TIMETABLE_ID", this.getTimeTableId());
		value.put("THEORY_CLASS", this.getTheoryClass());
		value.put("SEMINAR_CLASS", this.getSeminarClass());
		value.put("START_DATE", TimeCommon.formatDate(this.getStartDate()));
		value.put("END_DATE", TimeCommon.formatDate(this.getEndDate()));
		return value;
	}
	

	public final String getTableName() {
		return TABLE_NAME;
	}
	public final String getCreateTableQuery() {
		return CREAT_TABLE_SQL;
	}

	public final String[] getColumnNames() {
		return COLUMNS;
	}


	public void setValue(Cursor rs) {
		try{
			this.setId(rs.getLong(0));
			this.setTimeTableId(rs.getLong(1));
			this.setSubject(new SubjectEtt(rs.getLong(2)));
			this.setTheoryClass(rs.getString(3));
			this.setSeminarClass(rs.getString(4));
			this.setStartDate(TimeCommon.parseDate(rs.getString(5)));
			this.setEndDate(TimeCommon.parseDate(rs.getString(6)));
		}catch (Exception e){
			Log.d("SUBJECT_CLASS TABLE", "Set value error: "+e.getMessage());
		}
	}
public AbstractEntity setDetail(TimeTableDBAdapter adapter) throws Exception {
	if(subject ==null){
		throw new NullPointerException("Subject is null");
	}
	subject.setDetail(adapter);
	if(subjectStudyDay==null){
		setSubjectStudyDay(SubjectStudyClassEtt.getSubjectStudyClassFromTimeTable(this.id, adapter));
	}
	if(subjectStudyDay!=null){
		for(int i=0; i<subjectStudyDay.size(); i++){
			subjectStudyDay.set(i, (SubjectStudyClassEtt)subjectStudyDay.get(i).setDetail(adapter));
		}
	}
	return this;
		
	}
	public static List<SubjectClassEtt> getSubjectClassFromTimeTable(long id, TimeTableDBAdapter adapter) throws Exception{
		List<SubjectClassEtt> etts=new ArrayList<SubjectClassEtt>();
		SubjectClassEtt ett=new SubjectClassEtt();
		ett.setId(id);
		ett.setWhereQuery("TIMETABLE_ID="+id);
		Cursor cs=adapter.getEntry(ett);
		if(cs ==null){
			 Log.d(LOG_TAG, "No row !");
			 return etts;
		 }
		 if(cs.moveToFirst()){
			 do{
				 ett=new SubjectClassEtt();
				 ett.setValue(cs);
				 etts.add(ett);
			 }while(cs.moveToNext());
		 }else{
			 Log.d(LOG_TAG, "Cursor empty !");
		 }
		return etts;
	}
}
