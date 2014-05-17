package com.randalltower605.lucky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.service.StationAlarmService;
import com.randalltower605.lucky.util.AppConstants;
import com.randalltower605.lucky.util.DebugUtil;

/**
 * Created by eli on 4/23/14.
 */
public class DashboardActivity extends LocationFragmentActivity {
  public final static String FROM_STATION_ID = "fromStationId";
  public final static String TO_STATION_ID = "toStationId";

  StationAlarmResponseReceiver mDownloadStateReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);

    IntentFilter mStatusIntentFilter = new IntentFilter(AppConstants.BROADCAST_ACTION);
    // Adds a data filter for the HTTP scheme
    //mStatusIntentFilter.addDataScheme("http");

    // Instantiates a new DownloadStateReceiver
    mDownloadStateReceiver = new StationAlarmResponseReceiver();
    // Registers the DownloadStateReceiver and its intent filters
    LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver,mStatusIntentFilter);
  }

  @Override
  protected void onDestroy() {
    // Unregister since the activity is about to be closed.
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);

    Intent mServiceIntent = new Intent(this, StationAlarmService.class);
    stopService(mServiceIntent);
    super.onDestroy();
  }

  private class StationAlarmResponseReceiver extends BroadcastReceiver {
    private StationAlarmResponseReceiver() {

    }
    public void onReceive(Context context, Intent intent) {
      DebugUtil.showToast(context, "Dashboard receive response");
    }
  }
}
