package com.infotech.locationbox.tracking.locationprocess;

import com.infotech.locationbox.tracking.platform.base.*;
import com.infotech.locationbox.tracking.platform.indoorspatial.*;
import com.infotech.locationbox.tracking.platform.regionspatial.*;

import java.sql.*;

public class LocationProcessThread extends ProcessThread {
  private AppMain app = null;

  private RegionSpatial regionSpatial = null;
  private IndoorSpatial indoorSpatial = null;

  public LocationProcessThread() {
  }

  public LocationProcessThread(int  pollInterval, int processDayCount, AppMain app) {
    super("MOBILE_LOCATION", pollInterval, processDayCount, app);
    this.app = app;
    this.regionSpatial = new RegionSpatial();
    this.indoorSpatial = new IndoorSpatial();
  }

//-----------------------------------------------------------------------------

  public void doOperation(ResultSet rset) {
    try {
      long rowNo = rset.getLong("ROWNO");
      
      MobileLocation mloc = MobileLocation.getInstance(rset);
      if( mloc == null ) {
        Log.showError("*** Could not get MOBILE_LOCATION data !");
        this.dbMarkProcessed(tableName, rowNo, -1);
        return;
      }
  
      Mobile mobile = Mobile.getInstance(mloc.getMobileId());
      if( mobile == null ) {
        Log.showError("*** Mobile " + mloc.getMobileId() + " not found for " + mloc.getRowno() + "!");
        this.dbMarkProcessed(tableName, rowNo, -1);
        return;
      }
  
      Log.showText(tableName + " ROWNO: " + rowNo + ", MOBILE ID: " + mobile.getId() + ", NAME: " + mobile.getName());
      
      checkRegions(mobile, mloc);
      checkIndoor(mobile, mloc);
      
      this.dbMarkProcessed(tableName, rowNo);
    }
    catch (Exception ex) {
      Log.showError("Exception: " + ex.getMessage());
      ex.printStackTrace();
    }

    return;        
  } // doOperation()

//-----------------------------------------------------------------------------

  public void checkRegions(Mobile mobile, MobileLocation mloc) {
    try {
      String key = mloc.getKey();
      String timeStamp = mloc.getTimeStamp();
      int floor = mloc.getFloor();
      double xcoor = mloc.getXcoor();
      double ycoor = mloc.getYcoor();
      if( xcoor == 0.00 && ycoor == 0.00 ) return;
      
      RegionResult ress[] = regionSpatial.checkRegionsForMobile(key, mobile.getId(), xcoor, ycoor, floor,  timeStamp);
      if( ress != null ) {
        for( int i = 0; i < ress.length; i++ ) {
          RegionResult res = ress[i];
          Log.showText("REGION EVENT: " + res);

          String eventCode = null;
          String eventDesc = null;
          switch( res.getStatus() ) {
          case RegionSpatial.STATUS_UNKNOWN :
            continue;

          case RegionSpatial.STATUS_INSIDE :
            eventCode = "REGION_ENTER";
            eventDesc = mobile.getName() + ", " + res.getRegionName() + " BOLGESINE GIRDI.";
            break;
          case RegionSpatial.STATUS_OUTSIDE :
            eventCode = "REGION_LEAVE";
            eventDesc = mobile.getName() + ", " + res.getRegionName() + " BOLGESINDEN CIKTI.";
            break;
          } // switch()

          so.dbInsertMobileEvent(mobile, mloc, eventCode, eventDesc, res.getRegionType(), res.getRegionId(), res.getRegionName());
          if( so.isAlarmGenerationRequired(mobile.getId(), eventCode, res.getRegionType(), res.getRegionId(), mloc.getTimeStamp()) ) {
            so.dbInsertMobileAlarm(mobile, mloc, eventCode, eventDesc, res.getRegionType(), res.getRegionId(), res.getRegionName());
          }
        } // for()
      }
    }
    catch (Exception ex) {
      Log.showError("checkRegions --> MOBILE ID: " + mobile.getId() + ", MOBILE_LOCATION ROWNO: " + mloc.getRowno());
      ex.printStackTrace();
    }

    return;
  } // checkRegions()

//-----------------------------------------------------------------------------

  public void checkIndoor(Mobile mobile, MobileLocation mloc) {
    try {
      String timeStamp = mloc.getTimeStamp();
      int floor = mloc.getFloor();
      double xcoor = mloc.getXcoor();
      double ycoor = mloc.getYcoor();
      if( xcoor == 0.00 && ycoor == 0.00 ) return;

      IndoorResult ress[] = indoorSpatial.checkIndoorForMobile(mobile.getId(), xcoor, ycoor, floor,  timeStamp);
      if( ress != null ) {
        for( int i = 0; i < ress.length; i++ ) {
          IndoorResult res = ress[i];
          Log.showText("INDOOR EVENT: " + res);

          String eventCode = null;
          String eventDesc = null;
          switch( res.getStatus() ) {
          case IndoorSpatial.STATUS_UNKNOWN :
            continue;

          case IndoorSpatial.STATUS_INSIDE :
            eventCode = "INDOOR_ENTER";
            eventDesc = mobile.getName() + ", " + res.getIndoorName() + " ALANINA GIRDI.";
            break;
          case IndoorSpatial.STATUS_OUTSIDE :
            eventCode = "INDOOR_LEAVE";
            eventDesc = mobile.getName() + ", " + res.getIndoorName() + " ALANINDAN CIKTI.";
            break;
          } // switch()

          so.dbInsertMobileEvent(mobile, mloc, eventCode, eventDesc, res.getIndoorType(), res.getIndoorId(), res.getIndoorName());
          if( so.isAlarmGenerationRequired(mobile.getId(), eventCode, res.getIndoorType(), res.getIndoorId(), mloc.getTimeStamp()) ) {
            so.dbInsertMobileAlarm(mobile, mloc, eventCode, eventDesc, res.getIndoorType(), res.getIndoorId(), res.getIndoorName());
          }
        } // for()
      }
    }
    catch (Exception ex) {
      Log.showError("checkIndoor --> MOBILE ID: " + mobile.getId() + ", MOBILE_LOCATION ROWNO: " + mloc.getRowno());
      ex.printStackTrace();
    }

    return;
  } // checkIndoor()

}
