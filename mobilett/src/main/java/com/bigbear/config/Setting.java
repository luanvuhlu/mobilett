package com.bigbear.config;


public final class Setting {
	private static boolean[] hiddenDay={false, false, false, false, false, false};
	private static boolean hiddenMidi;
	private static boolean hiddenNight;
	private static boolean viewAllSubject;
	private static String viewSubjectCode;
	private Setting() {
		
	}
	public static void reset(){
		Setting.hiddenDay[0]=true;
		Setting.hiddenMidi=false;
		Setting.hiddenNight=true;
		Setting.viewAllSubject=true;
	}
	public static boolean[] getHiddenDay() {
		return Setting.hiddenDay;
	}
	public static String getViewSubjectCode() {
		return Setting.viewSubjectCode;
	}
	public static boolean isHiddenMidi() {
		return Setting.hiddenMidi;
	}
	public static boolean isHiddenNight() {
		return Setting.hiddenNight;
	}
	public static boolean isViewAllSubject() {
		return Setting.viewAllSubject;
	}
	
	public static void setHiddenDay(boolean[] hiddenDay) {
		Setting.hiddenDay = hiddenDay;
	}
	public static void setHiddenMidi(boolean hiddenMidi) {
		Setting.hiddenMidi = hiddenMidi;
	}
	public void setHiddenNight(boolean hiddenNight) {
		Setting.hiddenNight = hiddenNight;
	}
	public void setViewAllSubject(boolean viewAllSubject) {
		Setting.viewAllSubject = viewAllSubject;
	}
	public static void setViewSubjectCode(String viewSubjectCode) {
		Setting.viewSubjectCode = viewSubjectCode;
	}
	
}
