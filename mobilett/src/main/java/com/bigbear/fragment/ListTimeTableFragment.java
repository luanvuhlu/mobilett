package com.bigbear.fragment;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appspot.hlutimetable.timetable.Timetable;
import com.appspot.hlutimetable.timetable.Timetable.TimetableOperations.GetTimeTable;
import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;
import com.bigbear.adapter.TimeTableAdapter;
import com.bigbear.db.TimeTableDbManager;
import com.bigbear.db.TimeTableEtt;
import com.bigbear.mobilett.AppConstants;
import com.bigbear.mobilett.MainActivity;
import com.bigbear.mobilett.MainActivity.PlaceholderFragment;
import com.bigbear.mobilett.R;
import com.gc.materialdesign.views.ButtonFloat;

/**
 * Hiện ra list các {@link com.appspot.hlutimetable.timetable.Timetable}
 * @author luanvu
 */
public class ListTimeTableFragment extends Fragment {
    private static final String LOG_TAG = "LIST_TIME_TABLE";
    public static final String TIMETABLE_ID_TAG = "TIMETABLE_ID_TAG";
    /**
     * Quét QR Code
     * <br />Bắt đầu từ {@link #scanQr()}
     * Lấy về kết quả từ {@link #onActivityResult(int, int, android.content.Intent)}
     */
    private static final int QR_CODE_SCAN = 100;
    private ButtonFloat addBtn;

    public ListTimeTableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_timetable_fragment,
                container, false);
        setList(rootView);
        addBtn = (ButtonFloat) rootView.findViewById(R.id.add);
        addBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                scanQr();
            }
        });
        return rootView;
    }

    /**
     * Hiện ra giao diện để quét QR Code
     */
    private void scanQr() {
        displayTimeTableFromRandomKey("8XHOHFU5OBXXSE1IEV0MIDOMNNI3QK");
//		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//         intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//         startActivityForResult(intent,QR_CODE_SCAN);
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg0 == QR_CODE_SCAN) {
            if (arg1 == Activity.RESULT_OK) {

                String contents = arg2.getStringExtra("SCAN_RESULT");
                @SuppressWarnings("unused")
                String format = arg2.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i("App", "Scan successful");
                Log.d("QR Code", contents);
                displayTimeTableFromRandomKey(contents);
            } else if (arg1 == Activity.RESULT_CANCELED) {
                Log.i("App", "Scan unsuccessful");
            }
        }

    }

    /**
     * Khởi tạo list Thời khóa biểu
     *
     * @param rootView
     */
    private void setList(View rootView) {
        final ListView lv = (ListView) rootView.findViewById(R.id.listTT);

        TimeTableEtt[] ettArrs = TimeTableDbManager.getAllEntries(getActivity());
        TimeTableAdapter timeTableAdapter = new TimeTableAdapter(
                getActivity(), ettArrs);

        lv.setAdapter(timeTableAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                timeTableClicked(parent, view, position, id);
            }
        });

    }

    /**
     * Sự kiện click và Thời khóa biểu trên list
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    private void timeTableClicked(AdapterView<?> parent, View view,
                                  int position, long id) {
        final TimeTableEtt item = (TimeTableEtt) parent
                .getItemAtPosition(position);
        FragmentManager fragmentManager = getActivity()
                .getSupportFragmentManager();
        Fragment fragment = PlaceholderFragment
                .newInstance(MainActivity.NAVIGATION_DRAWER_TIMETABLE);
        Bundle bundle = new Bundle();
        bundle.putLong(TIMETABLE_ID_TAG, item.getId());
        bundle.putInt(MainActivity.ARG_SECTION_NUMBER,
                MainActivity.NAVIGATION_DRAWER_TIMETABLE);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    /**
     * {@link android.os.AsyncTask} connect server để lấy về dữ liệu và insert vào database trên máy
     *
     * @param rdKey
     */
    private void displayTimeTableFromRandomKey(String rdKey) {
        AsyncTask<String, Void, TimeTableTimeTableResponse> getAndDisplayTimetable = new AsyncTask<String, Void, TimeTableTimeTableResponse>() {
            @Override
            protected TimeTableTimeTableResponse doInBackground(String... str) {
                Timetable apiServiceHandle = AppConstants.getApiServiceHandle();
                try {
                    Log.d(LOG_TAG, "Request with key: " + str[0]);
                    GetTimeTable getTimeTable = apiServiceHandle.timetable().getTimeTable(str[0]);
                    return getTimeTable.execute();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Exception during API call: " + e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(TimeTableTimeTableResponse timeTableResponse) {
                if (timeTableResponse != null) {
                    saveDb(timeTableResponse);
                    ((MainActivity) getActivity()).onNavigationDrawerItemSelected(MainActivity.NAVIGATION_DRAWER_TIMETABLE_LIST);
                } else {
                    Log.e(LOG_TAG, "No timetable were returned by the API.");
                }
            }
        };
        addBtn.setEnabled(false);
        getAndDisplayTimetable.execute(rdKey);
    }

    /**
     * Lưu Thời khóa biểu và database trên máy từ response của server
     *
     * @param timeTableResponses
     */
    private void saveDb(TimeTableTimeTableResponse... timeTableResponses) {
        if (timeTableResponses == null || timeTableResponses.length != 1) {
            Log.d(LOG_TAG, "TimeTable was not present");
            return;
        }
        boolean res = TimeTableDbManager.saveDb(timeTableResponses[0], getActivity());
        Log.i(LOG_TAG, "Insert db: " + (res ? "success" : "fail") + " !");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(
                    MainActivity.ARG_SECTION_NUMBER));
        } catch (NullPointerException e) {

        }
    }
}
