package com.infotech.locationbox.servlet;

import com.infotech.address.cleaner.DataNameValue;

public class DataNamedValue {
    private String name = null;
    private String value = null;

    public static final String VALUE_YES = "YES";
    public static final String VALUE_NO = "NO";

    public DataNamedValue() {
    }

    public DataNamedValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public DataNamedValue(String name, long value) {
        this.name = name;
        this.value = "" + value;
    }

    public DataNamedValue(String name, double value) {
        this.name = name;
        this.value = "" + value;
    }

    //-----------------------------------------------------------------------------

    public static String getItemValue(DataNameValue[] nvp, String itemName) {
        if (nvp == null)
            return null;

        for (int i = 0; i < nvp.length; i++) {
            DataNameValue nv = nvp[i];
            if (nv.getName().equalsIgnoreCase(itemName))
                return nv.getValue();

        } // for()

        return null;
    } // getItemValue()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        return indent + "{ \"name\": \"" + Utils.convStr2Json(name) + "\", \"value\": \"" + value + "\" }";
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        return "<nv><name>" + Utils.convStr2Xml(name) + "</name><value>" + value + "</value></nv>";
    } // toXml()

    //-----------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
