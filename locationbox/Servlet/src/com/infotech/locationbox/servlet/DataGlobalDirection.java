package com.infotech.locationbox.servlet;


public class DataGlobalDirection extends DataDirection {
    private String id ="";
    private String polyline = "";
    public DataGlobalDirection() {
        super();
    }
    public DataGlobalDirection(int linkId, String direction, double distance, double duration, String id, String polyline) {
        super(linkId,  direction,  distance,  duration);
        this.id = id;
        this.polyline = polyline;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        StringBuilder s = new StringBuilder();
        s.append(indent + "{\n");
        s.append(indent + "  \"linkId\": " + getLinkId() + ",\n");
        s.append(indent + "  \"id\":  \"" + id + "\",\n");
        s.append(indent + "  \"polyline\": \"" + polyline + "\",\n");
        s.append(indent + "  \"direction\": \"" + (getDirection() == null ? "" : getDirection()) + "\",\n");
        s.append(indent + "  \"distance\": " + (int) (getDistance() + 0.5) + ",\n");
        s.append(indent + "  \"duration\": " + (int) (getDuration() + 0.5) + "\n");
        s.append(indent + "}");
        return s.toString();
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        StringBuilder s = new StringBuilder();
        s.append("      <direction>\n");
        s.append("        <linkId>" + getLinkId() + "</linkId>\n");
        s.append("        <id>" + id + "</id>\n");
        s.append("        <polyline>" + polyline + "</polyline>\n");
        s.append("        <explanation>" + (getDirection() == null ? "" : Utils.convStr2Xml(getDirection())) + "</explanation>\n");
        s.append("        <distance>" + (int) (getDistance() + 0.5) + "</distance>\n");
        s.append("        <duration>" + (int) (getDistance() + 0.5) + "</duration>\n");
        s.append("      </direction>\n");
        return s.toString();
    } // toXml()


    public void setId(String id) {
        this.id = id;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }
}
