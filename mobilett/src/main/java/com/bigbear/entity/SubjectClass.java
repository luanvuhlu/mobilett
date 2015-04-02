package com.bigbear.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectClassResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableSubjectStudyDayResponse;
import com.bigbear.common.TimeCommon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class SubjectClass {
    private Date endDate;
    private String seminarClass;
    private Date startDate;
    private Subject subject;
    private Set<SubjectStudyClass> subjectStudyDay;
    private String theoryClass;
    private long id;


    public SubjectClass(Date endDate, String seminarClass, Date startDate, Subject subject, Set<SubjectStudyClass> subjectStudyDay, String theoryClass, long id) {
        this.endDate = endDate;
        this.seminarClass = seminarClass;
        this.startDate = startDate;
        this.subject = subject;
        this.subjectStudyDay = subjectStudyDay;
        this.theoryClass = theoryClass;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public SubjectClass() {
    }

    public SubjectClass(long id) {
        this.id = id;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSeminarClass() {
        return seminarClass;
    }

    public void setSeminarClass(String seminarClass) {
        this.seminarClass = seminarClass;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Set<SubjectStudyClass> getSubjectStudyDay() {
        return subjectStudyDay;
    }

    public void setSubjectStudyDay(Set<SubjectStudyClass> subjectStudyDay) {
        this.subjectStudyDay = subjectStudyDay;
    }

    public String getTheoryClass() {
        return theoryClass;
    }

    public void setTheoryClass(String theoryClass) {
        this.theoryClass = theoryClass;
    }

}
