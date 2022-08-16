package com.infotech.locationbox.tracking.platform.indoorspatial;

import com.infotech.locationbox.tracking.platform.base.*;

import java.sql.*;
import java.util.*;

public class IndoorSpatial {
  public static final int STATUS_UNKNOWN  = 0;
  public static final int STATUS_INSIDE   = 1;
  public static final int STATUS_OUTSIDE  = 2;
  public static final int STATUS_MARK     = 3;

  public static final int LOCTYPE_REGION  = 1;
  public static final int LOCTYPE_CAMPUS  = 11;
  public static final int LOCTYPE_VENUE   = 12;
  public static final int LOCTYPE_FLOOR   = 13;
  public static final int LOCTYPE_AREA    = 14;

  public static final String LOCTYPE_S_REGION = "REGION";
  public static final String LOCTYPE_S_CAMPUS = "CAMPUS";
  public static final String LOCTYPE_S_VENUE  = "VENUE";
  public static final String LOCTYPE_S_FLOOR  = "FLOOR";
  public static final String LOCTYPE_S_AREA   = "AREA";

  public IndoorSpatial() {
  }

//-----------------------------------------------------------------------------

  public IndoorResult[] checkIndoorForMobile(long mobileId, double xcoor, double ycoor, int floor, String timeStamp) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int colno = 0;

    ArrayList array = new ArrayList();

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      markAllInsideAsSomething(mobileId);
      
      sql = "SELECT AREA_ID,AREA_NAME,FLOOR_NAME,VENUE_ID,VENUE_NAME,CAMPUS_ID,CAMPUS_NAME FROM INDOOR_ADMIN_AREA WHERE FLOOR=? AND SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE'";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setInt(colno++, floor);
      pstmt.setDouble(colno++, xcoor);
      pstmt.setDouble(colno++, ycoor);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        long areaId = rset.getLong("AREA_ID");
        String areaName = rset.getString("AREA_NAME");
        String floorName = rset.getString("FLOOR_NAME");
        long venueId = rset.getLong("VENUE_ID");
        String venueName = rset.getString("VENUE_NAME");
        long campusId = rset.getLong("CAMPUS_ID");
        String campusName = rset.getString("CAMPUS_NAME");
        
        if( areaId > 0 ) {
          int status = getLocMobileStatus(mobileId, LOCTYPE_AREA, areaId);
          if( status != STATUS_MARK ) {
            Log.showText("INDOOR TYPE: " + LOCTYPE_AREA + ", ID: " + areaId + ", NAME: " + areaName + " -- INSIDE");
            array.add( new IndoorResult(LOCTYPE_AREA, LOCTYPE_S_AREA, areaId, areaName, STATUS_INSIDE) );
          }
          makeMobileInside(mobileId, LOCTYPE_AREA, areaId, areaName, timeStamp);
        }

        if( venueId > 0 ) {
          int status = getLocMobileStatus(mobileId, LOCTYPE_VENUE, venueId);
          if( status != STATUS_MARK ) {
            Log.showText("INDOOR TYPE: " + LOCTYPE_VENUE + ", ID: " + venueId + ", NAME: " + venueName + " -- INSIDE");
            array.add( new IndoorResult(LOCTYPE_VENUE, LOCTYPE_S_VENUE, venueId, venueName, STATUS_INSIDE) );
          }
          makeMobileInside(mobileId, LOCTYPE_VENUE, venueId, venueName, timeStamp);

          status = getLocMobileStatus(mobileId, LOCTYPE_FLOOR, floor);
          if( status != STATUS_MARK ) {
            Log.showText("INDOOR TYPE: " + LOCTYPE_FLOOR + ", ID: " + floor + ", NAME: " + floorName + " -- INSIDE");
            array.add( new IndoorResult(LOCTYPE_FLOOR, LOCTYPE_S_FLOOR, floor, floorName, STATUS_INSIDE) );
          }
          makeMobileInside(mobileId, LOCTYPE_FLOOR, floor, floorName, timeStamp);
        }

