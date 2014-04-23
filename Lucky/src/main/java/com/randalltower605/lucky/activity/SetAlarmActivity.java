package com.randalltower605.lucky.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.fragment.ScheduleFragment;
import com.randalltower605.lucky.fragment.SetAlarmFragment;

public class SetAlarmActivity extends FragmentActivity {
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_alarm);/*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setAlarmContainer, new SetAlarmFragment())
                    .add(R.id.scheduleContainer, new ScheduleFragment())
                    .commit();
        }*/
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(
                        getSupportFragmentManager());

        mViewPager = (ViewPager)findViewById(R.id.mainViewPager);
        mViewPager.setAdapter(pagerAdapter);
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class MainPagerAdapter extends FragmentStatePagerAdapter {
        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            if(i == 0) {
                fragment = new SetAlarmFragment();
            } else if(i == 1) {
                fragment = new ScheduleFragment();
            }
            //Bundle args = new Bundle();
            // Our object is just an integer :-P
            //args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
            //fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    /*
    @Override
     public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        // Save the user's current game state
        savedInstanceState.putBoolean(KEY_VISITED, true);
    }*/


}
