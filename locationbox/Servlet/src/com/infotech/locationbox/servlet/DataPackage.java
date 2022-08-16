package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataPackage {
    private String name = null;
    private int count = 0;
    private String expireDate = null;

    public DataPackage() {
    }

    //-----------------------------------------------------------------------------

    public static DataPackage getInstance(ResultSet rset) {
        DataPackage dp = new DataPackage();

        try {
            dp.name = rset.getString("PACKAGE_NAME");
            dp.count = rset.getInt("COUNT");
            dp.expireDate = rset.getString("EXPIRE_DATE");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return dp;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson() {
        return "{ \"name\": \"" + Utils.convStr2Xml(name) + "\", \"count\": " + count + ", \"expire\": \"" +
               Utils.formatDate(expireDate) + "\" }";
    } // toJason()

    //-----------------------------------------------------------------------------

    public String toXml() {
        return "<package><name>" + Utils.convStr2Xml(name) + "</name><count>" + count + "</count><expire>" +
               Utils.formatDate(expireDate) + "</expire></package>\n";
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

}
