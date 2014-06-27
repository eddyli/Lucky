package com.randalltower605.lucky.manager;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.randalltower605.lucky.dal.StationDal;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.model.Stop;
import com.randalltower605.lucky.model.Trip;
import com.randalltower605.lucky.util.StationUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by eli on 5/18/14.
 */
public class TripManager {
  private static final String TAG = TripManager.class.getSimpleName();
  private static StationDal stationDal;
  private static TripManager m_instance;

  private int nextStationInterval = 20;

  public static TripManager getInstance(Context context) {
    if(m_instance == null) {
      m_instance = new TripManager(context.getApplicationContext());
    }
    return m_instance;
  }

  private TripManager(Context context) {
    stationDal = new StationDal(context);
  }

  public Trip guessTrip(Station from, Station to, Calendar now) {
    Calendar soon = (Calendar)now.clone();
    soon.add(Calendar.MINUTE, nextStationInterval);
    List<Trip> trips = stationDal.guessTrips(from, to, now, soon);

    Trip trip = null;
    Location center = from.getLocation();

    boolean headSouth = StationUtils.isSouthOf(to, from);
    float minDistance = Float.MAX_VALUE;

    for(int i=0; i< trips.size(); i++) {
      Trip thisTrip = trips.get(i);
      Location fromLocation = thisTrip.getFromLocation();

      if(headSouth) {
        Log.d(TAG, "headSouth");
      }

      if(center.getLatitude() >= fromLocation.getLatitude()) {
        Log.d(TAG, thisTrip.getFromStationId() + "center latitude greater" + center.getLatitude() + "," + fromLocation.getLatitude());
      }

      if(center.getLatitude() <= fromLocation.getLatitude()) {
        Log.d(TAG, thisTrip.getFromStationId() + "from Latitude greater" + center.getLatitude() + "," + fromLocation.getLatitude());
      }


      if((headSouth && (center.getLatitude() >= fromLocation.getLatitude())) ||
        (!headSouth && (center.getLatitude() <= fromLocation.getLatitude()))) {
        float distance = center.distanceTo(fromLocation);
        Log.d(TAG, "station " + thisTrip.getFromStationId() + " is " + distance);

        /*
        if (center.getLatitude() > fromLocation.getLatitude()) {
          Log.d(TAG, "what the hell");
          if (center.getLatitude() == fromLocation.getLatitude()) {
            Log.d(TAG, "what the hell 2");
          }
        }*/

        if (distance < minDistance) {
          minDistance = distance;
          trip = thisTrip;
        }
      }
    }

    return trip;
  }

  public List<Trip> guessTrips(Station from, Station to, Calendar now) {
    Calendar soon = (Calendar)now.clone();
    soon.add(Calendar.MINUTE, nextStationInterval);

    return stationDal.guessTrips(from, to, now, soon);
  }

  public List<Trip> getTrips(Station from, Station to, Calendar date) {
    return stationDal.getTrips(from.getId(), to.getId(), date);
  }

  public List<Stop> getStops(Trip trip) {
    List<Stop> stops = new ArrayList<Stop>();
    if(trip == null) {
      return stops;
    }

    return stationDal.getStops(trip);
  }
}
