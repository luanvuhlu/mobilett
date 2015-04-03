package com.bigbear.common;

import android.database.Cursor;

public class Validate {
    /**
     * Kiểm tra giá trị con trỏ với kiểu {@link java.lang.String} có null không
     * @param i
     * @param cursor
     * @return
     */
	public static String repNullCursor(int i, Cursor cursor){
		return cursor.isNull(i)?"":cursor.getString(i);
	}

    /**
     * Kiểm tra giá trị con trỏ với kiểu {@link java.lang.Integer} có null không
     * @param i
     * @param cursor
     * @return
     */
	public static int repNullIntCursor(int i, Cursor cursor){
		if(cursor.getType(i)!=Cursor.FIELD_TYPE_INTEGER){
			throw new NullPointerException("Type is not valid");
		}
		return cursor.isNull(i)?0:cursor.getInt(i);
	}

    /**
     * @param str
     * @return Nếu chuỗi rỗng thì trả về null, còn lại trả về chính nó
     */
	public static String retNull(String str){
		return "".equals(str)?null:str;
	}

    /**
     * @param str
     * @return Nếu chuỗi null thì trả về rỗng, còn lại trả về chính nó
     */
	public static String retSpace(String str){
		return str==null?"":str;
	}
}
