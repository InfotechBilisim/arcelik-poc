package com.infotech.locationbox.servlet;

public class DataGlobalRoute {
    private String pathId = null;
    private double pathDistance = 0.00;
    private double pathDuration = 0.00;
    private double pathDurationWithTmcFlow = 0.00;
    private DataGlobalDirection[] directions = null;
    private String[] pathLinks = null;
    protected boolean encode = false;
    private double[] coors;
   
    
    public String toJsonHere() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"pathid\": \"" + (pathId == null ? "" : pathId) + "\",\"distance\": " + (int) (pathDistance + 0.5) + ", \"duration\": " + pathDuration + ", \"durationwithtmc\": " + pathDurationWithTmcFlow +",\n");
        sb.append("  \"directions\": [\n");
        if (directions != null) {
            for (int i = 0; i < directions.length; i++) {
                if (i > 0)
                     sb.append(",\n");
                sb.append(directions[i].toJson("    "));
            } 
            sb.append("\n");
        }
        sb.append("  ]\n");
        if (coors != null) {
            sb.append(", \"geometry\": [\n");
                double[] oarray = coors;
                if (encode) {
                    String pline = PolylineUtils.encodePolyline(oarray);
                    sb.append("    \"" + pline + "\"");
                } else {
                    StringBuilder sbuf = new StringBuilder(oarray.length * 12);
                    for (int i = 0; i < oarray.length; i++) {
                        if (i > 0)
                            sbuf.append(',');
                        sbuf.append(oarray[i]);
                    } // for(i)
                   sb.append("    \"" + sbuf.toString() + "\"");
                   sbuf = null;
                }
           sb.append("  \n]");
         } 
        
        if (pathLinks != null) {
           sb.append(",  \"pathLinks\": [\n");

            for (int i = 0; i < pathLinks.length; i++) {
                if (i > 0)
                   sb.append(",\n");
               sb.append("            {\n" + "                \"pathLinkId\": " + pathLinks[i] + "\n" + "            }");
            } 
           sb.append("\n");
           sb.append("  ]\n");
        }


       sb.append("\n");
       sb.append("}\n");
        return sb.toString();
    } 
    
    public String toXmlHere() {
        StringBuilder sb = new StringBuilder();
        sb.append("  <route>\n");
        sb.append("    <pathid>" + pathId + "</pathid>\n");
        sb.append("    <distance>" + (int) (pathDistance + 0.5) + "</distance>\n");
        sb.append("    <duration>" + pathDuration + "</duration>\n");
        sb.append("    <durationwithtmc>" + pathDurationWithTmcFlow + "</durationwithtmc>\n");
        sb.append("    <directions>\n");
        if (directions != null) {
            for (int i = 0; i < directions.length; i++)
                sb.append(directions[i].toXml());
        }
       sb.append("    </directions>\n");
        if (coors != null) {
            sb.append("    <geometry>\n");
                double[] oarray = coors;
                if (encode) {
                    String pline = PolylineUtils.encodePolyline(oarray);
                    sb.append("      <ordinates>" + pline + "</ordinates>\n");
                } else {
                    StringBuilder sbuf = new StringBuilder(oarray.length * 12);
                    for (int i = 0; i < oarray.length; i++) {
                        if (i > 0)
                            sbuf.append(',');
                        sbuf.append(oarray[i]);
                    } // for(i)
                    sb.append("      <ordinates>" + sbuf.toString() + "</ordinates>\n");
                    sbuf = null;
                }
            sb.append("    </geometry>\n");
        } // if(objs)
        if (pathLinks != null) {
            sb.append("    <pathLinks>\n");
            for (int i = 0; i < pathLinks.length; i++)
               sb.append("<pathLinkId>" + pathLinks[i] + "</pathLinkId>");
            sb.append("    </pathLinks>\n");
        }

       sb.append("  </route>\n");
        return sb.toString();
    }


    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathDistance(double pathDistance) {
        this.pathDistance = pathDistance;
    }

    public double getPathDistance() {
        return pathDistance;
    }

    public void setPathDuration(double pathDuration) {
        this.pathDuration = pathDuration;
    }

    public double getPathDuration() {
        return pathDuration;
    }

    public void setPathDurationWithTmcFlow(double pathDurationWithTmcFlow) {
        this.pathDurationWithTmcFlow = pathDurationWithTmcFlow;
    }

    public double getPathDurationWithTmcFlow() {
        return pathDurationWithTmcFlow;
    }

    public void setDirections(DataGlobalDirection[] directions) {
        this.directions = directions;
    }

    public DataGlobalDirection[] getDirections() {
        return directions;
    }

    public void setPathLinks(String[] pathLinks) {
        this.pathLinks = pathLinks;
    }

    public String[] getPathLinks() {
        return pathLinks;
    }

    public void setEncode(boolean encode) {
        this.encode = encode;
    }

    public boolean isEncode() {
        return encode;
    }

    public void setCoors(double[] coors) {
        this.coors = coors;
    }

    public double[] getCoors() {
        return coors;
    }
}
