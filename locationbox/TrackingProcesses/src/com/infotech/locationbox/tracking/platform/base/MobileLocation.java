package com.infotech.locationbox.tracking.platform.base;

import java.sql.ResultSet;

public class MobileLocation {
  private long rowno = 0;
  private String timeStamp = null;
  private String key = null;
  private long mobileId = 0;
  private double xcoor = 0.00;
  private double ycoor = 0.00;
  private int floor = 0;
  
  public MobileLocation() {
  }
  
//-----------------------------------------------------------------------------
  
  public static MobileLocation getInstance(ResultSet rset) {
    MobileLocation mloc = new MobileLocation();
    try {
      mloc.rowno = rset.getLong("ROWNO");
      mloc.timeStamp = rset.getString("TIME_STAMP");
      if( mloc.timeStamp.length() > 19 ) mloc.timeStamp = mloc.timeStamp.substring(0, 19);
      mloc.key = rset.getString("KEY");
      mloc.mobileId = rset.getLong("MOBILE_ID");
      mloc.xcoor = rset.getDouble("XCOOR");
      mloc.ycoor = rset.getDouble("YCOOR");
      mloc.floor = rset.getInt("FLOOR");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
    
    return mloc;
  } // getInstance()

//-----------------------------------------------------------------------------

  public void setRowno(long rowno) {
    this.rowno = rowno;
  }

  public long getRowno() {
    return rowno;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setMobileId(long mobileId) {
    this.mobileId = mobileId;
  }

  public long getMobileId() {
    return mobileId;
  }

  public void setXcoor(double xcoor) {
    this.xcoor = xcoor;
  }

  public double getXcoor() {
    return xcoor;
  }

  public void setYcoor(double ycoor) {
    this.ycoor = ycoor;
  }

  public double getYcoor() {
    return ycoor;
  }

  public void setFloor(int floor) {
    this.floor = floor;
  }

  public int getFloor() {
    return floor;
  }
  
}
