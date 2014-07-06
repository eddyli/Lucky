package com.randalltower605.lucky.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.activity.DashboardActivity;
import com.randalltower605.lucky.manager.StationManager;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.model.Trip;

import java.text.SimpleDateFormat;

/**
 * Created by eli on 4/23/14.
 */
public class DashboardFragment extends Fragment {
  private View mRootView;
  private StationManager mStationManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mStationManager = StationManager.getInstance(getActivity());
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mRootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
    Intent intent = getActivity().getIntent();
    Trip trip = intent.getParcelableExtra(DashboardActivity.TRIP);
    setTrip(trip);
    return mRootView;
  }

  public void updateCurrentStation(Station station) {
    if(mRootView != null) {
      TextView currentStationText = (TextView)mRootView.findViewById(R.id.currentStation);
      currentStationText.setText(station.getName());

    }
  }

  public void setTrip(Trip trip) {
    if(mRootView != null) {

      TextView destinationStationText = (TextView)mRootView.findViewById(R.id.destinationStation);
      TextView estimatedArrivalTime = (TextView)mRootView.findViewById(R.id.estimatedArrivalTime);

      Station destination = mStationManager.getStationById(trip.getToStationId());
      destinationStationText.setText(destination.getName());

      SimpleDateFormat df = new SimpleDateFormat("HH:mm");
      estimatedArrivalTime.setText(df.format(trip.getArrival().getTime()));
    }
  }
}
