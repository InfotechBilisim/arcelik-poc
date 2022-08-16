package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class DataLocType {
  protected int type = 0;
  protected String desc = null;
  
  public DataLocType() {
  }

//-----------------------------------------------------------------------------

  public static DataLocType getInstance(ResultSet rset) {
    DataLocType dlt = new DataLocType();

    try {
      dlt.type = rset.getInt("LOC_TYP");
      dlt.desc = rset.getString("LOC_DESC");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return dlt;
  } // getInstance()

//-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"type\": " + type + ", \"desc\": \"" + desc +  "\" }";
  }

//-----------------------------------------------------------------------------

  public static DataLocType[] getLocTypes(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT LOC_TYP,LOC_DESC FROM LOC_TYP ORDER BY LOC_TYP";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataLocType dlt = getInstance(rset);
        array.add( dlt );
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
    
    DataLocType[] dlts = new DataLocType[array.size()];
    for( int i = 0; i < dlts.length; i++ ) dlts[i] = (DataLocType)array.get(i);
    return dlts;
  } // getLocTypes()
  
}
