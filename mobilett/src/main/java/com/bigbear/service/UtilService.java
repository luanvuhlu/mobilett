package com.bigbear.service;

import android.content.Context;

import com.bigbear.dao.AbstractDao;
import com.bigbear.dao.TimeTableDao;

/**
 * Created by luanvu on 4/5/15.
 */
public class UtilService extends AbstractService {
    public UtilService(Context context) {
        super(context);
    }

    public void initDatabase(){
        TimeTableDao dao=null;
        try {
            dao = new TimeTableDao(getContext());
            dao.open(AbstractDao.WRITE_MODE);
        }catch (Exception e){
            throw e;
        }finally {
            if(dao!=null)dao.close();
        }
    }
}
