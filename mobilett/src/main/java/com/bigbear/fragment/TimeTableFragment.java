package com.bigbear.fragment;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.bigbear.adapter.DefaultListHoursAdapter;
import com.bigbear.adapter.ListTimeTableDay;
import com.bigbear.adapter.TimeTableDayitem;
import com.bigbear.common.Text;
import com.bigbear.common.TimeCommon;
import com.bigbear.db.SubjectClassEtt;
import com.bigbear.db.TimeTableDbManager;
import com.bigbear.db.TimeTableEtt;
import com.bigbear.mobilett.MainActivity;   
import com.bigbear.mobilett.R;
import com.bigbear.mobilett.MainActivity.PlaceholderFragment;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.thehayro.view.InfinitePagerAdapter;
import com.thehayro.view.InfiniteViewPager;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Giao diện chính hiển thị thông tin thời khóa biểu
 * @author luanvu
 */
public class TimeTableFragment extends Fragment  implements OnItemClickListener  {
	private static final String LOG_TAG="TIME_TABLE_FRAGMENT";
    private static final String SUBJECT_CLASS_DAY_TAG = "SUBJECT_CLASS_DAY_TAG";
    private static final String TIMETABLE_TAG = "TIMETABLE_TAG";
    private static TimeTableEtt ett;
    private long currentTTId;
    private DayAdapter dayAdapter;
	public static final String SUBJECT_CLASS_STUDY_ID = "SUBJECT_CLASS_STUDY_ID";
	public static final String SELECTED_DATE = "SELECTED_DATE";
	public TimeTableFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.timetable_detail_fragment, container, false);
		final InfiniteViewPager viewPager = (InfiniteViewPager) rootView.findViewById(R.id.infinite_viewpager);
        dayAdapter=new DayAdapter(TimeCommon.addDate(new Date(), -3)); // Lùi lại 3 ngày
        viewPager.setAdapter(dayAdapter);
        viewPager.setPageMargin(3);
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
        
		return rootView;
	}
	// Click gridview item
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TimeTableDayitem item=(TimeTableDayitem)parent.getAdapter().getItem(position);
        Log.i(LOG_TAG, "Item day id: "+item.getDayId());
        if(item==null || item.getType()!=TimeTableDayitem.SUB_NAME || Text.isEmpty(item.getDayId())) return;
        try{
            long idDay=Long.parseLong(item.getDayId());
            FragmentManager fragmentManager = getActivity()
                    .getSupportFragmentManager();
            Fragment fragment = PlaceholderFragment
                    .newInstance(MainActivity.NAVIGATION_DRAWER_SUBJECT_DAY_DETAIL);
            Bundle bundle = fragment.getArguments();
            bundle.putLong(SUBJECT_CLASS_DAY_TAG, idDay);
            bundle.putLong(TIMETABLE_TAG, currentTTId);
            bundle.putString(SELECTED_DATE, TimeCommon.formatDate(dayAdapter.getSelectedDate(), TimeCommon.FORMAT_DDMMYYYY));
            bundle.putInt(MainActivity.ARG_SECTION_NUMBER,
                    MainActivity.NAVIGATION_DRAWER_SUBJECT_DAY_DETAIL);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }catch (NumberFormatException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			if(getArguments()==null){
				return;
			}
			long ttId = getArguments().getLong(ListTimeTableFragment.TIMETABLE_ID_TAG);
            if(ttId==0L){
                ett = TimeTableDbManager.getNewestTimeTable(getActivity());
            }else {
                ett = TimeTableDbManager.getTimeTable(ttId, getActivity());
            }
            currentTTId=ett.getId();
		}catch(NullPointerException e){
			Log.e(LOG_TAG, e.getMessage(), e);
            goListPage();
			// TODO
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage(), e);
            goListPage();
		}
	}

    private void goListPage(){
        ((MainActivity)getActivity()).onNavigationDrawerItemSelected(MainActivity.NAVIGATION_DRAWER_TIMETABLE_LIST);
    }
	private void setDateTitle(Date d){
		try {
			MainActivity.setActionBar(TimeCommon.formatDate(d, TimeCommon.FORMAT_EDDMMYYYY), getActivity());
		} catch (Exception e) {
			Log.e(LOG_TAG, "Format date fail", e);
			MainActivity.setActionBar("Ngày nào năm đó", getActivity());
		}
	}

    private void setHoursAdapter(Date d, final AsymmetricGridViewAdapter<TimeTableDayitem> asymmetricAdapter){
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
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
		@SuppressLint("InflateParams")
		@Override
        public ViewGroup instantiateItem(final Date indicator) {
            Log.d("InfiniteViewPager", "instantiating page " + indicator);
            final LinearLayout layout = (LinearLayout) ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.timetable_day_item, null);

            layout.setTag(indicator);
            // Grid view
            final AsymmetricGridView listView = (AsymmetricGridView) layout.findViewById(R.id.listView);
            final ListAdapter adapter = new DefaultListHoursAdapter(getActivity(), listView, new ArrayList<TimeTableDayitem>());
            final AsymmetricGridViewAdapter<TimeTableDayitem> asymmetricAdapter= (AsymmetricGridViewAdapter<TimeTableDayitem>) adapter;
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
}
