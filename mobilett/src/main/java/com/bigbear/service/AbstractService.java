package com.bigbear.service;

import android.content.Context;

/**
 * Created by luanvu on 4/2/15.
 */
public abstract class AbstractService {
    private Context context;

    protected AbstractService() {
    }

    public AbstractService(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
