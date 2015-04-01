package com.bigbear.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.appspot.hlutimetable.timetable.model.TimeTableSubjectClassResponse;
import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;
import com.bigbear.adapter.HoursEntity;
import com.bigbear.adapter.ListHours;
import com.bigbear.common.TimeCommon;
import com.bigbear.common.Validate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class TimeTable {
    private String semester;
    private Student student;
    private Set<SubjectStudyClass> subjectStudyClass;
    private String year;
    private Date createdDate;

    public TimeTable() {
    }

    public TimeTable(String semester, Student student, Set<SubjectStudyClass> subjectStudyClass, String year, Date createdDate) {
        this.semester = semester;
        this.student = student;
        this.subjectStudyClass = subjectStudyClass;
        this.year = year;
        this.createdDate = createdDate;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Set<SubjectStudyClass> getSubjectStudyClass() {
        return subjectStudyClass;
    }

    public void setSubjectStudyClass(Set<SubjectStudyClass> subjectStudyClass) {
        this.subjectStudyClass = subjectStudyClass;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
