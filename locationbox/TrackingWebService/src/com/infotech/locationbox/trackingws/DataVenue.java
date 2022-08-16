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

public class DataVenue {
  protected long id = 0;
  protected String name = null;
  protected double xcoor = 0.00;
  protected double ycoor = 0.00;
  protected Extent extent = null;
  
  protected DataFloor[] floors = null;

  public DataVenue() {
  }

//-----------------------------------------------------------------------------

  public static DataVenue getInstance(ResultSet rset) {
    DataVenue dv = new DataVenue();

    try {
      dv.id = rset.getLong("VENUE_ID");
      dv.name = rset.getString("VENUE_NAME");
      dv.xcoor = rset.getDouble("XCOOR");
      dv.ycoor = rset.getDouble("YCOOR");
      Struct obj = (Struct)rset.getObject("GEOMBR");
      if( obj != null ){
        JGeometry geo = JGeometry.loadJS(obj);
        double[] oarray = geo.getOrdinatesArray();
        dv.extent = new Extent(oarray[1], oarray[0], oarray[3], oarray[2]);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return dv;
  } // getInstance()

//-----------------------------------------------------------------------------

  public static DataVenue getInstance(DataRegister dr, long venueId, boolean detailed) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "SELECT VENUE_ID,VENUE_NAME,XCOOR,YCOOR,GEOMBR FROM INDOOR_VENUE WHERE VENUE_ID=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, venueId);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        DataVenue dv = getInstance(rset);
        if( detailed ) dv.floors = DataFloor.getFloors(dr, dv.id);
        return dv;
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
    String s = "{ \"id\": " + id + ", \"name\": \"" + name +  "\", \"xcoor\": " + xcoor + ", \"ycoor\": " + ycoor + ", \"extent\": " + extent;
    if( floors != null ) {
      s += ",\n  \"floors\": [";
      for( int i = 0; i < floors.length; i++ ) {
        if( i > 0 ) s += ",\n";
        s += "    " + floors[i];
      } // for()
      s += "]";
    }
    s += " }";
    return s;
  }

//-----------------------------------------------------------------------------

  public static DataVenue[] getVenues(DataRegister dr, long campusId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT VENUE_ID,VENUE_NAME,XCOOR,YCOOR,GEOMBR FROM INDOOR_VENUE WHERE CAMPUS_ID=? ORDER BY VENUE_NAME";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, campusId);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataVenue dv = getInstance(rset);
        array.add( dv );
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
    
    DataVenue[] dvs = new DataVenue[array.size()];
    for( int i = 0; i < dvs.length; i++ ) dvs[i] = (DataVenue)array.get(i);
    return dvs;
  } // getVenues()
  
}
