package com.randalltower605.lucky.activity;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationStatusCodes;
import com.randalltower605.lucky.R;
import com.randalltower605.lucky.service.StationArrivalService;
import com.randalltower605.lucky.util.DebugUtil;
import com.randalltower605.lucky.util.GeofenceUtils;

import java.util.List;

/**
 * Created by cheukmanli on 5/11/14.
 */
public class LocationFragmentActivity extends FragmentActivity implements
  GooglePlayServicesClient.ConnectionCallbacks,
  GooglePlayServicesClient.OnConnectionFailedListener,
  LocationListener,
  LocationClient.OnAddGeofencesResultListener,
  LocationClient.OnRemoveGeofencesResultListener {


  private final String TAG = LocationFragmentActivity.class.getSimpleName();
  protected LocationClient mLocationClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate :BEB");
    if(mLocationClient == null) {
      mLocationClient = new LocationClient(this, this, this);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Connect the client.
    if(isGooglePlayServicesAvailable()) {
      Log.d(TAG, "locationclient tries connecting :BEB");
      mLocationClient.connect();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    //what happens here?
    //mLocationClient.connect();
  }

  @Override
  protected void onStop() {
    // Disconnecting the client invalidates it.
    mLocationClient.disconnect();
    Log.d(TAG, "locationclient tries disconnecting :BEB");
    super.onStop();
  }


  /**
   * Verify that Google Play services is available before making a request.
   *
   * @return true if Google Play services is available, otherwise false
   */
  protected boolean isGooglePlayServicesAvailable() {

    // Check that Google Play services is available
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

    // If Google Play services is available
    if (ConnectionResult.SUCCESS == resultCode) {

      // In debug mode, log the status
      Log.d(GeofenceUtils.APPTAG, getString(R.string.play_services_available));
      Log.d(TAG, "google service available :BEB");
      DebugUtil.showToast(this, "google play services available");

      // Continue
      return true;
    } else {

      // Display an error dialog
      Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
      if (dialog != null) {
        ErrorDialogFragment errorFragment = new ErrorDialogFragment();
        errorFragment.setDialog(dialog);
        errorFragment.show(getSupportFragmentManager(), GeofenceUtils.APPTAG);
      }
      return false;
    }
  }

  protected Location getCurrentLocation() {
    Location currentLocation = null;
    if(mLocationClient.isConnected()) {
      currentLocation = mLocationClient.getLastLocation();
    }
    return currentLocation;
  }

  private PendingIntent getTransitionPendingIntent() {
    // Create an explicit Intent
    Intent intent = new Intent(this, StationArrivalService.class);
    return PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
  }

  protected void addGeoFences(List<Geofence> geofences) {
    //queue?
    if(mLocationClient.isConnected()) {
      mLocationClient.addGeofences(geofences, getTransitionPendingIntent(), this);
    }
  }

  protected void removeGeoFence() {
    //queue?
    if(mLocationClient.isConnected()) {

    }
  }

  @Override
  public void onConnected(Bundle bundle) {
    //super(bundle);
    Log.d(TAG, "locationclient connected :BEB");
    DebugUtil.showToast(this, "locationClient on connect");
  }

  @Override
  public void onDisconnected() {
    Log.d(TAG, "locationclient disconnected :BEB");
    DebugUtil.showToast(this, "locationClient onDisconnect");
    // Destroy the current location client
    mLocationClient = null;
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

    DebugUtil.showToast(this, "locationClient on connect failed");
  }

  @Override
  public void onLocationChanged(Location location) {
    // Report to the UI that the location was updated
    String msg = "Updated Location: " +
        Double.toString(location.getLatitude()) + "," +
        Double.toString(location.getLongitude());
    DebugUtil.showToast(this, "LocationFragmentActivity onLocationChanged");
  }

  @Override
  public void onAddGeofencesResult(int statusCode, String[] strings) {
    if (LocationStatusCodes.SUCCESS == statusCode) {
            /*
             * Handle successful addition of geofences here.
             * You can send out a broadcast intent or update the UI.
             * geofences into the Intent's extended data.
             */
      DebugUtil.showToast(this, "geofence succesfully added " + strings.toString());
    } else {
      // If adding the geofences failed
            /*
             * Report errors here.
             * You can log the error using Log.e() or update
             * the UI.
             */
    }
    // Turn off the in progress flag and disconnect the client
   // mInProgress = false;
   // mLocationClient.disconnect();
  }

  @Override
  public void onRemoveGeofencesByRequestIdsResult(int i, String[] strings) {

  }

  @Override
  public void onRemoveGeofencesByPendingIntentResult(int i, PendingIntent pendingIntent) {

  }


  /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
  public static class ErrorDialogFragment extends DialogFragment {

    // Global field to contain the error dialog
    private Dialog mDialog;

    /**
     * Default constructor. Sets the dialog field to null
     */
    public ErrorDialogFragment() {
      super();
      mDialog = null;
    }

    /**
     * Set the dialog to display
     *
     * @param dialog An error dialog
     */
    public void setDialog(Dialog dialog) {
      mDialog = dialog;
    }

    /*
     * This method must return a Dialog to the DialogFragment.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      return mDialog;
    }
  }

}
