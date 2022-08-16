package com.infotech.locationbox.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import oracle.spatial.geometry.JGeometry;

import oracle.sql.STRUCT;

public class DataRota {
    private long pathId = 0;
    private double pathDistance = 0.00;
    private double pathDuration = 0.00;
    private double pathDurationWithTmcFlow = 0.00;
    private int opType = 0;
    private String mapUrl = null;
    private DataTspPoint[] tspPoints = null;
    private DataDirection[] directions = null;
    private DataTollRoad[] tollRoads = null;
    protected JGeometry[] objs = null;
    protected boolean encode = false;
    private long[] pathLinks = null;
    private DataIl[] ilList = null;
    private double[] coors;

    public DataRota() {
    }

    public DataRota(long pathId, double pathDistance, double pathDuration, double pathDurationWithTmcFlow, int opType,
                    String mapUrl, DataTspPoint[] tspPoints, DataDirection[] directions, DataTollRoad[] tollRoads, boolean withCoors,
                    boolean encode) {
        this.pathId = pathId;
        this.pathDistance = pathDistance;
        this.pathDuration = pathDuration;
        this.pathDurationWithTmcFlow = pathDurationWithTmcFlow;
        this.opType = opType;
        this.mapUrl = mapUrl;
        this.tspPoints = tspPoints;
        this.directions = directions;
        this.tollRoads = tollRoads;
        if (withCoors)
            objs = getGeometry(pathId);
        this.encode = encode;
    }
//---------------------------------------------------------

    private static JGeometry[] getGeometry(long pathId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT GEOLOC FROM NET_PATHS WHERE PATH_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setLong(1, pathId);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(1));
                JGeometry geo = JGeometry.load(obj);
                return geo.getElements();
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getGeometry()

