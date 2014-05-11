package com.randalltower605.lucky.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.randalltower605.lucky.R;

import com.randalltower605.lucky.adapter.StationSelectAdapter;
import com.randalltower605.lucky.manager.StationManager;
import com.randalltower605.lucky.model.Station;

import java.util.List;

/**
 * Created by eli on 3/18/14.
 */

public class StationSelectorFragment extends DialogFragment {
    public static final String TAG = "StationSelectorFragment";
    private StationSelectorDialogListener stationSelectorDialogListener;
    private Station m_defaultStation;

    public interface StationSelectorDialogListener {
        void onStationSelectedDialog(Station station);
    }

    public StationSelectorFragment(Station defaultStation) {
        super();
        m_defaultStation = defaultStation;
    }

    public void show(android.support.v4.app.FragmentManager manager, java.lang.String tag, Station defaultStation) {
        super.show(manager, tag);
        m_defaultStation = defaultStation;
    }

    public void setStationSelectorDialogListener(StationSelectorDialogListener listener) {
        stationSelectorDialogListener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        StationManager stationManager = StationManager.getInstance(getActivity());
        List<Station> stations = stationManager.getStationsByGeoOrder();
        List<Station> recentStations = stationManager.getRecentStations();
        View v = inflater.inflate(R.layout.station_select_dialog_view, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        populateStationListView(v, stations);
        populateRecentStationListView(v, recentStations);

        return v;
    }

    private void populateStationListView(View v, List<Station> stations) {

        ListView listView = (ListView)v.findViewById(R.id.stationSelectorListView);
        listView.setAdapter(new StationSelectAdapter(getActivity(),
                R.layout.item_station_select,
                R.id.stationSelectorText,
                stations));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

          Station station = (Station) view.getTag();

          //callback to the opener then self dismiss
          if(stationSelectorDialogListener != null) {
              stationSelectorDialogListener.onStationSelectedDialog(station);
          }
          dismiss();
            }
        });
        listView.setDivider(null);

        //optimize this!
        if(m_defaultStation != null) {
            for(int i=0; i<stations.size(); i++) {
                if(stations.get(i).getId() == m_defaultStation.getId()) {
                    listView.setSelection(i);
                }
            }
        }
    }

    private void populateRecentStationListView(View v, List<Station> recentStations) {

        ListView listView = (ListView)v.findViewById(R.id.recentStationSelectorListView);
        listView.setAdapter(new StationSelectAdapter(getActivity(),
                R.layout.item_station_select,
                R.id.stationSelectorText,
                recentStations));

        //consolidate this
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Station station = (Station) view.getTag();

                //callback to the opener then self dismiss
                if(stationSelectorDialogListener != null) {
                    stationSelectorDialogListener.onStationSelectedDialog(station);
                }
                dismiss();
            }
        });
    }
}