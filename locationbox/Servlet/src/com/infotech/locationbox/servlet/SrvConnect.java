package com.infotech.locationbox.servlet;

import java.io.*;

import java.net.*;

import java.util.ArrayList;

public class SrvConnect extends Object {
    private InetAddress address = null;
    private BufferedInputStream inp = null;
    private BufferedOutputStream out = null;
    private Socket socket = null;
    private String hostName = null;
    private int portNumber = -1;

    private int nodeId = 0;
    private int pathId = 0;
    private double pathCost = 0.00;
    private double pathDistance = 0.00;
    private double pathDuration = 0.00;
    private double pathDurationWithTmcFlow = 0.00;
    private int opType = 0;

    private DataTspPoint[] tspPoints = null;
    private DataDirection[] directions = null;
    private DataTollRoad[] tollRoads = null;
    
    private String mapUrl = null;
    private boolean notfound = false;

    private boolean errorOccured = false;
    private boolean eofOccured = false;
    private boolean noDataLeft = false;

    //-----------------------------------------------------------------------------

    public SrvConnect(String hostName, int portNumber) {
        String str;

        this.hostName = hostName;
        this.portNumber = portNumber;
        try {
            address = InetAddress.getByName(hostName);
            socket = new Socket(address, portNumber);
            inp = new BufferedInputStream(socket.getInputStream());
            out = new BufferedOutputStream(socket.getOutputStream());
            socket.setSoTimeout(30000);
        } catch (Exception ex) {
            errorOccured = true;
            Utils.logInfo("Connect Error : Host: " + hostName + ", Port: " + portNumber);
        }
    }

