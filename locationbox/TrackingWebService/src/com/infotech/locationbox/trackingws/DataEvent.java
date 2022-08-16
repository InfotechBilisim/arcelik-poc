package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class DataEvent {
  protected long locationId = 0;
  protected long mobileId = 0;
  protected long deviceId = 0;
  protected String custId = null;
  protected String timeStamp = null;
  protected String eventCode = null;
  protected String eventDesc = null;
  protected int locTyp = 0;
  protected long locId = 0;
  protected String locName = null;
  protected long regionId = 0;
  protected String regionName = null;
  protected long campusId = 0;
  protected String campusName = null;
  protected long venueId = 0;
  protected String venueName = null;
  protected int floor = 0;
  protected String floorName = null;
  protected long areaId = 0;
  protected String areaName = null;

  public DataEvent() {
  }

//-----------------------------------------------------------------------------

  public static DataEvent getInstance(ResultSet rset) {
    DataEvent de = new DataEvent();

    try {
      de.locationId = rset.getLong("MOBILE_LOCATION_ROWNO");
      de.mobileId = rset.getLong("MOBILE_ID");
      de.deviceId = rset.getLong("DEVICE_ID");
      de.custId = rset.getString("CUST_ID");
      de.timeStamp = rset.getString("TIME_STAMP");
      if( de.timeStamp.length() > 19 ) de.timeStamp = de.timeStamp.substring(0, 19); 
      de.eventCode = rset.getString("EVENT_CODE");
      de.eventDesc = rset.getString("EVENT_DESC");
      de.locTyp = rset.getInt("LOC_TYP");
      de.locId = rset.getLong("LOC_ID");
      de.locName = rset.getString("LOC_NAME");
      if( de.locName == null ) de.locName = "";
      de.regionId = rset.getLong("REGION_ID");
      de.regionName = rset.getString("REGION_NAME");
      if( de.regionName == null ) de.regionName = "";
      de.campusId = rset.getLong("CAMPUS_ID");
      de.campusName = rset.getString("CAMPUS_NAME");
      if( de.campusName == null ) de.campusName = "";
      de.venueId = rset.getLong("VENUE_ID");
      de.venueName = rset.getString("VENUE_NAME");
      if( de.venueName == null ) de.venueName = "";
      de.floor = rset.getInt("FLOOR");
      de.floorName = rset.getString("FLOOR_NAME");
      if( de.floorName == null ) de.floorName = "";
      de.areaId = rset.getLong("AREA_ID");
      de.areaName = rset.getString("AREA_NAME");
      if( de.areaName == null ) de.areaName = "";
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return de;
  } // getInstance()

//-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"locationid\" : " + locationId + ", \"mobileid\": " + mobileId + ", \"deviceid\": " + deviceId + ", \"custid\": " + (custId != null ?  "\"" + custId + "\"" : null) + ",  \"ts\": \"" + timeStamp +  "\", \"eventcode\": \"" + eventCode +  "\", \"eventdesc\": \"" + eventDesc + "\", \"loctyp\": " + locTyp + ", \"locid\": " + locId + ", \"locname\": \"" + locName +  "\", \"regionid\": " + regionId + ", \"regionname\": \"" + regionName +  "\", \"campusid\": " + campusId + ", \"campusname\": \"" + campusName +  "\", \"venueid\": " + venueId + ", \"venuename\": \"" + venueName +  "\", \"floor\": " + floor + ", \"floorname\": \"" + floorName +  "\", \"areaid\": " + areaId + ", \"areaname\": \"" + areaName +  "\" }";
  }

//-----------------------------------------------------------------------------

  public static DataEvent[] getEvents(DataRegister dr, long mobileId, String eventCode, String startDate, String endDate) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql  = "SELECT ME.*, M.CUST_ID, M.DEVICE_ID FROM MOBILE_EVENT ME, MOBILE M ";
      sql += "WHERE M.MOBILE_ID = ME.MOBILE_ID AND";
      if( mobileId > 0 ) sql += " ME.MOBILE_ID=?";
      else sql += " ME.MOBILE_ID IN (SELECT MOBILE_ID FROM MOBILE WHERE KEY = ?)";
      if( eventCode != null ) sql += " AND ME.EVENT_CODE=?";
      sql += " AND ME.TIME_STAMP BETWEEN ? AND ? ORDER BY ME.TIME_STAMP";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      if( mobileId > 0 ) pstmt.setLong(colno++, mobileId);
      else  pstmt.setString(colno++, dr.key);
      if( eventCode != null ) pstmt.setString(colno++, eventCode);
      pstmt.setTimestamp(colno++, Utils.toTimestamp(startDate));
      pstmt.setTimestamp(colno++, Utils.toTimestamp(endDate));
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataEvent de = getInstance(rset);
        array.add( de );
      }
    }
    catch (Exception ex) {
            Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { cnn.close(); } catch (Exception e) {;}
    }
    
    if( array.size() <= 0 ) return null;
    
    DataEvent[] des = new DataEvent[array.size()];
    for( int i = 0; i < des.length; i++ ) des[i] = (DataEvent)array.get(i);
    return des;
  } // getEvents()
  
}
