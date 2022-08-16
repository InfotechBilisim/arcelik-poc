package com.infotech.locationbox.tracking.platform.base;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EncryptionTools {
  private static final String key1 = "A1B390F12D243680";
  private static final String key2 = "132FD66F5009895C";
  private static final String key3 = "06F58436588321FF";
  private static final String key4 = "0123456789ABCDEF";

  public EncryptionTools() {
  }
  
//-----------------------------------------------------------------------------

  public static String encryptData(SystemObject so, long num) {
    return encryptData(so, "" + num);
  } // encryptData()

//-----------------------------------------------------------------------------

  public static String encryptData(SystemObject so, String txt) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT ENCRYPTION.ENCRYPT(?,?,?,?,?) FROM DUAL";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, txt);
      pstmt.setString(2, key1);
      pstmt.setString(3, key2);
      pstmt.setString(4, key3);
      pstmt.setString(5, key4);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        String estr = rset.getString(1);
        return estr;
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "EncryptionTools.encryptData");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    
    return null;
  } // encryptData()

//-----------------------------------------------------------------------------

  public static String decryptData(SystemObject so, String estr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT ENCRYPTION.DECRYPT(?,?,?,?) FROM DUAL";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, estr);
      pstmt.setString(2, key1);
      pstmt.setString(3, key2);
      pstmt.setString(4, key3);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        String txt = rset.getString(1);
        return txt;
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "EncryptionTools.decryptData");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    
    return null;
  } // decryptData()

}
