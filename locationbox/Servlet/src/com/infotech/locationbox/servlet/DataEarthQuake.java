package com.infotech.locationbox.servlet;

public class DataEarthQuake {
    private double avgDepth = 0.0;
    private double avgMagnitude = 0.0;
    private double minDepth = 0.0;
    private double maxDepth = 0.0;
    private double minMagnitude = 0.0;
    private double maxMagnitude = 0.0;
    private long earthquakeCount = 0;
    
    public DataEarthQuake() {
    }
    
    
    public void setAvgDepth(double avgDepth) {
        this.avgDepth = avgDepth;
    }

    public double getAvgDepth() {
        return avgDepth;
    }

    public void setAvgMagnitude(double avgMagnitude) {
        this.avgMagnitude = avgMagnitude;
    }

    public double getAvgMagnitude() {
        return avgMagnitude;
    }

    public void setMinDepth(double minDepth) {
        this.minDepth = minDepth;
    }

    public double getMinDepth() {
        return minDepth;
    }

    public void setMaxDepth(double maxDepth) {
        this.maxDepth = maxDepth;
    }

    public double getMaxDepth() {
        return maxDepth;
    }

    public void setMinMagnitude(double minMagnitude) {
        this.minMagnitude = minMagnitude;
    }

    public double getMinMagnitude() {
        return minMagnitude;
    }

    public void setMaxMagnitude(double maxMagnitude) {
        this.maxMagnitude = maxMagnitude;
    }

    public double getMaxMagnitude() {
        return maxMagnitude;
    }

    public void setEarthquakeCount(long earthquakeCount) {
        this.earthquakeCount = earthquakeCount;
    }

    public long getEarthquakeCount() {
        return earthquakeCount;
    }
        
    public String toXml() {
        String s = "";
        s += "    <count>" + earthquakeCount + "</count>\n";
        s += "    <avgmagnitude>" + avgMagnitude + "</avgmagnitude>\n";
        s += "    <minmagnitude>" + minMagnitude + "</minmagnitude>\n";
        s += "    <maxmagnitude>" + maxMagnitude + "</maxmagnitude>\n";
        s += "    <avgdepth>" + avgDepth + "</avgdepth>\n";
        s += "    <mindepth>" + minDepth + "</mindepth>\n";
        s += "    <maxdepth>" + maxDepth + "</maxdepth>\n";
        
        return s;
    }

    public String toJson(String indent) {
        String s = "";
        s += "\"count\": " + earthquakeCount + ",\n";
        s += indent + indent + "\"avgMagnitude\": " + avgMagnitude + ",\n";
        s += indent + indent + "\"minMagnitude\": " + minMagnitude + ",\n";
        s += indent + indent + "\"maxMagnitude\": " + maxMagnitude + ",\n";
        s += indent + indent + "\"avgDepth\": " + avgDepth + ",\n";
        s += indent + indent + "\"minDepth\": " + minDepth + ",\n";
        s += indent + indent + "\"maxDepth\": " + maxDepth + "\n";
        
        return s;        
    }
    
}
