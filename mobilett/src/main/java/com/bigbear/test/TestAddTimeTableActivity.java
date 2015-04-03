package com.bigbear.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;

import com.bigbear.fragment.ListTimeTableFragment;
import com.bigbear.mobilett.MainActivity;
import com.bigbear.mobilett.R;

/**
 * Created by luanvu on 4/3/15.
 */
public class TestAddTimeTableActivity extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    public TestAddTimeTableActivity(){
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mActivity=getActivity();
    }
    private Fragment startFragment(Fragment fragment) {
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment, "tag");
        transaction.commit();
        getInstrumentation().waitForIdleSync();
        Fragment frag = mActivity.getSupportFragmentManager().findFragmentByTag("tag");
        return frag;
    }

    public void testFragment() {
        ListTimeTableFragment fragment = new ListTimeTableFragment() {
            //Override methods and add assertations here.
        };

        Fragment frag = startFragment(fragment);
    }
}
