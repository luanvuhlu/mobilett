package com.bigbear.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigbear.common.SharedPreferenceUtil;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.TimeTable;
import com.bigbear.fragment.ListTimeTableFragment;
import com.bigbear.mobilett.MainActivity;
import com.bigbear.mobilett.R;
import com.bigbear.mobilett.TimeTableActivity;
import com.bigbear.service.TimeTableService;
import com.gc.materialdesign.views.ButtonFlat;

import java.util.List;

/**
 * Adapter cho danh sách Thời khóa biểu
 */
@SuppressLint("ViewHolder")
public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder>{
    private static final String LOG_TAG = "TimeTableAdapter";
    private Context context;
	private List<TimeTable> values;
    private long activeTimeTable;
    private TimeTableService service;

	  public TimeTableAdapter(Context context, List<TimeTable> values, TimeTableService service) {
	    this.context = context;
	    this.values = values;
          this.service=service;
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.bindTimeTable(values.get(position));
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=service.delete(values.get(position).getId());
                if(id > 0) {
                    values.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, values.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
    public Context getContext() {
        return context;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private TimeTable timeTalbe;
        private TextView nameTv;
        private TextView timeTv;
        private ButtonFlat deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameTv=(TextView)itemView.findViewById(R.id.name);
            timeTv=(TextView)itemView.findViewById(R.id.createdTime);
            deleteBtn=(ButtonFlat)itemView.findViewById(R.id.deleteBtn);
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
            /*Intent intent=new Intent(v.getContext(), TimeTableActivity.class);
            intent.putExtra(ListTimeTableFragment.TIMETABLE_ID_TAG, timeTalbe.getId());
            intent.putExtra(MainActivity.ARG_SECTION_NUMBER,
                    MainActivity.NAVIGATION_DRAWER_TIMETABLE);
            v.getContext().startActivity(intent);*/
        }
    }
}
