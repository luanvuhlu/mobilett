package com.bigbear.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.bigbear.common.Text;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.TimeTable;
import com.bigbear.service.TimeTableService;

/**
 * Danh sách các ô chứa trên grid
 * @author luanvu
 */
public class ListTimeTableDay {
	private static final String LOG_TAG = "LIST_TIMETABLE_DAY";
	public static List<TimeTableDayitem> getListTimeTableDayitem(TimeTable ett, Date d){
		List<TimeTableDayitem> hourItems=new ArrayList<>();
		ListHours hoursDetail=null;
        TimeTableService timeTableService=new TimeTableService();
		try {
			hoursDetail=timeTableService.getSubjectStudyOnDate(ett, d);

			if(hoursDetail.size() < 1){
				Log.i(LOG_TAG, "This day is freedom: "+TimeCommon.formatDateVnText(d));
			}
		}catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			
			return hourItems;
		}
		HoursEntity hours;
		int index=0;
        boolean isStudy;
        boolean isSeminar;
		for(int i=1; i < 12; i+=2){
			hours=hoursDetail.get(i+"-"+(i+1));
            isStudy=!Text.isNullOrEmpty(hours.getDayId());
            isSeminar=isStudy && HoursEntity.SEMINAR_TYPE.equals(hours.getClassType());

			hourItems.add(new TimeTableDayitem(1,  1, index++, hours.getHours(),
                    TimeTableDayitem.HOURS, isStudy, isSeminar));
			hourItems.add(new TimeTableDayitem(2,  1, index++, hours.getSubjectName(),
                    TimeTableDayitem.SUB_NAME, isStudy, isSeminar, hours.getDayId()));
			hourItems.add(new TimeTableDayitem(1,  1, index++, hours.getLocation(),
                    TimeTableDayitem.LOCAL, isStudy, isSeminar));
		}
		return hourItems;
	}
}
