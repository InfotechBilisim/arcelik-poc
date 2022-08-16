package com.infotech.locationbox.tracking.locationprocess;

import com.infotech.locationbox.tracking.platform.base.*;

public class AppMain extends SystemObject {
  int inboxSleepDuration = 10;
  int inboxProcessDayCount = 10;
  int periodicCheckerSleepDuration = 60;

  Thread inboxProcess = null;

  public AppMain() {
  }

  public static void main(String[] args) {
    AppMain app = new AppMain();
    app.startUp(args, "LocationProcessor", "1.31");
    return;
  } // main()

  public void runThreads() {
    Log.showText("Starting threads...");
    inboxProcess = new Thread(new LocationProcessThread(inboxSleepDuration, inboxProcessDayCount, this));
    inboxProcess.start();
//    periodicChecker = new Thread(new PeriodicCheckerThread(periodicCheckerSleepDuration, this));
//    periodicChecker.start();
    Log.showText("All threads started.");
    return;
  } // runThreads()

  public boolean readModuleParams() {
    inboxSleepDuration   = this.getSysModuleParamInt("INBOX_SLEEP_DURATION"); 
    inboxProcessDayCount = this.getSysModuleParamInt("INBOX_PROCESS_DAY_COUNT"); 
    return  true;
  } // readModuleParams()

}
