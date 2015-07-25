package com.bigbear.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.api.client.repackaged.com.google.common.base.Strings;

import java.util.Date;

/**
 * Created by luanvu on 4/19/15.
 */
public class SharedPreferenceUtil {
    private static final String SHARE_PRE_NAME="SHARE_PRE_NAME";
    private static final String PRE_SELECTED_DATE="selected-date";
    private static final String PRE_ACTIVE_TIMETABLE="active-timetable";
    public static SharedPreferences.Editor getEdit(Context context){
        return context.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE).edit();
    }
    public static void putString(String key, String value, Context context){
        SharedPreferences.Editor edit=getEdit(context);
        edit.putString(key, value);
        edit.commit();
    }
    public static void putSelectedDate(Date d, Context context) throws Exception {
        if(d==null){
            throw new NullPointerException();
        }
        SharedPreferences.Editor edit=getEdit(context);
        String value=TimeCommon.formatDate(d, TimeCommon.FORMAT_DDMMYYYY);
        edit.putString(PRE_SELECTED_DATE, value);
        edit.commit();
    }
    public static void putSelectedDate(String dStr, Context context) throws Exception {
        if(Strings.isNullOrEmpty(dStr)){
            return;
        }
        SharedPreferences.Editor edit=getEdit(context);
        edit.putString(PRE_SELECTED_DATE, dStr);
        edit.commit();
    }
    public static Date getSelectedDate(Context context) throws Exception {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        String d=sharedPreferences.getString(PRE_SELECTED_DATE, "");
        if(Text.isNullOrEmpty(d)){
            throw new Exception("Preference empty "+PRE_SELECTED_DATE);
        }
        return TimeCommon.parseDate(d, TimeCommon.FORMAT_DDMMYYYY);
    }
    public static void putActiveTimeTable(long id, Context context) throws Exception {
        if(id==0){
            throw new IllegalArgumentException("ID is zero");
        }
        SharedPreferences.Editor edit=getEdit(context);
        edit.putLong(PRE_ACTIVE_TIMETABLE, id);
        edit.commit();
    }
    public static long getActiveTimeTable(Context context) throws Exception {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(PRE_ACTIVE_TIMETABLE, 0);
    }
}
