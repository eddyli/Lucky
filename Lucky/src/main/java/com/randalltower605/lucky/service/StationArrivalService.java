package com.randalltower605.lucky.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.randalltower605.lucky.util.AppConstants;

import java.util.List;

/**
 * Created by eli on 5/17/14.
 */
public class StationArrivalService extends IntentService {
  public static final String EVENT_GEO_FENCE_ENTER = "com.randalltower605.lucky.events.geofence.enter";

  public StationArrivalService() {
    super("StationArrivalService");
  }

  /**
   * Handles incoming intents
   *@param intent The Intent sent by Location Services. This
   * Intent is provided
   * to Location Services (inside a PendingIntent) when you call
   * addGeofences()
   */
  @Override
  protected void onHandleIntent(Intent intent) {
    // First check for errors
    if (LocationClient.hasError(intent)) {
      // Get the error code with a static method
      int errorCode = LocationClient.getErrorCode(intent);

    } else {
      // Get the type of transition (entry or exit)
      int transitionType =
          LocationClient.getGeofenceTransition(intent);
      // Test that a valid transition was reported
      if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
        List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);

        String[] triggerIds = new String[triggerList.size()];

        for (int i = 0; i < triggerIds.length; i++) {
          // Store the Id of each geofence
          triggerIds[i] = triggerList.get(i).getRequestId();
        }

        //send local broadcast
        Intent broadcastIntent = new Intent(AppConstants.BROADCAST_ACTION);
        broadcastIntent.putExtra(EVENT_GEO_FENCE_ENTER, triggerIds);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

      } else if ( transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {

      }
    }
  }

}
