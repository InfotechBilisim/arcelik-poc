package com.infotech.locationbox.servlet;

public class DataAdresMahalle {
    public DataAdresMahalle() {
        super();
    }

    private long ilId;
    private long ilUavt;
    private String ilAdi;
    private long ilceId;
    private long ilceUavt;
    private String ilceAdi;
    private long mahalleId;
    private long mahalleUavt;
    private String mahalleAdi;
    private double longitude;
    private double latitude;
    private int zone=0;
    


    public void setIlId(long ilId) {
        this.ilId = ilId;
    }

    public long getIlId() {
        return ilId;
    }

    public void setIlUavt(long ilUavt) {
        this.ilUavt = ilUavt;
    }

    public long getIlUavt() {
        return ilUavt;
    }

    public void setIlAdi(String ilAdi) {
        this.ilAdi = ilAdi;
    }

    public String getIlAdi() {
        return ilAdi;
    }

    public void setIlceId(long ilceId) {
        this.ilceId = ilceId;
    }

    public long getIlceId() {
        return ilceId;
    }

    public void setIlceUavt(long ilceUavt) {
        this.ilceUavt = ilceUavt;
    }

    public long getIlceUavt() {
        return ilceUavt;
    }

    public void setIlceAdi(String ilceAdi) {
        this.ilceAdi = ilceAdi;
    }

    public String getIlceAdi() {
        return ilceAdi;
    }

    public void setMahalleId(long mahalleId) {
        this.mahalleId = mahalleId;
    }

    public long getMahalleId() {
        return mahalleId;
    }

    public void setMahalleUavt(long mahalleUavt) {
        this.mahalleUavt = mahalleUavt;
    }

    public long getMahalleUavt() {
        return mahalleUavt;
    }

    public void setMahalleAdi(String mahalleAdi) {
        this.mahalleAdi = mahalleAdi;
    }

    public String getMahalleAdi() {
        return mahalleAdi;
    }



    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public int getZone() {
        return zone;
    }
    
    
    //----------------------------------------------------------------------------

    public String toJson() {
        String s = "";
        s += "{\n";
        s += "  \"latitude\": " + latitude + ",\n";
        s += "  \"longitude\": " + longitude + ",\n";
        s += "  \"zone\": " + zone + ",\n";
        s += "  \"ilId\": " + ilId + ",\n";
        s += "  \"ilAdresKodu\": " + ilUavt+ ",\n";
        s += "  \"ilAdi\": \"" + Utils.convStrIfNull(ilAdi) + "\",\n";
        s += "  \"ilceId\": " + ilceId + ",\n";
        s += "  \"ilceAdresKodu\": " + ilceUavt + ",\n";
        s += "  \"ilceAdi\": \"" + Utils.convStrIfNull(ilceAdi) + "\",\n";
        s += "  \"mahalleId\": " + mahalleId + ",\n";
        s += "  \"mahalleAdresKodu\": " + mahalleUavt + ",\n";
        s += "  \"mahalleAdi\": \"" + Utils.convStrIfNull(mahalleAdi) + "\"\n";
        s += "  }";
        
        return s;
    } // toJson()

    //----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <latitude>" + latitude + "</latitude>\n";
        s += "    <longitude>" + longitude + "</longitude>\n";
        s += "    <zone>" + zone + "</zone>\n";
        s += "    <ilid>" + ilId + "</ilid>\n";
        s += "    <iladreskodu>" + ilUavt + "</iladreskodu>\n";
        s += "    <iladi>" + Utils.convStr2Xml(ilAdi) + "</iladi>\n";
        s += "    <ilceid>" + ilceId+ "</ilceid>\n";
        s += "    <ilceadreskodu>" +ilceUavt + "</ilceadreskodu>\n";
        s += "    <ilceadi>" + Utils.convStr2Xml(ilceAdi) + "</ilceadi>\n";
        s += "    <mahalleid>" + mahalleId + "</mahalleid>\n";
        s += "    <mahalleadreskodu>" + mahalleUavt + "</mahalleadreskodu>\n";
        s += "    <mahalleadi>" + Utils.convStr2Xml(mahalleAdi) + "</mahalleadi>\n";
        s = s.replaceAll(">null<", "><");
        return s;
    } // toXml()


}
