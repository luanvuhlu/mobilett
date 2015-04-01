package com.bigbear.entity;


import java.util.Set;

public class Student {
    private long id;
    private String code;
    private String name;
    private String studentClass;
    public Set<TimeTable> timeTables;

    public Student() {
    }

    public Student(long id, String code, String name, String studentClass, Set<TimeTable> timeTables) {
        this.id=id;
        this.code = code;
        this.name = name;
        this.studentClass = studentClass;
        this.timeTables = timeTables;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}


