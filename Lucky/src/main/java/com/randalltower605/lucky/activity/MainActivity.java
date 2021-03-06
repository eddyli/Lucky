package com.randalltower605.lucky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.randalltower605.lucky.R;
import com.randalltower605.lucky.fragment.ScheduleFragment;
import com.randalltower605.lucky.fragment.SetAlarmFragment;
import com.randalltower605.lucky.manager.StationManager;
import com.randalltower605.lucky.manager.TripManager;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.model.Stop;
import com.randalltower605.lucky.model.Trip;
import com.randalltower605.lucky.service.StationArrivalService;
import com.randalltower605.lucky.util.AppConstants;
import com.randalltower605.lucky.util.DebugUtil;
import com.randalltower605.lucky.util.LocationTestUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends LocationFragmentActivity implements
  SetAlarmFragment.OnStartAlarmClickListener {
  ViewPager mViewPager;
  Location mCurrentLocation;
  StationManager mStationManager;
  TripManager mTripManager;
  private static final int GEOFENCE_RADIUS = 1000;
  private static final long GEOFENCE_EXPIRY = 2 * 60 * 60 * 1000;

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate: BEB");
    setContentView(R.layout.activity_main);
    MainPagerAdapter pagerAdapter = new MainPagerAdapter(
                    getSupportFragmentManager());

    mViewPager = (ViewPager)findViewById(R.id.mainViewPager);
    mViewPager.setAdapter(pagerAdapter);
    mStationManager = StationManager.getInstance(this);
    mTripManager = TripManager.getInstance(this);
  }

  @Override
  public void onStart() {
    super.onStart();

    Log.d(TAG, "onStart: BEB");
  }

  @Override
  public void onResume() {
    super.onResume();

    Log.d(TAG, "onResume: BEB");
  }

  @Override
  public void onPause() {
    super.onPause();

    Log.d(TAG, "onPause: BEB");
  }

  @Override
  public void onStop() {
    super.onStop();

    Log.d(TAG, "onStop: BEB");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy: BEB");
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
    /*
    List<Geofence> geofences = new ArrayList<Geofence>();
    for (int i = 0; i < LocationTestUtil.WAYPOINTS_LAT.length; i++) {
      Geofence geofence = new Geofence.Builder()
        .setRequestId(Integer.toString(i))
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        .setCircularRegion(
          LocationTestUtil.WAYPOINTS_LAT[i], LocationTestUtil.WAYPOINTS_LNG[i], GEOFENCE_RADIUS)
        .setExpirationDuration(GEOFENCE_EXPIRY)
        .build();
      geofences.add(geofence);
    }

    addGeoFences(geofences);*/


    Station fromStation = null;
    mCurrentLocation = getCurrentLocation();


    if(mCurrentLocation != null) {
      //DebugUtil.showToast(this, "current location = " + mCurrentLocation.toString());
      fromStation = mStationManager.getNearestStation(mCurrentLocation);
      DebugUtil.showToast(this, "nearest station = " + fromStation.getName());
    }

    if(selectedStation != null && fromStation != null) {
      Trip trip= mTripManager.guessTrip(mCurrentLocation, selectedStation, Calendar.getInstance());
      if(trip != null) {
        Station guess = mStationManager.getStationById(trip.getFromStationId());
        DebugUtil.showToast(this, "guessed from station is = " + guess.getName() + ", " + trip.getId(), true);
        List<Stop> stops = mTripManager.getStops(trip);

        if (stops != null && stops.size() > 0) {
          List<Geofence> geofences = new ArrayList<Geofence>();

          for (int i = 0; i < stops.size(); i++) {
            geofences.add(stops.get(i).toGeofence(Geofence.GEOFENCE_TRANSITION_ENTER, GEOFENCE_RADIUS, GEOFENCE_EXPIRY));
          }

          //geofences.add(fromStation.toGeofence(Geofence.GEOFENCE_TRANSITION_ENTER, GEOFENCE_RADIUS, GEOFENCE_EXPIRY));
          addGeoFences(geofences);

          Intent intent = new Intent(this, DashboardActivity.class);
          intent.putExtra(DashboardActivity.TRIP, trip);
          //intent.putExtra(DashboardActivity.STOPS, stops);
          startActivity(intent);
        } else {
          //no stops available
        }
      } else {
        //no guess trip available
      }

      //two things to think about
      //1. if not too far from the guess, can ignore north and south
      //2. if too far from the guess prompt user if he wants to start alarm later.
    }
  }
}
