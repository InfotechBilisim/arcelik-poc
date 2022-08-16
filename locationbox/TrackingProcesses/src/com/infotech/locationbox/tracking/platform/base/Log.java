package com.infotech.locationbox.tracking.platform.base;

import java.io.FileWriter;

import java.sql.*;

public class Log extends Object {
  private static SystemObject so = null;
  public static String prefix = null;

  public static final int INFO_DATABASE_CONNECTION_ESTABLISHED= 11;

  public static final int EXCEPTION_MOBILE_NOT_FOUND          = 80001;
  public static final int EXCEPTION_UNKNOWN_MESSAGE_TYPE      = 80002;
  public static final int EXCEPTION_SEVK_EMRI_DETAY_NOT_FOUND = 80003;
  public static final int EXCEPTION_TESLIMAT_NOT_FOUND        = 80004;
  public static final int EXCEPTION_BAYI_NOT_FOUND            = 80005;
  public static final int EXCEPTION_SHORT_PACKET              = 80006;
  public static final int EXCEPTION_WORKORDER_NOT_FOUND       = 80007;
  public static final int EXCEPTION_BRANCH_NOT_FOUND          = 80008;
  public static final int EXCEPTION_MOBILE_STATUS_NOT_FOUND   = 80009;
  public static final int EXCEPTION_MSISDN_NOT_FOUND          = 80010;
  public static final int EXCEPTION_ISEMRI_NOT_FOUND          = 80011;

  public Log() {
  }

//-----------------------------------------------------------------------------

  public static void initialize(SystemObject pSo, String pPrefix) {
    so = pSo;
    prefix = pPrefix;
  } // initialize()

//-----------------------------------------------------------------------------

