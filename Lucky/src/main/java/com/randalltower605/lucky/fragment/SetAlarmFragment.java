package com.randalltower605.lucky.fragment;

import android.support.v4.app.Fragment;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.randalltower605.lucky.R;
import com.randalltower605.lucky.manager.StationManager;
import com.randalltower605.lucky.model.Station;

import com.randalltower605.lucky.util.FontUtil;

/**
 * Created by eli on 4/12/14.
 */
public class SetAlarmFragment extends Fragment{

    private final String KEY_SELECTED_STATION_ID = "selectedStationId";
    private final String KEY_SELECTED_STATION_NAME = "selectedStationName";
    private Station selectedStation;
    private StationManager stationManager;

    public SetAlarmFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stationManager = new StationManager(getActivity());
        if(savedInstanceState != null) {
            String stationid = savedInstanceState.getString(KEY_SELECTED_STATION_ID);
            selectedStation = stationManager.getStationById(stationid);
        }
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
}
