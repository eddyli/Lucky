package com.randalltower605.lucky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.fragment.DashboardFragment;
import com.randalltower605.lucky.manager.StationManager;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.service.StationArrivalService;
import com.randalltower605.lucky.util.AppConstants;
import com.randalltower605.lucky.util.DebugUtil;

import java.util.Arrays;

/**
 * Created by eli on 4/23/14.
 */
public class DashboardActivity extends LocationFragmentActivity{
  public final static String TRIP = "trip";
  public final static String STOPS = "stops";
  public final static String TAG = DashboardActivity.class.getSimpleName();
  public DashboardFragment mDashboardFragment;
  StationArrivalResponseReceiver mStationArrivalResponseReceiver;

  private StationManager mStationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate: BEB");
    setContentView(R.layout.activity_dashboard);
    mStationManager = StationManager.getInstance(this);

    if (savedInstanceState == null) {
      mDashboardFragment = new DashboardFragment();
      getSupportFragmentManager().beginTransaction()
        .add(R.id.container, mDashboardFragment)
        .commit();
    }

    mStationArrivalResponseReceiver = new StationArrivalResponseReceiver(new Handler());
    LocalBroadcastManager.getInstance(this).registerReceiver(mStationArrivalResponseReceiver,
      new IntentFilter(AppConstants.BROADCAST_ACTION));
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
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy: BEB");

    LocalBroadcastManager.getInstance(this).unregisterReceiver(mStationArrivalResponseReceiver);
    Intent mServiceIntent = new Intent(this, StationArrivalService.class);
    stopService(mServiceIntent);
  }

  private void updateUI(Station station) {
    DebugUtil.showToast(getApplicationContext(), "onReceive: enter " + station.getName());
    mDashboardFragment.updateCurrentStation(station);
  }

  private class StationArrivalResponseReceiver extends BroadcastReceiver {
    private Handler handler;
    private StationArrivalResponseReceiver(Handler handler) {
      this.handler = handler;
    }
    public void onReceive(Context context, Intent intent) {
      final String[] triggerIds = intent.getStringArrayExtra(StationArrivalService.EVENT_GEO_FENCE_ENTER);

      handler.post(new Runnable() {
        @Override
        public void run() {
          if (triggerIds != null) {
            Station triggeredStation = mStationManager.getStationById(triggerIds[0]);
            if (triggeredStation != null) {
              updateUI(triggeredStation);
            } else {
              DebugUtil.showToast(getApplicationContext(), "onReceive: enter " + triggerIds[0]);
            }

            //how to remove geofence when locationclient is not connected (aka activity not active?)
            removeGeoFence(Arrays.asList(triggerIds));
          } else {
            DebugUtil.showToast(getApplicationContext(), "onReceive: triggerids is null");
          }
        }
      });
    }
  }
}
