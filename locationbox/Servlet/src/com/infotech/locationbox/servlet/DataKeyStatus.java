package com.infotech.locationbox.servlet;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DataKeyStatus {
    private Date expireDate = null;
    private int requestLimit = 0;
    private int requestCount = 0;
    private int durum = 0;
    private String key = "";


    public DataKeyStatus() {
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public void setRequestLimit(int requestLimit) {
        this.requestLimit = requestLimit;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public void setStatus(int durum) {
        this.durum = durum;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toXml() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        s += "    <key>" + key + "</key>\n";
        s += "    <expire_date>" + sdf.format(expireDate) + "</expire_date>\n";
        s += "    <request_limit>" + requestLimit + "</request_limit>\n";
        s += "    <request_count>" + requestCount + "</request_count>\n";
        s += "    <active>" + durum + "</active>\n";
        return s;
    }

    public String toJson(String indent) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        s += "\"key\": \"" + key + "\",\n";
        s += indent + indent + "\"expireDate\": \"" + sdf.format(expireDate) + "\",\n";
        s += indent + indent + "\"requestLimit\": " + requestLimit + ",\n";
        s += indent + indent + "\"requestCount\": " + requestCount + ",\n";
        s += indent + indent + "\"active\": " + durum + ",\n";
        return s;
    }
}
