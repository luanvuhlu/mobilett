package com.bigbear.service;

import android.content.Context;

import com.bigbear.dao.AbstractDao;
import com.bigbear.dao.StudentDao;
import com.bigbear.dao.SubjectStudyClassDao;
import com.bigbear.entity.Student;
import com.bigbear.entity.SubjectStudyClass;

/**
 * Created by luanvu on 4/1/15.
 */
public class SubjectStudyClassService extends AbstractService {

    public SubjectStudyClassService(Context context) {
        super(context);
    }

    /**
     * Lưu SubjectStudyClass
     * @param entity
     * @return id SubjectStudyClass hoặc -1 nếu thất bại
     */
    public long save(SubjectStudyClass entity){
        SubjectStudyClassDao dao=null;
        try {
            dao=new SubjectStudyClassDao(getContext());
            dao.open(AbstractDao.WRITE_MODE);
            return dao.save(entity);
        }catch (Exception e){
            throw  e;
        }finally {
            if(dao!=null)dao.close();
        }
    }
    public SubjectStudyClass getSubjectStudyClassDetailByDay(long ttId, long dayId){
        SubjectStudyClass entity=new SubjectStudyClass();
        return entity;
    }
}
