package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class LbsGlobalGeocode{
    private String key = null;
    private int typ = 0;
    private String url = null;
    private String apiKey = null;
    private String reverseUrl = null;
    
    public static LbsGlobalGeocode getInstance(ResultSet rset) {
        LbsGlobalGeocode lbsGlobalGeocode = new LbsGlobalGeocode();
        try {
            lbsGlobalGeocode.key = rset.getString("KEY");
            lbsGlobalGeocode.typ = rset.getInt("TYP");
            lbsGlobalGeocode.url = rset.getString("URL");
            lbsGlobalGeocode.apiKey = rset.getString("API_KEY");
            lbsGlobalGeocode.reverseUrl = rset.getString("REVERSE_URL");
            return lbsGlobalGeocode;
        } catch (Exception e) {
            Utils.showError("LbsGlobalGeocode.getInstance" + e.getMessage());
        }
        return null;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getTyp() {
        return typ;
    }

    public String getUrl() {
        return url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getReverseUrl() {
        return reverseUrl;
    }

}
