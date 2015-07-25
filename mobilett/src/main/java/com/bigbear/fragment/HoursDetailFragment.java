package com.bigbear.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigbear.common.Text;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.mobilett.MainActivity;
import com.bigbear.mobilett.R;
import com.bigbear.service.SubjectStudyClassService;

import java.util.Date;

/**
 * Hiển thị thông tin chi tiết về ngày học và môn học
 * @author luanvu
 */
public class HoursDetailFragment extends Fragment   {
	private static final String LOG_TAG="SUBJECT_DAY_FRAGMENT";
	private Date currentDateSelected;
    private SubjectStudyClassService service;
    private SubjectStudyClass entity;
	public HoursDetailFragment() {
        super();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.hours_detail_fragment, container,
				false);
        TextView subjectName=(TextView) rootView.findViewById(R.id.subjectName);
        TextView subjectClass=(TextView) rootView.findViewById(R.id.className);
        TextView subjectStudyDetail=(TextView) rootView.findViewById(R.id.subjectDetail);
        String subjectNameStr="";
        String subjectClassStr="";
        String subjectStudyDetailStr="";
        if(entity==null){
            // TODO
            Log.d(LOG_TAG, "Entity is null");
        }else {
            if(Text.isNullOrEmpty(entity.getSubjectClass().getSeminarClass())) {
                subjectNameStr = entity.getSubjectClass().getTheoryClass();
            }else{
                subjectNameStr = entity.getSubjectClass().getSeminarClass();
            }
            subjectClassStr=entity.getSubjectClass().getSubject().getSubjectName();
            subjectStudyDetailStr="Học tiết "+entity.getDayHours()+" tại "+entity.getDayLocations();
        }
        subjectClass.setText(subjectNameStr);
        subjectName.setText(subjectClassStr);
        subjectStudyDetail.setText(subjectStudyDetailStr);
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
            service=new SubjectStudyClassService(getActivity());
            Bundle bundle=getActivity().getIntent().getExtras();
			if(bundle==null){
				return;
			}
            currentDateSelected=TimeCommon.parseDate(bundle.getString(TimeTableFragment2.SELECTED_DATE), TimeCommon.FORMAT_DDMMYYYY);
            long ttId=bundle.getLong(TimeTableFragment2.TIMETABLE_TAG);
            long dayId=bundle.getLong(TimeTableFragment2.SUBJECT_CLASS_DAY_TAG);
            if(ttId==0 || dayId ==0){
                Log.e(LOG_TAG, "Timetable id or Day Id is zero");
                throw new Exception();
            }
            entity=service.getSubjectStudyClassDetailByDay(ttId, dayId, currentDateSelected);
		}catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            // TODO
			Log.e(LOG_TAG, e.getMessage(), e);
		}
	}

}
