package com.infotech.locationbox.servlet;

import com.infotech.address.cleaner.DataAddress;
import java.text.SimpleDateFormat;

public class DataUavtAddress {

    private DataAddress da = null;

    public DataUavtAddress() {
    }

    public DataUavtAddress(DataAddress da) {
        this.da = da;
    }

    public String toXml() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        return s;
    }

    public String toJson(String indent) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = "";

        s += "    <adr>\n";
        s += "      <iladi>" + Utils.convStr2Xml(da.getAdrIlAdi()) + "</iladi>\n";
        s += "      <ilceadi>" + Utils.convStr2Xml(da.getAdrIlceAdi()) + "</ilceadi>\n";
        s += "      <mahalleadi>" + Utils.convStr2Xml(da.getAdrMahalleAdi()) + "</mahalleadi>\n";
        s += "      <koyadi>" + Utils.convStr2Xml(da.getAdrKoyAdi()) + "</koyadi>\n";
        s += "      <semtadi>" + Utils.convStr2Xml(da.getAdrSemtAdi()) + "</semtadi>\n";
        s += "      <caddeadi>" + Utils.convStr2Xml(da.getAdrCaddeAdi()) + "</caddeadi>\n";
        s += "      <sokakadi>" + Utils.convStr2Xml(da.getAdrSokakAdi()) + "</sokakadi>\n";
        s += "      <siteadi>" + Utils.convStr2Xml(da.getAdrSite()) + "</siteadi>\n";
        s += "      <kapino>" + Utils.convStr2Xml(da.getAdrKapiNo()) + "</kapino>\n";
        s += "      <kapiadi>" + Utils.convStr2Xml(da.getAdrKapiAdi()) + "</kapiadi>\n";
        s += "      <kurumadi>" + Utils.convStr2Xml(da.getAdrKurum()) + "</kurumadi>\n";
        s += "      <kat>" + Utils.convStr2Xml(da.getAdrKat()) + "</kat>\n";
        s += "      <daireno>" + Utils.convStr2Xml(da.getAdrDaireNo()) + "</daireno>\n";
        s += "      <postakodu>" + da.getAdrPostaKodu() + "</postakodu>\n";
        s += "    </adr>\n";

        s = s.replaceAll(">null<", "><");
        return s;
    }

    public void setDa(DataAddress da) {
        this.da = da;
    }

    public DataAddress getDa() {
        return da;
    }
}
