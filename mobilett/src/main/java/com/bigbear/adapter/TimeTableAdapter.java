package com.bigbear.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigbear.common.SharedPreferenceUtil;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.TimeTable;
import com.bigbear.mobilett.R;

/**
 * Adapter cho danh sách Thời khóa biểu
 */
@SuppressLint("ViewHolder")
public class TimeTableAdapter extends ArrayAdapter<TimeTable>{
    private static final String LOG_TAG = "TimeTableAdapter";
    private final Context context;
	  private final TimeTable[] values;
    private long activeTimeTable;

	  public TimeTableAdapter(Context context, TimeTable[] values) {
	    super(context, R.layout.tt_item, values);
	    this.context = context;
	    this.values = values;
          try {
              activeTimeTable= SharedPreferenceUtil.getActiveTimeTable(context);
          } catch (Exception e) {
              Log.e(LOG_TAG, "TT Id is empty: "+e.getMessage(), e);
              activeTimeTable=0;
          }
      }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.tt_item, parent, false);
	    TextView label = (TextView) rowView.findViewById(R.id.label);
	    TextView time = (TextView) rowView.findViewById(R.id.createdTime);
        TimeTable item=values[position];
	    label.setText(item.getStudent().getName());
	    try{
	    	time.setText(TimeCommon.formatDate(item.getCreatedDate(), TimeCommon.FORMAT_HHMMSSEDDMMYYYY));
	    }catch(Exception e){
	    	time.setText(context.getResources().getString(R.string.unknown));
	    }
        if(activeTimeTable!=0 && activeTimeTable==item.getId()){
            rowView.setBackgroundColor(context.getResources().getColor(R.color.green));
            label.setTextColor(context.getResources().getColor(R.color.white));
            time.setTextColor(context.getResources().getColor(R.color.white));
        }
	    return rowView;
	  }
}
