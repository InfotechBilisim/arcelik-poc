package com.infotech.locationbox.servlet;

public class CampCategory {
    private String category = null;
    private String description = null;
    private int count = -1;

    public CampCategory() {
        super();
    }

    public CampCategory(String category, String description) {
        this.category = category;
        this.description = description;
    }

    public CampCategory(String category, String description, int count) {
        this.category = category;
        this.description = description;
        this.count = count;
    }

    //-----------------------------------------------------------------------------

    public String toJson() {
        return "{ \"category\": \"" + Utils.convStr2Json(category) + "\", \"description\": \"" + description + "\"" +
               (count < 0 ? "" : ", \"count\": " + count) + " }";
    } // toJason()

    //-----------------------------------------------------------------------------

    public String toXml() {
        return "<item><category>" + Utils.convStr2Xml(category) + "</category><description>" +
               Utils.convStr2Xml(description) + "</description>" + (count < 0 ? "" : "<count>" + count + "</count>") +
               "</item>\n";
    } // toXml()

    //-----------------------------------------------------------------------------

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
