package com.infotech.locationbox.servlet;

import java.sql.ResultSet;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

public class DataIl {
  private long id = 0;
  private String name = null;
  private double latitude = 0.00;
  private double longitude = 0.00;
  private Extent extent = null;
  protected JGeometry[] objs = null;
  protected boolean encode = false;
  private long count = 0;

  private String groupValue = null;
  private long adresKodu = 0;

  public DataIl() {
  }

  public DataIl(long id, String name, double latitude, double longitude, Extent extent, long count) {
    this.id = id;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.extent = extent;
    this.count = count;
  }

  //-----------------------------------------------------------------------------

  public DataIl(long id, String name, double latitude, double longitude, Extent extent, long count, String groupValue) {
    this.id = id;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.extent = extent;
    this.count = count;
    this.groupValue = groupValue;
  }

  //-----------------------------------------------------------------------------

  public DataIl(long id, String name, double latitude, double longitude, Extent extent) {
    this.id = id;
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.extent = extent;
  }

  //-----------------------------------------------------------------------------  
  public static DataIl getInstance(ResultSet rset) {
    DataIl di = new DataIl();
    try {
      di.id = rset.getLong(1);
      di.name = rset.getString(2);
      return di;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public static DataIl getInstance(ResultSet rset, boolean withCoors, boolean encode) {
    DataIl di = new DataIl();
    try {
      di.id = rset.getLong(1);
      di.name = rset.getString(2);
      di.longitude = rset.getDouble(3);
      di.latitude = rset.getDouble(4);
      di.adresKodu = rset.getLong(5);
      STRUCT obj = DbConn.convToSTRUCT(rset.getObject(6));
      if (obj != null)
        di.extent = Utils.getExtent(obj);

      if (withCoors) {
        STRUCT objGeo = DbConn.convToSTRUCT(rset.getObject(7));
        JGeometry geo = JGeometry.load(objGeo);
        di.objs = geo.getElements();
        di.encode = encode;
      }
      return di;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public String toJson() {
    String result = "{ ";
    if (count > 0)
      result += "\"count\": " + count + ", ";
    if (groupValue != null && groupValue.length() > 0)
      result += "\"groupvalue\": \"" + groupValue + "\", ";
    result += "\"id\": " + id;
    result += ", \"adreskodu\": " + (adresKodu == 0 ? "0" : adresKodu) + "";
    result += ", \"name\": \"" + Utils.convStr2Json(name) + "\"";
    result +=
      ", \"latitude\": " + Utils.makeCoorFormat(latitude) + ", \"longitude\": " + Utils.makeCoorFormat(longitude) + ", \"extent\": { \"minlatitude\": " +
      Utils.makeCoorFormat(extent.getMinLatitude()) + ", \"minlongitude\": " + Utils.makeCoorFormat(extent.getMinLongitude()) + ", \"maxlatitude\": " +
      Utils.makeCoorFormat(extent.getMaxLatitude()) + ", \"maxlongitude\": " + Utils.makeCoorFormat(extent.getMaxLongitude()) + " } }";
    return result;
  } // toJason()

  //-----------------------------------------------------------------------------

  public String toXml() {

    String result = "<il>";
    if (count > 0)
      result += "<count>" + count + "</count>";
    if (groupValue != null && groupValue.length() > 0)
      result += "<groupvalue>" + groupValue + "</groupvalue>";
    result += "<id>" + id + "</id><name>" + Utils.convStr2Xml(name) + "</name>";
    result += "<adreskodu>" + adresKodu + "</adreskodu>";
    result +=
      "<latitude>" + Utils.makeCoorFormat(latitude) + "</latitude><longitude>" + Utils.makeCoorFormat(longitude) + "</longitude><extent><minlatitude>" +
      Utils.makeCoorFormat(extent.getMinLatitude()) + "</minlatitude><minlongitude>" + Utils.makeCoorFormat(extent.getMinLongitude()) + "</minlongitude><maxlatitude>" +
      Utils.makeCoorFormat(extent.getMaxLatitude()) + "</maxlatitude><maxlongitude>" + Utils.makeCoorFormat(extent.getMaxLongitude()) + "</maxlongitude></extent>";
    result += "</il>\n";
    return result;
  } // toXml()

  //-----------------------------------------------------------------------------

  public String toJson(String indent) {
    String s = "";
    //s += indent + "{\n";
    s += "\"id\": " + id + ",\n";
    s += indent + "  \"adreskodu\": " + (adresKodu == 0 ? "0" : adresKodu) + "";
    s += ",\n";
    if (count > 0) {
      s += indent + "  \"count\": \"" + (count == 0 ? "" : count) + "\"";
      s += ",\n";
    }
    s += indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Xml(name)) + "\"";
    if (latitude != 0.00 && longitude != 0.00) {
      s += ",\n";
      s += indent + "  \"latitude\": " + Utils.makeCoorFormat(latitude) + ",\n";
      s += indent + "  \"longitude\": " + Utils.makeCoorFormat(longitude);
    }
    if (extent != null) {
      s += ",\n";
      s +=
        indent + "  \"extent\": { \"minlatitude\": " + Utils.makeCoorFormat(extent.getMinLatitude()) + ", \"minlongitude\": " + Utils.makeCoorFormat(extent.getMinLongitude()) +
        ", \"maxlatitude\": " + Utils.makeCoorFormat(extent.getMaxLatitude()) + ", \"maxlongitude\": " + Utils.makeCoorFormat(extent.getMaxLongitude()) + " } ";
    }
    if (objs != null) {
      s += ",\n";
      s += indent + "  \"geometry\": [\n";
      for (int k = 0; k < objs.length; k++) {
        double[] oarray = objs[k].getOrdinatesArray();
        if (encode) {
          String pline = PolylineUtils.encodePolyline(oarray);
          s += indent + "    \"" + pline + "\"";
        } else {
          if (k > 0)
            s += ",\n";
          StringBuilder sbuf = new StringBuilder(oarray.length * 12);
          for (int i = 0; i < oarray.length; i++) {
            if (i > 0)
              sbuf.append(',');
            sbuf.append(oarray[i]);
          } // for(i)
          s += indent + "    \"" + sbuf.toString() + "\"";
        }
      } // for(k)
      s += "\n";
      s += indent + "  ]";
    } // if(objs)
    s += "\n";
    //s += indent + "}";
    return s;
  } // toJson()

  //-----------------------------------------------------------------------------

  public String toXml(String indent) {
    String s = "";
    s += "    <il>\n";
    s += "      <id>" + id + "</id>\n";
    s += "      <adreskodu>" + adresKodu + "</adreskodu>\n";
    s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
    if (count > 0) {
      s += "      <count>" + count + "</count>\n";
    }
    if (latitude != 0.00 && longitude != 0.00) {
      s += "      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
      s += "      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
    }
    if (extent != null) {
      s += "      <extent>";
      s += "          <minlatitude>" + Utils.makeCoorFormat(extent.getMinLatitude()) + "</minlatitude>\n";
      s += "          <minlongitude>" + Utils.makeCoorFormat(extent.getMinLongitude()) + "</minlongitude>\n";
      s += "          <maxlatitude>" + Utils.makeCoorFormat(extent.getMaxLatitude()) + "</maxlatitude>\n";
      s += "          <maxlongitude>" + Utils.makeCoorFormat(extent.getMaxLongitude()) + "</maxlongitude>\n";
      s += "      </extent>";
    }
    if (objs != null) {
      s += "      <geometry>\n";
      for (int k = 0; k < objs.length; k++) {
        double[] oarray = objs[k].getOrdinatesArray();
        if (encode) {
          String pline = PolylineUtils.encodePolyline(oarray);
          s += "        <ordinates>" + pline + "</ordinates>\n";
        } else {
          StringBuilder sbuf = new StringBuilder(oarray.length * 12);
          for (int i = 0; i < oarray.length; i++) {
            if (i > 0)
              sbuf.append(',');
            sbuf.append(oarray[i]);
          } // for(i)
          s += "        <ordinates>" + sbuf.toString() + "</ordinates>\n";
        }
      } // for(k)
      s += "      </geometry>\n";
    } // if(objs)
    s += "    </il>\n";
    return s;
  } // toXml()

  //-----------------------------------------------------------------------------

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public long getCount() {
    return count;
  }

  public void setGroupValue(String groupValue) {
    this.groupValue = groupValue;
  }

  public String getGroupValue() {
    return groupValue;
  }

  public void setAdresKodu(long adresKodu) {
    this.adresKodu = adresKodu;
  }

  public long getAdresKodu() {
    return adresKodu;
  }

}
