package com.bigbear.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.api.client.util.DateTime;

public class TimeCommon {
	public static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm";
	public static final String VN_FORMAT="dd/MM/yyyy HH:mm";
	public static final String FORMAT_DDMMYYYY="dd/MM/yyyy";
	public static final String FORMAT_EDDMMYYYY="E dd/MM/yyyy";
	public static final String FORMAT_HHMMSSEDDMMYYYY="HH:mm:ss E dd/MM/yyyy";
	public static String LANG="VI";
	private static String format=STANDARD_FORMAT;
	public static Locale locale=new Locale(LANG);
	public static final int GT=1;
	public static final int EQ=0;
	public static final int LT=-1;
	public static final String DEFAULT_CURSOR_DATE = "yyyy-MM-dd HH:mm:ss";

    /**
     * Format lại ngày tháng ra kiểu {@link java.lang.String} dựa trên {@link #locale}
     * @param d Ngày tháng kiểu {@link java.util.Date }
     * @param f format muốn đổi thành
     * @return Chuỗi ngày tháng đã được format
     * @throws Exception
     */
	public static String formatDate(Date d, String f) throws Exception{
		if(d==null) return null;
		try{
			return new SimpleDateFormat(f, locale).format(d);
		}catch( Exception e){
			throw e;
		}
	}
	public static String getFormat() {
		return format;
	}
	public static void setFormat(String format) {
		TimeCommon.format = format;
	}

    /**
     * Format date dựa trên lang
     * Date Style là {@link java.text.DateFormat} LONG
     * @param d
     * @param lang
     * @return
     * @throws Exception
     */
	public static String formatDateText(Date d, String lang) throws Exception{
		if(d==null) return null;
		try{
			return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, new Locale(lang)).format(d);
		}catch( Exception e){
			throw e;
		}
	}

    /**
     * Format ngày tháng theo định dạng của Việt Nam
     * Date Style là {@link java.text.DateFormat} LONG
     * @param d
     * @return
     * @throws Exception
     */
	public static String formatDateVnText(Date d) throws Exception{
		if(d==null) return null;
		try{
			return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, new Locale("vi")).format(d);
		}catch( Exception e){
			throw e;
		}
	}

    /**
     * Format Ngày tháng theo định dạng mặc định {@link #format }
     * @param d
     * @return
     * @throws Exception
     */
	public static String formatDate(Date d) throws Exception{
		return formatDate(d, format);
	}

    /**
     * Chuyển text thành Ngày tháng theo định dạng format
     * @param value
     * @param f
     * @return
     * @throws Exception
     */
	public static Date parseDate(String value, String f) throws Exception{
		if(value==null || "".equals(value)) return null;
		try{
			return new SimpleDateFormat(f, locale).parse(value);
		}catch( Exception e){
			throw e;
		}
	}

    /**
     * Chuyển text thành Ngày tháng theo định dạng mặc định {@link #format}
     * @param value
     * @return
     * @throws Exception
     */
	public static Date parseDate(String value) throws Exception{
		if(value==null || "".equals(value)) return null;
		try{
			return new SimpleDateFormat(format, locale).parse(value);
		}catch( Exception e){
			throw e;
		}
	}

    /**
     * Chuyển từ {@link com.google.api.client.util.DateTime} sang {@link java.util.Date}
     * @param d
     * @return
     */
	public static Date toDate(DateTime d){
		if(d==null) return null;
		return new Date(d.getValue());
	}

    /**
     * Cộng ngày(day) vào date
     * @param d
     * @param day số ngày. Là số âm sẽ trừ ngày, là số dương sẽ cộng ngày
     * @return Ngày mới sau khi được thêm/bớt
     */
	public static Date addDate(Date d, int day){
    	Calendar cal=Calendar.getInstance(locale);
    	cal.setTime(d);
    	cal.set(Calendar.DATE, cal.get(Calendar.DATE)+day);
    	return cal.getTime();
    }

    /**
     * Trả về ngày trong tuần của ngày hiện tại
     * @param d
     * @return Ngày trong tuần theo định dạng số xem DAY_OF_WEEK của {@link java.util.Calendar }
     */
	public static int getDayOfWeek(Date d){
		if(d==null) throw new NullPointerException("Date is null");
    	Calendar cal=Calendar.getInstance(locale);
    	cal.setTime(d);
    	return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * So sánh hai
     * @param d1
     * @param d2
     * @returnngày
     * Lớn hơn là {@link #GT} = 1
     * Bằng {@link #EQ} = 2
     * Nhỏ hơn là {@link #LT} = -1
     */
	public static int compareDate(Date d1, Date d2){
    	return d1.getTime() > d2.getTime() ? GT:(d1.getTime() == d2.getTime()?EQ:LT);
    }
}
