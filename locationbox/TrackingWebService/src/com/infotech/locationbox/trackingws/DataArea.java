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

public class DataArea {
  protected long id = 0;
  protected String name = null;
  protected long venueId = 0;
  protected int floor = 0;
  protected int type = 0;
  protected int subType = 0;
  protected double xcoor = 0.00;
  protected double ycoor = 0.00;
  protected Extent extent = null;

  public DataArea() {
  }

//-----------------------------------------------------------------------------

  public static DataArea getInstance(ResultSet rset) {
    DataArea da = new DataArea();

    try {
      da.id = rset.getLong("AREA_ID");
      da.name = rset.getString("AREA_NAME");
      da.venueId = rset.getLong("VENUE_ID");
      da.floor = rset.getInt("FLOOR");
      da.type = rset.getInt("TYPE");
      da.subType = rset.getInt("SUB_TYPE");
      da.xcoor = rset.getDouble("XCOOR_CENTER");
      da.ycoor = rset.getDouble("YCOOR_CENTER");
      Struct obj = (Struct)rset.getObject("GEOMBR");
      if( obj != null ){
        JGeometry geo = JGeometry.loadJS(obj);
        double[] oarray = geo.getOrdinatesArray();
        da.extent = new Extent(oarray[1], oarray[0], oarray[3], oarray[2]);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return da;
  } // getInstance()

//-----------------------------------------------------------------------------

  public static DataArea getInstance(DataRegister dr, long areaId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "SELECT AREA_ID,AREA_NAME,VENUE_ID,FLOOR,TYPE,SUB_TYPE,XCOOR_CENTER,YCOOR_CENTER,SDO_GEOM.SDO_MBR(GEOLOC) GEOMBR FROM INDOOR_AREA WHERE AREA_ID=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, areaId);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        DataArea da = getInstance(rset);
        return da;
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
    return "{ \"id\": " + id + ", \"name\": \"" + name +  "\", \"venueid\": " + venueId + ", \"floor\": " + floor + ", \"type\": " + type + ", \"subtype\": " + subType + ", \"xcoor\": " + xcoor + ", \"ycoor\": " + ycoor + ", \"extent\": " + extent + " }";
  }

//-----------------------------------------------------------------------------

  public static DataArea[] getAreas(DataRegister dr, long venueId, int floor) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT AREA_ID,AREA_NAME,VENUE_ID,FLOOR,TYPE,SUB_TYPE,XCOOR_CENTER,YCOOR_CENTER,SDO_GEOM.SDO_MBR(GEOLOC) GEOMBR FROM INDOOR_AREA WHERE VENUE_ID=? AND FLOOR=? ORDER BY AREA_NAME";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, venueId);
      pstmt.setInt(colno++, floor);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataArea da = getInstance(rset);
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
    
    DataArea[] das = new DataArea[array.size()];
    for( int i = 0; i < das.length; i++ ) das[i] = (DataArea)array.get(i);
    return das;
  } // getAreas()
  
}
