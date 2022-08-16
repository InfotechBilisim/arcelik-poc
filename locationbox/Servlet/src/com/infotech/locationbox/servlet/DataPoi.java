package com.infotech.locationbox.servlet;

import java.sql.ResultSet;
import java.util.ArrayList;


public class DataPoi {
  protected long id = 0;
  protected String name = null;
  protected String address = null;
  protected long ilId = 0;
  protected String ilAdi = null;
  protected long ilceId = 0;
  protected String ilceAdi = null;
  protected long mahalleId = 0;
  protected String mahalleAdi = null;
  protected String yolAdi = null;
  protected String kapiNo = null;
  protected String phone = null;
  protected String postalCode = null;
  protected int subtype = 0;
  protected double latitude = 0.00;
  protected double longitude = 0.00;
  protected double distance = 0.00;
  protected String markerName = null;
  protected String brandName = null;


  protected DataNamedValue[] namedValues = null;

  protected DataPoiAttribute[] poiAttributes = null;


  public DataPoi() {
  }

  public DataPoi(long id, String name, String address, String phone, int subtype, double latitude, double longitude, double distance) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.phone = phone;
    this.subtype = subtype;
    this.latitude = latitude;
    this.longitude = longitude;
    this.distance = distance;
  }

  //-----------------------------------------------------------------------------
  public static DataPoi getInstance(ResultSet rset, int brandGroup) throws Exception {
      DataPoi dp = new DataPoi();
      dp.setName(rset.getString("STANDARD_NAME"));
      dp.setBrandName(rset.getString("BRAND_NAME"));
      try {
        dp.distance = (int) (rset.getDouble("DISTANCE") + 0.5);
      } catch (Exception e) {
        ;
      }
    return dp;
  }
  public static DataPoi getInstance(ResultSet rset) throws Exception {
    DataPoi dp = new DataPoi();
    dp.id = rset.getLong("ID");
    dp.name = rset.getString("STANDARD_NAME");
    dp.ilId = rset.getLong("IL_ID");
    dp.ilAdi = rset.getString("IL_ADI");
    dp.ilceId = rset.getLong("ILCE_ID");
    dp.ilceAdi = rset.getString("ILCE_ADI");
    dp.mahalleId = rset.getLong("MAHALLE_ID");
    dp.mahalleAdi = rset.getString("MAHALLE_ADI");
    dp.yolAdi = rset.getString("STREET_NAME");
    String streetType = rset.getString("STREET_TYPE");
    dp.kapiNo = rset.getString("HSN");
    dp.postalCode = rset.getString("POSTAL_CODE");    
    dp.address = dp.mahalleAdi + " MAH.";
    if (dp.yolAdi != null) {
      dp.address += " " + dp.yolAdi;
      if (streetType != null) { 
        dp.yolAdi += " " + streetType;
        dp.address += " " + streetType;
      }
    }
    dp.address += (dp.kapiNo == null || dp.kapiNo.length() <= 0 ? "" : " NO: " + dp.kapiNo);
    dp.address += (dp.postalCode == null || dp.postalCode.length() < 3 ? "" : " " + dp.postalCode);
    dp.address += " " + dp.ilceAdi;
    dp.address += " " + dp.ilAdi;
    int countryCode = rset.getInt("COUNTRY_CODE");
    int areaCode = rset.getInt("AREA_CODE");
    int telephone = rset.getInt("TELEPHONE");
    dp.subtype = rset.getInt("SUB_TYPE");
    if (countryCode > 0 && areaCode > 0 || telephone > 0)
      dp.phone = "+" + countryCode + " " + areaCode + " " + telephone;
    else
      dp.phone = "";
    dp.longitude = rset.getDouble("XCOOR");
    dp.latitude = rset.getDouble("YCOOR");
    try {
      dp.distance = (int) (rset.getDouble("DISTANCE") + 0.5);
    } catch (Exception e) {
      ;
    }
    ArrayList array = new ArrayList();
    try {
      array.add(new DataNamedValue("OCTANE95", rset.getString("OCTANE95")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("OCTANE95PLUS", rset.getString("OCTANE95PLUS")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("OCTANE97", rset.getString("OCTANE97")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("EURODIESEL10", rset.getString("EURODIESEL10")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("PRODIESEL10", rset.getString("PRODIESEL10")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("CAPACITY", rset.getString("CAPACITY")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR1", rset.getString("HOUR1")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR2", rset.getString("HOUR2")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR3", rset.getString("HOUR3")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR4", rset.getString("HOUR4")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR5", rset.getString("HOUR5")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR6", rset.getString("HOUR6")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR7", rset.getString("HOUR7")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR8", rset.getString("HOUR8")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR12", rset.getString("HOUR12")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR24", rset.getString("HOUR24")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR48", rset.getString("HOUR48")));
    } catch (Exception e) {
      ;
    }
    try {
      array.add(new DataNamedValue("HOUR72", rset.getString("HOUR72")));
    } catch (Exception e) {
      ;
    }
    if (array.size() > 0) {
      dp.namedValues = new DataNamedValue[array.size()];
      for (int i = 0; i < dp.namedValues.length; i++)
        dp.namedValues[i] = (DataNamedValue) array.get(i);
    }
    return dp;
  } // getInstance()

  //-----------------------------------------------------------------------------
  public String toJson(String indent, int brandGroup) {
    StringBuffer s = new StringBuffer();
    s.append(indent + "{\n");
    s.append(indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n");
    s.append(indent + "  \"brandname\": \"" + (brandName == null ? "" : Utils.convStr2Json(brandName)) + "\",\n");
    s.append(indent + "  \"distance\": " + (int) (distance + 0.5));
    s.append("\n");
    s.append(indent + "}");
    return s.toString();
  } // toJson()
  
  public String toJson(String indent) {
    String s = "";
    s += indent + "{\n";
    s += indent + "  \"id\": " + id + ",\n";
    s += indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n";
    s += indent + "  \"address\": \"" + (address == null ? "" : Utils.convStr2Json(address)) + "\",\n";
    s += indent + "  \"ilId\": " + ilId + ",\n";
    s += indent + "  \"ilAdi\": \"" + (ilAdi == null ? "" : Utils.convStr2Json(ilAdi)) + "\",\n";
    s += indent + "  \"ilceId\": " + ilceId + ",\n";
    s += indent + "  \"ilceAdi\": \"" + (ilceAdi == null ? "" : Utils.convStr2Json(ilceAdi)) + "\",\n";
    s += indent + "  \"mahalleId\": " + mahalleId + ",\n";
    s += indent + "  \"mahalleAdi\": \"" + (mahalleAdi == null ? "" : Utils.convStr2Json(mahalleAdi)) + "\",\n";
    s += indent + "  \"yolAdi\": \"" + (yolAdi == null ? "" : Utils.convStr2Json(yolAdi)) + "\",\n";
    s += indent + "  \"kapiNo\": \"" + (kapiNo == null ? "" : Utils.convStr2Json(kapiNo)) + "\",\n";
    s += indent + "  \"postalCode\": \"" + (postalCode == null ? "" : Utils.convStr2Json(postalCode)) + "\",\n";
    s += indent + "  \"phone\": \"" + (phone == null ? "" : Utils.convStr2Json(phone)) + "\",\n";
    s += indent + "  \"subtype\": " + subtype;
    
    if( brandName != null ){
         s += ",\n";
         s += indent + "  \"brandname\": \"" + Utils.convStr2Json(brandName) + "\"";
     }
    
    if (latitude != 0.00 && longitude != 0.00) {
      s += ",\n";
      s += indent + "  \"latitude\": " + Utils.makeCoorFormat(latitude) + ",\n";
      s += indent + "  \"longitude\": " + Utils.makeCoorFormat(longitude) + ",\n";
      s += indent + "  \"distance\": " + (int) (distance + 0.5);
    }
    if (namedValues != null) {
      s += ",\n";
      s += indent + "  \"nvs\": [\n";
      for (int i = 0; i < namedValues.length; i++) {
        if (i > 0)
          s += ",\n";
        s += indent + "  " + namedValues[i].toJson("  ");
      } // for()
      s += indent + "  ]";
    }
    if (poiAttributes != null) {
      s += ",\n";
      s += indent + "  \"attributes\": [\n";
      for (int i = 0; i < poiAttributes.length; i++) {
        if (i > 0)
          s += ",\n";
        s += indent + "  " + poiAttributes[i].toJson(" ");
      } // for()
      s += "\n"; 
      s += indent + "  ]";
    }
    if( markerName!=null){
        s += ",\n";
       s += indent + "  \"markerName\": \"" + Utils.convStr2Json(markerName) + "\"";
    }
    s += "\n";
    s += indent + "}";
    return s;
  } // toJson()
//--------------------------------------------------------------------------------------
  public String toXml() {
    String s = "";
    s += "    <poi>\n";
    s += "      <id>" + id + "</id>\n";
    s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
    s += "      <address>" + (address == null ? "" : Utils.convStr2Xml(address)) + "</address>\n";
    s += "      <ilid>" + ilId + "</ilid>\n";
    s += "      <iladi>" + (ilAdi == null ? "" : Utils.convStr2Xml(ilAdi)) + "</iladi>\n";
    s += "      <ilceid>" + ilceId + "</ilceid>\n";
    s += "      <ilceadi>" + (ilceAdi == null ? "" : Utils.convStr2Xml(ilceAdi)) + "</ilceadi>\n";
    s += "      <mahalleid>" + mahalleId + "</mahalleid>\n";
    s += "      <mahalleadi>" + (mahalleAdi == null ? "" : Utils.convStr2Xml(mahalleAdi)) + "</mahalleadi>\n";
    s += "      <yoladi>" + (yolAdi == null ? "" : Utils.convStr2Xml(yolAdi)) + "</yoladi>\n";
    s += "      <kapino>" + (kapiNo == null ? "" : Utils.convStr2Xml(kapiNo)) + "</kapino>\n";
    s += "      <postalcode>" + (postalCode == null ? "" : Utils.convStr2Xml(postalCode)) + "</postalcode>\n";
    s += "      <phone>" + (phone == null ? "" : Utils.convStr2Xml(phone)) + "</phone>\n";
    s += "      <subtype>" + subtype + "</subtype>\n";
    
    if( brandName != null )
          s += "      <brandname>" + Utils.convStr2Xml(brandName) + "</brandname>\n"; 

    if (latitude != 0.00 && longitude != 0.00) {
      s += "      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
      s += "      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
      s += "      <distance>" + (int) (distance + 0.5) + "</distance>\n";
    }
    if (namedValues != null) {
      s += "      <nvs>\n";
      for (int i = 0; i < namedValues.length; i++)
        s += "        " + namedValues[i].toXml();
      s += "      </nvs>\n";
    }
    if (poiAttributes != null) {
      s += "    <attributes>\n";
      for (int i = 0; i < poiAttributes.length; i++)
        s += "        " + poiAttributes[i].toXml();
      s += "    </attributes>\n";
    }
    
    if( markerName!=null )
        s += "      <markerName>" + Utils.convStr2Xml(markerName) + "</markerName>\n"; 
    s += "    </poi>\n";
    return s;
  } // toXml()

  //-----------------------------------------------------------------------------
  public String toXml(int brandGroup) {
    StringBuffer s = new StringBuffer();
    s.append("    <poi>\n");
    s.append("      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n");
    s.append("      <brandname>" + Utils.convStr2Xml(brandName) + "</brandname>\n"); 
    s.append("      <distance>" + (int) (distance + 0.5) + "</distance>\n");
    s.append("    </poi>\n");
    return s.toString();
  } // toXml()

//-------------------------------------------------------------------------------
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

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public void setIlId(long ilId) {
    this.ilId = ilId;
  }

  public long getIlId() {
    return ilId;
  }

  public void setIlAdi(String ilAdi) {
    this.ilAdi = ilAdi;
  }

  public String getIlAdi() {
    return ilAdi;
  }

  public void setIlceId(long ilceId) {
    this.ilceId = ilceId;
  }

  public long getIlceId() {
    return ilceId;
  }

  public void setIlceAdi(String ilceAdi) {
    this.ilceAdi = ilceAdi;
  }

  public String getIlceAdi() {
    return ilceAdi;
  }

  public void setMahalleId(long mahalleId) {
    this.mahalleId = mahalleId;
  }

  public long getMahalleId() {
    return mahalleId;
  }

  public void setMahalleAdi(String mahalleAdi) {
    this.mahalleAdi = mahalleAdi;
  }

  public String getMahalleAdi() {
    return mahalleAdi;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setYolAdi(String yolAdi) {
    this.yolAdi = yolAdi;
  }

  public String getYolAdi() {
    return yolAdi;
  }

  public void setKapiNo(String kapiNo) {
    this.kapiNo = kapiNo;
  }

  public String getKapiNo() {
    return kapiNo;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPhone() {
    return phone;
  }

  public void setSubtype(int subtype) {
    this.subtype = subtype;
  }

  public int getSubtype() {
    return subtype;
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

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public double getDistance() {
    return distance;
  }

  public void setNamedValues(DataNamedValue[] namedValues) {
    this.namedValues = namedValues;
  }

  public DataNamedValue[] getNamedValues() {
    return namedValues;
  }

  public void setPoiAttributes(DataPoiAttribute[] poiAttributes) {
    this.poiAttributes = poiAttributes;
  }

  public DataPoiAttribute[] getPoiAttributes() {
    return poiAttributes;
  }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }
}
