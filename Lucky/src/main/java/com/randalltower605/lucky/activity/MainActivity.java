package com.randalltower605.lucky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.fragment.ScheduleFragment;
import com.randalltower605.lucky.fragment.SetAlarmFragment;
import com.randalltower605.lucky.manager.StationManager;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.service.StationAlarmService;
import com.randalltower605.lucky.util.AppConstants;
import com.randalltower605.lucky.util.DebugUtil;

public class MainActivity extends LocationFragmentActivity implements
  SetAlarmFragment.OnStartAlarmClickListener {
  ViewPager mViewPager;
  Location mCurrentLocation;
  StationManager mStationManager;
  //StationAlarmResponseReceiver mDownloadStateReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    MainPagerAdapter pagerAdapter = new MainPagerAdapter(
                    getSupportFragmentManager());

    mViewPager = (ViewPager)findViewById(R.id.mainViewPager);
    mViewPager.setAdapter(pagerAdapter);
    mStationManager = StationManager.getInstance(this);


    //IntentFilter mStatusIntentFilter = new IntentFilter(AppConstants.BROADCAST_ACTION);
    // Adds a data filter for the HTTP scheme
    //mStatusIntentFilter.addDataScheme("http");

    // Instantiates a new DownloadStateReceiver
    //mDownloadStateReceiver = new StationAlarmResponseReceiver();
    // Registers the DownloadStateReceiver and its intent filters
    //LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver,mStatusIntentFilter);

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
    Station fromStation = null;
    mCurrentLocation = getCurrentLocation();
    if(mCurrentLocation != null) {
      DebugUtil.showToast(this, "current location = " + mCurrentLocation.toString());
      fromStation = mStationManager.getNearestStation(mCurrentLocation);
      DebugUtil.showToast(this, "nearest station = " + fromStation.getName());
    }

    if(selectedStation != null && fromStation != null) {


      Intent mServiceIntent = new Intent(this, StationAlarmService.class);
      mServiceIntent.setData(Uri.parse(selectedStation.getId()));

      startService(mServiceIntent);


      Intent intent = new Intent(this, DashboardActivity.class);
      Bundle b = new Bundle();
      b.putString(DashboardActivity.TO_STATION_ID, selectedStation.getId()); //Your id
      b.putString(DashboardActivity.FROM_STATION_ID, fromStation.getId()); //Your id
      intent.putExtras(b); //Put your id to your next Intent
      startActivity(intent);
    }
  }

  private class StationAlarmResponseReceiver extends BroadcastReceiver {
    private StationAlarmResponseReceiver() {

    }
    public void onReceive(Context context, Intent intent) {
      DebugUtil.showToast(context, "Main receive response");
    }
  }
}
