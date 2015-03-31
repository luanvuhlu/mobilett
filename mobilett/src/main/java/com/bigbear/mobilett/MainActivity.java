package com.bigbear.mobilett;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bigbear.common.TimeCommon;
import com.bigbear.fragment.ListTimeTableFragment;
import com.bigbear.fragment.NavigationDrawerFragment;
import com.bigbear.fragment.SubjectDayFragment;
import com.bigbear.fragment.TimeTableFragment;

import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Main";
	public static final int NAVIGATION_DRAWER_SUBJECT_DAY_DETAIL=-1;
	public static final int NAVIGATION_DRAWER_TIMETABLE=0;
	public static final int NAVIGATION_DRAWER_TIMETABLE_LIST=1;
	public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }

    /**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */

	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private void initConfig(){
		TimeCommon.locale=getResources().getConfiguration().locale;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initConfig();
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	public void onNavigationDrawerItemSelected(int position) {
        getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position)).commit();
	}
	
	public void onSectionAttached(int number) {
		switch (number) {
		case NAVIGATION_DRAWER_TIMETABLE:
			mTitle = getString(R.string.title_view);
			break;
		case NAVIGATION_DRAWER_TIMETABLE_LIST:
			mTitle = getString(R.string.list_tt_header);
			break;
		default:
			mTitle = getString(R.string.app_name);
		}
	}
	public void setActionBarTitle(String title){
		mTitle=title;
		getSupportActionBar().setTitle(mTitle);
	}
	public static void setActionBar(String title, Context context){
		((MainActivity) context).setActionBarTitle(title);
	}
	@SuppressWarnings("deprecation")
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		public static Fragment newInstance(int sectionNumber) {
			Fragment fragment =null;
			switch (sectionNumber) {
			case NAVIGATION_DRAWER_TIMETABLE_LIST:
				fragment=new ListTimeTableFragment();
				break;
			case NAVIGATION_DRAWER_TIMETABLE:
				fragment=new TimeTableFragment();
				break;
			case NAVIGATION_DRAWER_SUBJECT_DAY_DETAIL:
                fragment=new SubjectDayFragment();
                break;
			default:
				fragment=new TimeTableFragment();
				break;
			}
			Bundle bundle = new Bundle();
			bundle.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(bundle);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.timetable_detail_fragment, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached((getArguments()==null)?NAVIGATION_DRAWER_TIMETABLE:getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
}
