package com.randalltower605.lucky.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.activity.DashboardActivity;
import com.randalltower605.lucky.manager.StationManager;
import com.randalltower605.lucky.model.Station;

import com.randalltower605.lucky.util.FontUtil;

/**
 * Created by eli on 4/12/14.
 */
public class SetAlarmFragment extends Fragment{

  private final String KEY_SELECTED_STATION_ID = "selectedStationId";
  private Station selectedStation;
  private StationManager stationManager;
  private OnStartAlarmClickListener mCallback;
  private static final String TAG = SetAlarmFragment.class.getSimpleName();

  public SetAlarmFragment() {
    Log.d(TAG, "constructor: BEB");
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate: BEB");
    stationManager = StationManager.getInstance(getActivity());
    if(savedInstanceState != null) {
      String stationid = savedInstanceState.getString(KEY_SELECTED_STATION_ID);
      selectedStation = stationManager.getStationById(stationid);
    }
  }
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    Log.d(TAG, "onAttach: BEB");
    try {
      mCallback = (OnStartAlarmClickListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement OnStartAlarmClickListener");
    }
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.d(TAG, "onActivityCreated: BEB");
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
  public void onDestroyView() {
    super.onDestroyView();
    Log.d(TAG, "onDestroyView: BEB");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy: BEB");
  }

  @Override
  public void onDetach() {
    super.onDetach();
    Log.d(TAG, "onDetach: BEB");
  }

  private void updateUI(View v) {
    String defaultText = v.getResources().getString(R.string.station_selector_text);
    if(v == null)
      return;

    Button stationSelectorButton = (Button)v.findViewById(R.id.stationSelectorButton);
    if(stationSelectorButton != null) {
      if(selectedStation == null) {
        stationSelectorButton.setText(defaultText);
      } else {
        stationSelectorButton.setText(selectedStation.getName());
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView: BEB");
    View rootView = inflater.inflate(R.layout.fragment_set_alarm, container, false);

    //set page title style
    TextView pageTitle = (TextView)rootView.findViewById(R.id.pageTitle);
    Button startAlarmButton = (Button)rootView.findViewById(R.id.startAlarmButton);

    Typeface pageTitleTypeFace = FontUtil.getTypeFace(getActivity().getAssets(), FontUtil.GOTHAM_NARROW_MEDIUM);
    if(pageTitleTypeFace != null)
    {
      pageTitle.setTypeface(pageTitleTypeFace);
      pageTitle.setPaintFlags(pageTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
      startAlarmButton.setTypeface(pageTitleTypeFace);
    }

    Button stationSelectorButton = (Button)rootView.findViewById(R.id.stationSelectorButton);
    Typeface gothemLight = FontUtil.getTypeFace(getActivity().getAssets(), FontUtil.GOTHAM_NARROW_LIGHT);
    if(gothemLight != null)
    {
      stationSelectorButton.setTypeface(gothemLight);
    }

    stationSelectorButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        StationSelectorFragment stationSelectorFragment = new StationSelectorFragment(selectedStation);
        stationSelectorFragment.setStationSelectorDialogListener(new StationSelectorFragment.StationSelectorDialogListener() {
          public void onStationSelectedDialog(Station station) {

            if(station != null) {
              stationManager.pushRecentStation(station);
            }
            selectedStation = station;
            updateUI(getView());
          }
        });
        stationSelectorFragment.show(getFragmentManager(), "SSF", selectedStation);
      }
    });
    updateUI(rootView);

    Button startAlaramButton = (Button)rootView.findViewById(R.id.startAlarmButton);
    startAlarmButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mCallback.onStartAlarmClick(selectedStation);
      }
    });
    return rootView;
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
    // Save the user's current game state
    if(selectedStation != null) {
      savedInstanceState.putString(KEY_SELECTED_STATION_ID, selectedStation.getId());
    }
  }

  public interface OnStartAlarmClickListener {
    public void onStartAlarmClick(Station station);
  }
}
