package com.randalltower605.lucky.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.randalltower605.lucky.model.Stop;
import com.randalltower605.lucky.model.Trip;
import com.randalltower605.lucky.model.Station;
import com.randalltower605.lucky.util.DalUtils;
import com.randalltower605.lucky.util.StationUtils;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by eli on 4/16/14.
 */
public class StationDal  extends SQLiteAssetHelper {
  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "caltrain.sqlite";

  //caltrain db day don't end at 00, overnight train hour could exceed 24. for example 1am = 25, etc.
  private static final SimpleDateFormat dbTimeFormat = new SimpleDateFormat("HH:mm:ss");
  private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public StationDal(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    super.onUpgrade(db, oldVersion, newVersion);
  }


  public List<Station> getAllParentStations() {
    List<Station> stations = new ArrayList<Station>();
    String selectQuery = "SELECT  * FROM stops WHERE parent_station is null ORDER BY stop_lat desc";

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {
      do {
        Station station = new Station();
        station.setId(cursor.getString(0));
        station.setName(cursor.getString(2));
        Location l = new Location(Station.LOCATION_PROVIDER);
        l.setLatitude(Double.parseDouble(cursor.getString(4)));
        l.setLongitude(Double.parseDouble(cursor.getString(5)));
        station.setLocation(l);
        if(cursor.getString(6) != null) {
            station.setZone(Integer.parseInt(cursor.getString(6)));
        }
        stations.add(station);
      } while (cursor.moveToNext());
    }

    db.close();

    // return contact list
    return stations;
  }

  public List<Trip> guessTrips(Station from, Station to, Calendar now, Calendar soon) {
    List<Trip> trips = new ArrayList<Trip>();
    String selectQuery = "select a1.trip_id, e1.parent_station, e.parent_station, a1.departure_time, a.arrival_time, e1.stop_lat, e1.stop_lon, e.stop_lat, e.stop_lon " +
      "from stop_times a, stop_times a1, trips b, calendar d, stops e1, stops e " +
      "where a.stop_id = e.stop_id and e.parent_station='%s' and a.arrival_time > '%s' " +
      "and a1.departure_time > '%s' and a1.arrival_time < '%s' " +
      "and a.trip_id = b.trip_id and a1.trip_id = a.trip_id and a1.stop_sequence < a.stop_sequence and b.direction_id=%s " +
      "and d.%s = 1 and d.start_date <='%s' and d.end_date >='%s' and b.service_id = d.service_id " +
      "and e1.stop_id = a1.stop_id";

    String timeStr = DalUtils.getDBTimeString(now);
    String dateStr = DalUtils.getDBDateString(now);
    String weekdayFlag = DalUtils.getDBWeekDayString(now);
    String toTimeStr = DalUtils.getDBTimeString(soon);
    String direction = StationUtils.isSouthOf(from, to) ? "0" : "1";

    String fulFilledSelectQuery = String.format(selectQuery,
      to.getId(),
      timeStr,
      timeStr,
      toTimeStr,
      direction,
      weekdayFlag,
      dateStr,
      dateStr);

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(fulFilledSelectQuery, null);

    try {
      if (cursor.moveToFirst()) {
        do {
          Trip trip = new Trip();
          trip.setId(cursor.getString(0));
          trip.setFromStation(cursor.getString(1));
          trip.setToStation(cursor.getString(2));

          Calendar arrival = Calendar.getInstance();
          arrival.setTime(dbTimeFormat.parse(cursor.getString(4)));
          trip.setArrival(arrival);

          Calendar departure = Calendar.getInstance();
          departure.setTime(dbTimeFormat.parse(cursor.getString(3)));
          trip.setDeparture(departure);

          Location fromLocation = new Location(Station.LOCATION_PROVIDER);
          fromLocation.setLatitude(Double.parseDouble(cursor.getString(5)));
          fromLocation.setLongitude(Double.parseDouble(cursor.getString(6)));
          trip.setFromLocation(fromLocation);

          Location toLocation = new Location(Station.LOCATION_PROVIDER);
          toLocation.setLatitude(Double.parseDouble(cursor.getString(7)));
          toLocation.setLongitude(Double.parseDouble(cursor.getString(8)));
          trip.setToLocation(toLocation);

          trips.add(trip);
        } while (cursor.moveToNext());
      }
    } catch (ParseException e) {
      return new ArrayList<Trip>();
    }

    db.close();

    /*
    select a1.trip_id, e1.parent_station, e.parent_station, a1.departure_time, a.arrival_time from stop_times a, stop_times a1, trips b, calendar d, stops e1, stops e

where a.stop_id = e.stop_id and e.parent_station='ctmv' and a.arrival_time > '08:30:00'

and a1.departure_time > '08:30:00' and a1.arrival_time < '08:50:00'

and a.trip_id = b.trip_id and a1.trip_id = a.trip_id and a1.stop_sequence < a.stop_sequence and b.direction_id=1

and d.thursday = 1 and d.start_date <='2014-06-26' and d.end_date >='2014-06-26' and b.service_id = d.service_id

and e1.stop_id = a1.stop_id

     */
    return trips;
  }

