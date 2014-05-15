package com.randalltower605.lucky.activity;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.fragment.ScheduleFragment;
import com.randalltower605.lucky.fragment.SetAlarmFragment;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.util.DebugUtil;

public class MainActivity extends LocationFragmentActivity implements
  SetAlarmFragment.OnStartAlarmClickListener {
  ViewPager mViewPager;
  Location mCurrentLocation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
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

  public void onStartAlarmClick(Station selectedStation) {

    mCurrentLocation = getCurrentLocation();
    if(mCurrentLocation != null) {
      DebugUtil.showToast(this, "current location = " + mCurrentLocation.toString());
    }

    if(selectedStation != null) {
      Intent intent = new Intent(this, DashboardActivity.class);
      Bundle b = new Bundle();
      b.putString(DashboardActivity.TO_STATION_ID, selectedStation.getId()); //Your id
      intent.putExtras(b); //Put your id to your next Intent
      startActivity(intent);
    }
  }
}