    //-----------------------------------------------------------------------------

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            errorOccured = true;
            Utils.logInfo("Disconnect Error !");
        }
        return;
    } // close()

    //-----------------------------------------------------------------------------

    public void sendLine(String str) {
        byte[] outBuffer;

        Utils.logInfo(str);
        outBuffer = (str + "\r\n").getBytes();
        try {
            out.write(outBuffer, 0, outBuffer.length);
            out.flush();
        } catch (IOException e) {
            errorOccured = true;
            Utils.logInfo("Send Error !");
        }

        return;
    }

    //-----------------------------------------------------------------------------

    public String getLine() {
        String str;
        int ch;

        str = "";
        try {
            while (true) {
                ch = inp.read();
                if (ch == -1) {
                    eofOccured = true;
                    break; // End of file
                }

                if (ch == 0x0d)
                    continue;
                if (ch == 0x0a)
                    break;

                str += (char) ch;

            } // while()
        } catch (IOException e) {
            if (e.getMessage().indexOf("timed out") != -1) {
                Utils.logInfo("Get Timeout: " + e.getMessage());
                return (str);
            }

            errorOccured = true;
            Utils.logInfo("Get Error: " + e.getMessage());
        }

        return (str);
    }

    //-----------------------------------------------------------------------------

    public int getData(byte[] b) {
        int len = 0;

        try {
            len = inp.read(b, 0, b.length);
            if (len == -1)
                eofOccured = true;
        } catch (IOException e) {
            if (e.getMessage().indexOf("timed out") != -1) {
                Utils.logInfo("Timed Out: " + e.getMessage());
                return (0);
            }

            errorOccured = true;
            Utils.logInfo("Get Error: " + e.getMessage());
        }

        if (len <= 0)
            return (0);

        return (len);
    }

    //-----------------------------------------------------------------------------

    public boolean getLineWithErrorCheck() {
        ArrayList array = new ArrayList();

        notfound = false;
        pathId = 0;
        pathCost = 0.00;
        pathDistance = 0.00;
        pathDuration = 0.00;
        pathDurationWithTmcFlow = 0.00;
        opType = 0;
        tspPoints = null;
        directions = null;
        
        if (this.isErrorOccured())
            return false;

        String line = null;
        while (line == null || line.length() <= 0) {
            line = getLine();
            if (this.isErrorOccured() || this.isEofOccured())
                return false;

        } // while()

        Utils.logInfo("Received Line: " + line);

        if (line.startsWith("HELLO"))
            return true;

        if (line.startsWith("NODE ID :")) {
            nodeId = Integer.parseInt(line.substring(10));
            line = null;
            while (line == null || line.length() <= 0) {
                line = getLine();
                if (this.isErrorOccured() || this.isEofOccured())
                    return false;

            } // while()
        }

        if (line.startsWith("PATH ID :")) {
            String info[] = Utils.splitString(line, ",");
            for (int i = 0; i < info.length; i++) {
                String[] data = Utils.splitString(info[i], ":");
                String typ = data[0].trim();
                String val = data[1].trim();
                if (typ.equals("PATH ID"))
                    pathId = Integer.parseInt(val);
                else if (typ.equals("COST"))
                    pathCost = Double.parseDouble(val);
                else if (typ.equals("DISTANCE"))
                    pathDistance = Double.parseDouble(val);
                else if (typ.equals("DURATION"))
                    pathDuration = Double.parseDouble(val);
                else if (typ.equals("DURATIONWITHTMCFLOW"))
                    pathDurationWithTmcFlow = Double.parseDouble(val);
                else if (typ.equals("OPTYPE"))
                    opType = Integer.parseInt(val);
            } // for(i)
            int pos = line.indexOf("MAP URL : ");
            if (pos >= 0)
                mapUrl = line.substring(pos + 10);
            if (mapUrl.equalsIgnoreCase("null"))
                mapUrl = null;

            line = null;
            while (line == null || line.length() <= 0) {
                line = getLine();
                if (this.isErrorOccured() || this.isEofOccured())
                    return false;

            } // while()

            while (line.startsWith("TSPPOINT")) {
                info = Utils.splitString(line, " ");
                if (info.length == 5) {
                    String name = Utils.convToTurkish(info[1]);
                    double cost = Double.parseDouble(info[2]);
                    double distance = Double.parseDouble(info[3]);
                    double duration = Double.parseDouble(info[4]);
                    DataTspPoint dtp = new DataTspPoint(name, cost, distance, duration);
                    array.add(dtp);
                } // if()

                line = null;
                while (line == null || line.length() <= 0) {
                    line = getLine();
                    if (this.isErrorOccured() || this.isEofOccured())
                        return false;

                } // while()
            } // while()

            if (array.size() <= 0)
                tspPoints = null;
            else {
                tspPoints = new DataTspPoint[array.size()];
                for (int i = 0; i < array.size(); i++)
                    tspPoints[i] = (DataTspPoint) array.get(i);
            }

            array.clear();
            while (line.startsWith("DIRLINE")) {
                info = Utils.splitString(line, " ");
                if (info.length >= 7) {
                    int linkId = Integer.parseInt(info[2]);
                    double distance = Double.parseDouble(info[3]);
                    double duration = Double.parseDouble(info[4]);
                    double cost = Double.parseDouble(info[5]);
                    String description = Utils.convToTurkish(info[6]);
                    DataDirection dd = new DataDirection(linkId, description, distance, duration);
                    array.add(dd);
                } // if()

                line = null;
                while (line == null || line.length() <= 0) {
                    line = getLine();
                    if (this.isErrorOccured() || this.isEofOccured())
                        return false;

                } // while()
            } // while()

            if (array.size() <= 0)
                directions = null;
            else {
                directions = new DataDirection[array.size()];
                for (int i = 0; i < array.size(); i++)
                    directions[i] = (DataDirection) array.get(i);
            }

            array.clear();
            while (line.startsWith("POINT")) {
                // Skip point responses if occurs somehow
              

                line = null;
                while (line == null || line.length() <= 0) {
                    line = getLine();
                    if (this.isErrorOccured() || this.isEofOccured())
                        return false;

                } // while()
            } // while()
            
            array.clear();
            while( line.startsWith("TOLLROAD") ) {
              info = Utils.splitString(line, " ");
              if( info.length >= 5 ) {
                int linkId = Integer.parseInt(info[2]);
                double tollValue = Double.parseDouble(info[3]);
                String linkName = Utils.convToTurkish(info[4]);
                String tollRoads = null;
                if( tollRoads == null ) tollRoads = "";
                DataTollRoad dd = new DataTollRoad(linkId, linkName, tollValue);
                array.add(dd);
              } // if()

              line = null;
              while( line == null || line.length() <= 0 ) {
                line = getLine();
                  if (this.isErrorOccured() || this.isEofOccured())
                      return false;
                
              } // while()
              Utils.showText(line);
            } // while()
            
            if (array.size() <= 0)
                tollRoads = null;
            else {
                tollRoads = new DataTollRoad[array.size()];
                for (int i = 0; i < array.size(); i++)
                    tollRoads[i] = (DataTollRoad) array.get(i);
            }

        } // if()

        while (line.startsWith("NODELINE")) {
            // Ignore those lines

            line = null;
            while (line == null || line.length() <= 0) {
                line = getLine();
                if (this.isErrorOccured() || this.isEofOccured())
                    return false;

            } // while()
        } // while()

        if (line.equals("NOT FOUND")) {
            notfound = true;
            return true;
        }

        if (line.equals("OK"))
            return true;

        return false;
    } // getLineWithErrorCheck()

    //-----------------------------------------------------------------------------

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public double getPathCost() {
        return pathCost;
    }

    public void setPathCost(double pathCost) {
        this.pathCost = pathCost;
    }

    public double getPathDistance() {
        return pathDistance;
    }

    public void setPathDistance(double pathDistance) {
        this.pathDistance = pathDistance;
    }

    public double getPathDuration() {
        return pathDuration;
    }

    public void setPathDuration(double pathDuration) {
        this.pathDuration = pathDuration;
    }

    public double getPathDurationWithTmcFlow() {
        return pathDurationWithTmcFlow;
    }

    public void setPathDurationWithTmcFlow(double pathDurationWithTmcFlow) {
        this.pathDurationWithTmcFlow = pathDurationWithTmcFlow;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public DataTspPoint[] getTspPoints() {
        return tspPoints;
    }

    public void setTspPoints(DataTspPoint[] tspPoints) {
        this.tspPoints = tspPoints;
    }

    public DataDirection[] getDirections() {
        return directions;
    }

    public void setDirections(DataDirection[] directions) {
        this.directions = directions;
    }
    
    public void setTollRoads(DataTollRoad[] tollRoads) {
      this.tollRoads = tollRoads;
    }

    public DataTollRoad[] getTollRoads() {
      return tollRoads;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public boolean getNotfound() {
        return notfound;
    }

    public void setNotfound(boolean notfound) {
        this.notfound = notfound;
    }

    public boolean isErrorOccured() {
        return errorOccured;
    }

    public boolean isEofOccured() {
        return eofOccured;
    }

    public boolean isNoDataLeft() {
        return noDataLeft;
    }

}
