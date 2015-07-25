package com.bigbear.mobilett;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bigbear.common.SharedPreferenceUtil;
import com.bigbear.common.Text;
import com.bigbear.common.TimeCommon;
import com.bigbear.fragment.TimeTableFragment2;

import java.util.Calendar;
import java.util.Date;


public class TimeTableActivity2 extends AppCompatActivity {

    private static final String LOG_TAG = "TimeTableActivity2";
    private final String UNKNOWN_DATE="Không rõ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_activity2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (savedInstanceState == null) {
            replaceMainFragment(new TimeTableFragment2());
        }
    }

    private void replaceMainFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.jumpDay:
                // TODO

                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateChanged(year, monthOfYear, dayOfMonth);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),  c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void dateChanged(int y, int m, int d){
        Calendar c = Calendar.getInstance();
        c.set(y, m, d);
        String dateStr=null;
        try {
            dateStr= TimeCommon.formatDate(c.getTime(), TimeCommon.FORMAT_DDMMYYYY);
            SharedPreferenceUtil.putSelectedDate(dateStr, this);
            Log.d(LOG_TAG, "Date selected: "+dateStr);
        } catch (Exception e) {
            Toast.makeText(this, "Selected Date error!", Toast.LENGTH_LONG).show();
            Log.e(LOG_TAG, "Parse date error: "+e.getMessage(), e);
            return;
        }
        TimeTableFragment2 fragment=new TimeTableFragment2();
        Bundle bundle=new Bundle();
        setDateTextActionBar(dateStr);
        bundle.putCharSequence(TimeTableFragment2.SELECTED_DATE, dateStr);
        fragment.setArguments(bundle);
        replaceMainFragment(fragment);
    }
    public void setDateTextActionBar(Date d){
        if(d==null){
            return;
        }
        String dStr=null;
        try {
            dStr=TimeCommon.formatDate(d, TimeCommon.FORMAT_DDMMYYYY);
        } catch (Exception e) {
            dStr=UNKNOWN_DATE;
        }
        setDateTextActionBar(dStr);
    }
    public void setDateTextActionBar(String dStr){
        getSupportActionBar().setTitle(Text.isNullOrEmpty(dStr)?UNKNOWN_DATE:dStr);
    }
}
