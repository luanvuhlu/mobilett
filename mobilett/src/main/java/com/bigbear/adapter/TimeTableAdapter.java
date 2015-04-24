package com.bigbear.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigbear.common.SharedPreferenceUtil;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.TimeTable;
import com.bigbear.fragment.ListTimeTableFragment;
import com.bigbear.mobilett.MainActivity;
import com.bigbear.mobilett.R;

/**
 * Adapter cho danh sách Thời khóa biểu
 */
@SuppressLint("ViewHolder")
public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder>{
    private static final String LOG_TAG = "TimeTableAdapter";
    private Context context;
	private TimeTable[] values;
    private long activeTimeTable;

	  public TimeTableAdapter(Context context, TimeTable[] values) {
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
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tt_item, viewGroup, false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bindTimeTable(values[i]);
    }

    @Override
    public int getItemCount() {
        return values.length;
    }
    public Context getContext() {
        return context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private TimeTable timeTalbe;
        private final TextView nameTv;
        private final TextView timeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameTv=(TextView)itemView.findViewById(R.id.name);
            timeTv=(TextView)itemView.findViewById(R.id.createdTime);
        }
        public void bindTimeTable(TimeTable timeTalbe){
            this.timeTalbe=timeTalbe;
            nameTv.setText(timeTalbe.getStudent().getName());
            try {
                timeTv.setText(TimeCommon.formatDate(timeTalbe.getCreatedDate(), TimeCommon.FORMAT_HHMMSSEDDMMYYYY));
            } catch (Exception e) {
                e.printStackTrace();
                timeTv.setText(itemView.getContext().getResources().getString(R.string.unknown));
            }
        }
        @Override
        public void onClick(View v) {
            try {
                SharedPreferenceUtil.putActiveTimeTable(timeTalbe.getId(), v.getContext());
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception save timetable id: "+e.getMessage(), e);
            }
            FragmentManager fragmentManager = ((MainActivity)v.getContext()).getSupportFragmentManager();
            Fragment fragment = MainActivity.PlaceholderFragment
                    .newInstance(MainActivity.NAVIGATION_DRAWER_TIMETABLE);
            Bundle bundle = new Bundle();
            bundle.putLong(ListTimeTableFragment.TIMETABLE_ID_TAG, timeTalbe.getId());
            bundle.putInt(MainActivity.ARG_SECTION_NUMBER,
                    MainActivity.NAVIGATION_DRAWER_TIMETABLE);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

	  /*@Override
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
	  }*/

}
