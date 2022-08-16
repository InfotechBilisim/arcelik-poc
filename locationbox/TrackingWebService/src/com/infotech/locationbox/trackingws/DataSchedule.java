package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

import javax.json.JsonObject;

public class DataSchedule {
  protected long id = 0;
  protected String name = null;
  protected long mobileId = 0;
  protected String eventCode = null;
  protected int locTyp = 0;
  protected long locId = 0;
  protected String startDate = null;
  protected String endDate = null;
  protected String startTime = null;
  protected String endTime = null;
  protected boolean always = false;
  protected boolean sunday = false;
  protected boolean monday = false;
  protected boolean tuesday = false;
  protected boolean wednesday = false;
  protected boolean thursday = false;
  protected boolean friday = false;
  protected boolean saturday = false;

  public DataSchedule() {
  }

  public DataSchedule(long id) {
    this.id = id;
  }

//-----------------------------------------------------------------------------

  public static DataSchedule getInstance(ResultSet rset) {
    DataSchedule ds = new DataSchedule();

    try {
      ds.id = rset.getLong("SCHEDULE_ID");
      ds.name = rset.getString("SCHEDULE_NAME");
      ds.mobileId = rset.getLong("MOBILE_ID");
      ds.eventCode = rset.getString("EVENT_CODE");
      ds.locTyp = rset.getInt("LOC_TYP");
      ds.locId = rset.getLong("LOC_ID");
      ds.startDate = rset.getString("START_DATE");
      if( ds.startDate != null ) ds.startDate = ds.startDate.substring(0, 10);
      ds.endDate = rset.getString("END_DATE");
      if( ds.endDate != null ) ds.endDate = ds.endDate.substring(0, 10);
      ds.startTime = rset.getString("START_TIME");
      if( ds.startTime != null ) ds.startTime = ds.startTime.substring(11, 16);
      ds.endTime = rset.getString("END_TIME");
      if( ds.endTime != null ) ds.endTime = ds.endTime.substring(11, 16);
      ds.always = rset.getInt("ALWAYS") != 0;
      ds.sunday = rset.getInt("SUNDAY") != 0;
      ds.monday = rset.getInt("MONDAY") != 0;
      ds.tuesday = rset.getInt("TUESDAY") != 0;
      ds.wednesday = rset.getInt("WEDNESDAY") != 0;
      ds.thursday = rset.getInt("THURSDAY") != 0;
      ds.friday = rset.getInt("FRIDAY") != 0;
      ds.saturday = rset.getInt("SATURDAY") != 0;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return ds;
  } // getInstance()

//-----------------------------------------------------------------------------

  public static DataSchedule getInstance(DataRegister dr, long scheduleId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "SELECT * FROM MOBILE_SCHEDULE WHERE SCHEDULE_ID=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, scheduleId);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        DataSchedule ds = getInstance(rset);
        return ds;
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

    return null;
  } // getInstance()

//-----------------------------------------------------------------------------

  public static DataSchedule getInstance(JsonObject params) {
    DataSchedule ds = new DataSchedule();

    try {
      ds.id = params.getJsonNumber("id").longValue();
      ds.name = params.getString("name");
      ds.mobileId = params.getJsonNumber("mobileid").longValue();
      ds.eventCode = params.getString("eventcode");
      ds.locTyp = params.getJsonNumber("loctyp").intValue();
      ds.locId = params.getJsonNumber("locid").longValue();
      ds.startDate = params.getString("startdate");
      ds.endDate = params.getString("enddate");
      ds.startTime = params.getString("starttime");
      ds.endTime = params.getString("endtime");
      ds.always = params.getBoolean("always");
      ds.sunday = params.getBoolean("sunday");
      ds.monday = params.getBoolean("monday");
      ds.tuesday = params.getBoolean("tuesday");
      ds.wednesday = params.getBoolean("wednesday");
      ds.thursday = params.getBoolean("thursday");
      ds.friday = params.getBoolean("friday");
      ds.saturday = params.getBoolean("saturday");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }

    return ds;
  } // getInstance()

//-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"id\": " + id + ", \"name\": \"" + name +  "\", \"mobileid\": " + mobileId + ", \"eventcode\": \"" + eventCode + "\", \"loctyp\": " + locTyp + ", \"locid\": " + locId +
           ", \"startdate\": \"" + startDate + "\", \"enddate\": \"" + endDate + "\", \"starttime\": \"" + startTime + "\", \"endtime\": \"" + endTime + "\"" +
           ", \"always\": " + always + 
           ", \"sunday\": " + sunday + ", \"monday\": " + monday + ", \"tuesday\": " + tuesday + ", \"wednesday\": " + wednesday + ", \"thursday\": " + thursday + ", \"friday\": " + friday + ", \"saturday\": " + saturday + " }";
  }

//-----------------------------------------------------------------------------

