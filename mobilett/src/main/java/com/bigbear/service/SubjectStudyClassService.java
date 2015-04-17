package com.bigbear.service;

import android.content.Context;
import android.util.Log;

import com.bigbear.dao.AbstractDao;
import com.bigbear.dao.SubjectStudyClassDao;
import com.bigbear.entity.SubjectStudyClass;

import java.util.Date;

/**
 * Created by luanvu on 4/1/15.
 */
public class SubjectStudyClassService extends AbstractService {

    private static final String LOG_TAG = "SSS_SERVICE";

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
    public SubjectStudyClass getSubjectStudyClassDetailByDay(long ttId, long dayId, Date d) throws Exception{
        SubjectStudyClassDao dao=null;

        SubjectStudyClass entity =null;
        try {
            dao = new SubjectStudyClassDao(getContext());
            dao.open(AbstractDao.READ_MODE);
            entity=dao.getEntry(ttId, dayId);
        }catch (Exception e){
            Log.e(LOG_TAG, "Get subject study detail error: "+ e.getMessage(), e);
            throw e;
        }finally {
            if(dao!=null){
                dao.close();
            }
        }
        return entity;
    }
}
