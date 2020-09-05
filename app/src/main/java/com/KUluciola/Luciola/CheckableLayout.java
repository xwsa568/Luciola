package com.KUluciola.Luciola;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CheckableLayout extends ConstraintLayout implements Checkable {
    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // mIsChecked = false ;
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        return cb.isChecked() ;
        // return mIsChecked ;
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        setChecked(cb.isChecked() ? false : true) ;
        // setChecked(mIsChecked ? false : true) ;
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkBox1) ;

        if (cb.isChecked() != checked) {
            cb.setChecked(checked) ;
        }

        // CheckBox 가 아닌 View의 상태 변경.
    }
}
