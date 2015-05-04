package com.bigbear.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
    public HoursTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public HoursTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setSubjectStudyClass(SubjectStudyClass subjectStudyClass) {
        this.subjectStudyClass = subjectStudyClass;
    }


}
