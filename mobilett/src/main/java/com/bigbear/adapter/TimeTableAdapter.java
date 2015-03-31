package com.bigbear.adapter;

import com.bigbear.common.TimeCommon;
import com.bigbear.db.TimeTableEtt;
import com.bigbear.mobilett.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class TimeTableAdapter extends ArrayAdapter<TimeTableEtt>{
	private final Context context;
	  private final TimeTableEtt[] values;

	  public TimeTableAdapter(Context context, TimeTableEtt[] values) {
	    super(context, R.layout.tt_item, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.tt_item, parent, false);
	    TextView label = (TextView) rowView.findViewById(R.id.label);
	    TextView time = (TextView) rowView.findViewById(R.id.createdTime);
	    label.setText(values[position].getStudent().getName());
	    try{
	    	time.setText(TimeCommon.formatDate(values[position].getCreatedDate(), TimeCommon.FORMAT_HHMMSSEDDMMYYYY));
	    }catch(Exception e){
	    	time.setText(context.getResources().getString(R.string.unknown));
	    }
	    return rowView;
	  }
}
