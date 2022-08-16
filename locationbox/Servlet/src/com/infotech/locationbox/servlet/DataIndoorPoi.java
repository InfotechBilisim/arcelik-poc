package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataIndoorPoi {
  protected long id = 0;
  protected String name = null;
  protected String venueName = null;
  protected String brandName = null;
  protected long floorLevel = -100;
  protected String floorName = null;
  protected int subtype = 0;
  protected int brandId = 0;
  protected double centerLatitude = 0.00;
  protected double centerLongitude = 0.00;
  protected double entranceLatitude = 0.00;
  protected double entranceLongitude = 0.00;
  protected double distance = 0.00;

  public DataIndoorPoi() {
  }

  public DataIndoorPoi(long id, String name, String venueName, int floorLevel, String floorName, int brandId, String brandName, int subtype, String phone, String url, String logoName, double centerLatitude, double centerLongitude, double entranceLatitude, double entranceLongitude, double distance) {
    this.id = id;
    this.name = name;
    this.venueName = venueName;
    this.floorLevel = floorLevel;
    this.floorName = floorName;
    this.brandId = brandId;
    this.brandName = brandName;
    this.subtype = subtype;
    this.centerLatitude = centerLatitude;
    this.centerLongitude = centerLongitude;
    this.entranceLatitude = entranceLatitude;
    this.entranceLongitude = entranceLongitude;
    this.distance = distance;
  }

  //-----------------------------------------------------------------------------

  public static DataIndoorPoi getInstance(ResultSet rset) throws Exception {
    DataIndoorPoi dp = new DataIndoorPoi();
    dp.id = rset.getLong("AREA_ID");
    dp.name = rset.getString("AREA_NAME");
    dp.venueName = rset.getString("VENUE_NAME");
    dp.floorLevel = rset.getInt("FLOOR");
    dp.floorName = rset.getString("FLOOR_NAME");
    dp.brandId = rset.getInt("BRAND_ID");
    dp.brandName = rset.getString("BRAND_NAME");
    dp.subtype = rset.getInt("SUB_TYPE");
    dp.centerLongitude = rset.getDouble("XCOOR_CENTER");
    dp.centerLatitude = rset.getDouble("YCOOR_CENTER");
    dp.entranceLongitude = rset.getDouble("XCOOR_ENTRANCE");
    dp.entranceLatitude = rset.getDouble("YCOOR_ENTRANCE");
    try {
      dp.distance = (int) (rset.getDouble("DISTANCE") + 0.5);
    } 
    catch (Exception e) {
      dp.distance = 0;
    }
    return dp;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public String toJson() {
    String s = "";
    s += "{\n";
    s += "  \"id\": " + id + ",\n";
    s += "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n";
    s += "  \"venuename\": \"" + (venueName == null ? "" : Utils.convStr2Json(venueName)) + "\",\n";
    s += "  \"floorlevel\": " + floorLevel + ",\n";
    s += "  \"floorname\": \"" + (floorName == null ? "" : Utils.convStr2Json(floorName)) + "\",\n";
    s += "  \"brandid\": " + brandId + ",\n";
    s += "  \"brandname\": \"" + (brandName == null ? "" : Utils.convStr2Json(brandName)) + "\",\n";
    s += "  \"subtype\": " + subtype + "\"\n";
    if (centerLatitude != 0.00 && centerLongitude != 0.00) {
      s += ",\n";
      s += "  \"centerlatitude\": " + Utils.makeCoorFormat(centerLatitude) + ",\n";
      s += "  \"centerlongitude\": " + Utils.makeCoorFormat(centerLongitude) + "\n";
    }
    if (entranceLatitude != 0.00 && entranceLongitude != 0.00) {
      s += ",\n";
      s += "  \"latitude\": " + Utils.makeCoorFormat(entranceLatitude) + ",\n";
      s += "  \"longitude\": " + Utils.makeCoorFormat(entranceLongitude) + ",\n";
      s += "  \"distance\": " + (int) (distance + 0.5);
    }

    s += "\n";
    s += "}";
    return s;
  } // toJson()

  //-----------------------------------------------------------------------------

  public String toXml() {
    String s = "";
    s += "    <indoorpoi>\n";
    s += "      <id>" + id + "</id>\n";
    s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
    s += "      <venuename>" + (venueName == null ? "" : Utils.convStr2Xml(venueName)) + "</venuename>\n";
    s += "      <floorlevel>" + floorLevel + "</floorlevel>\n";
    s += "      <floorname>" + (floorName == null ? "" : Utils.convStr2Xml(floorName)) + "</floorname>\n";
    s += "      <brandid>" + brandId + "</brandid>\n";
    s += "      <brandname>" + (brandName == null ? "" : Utils.convStr2Xml(brandName)) + "</brandname>\n";
    s += "      <subtype>" + subtype + "</subtype>\n";
    if (centerLatitude != 0.00 && centerLongitude != 0.00) {
      s += "      <centerlatitude>" + Utils.makeCoorFormat(centerLatitude) + "</centerlatitude>\n";
      s += "      <centerlongitude>" + Utils.makeCoorFormat(centerLongitude) + "</centerlongitude>\n";
    }
    if (entranceLatitude != 0.00 && entranceLongitude != 0.00) {
      s += "      <entrancelatitude>" + Utils.makeCoorFormat(centerLatitude) + "</entrancelatitude>\n";
      s += "      <entrancelongitude>" + Utils.makeCoorFormat(centerLongitude) + "</entrancelongitude>\n";
      s += "      <distance>" + (int) (distance + 0.5) + "</distance>\n";
    }
    s += "    </indoorpoi>\n";
    return s;
  } // toXml()

  //-----------------------------------------------------------------------------

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setSubtype(int subtype) {
    this.subtype = subtype;
  }

  public int getSubtype() {
    return subtype;
  }

  public void setCenterLatitude(double centerLatitude) {
    this.centerLatitude = centerLatitude;
  }

  public double getCenterLatitude() {
    return centerLatitude;
  }

  public void setCenterLongitude(double centerLongitude) {
    this.centerLongitude = centerLongitude;
  }

  public double getCenterLongitude() {
    return centerLongitude;
  }

  public void setEntranceLatitude(double entranceLatitude) {
    this.entranceLatitude = entranceLatitude;
  }

  public double getEntranceLatitude() {
    return entranceLatitude;
  }

  public void setEntranceLongitude(double entranceLongitude) {
    this.entranceLongitude = entranceLongitude;
  }

  public double getEntranceLongitude() {
    return entranceLongitude;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public double getDistance() {
    return distance;
  }


}
