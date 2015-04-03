package com.bigbear.adapter;

import com.bigbear.db.SubjectEtt;

/**
 * Entity cho mỗi ô
 * @author luanvu
 */
public class HoursEntity {
    /**
     * Giờ học
     */

	private String hours;
    /**
     * Tên môn học
     */
	private String subjectName;
    /**
     * Phòng học
     */
	private String location;
    /**
     * ID của {@link com.bigbear.entity.SubjectStudyClass}
     */
	private String dayId;
    /**
     * Loại lớp "S" là {@link #SEMINAR_TYPE} Thảo luận
     * Loại lớp "T" là {@link #THEORY_TYPE} Lý thuyết
     */
    private String classType;
    public static final String SEMINAR_TYPE="S";
    public static final String THEORY_TYPE="T";
	public String getHours() {
		return hours;
	}
	public String getDayId() {
		return dayId;
	}
	public void setDayId(String dayId) {
		this.dayId = dayId;
	}
	public String getLocation() {
		return location;
	}

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public void setHours(String hours) {
		this.hours = hours;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public HoursEntity() {
		
	}
	
	public HoursEntity(String hours, String subjectName,
			String location, String classType, String dayId) {
		this.hours = hours;
		this.subjectName = subjectName;
		this.location = location;
        this.classType=classType;
		this.dayId=dayId;
	}
	public HoursEntity(String hours) {
		this.hours = hours;
		this.subjectName = "";
		this.location = "";
        this.classType = "";
		this.dayId=null;
	}
	public void reset(String hours){
		this.hours = hours;
		this.subjectName = "";
		this.location = "";
        this.classType = "";
		this.dayId=null;
	}
	public int[] getHoursInt() {
		int[] hsInt=new int[2];
		
		return hsInt;
	}
	public boolean equals(HoursEntity obj) {
		if(obj==null || this.getHours()==null)return false;
		return this.getHours().equals(obj.getHours());
	}
	public String getSubjectName(){
		return subjectName==null?"":subjectName;
	}
	
}
