package com.bigbear.entity;

/**
 * Giờ học
 */
public class SubjectStudyClass {

    public static final String DAY_9_10 = "9-10";
    public static final String DAY_7_8 = "7-8";
    public static final String DAY_11_12 = "11-12";
    public static final String DAY_5_6 = "5-6";
    public static final String DAY_3_4 = "3-4";
    public static final String DAY_1_2 = "1-2";

    public static final String SEMINAR_TYPE="S";
    public static final String THEORY_TYPE="T";
    /**
     * Loại giờ học. S hoặc T
     */
    private String classType;
    private String dayName;
    /**
     * Giờ học. Ví dụ: 11-12
     */
    private String dayHours;
    /**
     * Phòng học
     */
    private String dayLocations;
    /**
     * Lớp học của môn {@link com.bigbear.entity.SubjectClass}
     */
    private SubjectClass subjectClass;
    /**
     * Thời khóa biểu {@link com.bigbear.entity.TimeTable}
     */
    private TimeTable timeTable;
    private long id;

    public SubjectStudyClass() {
    }


    public SubjectStudyClass(String classType, String dayName, String dayHours, String dayLocations, SubjectClass subjectClass, TimeTable timeTable, long id) {
        this.classType = classType;
        this.dayName = dayName;
        this.dayHours = dayHours;
        this.dayLocations = dayLocations;
        this.subjectClass = subjectClass;
        this.timeTable = timeTable;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayHours() {
        return dayHours;
    }

    public void setDayHours(String dayHours) {
        this.dayHours = dayHours;
    }

    public String getDayLocations() {
        return dayLocations;
    }

    public void setDayLocations(String dayLocations) {
        this.dayLocations = dayLocations;
    }

    public SubjectClass getSubjectClass() {
        return subjectClass;
    }

    public void setSubjectClass(SubjectClass subjectClass) {
        this.subjectClass = subjectClass;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }
}
