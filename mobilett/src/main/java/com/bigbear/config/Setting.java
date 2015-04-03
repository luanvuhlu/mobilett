package com.bigbear.config;


public final class Setting {
    /**
     * Có ẩn đi ngày nào. Mặc định là hiện tất cả.
     */
	private static boolean[] hiddenDay={false, false, false, false, false, false};
    /**
     * Có ẩn đi khoảng giữa buổi trưa không. Mặc định là không.
     */
	private static boolean hiddenMidi;
    /**
     * Có bỏ buổi tối không. Mặc định là không.
     */
	private static boolean hiddenNight;
    /**
     * Có cho hiện tất cả các lớp hay không. Mặc định là có
     */
	private static boolean viewAllSubject;

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

	
}
