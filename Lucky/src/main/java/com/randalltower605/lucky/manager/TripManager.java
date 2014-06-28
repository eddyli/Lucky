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

  private static final int DISTANCE_CLOSE_ENOUGH = 500;
  private static final int DISTANCE_TOO_FAR = 4000;

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

  private boolean isOnTheWay(boolean goSouth, Location l1, Location l2) {
    if((goSouth && (l1.getLatitude() >= l2.getLatitude())) ||
      (!goSouth && (l1.getLatitude() <= l2.getLatitude()))) {
      Log.d(TAG, "on the way");
      return true;
    }
    return false;
  }
  private boolean isCloseEnough(Location l1, Location l2) {
    float dist = l1.distanceTo(l2);
    if(dist <= DISTANCE_CLOSE_ENOUGH) {
      Log.d(TAG, "close enough");
      return true;
    }
    return false;
  }

  private boolean isTooFar(Location l1, Location l2) {
    float dist = l1.distanceTo(l2);
    if(dist >= DISTANCE_TOO_FAR) {
      Log.d(TAG, "too far");
      return true;
    }
    return false;
  }

  public Trip guessTrip(Location at, Station to, Calendar now) {
    // get list of trip candidates
    boolean goSouth = at.getLatitude() > to.getLatitude();
    Calendar soon = (Calendar)now.clone();
    soon.add(Calendar.MINUTE, nextStationInterval);
    List<Trip> trips = stationDal.guessTrips(to, now, goSouth, soon);

    Trip trip = null;
    float minDistance = Float.MAX_VALUE;

    for(int i=0; i< trips.size(); i++) {
      Trip thisTrip = trips.get(i);

      Log.d(TAG, "Evaluating " + thisTrip.getFromStationId() + " to " + thisTrip.getToStationId()+ " for location(" + at.getLatitude() + "," + at.getLongitude() + ")");
      Location fromLocation = thisTrip.getFromLocation();

      if(!isTooFar(at, fromLocation) &&
        (isCloseEnough(at, fromLocation) || isOnTheWay(goSouth, at, fromLocation))) {

        float distance = at.distanceTo(fromLocation);
        Log.d(TAG, " distance is " + distance);
        if (distance < minDistance) {
          minDistance = distance;
          trip = thisTrip;
        }
      }
    }

    if(trip != null)
    {    Log.d(TAG, "trip wins : " + trip.getFromStationId() + " to " + trip.getToStationId());
    } else {
      Log.d(TAG, "NO ONE WINS");
    }
    return trip;
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
