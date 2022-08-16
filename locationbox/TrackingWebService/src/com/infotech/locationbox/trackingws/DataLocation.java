package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class DataLocation {
  protected long mobileId = 0;
  protected long deviceId = 0;
  protected String custId = null;
  protected String timeStamp = null;
  protected long campusId = 0;
  protected String campusName = null;
  protected long venueId = 0;
  protected String venueName = null;
  protected int floor = 0;
  protected String floorName = null;
  protected long areaId = 0;
  protected String areaName = null;
  protected long baseStationId = 0;
  protected String baseStationName = null;
  protected double xcoor = 0.00;
  protected double ycoor = 0.00;

  protected int status = 0;
  protected String message = null;

  public DataLocation() {
  }

  //-----------------------------------------------------------------------------

  public static DataLocation getInstance(ResultSet rset) {
    DataLocation dl = new DataLocation();

    try {
      dl.mobileId = rset.getLong("MOBILE_ID");
      dl.deviceId = rset.getLong("DEVICE_ID");
      dl.custId = rset.getString("CUST_ID");
      dl.timeStamp = rset.getString("TIME_STAMP");
      if (dl.timeStamp != null && dl.timeStamp.length() > 19)
        dl.timeStamp = dl.timeStamp.substring(0, 19);
      dl.campusId = rset.getLong("CAMPUS_ID");
      dl.campusName = rset.getString("CAMPUS_NAME");
      dl.venueId = rset.getLong("VENUE_ID");
      dl.venueName = rset.getString("VENUE_NAME");
      dl.floor = rset.getInt("FLOOR");
      dl.floorName = rset.getString("FLOOR_NAME");
      dl.areaId = rset.getLong("AREA_ID");
      dl.areaName = rset.getString("AREA_NAME");
      dl.baseStationId = rset.getLong("BASE_STATION_ID");
      dl.baseStationName = rset.getString("BASE_STATION_NAME");
      dl.xcoor = rset.getDouble("XCOOR");
      dl.ycoor = rset.getDouble("YCOOR");
    } catch (Exception ex) {
      System.out.println("DataLocation " + dl.toString());
      ex.printStackTrace();
      return null;
    }

    return dl;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public static DataLocation getInstance(DataRegister dr, long mobileId) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();

    try {
      sql = "SELECT * FROM MOBILE_LOCATION_LAST WHERE MOBILE_ID=? AND KEY=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, mobileId);
      pstmt.setString(colno++, dr.key);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        DataLocation dl = getInstance(rset);
        return dl;
      }

    } catch (Exception ex) {
      Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return null;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public static DataLocation[] getInstance(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();

    List<DataLocation> dll = new ArrayList<>();
    DataLocation[] dls = null;

    try {
      sql = "SELECT * FROM MOBILE_LOCATION_LAST WHERE KEY=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setString(colno++, dr.key);
      rset = pstmt.executeQuery();
      while (rset.next()) {
        DataLocation dl = getInstance(rset);
        dll.add(dl);
      }

    } catch (Exception ex) {
      Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    dls = new DataLocation[dll.size()];
    for (int i = 0; i < dll.size(); i++) {
      dls[i] = dll.get(i);
    }

    return dls;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public static DataLocation[] getInstance(DataRegister dr, List<Long> mobileList) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();

    List<DataLocation> dll = new ArrayList<>();
    DataLocation[] dls = null;

    try {
      sql = "SELECT * FROM MOBILE_LOCATION_LAST WHERE KEY=? AND MOBILE_ID IN (?";
      for (int i = 1; i < mobileList.size(); i++)
        sql += ",?";
      sql += ")";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setString(colno++, dr.key);
      for (int i = 0; i < mobileList.size(); i++) {
        pstmt.setLong(colno++, mobileList.get(i));
      }
      rset = pstmt.executeQuery();
      while (rset.next()) {
        DataLocation dl = getInstance(rset);
        dll.add(dl);
      }

    } catch (Exception ex) {
      Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    dls = new DataLocation[dll.size()];
    for (int i = 0; i < dll.size(); i++) {
      dls[i] = dll.get(i);
    }

    return dls;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public static DataLocation getInstance(JsonObject params) {
    DataLocation dl = new DataLocation();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    try {
      dl.mobileId = params.getJsonNumber("mobileid").longValue();
      dl.deviceId = params.getJsonNumber("deviceid").longValue();
      try {
        dl.custId = String.valueOf(params.getJsonNumber("custid").longValue());
      } catch (Exception e) {
        dl.custId = params.getString("custid");
      }
      /*
            * VenueId opsiyonel olarak deðiþtirildi
            * @SAL  - 25.10.2017
            */
      if (params.getJsonNumber("venueid") != null) {
        dl.venueId = params.getJsonNumber("venueid").longValue();
      }
      dl.baseStationId = params.getJsonNumber("basestationid").longValue();
      dl.xcoor = params.getJsonNumber("xcoor").doubleValue();
      dl.ycoor = params.getJsonNumber("ycoor").doubleValue();
      dl.floor = params.getJsonNumber("floor").intValue();
      dl.status = params.getJsonNumber("status").intValue();
      dl.message = params.getString("message");


      if (params.getString("timestamp").length() == 14)
        dl.timeStamp = Utils.timeStampControl(df.parse(params.getString("timestamp")));
      else if (params.getString("timestamp").length() == 10) //timestamp?n boyutu 11 oldu?u zaman ki tarih -> Cumartesi, 20 Kas?m 2286 20:46:39
        dl.timeStamp = Utils.timeStampToDate(Long.valueOf(params.getString("timestamp")));

    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }

    return dl;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public static DataLocation[] getInstance(JsonArray paramsArray) {
    DataLocation dl = new DataLocation();

    DataLocation[] dlList = new DataLocation[paramsArray.size()];

    JsonObject params = null;

    for (int i = 0; i < paramsArray.size(); i++) {
      params = paramsArray.getJsonObject(i);

      try {
        dl.mobileId = params.getJsonNumber("mobileid").longValue();
        dl.deviceId = params.getJsonNumber("deviceid").longValue();
        dl.custId = params.getString("custid");
        dl.venueId = params.getJsonNumber("venueid").longValue();
        dl.baseStationId = params.getJsonNumber("basestationid").longValue();
        dl.xcoor = params.getJsonNumber("xcoor").doubleValue();
        dl.ycoor = params.getJsonNumber("ycoor").doubleValue();
        dl.floor = params.getJsonNumber("floor").intValue();
        dl.status = params.getJsonNumber("status").intValue();
        dl.message = params.getString("message");
        dl.timeStamp = params.getString("timestamp");

        dlList[i] = dl;
      } catch (Exception ex) {
        ex.printStackTrace();
        return null;
      }
    }

    return dlList;
  } // getInstance()

  //-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"mobileid\": " + mobileId + ", \"deviceid\":  " + deviceId + ",  \"custid\": \"" + custId + "\", " + "\"ts\": \"" + timeStamp + "\", \"campusid\": " + campusId +
           ", \"campusname\": \"" + campusName + "\", " + "\"venueid\": " + venueId + ", \"venuename\": \"" + venueName + "\", \"floor\": " + floor + ", \"floorname\": \"" + floorName + "\", " +
           "\"areaid\": " + areaId + ", \"areaname\": \"" + areaName + "\", \"basestationid\": " + baseStationId + ", \"basestationname\": \"" + baseStationName + "\", " + "\"xcoor\": " +
           (Math.round(xcoor * 10000000.0) / 10000000.0) + ", \"ycoor\": " + (Math.round(ycoor * 10000000.0) / 10000000.0) + " }";
  }

  //-----------------------------------------------------------------------------

  public long insert(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int colno = 0;
    int count = 0;

    Connection cnn = DbConn.getPooledConnection();

    try {
      // Standart kullanimda olmasi gereken bir kontrol ancak, standartlasma asamasinda herhangi bir mobile_id ile insert edilmesi saglanacak..
      /*
             * KEY kontrolü eklendi
             * @SAL 25.10.2017
             */
      if (mobileId > 0) {
        sql = "SELECT COUNT(*) FROM MOBILE WHERE MOBILE_ID=? AND KEY=?";
        pstmt = cnn.prepareStatement(sql);
        pstmt.clearParameters();

        pstmt.setLong(1, mobileId);
        pstmt.setString(2, dr.key);

        rset = pstmt.executeQuery();
        rset.next();
        int cnt = rset.getInt(1);
        if (cnt <= 0)
          return -1;

        rset.close();
        rset = null;
        pstmt.close();
        pstmt = null;
      }

      else {
        mobileId = DataMobile.checkOrCreateMobileId(deviceId, custId, dr.key);
        if (mobileId <= 0)
          return -1;
      }

      int res = fillInfoFromAdminArea();
      //("LOCATIONBOX"."MOBILE_LOCATION"."VENUE_ID") içine NULL eklenemez
      if (res < 0) {
        venueId = 0;
        //floor = 0;
      }

      sql =
        "INSERT INTO MOBILE_LOCATION (ROWNO,KEY,MOBILE_ID,CUST_ID,DEVICE_ID,TIME_STAMP,CAMPUS_ID,CAMPUS_NAME,VENUE_ID,VENUE_NAME,FLOOR,FLOOR_NAME,AREA_ID,AREA_NAME,BASE_STATION_ID,BASE_STATION_NAME,XCOOR,YCOOR,GEOLOC,PROCESS_STATUS,PROCESS_TIME_STAMP,PROCESS_MODULE_NAME,PROCESS_MODULE_INSTANCE)";
      sql += " VALUES (SEQ_MOBILE_LOCATION_ROWNO.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,(SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)),0,NULL,NULL,NULL)";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setString(colno++, dr.key);
      pstmt.setLong(colno++, mobileId);
      pstmt.setString(colno++, custId);
      pstmt.setLong(colno++, deviceId);
      pstmt.setTimestamp(colno++, Utils.toTimestamp(timeStamp));
      if (campusId <= 0) {
        pstmt.setNull(colno++, Types.NUMERIC);
        pstmt.setNull(colno++, Types.VARCHAR);
      } else {
        pstmt.setLong(colno++, campusId);
        pstmt.setString(colno++, campusName);
      }
      if (venueId <= 0) {
        pstmt.setLong(colno++, 0);
        pstmt.setNull(colno++, Types.VARCHAR);
        pstmt.setLong(colno++, floor);
        pstmt.setNull(colno++, Types.VARCHAR);
      } else {
        pstmt.setLong(colno++, venueId);
        pstmt.setString(colno++, venueName);
        pstmt.setInt(colno++, floor);
        pstmt.setString(colno++, floorName);
      }
      if (areaId <= 0) {
        pstmt.setNull(colno++, Types.NUMERIC);
        pstmt.setNull(colno++, Types.VARCHAR);
      } else {
        pstmt.setLong(colno++, areaId);
        pstmt.setString(colno++, areaName);
      }
      if (baseStationId <= 0) {
        pstmt.setNull(colno++, Types.NUMERIC);
        pstmt.setNull(colno++, Types.VARCHAR);
      } else {
        pstmt.setLong(colno++, baseStationId);
        pstmt.setString(colno++, baseStationName);
      }
      pstmt.setDouble(colno++, xcoor);
      pstmt.setDouble(colno++, ycoor);
      pstmt.setDouble(colno++, xcoor);
      pstmt.setDouble(colno++, ycoor);
      count = pstmt.executeUpdate();
    } catch (Exception ex) {
      Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
      return -9;
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return mobileId;
  } // insert()

  //-----------------------------------------------------------------------------

  //@SAL
  public int fillInfoFromAdminArea() {
    PreparedStatement pstmt = null;
    PreparedStatement pstmtCampus = null;
    ResultSet rset = null;
    String sql = null;
    String campusSql = null;

    Connection cnn = DbConn.getPooledConnection();

    try {
      //Floor koþulu kaldýrýldý ...WHERE FLOOR=? AND SDO_...
      sql = "SELECT * FROM INDOOR_ADMIN_AREA WHERE ";
      //DenizBank'a dikkat floor=0 var
      if (floor != 0) {
        sql += "FLOOR=? AND ";
      }
      sql += "SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE'";

      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      if (floor != 0) {
        pstmt.setDouble(colno++, floor);
      }
      pstmt.setDouble(colno++, xcoor);
      pstmt.setDouble(colno++, ycoor);
      rset = pstmt.executeQuery();

      if (!rset.next()) {

        campusSql = "SELECT * FROM INDOOR_CAMPUS WHERE SDO_ANYINTERACT(GEOMBR, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE'";
        pstmtCampus = cnn.prepareStatement(campusSql);
        pstmtCampus.clearParameters();
        pstmtCampus.setDouble(1, xcoor);
        pstmtCampus.setDouble(2, ycoor);
        rset = pstmtCampus.executeQuery();
        if (!rset.next()) {
          return -1;
        } else {
          campusId = rset.getLong("CAMPUS_ID");
          campusName = rset.getString("CAMPUS_NAME");
        }
      } else {
        /*CAMPUSID ve FLOOR eklendi
                 * @SAL - 25.10.2017
                 * Eðer floor->0 girilir ve indoor_admin_area'da data gelirse en düþük kattakini alýyor
                 */
        campusId = rset.getLong("CAMPUS_ID");
        campusName = rset.getString("CAMPUS_NAME");
        venueId = rset.getLong("VENUE_ID");
        venueName = rset.getString("VENUE_NAME");
        floor = rset.getInt("FLOOR");
        floorName = rset.getString("FLOOR_NAME");
        areaId = rset.getLong("AREA_ID");
        areaName = rset.getString("AREA_NAME");
      }


    } catch (Exception ex) {
      Utils.showError("Exception message: " + ex.getMessage());
      ex.printStackTrace();
      return -9;
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return 0;
  } // fillInfoFromAdminArea()

}
