package com.bigbear.fragment;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigbear.adapter.TimeTableDayitem;
import com.bigbear.common.SharedPreferenceUtil;
import com.bigbear.common.Text;
import com.bigbear.common.TimeCommon;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;
import com.bigbear.mobilett.HoursDetailActivity;
import com.bigbear.mobilett.R;
import com.bigbear.service.TimeTableService;
import com.bigbear.view.HoursTextView;
import com.thehayro.view.InfinitePagerAdapter;
import com.thehayro.view.InfiniteViewPager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeTableFragment2 extends Fragment implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = "TIME_TABLE_FRAGMENT";
    public static final String SUBJECT_CLASS_DAY_TAG = "SUBJECT_CLASS_DAY_TAG";
    public static final String TIMETABLE_TAG = "TIMETABLE_TAG";
    private static TimeTable ett;
    private long currentTTId;
    private DayAdapter dayAdapter;
    public static final String SELECTED_DATE = "SELECTED_DATE";
    private TimeTableService service;
    private Date currnetSelected;
    private HoursTextView[][] tvs;
    private List<SubjectStudyClass> subjectStudyClassList;

    public TimeTableFragment2() {
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
                Log.d(LOG_TAG, "Share preference empty " + e.getMessage());
                currnetSelected = new Date();
            }
        }
        View rootView = inflater.inflate(R.layout.fragment_time_table_activity2, container, false);
        subjectStudyClassList=new ArrayList<>();
        initTextViews(rootView);
        final InfiniteViewPager viewPager = (InfiniteViewPager) rootView.findViewById(R.id.infinite_viewpager);
        dayAdapter = new DayAdapter(currnetSelected);
        viewPager.setAdapter(dayAdapter);
        viewPager.setPageMargin(3);
        viewPager.setOnInfinitePageChangeListener(new InfiniteViewPager.OnInfinitePageChangeListener() {

            public void onPageScrolled(final Object indicator, final float positionOffset,
                                       final int positionOffsetPixels) {

            }

            public void onPageSelected(final Object indicator) {
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

    /**
     * Init textview
     * @param rootView
     */
    private void initTextViews(View rootView) {
        tvs = new HoursTextView[6][3];
        tvs[0][0] = (HoursTextView) rootView.findViewById(R.id.h1);
        tvs[0][1] = (HoursTextView) rootView.findViewById(R.id.s1);
        tvs[0][2] = (HoursTextView) rootView.findViewById(R.id.l1);

        tvs[1][0] = (HoursTextView) rootView.findViewById(R.id.h2);
        tvs[1][1] = (HoursTextView) rootView.findViewById(R.id.s2);
        tvs[1][2] = (HoursTextView) rootView.findViewById(R.id.l2);

        tvs[2][0] = (HoursTextView) rootView.findViewById(R.id.h3);
        tvs[2][1] = (HoursTextView) rootView.findViewById(R.id.s3);
        tvs[2][2] = (HoursTextView) rootView.findViewById(R.id.l3);

        tvs[3][0] = (HoursTextView) rootView.findViewById(R.id.h4);
        tvs[3][1] = (HoursTextView) rootView.findViewById(R.id.s4);
        tvs[3][2] = (HoursTextView) rootView.findViewById(R.id.l4);

        tvs[4][0] = (HoursTextView) rootView.findViewById(R.id.h5);
        tvs[4][1] = (HoursTextView) rootView.findViewById(R.id.s5);
        tvs[4][2] = (HoursTextView) rootView.findViewById(R.id.l5);

        tvs[5][0] = (HoursTextView) rootView.findViewById(R.id.h6);
        tvs[5][1] = (HoursTextView) rootView.findViewById(R.id.s6);
        tvs[5][2] = (HoursTextView) rootView.findViewById(R.id.l6);
    }

    /**
     * Hiệu ứng chuyển động cho view
     * @param myView
     */
    private void startAnim(View myView) {
        // TODO
//        ArcAnimator.createArcAnimator(myView, 100, 100, 100, Side.LEFT)
//                .setDuration(500)
//                .start();
    }

    // Click gridview item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startAnim(view);
        TimeTableDayitem item = (TimeTableDayitem) parent.getAdapter().getItem(position);
        if (item == null || item.getType() != TimeTableDayitem.SUB_NAME || Text.isEmpty(item.getDayId()))
            return;
        try {
            long idDay = Long.parseLong(item.getDayId());
            Intent intent = new Intent(getActivity(), HoursDetailActivity.class);
            intent.putExtra(SUBJECT_CLASS_DAY_TAG, idDay);
            intent.putExtra(TIMETABLE_TAG, currentTTId);
            intent.putExtra(SELECTED_DATE, TimeCommon.formatDate(dayAdapter.getSelectedDate(), TimeCommon.FORMAT_DDMMYYYY));
            startActivity(intent);

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
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
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
//            dateText = (HoursTextView) layout.findViewById(R.id.date);
            setDateTitle(indicator);
            layout.setTag(indicator);
            try {
                //fillData(indicator);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error: "+e.getMessage(), e);
            }
            return layout;
        }

        /**
         * Chèn data vào các text field
         * @param d Ngày hiện tại
         * @throws Exception
         */
        private void fillData(Date d) throws Exception {
            subjectStudyClassList.clear();
            subjectStudyClassList.addAll(service.getSubjectStudyClassOnDate(ett, d));
            for(SubjectStudyClass subjectStudyClass:subjectStudyClassList){
                /*if(SubjectStudyClass.DAY_1_2.equalsIgnoreCase(subjectStudyClass.getDayHours())){
                    tvs[0][1].setText(subjectStudyClass.getSubjectClass().getSubject().getSubjectName());
                    tvs[0][1].setSubjectStudyClass(subjectStudyClass);

                    tvs[0][2].setText(subjectStudyClass.getDayLocations());
                    continue;
                }*/
                /*if(SubjectStudyClass.DAY_3_4.equalsIgnoreCase(subjectStudyClass.getDayHours())){
                    tvs[1][1].setText(subjectStudyClass.getSubjectClass().getSubject().getSubjectName());
                    tvs[1][1].setSubjectStudyClass(subjectStudyClass);

                    tvs[1][2].setText(subjectStudyClass.getDayLocations());
                    continue;
                }
                if(SubjectStudyClass.DAY_5_6.equalsIgnoreCase(subjectStudyClass.getDayHours())){
                    tvs[2][1].setText(subjectStudyClass.getSubjectClass().getSubject().getSubjectName());
                    tvs[2][1].setSubjectStudyClass(subjectStudyClass);

                    tvs[2][2].setText(subjectStudyClass.getDayLocations());
                    continue;
                }
                if(SubjectStudyClass.DAY_7_8.equalsIgnoreCase(subjectStudyClass.getDayHours())){
                    tvs[3][1].setText(subjectStudyClass.getSubjectClass().getSubject().getSubjectName());
                    tvs[3][1].setSubjectStudyClass(subjectStudyClass);

                    tvs[3][2].setText(subjectStudyClass.getDayLocations());
                    continue;
                }
                if(SubjectStudyClass.DAY_9_10.equalsIgnoreCase(subjectStudyClass.getDayHours())){
                    tvs[4][1].setText(subjectStudyClass.getSubjectClass().getSubject().getSubjectName());
                    tvs[4][1].setSubjectStudyClass(subjectStudyClass);

                    tvs[4][2].setText(subjectStudyClass.getDayLocations());
                    continue;
                }
                if(SubjectStudyClass.DAY_11_12.equalsIgnoreCase(subjectStudyClass.getDayHours())){
                    tvs[5][1].setText(subjectStudyClass.getSubjectClass().getSubject().getSubjectName());
                    tvs[5][1].setSubjectStudyClass(subjectStudyClass);

                    tvs[5][2].setText(subjectStudyClass.getDayLocations());
                    continue;
                }*/
            }
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
            /*try {
                dateText.setText(TimeCommon.formatDate(d, TimeCommon.FORMAT_EDDMMYYYY));
            } catch (Exception e) {
                Log.e(LOG_TAG, "Format date fail", e);
                dateText.setText("Ngày nào năm đó");
            }*/
        }
    }
}
