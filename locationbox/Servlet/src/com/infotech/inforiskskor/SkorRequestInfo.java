package com.infotech.inforiskskor;


public class SkorRequestInfo {
    private int teminatTuru;
    private double xCoor;
    private double yCoor;
    private int adresSeviye;
    private String key;
    private int[] beyanList = new int[21];


    public SkorRequestInfo(String key, int teminatTuru, double xCoor, double yCoor, int adresSeviye, int[] beyanList) {
        this.teminatTuru = teminatTuru;
        this.xCoor = xCoor;
        this.yCoor = yCoor;
        this.adresSeviye = adresSeviye;
        this.beyanList = beyanList;
        this.key = key;

    }

    public SkorRequestInfo() {
    }


    public void setTeminatTuru(int teminatTuru) {
        this.teminatTuru = teminatTuru;
    }

    public int getTeminatTuru() {
        return teminatTuru;
    }

    public void setXCoor(double xCoor) {
        this.xCoor = xCoor;
    }

    public double getXCoor() {
        return xCoor;
    }

    public void setYCoor(double yCoor) {
        this.yCoor = yCoor;
    }

    public double getYCoor() {
        return yCoor;
    }

    public void setAdresSeviye(int adresSeviye) {
        this.adresSeviye = adresSeviye;
    }

    public int getAdresSeviye() {
        return adresSeviye;
    }

    public void setBeyanValue(int beyanNo, int value) {
        this.beyanList[beyanNo] = value;
    }

    public int getBeyanValue(int beyanNo) {
        return beyanList[beyanNo];
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
