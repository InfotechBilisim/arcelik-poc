package com.infotech.locationbox.tracking.platform.gtw.medrics;

import com.infotech.locationbox.tracking.platform.base.*;

import java.sql.*;

public class OutboxProcessThread extends ProcessThread {
  private AppMain app = null;
  private String gatewayName = "MEDRICS";

  public OutboxProcessThread() {
  }

  public OutboxProcessThread(int pollInterval, int processDayCount, String gatewayName, AppMain app) {
    super("OUTBOX", pollInterval, processDayCount, app);
    this.app = app;
    this.gatewayName = gatewayName;
    this.whereAddition = "GATEWAY = '" + gatewayName + "'";
  }

  public void doOperation(ResultSet rset) {
    try {
      long rowNo = rset.getLong("ROWNO");
      int status = rset.getInt("PROCESS_STATUS");

      String destination = rset.getString("DESTINATION");
      Log.showText("DESTINATION: " + destination);

      String data = rset.getString("DATA");
      MedricsService.sendAlarm(rowNo, destination, data);
      this.dbMarkProcessed("OUTBOX", rowNo);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      Log.logException(1, "Get Column Data", "OutboxPollerThread.doOperation");
    }
    return;
  } // doOperation()

}