    //----------------------------------------------------------------------------

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"pathid\": " + pathId + ", \"distance\": " + (int) (pathDistance + 0.5) + ", \"duration\": " + pathDuration + ", \"durationwithtmc\": " + pathDurationWithTmcFlow + ", \"optype\": " + opType + ", \"mapurl\": \"" + (mapUrl == null ? "" : mapUrl) + "\",\n");
        sb.append( "  \"tsppoints\": [\n");
        if (tspPoints != null) {
            for (int i = 0; i < tspPoints.length; i++) {
                if (i > 0)
                    sb.append(",\n");
               sb.append(tspPoints[i].toJson("    "));
            } // for()
           sb.append("\n");
        }
       sb.append("  ],\n");
       sb.append("  \"directions\": [\n");
        if (directions != null) {
            for (int i = 0; i < directions.length; i++) {
                if (i > 0)
                   sb.append(",\n");
               sb.append(directions[i].toJson("    "));
            } // for()
          sb.append("\n");
        }
        sb.append("  ],\n");
        sb.append("  \"tollroads\": [\n");
        if (tollRoads != null) {
          for (int i = 0; i < tollRoads.length; i++) {
              if (i > 0)
                 sb.append(",\n");
             sb.append(tollRoads[i].toJson("    "));
          } // for()
          sb.append("\n");
        }
        sb.append("  ]");
        if (objs != null) {
           sb.append(",\n");
           sb.append("  \"geometry\": [\n");
            for (int k = 0; k < objs.length; k++) {
                double[] oarray = objs[k].getOrdinatesArray();
                if (encode) {
                    String pline = PolylineUtils.encodePolyline(oarray);
                   sb.append("    \"" + pline + "\"");
                } else {
                    if (k > 0)
                       sb.append(",\n");
                    StringBuilder sbuf = new StringBuilder(oarray.length * 12);
                    for (int i = 0; i < oarray.length; i++) {
                        if (i > 0)
                            sbuf.append(',');
                        sbuf.append(oarray[i]);
                    } // for(i)
                    sb.append("    \"" + sbuf.toString() + "\"");
                    sbuf = null;
                }
            } // for(k)
          sb.append("\n");
          sb.append("  ]");
        } // if(objs)


        if (pathLinks != null) {
           sb.append(",  \"pathLinks\": [\n");

            for (int i = 0; i < pathLinks.length; i++) {
                if (i > 0)
                   sb.append(",\n");
              sb.append("            {\n" + "                \"pathLinkId\": " + pathLinks[i] + "\n" + "            }");
            } // for()
            sb.append("\n");
           sb.append("  ]\n");
        }


        sb.append("\n");
        sb.append("}\n");
        return sb.toString();
    } // toJson()

    //----------------------------------------------------------------------------

    public String toJsonHere() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"pathid\": " + pathId + ", \"distance\": " + (int) (pathDistance + 0.5) + ", \"duration\": " + pathDuration + ", \"durationwithtmc\": " + pathDurationWithTmcFlow + ", \"optype\": " + opType + ", \"mapurl\": \"" + (mapUrl == null ? "" : mapUrl) + "\",\n");
        sb.append("  \"tsppoints\": [\n");
        if (tspPoints != null) {
            for (int i = 0; i < tspPoints.length; i++) {
                if (i > 0)
                    sb.append(",\n");
                 sb.append(tspPoints[i].toJson("    "));
            } // for()
            sb.append("\n");
        }
        sb.append("  ],\n");
        sb.append("  \"directions\": [\n");
        if (directions != null) {
            for (int i = 0; i < directions.length; i++) {
                if (i > 0)
                     sb.append(",\n");
                sb.append(directions[i].toJson("    "));
            } // for()
            sb.append("\n");
        }
        sb.append("  ],\n");
        sb.append("  \"tollroads\": [\n");
        if (tollRoads != null) {
          for (int i = 0; i < tollRoads.length; i++) {
              if (i > 0)
                 sb.append(",\n");
              sb.append(tollRoads[i].toJson("    "));
          } // for()
         sb.append("\n");
        }
       sb.append("  ]");
        if (coors != null) {
            sb.append(",\n");
            sb.append("  \"geometry\": [\n");
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
           sb.append("\n");
           sb.append("  ]");
         } // if(objs)
        

        if (pathLinks != null) {
           sb.append(",  \"pathLinks\": [\n");

            for (int i = 0; i < pathLinks.length; i++) {
                if (i > 0)
                   sb.append(",\n");
               sb.append("            {\n" + "                \"pathLinkId\": " + pathLinks[i] + "\n" + "            }");
            } // for()
           sb.append("\n");
           sb.append("  ]\n");
        }


       sb.append("\n");
       sb.append("}\n");
        return sb.toString();
    } // toJson()

    //----------------------------------------------------------------------------

    public String toXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("  <rota>\n");
        sb.append("    <pathid>" + pathId + "</pathid>\n");
        sb.append("    <distance>" + (int) (pathDistance + 0.5) + "</distance>\n");
        sb.append("    <duration>" + pathDuration + "</duration>\n");
        sb.append("    <durationwithtmc>" + pathDurationWithTmcFlow + "</durationwithtmc>\n");
        sb.append("    <mapurl>" + Utils.convStr2Xml(mapUrl) + "</mapurl>\n");
        sb.append("    <tsppoints>\n");
        if (tspPoints != null) {
            for (int i = 0; i < tspPoints.length; i++)
                sb.append(tspPoints[i].toXml());
        }
        sb.append("    </tsppoints>\n");
        sb.append("    <directions>\n");
        if (directions != null) {
            for (int i = 0; i < directions.length; i++)
                sb.append(directions[i].toXml());
        }
       sb.append("    </directions>\n");
       sb.append("    <tollroads>\n");
        if (tollRoads != null) {
          for (int i = 0; i < tollRoads.length; i++)
             sb.append(tollRoads[i].toXml());
        }
        sb.append("    </tollroads>\n");
        if (objs != null) {
            sb.append("    <geometry>\n");
            for (int k = 0; k < objs.length; k++) {
                double[] oarray = objs[k].getOrdinatesArray();
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
            } // for(k)
            sb.append("    </geometry>\n");
        } // if(objs)


        if (pathLinks != null) {
            sb.append("    <pathLinks>\n");
            for (int i = 0; i < pathLinks.length; i++)
               sb.append("<pathLinkId>" + pathLinks[i] + "</pathLinkId>");
            sb.append("    </pathLinks>\n");
        }

        sb.append("  </rota>\n");
        return sb.toString();
    } // toXml()

    //----------------------------------------------------------------------------
    
    public String toXmlHere() {
        StringBuilder sb = new StringBuilder();
        sb.append("  <rota>\n");
        sb.append("    <pathid>" + pathId + "</pathid>\n");
        sb.append("    <distance>" + (int) (pathDistance + 0.5) + "</distance>\n");
        sb.append("    <duration>" + pathDuration + "</duration>\n");
        sb.append("    <durationwithtmc>" + pathDurationWithTmcFlow + "</durationwithtmc>\n");
        sb.append("    <mapurl>" + Utils.convStr2Xml(mapUrl) + "</mapurl>\n");
        sb.append("    <tsppoints>\n");
        if (tspPoints != null) {
            for (int i = 0; i < tspPoints.length; i++)
                sb.append(tspPoints[i].toXml());
        }
        sb.append("    </tsppoints>\n");
        sb.append("    <directions>\n");
        if (directions != null) {
            for (int i = 0; i < directions.length; i++)
                sb.append(directions[i].toXml());
        }
       sb.append("    </directions>\n");
       sb.append("    <tollroads>\n");
        if (tollRoads != null) {
          for (int i = 0; i < tollRoads.length; i++)
             sb.append(tollRoads[i].toXml());
        }
        sb.append("    </tollroads>\n");
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

       sb.append("  </rota>\n");
        return sb.toString();
    } // toXml()

    //----------------------------------------------------------------------------

    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    public long getPathId() {
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

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getOpType() {
        return opType;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setTspPoints(DataTspPoint[] tspPoints) {
        this.tspPoints = tspPoints;
    }

    public DataTspPoint[] getTspPoints() {
        return tspPoints;
    }

    public void setDirections(DataDirection[] directions) {
        this.directions = directions;
    }

    public DataDirection[] getDirections() {
        return directions;
    }

    public void setIlList(DataIl[] ilList) {
        this.ilList = ilList;
    }

    public DataIl[] getIlList() {
        return ilList;
    }

    public void setTollRoads(DataTollRoad[] tollRoads) {
      this.tollRoads = tollRoads;
    }

    public DataTollRoad[] getTollRoads() {
      return tollRoads;
    }

    public void setPathLinks(long[] pathLinks) {
        this.pathLinks = pathLinks;
    }

    public long[] getPathLinks() {
        return pathLinks;
    }


    public void setCoors(double[] coors) {
        this.coors = coors;
    }

    public double[] getCoors() {
        return coors;
    }

    public void setEncode(boolean encode) {
        this.encode = encode;
    }

    public boolean isEncode() {
        return encode;
    }
}
