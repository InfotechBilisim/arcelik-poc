package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class DataEventCode {
  protected String code = null;
  protected String desc = null;
  
  public DataEventCode() {
  }

//-----------------------------------------------------------------------------

  public static DataEventCode getInstance(ResultSet rset) {
    DataEventCode dec = new DataEventCode();

    try {
      dec.code = rset.getString("EVENT_CODE");
      dec.desc = rset.getString("EVENT_DESC");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return dec;
  } // getInstance()

//-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"code\": \"" + code + "\", \"desc\": \"" + desc +  "\" }";
  }

//-----------------------------------------------------------------------------

  public static DataEventCode[] getEventCodes(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT EVENT_CODE,EVENT_DESC FROM MOBILE_EVENT_CODE ORDER BY EVENT_CODE";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataEventCode dec = getInstance(rset);
        array.add( dec );
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
    
    DataEventCode[] decs = new DataEventCode[array.size()];
    for( int i = 0; i < decs.length; i++ ) decs[i] = (DataEventCode)array.get(i);
    return decs;
  } // getEventCodes()
  
}
