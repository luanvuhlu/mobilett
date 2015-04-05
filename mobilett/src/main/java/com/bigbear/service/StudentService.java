package com.bigbear.service;

import android.content.Context;

import com.bigbear.dao.AbstractDao;
import com.bigbear.dao.StudentDao;
import com.bigbear.entity.Student;

/**
 * Created by luanvu on 4/1/15.
 */
public class StudentService extends AbstractService {

    public StudentService(Context context) {
        super(context);
    }

    /**
     * Lưu student
     * @param student
     * @return id student hoặc -1 nếu thất bại
     */
    public long save(Student student){
        StudentDao dao=null;
        try {
            new StudentDao(getContext());
            dao.open(AbstractDao.WRITE_MODE);
            return dao.save(student);
        }catch (Exception e){
            throw  e;
        }finally {
            if(dao!=null)dao.close();
        }
    }
}
