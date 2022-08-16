package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;
import com.infotech.locationbox.utils.Extent;

import com.infotech.locationbox.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Struct;

import java.util.ArrayList;

import oracle.spatial.geometry.JGeometry;

public class DataFloor {
  protected long venueId = 0;
  protected int floor = 0;
  protected String name = null;
  protected double xcoor = 0.00;
  protected double ycoor = 0.00;
  protected Extent extent = null;
  
  protected DataArea[] areas = null;

  public DataFloor() {
  }

//-----------------------------------------------------------------------------

  public static DataFloor getInstance(ResultSet rset) {
    DataFloor df = new DataFloor();

    try {
      df.venueId = rset.getLong("VENUE_ID");
      df.floor = rset.getInt("FLOOR");
      df.name = rset.getString("FLOOR_NAME");
      Struct obj = (Struct)rset.getObject("GEOMBR");
      if( obj != null ){
        JGeometry geo = JGeometry.loadJS(obj);
        double[] oarray = geo.getOrdinatesArray();
        df.extent = new Extent(oarray[1], oarray[0], oarray[3], oarray[2]);
        df.xcoor = df.extent.getCenterX();
        df.ycoor = df.extent.getCenterY();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return df;
  } // getInstance()

//-----------------------------------------------------------------------------

  public static DataFloor getInstance(DataRegister dr, long venueId, int floor, boolean detailed) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "SELECT VENUE_ID,FLOOR,FLOOR_NAME,SDO_GEOM.SDO_MBR(GEOLOC) GEOMBR FROM INDOOR_VENUE_FLOOR WHERE VENUE_ID=? AND FLOOR=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, venueId);
      pstmt.setInt(colno++, floor);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        DataFloor df = getInstance(rset);
        if( detailed ) df.areas = DataArea.getAreas(dr, df.venueId, df.floor);
        return df;
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

  public String toString() {
    String s = "{ \"venueid\": " + venueId + ", \"floor\": " + floor + ", \"name\": \"" + name +  "\", \"xcoor\": " + xcoor + ", \"ycoor\": " + ycoor + ", \"extent\": " + extent;
    if( areas != null ) {
      s += ",\n  \"areas\": [";
      for( int i = 0; i < areas.length; i++ ) {
        if( i > 0 ) s += ",\n";
        s += "    " + areas[i];
      } // for()
      s += "]";
    }
    s += " }";
    return s;
  }

//-----------------------------------------------------------------------------

  public static DataFloor[] getFloors(DataRegister dr, long venueId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT VENUE_ID,FLOOR,FLOOR_NAME,SDO_GEOM.SDO_MBR(GEOLOC) GEOMBR FROM INDOOR_VENUE_FLOOR WHERE VENUE_ID=? ORDER BY FLOOR_NAME";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, venueId);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataFloor df = getInstance(rset);
        array.add( df );
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
    
    DataFloor[] dfs = new DataFloor[array.size()];
    for( int i = 0; i < dfs.length; i++ ) dfs[i] = (DataFloor)array.get(i);
    return dfs;
  } // getFloors()
  
}
