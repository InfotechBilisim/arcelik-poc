package com.infotech.locationbox.servlet;

import java.text.SimpleDateFormat;
public class DataMaxYolSpeed {
    private int carLimit = 0 ;
    private int minibusLimit = 0 ;
    private int busLimit = 0 ;
    private int truckLimit = 0 ;
    private int motorcycleLimit = 0 ;
    private int dangerousVehicleLimit = 0 ;
    private int tractorLimit = 0 ;

    public DataMaxYolSpeed() {
    }
    public void setCarLimit(int carLimit){
        this.carLimit = carLimit;
    }
    public void setMinibusLimit(int minibusLimit){
        this.minibusLimit = minibusLimit;
    }
    public void setBusLimit(int busLimit){
        this.busLimit = busLimit;
    }
    public void setTruckLimit(int truckLimit){
        this.truckLimit = truckLimit;
    }
    public void setMotorcycleLimit(int motorcycleLimit){
        this.motorcycleLimit = motorcycleLimit;
    }
    public void setDangerousVehicleLimit(int dangerousVehicleLimit){
        this.dangerousVehicleLimit = dangerousVehicleLimit;
    }
    public void setTractorLimit(int tractorLimit){
        this.tractorLimit = tractorLimit;
    }


    public String toXml() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        s += "    <car>" + carLimit + "</car>\n";
        s += "    <minibus>" + minibusLimit + "</minibus>\n";
        s += "    <bus>" + busLimit + "</bus>\n";
        s += "    <truck>" + truckLimit + "</truck>\n";
        s += "    <motorcycle>" + motorcycleLimit + "</motorcycle>\n";
        s += "    <dangerousvehicle>" + dangerousVehicleLimit + "</dangerousvehicle>\n";
        s += "    <tractor>" + tractorLimit + "</tractor>\n";
        
        return s;
    }

    public String toJson(String indent) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        s += "\"car\": \"" + carLimit + "\",\n";
        s += indent + indent + "\"minibus\": \"" + minibusLimit + "\",\n";
        s += indent + indent + "\"bus\": " + busLimit + ",\n";
        s += indent + indent + "\"truck\": " + truckLimit + ",\n";
        s += indent + indent + "\"motorcycle\": " + motorcycleLimit + ",\n";
        s += indent + indent + "\"dangerousVehicle\": " + dangerousVehicleLimit + ",\n";
        s += indent + indent + "\"tractor\": " + tractorLimit + "\n";
        return s;        
    }
}
