package com.randalltower605.lucky.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.randalltower605.lucky.util.AppConstants;
import com.randalltower605.lucky.util.DebugUtil;

/**
 * Created by cheukmanli on 5/15/14.
 */
public class StationAlarmService extends IntentService {

  public final static String FROM_STATION_ID = "fromStationId";
  public final static String TO_STATION_ID = "toStationId";

  public StationAlarmService() {
    super("StationAlarmService");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    DebugUtil.showToast(this, "service starting");
    return super.onStartCommand(intent,flags,startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    DebugUtil.showToast(this, "service destroying");
  }

  @Override
  protected void onHandleIntent(Intent intent) {

    for(int i=0; i< 10; i++ ) {
      try {

        Intent localIntent = new Intent(AppConstants.BROADCAST_ACTION)
          .putExtra(AppConstants.EXTENDED_DATA_STATUS, "OK");

        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        DebugUtil.showToast(this, "service broadcast!");

        Thread.sleep(1500);

      } catch (InterruptedException e) {
        e.printStackTrace();
        // handle the exception...
        // For example consider calling Thread.currentThread().interrupt(); here.
      }
    }
  }
}
