package com.infotech.locationbox.servlet;

import java.sql.ResultSet;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

public class DataYol {
    protected long id = 0;
    protected String name = null;
    protected double latitude = 0.00;
    protected double longitude = 0.00;
    protected int yolSinifi = 0;
    protected String yolTuru = null;
    protected int zoomLevel = 0;
    protected String postaKodu = null;
    protected long ilId = 0;
    protected String ilAdi = null;
    protected long ilceId = 0;
    protected String ilceAdi = null;
    protected long mahalleId = 0;
    protected String mahalleAdi = null;
    protected JGeometry[] objs = null;

    protected boolean encode = false;
    protected long adresKodu = 0;

    public void setYolTuru(String yolTuru) {
        this.yolTuru = yolTuru;
    }

    public String getYolTuru() {
        return yolTuru;
    }

    public void setEncode(boolean encode) {
        this.encode = encode;
    }

    public boolean isEncode() {
        return encode;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }
    protected double distance;

    public DataYol() {
    }

    public DataYol(long id, String name, double latitude, double longitude, int yolSinifi, int zoomLevel, String postaKodu, long ilId, String ilAdi, long ilceId, String ilceAdi, long mahalleId, String mahalleAdi, long adresKodu, String yolTuru) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.yolSinifi = yolSinifi;
        this.zoomLevel = zoomLevel;
        this.postaKodu = postaKodu;
        this.ilId = ilId;
        this.ilAdi = ilAdi;
        this.ilceId = ilceId;
        this.ilceAdi = ilceAdi;
        this.mahalleId = mahalleId;
        this.mahalleAdi = mahalleAdi;
        this.adresKodu = adresKodu;  this.yolTuru = yolTuru;
    }
    
    public DataYol(long id, String name, double latitude, double longitude, int zoomLevel, String postaKodu, long ilId,
                   String ilAdi, long ilceId, String ilceAdi, long mahalleId, String mahalleAdi, double distance) {
        this.id = id;
        this.name = name;
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
        this.distance = distance;
    }

    //-----------------------------------------------------------------------------
    public static DataYol getInstance(ResultSet rset) {
        DataYol dy = new DataYol();
        try {
            dy.id = rset.getLong("YOL_ID");
            dy.name = rset.getString("YOL_ADI");
            dy.longitude = rset.getDouble("XCOOR");
            dy.latitude = rset.getDouble("YCOOR");
            dy.mahalleId = rset.getLong("CENTER_MAHALLE_ID");
            dy.mahalleAdi = rset.getString("CENTER_MAHALLE_ADI");
            dy.distance = rset.getDouble("DISTANCE");
            return dy;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    } // getInstance()
    //-----------------------------------------------------------------------------

    public static DataYol getInstance(ResultSet rset, boolean withCoors, boolean encode) {
        DataYol dy = new DataYol();
        try {
            dy.id = rset.getLong(1);
            dy.name = rset.getString(2);
            dy.zoomLevel = rset.getInt(3);
            dy.postaKodu = rset.getString(4);
            dy.longitude = rset.getDouble(5);
            dy.latitude = rset.getDouble(6);
            dy.adresKodu = rset.getLong(7);
            dy.ilId = rset.getLong(8);
            dy.ilAdi = rset.getString(9);
            dy.ilceId = rset.getLong(10);
            dy.ilceAdi = rset.getString(11);
            dy.mahalleId = rset.getLong(12);
            dy.mahalleAdi = rset.getString(13);
            if (withCoors) {
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(14));
                JGeometry geo = JGeometry.load(obj);
                dy.objs = geo.getElements();
                dy.encode = encode;
            }
            return dy;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public static DataYol getInstance(ResultSet rset, boolean withCoors, boolean encode, long adresKodu) {
        DataYol dy = new DataYol();
        try {
            dy.id = rset.getLong(1);
            dy.adresKodu = rset.getLong(2);
            dy.name = rset.getString(3);
            dy.longitude = rset.getDouble(4);
            dy.latitude = rset.getDouble(5);
            dy.zoomLevel = rset.getInt(6);
            if (withCoors) {
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(7));
                JGeometry geo = JGeometry.load(obj);
                dy.objs = geo.getElements();
                dy.encode = encode;
            }
            return dy;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"id\": " + id + ",\n";
        s += indent + "  \"adreskodu\": " + (adresKodu == 0 ? "0" : adresKodu) + "";
        s += ",\n";
        s += indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\"";
        s += ",\n";
        s += indent + "  \"latitude\": " + Utils.makeCoorFormat(latitude) + ",\n";
        s += indent + "  \"longitude\": " + Utils.makeCoorFormat(longitude);
        s += ",\n" + indent + "  \"zoomlevel\": " + zoomLevel;
        if (distance > 0)
          s += ",\n" + indent + "  \"distance\": " + distance;
    
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
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <yol>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <adreskodu>" + adresKodu + "</adreskodu>\n";
        s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
        s += "      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
        s += "      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
        s += "      <zoomlevel>" + zoomLevel + "</zoomlevel>\n";
        if (distance > 0)
          s += "      <distance>" + distance + "</distance>\n";
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
        s += "    </yol>\n";
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
    
    public void setYolSinifi(int yolSinifi) {
        this.yolSinifi = yolSinifi;
    }

    public int getYolSinifi() {
        return yolSinifi;
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

    public void setAdresKodu(long adresKodu) {
        this.adresKodu = adresKodu;
    }

    public long getAdresKodu() {
        return adresKodu;
    }
}
