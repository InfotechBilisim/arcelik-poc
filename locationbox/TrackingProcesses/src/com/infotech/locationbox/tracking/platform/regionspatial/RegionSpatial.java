package com.infotech.locationbox.tracking.platform.regionspatial;

import com.infotech.locationbox.tracking.platform.base.*;
import java.sql.*;
import java.util.*;

public class RegionSpatial {
  public static final int STATUS_UNKNOWN     = 0;
  public static final int STATUS_INSIDE      = 1;
  public static final int STATUS_OUTSIDE     = 2;
  public static final int STATUS_MARK        = 3;

  public static final int LOCTYPE_REGION     = 1;

  public RegionSpatial() {
  }

//-----------------------------------------------------------------------------

  public RegionResult[] checkRegionsForMobile(String key, long mobileId, double xcoor, double ycoor, int floor, String timeStamp) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int colno = 0;

    ArrayList array = new ArrayList();

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      markAllInsideAsSomething(mobileId);
      
      sql = "SELECT REGION_ID,REGION_NAME FROM LBS_USER_REGION WHERE KEY=? AND (NUMBER_1 IS NULL OR NUMBER_1=?) AND SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE'";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setString(colno++, key);
      pstmt.setInt(colno++, floor);
      pstmt.setDouble(colno++, xcoor);
      pstmt.setDouble(colno++, ycoor);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        long regionId = rset.getLong("REGION_ID");
        String regionName = rset.getString("REGION_NAME");

        int status = getLocMobileStatus(mobileId, LOCTYPE_REGION, regionId);
        if( status != STATUS_MARK ) {
          Log.showText("REGION ID: " + regionId + ", NAME: " + regionName + " -- INSIDE");
          array.add( new RegionResult(regionId, regionName, STATUS_INSIDE) );
        }
        makeMobileInside(mobileId, LOCTYPE_REGION, regionId, regionName, timeStamp);
      } // while()
      
      rset.close();
      rset = null;
      pstmt.close();
      pstmt = null;

      sql = "SELECT LOC_ID,LOC_NAME FROM LOC_MOBILE WHERE LOC_TYP IN (?) AND STATUS=? AND MOBILE_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setInt(colno++, LOCTYPE_REGION);
      pstmt.setInt(colno++, STATUS_MARK);
      pstmt.setLong(colno++, mobileId);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        long locId = rset.getLong(1);
        String locName = rset.getString(2);
        
        Log.showText("REGION ID: " + locId + ", NAME: " + locName + " -- OUTSIDE");
        array.add( new RegionResult(locId, locName, STATUS_OUTSIDE) );
        makeMobileOutside(mobileId, LOCTYPE_REGION, locId, timeStamp);
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

    RegionResult[] rres = new RegionResult[array.size()];
    for( int i = 0; i < array.size(); i++ ) {
      RegionResult res = (RegionResult)array.get(i);
      rres[i] = res;
    } // for()

    return( rres );
  } // checkRegionsForMobile()

//-----------------------------------------------------------------------------

  private void markAllInsideAsSomething(long mobileId) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE LOC_MOBILE SET STATUS=? WHERE LOC_TYP IN (?) AND STATUS=? AND MOBILE_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setInt(colno++, STATUS_MARK);
      pstmt.setInt(colno++, LOCTYPE_REGION);
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
      sql = "UPDATE LOC_MOBILE SET ENTER_TIME_STAMP=?,EXIT_TIME_STAMP=NULL,STATUS=? WHERE LOC_ID=? AND LOC_TYP=? AND MOBILE_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setTimestamp(colno++, Utils.toTimestamp(timeStamp));
      pstmt.setInt(colno++, STATUS_INSIDE);
      pstmt.setLong(colno++, locId);
      pstmt.setInt(colno++, locTyp);
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

  private void makeMobileOutside(long mobileId, int locType, long locId, String timeStamp) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE LOC_MOBILE SET EXIT_TIME_STAMP=?,STATUS=? WHERE LOC_ID=? AND LOC_TYP=? AND MOBILE_ID=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setTimestamp(colno++, Utils.toTimestamp(timeStamp));
      pstmt.setInt(colno++, STATUS_OUTSIDE);
      pstmt.setLong(colno++, locId);
      pstmt.setInt(colno++, locType);
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

}
