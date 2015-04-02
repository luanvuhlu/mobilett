package com.bigbear.service;

import android.content.Context;

import com.bigbear.dao.StudentDao;

/**
 * Created by luanvu on 4/1/15.
 */
public class StudentService {
    private Context context;

    public void save(){
        StudentDao dao=new StudentDao(context);

    }
}
