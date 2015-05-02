package com.bigbear.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigbear.common.Text;
import com.bigbear.mobilett.R;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.util.List;

/**
 * Tương ứng cho mỗi ô trên grid Thời khóa biểu
 * @author luanvu
 */
public class DefaultListHoursAdapter extends AsymmetricGridViewAdapter<TimeTableDayitem> {

    public DefaultListHoursAdapter(final Context context, final AsymmetricGridView listView, final List<TimeTableDayitem> items) {
        super(context, listView, items);
    }
    @Override
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
    /**
     * Cho phép ô đó có khả năng được click hay không.
     * @param position
     * @return Nếu ô đó không phải ô {@link com.bigbear.adapter.TimeTableDayitem} SUB_NAME hoặc rỗng thì trả về false
     */
    @Override
    public boolean isEnabled(int position) {
        TimeTableDayitem item = getItem(position);
        return item.getType()==TimeTableDayitem.SUB_NAME && !Text.isEmpty(item.getText());
    }

}