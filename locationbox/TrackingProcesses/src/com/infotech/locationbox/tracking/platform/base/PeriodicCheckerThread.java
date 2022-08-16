package com.infotech.locationbox.tracking.platform.base;

import java.sql.PreparedStatement;
import oracle.ucp.UniversalConnectionPool;
import oracle.ucp.UniversalConnectionPoolStatistics;

public class PeriodicCheckerThread extends Thread {
  private SystemObject so = null;
  private int pollInterval = 60;
  private boolean running = false;

  public PeriodicCheckerThread() {
  }

  public PeriodicCheckerThread(int pollInterval, SystemObject so) {
    this.pollInterval = pollInterval;
    this.so = so;
  }

//-----------------------------------------------------------------------------

  public void run() {
    if( !DbConnection.isConnectionPool ) return;
    
    running = true;
    while( running ) {
      try {
        displayConnectionPoolStatistics();
        updateConnectionPoolMonitorTable();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      try { sleep(pollInterval * 1000); } catch (Exception e) {;}
    } // while()
    return;
  } // run()

//-----------------------------------------------------------------------------

  public void displayConnectionPoolStatistics() {
    
    try {
      UniversalConnectionPool ucp = DbConnection.mgr.getConnectionPool("INFOAPP");
      UniversalConnectionPoolStatistics upcs = ucp.getStatistics();
      Log.showText("*** STATISTICS - Borrowed Connections: " + upcs.getBorrowedConnectionsCount() + ", Remaining Pool Capacity: " + upcs.getRemainingPoolCapacityCount());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    return;
  } // displayConnectionPoolStatistics()

//-----------------------------------------------------------------------------

  private void updateConnectionPoolMonitorTable() {
    PreparedStatement pstmt = null;
    String sql = null;
  
    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      UniversalConnectionPool ucp = DbConnection.mgr.getConnectionPool("INFOAPP");
      UniversalConnectionPoolStatistics upcs = ucp.getStatistics();

      sql = "UPDATE MON_CONNECTION_POOL SET TIME_STAMP=SYSDATE,ABANDONED_CONN_COUNT=?,AVAILABLE_CONN_COUNT=?,AVG_BORROWED_CONN_COUNT=?,AVG_CONN_WAIT_TIME=?," +
            "BORROWED_CONN_COUNT=?,CONN_CLOSED_COUNT=?,CONN_CREATED_COUNT=?,CUMUL_CONN_BORROWED_COUNT=?,CUMUL_CONN_RETURNED_COUNT=?,CUMUL_CONN_USE_TIME=?,CUMUL_CONN_WAIT_TIME=?," +
            "CUMUL_FAILED_CONN_WAIT_COUNT=?,CUMUL_FAILED_CONN_WAIT_TIME=?,CUMUL_SUCCESS_CONN_WAIT_COUNT=?,CUMUL_SUCCESS_CONN_WAIT_TIME=?,LABELED_CONN_COUNT=?,PEAK_CONN_WAIT_TIME=?,PEAK_CONN_COUNT=?,PENDING_REQUESTS_COUNT=?,REMAINING_POOL_CAPACITY_COUNT=?,TOTAL_CONN_COUNT=? WHERE MODULE_NAME=? AND MODULE_INSTANCE=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setInt(colno++, upcs.getAbandonedConnectionsCount());
      pstmt.setInt(colno++, upcs.getAvailableConnectionsCount());
      pstmt.setInt(colno++, upcs.getAverageBorrowedConnectionsCount());
      pstmt.setLong(colno++, upcs.getAverageConnectionWaitTime());
      pstmt.setInt(colno++, upcs.getBorrowedConnectionsCount());
      pstmt.setInt(colno++, upcs.getConnectionsClosedCount());
      pstmt.setInt(colno++, upcs.getConnectionsCreatedCount());
      pstmt.setLong(colno++, upcs.getCumulativeConnectionBorrowedCount());
      pstmt.setLong(colno++, upcs.getCumulativeConnectionReturnedCount());
      pstmt.setLong(colno++, upcs.getCumulativeConnectionUseTime());
      pstmt.setLong(colno++, upcs.getCumulativeConnectionWaitTime());
      pstmt.setLong(colno++, upcs.getCumulativeFailedConnectionWaitCount());
      pstmt.setLong(colno++, upcs.getCumulativeFailedConnectionWaitTime());
      pstmt.setLong(colno++, upcs.getCumulativeSuccessfulConnectionWaitCount());
      pstmt.setLong(colno++, upcs.getCumulativeSuccessfulConnectionWaitTime());
      pstmt.setInt(colno++, upcs.getLabeledConnectionsCount());
      pstmt.setLong(colno++, upcs.getPeakConnectionWaitTime());
      pstmt.setInt(colno++, upcs.getPeakConnectionsCount());
      pstmt.setInt(colno++, upcs.getPendingRequestsCount());
      pstmt.setInt(colno++, upcs.getRemainingPoolCapacityCount());
      pstmt.setInt(colno++, upcs.getTotalConnectionsCount());
      pstmt.setString(colno++, so.moduleName);
      pstmt.setInt(colno++, so.moduleInstance);
      int count = pstmt.executeUpdate();
      if( count <= 0 ) {
        pstmt.close();
        pstmt = null;

        sql  = "INSERT INTO MON_CONNECTION_POOL (MODULE_NAME,MODULE_INSTANCE,TIME_STAMP,ABANDONED_CONN_COUNT,AVAILABLE_CONN_COUNT,AVG_BORROWED_CONN_COUNT,AVG_CONN_WAIT_TIME,BORROWED_CONN_COUNT,CONN_CLOSED_COUNT,CONN_CREATED_COUNT,CUMUL_CONN_BORROWED_COUNT,CUMUL_CONN_RETURNED_COUNT,CUMUL_CONN_USE_TIME,CUMUL_CONN_WAIT_TIME,CUMUL_FAILED_CONN_WAIT_COUNT,CUMUL_FAILED_CONN_WAIT_TIME,CUMUL_SUCCESS_CONN_WAIT_COUNT,CUMUL_SUCCESS_CONN_WAIT_TIME,LABELED_CONN_COUNT,PEAK_CONN_WAIT_TIME,PEAK_CONN_COUNT,PENDING_REQUESTS_COUNT,REMAINING_POOL_CAPACITY_COUNT,TOTAL_CONN_COUNT)";
        sql += " VALUES (?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        colno = 1;
        pstmt.setString(colno++, so.moduleName);
        pstmt.setInt(colno++, so.moduleInstance);
        pstmt.setInt(colno++, upcs.getAbandonedConnectionsCount());
        pstmt.setInt(colno++, upcs.getAvailableConnectionsCount());
        pstmt.setInt(colno++, upcs.getAverageBorrowedConnectionsCount());
        pstmt.setLong(colno++, upcs.getAverageConnectionWaitTime());
        pstmt.setInt(colno++, upcs.getBorrowedConnectionsCount());
        pstmt.setInt(colno++, upcs.getConnectionsClosedCount());
        pstmt.setInt(colno++, upcs.getConnectionsCreatedCount());
        pstmt.setLong(colno++, upcs.getCumulativeConnectionBorrowedCount());
        pstmt.setLong(colno++, upcs.getCumulativeConnectionReturnedCount());
        pstmt.setLong(colno++, upcs.getCumulativeConnectionUseTime());
        pstmt.setLong(colno++, upcs.getCumulativeConnectionWaitTime());
        pstmt.setLong(colno++, upcs.getCumulativeFailedConnectionWaitCount());
        pstmt.setLong(colno++, upcs.getCumulativeFailedConnectionWaitTime());
        pstmt.setLong(colno++, upcs.getCumulativeSuccessfulConnectionWaitCount());
        pstmt.setLong(colno++, upcs.getCumulativeSuccessfulConnectionWaitTime());
        pstmt.setInt(colno++, upcs.getLabeledConnectionsCount());
        pstmt.setLong(colno++, upcs.getPeakConnectionWaitTime());
        pstmt.setInt(colno++, upcs.getPeakConnectionsCount());
        pstmt.setInt(colno++, upcs.getPendingRequestsCount());
        pstmt.setInt(colno++, upcs.getRemainingPoolCapacityCount());
        pstmt.setInt(colno++, upcs.getTotalConnectionsCount());
        pstmt.executeUpdate();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "PeriodicCheckerThread.updateConnectionPoolMonitorTable");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return;
  } // updateConnectionPoolMonitorTable()

}
