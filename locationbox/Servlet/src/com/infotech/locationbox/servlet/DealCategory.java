package com.infotech.locationbox.servlet;

public class DealCategory {
    private int id = 0;
    private String name = null;
    private int count = -1;

    public DealCategory() {
        super();
    }

    public DealCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public DealCategory(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    //-----------------------------------------------------------------------------

    public String toJson() {
        return "{ \"id\": \"" + id + "\", \"name\": \"" + Utils.convStr2Json(name) + "\"" +
               (count < 0 ? "" : ", \"count\": " + count) + " }";
    } // toJason()

    //-----------------------------------------------------------------------------

    public String toXml() {
        return "<item><id>" + id + "</id><name>" + Utils.convStr2Xml(name) + "</name>" +
               (count < 0 ? "" : "<count>" + count + "</count>") + "</item>\n";
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

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

}
