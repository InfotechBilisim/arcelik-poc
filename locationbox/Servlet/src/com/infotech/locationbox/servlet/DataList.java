package com.infotech.locationbox.servlet;


public class DataList {
  protected String id = null;
  protected String name = null;
  protected int type = -1;
  protected double latitude = 0.00;
  protected double longitude = 0.00;
  protected double angle = 0.00;
  protected Extent extent = null;
  protected int distance = -1;
  protected String address = null;
  protected String telephone = null;

  public DataList() {
  }

  public DataList(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public DataList(String id, String name, int type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public DataList(String id, String name, int type, double latitude, double longitude, double angle) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.latitude = latitude;
    this.longitude = longitude;
    this.angle = angle;
  }

  public DataList(String id, String name, int type, double latitude, double longitude, double angle, int distance, String address, String telephone) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.latitude = latitude;
    this.longitude = longitude;
    this.angle = angle;
    this.distance = distance;
    this.address = address;
    this.telephone = telephone;
  }

  public DataList(String id, String name, int type, Extent extent) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.extent = extent;
  }
  //-----------------------------------------------------------------------------
  public String toJson(String indent) {
    String s = "";
    s += indent + "{\n";
    s += indent + "  \"id\": \"" + id + "\",\n";
    s += indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\"";
    if (type >= 0) {
      s += ",\n";
      s += indent + "  \"type\": " + type;
    }
    s += ",\n";
    s += indent + "  \"latitude\": " + latitude + ",\n";
    s += indent + "  \"longitude\": " + longitude + ",\n";
    s += indent + "  \"angle\": " + angle;
    if (distance >= 0.00) {
      s += ",\n";
      s += indent + "  \"distance\": " + distance;
    }

    s += ",\n" + indent + "  \"address\": \"" + address + "\"";
    s += ",\n" + indent + "  \"telephone\": \"" + telephone + "\"";

    if (extent != null) {
      s += ",\n";
      s += indent + "  \"extent\": {\n";
      s += indent + "    \"minlatitude\": " + Utils.makeCoorFormat(extent.getMinLatitude()) + ",\n";
      s += indent + "    \"minlongitude\": " + Utils.makeCoorFormat(extent.getMinLongitude()) + ",\n";
      s += indent + "    \"maxlatitude\": " + Utils.makeCoorFormat(extent.getMaxLatitude()) + ",\n";
      s += indent + "    \"maxlongitude\": " + Utils.makeCoorFormat(extent.getMaxLongitude()) + "\n";
      s += indent + "  }";
    }

    s += "\n";
    s += indent + "}";
    return s;
  } // toJson()
  //-----------------------------------------------------------------------------
  public String toXml() {
    String s = "";
    s += "    <item>\n";
    s += "      <id>" + id + "</id>\n";
    s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
    if (type >= 0) {
      s += "      <type>" + type + "</type>\n";
    }
    s += "      <latitude>" + latitude + "</latitude>\n";
    s += "      <longitude>" + longitude + "</longitude>\n";
    s += "      <angle>" + angle + "</angle>\n";
    if (distance >= 0.00) {
      s += "      <distance>" + distance + "</distance>\n";
    }
    s += "      <address>" + address + "</address>\n";
    s += "      <telephone>" + telephone + "</telephone>\n";
    if (extent != null) {
      s += "      <extent>\n";
      s += "        <minlatitude>" + Utils.makeCoorFormat(extent.getMinLatitude()) + "</minlatitude>\n";
      s += "        <minlongitude>" + Utils.makeCoorFormat(extent.getMinLongitude()) + "</minlongitude>\n";
      s += "        <maxlatitude>" + Utils.makeCoorFormat(extent.getMaxLatitude()) + "</maxlatitude>\n";
      s += "        <maxlongitude>" + Utils.makeCoorFormat(extent.getMaxLongitude()) + "</maxlongitude>\n";
      s += "      </extent>\n";
    }
    s += "    </item>\n";
    return s;
  } // toXml()
  //-----------------------------------------------------------------------------
  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setExtent(Extent extent) {
    this.extent = extent;
  }

  public Extent getExtent() {
    return extent;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }

  public double getAngle() {
    return angle;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  public int getDistance() {
    return distance;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

}
