package com.infotech.locationbox.tracking.platform.base;

import java.sql.*;

public class Mobile extends Object {
  private long id = 0;
  private String name = null;
  private int status = 0;
  private int typ = 0;
  
  public Mobile() {
  }

//-----------------------------------------------------------------------------

  public static Mobile getInstance(ResultSet rset) throws Exception {
    Mobile mobile = new Mobile();
    mobile.id = rset.getLong("MOBILE_ID");
    mobile.name = Utils.checkNullString(rset.getString("MOBILE_NAME"));
    mobile.status = rset.getInt("STATUS");
    mobile.typ = rset.getInt("TYP");
    return mobile;
  } // getInstance()
  
//-----------------------------------------------------------------------------

  public static Mobile getInstance(long mobileId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT * FROM MOBILE WHERE MOBILE_ID=? AND ACTIVE_FLAG=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setLong(1, mobileId);
      pstmt.setInt(2, 1);
      rset = pstmt.executeQuery();
      if( rset.next() ) return getInstance(rset);

    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "Mobile.getInstance");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return null;
  } // getInstance()

//-----------------------------------------------------------------------------

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public void setTyp(int typ) {
    this.typ = typ;
  }

  public int getTyp() {
    return typ;
  }

}
