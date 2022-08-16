package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.utils.Utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Types;

import java.util.ArrayList;

import javax.json.JsonObject;

public class DataMobile {
  protected long id = 0;
  protected long deviceId = 0;
  protected String custId = null;
  protected String name = null;
  protected String key = null;
  protected int typ = 0;
  protected int status = 0;
//  protected String filter1 = null;
//  protected String filter2 = null;

  public DataMobile() {
  }

  public DataMobile(long id) {
    this.id = id;
  }

//-----------------------------------------------------------------------------

  public static DataMobile getInstance(ResultSet rset) {
    DataMobile dm = new DataMobile();

    try {
      dm.id = rset.getLong("MOBILE_ID");
      dm.deviceId = rset.getLong( "DEVICE_ID" );
      dm.custId = rset.getString( "CUST_ID" );
      dm.name = rset.getString("MOBILE_NAME");
      dm.key = rset.getString("KEY");
      dm.typ = rset.getInt("TYP");
      dm.status = rset.getInt("STATUS");
/*
      dm.filter1 = rset.getString("FILTER1");
      if( dm.filter1 == null ) dm.filter1 = "";
      dm.filter2 = rset.getString("FILTER2");
      if( dm.filter2 == null ) dm.filter2 = "";
*/
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return dm;
  } // getInstance()

//-----------------------------------------------------------------------------

  public static DataMobile getInstance(DataRegister dr, long mobileId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    try {
      sql = "SELECT MOBILE_ID,DEVICE_ID,CUST_ID,MOBILE_NAME,KEY,TYP,STATUS FROM MOBILE WHERE MOBILE_ID=? AND KEY=? AND ACTIVE_FLAG=1";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, mobileId);
      pstmt.setString(colno++, dr.key);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        DataMobile dm = getInstance(rset);
        return dm;
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

  public static DataMobile getInstance(JsonObject params) {
    DataMobile dm = new DataMobile();

    try {
      try { dm.id = params.getJsonNumber( "id" ).longValue(); }  catch(Exception e) { dm.id = 0; }
      try { dm.deviceId = params.getJsonNumber( "deviceid" ).longValue(); }  catch(Exception e) { dm.deviceId = 0; }
      try { dm.custId = params.getString( "custid" ); }  catch(Exception e) { dm.custId = null; }
      dm.name = params.getString("name");
      dm.key = params.getString("key");
      dm.status = params.getJsonNumber("status").intValue();
      dm.typ = params.getJsonNumber("typ").intValue();
//      dm.filter1 = params.getString("filter1");
//      dm.filter2 = params.getString("filter2");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }

    return dm;
  } // getInstance()

//-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"id\": " + id + ", \"name\": \"" + name +  "\", \"deviceid\": " + deviceId + ", \"custid\": " + (custId != null ? "\"" + custId + "\"" : null) + ", \"key\": \"" + key + "\", \"typ\": " + typ + ", \"status\": " + status + " }";
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
      id = produceMobileId();
      if( id <= 0 ) return -1;
      
      // mobile kontrolu..
      // mobile unique uretilmis olsa bile, ayni key catisi altinda device_id ve cust_id tekrar edemez..
      sql = "SELECT COUNT(*) FROM MOBILE WHERE MOBILE_ID=? OR ((DEVICE_ID > 0 AND DEVICE_ID=?) OR (CUST_ID IS NOT NULL AND CUST_ID=?) AND KEY=?)";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setLong(colno++, id);
      pstmt.setLong( colno++, deviceId);
      pstmt.setString( colno++, custId);
      pstmt.setString( colno++, dr.key);
      rset = pstmt.executeQuery();
      rset.next();
      int cnt = rset.getInt(1);
      if( cnt > 0 ) return -2;
      
      rset.close();
      rset = null;
      pstmt.close();
      pstmt = null;

      sql = "INSERT INTO MOBILE (MOBILE_ID,MOBILE_NAME,KEY,TYP,STATUS,ACTIVE_FLAG,DEVICE_ID,CUST_ID) VALUES (?,?,?,?,0,1,?,?)";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setLong(colno++, id);
      pstmt.setString(colno++, name);
      pstmt.setString(colno++, dr.key);
      pstmt.setInt(colno++, typ);
      pstmt.setLong( colno++, deviceId );
      pstmt.setString( colno++, custId );
      count = pstmt.executeUpdate();
    }
    catch (Exception ex) {
            Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
      return -9;
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
      sql = "UPDATE MOBILE SET MOBILE_NAME=?,TYP=? WHERE MOBILE_ID=? AND KEY=? AND ACTIVE_FLAG=1";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setString(colno++, name);
      pstmt.setInt(colno++, typ);
      pstmt.setLong(colno++, id);
      pstmt.setString(colno++, dr.key);
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
      sql = "SELECT COUNT(*) FROM LOC_MOBILE WHERE MOBILE_ID=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setLong(1, id);
      rset = pstmt.executeQuery();
      rset.next();
      count = rset.getInt(1);
      if( count > 0 ) return -1;
      
      rset.close();
      rset = null;
      pstmt.close();
      pstmt = null;
      
      sql = "DELETE MOBILE WHERE MOBILE_ID=? AND KEY=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setLong(colno++, id);
      pstmt.setString(colno++, dr.key);
      count = pstmt.executeUpdate();
      if( count <= 0 ) return -3;
      
    }
    catch (Exception ex) {
            Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
      return -9;
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { cnn.close(); } catch (Exception e) {;}
    }
    
    return 0;
  } // delete()

//-----------------------------------------------------------------------------

  public static DataMobile[] getMobiles(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    ArrayList array = new ArrayList();

    try {
      sql = "SELECT MOBILE_ID,DEVICE_ID,CUST_ID,MOBILE_NAME,KEY,TYP,STATUS FROM MOBILE WHERE KEY=? AND ACTIVE_FLAG=1 ORDER BY MOBILE_NAME";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, dr.key);
      rset = pstmt.executeQuery();
      while( rset.next() ) {
        DataMobile dm = getInstance(rset);
        array.add( dm );
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
    
    DataMobile[] dms = new DataMobile[array.size()];
    for( int i = 0; i < dms.length; i++ ) dms[i] = (DataMobile)array.get(i);
    return dms;
  } // getMobiles()
  
//-----------------------------------------------------------------------------
  
  public static long produceMobileId() {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();
    
    long id = 0;

    try {
      sql = "SELECT SEQ_MOBILE_ID.NEXTVAL FROM DUAL";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        id = rset.getLong(1);
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
    
    return id;
  } // produceMobileId()
  
  
//-----------------------------------------------------------------------------
    
   public static long checkOrCreateMobileId(long deviceId, String custId, String key) {
     CallableStatement cstmt = null;
     ResultSet rset = null;
     String sql = null;

     Connection cnn = DbConn.getPooledConnection();      
     long id = 0;

     try {
       sql = "{ ? = call INDOOR_CHECK_CREATE_MOBILE_ID(?, ?, ?) }";
       cstmt = cnn.prepareCall(sql);
       cstmt.registerOutParameter(1, Types.NUMERIC);
       cstmt.setLong(2, deviceId);
       cstmt.setString(3, custId);
       cstmt.setString(4, key);
       cstmt.execute();
       id = cstmt.getLong(1);
     }
     catch (Exception ex) {
            Utils.showError("Exception message: " + ex.getMessage());
       ex.printStackTrace();
     }
     finally {
       try { if( rset != null ) rset.close(); } catch (Exception e) {;}
       try { if( cstmt != null ) cstmt.close(); } catch (Exception e) {;}
       try { cnn.close(); } catch (Exception e) {;}
     }
      
     return id;
   } // checkOrCreateMobileId()
  
}
