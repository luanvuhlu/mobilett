package com.bigbear.entity;


public class SubjectStudyClass {
    private String classType;
    private String dayName;
    private String dayHours;
    private String dayLocations;
    private SubjectClass subjectClass;
    private TimeTable timeTable;

    public SubjectStudyClass(String classType) {
    }

    public SubjectStudyClass(String classType, String dayName, String dayHours, String dayLocations, SubjectClass subjectClass, TimeTable timeTable) {
        this.classType = classType;
        this.dayName = dayName;
        this.dayHours = dayHours;
        this.dayLocations = dayLocations;
        this.subjectClass = subjectClass;
        this.timeTable = timeTable;
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