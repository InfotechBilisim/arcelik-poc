package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Struct;

import java.util.ArrayList;

import javax.json.JsonArray;
import javax.json.JsonObject;

import oracle.spatial.geometry.JGeometry;

public class DataRegion {
  protected long id = 0;
  protected String name = null;
  protected String key = null;
  protected int typ = 9;
  protected String string1 = null;
  protected int number1 = 0;
  
  protected String cmd = null;           // copy, union, coors, circle
  protected long copy = 0;
  protected long[] union = null;
  protected double[] coors = null;
  protected double[] circle = null;

  public DataRegion() {
  }

  public DataRegion(long id) {
    this.id = id;
  }

//-----------------------------------------------------------------------------

  public static DataRegion getInstance(ResultSet rset) {
    DataRegion dr = new DataRegion();

    try {
      dr.id = rset.getLong("REGION_ID");
      dr.name = rset.getString("REGION_NAME");
      dr.key = rset.getString("KEY");
      dr.string1 = rset.getString("STRING_1");
      if( dr.string1 == null ) dr.string1 = "";
      dr.number1 = rset.getInt("NUMBER_1");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return dr;
  } // getInstance()

//-----------------------------------------------------------------------------

  public static DataRegion getInstance(DataRegister dr, long regionId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "SELECT REGION_ID,REGION_NAME,KEY,STRING_1,NUMBER_1 FROM LBS_USER_REGION WHERE REGION_ID=? AND KEY=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, regionId);
      pstmt.setString(colno++, dr.key);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        DataRegion rgn = getInstance(rset);
        return rgn;
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

  public static DataRegion getInstance(JsonObject params) {
    DataRegion rgn = new DataRegion();

    try {
      rgn.id = params.getJsonNumber("id").longValue();
      rgn.name = params.getString("name");
      rgn.key = params.getString("key");
      rgn.string1 = params.getString("string1");
      rgn.number1 = params.getJsonNumber("number1").intValue();
      
      JsonArray aUnion = params.getJsonArray("union");
      if( aUnion != null && aUnion.size() > 0 ) {
        rgn.union = new long[aUnion.size()];
        for( int i = 0; i < rgn.union.length; i++ ) rgn.union[i] = aUnion.getJsonNumber(i).longValue();
      }
      
      JsonArray aCoors = params.getJsonArray("coors");
      if( aCoors != null && aCoors.size() > 0 ) {
        rgn.coors = new double[aCoors.size()];
        for( int i = 0; i < rgn.coors.length; i++ ) rgn.coors[i] = aCoors.getJsonNumber(i).doubleValue();
      }
      
      JsonArray aCircle = params.getJsonArray("circle");
      if( aCircle != null && aCircle.size() > 0 ) {
        rgn.circle = new double[aCircle.size()];
        for( int i = 0; i < rgn.circle.length; i++ ) rgn.circle[i] = aCircle.getJsonNumber(i).doubleValue();
      }
      
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }

    return rgn;
  } // getInstance()

//-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"id\": " + id + ", \"name\": \"" + name +  "\", \"key\": \"" + key + "\", \"typ\": " + typ + ", \"string1\": \"" + string1 + "\", \"number1\": " + number1 + " }";
  }

//-----------------------------------------------------------------------------

  public long insert(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int colno = 0;
    int count = 0;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      id = Utils.getUniqueId("LBS_USER_REGION");

      sql = "INSERT INTO LBS_USER_REGION (REGION_ID,REGION_NAME,KEY,TYP,STRING_1,NUMBER_1,CREATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR) VALUES (?,?,?,?,?,?,SYSDATE,0.00,0.00,";
      if( union == null ) {
        if( circle == null ) sql += "?";
        else
          sql += "SDO_UTIL.CIRCLE_POLYGON(?,?,?,0.001)";
      }
      else {
        if( union.length == 2 )
          sql += "SDO_GEOM.SDO_UNION((SELECT A1.GEOLOC FROM INDOOR_AREA A1 WHERE A1.AREA_ID=?),(SELECT A2.GEOLOC FROM INDOOR_AREA A2 WHERE A2.AREA_ID=?),0.001)";
        else {
          sql += "(SELECT SDO_AGGR_UNION(SDOAGGRTYPE(A.GEOLOC, 0.001)) FROM INDOOR_AREA A WHERE A.AREA_ID IN (";
          for( int i = 0; i < union.length; i++ ) {
            if( i > 0 ) sql += ",?"; else sql += "?";
          } // for()
          sql += "))";
        }
      }
      sql += ",NULL)";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setString(colno++, "" + id);
      pstmt.setString(colno++, name);
      pstmt.setString(colno++, dr.key);
      pstmt.setInt(colno++, typ);
      pstmt.setString(colno++, string1);
      pstmt.setInt(colno++, number1);
      if( union != null ) {
        for( int i = 0; i < union.length; i++ ) pstmt.setLong(colno++, union[i]);
      }
      else
      if( coors != null ) {
        JGeometry geo = JGeometry.createLinearPolygon(coors, 2, 8307);
        Struct obj = JGeometry.storeJS(geo, cnn);
        pstmt.setObject(colno++, obj);
      }
      else
      if( circle != null ) {
        pstmt.setDouble(colno++, circle[0]);
        pstmt.setDouble(colno++, circle[1]);
        pstmt.setDouble(colno++, circle[2]);
      }
      count = pstmt.executeUpdate();
      if( count > 0 ) {
        pstmt.close();
        pstmt = null;

        sql = "UPDATE LBS_USER_REGION UR SET XCOOR=SDO_GEOM.SDO_CENTROID(UR.GEOLOC, 0.001).SDO_POINT.X,YCOOR=SDO_GEOM.SDO_CENTROID(UR.GEOLOC, 0.001).SDO_POINT.Y,GEOMBR=SDO_GEOM.SDO_MBR(UR.GEOLOC) WHERE UR.REGION_ID=? AND UR.KEY=?";
        pstmt = cnn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, "" + id);
        pstmt.setString(2, dr.key);
        pstmt.executeUpdate();

      }
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

  public int delete(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int colno = 0;
    int count = 0;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "DELETE LBS_USER_REGION WHERE REGION_ID=? AND KEY=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setLong(colno++, id);
      pstmt.setString(colno++, dr.key);
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

  public static DataRegion[] getRegions(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT REGION_ID,REGION_NAME,KEY,STRING_1,NUMBER_1 FROM LBS_USER_REGION WHERE KEY=? ORDER BY REGION_NAME";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, dr.key);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataRegion rgn = getInstance(rset);
        array.add( rgn );
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
    
    DataRegion[] drs = new DataRegion[array.size()];
    for( int i = 0; i < drs.length; i++ ) drs[i] = (DataRegion)array.get(i);
    return drs;
  } // getRegions()
  
}
