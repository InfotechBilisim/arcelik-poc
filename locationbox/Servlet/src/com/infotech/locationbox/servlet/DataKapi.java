package com.infotech.locationbox.servlet;

import java.sql.ResultSet;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

public class DataKapi {
    private long id = 0;
    private String name = null;
    private String no = null;
    private double latitude = 0.00;
    private double longitude = 0.00;
    private int zoomLevel = 0;
    private String postaKodu = null;
    private long ilId = 0;
    private String ilAdi = null;
    private long ilceId = 0;
    private String ilceAdi = null;
    private long mahalleId = 0;
    private String mahalleAdi = null;
    private long koyId = 0;
    private String koyAdi = null;
    private long yolId = 0;
    private String yolAdi = null;
    private int zone = 0;
    private long adresKodu = 0;
    private long yolAdresKodu = 0;
    private long koyAdresKodu = 0;
    private long mahalleAdresKodu = 0;
    private long ilceAdresKodu = 0;
    private long ilAdresKodu = 0;
    private JGeometry[] objs = null;
    private boolean encode = false;
    private boolean existGeo = false;

    public DataKapi() {
    }

    public DataKapi(long id, String name, String no, double latitude, double longitude, int zoomLevel, String postaKodu,
                    long ilId, String ilAdi, long ilceId, String ilceAdi, long mahalleId, String mahalleAdi, long yolId,
                    String yolAdi, int zone) {
        this.id = id;
        this.name = name;
        this.no = no;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoomLevel = zoomLevel;
        this.postaKodu = postaKodu;
        this.ilId = ilId;
        this.ilAdi = ilAdi;
        this.ilceId = ilceId;
        this.ilceAdi = ilceAdi;
        this.mahalleId = mahalleId;
        this.mahalleAdi = mahalleAdi;
        this.yolId = yolId;
        this.yolAdi = yolAdi;
        this.zone = zone;
    }

    //-----------------------------------------------------------------------------

