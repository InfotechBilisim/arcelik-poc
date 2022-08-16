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

public class DataCampus {
  protected long id = 0;
  protected String name = null;
  protected double xcoor = 0.00;
  protected double ycoor = 0.00;
  protected Extent extent = null;
  
  protected DataVenue[] venues = null;

  public DataCampus() {
  }

//-----------------------------------------------------------------------------

  public static DataCampus getInstance(ResultSet rset) {
    DataCampus dc = new DataCampus();

    try {
      dc.id = rset.getLong("CAMPUS_ID");
      dc.name = rset.getString("CAMPUS_NAME");
      dc.xcoor = rset.getDouble("XCOOR");
      dc.ycoor = rset.getDouble("YCOOR");
      Struct obj = (Struct)rset.getObject("GEOMBR");
      if( obj != null ){
        JGeometry geo = JGeometry.loadJS(obj);
        double[] oarray = geo.getOrdinatesArray();
        dc.extent = new Extent(oarray[1], oarray[0], oarray[3], oarray[2]);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return dc;
  } // getInstance()

//-----------------------------------------------------------------------------

  public static DataCampus getInstance(DataRegister dr, long campusId, boolean detailed) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "SELECT CAMPUS_ID,CAMPUS_NAME,XCOOR,YCOOR,GEOMBR FROM INDOOR_CAMPUS WHERE CAMPUS_ID=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, campusId);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        DataCampus dc = getInstance(rset);
        if( detailed ) dc.venues = DataVenue.getVenues(dr, dc.id);
        return dc;
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
    if( venues != null ) {
      s += ",\n  \"venues\": [";
      for( int i = 0; i < venues.length; i++ ) {
        if( i > 0 ) s += ",\n";
        s += "    " + venues[i];
      } // for()
      s += "]";
    }
    s += " }";
    return s;
  }

//-----------------------------------------------------------------------------

  public static DataCampus[] getCampuses(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT CAMPUS_ID,CAMPUS_NAME,XCOOR,YCOOR,GEOMBR FROM INDOOR_CAMPUS ORDER BY CAMPUS_NAME";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataCampus dc = getInstance(rset);
        array.add( dc );
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
    
    DataCampus[] dcs = new DataCampus[array.size()];
    for( int i = 0; i < dcs.length; i++ ) dcs[i] = (DataCampus)array.get(i);
    return dcs;
  } // getCampuses()
  
}
