package com.infotech.locationbox.servlet;

public class DataPoiAttribute {

    protected long poiId = 0;
    protected String attribute = null;
    protected int type = 0;
    protected String value = null;

    public DataPoiAttribute() {

    }

    public DataPoiAttribute(long poiId, String attribute, int dataType, String value) {
        this.poiId = poiId;
        this.attribute = attribute;
        this.type = dataType;
        this.value = value;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        return indent + "{ \"name\": \"" + Utils.convStr2Json(attribute) + "\", \"type\": " + type + ", \"value\": \"" +
               Utils.convStr2Json(value) + "\" }";
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        return "<attributes><name>" + Utils.convStr2Xml(attribute) + "</name><type>" + type + "</type><value>" +
               Utils.convStr2Xml(value) + "</value></attributes>";
    } // toXml()

    //-----------------------------------------------------------------------------


    public DataPoiAttribute(String attribute, int dataType) {
        this.attribute = attribute;
        this.type = dataType;
    }

    public DataPoiAttribute(String attribute, int dataType, String value) {
        this.attribute = attribute;
        this.type = dataType;
        this.value = value;
    }

    public void setPoiId(long poiId) {
        this.poiId = poiId;
    }

    public long getPoiId() {
        return poiId;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setDataType(int dataType) {
        this.type = dataType;
    }

    public int getDataType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
