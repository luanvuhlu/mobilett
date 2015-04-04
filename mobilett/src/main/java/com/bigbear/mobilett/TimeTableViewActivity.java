package com.bigbear.mobilett;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.bigbear.adapter.DefaultListHoursAdapter;
import com.bigbear.adapter.ListTimeTableDay;
import com.bigbear.adapter.TimeTableDayitem;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.TimeTable;
import com.bigbear.service.TimeTableService;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.thehayro.view.InfinitePagerAdapter;
import com.thehayro.view.InfiniteViewPager;

import java.util.ArrayList;
import java.util.Date;


public class TimeTableViewActivity extends ActionBarActivity implements OnItemClickListener {
    private CharSequence mTitle;
    private static final String LOG_TAG="TIME_TABLE_FRAGMENT";
    public static final String TIMETABLE_ID_TAG = "TIMETABLE_ID_TAG";
    private static TimeTable ett;
    private DayAdapter dayAdapter;
    public static final String SUBJECT_CLASS_STUDY_ID = "SUBJECT_CLASS_STUDY_ID";
    public static final String SELECTED_DATE = "SELECTED_DATE";
    private TimeTableService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_view);
        // TODO get ID timetable
        service=new TimeTableService(this);
        mTitle = getTitle();
        initTimetableEtt();
        final InfiniteViewPager viewPager = (InfiniteViewPager) findViewById(R.id.infinite_viewpager);
        viewPager.setAdapter(new DayAdapter(new Date()));
        viewPager.setPageMargin(1);
        viewPager.setOnInfinitePageChangeListener(new InfiniteViewPager.OnInfinitePageChangeListener() {

            public void onPageScrolled(final Object indicator,final float positionOffset,
                                       final int positionOffsetPixels) {
            }

            public void onPageSelected(final Object indicator) {
                if(indicator instanceof Date && indicator !=null) {
                    setDateTitle((Date)indicator);
                }
            }

            public void onPageScrollStateChanged(final int state) {

            }
        });
    }
    private void initTimetableEtt(){
        try{
            Intent intent=getIntent();
            long ttId = intent.getLongExtra(TIMETABLE_ID_TAG, 0);
            if(ttId==0){
                ett=service.getNewest();
            }else {
                ett = service.findById(ttId);
            }
        }catch(NullPointerException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    // Click gridview item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Log.i(LOG_TAG, "Item clicked !");
        TimeTableDayitem item=(TimeTableDayitem)parent.getAdapter().getItem(position);
        // TODO
    }

    private void setDateTitle(Date d){
        try {
            setActionBarTitle(TimeCommon.formatDate(d, TimeCommon.FORMAT_EDDMMYYYY));
        } catch (Exception e) {
            Log.e(LOG_TAG, "Format date fail", e);
            setActionBarTitle("Ngày nào năm đó");
        }
    }

    private void setHoursAdapter(Date d,
                                 final AsymmetricGridView listView,
                                 final AsymmetricGridViewAdapter<TimeTableDayitem> asymmetricAdapter){
        if(	d==null){
            Log.d(LOG_TAG,"Time is not valid");
            return;
        }
        asymmetricAdapter.appendItems(ListTimeTableDay.getListTimeTableDayitem(ett, d));
    }


    /** Adapter */
    private class DayAdapter extends InfinitePagerAdapter<Date> {
        public DayAdapter(final Date initValue) {
            super(initValue);
            setDateTitle(initValue);
        }

        @Override
        public ViewGroup instantiateItem(final Date indicator) {
            Log.d("InfiniteViewPager", "instantiating page " + indicator);
            final LinearLayout layout = (LinearLayout) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.timetable_day_item, null);
//            final TextView text = (TextView) layout.findViewById(R.id.moving_view_x);
//            text.setText(String.format("Page %s", indicator));
//            Log.i("InfiniteViewPager", String.format("textView.text() == %s", text.getText()));
            layout.setTag(indicator);

            // Grid view
            final AsymmetricGridView listView = (AsymmetricGridView) layout.findViewById(R.id.listView);
            final ListAdapter adapter = new DefaultListHoursAdapter(TimeTableViewActivity.this, listView, new ArrayList<TimeTableDayitem>());
            final AsymmetricGridViewAdapter<TimeTableDayitem> asymmetricAdapter= (AsymmetricGridViewAdapter<TimeTableDayitem>) adapter;
            setHoursAdapter(indicator,listView, asymmetricAdapter);
            listView.setAdapter(adapter);
            listView.setRequestedColumnCount(4);
            listView.setDebugging(true);
            listView.setRequestedHorizontalSpacing(0);
            listView.setOnItemClickListener(TimeTableViewActivity.this);
            // End gridview

            return layout;
        }
        @Override
        public Date getNextIndicator() {
            return TimeCommon.addDate(getCurrentIndicator(), 1);
        }

        @Override
        public Date getPreviousIndicator() {
            return TimeCommon.addDate(getCurrentIndicator(), -1);
        }

        public Date getSelectedDate() {
            return getCurrentIndicator();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_time_table_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setActionBarTitle(String title){
        mTitle=title;
        getSupportActionBar().setTitle(mTitle);
    }
}
