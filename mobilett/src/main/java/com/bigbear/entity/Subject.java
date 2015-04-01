package com.bigbear.entity;

public class Subject {
    private int courseCredit;
    private String speciality;
    private String subjectCode;
    private String subjectName;
    private String subjectShortName;

    public Subject(int courseCredit, String speciality, String subjectCode, String subjectName, String subjectShortName) {
        this.courseCredit = courseCredit;
        this.speciality = speciality;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectShortName = subjectShortName;
    }

    public Subject() {
    }

    public int getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(int courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectShortName() {
        return subjectShortName;
    }

    public void setSubjectShortName(String subjectShortName) {
        this.subjectShortName = subjectShortName;
    }
}