        if( campusId > 0 ) {
          int status = getLocMobileStatus(mobileId, LOCTYPE_CAMPUS, campusId);
          if( status != STATUS_MARK ) {
            Log.showText("INDOOR TYPE: " + LOCTYPE_CAMPUS + ", ID: " + campusId + ", NAME: " + campusName + " -- INSIDE");
            array.add( new IndoorResult(LOCTYPE_CAMPUS, LOCTYPE_S_CAMPUS, campusId, campusName, STATUS_INSIDE) );
          }
          makeMobileInside(mobileId, LOCTYPE_CAMPUS, campusId, campusName, timeStamp);
        }
        
      } // while()
      
      rset.close();
      rset = null;
      pstmt.close();
      pstmt = null;

      sql = "SELECT LOC_TYP,LOC_ID,LOC_NAME FROM LOC_MOBILE WHERE LOC_TYP IN (?,?,?,?) AND STATUS=? AND MOBILE_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setInt(colno++, LOCTYPE_AREA);
      pstmt.setInt(colno++, LOCTYPE_FLOOR);
      pstmt.setInt(colno++, LOCTYPE_VENUE);
      pstmt.setInt(colno++, LOCTYPE_CAMPUS);
      pstmt.setInt(colno++, STATUS_MARK);
      pstmt.setLong(colno++, mobileId);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        int locTyp = rset.getInt(1);
        String locTypName = getLocTypName(locTyp);
        long locId = rset.getLong(2);
        String locName = rset.getString(3);
        
        Log.showText("INDOOR TYPE: " + locTypName + ", ID: " + locId + ", NAME: " + locName + " -- OUTSIDE");
        array.add( new IndoorResult(locTyp, locTypName, locId, locName, STATUS_OUTSIDE) );
        makeMobileOutside(mobileId, locTyp, locId, timeStamp);
      } // while()

    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close();  } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    if( array.size() <= 0 ) return null;

    IndoorResult[] ress = new IndoorResult[array.size()];
    for( int i = 0; i < array.size(); i++ ) ress[i] = (IndoorResult)array.get(i);
    return ress;
  } // checkIndoorForMobile()

//-----------------------------------------------------------------------------

  private void markAllInsideAsSomething(long mobileId) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE LOC_MOBILE SET STATUS=? WHERE LOC_TYP IN (?,?,?,?) AND STATUS=? AND MOBILE_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setInt(colno++, STATUS_MARK);
      pstmt.setInt(colno++, LOCTYPE_AREA);
      pstmt.setInt(colno++, LOCTYPE_FLOOR);
      pstmt.setInt(colno++, LOCTYPE_VENUE);
      pstmt.setInt(colno++, LOCTYPE_CAMPUS);
      pstmt.setInt(colno++, 1); // Inside
      pstmt.setLong(colno++, mobileId);
      pstmt.executeUpdate();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return;
  } // markAllInsideAsSomething()

//-----------------------------------------------------------------------------

  private int getLocMobileStatus(long mobileId, int locTyp, long locId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int status = 0;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT STATUS FROM LOC_MOBILE WHERE MOBILE_ID=? AND LOC_TYP=? AND LOC_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, mobileId);
      pstmt.setInt(colno++, locTyp);
      pstmt.setLong(colno++, locId);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        status = rset.getInt(1);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return status;
  } // markAllInsideAsSomething()

//-----------------------------------------------------------------------------

  private void makeMobileInside(long mobileId, int locTyp, long locId, String locName, String timeStamp) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE LOC_MOBILE SET ENTER_TIME_STAMP=?,EXIT_TIME_STAMP=NULL,STATUS=? WHERE LOC_TYP=? AND LOC_ID=? AND MOBILE_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setTimestamp(colno++, Utils.toTimestamp(timeStamp));
      pstmt.setInt(colno++, STATUS_INSIDE);
      pstmt.setInt(colno++, locTyp);
      pstmt.setLong(colno++, locId);
      pstmt.setLong(colno++, mobileId);
      int count = pstmt.executeUpdate();
      if( count <= 0 ) {
        pstmt.close();
        pstmt = null;
        
        sql = "INSERT INTO LOC_MOBILE (MOBILE_ID,LOC_TYP,LOC_ID,LOC_NAME,STATUS,ENTER_TIME_STAMP,EXIT_TIME_STAMP) VALUES (?,?,?,?,?,?,NULL)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        colno = 1;
        pstmt.setLong(colno++, mobileId);
        pstmt.setInt(colno++, locTyp);
        pstmt.setLong(colno++, locId);
        pstmt.setString(colno++, locName);
        pstmt.setInt(colno++, STATUS_INSIDE);
        pstmt.setTimestamp(colno++, Utils.toTimestamp(timeStamp));
        pstmt.executeUpdate();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return;
  } // makeMobileInside()

//-----------------------------------------------------------------------------

  private void makeMobileOutside(long mobileId, int locTyp, long locId, String timeStamp) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE LOC_MOBILE SET EXIT_TIME_STAMP=?,STATUS=? WHERE LOC_TYP=? AND LOC_ID=? AND MOBILE_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setTimestamp(colno++, Utils.toTimestamp(timeStamp));
      pstmt.setInt(colno++, STATUS_OUTSIDE);
      pstmt.setInt(colno++, locTyp);
      pstmt.setLong(colno++, locId);
      pstmt.setLong(colno++, mobileId);
      pstmt.executeUpdate();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return;
  } // makeMobileOutside()

//-----------------------------------------------------------------------------

  private String getLocTypName(int locTyp) {
    String name = "";
    switch( locTyp ) {
    case LOCTYPE_REGION : name  = LOCTYPE_S_REGION; break;
    case LOCTYPE_CAMPUS : name  = LOCTYPE_S_CAMPUS; break;
    case LOCTYPE_VENUE : name  = LOCTYPE_S_VENUE; break;
    case LOCTYPE_FLOOR : name  = LOCTYPE_S_FLOOR; break;
    case LOCTYPE_AREA : name  = LOCTYPE_S_AREA; break;
    } // switch()
    return name;
  } // getLocTypName()

}
