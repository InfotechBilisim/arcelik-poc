package com.infotech.inforiskskor;

public class SkorResultInfo implements java.io.Serializable {
    protected int cogParamLength;
    protected int beyanParamLength;
    protected int resultCode;
    protected double skorValue;
    protected double[] skorCog = new double[10];
    protected double[] skorBeyan = new double[21];

    public SkorResultInfo() {
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setCogParamLength(int cogParamLength) {
        this.cogParamLength = cogParamLength;
    }

    public int getCogParamLength() {
        return cogParamLength;
    }

    public void setBeyanParamLength(int beyanParamLength) {
        this.beyanParamLength = beyanParamLength;
    }

    public int getBeyanParamLength() {
        return beyanParamLength;
    }


    public void setSkorValue(double skorValue) {
        this.skorValue = skorValue;
    }

    public double getSkorValue() {
        return skorValue;
    }

    public void setSkorCog(double skorValue, int index) {
        this.skorCog[index] = skorValue;
    }

    public double getSkorCog(int index) {
        return skorCog[index];
    }

    public void setSkorBeyan(double skorValue, int index) {
        this.skorBeyan[index] = skorValue;
    }

    public double getSkorBeyan(int index) {
        return skorBeyan[index];
    }


}
