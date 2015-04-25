package com.bigbear.mobilett;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bigbear.adapter.DefaultListHoursAdapter;
import com.bigbear.adapter.ListTimeTableDay;
import com.bigbear.adapter.TimeTableDayitem;
import com.bigbear.common.SharedPreferenceUtil;
import com.bigbear.common.Text;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.TimeTable;
import com.bigbear.service.TimeTableService;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.thehayro.view.InfinitePagerAdapter;
import com.thehayro.view.InfiniteViewPager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Giao diện chính hiển thị thông tin thời khóa biểu
 *
 * @author luanvu
 */
public class TimeTableFragment extends Fragment implements OnItemClickListener {
    private static final String LOG_TAG = "TIME_TABLE_FRAGMENT";
    public static final String SUBJECT_CLASS_DAY_TAG = "SUBJECT_CLASS_DAY_TAG";
    public static final String TIMETABLE_TAG = "TIMETABLE_TAG";
    private static TimeTable ett;
    private long currentTTId;
    private DayAdapter dayAdapter;
    public static final String SUBJECT_CLASS_STUDY_ID = "SUBJECT_CLASS_STUDY_ID";
    public static final String SELECTED_DATE = "SELECTED_DATE";
    private TimeTableService service;
    private Date currnetSelected;

    public TimeTableFragment() {
        super();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            outState.putString(SELECTED_DATE, TimeCommon.formatDate(currnetSelected, TimeCommon.FORMAT_DDMMYYYY));
            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Format date error: " + e.getMessage(), e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            try {
                currnetSelected = TimeCommon.parseDate(savedInstanceState.getString(SELECTED_DATE), TimeCommon.FORMAT_DDMMYYYY);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Parse date error: " + e.getMessage(), e);
                currnetSelected = new Date();
            }
        } else {
            try {
                currnetSelected = SharedPreferenceUtil.getSelectedDate(getActivity());
            } catch (Exception e) {
                Log.e(LOG_TAG, "Share preference empty " + e.getMessage(), e);
                currnetSelected = new Date();
            }
        }
        View rootView = inflater.inflate(R.layout.timetable_fragment, container, false);
        final InfiniteViewPager viewPager = (InfiniteViewPager) rootView.findViewById(R.id.infinite_viewpager);
        dayAdapter = new DayAdapter(currnetSelected); // Lùi lại 0 ngày
        viewPager.setAdapter(dayAdapter);
        viewPager.setPageMargin(3);
        viewPager.setOnInfinitePageChangeListener(new InfiniteViewPager.OnInfinitePageChangeListener() {

            public void onPageScrolled(final Object indicator, final float positionOffset,
                                       final int positionOffsetPixels) {

            }

            public void onPageSelected(final Object indicator) {
                if (indicator instanceof Date && indicator != null) {
//                    dayAdapter.setDateTitle((Date) indicator);
                }
                currnetSelected = (Date) indicator;
                try {
                    SharedPreferenceUtil.putSelectedDate(currnetSelected, getActivity());
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Save current date error: " + e.getMessage(), e);
                }
            }

            public void onPageScrollStateChanged(final int state) {

            }
        });

        return rootView;
    }

    // Click gridview item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TimeTableDayitem item = (TimeTableDayitem) parent.getAdapter().getItem(position);
        if (item == null || item.getType() != TimeTableDayitem.SUB_NAME || Text.isEmpty(item.getDayId()))
            return;
        try {
            long idDay = Long.parseLong(item.getDayId());
            Intent intent=new Intent(getActivity(), HoursDetailActivity.class);
            intent.putExtra(SUBJECT_CLASS_DAY_TAG, idDay);
            intent.putExtra(TIMETABLE_TAG, currentTTId);
            intent.putExtra(SELECTED_DATE, TimeCommon.formatDate(dayAdapter.getSelectedDate(), TimeCommon.FORMAT_DDMMYYYY));
            startActivity(intent);
            /*FragmentManager fragmentManager = getActivity()
                    .getSupportFragmentManager();
            Fragment fragment = PlaceholderFragment
                    .newInstance(MainActivity.NAVIGATION_DRAWER_SUBJECT_STUDY_CLASS_DETAIL);
            Bundle bundle = fragment.getArguments();
            bundle.putLong(SUBJECT_CLASS_DAY_TAG, idDay);
            bundle.putLong(TIMETABLE_TAG, currentTTId);
            bundle.putString(SELECTED_DATE, TimeCommon.formatDate(dayAdapter.getSelectedDate(), TimeCommon.FORMAT_DDMMYYYY));
            bundle.putInt(MainActivity.ARG_SECTION_NUMBER,
                    MainActivity.NAVIGATION_DRAWER_SUBJECT_STUDY_CLASS_DETAIL);
            Log.i(LOG_TAG, "ID: " + idDay);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();*/
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Null cai gi day: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage(), e);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        service = new TimeTableService(getActivity());
        try {
            long ttId = SharedPreferenceUtil.getActiveTimeTable(getActivity());
            if (ttId == 0L) {
                ett = service.getNewest();
            } else {
                ett = service.findById(ttId);
            }
            currentTTId = ett.getId();
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            // TODO
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private void setHoursAdapter(Date d, final AsymmetricGridViewAdapter<TimeTableDayitem> asymmetricAdapter) {
        if (d == null) {
            Log.d(LOG_TAG, "Time is not valid");
            return;
        }
        asymmetricAdapter.appendItems(ListTimeTableDay.getListTimeTableDayitem(ett, d));
    }


    /**
     * Adapter
     */
    private class DayAdapter extends InfinitePagerAdapter<Date> {
        private TextView dateText;
        public DayAdapter(final Date initValue) {
            super(initValue);
//            setDateTitle(initValue);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @SuppressLint("InflateParams")
        @Override
        public ViewGroup instantiateItem(final Date indicator) {
            Log.d("InfiniteViewPager", "instantiating page " + indicator);
            final LinearLayout layout = (LinearLayout) ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.hours_item, null);
            dateText=(TextView)layout.findViewById(R.id.date);
            setDateTitle(indicator);
            layout.setTag(indicator);
            // Grid view
            final AsymmetricGridView listView = (AsymmetricGridView) layout.findViewById(R.id.listView);
            final ListAdapter adapter = new DefaultListHoursAdapter(getActivity(), listView, new ArrayList<TimeTableDayitem>());
            final AsymmetricGridViewAdapter<TimeTableDayitem> asymmetricAdapter = (AsymmetricGridViewAdapter<TimeTableDayitem>) adapter;
            setHoursAdapter(indicator, asymmetricAdapter);
            listView.setAdapter(adapter);
            listView.setRequestedColumnCount(4);
            listView.setDebugging(true);
            listView.setRequestedHorizontalSpacing(0);
            listView.setOnItemClickListener(TimeTableFragment.this);
            // End gridview

            return layout;
        }

        @Override
        public Date getNextIndicator() {
            return TimeCommon.changeDate(getCurrentIndicator(), 1);
        }

        @Override
        public Date getPreviousIndicator() {
            return TimeCommon.changeDate(getCurrentIndicator(), -1);
        }

        public Date getSelectedDate() {
            return getCurrentIndicator();
        }

        public void setDateTitle(Date d) {
            try {
                dateText.setText(TimeCommon.formatDate(d, TimeCommon.FORMAT_EDDMMYYYY));
            } catch (Exception e) {
                Log.e(LOG_TAG, "Format date fail", e);
                dateText.setText("Ngày nào năm đó");
            }
        }
    }
}
