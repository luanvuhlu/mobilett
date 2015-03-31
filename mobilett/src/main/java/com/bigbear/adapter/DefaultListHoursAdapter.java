package com.bigbear.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigbear.common.Text;
import com.bigbear.mobilett.R;
import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.util.List;

// Sample adapter implementation extending from AsymmetricGridViewAdapter<TimeTableDayitem>.
// This is the easiest way to get started.
public class DefaultListHoursAdapter extends AsymmetricGridViewAdapter<TimeTableDayitem> {

    public DefaultListHoursAdapter(final Context context, final AsymmetricGridView listView, final List<TimeTableDayitem> items) {
        super(context, listView, items);
    }

    @Override
    @SuppressWarnings("deprecation")
    public View getActualView(final int position, final View convertView, final ViewGroup parent) {
        TextView v;

        TimeTableDayitem item = getItem(position);

        if (convertView == null) {
            v = new TextView(context);
            v.setId(item.getPosition());
        } else {
            v = (TextView) convertView;
        }
        v.setText(Text.retSpace(item.getText()));
        v.setTextColor(context.getResources().getColor(R.color.black));
        v.setTextSize(context.getResources().getDimension(R.dimen.text_table_small_size));
        v.setGravity(Gravity.CENTER);
        v.setBackgroundColor(context.getResources().getColor(R.color.white));
        if(item.isStudy() && !item.isSeminar()){
            v.setBackgroundColor(context.getResources().getColor(R.color.light_green));
            v.setTextColor(context.getResources().getColor(R.color.white));
        }
        if(item.isSeminar()){
            v.setBackgroundColor(context.getResources().getColor(R.color.less_orange));
            v.setTextColor(context.getResources().getColor(R.color.white));
        }
        return v;
    }

    @Override
    public boolean isEnabled(int position) {
        TimeTableDayitem item = getItem(position);
        return item.getType()==TimeTableDayitem.SUB_NAME && !Text.isEmpty(item.getText());
    }
}