package com.infotech.locationbox.tracking.platform.gtw.medrics;

import com.infotech.locationbox.tracking.platform.base.*;

public class AppMain extends SystemObject {
  private String gatewayName = "MEDRICS";

  private int outboxSleepDuration = 10;
  private int outboxProcessDayCount = 10;
  private Thread outboxProcess = null;

  private int periodicCheckerSleepDuration = 60;
  private Thread periodicChecker = null;

  public AppMain() {
  }

//------------------------------------------------------------------------------

  public static void main(String[] args) {
    AppMain appMain = new AppMain();
    appMain.startUp(args, "MedricsGtw", "1.11");
    return;
  }

//------------------------------------------------------------------------------

  public void runThreads() {
    Log.showText("Gateway Name: " + gatewayName);
    Log.showText("Starting threads...");
    try {
      outboxProcess = new Thread(new OutboxProcessThread(outboxSleepDuration, outboxProcessDayCount, gatewayName, this));
      outboxProcess.start();
      periodicChecker = new Thread(new PeriodicCheckerThread(periodicCheckerSleepDuration, this));
      periodicChecker.start();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      Log.showText("Exception occured in runThreads() function !");
    }
    Log.showText("All threads started.");
    return;
  } // runThreads()

//------------------------------------------------------------------------------

  public boolean readModuleParams() {
    outboxSleepDuration = this.getSysModuleParamInt("OUTBOX_SLEEP_DURATION");
    outboxProcessDayCount = this.getSysModuleParamInt("OUTBOX_PROCESS_DAY_COUNT");

    MedricsService.endPoint = this.getSysModuleParamString("MEDRICS_SERVICE_ENDPOINT");

    return  true;
  } // readModuleParams()

//------------------------------------------------------------------------------

  public String getGatewayName() {
    return gatewayName;
  }

}
