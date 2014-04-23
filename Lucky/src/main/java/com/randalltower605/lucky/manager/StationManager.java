package com.randalltower605.lucky.manager;

import android.content.Context;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.randalltower605.lucky.dal.StationDal;
import com.randalltower605.lucky.model.Trip;
import com.randalltower605.lucky.model.Station;

/**
 * Created by eli on 3/16/14.
 */
public class StationManager {
    private static StationManager instance;
    private static List<Station> mStations;
    private static Queue<Station> mRecentStations;
    private static final int MAX_RECENT_STATIONS_SIZE = 2;
    private static Context context;

    private static StationDal stationDal;
    public StationManager(Context c) {
        if(instance != null)
        {
            mStations = instance.getStations();
            return;
        }

        instance = this;
        context = c;
        load();
    }

    private void load() {
        stationDal = new StationDal(context);
    }

    public static Station getStationById(String id) {

        Station station = null;
        for(int i=0; i< mStations.size(); i++)
        {
            Station thisStation = mStations.get(i);
            if(thisStation.getId().equals(id))
                station = thisStation;
        }

        return station;
    }

    public List<Station> getStations() {
        return mStations;
    }
    public List<Station> getStationsByGeoOrder() {

        return stationDal.getAllParentStations();
    }

    public List<Trip> getTrips(Station from, Station to, Calendar today) {

        return stationDal.getTrips(from.getId(), to.getId(), today);
    }

    //get this save into file system too.
    public void pushRecentStation(Station station) {
        if(mRecentStations == null) {
            mRecentStations = new LinkedList<Station>();
        }

        //if it is already in the queue, re-queue it
        if(mRecentStations.remove(station)) {
            mRecentStations.add(station);
        }
        else {
            mRecentStations.add(station);
            if(mRecentStations.size() > MAX_RECENT_STATIONS_SIZE) {
                mRecentStations.remove();
            }
        }
    }

    public List<Station> getRecentStations() {
        if(mRecentStations == null)
            return new ArrayList<Station>();

        List<Station> recentStations = new ArrayList<Station>(mRecentStations);
        Collections.reverse(recentStations);
        return recentStations;
    }
}
