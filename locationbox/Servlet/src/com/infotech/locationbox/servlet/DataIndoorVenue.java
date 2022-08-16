package com.infotech.locationbox.servlet;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import oracle.sql.STRUCT;

public class DataIndoorVenue {
  
    private long id = 0;
    private String name = null;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Extent extent = new Extent();
    private Map<Integer, String> floors = new HashMap<Integer, String>();
    private Map<Integer, String> entrances = new HashMap<Integer, String>();

    public DataIndoorVenue() {
    }

    public static DataIndoorVenue getInstance(ResultSet rset) {
        DataIndoorVenue div = new DataIndoorVenue();

        try {
          div.id = rset.getLong("VENUE_ID");
          div.name = rset.getString("VENUE_NAME");
          div.longitude =  rset.getDouble("XCOOR");
          div.latitude = rset.getDouble("YCOOR");
          STRUCT obj = DbConn.convToSTRUCT(rset.getObject("GEOMBR"));
          if (obj != null)  div.setExtent(Utils.getExtent(obj));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return div;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson() {
        String s = "";
        s += "{\n";
        s += "  \"id\": " + id + ",\n";
        s += "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n";
        s += "  \"latitude\": \"" + Utils.makeCoorFormat(latitude) + "\",\n";
        s += "  \"longitude\": \"" + Utils.makeCoorFormat(longitude) + "\",\n";
        s += "  \"extent\": { \"minlatitude\": " + Utils.makeCoorFormat(extent.getMinLatitude()) +
          ", \"minlongitude\": " + Utils.makeCoorFormat(extent.getMinLongitude()) + ", \"maxlatitude\": " +
          Utils.makeCoorFormat(extent.getMaxLatitude()) + ", \"maxlongitude\": " +
          Utils.makeCoorFormat(extent.getMaxLongitude()) + " }, ";
        s += "  \"floors\": [ \n";
        int i = 0;
        for ( int key : floors.keySet() ) {
          i++;
          s += "  [ " + key + ", \"" + floors.get(key)  + "\" ]";
          if (i < floors.size() ) s += ",";
        }
        s +=  "],";
        s += "  \"entrances\": [ \n";
        int j = 0;
        for ( int key : entrances.keySet() ) {
          j++;
          s += "  [ " + key + ", \"" + entrances.get(key)  + "\" ]";
          if (j < entrances.size() ) s += ",";
        }
        s +=  "]";
        s +=  "}";
        s += "\n";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <venue>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <name>" + Utils.convStr2Xml(name) + "</name>\n";
        s += "      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
        s += "      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
        s += "      <extent>\n";
        s += "         <minlatitude>" + Utils.makeCoorFormat(extent.getMinLatitude()) + "</minlatitude>\n" ;
        s += "         <minlongitude>" + Utils.makeCoorFormat(extent.getMinLongitude()) + "</minlongitude>\n";
        s += "         <maxlatitude>" + Utils.makeCoorFormat(extent.getMaxLatitude()) + "</maxlatitude>\n";
        s += "         <maxlongitude>" + Utils.makeCoorFormat(extent.getMaxLongitude()) + "</maxlongitude>\n";
        s += "      </extent>\n";
        s += "      <floors>\n";
        for ( int key : floors.keySet() ) {
          s += "         <floor>";
          s += "           <level>" + key  + "</level><name>" + floors.get(key)  + "</name>";
          s += "         </floor>";
        }
        s += "      </floors>\n";
        s += "      <entrances>\n";
        for ( int key : entrances.keySet() ) {
          s += "         <entrance>";
          s += "           <level>" + key  + "</level><name>" + entrances.get(key)  + "</name>";
          s += "         </entrance>";
        }
        s += "      </entrances>\n";
        s += "    </venue>\n";
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

    public void setExtent(Extent extent) {
      this.extent = extent;
    }

    public Extent getExtent() {
      return extent;
    }

    public void setFloors(Map<Integer, String> floors) {
      this.floors = floors;
    }

    public Map<Integer, String> getFloors() {
      return floors;
    }
    
    public void setEntrances(Map<Integer, String> entrances) {
      this.entrances = entrances;
    }

    public Map<Integer, String> getEntrances() {
      return entrances;
    }

}
