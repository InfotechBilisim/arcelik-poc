package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataBagimsizBirim {
    private long mahalleId = 0;
    private long uavtKapiId = 0;
    private long uavtIcKapiId = 0;
    private String icKapiTuru = null;
    private String icKapiNo = null;


    public DataBagimsizBirim(long mahalleId, long uavtKapiId, long uavtIcKapiId, String icKapiTuru, String icKapiNo) {
        this.mahalleId = mahalleId;
        this.uavtKapiId = uavtKapiId;
        this.uavtIcKapiId = uavtIcKapiId;
        this.icKapiTuru = icKapiTuru;
        this.icKapiNo = icKapiNo;
    }

    private DataBagimsizBirim() {
    }

    public static DataBagimsizBirim getInstance(ResultSet rset) {
        DataBagimsizBirim dk = new DataBagimsizBirim();
        try {
            dk.uavtKapiId = rset.getLong(1);
            dk.uavtIcKapiId = rset.getLong(2);
            dk.mahalleId = rset.getLong(3);
            dk.icKapiNo = rset.getString(4);
            dk.icKapiTuru = rset.getString(5);
            return dk;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        StringBuilder s = new StringBuilder();
    
        s.append(indent + "{\n");
        s.append(indent + "  \"uavtKapiId\": " + uavtKapiId + ",\n");
        s.append(indent + "  \"uavtIcKapiId\": " + uavtIcKapiId + ",\n");
        s.append(indent + "  \"mahalleId\": " + mahalleId + ",\n");
        s.append(indent + "  \"icKapiNo\": \"" + (icKapiNo == null ? "" : Utils.convStr2Json(icKapiNo)) + "\",\n");
        s.append(indent + "  \"icKapiTuru\": \"" + (icKapiTuru == null ? "" : Utils.convStr2Json(icKapiTuru)) + "\"\n");
        
        s.append( indent + "}");
        return s.toString();
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
       StringBuilder s = new StringBuilder();
       s.append( "   <icKapi>\n");
       s.append( "      <uavtKapiId>" + uavtKapiId + "</uavtKapiId>\n");
       s.append("       <uavtIcKapiId>" + uavtIcKapiId + "</uavtIcKapiId>\n");
        s.append("      <mahalleId>" + mahalleId + "</mahalleId>\n");
       s.append("       <icKapiNo>" + (icKapiNo == null ? "" : Utils.convStr2Xml(icKapiNo)) + "</icKapiNo>\n");
       s.append( "      <icKapiTuru>" + (icKapiTuru == null ? "" : Utils.convStr2Xml(icKapiTuru)) + "</icKapiTuru>\n");
       s.append( "   </icKapi>\n");
      return s.toString();
    } // toXml()



    public void setMahalleId(long mahalleId) {
        this.mahalleId = mahalleId;
    }

    public long getMahalleId() {
        return mahalleId;
    }

    public void setUavtKapiId(long uavtKapiId) {
        this.uavtKapiId = uavtKapiId;
    }

    public long getUavtKapiId() {
        return uavtKapiId;
    }

    public void setUavtIcKapiId(long uavtIcKapiId) {
        this.uavtIcKapiId = uavtIcKapiId;
    }

    public long getUavtIcKapiId() {
        return uavtIcKapiId;
    }

    public void setIcKapiTuru(String icKapiTuru) {
        this.icKapiTuru = icKapiTuru;
    }

    public String getIcKapiTuru() {
        return icKapiTuru;
    }

    public void setIcKapiNo(String icKapiNo) {
        this.icKapiNo = icKapiNo;
    }

    public String getIcKapiNo() {
        return icKapiNo;
    }

}
