package com.bigbear.common;

import android.database.Cursor;

public class Validate {
	public static String repNullCursor(int i, Cursor cursor){
		return cursor.isNull(i)?"":cursor.getString(i);
	}
	public static int repNullIntCursor(int i, Cursor cursor){
		if(cursor.getType(i)!=Cursor.FIELD_TYPE_INTEGER){
			throw new NullPointerException("Type is not valid");
		}
		return cursor.isNull(i)?0:cursor.getInt(i);
	}
	public static String retNull(String str){
		return "".equals(str)?null:str;
	}
	public static String retSpace(String str){
		return str==null?"":str;
	}
}
