package com.bigbear.service;

import android.content.Context;

import com.appspot.hlutimetable.timetable.model.TimeTableTimeTableResponse;
import com.bigbear.adapter.HoursEntity;
import com.bigbear.adapter.ListHours;
import com.bigbear.common.TimeCommon;
import com.bigbear.dao.StudentDao;
import com.bigbear.dao.SubjectClassDao;
import com.bigbear.dao.SubjectStudyClassDao;
import com.bigbear.dao.TimeTableDao;
import com.bigbear.entity.SubjectStudyClass;
import com.bigbear.entity.TimeTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luanvu on 4/2/15.
 */
public class TimeTableService extends AbstractService {
    public TimeTableService(Context context) {
        super(context);
    }
    public TimeTableService() {

    }

    /**
     * Lưu timetable
     * @param entity
     * @return id của entity hoặc -1 nếu thất
     */
    public long save(TimeTable entity){
        TimeTableDao dao=null;
        try {
            dao = new TimeTableDao(getContext());

            dao.open();
            return dao.save(entity);
        }catch (Exception e){
            throw e;
        }finally {
            if(dao!=null)dao.close();
        }

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
            dao.open();
            StudentDao studentDao = new StudentDao(getContext());
            SubjectClassDao subjectClassDao=new SubjectClassDao(getContext());
            SubjectStudyClassDao subjectStudyClassDao=new SubjectStudyClassDao(getContext());
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

    /**
     * Tính toán các tiết học trong ngày
     * @param ett
     * @param d
     * @return {@link com.bigbear.adapter.ListHours} các giờ học trong ngày
     * @throws Exception
     */
    public ListHours getSubjectStudyOnDate(TimeTable ett, Date d) throws Exception{
        if(	d==null){
            throw new NullPointerException("Time is not valid");
        }
        ListHours lsHoursAdapter=new ListHours();

        if(ett.getSubjectStudyClass()==null){
            throw new Exception("Subject Class List is null");
        }
        for(SubjectStudyClass subjectStudyClass:ett.getSubjectStudyClass()){
            if(subjectStudyClass==null){
                throw new Exception("Subject Study Class is null");
            }
            if(subjectStudyClass.getSubjectClass()==null){
                throw new NullPointerException("Subject Class is null");
            }
            if(subjectStudyClass.getSubjectClass().getSubject()==null){
                throw new NullPointerException("Subject is null");
            }

            if(subjectStudyClass.getSubjectClass().getStartDate()==null || subjectStudyClass.getSubjectClass().getEndDate()==null){
                throw new NullPointerException("Start date and End date is null in ID: "+subjectStudyClass.getId());
            }
            if(TimeCommon.compareDate(d, subjectStudyClass.getSubjectClass().getStartDate()) == TimeCommon.LT ||
                    TimeCommon.compareDate(subjectStudyClass.getSubjectClass().getEndDate(), d) == TimeCommon.LT){
                continue;
            }
            if(!(TimeCommon.getDayOfWeek(d)+"").equals(subjectStudyClass.getDayName())){
                continue;
            }
            lsHoursAdapter.add(new HoursEntity(subjectStudyClass.getDayHours(), subjectStudyClass.getSubjectClass().getSubject().getSubjectName(), subjectStudyClass.getDayLocations(),subjectStudyClass.getClassType(), subjectStudyClass.getId()+""));

        }
        return lsHoursAdapter;
    }
    // TODO

    /**
     *
     * @return Trả về tất cả thời khóa biểu {@link com.bigbear.entity.TimeTable }
     */
    public List<TimeTable> findAll(){
        TimeTableDao dao=null;
        try{
            dao=new TimeTableDao(getContext());
            return dao.findAll();
        }catch (Exception e){
            throw e;
        }finally {
            if(dao!=null)dao.close();
        }
    }
    /**
     *
     * @return Trả về thời khóa biểu mới được import {@link com.bigbear.entity.TimeTable }
     */
    public TimeTable getNewest(){
        TimeTableDao dao=null;
        try{
            dao=new TimeTableDao(getContext());
            return dao.getNewest();
        }catch (Exception e){
            throw e;
        }finally {
            if(dao!=null)dao.close();
        }
    }
    /**
     *
     * @param id
     * @return Trả về Thời khóa biểu theo id
     */
    public TimeTable findById(long id){
        TimeTableDao dao=null;
        try{
            dao=new TimeTableDao(getContext());
            return dao.findById(id);
        }catch (Exception e){
            throw e;
        }finally {
            if(dao!=null)dao.close();
        }
    }

}
