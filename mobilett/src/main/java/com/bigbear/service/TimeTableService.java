package com.bigbear.service;

import android.content.Context;

import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;
import com.bigbear.dao.StudentDao;
import com.bigbear.dao.SubjectClassDao;
import com.bigbear.dao.SubjectStudyClassDao;
import com.bigbear.dao.TimeTableDao;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;

/**
 * Created by luanvu on 4/2/15.
 */
public class TimeTableService extends AbstractService {
    public TimeTableService(Context context) {
        super(context);
    }

    /**
     * Lưu timetable
     * @param entity
     * @return id của entity hoặc -1 nếu thất
     */
    public long save(TimeTable entity){
        TimeTableDao dao=new TimeTableDao(getContext());
        return dao.save(entity);
    }

    /**
     * Lấy dữ liệu timetable từ response và lưu vào database
     * @param timeTableResponse
     * @return id timetable enity vừa được lưu. -1 nếu lưu thất bại.
     */
    public long save(TimeTableTimeTableResponse timeTableResponse){
        TimeTableDao dao =null;
        try {
             dao = new TimeTableDao(getContext());
            StudentDao studentDao = new StudentDao(getContext());
            SubjectClassDao subjectClassDao=new SubjectClassDao(getContext());
            SubjectStudyClassDao subjectStudyClassDao=new SubjectStudyClassDao(getContext());
            dao.open();
            TimeTable entity = dao.getEntityFromResponse(timeTableResponse);
            studentDao.save(entity.getStudent());
            for(SubjectStudyClass subjectStudyClass:entity.getSubjectStudyClass()){
                subjectClassDao.save(subjectStudyClass.getSubjectClass());
                subjectStudyClassDao.save(subjectStudyClass);
            }
            return dao.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        }finally {
            if(dao!=null)dao.close();
        }
    }
}
