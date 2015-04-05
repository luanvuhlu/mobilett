package com.bigbear.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigbear.common.TimeCommon;
import com.bigbear.mobilett.MainActivity;
import com.bigbear.mobilett.R;

import java.util.Date;

/**
 * Hiển thị thông tin chi tiết về ngày học và môn học
 * @author luanvu
 */
public class SubjectDayFragment extends Fragment   {
	private static final String LOG_TAG="SUBJECT_DAY_FRAGMENT";
	private Date currentDateSelected;
	public SubjectDayFragment() {
        super();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.subject_day_fragment, container,
				false);
        
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			if(getArguments()==null){
				return;
			}
            Log.i(LOG_TAG, getArguments().getString(TimeTableFragment.SELECTED_DATE));
		}catch(NullPointerException e){
			Log.e(LOG_TAG, e.getMessage(), e);
			// TODO
//			((MainActivity)getActivity()).onNavigationDrawerItemSelected(MainActivity.NAVIGATION_DRAWER_TIMETABLE_LIST);
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage(), e);
		}
	}
	private void setDateTitle(){
		try {
			MainActivity.setActionBar(TimeCommon.formatDate(currentDateSelected, TimeCommon.FORMAT_EDDMMYYYY), getActivity());
		} catch (Exception e) {
			Log.e(LOG_TAG, "Format date fail", e);
			MainActivity.setActionBar("Ngày nào năm đó", getActivity());
		}
	}
	
}
