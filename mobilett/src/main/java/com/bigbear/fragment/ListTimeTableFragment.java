package com.bigbear.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appspot.hlutimetable.timetable.Timetable;
import com.appspot.hlutimetable.timetable.Timetable.TimetableOperations.GetTimeTable;
import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;
import com.bigbear.adapter.TimeTableAdapter;
import com.bigbear.entity.TimeTable;
import com.bigbear.mobilett.AppConstants;
import com.bigbear.mobilett.R;
import com.bigbear.mobilett.ScannerActivity;
import com.bigbear.service.TimeTableService;
import com.gc.materialdesign.views.ButtonFloat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hiện ra list các {@link com.appspot.hlutimetable.timetable.Timetable}
 *
 * @author luanvu
 */
public class ListTimeTableFragment extends Fragment {
    private static final String LOG_TAG = "LIST_TIME_TABLE";
    public static final String TIMETABLE_ID_TAG = "TIMETABLE_ID_TAG";
    private TimeTableService service;
    private RecyclerView mRecyclerView;
    private TimeTableAdapter timeTableAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TimeTable> timeTableList;

    /**
     * Quét QR Code
     * <br />Bắt đầu từ {@link #scanQr()}
     * Lấy về kết quả từ {@link #onActivityResult(int, int, android.content.Intent)}
     */
    private static final int QR_CODE_SCAN = 100;
    private ButtonFloat addBtn;

    public ListTimeTableFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_timetable_fragment,
                container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listTT);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        setListItems();
        setTimeTableAdapter();
        addBtn = (ButtonFloat) rootView.findViewById(R.id.add);
        addBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
//                ArcAnimator.createArcAnimator(addBtn, 100, 100, 90, Side.LEFT)
//                        .setDuration(500)
//                        .start();
                scanQr();
            }
        });
        return rootView;
    }

    private void setTimeTableAdapter() {
        timeTableAdapter = new TimeTableAdapter(getActivity(), timeTableList, service);
        mRecyclerView.setAdapter(timeTableAdapter);
    }

    /**
     * Hiện ra giao diện để quét QR Code
     */
    private void scanQr() {
        displayTimeTableFromRandomKey("NFPYXK53U69Q4DMQA7RW4MMHFIN2HM");
//        Intent intent=new Intent(getActivity(), ScannerActivity.class);
//        startActivityForResult(intent, QR_CODE_SCAN);
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg0 == QR_CODE_SCAN) {
            if (arg1 == Activity.RESULT_OK && arg2.getExtras() != null) {
                String contents = arg2.getStringExtra(ScannerActivity.SCAN_RESULT_KEY);
                @SuppressWarnings("unused")
                String format = arg2.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i(LOG_TAG, "Scan successful");
                Log.d(LOG_TAG, contents);
                displayTimeTableFromRandomKey(contents);
            } else if (arg1 == Activity.RESULT_CANCELED) {
                Log.i("App", "Scan unsuccessful");
            }
        }

    }

    /**
     * Khởi tạo list Thời khóa biểu
     */
    private void setListItems() {
        try {
            timeTableList = service.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            timeTableList = new ArrayList<>();
        }
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
                boolean success = false;
                long id = 0;
                if (timeTableResponse != null && (id = saveDb(timeTableResponse)) > 0) {
                    success = true;
                    try {
                        timeTableList.add(service.findById(id));
                        setTimeTableAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                        success = false;
                    }
                } else {
                    success = false;
                    Log.e(LOG_TAG, "No timetable were returned by the API.");
                }
                showMessge(success ? "Success" : "Fail");
                addBtn.setEnabled(true);
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
    private long saveDb(TimeTableTimeTableResponse... timeTableResponses) {
        if (timeTableResponses == null || timeTableResponses.length != 1) {
            Log.d(LOG_TAG, "TimeTable was not present");
            return 0;
        }
        long id = service.save(timeTableResponses[0]);
        Log.i(LOG_TAG, "Insert db ID: " + id);
        return id;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        service = new TimeTableService(getActivity());
    }

    private void showMessge(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }
}
