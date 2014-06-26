package com.randalltower605.lucky.manager;

import android.content.Context;

import com.randalltower605.lucky.dal.StationDal;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.model.Trip;

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

  public static TripManager getInstance(Context context) {
    if(m_instance == null) {
      m_instance = new TripManager(context.getApplicationContext());
    }
    return m_instance;
  }

  private TripManager(Context context) {
    stationDal = new StationDal(context);
  }

  public List<Trip> getTrips(Station from, Station to, Calendar date) {
    return stationDal.getTrips(from.getId(), to.getId(), date);
  }

  public List<Station> getStops(Trip trip) {
    List<Station> stops = new ArrayList<Station>();
    if(trip == null) {
      return stops;
    }

    return stationDal.getStops(trip);
  }
}
