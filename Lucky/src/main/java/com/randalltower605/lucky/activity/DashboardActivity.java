package com.randalltower605.lucky.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.randalltower605.lucky.R;

/**
 * Created by eli on 4/23/14.
 */
public class DashboardActivity extends GooglePlayServiceFragmentActivity {
  public final static String FROM_STATION_ID = "fromStationId";
  public final static String TO_STATION_ID = "toStationId";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    Intent intent = getIntent();
    String toStationId = intent.getStringExtra(FROM_STATION_ID);
    if(servicesConnected()) {

    }
  }

}
