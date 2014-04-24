package com.randalltower605.lucky.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.manager.StationManager;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.model.Trip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by eli on 4/12/14.
 */
public class ScheduleFragment extends Fragment {
  private Button mFromSelectButton;
  private Button mToSelectButton;
  private Station mFromStation;
  private Station mToStation;
  private StationManager stationManager;
  private TableLayout mScheduleTable;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    stationManager = new StationManager(getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
    mFromSelectButton = (Button) rootView.findViewById(R.id.fromSelectorButton);
    mToSelectButton = (Button) rootView.findViewById(R.id.toSelectorButton);
    final String defaultText = rootView.getResources().getString(R.string.station_selector_text);

    //consolidate these logic into a Button class.
    mFromSelectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        StationSelectorFragment stationSelectorFragment = new StationSelectorFragment(mFromStation);
        stationSelectorFragment.setStationSelectorDialogListener(new StationSelectorFragment.StationSelectorDialogListener() {
          public void onStationSelectedDialog(Station station) {

            if(station != null) {
              stationManager.pushRecentStation(station);
            }
            mFromStation = station;
            if(mFromSelectButton != null) {
              if(mFromStation == null) {
                mFromSelectButton.setText(defaultText);
              } else {
                mFromSelectButton.setText(mFromStation.getName());
              }
            }
            refreshSchedules();
          }
        });
        stationSelectorFragment.show(getFragmentManager(), "SSF", mFromStation);
      }
    });
    mToSelectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        StationSelectorFragment stationSelectorFragment = new StationSelectorFragment(mToStation);
        stationSelectorFragment.setStationSelectorDialogListener(new StationSelectorFragment.StationSelectorDialogListener() {
          public void onStationSelectedDialog(Station station) {

            if (station != null) {
              stationManager.pushRecentStation(station);
            }
            mToStation = station;
            if(mToSelectButton != null) {
              if(mToStation == null) {
                  mToSelectButton.setText(defaultText);
              } else {
                  mToSelectButton.setText(mToStation.getName());
              }
            }
            refreshSchedules();
          }
        });
        stationSelectorFragment.show(getFragmentManager(), "SSF", mToStation);
      }
    });

    mScheduleTable = (TableLayout) rootView.findViewById(R.id.scheduleResultsTable);

    return rootView;
  }

  private void refreshSchedules() {
    if(mFromStation != null && mToStation != null) {
      List<Trip> trips = stationManager.getTrips(mFromStation, mToStation, Calendar.getInstance());
      updateScheduleTable(trips);
    }
  }

  private void updateScheduleTable(List<Trip> trips) {
    if(mScheduleTable == null) {
      return;
    }
    mScheduleTable.removeAllViews();
    for(int i=0; i<trips.size(); i++) {
      SimpleDateFormat simpleDF = new SimpleDateFormat("h:mm");
      String departStr = simpleDF.format(trips.get(i).getDeparture().getTime());
      String arrivalStr = simpleDF.format(trips.get(i).getArrival().getTime());
      TableRow row = new TableRow(getActivity());
      TextView departureTextView = new TextView(getActivity());
      departureTextView.setText(departStr);
      TextView arrivalTextView = new TextView(getActivity());
      arrivalTextView.setText(arrivalStr);
      row.addView(departureTextView);
      row.addView(arrivalTextView);
      mScheduleTable.addView(row);
    }
  }
}