  public synchronized static void logToFile(String txt) {
    if( prefix == null ) return;

    FileWriter out = null;
    String fullPath = prefix + "_" + Utils.getCurrentDate() + ".log";
    try {
      out = new FileWriter(fullPath, true);
      out.write(Utils.getCurrentDateTime() + " : " + txt + "\n");
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    finally {
      try { if( out != null ) out.close(); } catch (Exception e) {;}
    }
    return;
  } // logToFile()

//-----------------------------------------------------------------------------

  public synchronized static void logToDebugFile(String txt) {
    if( prefix == null ) return;

    FileWriter out = null;
    String fullPath = prefix + "_" + Utils.getCurrentDate() + "_debug.log";
    try {
      out = new FileWriter(fullPath, true);
      out.write(Utils.getCurrentDateTime() + " : " + txt + "\n");
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    finally {
      try { if( out != null ) out.close(); } catch (Exception e) {;}
    }
    return;
  } // logToDebugFile()

//-----------------------------------------------------------------------------

  public static void showTextForService(String txt) {
    System.out.println(Utils.getCurrentDateTime() + " : " + txt);
    Log.logToFile(txt);
    return;
  } // showTextForService()

//-----------------------------------------------------------------------------

  public static void showText(String txt) {
    System.out.println(Utils.getCurrentDateTime() + " : " + txt);
    Log.logToFile(txt);
    if( so != null && so.srvListener != null ) so.srvListener.sendStringToAll(txt);
    return;
  } // showText()

//-----------------------------------------------------------------------------

  public static void showError(String txt) {
    System.err.println(Utils.getCurrentDateTime() + " : " + txt);
    Log.logToFile(txt);
    if( so != null && so.srvListener != null ) so.srvListener.sendStringToAll(txt);
    return;
  } // showError()

//-----------------------------------------------------------------------------

  public static void logException(int no, String data, String procedureName) {
    logException(no, data, procedureName, null, null, null, null, null, "tr", 0, null);
    return;
  } // logException()

//-----------------------------------------------------------------------------

  public static void logException(int no, String data,  String procedureName, String alarmGsmNumber, String alarmEMail, String alarmTwitterName, String alarmUsername, String alarmIvrNumber, String alarmLang) {
    logException(no, data, procedureName, alarmGsmNumber, alarmEMail, alarmTwitterName, alarmUsername, alarmIvrNumber, alarmLang, 0, null);
    return;
  } // logException()

//-----------------------------------------------------------------------------

  public static void logException(int no, String data, String procedureName, String alarmGsmNumber, String alarmEMail, String alarmTwitterName, String alarmUsername, String alarmIvrNumber, String alarmLang, long transactionRowNo, String posTimeStamp) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    data = data.replace('\'','`');
    if( data.length() > 3500 ) data = data.substring(0, 3500);

    String exceptionType = null;
    String exceptionDesc = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT * FROM SYS_EXCEPTION WHERE EXCEPTION_NO=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setInt(1, no);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        exceptionType = rset.getString("EXCEPTION_TYPE");
        exceptionDesc = rset.getString("EXCEPTION_DESCRIPTION");
      }
      rset.close();
      rset = null;
      pstmt.close();
      pstmt = null;
      
      if( exceptionType == null || exceptionType.length() <= 0 ) {
        Log.showError("*** ERROR - NO: " + no + ", DATA: " + data + ", Could not find exception data from SYS_EXCEPTION !");
        return;
      }

      long rowNo = so.dbGetUniqueRowNo("SYS_LOG");
// Log.showError("ROWNO: " + rowNo + ", EXCEPTION NO: " + no + ", USERNAME: " + alarmUsername);

      sql = "INSERT INTO SYS_LOG (ROWNO,MODULE_NAME,MODULE_INSTANCE,TIME_STAMP,EXCEPTION_NO,EXCEPTION_TYPE,EXCEPTION_DESCRIPTION,EXCEPTION_DATA,EXCEPTION_ORIGIN,ALARM_GSM_NUMBER,ALARM_EMAIL,ALARM_TWITTER_NAME,ALARM_USERNAME,ALARM_IVR_NUMBER,ALARM_LANG,TRANSACTION_ROWNO,POS_TIME_STAMP) VALUES (?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, rowNo);
      pstmt.setString(colno++, so.moduleName);
      pstmt.setInt(colno++, so.moduleInstance);
      pstmt.setInt(colno++, no);
      pstmt.setString(colno++, exceptionType);
      pstmt.setString(colno++, exceptionDesc);
      pstmt.setString(colno++, data);
      if( procedureName == null ) pstmt.setNull(colno++, Types.VARCHAR); else pstmt.setString(colno++, procedureName);
      if( alarmGsmNumber == null ) pstmt.setNull(colno++, Types.VARCHAR); else pstmt.setString(colno++, alarmGsmNumber.trim());
      if( alarmEMail == null ) pstmt.setNull(colno++, Types.VARCHAR); else pstmt.setString(colno++, alarmEMail.trim());
      if( alarmTwitterName == null ) pstmt.setNull(colno++, Types.VARCHAR); else pstmt.setString(colno++, alarmTwitterName.trim());
      if( alarmUsername == null ) pstmt.setNull(colno++, Types.VARCHAR); else pstmt.setString(colno++, alarmUsername.trim());
      if( alarmIvrNumber == null ) pstmt.setNull(colno++, Types.VARCHAR); else pstmt.setString(colno++, alarmIvrNumber.trim());
      if( alarmLang == null ) pstmt.setNull(colno++, Types.VARCHAR); else pstmt.setString(colno++, alarmLang.trim());
      pstmt.setLong(colno++, transactionRowNo);
      if( posTimeStamp == null ) pstmt.setNull(colno++, Types.DATE); else pstmt.setTimestamp(colno++, Utils.toTimestamp(posTimeStamp));
      pstmt.executeUpdate();
    }
    catch (Exception ex) {
      Log.showError("SQL: " + sql);
      Log.showError("NO: " + no + ", DATA: " + data);
      Log.showError("PROCEDURE NAME: " + procedureName);
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    
    return;
  } // logException()

}
