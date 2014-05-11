package com.randalltower605.lucky.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.randalltower605.lucky.R;
import com.randalltower605.lucky.util.GeofenceUtils;

/**
 * Created by cheukmanli on 5/11/14.
 */
public class GooglePlayServiceFragmentActivity extends FragmentActivity {

  /**
   * Verify that Google Play services is available before making a request.
   *
   * @return true if Google Play services is available, otherwise false
   */
  protected boolean servicesConnected() {

    // Check that Google Play services is available
    int resultCode =
      GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

    // If Google Play services is available
    if (ConnectionResult.SUCCESS == resultCode) {

      // In debug mode, log the status
      Log.d(GeofenceUtils.APPTAG, getString(R.string.play_services_available));

      // Continue
      return true;

      // Google Play services was not available for some reason
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
