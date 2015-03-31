package com.bigbear.db;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;

public class TimeTableDbManager {

	private static final String LOG_TAG = "TIMETABLE_DB_MANAGER";

	public static boolean saveDb(
			TimeTableTimeTableResponse timeTableResponse,
			Context context) {
		TimeTableDBAdapter adapter=null;
		try{
			adapter=new TimeTableDBAdapter(context);
			adapter.open();
			
			Log.d(LOG_TAG, "INSERTING STUDENT ");
			StudentEtt studentEtt=StudentEtt.getInstance(timeTableResponse.getStudent());
			studentEtt.setId(adapter.insertEntry(studentEtt));
			TimeTableEtt ett=TimeTableEtt.getInstance(timeTableResponse, studentEtt);
			Log.d(LOG_TAG, "INSERTING TIMETABLE of STUDENT "+studentEtt.getId());
			ett.setStudent(studentEtt);
			ett.setId(adapter.insertEntry(ett));
			
			for(SubjectClassEtt s:ett.getSubjectClass()){
				Log.d(LOG_TAG, "INSERTING SUBJECT "+s.getSubject().getSubjectName());
				s.getSubject().setId(adapter.insertEntry(s.getSubject()));
				Log.d(LOG_TAG, "INSERTING SUBJECT_CLASS "+s.getTheoryClass());
				s.setId(adapter.insertEntry(s));
				for(SubjectStudyClassEtt d:s.getSubjectStudyDay()){
					Log.d(LOG_TAG, "INSERTING SUBJECT_STUDY_CLASS "+d.getDayName());
					adapter.insertEntry(d);
				}
			}
			Log.d(LOG_TAG, "************DONE**************");
		}catch(Exception e){
			Log.e(LOG_TAG, "Error: "+e.getMessage(), e);
			return false;
		}finally{
			if(adapter!=null) adapter.close();
		}
		return true;		
	}
	public static TimeTableEtt[] getAllEntries(Context context){
		List<TimeTableEtt> etts = null;
		TimeTableEtt[] ettArrs = null;
		TimeTableDBAdapter adapter = null;
		try {
			adapter = new TimeTableDBAdapter(context);
			adapter.open();
			etts = TimeTableEtt.getAllEntries(adapter);
			if (etts == null) {
				Log.d(LOG_TAG, "Ett is null");
				return new TimeTableEtt[0];
			}
			ettArrs = new TimeTableEtt[etts.size()];
			etts.toArray(ettArrs);
		} catch (Exception e) {
			Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
			return new TimeTableEtt[0];
		} finally {
			if (adapter != null)
				adapter.close();
		}
		return ettArrs;
	}
	public static TimeTableEtt getTimeTable(long ttId, Context context) throws Exception{
		TimeTableDBAdapter adapter=null;
		TimeTableEtt ett=null;
		try{
			adapter=new TimeTableDBAdapter(context);
			adapter.open();
            ett=TimeTableEtt.getInstance(ttId);
            ett=(TimeTableEtt) adapter.getEntryById(ett);
			ett=(TimeTableEtt) ett.setDetail(adapter);
		}catch(NullPointerException e){
			throw new NullPointerException("Cannot find timetable "+ttId);
		}catch(Exception e){
			throw e;
		}finally{
			if(adapter!=null) adapter.close();
		}
		return ett;
	}
    public static TimeTableEtt getNewestTimeTable(Context context) throws Exception{
        TimeTableDBAdapter adapter=null;
        TimeTableEtt ett=null;
        try{
            adapter=new TimeTableDBAdapter(context);
            adapter.open();
            ett=TimeTableEtt.getInstance();
            ett.setOrder("CREATED_TIME DESC");
            adapter.getEntryNewest(ett);
            ett=(TimeTableEtt) ett.setDetail(adapter);
        }catch(NullPointerException e){
            throw new NullPointerException("Cannot get timetable ");
        }catch(Exception e){
            throw e;
        }finally{
            if(adapter!=null) adapter.close();
        }
        return ett;
    }
    // Chua chih xac
    public static SubjectClassEtt getSubjectClassFromDayId(long dayId, Context context) throws Exception{
        TimeTableDBAdapter adapter=null;
        SubjectStudyClassEtt subjectStudyClassEtt=null;
        SubjectClassEtt subjectClassEtt=null;
        try{
            adapter=new TimeTableDBAdapter(context);
            adapter.open();
            subjectStudyClassEtt=SubjectStudyClassEtt.getInstance(dayId);
            subjectStudyClassEtt=(SubjectStudyClassEtt) adapter.getEntryById(subjectStudyClassEtt);
            subjectClassEtt=SubjectClassEtt.getInstance(subjectStudyClassEtt.getSubjectClassId());
            subjectClassEtt=(SubjectClassEtt) adapter.getEntryById(subjectClassEtt);
            subjectClassEtt=(SubjectClassEtt) subjectClassEtt.setDetail(adapter);
        }catch(NullPointerException e){
            throw new NullPointerException("Cannot find day "+dayId);
        }catch(Exception e){
            throw e;
        }finally{
            if(adapter!=null) adapter.close();
        }
        return subjectClassEtt;
    }
}
