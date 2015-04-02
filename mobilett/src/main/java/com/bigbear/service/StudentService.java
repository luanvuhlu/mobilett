package com.bigbear.service;

import android.content.Context;

import com.bigbear.dao.StudentDao;
import com.bigbear.entity.Student;

/**
 * Created by luanvu on 4/1/15.
 */
public class StudentService extends AbstractService {

    public StudentService(Context context) {
        super(context);
    }

    public long save(Student student){
        StudentDao dao=new StudentDao(getContext());
        return dao.save(student);
    }
}
