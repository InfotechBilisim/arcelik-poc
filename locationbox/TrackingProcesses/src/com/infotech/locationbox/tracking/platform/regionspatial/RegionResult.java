package com.infotech.locationbox.tracking.platform.regionspatial;

public class RegionResult {
  private int regionType = 1;
  private long regionId = 0;
  private String regionName = null;
  private int status = 0;

  public RegionResult() {
  }

  public RegionResult(long regionId, String regionName, int status) {
    this.regionId = regionId;
    this.regionName = regionName;
    this.status = status;
  }

//-----------------------------------------------------------------------------

  public String toString() {
    return "Region Result : " + (status == RegionSpatial.STATUS_INSIDE ? "INSIDE" : "OUTSIDE") + ", REGION: " + regionName;
  }

//-----------------------------------------------------------------------------

  public void setRegionType(int regionType) {
    this.regionType = regionType;
  }

  public int getRegionType() {
    return regionType;
  }

  public void setRegionId(long regionId) {
    this.regionId = regionId;
  }

  public long getRegionId() {
    return regionId;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
  
}
