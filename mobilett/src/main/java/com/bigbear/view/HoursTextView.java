package com.bigbear.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.bigbear.entity.SubjectStudyClass;

/**
 * Created by luanvv on 5/4/2015.
 */
public class HoursTextView extends TextView {
    private SubjectStudyClass subjectStudyClass;

    public HoursTextView(Context context) {
        super(context);
    }
    public void setSubjectStudyClass(SubjectStudyClass subjectStudyClass) {
        this.subjectStudyClass = subjectStudyClass;
    }


}