  public long insert(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int count = 0;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      id = Utils.getUniqueId("MOBILE_SCHEDULE");

      sql  = "INSERT INTO MOBILE_SCHEDULE (SCHEDULE_ID,SCHEDULE_NAME,MOBILE_ID,EVENT_CODE,LOC_TYP,LOC_ID,START_DATE,END_DATE,START_TIME,END_TIME,ALWAYS,SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,ACTIVE_FLAG)";
      sql += " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1)";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, id);
      pstmt.setString(colno++, name);
      pstmt.setLong(colno++, mobileId);
      pstmt.setString(colno++, eventCode);
      pstmt.setInt(colno++, locTyp);
      pstmt.setLong(colno++, locId);
      pstmt.setTimestamp(colno++, Utils.toTimestamp(startDate));
      pstmt.setTimestamp(colno++, Utils.toTimestamp(endDate));
      pstmt.setTimestamp(colno++, Utils.toTimestamp(startTime));
      pstmt.setTimestamp(colno++, Utils.toTimestamp(endTime));
      pstmt.setInt(colno++, (always ? 1 : 0));
      pstmt.setInt(colno++, (sunday ? 1 : 0));
      pstmt.setInt(colno++, (monday ? 1 : 0));
      pstmt.setInt(colno++, (tuesday ? 1 : 0));
      pstmt.setInt(colno++, (wednesday ? 1 : 0));
      pstmt.setInt(colno++, (thursday ? 1 : 0));
      pstmt.setInt(colno++, (friday ? 1 : 0));
      pstmt.setInt(colno++, (saturday ? 1 : 0));
      count = pstmt.executeUpdate();
    }
    catch (Exception ex) {
            Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
      return -1;
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { cnn.close(); } catch (Exception e) {;}
    }
    
    return id;
  } // insert()

//-----------------------------------------------------------------------------

  public boolean update(DataRegister dr) {
    PreparedStatement pstmt = null;
    String sql = null;
    int count = 0;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "UPDATE MOBILE_SCHEDULE SET SCHEDULE_NAME=?,MOBILE_ID=?,EVENT_CODE=?,LOC_TYP=?,LOC_ID=?,START_DATE=?,END_DATE=?,START_TIME=?,END_TIME=?,ALWAYS=?,SUNDAY=?,MONDAY=?,TUESDAY=?,WEDNESDAY=?,THURSDAY=?,FRIDAY=?,SATURDAY=? WHERE SCHEDULE_ID=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setString(colno++, name);
      pstmt.setLong(colno++, mobileId);
      pstmt.setString(colno++, eventCode);
      pstmt.setInt(colno++, locTyp);
      pstmt.setLong(colno++, locId);
      pstmt.setTimestamp(colno++, Utils.toTimestamp(startDate));
      pstmt.setTimestamp(colno++, Utils.toTimestamp(endDate));
      pstmt.setTimestamp(colno++, Utils.toTimestamp(startTime));
      pstmt.setTimestamp(colno++, Utils.toTimestamp(endTime));
      pstmt.setInt(colno++, (always ? 1 : 0));
      pstmt.setInt(colno++, (sunday ? 1 : 0));
      pstmt.setInt(colno++, (monday ? 1 : 0));
      pstmt.setInt(colno++, (tuesday ? 1 : 0));
      pstmt.setInt(colno++, (wednesday ? 1 : 0));
      pstmt.setInt(colno++, (thursday ? 1 : 0));
      pstmt.setInt(colno++, (friday ? 1 : 0));
      pstmt.setInt(colno++, (saturday ? 1 : 0));
      pstmt.setLong(colno++, id);
      count = pstmt.executeUpdate();
    }
    catch (Exception ex) {
            Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { cnn.close(); } catch (Exception e) {;}
    }
    
    return (count > 0);
  } // update()

//-----------------------------------------------------------------------------

  public int delete(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int colno = 0;
    int count = 0;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "DELETE MOBILE_SCHEDULE WHERE SCHEDULE_ID=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setLong(colno++, id);
      count = pstmt.executeUpdate();
      return 0;
      
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
    
    return -2;
  } // delete()

//-----------------------------------------------------------------------------

  public static DataSchedule[] getSchedules(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT * FROM MOBILE_SCHEDULE WHERE ACTIVE_FLAG=1 ORDER BY SCHEDULE_NAME";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataSchedule ds = getInstance(rset);
        array.add( ds );
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
    
    DataSchedule[] dss = new DataSchedule[array.size()];
    for( int i = 0; i < dss.length; i++ ) dss[i] = (DataSchedule)array.get(i);
    return dss;
  } // getSchedules()
  
}
