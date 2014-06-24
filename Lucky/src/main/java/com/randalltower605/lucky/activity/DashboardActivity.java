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
import com.randalltower605.lucky.service.StationArrivalService;
import com.randalltower605.lucky.util.AppConstants;
import com.randalltower605.lucky.util.DebugUtil;

/**
 * Created by eli on 4/23/14.
 */
public class DashboardActivity extends LocationFragmentActivity{
  public final static String FROM_STATION_ID = "fromStationId";
  public final static String TO_STATION_ID = "toStationId";
  public final static String TAG = DashboardActivity.class.getSimpleName();
  StationArrivalResponseReceiver mStationArrivalResponseReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate: BEB");
    setContentView(R.layout.activity_dashboard);

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

  private class StationArrivalResponseReceiver extends BroadcastReceiver {
    private Handler handler;
    private StationArrivalResponseReceiver(Handler handler) {
      this.handler = handler;
    }
    public void onReceive(Context context, Intent intent) {
      DebugUtil.showToast(context, "Dashboard receive response");
      String[] triggerIds = intent.getStringArrayExtra(StationArrivalService.EVENT_GEO_FENCE_ENTER);

      handler.post(new Runnable() {
        @Override
        public void run() {
          DebugUtil.showToast(getApplicationContext(), "onReceive: enter geofence");
        }
      });
      Log.d(TAG, "onReceive: triggerIds = ");
    }
  }
}