    public static DataKapi getInstance(ResultSet rset, boolean withCoors, boolean encode) {
        DataKapi dk = new DataKapi();
        try {
            dk.id = rset.getLong(1);
            dk.name = rset.getString(2);
            dk.no = rset.getString(3);
            dk.zoomLevel = rset.getInt(4);
            dk.postaKodu = rset.getString(5);
            dk.longitude = rset.getDouble(6);
            dk.latitude = rset.getDouble(7);
            dk.ilId = rset.getLong(8);
            dk.ilAdi = rset.getString(9);
            dk.ilceId = rset.getLong(10);
            dk.ilceAdi = rset.getString(11);
            dk.mahalleId = rset.getLong(12);
            dk.mahalleAdi = rset.getString(13);
            dk.koyId = rset.getLong(14);
            dk.koyAdi = rset.getString(15);
            dk.yolId = rset.getLong(16);
            dk.yolAdi = rset.getString(17);
            dk.zone = rset.getInt(18);
            dk.adresKodu = rset.getLong(20);
            dk.yolAdresKodu = rset.getLong(21);
            dk.koyAdresKodu = rset.getLong(22);
            dk.mahalleAdresKodu = rset.getLong(23);
            dk.ilceAdresKodu = rset.getLong(24);
            dk.ilAdresKodu = rset.getLong(25);
            if (withCoors) {
              dk.setExistGeo(withCoors); 
              STRUCT objGeo = DbConn.convToSTRUCT(rset.getObject(26));
              if(objGeo!= null){
               JGeometry geo = JGeometry.load(objGeo);
               dk.objs = geo.getElements();
              }else{
                  dk.objs = null;
              }
              dk.encode = encode;
            }
            return dk;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void setEncode(boolean encode) {
        this.encode = encode;
    }

    public boolean isEncode() {
        return encode;
    }
    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        StringBuilder s = new StringBuilder();
  
        s.append(indent + "{\n");
        s.append(indent + "  \"id\": " + id + ",\n");
        s.append(indent + "  \"adreskodu\": " + (adresKodu == 0 ? "0" : adresKodu) + "");
        s.append(",\n");
        s.append(" \"postakodu\": \""  + (postaKodu == null ? "" : Utils.convStr2Json(postaKodu))  + "\",\n");
        s.append(indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n");
        s.append(indent + "  \"no\": \"" + (no == null ? "" : Utils.convStr2Json(no)) + "\"\n");
        
        if (isExistGeo()) {
           s.append( ",");
           s.append( indent + "  \"geometry\": [\n");
          if (objs != null) {
              for (int k = 0; k < objs.length; k++) {
                double[] oarray = objs[k].getOrdinatesArray();
                if (encode) {
                  String pline = PolylineUtils.encodePolyline(oarray);
                  s.append( indent + "    \"" + pline + "\"");
                } else {
                  if (k > 0)
                   s.append(",\n");
                  StringBuilder sbuf = new StringBuilder(oarray.length * 12);
                  for (int i = 0; i < oarray.length; i++) {
                    if (i > 0)
                      sbuf.append(',');
                    sbuf.append(oarray[i]);
                  } // for(i)
                  s.append( indent + "    \"" + sbuf.toString() + "\"");
                 }
             } // for(k)
          }
         s.append("\n");
         s.append( indent + "  ]");
          
        } // if(isExistGeo)
        
        s.append( indent + "}");
        return s.toString();
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
       StringBuilder s = new StringBuilder();
       s.append( "    <kapi>\n");
       s.append( "      <id>" + id + "</id>\n");
       s.append("      <adreskodu>" + adresKodu + "</adreskodu>\n");
       s.append("      <postakodu>" + (postaKodu == null ? "" : Utils.convStr2Xml(postaKodu)) + "</postakodu>\n");
       s.append("      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n");
       s.append( "      <no>" + (no == null ? "" : Utils.convStr2Xml(no)) + "</no>\n");
        if (isExistGeo()) {
         s.append("      <geometry>\n");
         if (objs != null) {
              for (int k = 0; k < objs.length; k++) {
                double[] oarray = objs[k].getOrdinatesArray();
                if (encode) {
                  String pline = PolylineUtils.encodePolyline(oarray);
                  s.append("        <ordinates>" + pline + "</ordinates>\n");
                } else {
                  StringBuilder sbuf = new StringBuilder(oarray.length * 12);
                  for (int i = 0; i < oarray.length; i++) {
                    if (i > 0)
                      sbuf.append(',');
                    sbuf.append(oarray[i]);
                  } // for(i)
                s.append( "        <ordinates>" + sbuf.toString() + "</ordinates>\n");
                }
              } // for(k)
         }
         s.append("      </geometry>\n");
        } // if(objs)
        
       s.append( "    </kapi>\n");
      return s.toString();
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

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
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

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setPostaKodu(String postaKodu) {
        this.postaKodu = postaKodu;
    }

    public String getPostaKodu() {
        return postaKodu;
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
    
    public void setKoyId(long koyId) {
        this.koyId = koyId;
    }

    public long getKoyId() {
        return koyId;
    }

    public void setKoyAdi(String koyAdi) {
        this.koyAdi = koyAdi;
    }

    public String getKoyAdi() {
        return koyAdi;
    }

    public void setYolId(long yolId) {
        this.yolId = yolId;
    }

    public long getYolId() {
        return yolId;
    }

    public void setYolAdi(String yolAdi) {
        this.yolAdi = yolAdi;
    }

    public String getYolAdi() {
        return yolAdi;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public int getZone() {
        return zone;
    }

    public void setAdresKodu(long adresKodu) {
        this.adresKodu = adresKodu;
    }

    public long getAdresKodu() {
        return adresKodu;
    }
    
    public void setYolAdresKodu(long yolAdresKodu) {
        this.yolAdresKodu = yolAdresKodu;
    }

    public long getYolAdresKodu() {
        return yolAdresKodu;
    }

    public void setKoyAdresKodu(long koyAdresKodu) {
        this.koyAdresKodu = koyAdresKodu;
    }

    public long getKoyAdresKodu() {
        return koyAdresKodu;
    }

    public void setMahalleAdresKodu(long mahalleAdresKodu) {
        this.mahalleAdresKodu = mahalleAdresKodu;
    }

    public long getMahalleAdresKodu() {
        return mahalleAdresKodu;
    }

    public void setIlceAdresKodu(long ilceAdresKodu) {
        this.ilceAdresKodu = ilceAdresKodu;
    }

    public long getIlceAdresKodu() {
        return ilceAdresKodu;
    }

    public void setIlAdresKodu(long ilAdresKodu) {
        this.ilAdresKodu = ilAdresKodu;
    }

    public long getIlAdresKodu() {
        return ilAdresKodu;
    }

    public void setExistGeo(boolean existGeo) {
        this.existGeo = existGeo;
    }

    public boolean isExistGeo() {
        return existGeo;
    }
}