  public List<Trip> getTrips(String fromId, String toId, Calendar now) {

    List<Trip> trips = new ArrayList<Trip>();

    String selectQuery = " SELECT c.trip_id, a.parent_station, a1.parent_station, b.departure_time, b1.arrival_time " +
      "FROM stops a, stop_times b, stops a1, stop_times b1, trips c, calendar d " +
      "where a.parent_station='%s' and a.stop_id = b.stop_id " +
      "and b.trip_id = b1.trip_id and a1.parent_station='%s' and a1.stop_id = b1.stop_id " +
      "and b.stop_sequence < b1.stop_sequence " +
      "and b.trip_id = c.trip_id and c.service_id = d.service_id and d.%s = 1 " +
      "and d.start_date <= '%s' and d.end_date >= '%s' and b.departure_time > '%s' " +
      "order by b.arrival_time";

    String dateStr = DalUtils.getDBDateString(now);
    String weekdayFlag = DalUtils.getDBWeekDayString(now);
    String timeStr = DalUtils.getDBTimeString(now);

    String fulFilledSelectQuery = String.format(selectQuery,
      fromId,
      toId,
      weekdayFlag,
      dateStr,
      dateStr,
      timeStr);

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(fulFilledSelectQuery, null);

    try {
      if (cursor.moveToFirst()) {
        do {
          Trip trip = new Trip();
          trip.setId(cursor.getString(0));
          trip.setFromStation(cursor.getString(1));
          trip.setToStation(cursor.getString(2));

          Calendar arrival = Calendar.getInstance();
          arrival.setTime(dbTimeFormat.parse(cursor.getString(4)));
          trip.setArrival(arrival);

          Calendar departure = Calendar.getInstance();
          departure.setTime(dbTimeFormat.parse(cursor.getString(3)));
          trip.setDeparture(departure);

          trips.add(trip);
        } while (cursor.moveToNext());
      }
    } catch (ParseException e) {
      return new ArrayList<Trip>();
    }

    db.close();
    return trips;
  }

  public List<Stop> getStops(Trip trip) {
    List<Stop> stops = new ArrayList<Stop>();
    if(trip == null) {
      return stops;
    }

    String selectQuery = "select b.stop_id, b.stop_name, b.stop_lat, b.stop_lon, b.zone_id, a.departure_time, a.arrival_time " +
      "from stop_times a, stops b where a.stop_id = b.stop_id and a.trip_id='%s' and a.departure_time >= '%s' and a.arrival_time <= '%s'";

    String departureTimeStr = DalUtils.getDBTimeString(trip.getDeparture());
    String arrivalTimeStr = DalUtils.getDBTimeString(trip.getArrival());

    String fulfilledSelectQuery = String.format(selectQuery,
      trip.getId(),
      departureTimeStr,
      arrivalTimeStr);

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(fulfilledSelectQuery, null);

    try {
      if (cursor.moveToFirst()) {
        do {
          Stop stop = new Stop();
          stop.setId(cursor.getString(0));
          stop.setName(cursor.getString(1));
          Location l = new Location(Station.LOCATION_PROVIDER);
          l.setLatitude(Double.parseDouble(cursor.getString(2)));
          l.setLongitude(Double.parseDouble(cursor.getString(3)));
          stop.setLocation(l);
          if (cursor.getString(4) != null) {
            stop.setZone(Integer.parseInt(cursor.getString(4)));
          }
          Calendar arrival = Calendar.getInstance();
          arrival.setTime(dbTimeFormat.parse(cursor.getString(5)));
          stop.setArrival(arrival);

          Calendar departure = Calendar.getInstance();
          departure.setTime(dbTimeFormat.parse(cursor.getString(6)));
          stop.setDeparture(departure);

          stops.add(stop);
        } while (cursor.moveToNext());
      }
    } catch(ParseException e) {
      return new ArrayList<Stop>();
    }

    db.close();
    return stops;
  }
}
