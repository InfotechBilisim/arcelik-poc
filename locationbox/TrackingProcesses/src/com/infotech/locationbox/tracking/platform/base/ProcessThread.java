package com.infotech.locationbox.tracking.platform.base;

import java.sql.*;

public class ProcessThread implements Runnable {
  protected SystemObject so = null;
  protected String tableName = null;
  protected String whereAddition = null;
  protected int sleepDuration = 10;
  protected int processDayCount = 10;
  protected int commitDuration = 0;
  protected int commitCount = 0;
  protected int commitCounter = 0;
  protected java.util.Date commitDate = new java.util.Date();
  protected java.util.Date startDate = new java.util.Date();
  protected boolean running = false;
  protected int processStatus = 0;

  public ProcessThread() {
  }

  public ProcessThread(String tableName, int  sleepDuration, int processDayCount, SystemObject so) {
    this.tableName = tableName;
    this.sleepDuration = sleepDuration;
    this.processDayCount = processDayCount;
    this.so = so;
  }

  public void run() {
    DbConnection dbCnn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    running = true;
    while( running ) {
      if( DbConnection.isConnectionPool ) dbCnn = DbConnection.getPooledConnection();
      else {
        dbCnn = DbConnection.getPooledConnection();
        if( commitDuration > 0 && commitCount > 0 ) {
          try { dbCnn.setAutoCommit(false); } catch (Exception ex) { ex.printStackTrace(); }
        }
      } // else

      try {
        if( tableName.equals("INBOX") ) {
          sql = "alter session set cursor_sharing=exact";
          pstmt = dbCnn.prepareStatement(sql);
          pstmt.clearParameters();
          pstmt.execute();
          pstmt.close();
          pstmt = null;
        }

        sql  = "SELECT " + (tableName.equals("TRANS") || tableName.equals("TRANSACTION") ? "/*+ INDEX(TRANS TRANS_INX_06) */ " : "") + "* FROM " + tableName;
        sql += " WHERE " + (tableName.equals("TRANS") || tableName.equals("TRANSACTION") ? "POS_TIME_STAMP" : (tableName.indexOf("_NAKLIYE") > 0 ? "OPERASYON_TARIHI" : "TIME_STAMP")) + " > SYSDATE-" + processDayCount;
        sql += " AND PROCESS_STATUS=" + processStatus;
        if( tableName.equals("INBOX") )
          sql += so.mobileRangeClause(true);
        else {
          if( !tableName.equals("OUTBOX") && !tableName.equals("USERS_REPORT_REQUEST") )
            sql += so.mobileRangeClause();
        }
        sql += (whereAddition == null ? "" : " AND " + whereAddition);
        sql += " ORDER BY " + (tableName.indexOf("_NAKLIYE") > 0 ? "NAKLIYE_NUMARASI" : "ROWNO");

// Log.showText("SQL: " + sql);
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        if( !tableName.equals("OUTBOX") && !tableName.equals("USERS_REPORT_REQUEST") ) so.mobileRangeParams(pstmt, 1);
        rset = pstmt.executeQuery();
// Log.showText("SELECTION FINISHED.");
        if( rset != null ) {
          int cnt = 0;
          while( rset.next() ) {
            doOperation(rset);
            cnt++;
            
            if( !DbConnection.isConnectionPool ) {
              if( commitDuration > 0 && commitCount > 0 ) {
                commitCounter++;
                if( commitCounter >= commitCount ) {
                  Log.showText("Commit count !");
                  try { dbCnn.commit(); } catch (Exception ex) { ex.printStackTrace(); }
                  commitDate = new java.util.Date();
                  commitCounter = 0;
                }
              } // if(commit)
            } // if()

          } // while()
          endOperation(cnt);
        } // if()

        if( !DbConnection.isConnectionPool ) {
          if( commitDuration > 0 && commitCount > 0 ) {
            java.util.Date dt = new java.util.Date();
            long tm = (dt.getTime() - commitDate.getTime()) / 1000;
            if( tm > commitDuration ) {
              Log.showText("Commit duration !");
              try { dbCnn.commit(); } catch (Exception ex) { ex.printStackTrace(); }
              commitDate = dt;
              commitCounter = 0;
            }
          } // if(commit)
        } // if()

      }
      catch (Exception ex) {
        ex.printStackTrace();
        dbCnn.checkSQLException(ex);
        Log.logException(1, "Exception occured in " + tableName + " ProcessThread.run() function !", tableName + " ProcessThread.Run");
      }
      finally {
        try { if( rset != null ) rset.close(); rset = null; } catch (Exception e) {;}
        try { if( pstmt != null ) pstmt.close(); pstmt = null; } catch (Exception e) {;}
        try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
      }

      try { Thread.sleep(sleepDuration * 1000); } catch (Exception e) {;}
    } // while()

    if( !DbConnection.isConnectionPool ) {
      if( commitDuration > 0 && commitCount > 0 ) {
        Log.showText("Commit while exiting !");
        try { dbCnn.commit(); } catch (Exception ex) { ex.printStackTrace(); }
      } // if(commit)
    } // if()

    return;
  } // run()
  
//-----------------------------------------------------------------------------

  public void dbMarkProcessed(String tableName, long rowNo) {
    so.dbMarkProcessed(tableName, processDayCount, rowNo);
    return;
  } // dbMarkProcessed()
  
//------------------------------------------------------------------------------

  public void dbMarkProcessed(String tableName, long rowNo, int status) {
    so.dbMarkProcessed(tableName, processDayCount, rowNo, status);
    return;
  } // dbMarkProcessed()

//-----------------------------------------------------------------------------

  public void stopThread() {
    running = false;
    return;
  } // stopThread()

  public void stopOperation() {
    return;
  } // stopOperation()
  
  public void doOperation(ResultSet rset) {
    return;
  } // doOperation()

  public void endOperation(int count) {
    return;
  } // endOperation()

  public String getWhereAddition() {
    return whereAddition;
  }

  public void setWhereAddition(String whereAddition) {
    this.whereAddition = whereAddition;
  }

}
