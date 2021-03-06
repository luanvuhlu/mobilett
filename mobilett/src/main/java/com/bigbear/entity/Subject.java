package com.bigbear.entity;

/**
 * Môn học
 */
public class Subject {
    private long id;
    /**
     * Số tín chỉ của môn học.
     */
    private int courseCredit;
    private String speciality;
    private String subjectCode;
    private String subjectName;
    /**
     * Tên ngắn của môn học
     */
    private String subjectShortName;

    public Subject(long id, int courseCredit, String speciality, String subjectCode, String subjectName, String subjectShortName) {
        this.id=id;
        this.courseCredit = courseCredit;
        this.speciality = speciality;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectShortName = subjectShortName;
    }

    public Subject() {
    }
    public Subject(long id) {
        this.id=id;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return Số tín chỉ của môn học
     */
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

    /**
     *
     * @return Tên ngắn của môn học
     */
    public String getSubjectShortName() {
        return subjectShortName;
    }

    public void setSubjectShortName(String subjectShortName) {
        this.subjectShortName = subjectShortName;
    }
}