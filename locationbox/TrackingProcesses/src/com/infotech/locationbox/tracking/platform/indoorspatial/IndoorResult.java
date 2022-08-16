package com.infotech.locationbox.tracking.platform.indoorspatial;

public class IndoorResult {
  private int indoorType = 0;
  private String indoorTypeName = null;
  private long indoorId = 0;
  private String indoorName = null;
  private int status = 0;

  public IndoorResult() {
  }

  public IndoorResult(int indoorType, String indoorTypeName, long indoorId, String indoorName, int status) {
    this.indoorType = indoorType;
    this.indoorTypeName = indoorTypeName;
    this.indoorId = indoorId;
    this.indoorName = indoorName;
    this.status = status;
  }

//-----------------------------------------------------------------------------

  public String toString() {
    return "Indoor Result : " + (status == IndoorSpatial.STATUS_INSIDE ? "INSIDE" : "OUTSIDE") + ", INDOOR TYPE: " + indoorTypeName + ", INDOOR NAME: " + indoorName;
  }

//-----------------------------------------------------------------------------

  public void setIndoorType(int indoorType) {
    this.indoorType = indoorType;
  }

  public int getIndoorType() {
    return indoorType;
  }

  public void setIndoorTypeName(String indoorTypeName) {
    this.indoorTypeName = indoorTypeName;
  }

  public String getIndoorTypeName() {
    return indoorTypeName;
  }

  public void setIndoorId(long indoorId) {
    this.indoorId = indoorId;
  }

  public long getIndoorId() {
    return indoorId;
  }

  public void setIndoorName(String indoorName) {
    this.indoorName = indoorName;
  }

  public String getIndoorName() {
    return indoorName;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
  
}
