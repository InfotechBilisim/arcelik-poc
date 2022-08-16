package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class DataAlarm {
  protected long rowno = 0;
  protected long mobileId = 0;
  protected String timeStamp = null;
  protected String eventCode = null;
  protected String eventDesc = null;
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

  public DataAlarm() {
  }

//-----------------------------------------------------------------------------

  public static DataAlarm getInstance(ResultSet rset) {
    DataAlarm da = new DataAlarm();

    try {
      da.rowno = rset.getLong("ROWNO");
      da.mobileId = rset.getLong("MOBILE_ID");
      da.timeStamp = rset.getString("TIME_STAMP");
      if( da.timeStamp.length() > 19 ) da.timeStamp = da.timeStamp.substring(0, 19); 
      da.eventCode = rset.getString("EVENT_CODE");
      da.eventDesc = rset.getString("EVENT_DESC");
      da.regionId = rset.getLong("REGION_ID");
      da.regionName = rset.getString("REGION_NAME");
      if( da.regionName == null ) da.regionName = "";
      da.campusId = rset.getLong("CAMPUS_ID");
      da.campusName = rset.getString("CAMPUS_NAME");
      if( da.campusName == null ) da.campusName = "";
      da.venueId = rset.getLong("VENUE_ID");
      da.venueName = rset.getString("VENUE_NAME");
      if( da.venueName == null ) da.venueName = "";
      da.floor = rset.getInt("FLOOR");
      da.floorName = rset.getString("FLOOR_NAME");
      if( da.floorName == null ) da.floorName = "";
      da.areaId = rset.getLong("AREA_ID");
      da.areaName = rset.getString("AREA_NAME");
      if( da.areaName == null ) da.areaName = "";
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return da;
  } // getInstance()

//-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"rowno\": " + rowno + ", \"mobileid\": " + mobileId + ", \"ts\": \"" + timeStamp +  "\", \"eventcode\": \"" + eventCode +  "\", \"eventdesc\": \"" + eventDesc + "\", \"regionid\": " + regionId + ", \"regionname\": \"" + regionName +  "\", \"campusid\": " + campusId + ", \"campusname\": \"" + campusName +  "\", \"venueid\": " + venueId + ", \"venuename\": \"" + venueName +  "\", \"floor\": " + floor + ", \"floorname\": \"" + floorName +  "\", \"areaid\": " + areaId + ", \"areaname\": \"" + areaName +  "\" }";
  }

//-----------------------------------------------------------------------------

  public static DataAlarm[] getAlarms(DataRegister dr, int hoursBefore) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT * FROM (SELECT MA.* FROM MOBILE_ALARM MA, MOBILE M WHERE (MA.MOBILE_ID = M.MOBILE_ID) AND M.KEY=? AND MA.PROCESS_STATUS=? AND MA.TIME_STAMP > SYSDATE - ?/24 ORDER BY MA.ROWNO) WHERE ROWNUM < 100";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setString(colno++, dr.key);
      pstmt.setInt(colno++, 0);
      pstmt.setInt(colno++, hoursBefore);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataAlarm da = getInstance(rset);
        array.add( da );
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
    
    DataAlarm[] das = new DataAlarm[array.size()];
    for( int i = 0; i < das.length; i++ ) {
      das[i] = (DataAlarm)array.get(i);
      try { das[i].markProcessed(); } catch (Exception e) {;}
    } // for()
    return das;
  } // getAlarms()
  
//-----------------------------------------------------------------------------

  private void markProcessed() {
    PreparedStatement pstmt = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "UPDATE MOBILE_ALARM SET PROCESS_STATUS=?,PROCESS_TIME_STAMP=SYSDATE,PROCESS_MODULE_NAME=?,PROCESS_MODULE_INSTANCE=? WHERE ROWNO=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setInt(colno++, 1);
      pstmt.setString(colno++, "trackingws");
      pstmt.setInt(colno++, 1);
      pstmt.setLong(colno++, rowno);
      pstmt.executeUpdate();
    }
    catch (Exception ex) {
            Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { cnn.close(); } catch (Exception e) {;}
    }

    return;
  } // markProcessed()

}
