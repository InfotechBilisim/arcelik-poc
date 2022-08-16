package com.infotech.locationbox.servlet;

import com.infotech.address.cleaner.DataAddress;
import com.infotech.address.cleaner.DataParam;

import java.util.ArrayList;

public class DataGeocode {
    private DataAddress da = null;

    private String address = null;
    
    private double latitude = 0.00;
    private double longitude = 0.00;
    
    private String what3words = null;
    private int zone = 0;

    private String resAddress = null;
    private String adrResAddress = null;
    private int geolocLevel = 0;
    private int verifyLevel = 0;
    private int resolveLevel = 0;
    private int suggestion = 0;
    private String frc = null;

    public void setSuggestion(int suggestion) {
      this.suggestion = suggestion;
    }
  
    public int getSuggestion() {
      return suggestion;
    }

    private ArrayList<DataGeocode> dgSuggests = new ArrayList<>();

    public DataGeocode() {
      
    }


    public DataGeocode(DataAddress da) {
        this.da = da;
    }

    public DataGeocode(String address) {
        this.address = address;
    }

    public DataGeocode(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


  //----------------------------------------------------------------------------

    public static DataGeocode getInstance(DataAddress da, String address, int suggestion) {
        DataGeocode dg = new DataGeocode(address);
        String tmp = "";
        dg.da = da;
        dg.geolocLevel = da.getGeolocSeviyesi();
        dg.verifyLevel = da.getDogrulamaSeviyesi();
        dg.resolveLevel = da.getCozumlemeSeviyesi();
        
        dg.resAddress = da.getAdrAdres();
        dg.adrResAddress = da.getAdrResAdres();
        
        dg.latitude = Utils.truncCoords(da.getYCoor());
        dg.longitude = Utils.truncCoords(da.getXCoor());
        dg.zone = da.getZone();
        
        dg.suggestion = suggestion;

        da.setMahalleAdi((da.getMahalleAdi() +
                          (da.getDpMahalle() == null ? "" : " " + da.getDpMahalle().getExp().trim())).trim());
        da.setMahalleAdi(Utils.getTruncatedString(da.getMahalleAdi(), 64));

        da.setKoyAdi((da.getKoyAdi() + (da.getDpKoy() == null ? "" : " " + da.getDpKoy().getExp().trim())).trim());
        da.setKoyAdi(Utils.getTruncatedString(da.getKoyAdi(), 64));

        da.setSemtAdi((da.getSemtAdi() + (da.getDpSemt() == null ? "" : " " + da.getDpSemt().getExp().trim())).trim());
        da.setSemtAdi(Utils.getTruncatedString(da.getSemtAdi(), 64));

        da.setCaddeAdi((da.getCaddeAdi() +
                        (da.getDpCadde() == null ? "" : " " + da.getDpCadde().getExp().trim())).trim());
        da.setCaddeAdi(Utils.getTruncatedString(da.getCaddeAdi(), 64));

        da.setSokakAdi((da.getSokakAdi() +
                        (da.getDpSokak() == null ? "" : " " + da.getDpSokak().getExp().trim())).trim());
        da.setSokakAdi(Utils.getTruncatedString(da.getSokakAdi(), 64));

        tmp = da.getSite();
        if (da.getDpSite() != null && tmp.length() > 0 && tmp.indexOf(da.getDpSite().getExp().trim()) < 0)
            tmp += " " + da.getDpSite().getExp();
        tmp = tmp.trim();
        da.setSite(Utils.getTruncatedString(tmp, 64));

        tmp = da.getKurum();
        if (da.getKurum() != null && tmp.length() > 0 && tmp.indexOf(da.getDpKurum().getExp().trim()) < 0)
            tmp += " " + da.getDpKurum().getExp();
        tmp = tmp.trim();
        da.setKurum(Utils.getTruncatedString(tmp, 64));

        tmp = da.getKapiAdi();
        if (da.getKapiAdi() != null && tmp.length() > 0 && tmp.indexOf(da.getDpKapi().getExp().trim()) < 0)
            tmp += " " + da.getDpKapi().getExp();
        tmp = tmp.trim();
        da.setKapiAdi(Utils.getTruncatedString(tmp, 64));

        return dg;
    }  // getInstance()

    //----------------------------------------------------------------------------

    private static String getDataString(String name, DataParam dp, String suffix) {
        if (name == null || name.length() <= 0)
            return "";

        String res = null;
        if (dp == null)
            res = name.trim() + " " + suffix;
        else
            res = name + " " + dp.getExp().trim();
        return " " + res.trim();
    } // getDataString()

    //----------------------------------------------------------------------------

    public String toJson() {
        String s = "";
        s += "{\n";
        s += "  \"address\": \"" + (address == null ? "" : address) + "\",\n";
        s += "  \"latitude\": " + latitude + ",\n";
        s += "  \"longitude\": " + longitude + ",\n";
        s += "  \"zone\": " + zone + ",\n";
        s += "  \"resaddress\": \"" + (resAddress == null ? "" : resAddress) + "\",\n";
        s += "  \"adrresaddress\": \"" + (adrResAddress == null ? "" : adrResAddress) + "\",\n";
        s += "  \"reslevel\": " + resolveLevel + ",\n";
        s += "  \"vfylevel\": " + verifyLevel + ",\n";
        s += "  \"geolevel\": " + geolocLevel + ",\n";
        s += "  \"extra\": \"" + (da.getAciklama() == null ? "" : da.getAciklama()) + "\",\n";
        s += "  \"ilId\": " + da.getIlId() + ",\n";
        s += "  \"ilAdresKodu\": " + da.getIlUavt() + ",\n";
        s += "  \"ilAdi\": \"" + Utils.convStrIfNull(da.getIlAdi()) + "\",\n";
        s += "  \"ilceId\": " + da.getIlceId() + ",\n";
        s += "  \"ilceAdresKodu\": " + da.getIlceUavt() + ",\n";
        s += "  \"ilceAdi\": \"" + Utils.convStrIfNull(da.getIlceAdi()) + "\",\n";
        s += "  \"mahalleId\": " + da.getMahalleId() + ",\n";
        s += "  \"mahalleAdresKodu\": " + da.getMahalleUavt() + ",\n";
        s += "  \"mahalleAdi\": \"" + Utils.convStrIfNull(da.getMahalleAdi()) + "\",\n";
        s += "  \"koyId\": " + da.getKoyId() + ",\n";
        s += "  \"koyAdresKodu\": " + da.getKoyUavt() + ",\n";
        s += "  \"koyAdi\": \"" + Utils.convStrIfNull(da.getKoyAdi()) + "\",\n";
        s += "  \"semtId\": " + da.getSemtId() + ",\n";
        s += "  \"semtAdi\": \"" + Utils.convStrIfNull(da.getSemtAdi()) + "\",\n";
        s += "  \"caddeId\": " + da.getCaddeId() + ",\n";
        s += "  \"caddeAdresKodu\": " + da.getCaddeUavt() + ",\n";
        s += "  \"caddeAdi\": \"" + Utils.convStrIfNull(da.getCaddeAdi()) + "\",\n";
        s += "  \"sokakId\": " + da.getSokakId() + ",\n";
        s += "  \"sokakAdresKodu\": " + da.getSokakUavt() + ",\n";
        s += "  \"sokakAdi\": \"" + Utils.convStrIfNull(da.getSokakAdi()) + "\",\n";
        s += "  \"yolturu\": \"" + Utils.convStrIfNull(da.getYolTuru()) + "\",\n";
        s += "  \"frc\": \"" + (frc == null ? "" : frc) + "\",\n";
        s += "  \"siteId\": " + da.getSiteId() + ",\n";
        s += "  \"siteAdi\": \"" + Utils.convStrIfNull(da.getSite()) + "\",\n";
        s += "  \"kapiId\": " + da.getKapiId() + ",\n";
        s += "  \"kapiAdresKodu\": " + da.getKapiUavt() + ",\n";
        s += "  \"kapiNo\": \"" + da.getKapiNo() + "\",\n";
        s += "  \"kapiAdi\": \"" + Utils.convStrIfNull(da.getKapiAdi()) + "\",\n";
        s += "  \"kurumId\": " + da.getKurumId() + ",\n";
        s += "  \"kurumAdi\": \"" + Utils.convStrIfNull(da.getKurum()) + "\",\n";
        s += "  \"kat\": \"" + da.getKat() + "\",\n";
        s += "  \"daireAdresKodu\": " + da.getDaireUavt() + ",\n";
        s += "  \"daireNo\": \"" + da.getDaireNo() + "\",\n";
        s += "  \"postaKodu\": \"" + da.getPostaKodu() + "\",\n";
        s += "  \"subtype\": " + da.getSubtype() + ",\n";
        s += "  \"what3words\": \"" + (what3words == null ? "" : what3words) + "\",\n";
        s += "  \"adr\": { \n";
        s += "    \"ilAdi\": \"" + Utils.convStrIfNull(da.getAdrIlAdi()) + "\",\n";
        s += "    \"ilceAdi\": \"" + Utils.convStrIfNull(da.getAdrIlceAdi()) + "\",\n";
        s += "    \"mahalleAdi\": \"" + Utils.convStrIfNull(da.getAdrMahalleAdi()) + "\",\n";
        s += "    \"koyAdi\": \"" + Utils.convStrIfNull(da.getAdrKoyAdi()) + "\",\n";
        s += "    \"semtAdi\": \"" + Utils.convStrIfNull(da.getAdrSemtAdi()) + "\",\n";
        s += "    \"caddeAdi\": \"" + Utils.convStrIfNull(da.getAdrCaddeAdi()) + "\",\n";
        s += "    \"sokakAdi\": \"" + Utils.convStrIfNull(da.getAdrSokakAdi()) + "\",\n";
        s += "    \"siteAdi\": \"" + Utils.convStrIfNull(da.getAdrSite()) + "\",\n";
        s += "    \"kapiNo\": \"" + Utils.convStrIfNull(da.getAdrKapiNo()) + "\",\n";
        s += "    \"kapiAdi\": \"" + Utils.convStrIfNull(da.getAdrKapiAdi()) + "\",\n";
        s += "    \"kurumAdi\": \"" + Utils.convStrIfNull(da.getAdrKurum()) + "\",\n";
        s += "    \"kat\": \"" + da.getAdrKat() + "\",\n";
        s += "    \"daireNo\": \"" + da.getAdrDaireNo() + "\",\n";
        s += "    \"postaKodu\": \"" + da.getAdrPostaKodu() + "\"\n";
        s += "  },\n";
        
        s += "   \"suggestions\": [\n";
        if(suggestion == 1) {
          for(int i = 0; i < dgSuggests.size(); i++) {
            s += " {\n";
            s += "  \"address\": \"" + (dgSuggests.get(i).address == null ? "" : dgSuggests.get(i).address) + "\",\n";
            s += "  \"latitude\": " + dgSuggests.get(i).latitude + ",\n";
            s += "  \"longitude\": " + dgSuggests.get(i).longitude + ",\n";
            s += "  \"zone\": " + dgSuggests.get(i).zone + ",\n";
            s += "  \"resaddress\": \"" + (dgSuggests.get(i).resAddress == null ? "" : dgSuggests.get(i).resAddress) + "\",\n";
            s += "  \"adrresaddress\": \"" + (dgSuggests.get(i).adrResAddress == null ? "" : dgSuggests.get(i).adrResAddress) + "\",\n";
            s += "  \"reslevel\": " + dgSuggests.get(i).resolveLevel + ",\n";
            s += "  \"vfylevel\": " + dgSuggests.get(i).verifyLevel + ",\n";
            s += "  \"geolevel\": " + dgSuggests.get(i).geolocLevel + ",\n";
            s += "  \"extra\": \"" + (da.getAddressSuggestList().get(i).getAciklama() == null ? "" : da.getAddressSuggestList().get(i).getAciklama()) + "\",\n";
            s += "  \"ilId\": " + da.getAddressSuggestList().get(i).getIlId() + ",\n";
            s += "  \"ilAdresKodu\": " + da.getAddressSuggestList().get(i).getIlUavt() + ",\n";
            s += "  \"ilAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getIlAdi()) + "\",\n";
            s += "  \"ilceId\": " + da.getAddressSuggestList().get(i).getIlceId() + ",\n";
            s += "  \"ilceAdresKodu\": " + da.getAddressSuggestList().get(i).getIlceUavt() + ",\n";
            s += "  \"ilceAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getIlceAdi()) + "\",\n";
            s += "  \"mahalleId\": " + da.getAddressSuggestList().get(i).getMahalleId() + ",\n";
            s += "  \"mahalleAdresKodu\": " + da.getAddressSuggestList().get(i).getMahalleUavt() + ",\n";
            s += "  \"mahalleAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getMahalleAdi()) + "\",\n";
            s += "  \"koyId\": " + da.getAddressSuggestList().get(i).getKoyId() + ",\n";
            s += "  \"koyAdresKodu\": " + da.getAddressSuggestList().get(i).getKoyUavt() + ",\n";
            s += "  \"koyAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getKoyAdi()) + "\",\n";
            s += "  \"semtId\": " + da.getAddressSuggestList().get(i).getSemtId() + ",\n";
            s += "  \"semtAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getSemtAdi()) + "\",\n";
            s += "  \"caddeId\": " + da.getAddressSuggestList().get(i).getCaddeId() + ",\n";
            s += "  \"caddeAdresKodu\": " + da.getAddressSuggestList().get(i).getCaddeUavt() + ",\n";
            s += "  \"caddeAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getCaddeAdi()) + "\",\n";
            s += "  \"sokakId\": " + da.getAddressSuggestList().get(i).getSokakId() + ",\n";
            s += "  \"sokakAdresKodu\": " + da.getAddressSuggestList().get(i).getSokakUavt() + ",\n";
            s += "  \"sokakAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getSokakAdi()) + "\",\n";
            s += "  \"siteId\": " + da.getAddressSuggestList().get(i).getSiteId() + ",\n";
            s += "  \"siteAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getSite()) + "\",\n";
            s += "  \"kapiId\": " + da.getAddressSuggestList().get(i).getKapiId() + ",\n";
            s += "  \"kapiAdresKodu\": " + da.getAddressSuggestList().get(i).getKapiUavt() + ",\n";
            s += "  \"kapiNo\": \"" + da.getAddressSuggestList().get(i).getKapiNo() + "\",\n";
            s += "  \"kapiAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getKapiAdi()) + "\",\n";
            s += "  \"kurumId\": " + da.getAddressSuggestList().get(i).getKurumId() + ",\n";
            s += "  \"kurumAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getKurum()) + "\",\n";
            s += "  \"kat\": \"" + da.getAddressSuggestList().get(i).getKat() + "\",\n";
            s += "  \"daireAdresKodu\": " + da.getAddressSuggestList().get(i).getDaireUavt() + ",\n";
            s += "  \"daireNo\": \"" + da.getAddressSuggestList().get(i).getDaireNo() + "\",\n";
            s += "  \"postaKodu\": \"" + da.getAddressSuggestList().get(i).getPostaKodu() + "\",\n";
            s += "  \"subtype\": " + da.getAddressSuggestList().get(i).getSubtype() + ",\n";
            //s += "  \"what3words\": \"" + (what3words == null ? "" : what3words) + "\",\n";
            s += "  \"adr\": { \n";
            s += "    \"ilAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrIlAdi()) + "\",\n";
            s += "    \"ilceAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrIlceAdi()) + "\",\n";
            s += "    \"mahalleAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrMahalleAdi()) + "\",\n";
            s += "    \"koyAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrKoyAdi()) + "\",\n";
            s += "    \"semtAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrSemtAdi()) + "\",\n";
            s += "    \"caddeAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrCaddeAdi()) + "\",\n";
            s += "    \"sokakAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrSokakAdi()) + "\",\n";
            s += "    \"siteAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrSite()) + "\",\n";
            s += "    \"kapiNo\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrKapiNo()) + "\",\n";
            s += "    \"kapiAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrKapiAdi()) + "\",\n";
            s += "    \"kurumAdi\": \"" + Utils.convStrIfNull(da.getAddressSuggestList().get(i).getAdrKurum()) + "\",\n";
            s += "    \"kat\": \"" + da.getAddressSuggestList().get(i).getAdrKat() + "\",\n";
            s += "    \"daireNo\": \"" + da.getAddressSuggestList().get(i).getAdrDaireNo() + "\",\n";
            s += "    \"postaKodu\": \"" + da.getAddressSuggestList().get(i).getAdrPostaKodu() + "\"\n";
            s += "   }\n";
            s += " }\n";
            if((i + 1) != dgSuggests.size()) s += ",";
          }
        
        }
        
        s += "]}\n";
        return s;
    } // toJson()

    //----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <address>" + (address == null ? "" : address) + "</address>\n";
        s += "    <latitude>" + latitude + "</latitude>\n";
        s += "    <longitude>" + longitude + "</longitude>\n";
        s += "    <zone>" + zone + "</zone>\n";
        s += "    <resaddress>" + (resAddress == null ? "" : resAddress) + "</resaddress>\n";
        s += "    <adrresaddress>" + (adrResAddress == null ? "" : adrResAddress) + "</adrresaddress>\n";
        s += "    <reslevel>" + resolveLevel + "</reslevel>\n";
        s += "    <vfylevel>" + verifyLevel + "</vfylevel>\n";
        s += "    <geolevel>" + geolocLevel + "</geolevel>\n";
        s += "    <extra>" + Utils.convStr2Xml(da.getAciklama()) + "</extra>\n";
        s += "    <ilid>" + da.getIlId() + "</ilid>\n";
        s += "    <iladreskodu>" + da.getIlUavt() + "</iladreskodu>\n";
        s += "    <iladi>" + Utils.convStr2Xml(da.getIlAdi()) + "</iladi>\n";
        s += "    <ilceid>" + da.getIlceId() + "</ilceid>\n";
        s += "    <ilceadreskodu>" + da.getIlceUavt() + "</ilceadreskodu>\n";
        s += "    <ilceadi>" + Utils.convStr2Xml(da.getIlceAdi()) + "</ilceadi>\n";
        s += "    <mahalleid>" + da.getMahalleId() + "</mahalleid>\n";
        s += "    <mahalleadreskodu>" + da.getMahalleUavt() + "</mahalleadreskodu>\n";
        s += "    <mahalleadi>" + Utils.convStr2Xml(da.getMahalleAdi()) + "</mahalleadi>\n";
        s += "    <koyid>" + da.getKoyId() + "</koyid>\n";
        s += "    <koyadreskodu>" + da.getKoyUavt() + "</koyadreskodu>\n";
        s += "    <koyadi>" + Utils.convStr2Xml(da.getKoyAdi()) + "</koyadi>\n";
        s += "    <semtid>" + da.getSemtId() + "</semtid>\n";
        s += "    <semtadi>" + Utils.convStr2Xml(da.getSemtAdi()) + "</semtadi>\n";
        s += "    <caddeid>" + da.getCaddeId() + "</caddeid>\n";
        s += "    <caddeadreskodu>" + da.getCaddeUavt() + "</caddeadreskodu>\n";
        s += "    <caddeadi>" + Utils.convStr2Xml(da.getCaddeAdi()) + "</caddeadi>\n";
        s += "    <sokakid>" + da.getSokakId() + "</sokakid>\n";
        s += "    <sokakadreskodu>" + da.getSokakUavt() + "</sokakadreskodu>\n";
        s += "    <sokakadi>" + Utils.convStr2Xml(da.getSokakAdi()) + "</sokakadi>\n";
        s += "    <yolturu>" + Utils.convStr2Xml(da.getYolTuru()) + "</yolturu>\n"; 
        s += "    <frc>" + (frc == null ? "" : frc) + "</frc>\n";
        s += "    <siteid>" + da.getSiteId() + "</siteid>\n";
        s += "    <siteadi>" + Utils.convStr2Xml(da.getSite()) + "</siteadi>\n";
        s += "    <kapiid>" + da.getKapiId() + "</kapiid>\n";
        s += "    <kapino>" + Utils.convStr2Xml(da.getKapiNo()) + "</kapino>\n";
        s += "    <kapiadreskodu>" + da.getKapiUavt() + "</kapiadreskodu>\n";
        s += "    <kapiadi>" + Utils.convStr2Xml(da.getKapiAdi()) + "</kapiadi>\n";
        s += "    <kurumid>" + da.getKurumId() + "</kurumid>\n";
        s += "    <kurumadi>" + Utils.convStr2Xml(da.getKurum()) + "</kurumadi>\n";
        s += "    <kat>" + Utils.convStr2Xml(da.getKat()) + "</kat>\n";
        s += "    <daireadreskodu>" + da.getDaireUavt() + "</daireadreskodu>\n";
        s += "    <daireno>" + Utils.convStr2Xml(da.getDaireNo()) + "</daireno>\n";
        s += "    <postakodu>" + da.getPostaKodu() + "</postakodu>\n";
        s += "    <subtype>" + da.getSubtype() + "</subtype>\n";
        s += "    <what3words>" + (what3words == null ? "" : what3words) + "</what3words>\n";
        //s += "    <dataVersion>" + dataVersion + "</dataVersion>\n";
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
        s += "    <suggestions>\n"; 
      if(suggestion == 1) {
          for(int i=0; i < dgSuggests.size(); i++) {
            s += "    <suggestion>\n"; 
            s += "    <address>" + (dgSuggests.get(i).address == null ? "" : dgSuggests.get(i).address) + "</address>\n";
            s += "    <latitude>" + dgSuggests.get(i).latitude + "</latitude>\n";
            s += "    <longitude>" + dgSuggests.get(i).longitude + "</longitude>\n";
            s += "    <zone>" + dgSuggests.get(i).zone + "</zone>\n";
            s += "    <resaddress>" + (dgSuggests.get(i).resAddress == null ? "" : dgSuggests.get(i).resAddress) + "</resaddress>\n";
            s += "    <adrresaddress>" + (dgSuggests.get(i).adrResAddress == null ? "" : dgSuggests.get(i).adrResAddress) + "</adrresaddress>\n";
            s += "    <reslevel>" + dgSuggests.get(i).resolveLevel + "</reslevel>\n";
            s += "    <vfylevel>" + dgSuggests.get(i).verifyLevel + "</vfylevel>\n";
            s += "    <geolevel>" + dgSuggests.get(i).geolocLevel + "</geolevel>\n";
            s += "    <extra>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAciklama()) + "</extra>\n";
            s += "    <ilid>" + da.getAddressSuggestList().get(i).getIlId() + "</ilid>\n";
            s += "    <iladreskodu>" + da.getAddressSuggestList().get(i).getIlUavt() + "</iladreskodu>\n";
            s += "    <iladi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getIlAdi()) + "</iladi>\n";
            s += "    <ilceid>" + da.getAddressSuggestList().get(i).getIlceId() + "</ilceid>\n";
            s += "    <ilceadreskodu>" + da.getAddressSuggestList().get(i).getIlceUavt() + "</ilceadreskodu>\n";
            s += "    <ilceadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getIlceAdi()) + "</ilceadi>\n";
            s += "    <mahalleid>" + da.getAddressSuggestList().get(i).getMahalleId() + "</mahalleid>\n";
            s += "    <mahalleadreskodu>" + da.getAddressSuggestList().get(i).getMahalleUavt() + "</mahalleadreskodu>\n";
            s += "    <mahalleadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getMahalleAdi()) + "</mahalleadi>\n";
            s += "    <koyid>" + da.getAddressSuggestList().get(i).getKoyId() + "</koyid>\n";
            s += "    <koyadreskodu>" + da.getAddressSuggestList().get(i).getKoyUavt() + "</koyadreskodu>\n";
            s += "    <koyadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getKoyAdi()) + "</koyadi>\n";
            s += "    <semtid>" + da.getAddressSuggestList().get(i).getSemtId() + "</semtid>\n";
            s += "    <semtadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getSemtAdi()) + "</semtadi>\n";
            s += "    <caddeid>" + da.getAddressSuggestList().get(i).getCaddeId() + "</caddeid>\n";
            s += "    <caddeadreskodu>" + da.getAddressSuggestList().get(i).getCaddeUavt() + "</caddeadreskodu>\n";
            s += "    <caddeadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getCaddeAdi()) + "</caddeadi>\n";
            s += "    <sokakid>" + da.getAddressSuggestList().get(i).getSokakId() + "</sokakid>\n";
            s += "    <sokakadreskodu>" + da.getAddressSuggestList().get(i).getSokakUavt() + "</sokakadreskodu>\n";
            s += "    <sokakadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getSokakAdi()) + "</sokakadi>\n";
            s += "    <siteid>" + da.getAddressSuggestList().get(i).getSiteId() + "</siteid>\n";
            s += "    <siteadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getSite()) + "</siteadi>\n";
            s += "    <kapiid>" + da.getAddressSuggestList().get(i).getKapiId() + "</kapiid>\n";
            s += "    <kapino>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getKapiNo()) + "</kapino>\n";
            s += "    <kapiadreskodu>" + da.getAddressSuggestList().get(i).getKapiUavt() + "</kapiadreskodu>\n";
            s += "    <kapiadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getKapiAdi()) + "</kapiadi>\n";
            s += "    <kurumid>" + da.getAddressSuggestList().get(i).getKurumId() + "</kurumid>\n";
            s += "    <kurumadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getKurum()) + "</kurumadi>\n";
            s += "    <kat>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getKat()) + "</kat>\n";
            s += "    <daireadreskodu>" + da.getAddressSuggestList().get(i).getDaireUavt() + "</daireadreskodu>\n";
            s += "    <daireno>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getDaireNo()) + "</daireno>\n";
            s += "    <postakodu>" + da.getAddressSuggestList().get(i).getPostaKodu() + "</postakodu>\n";
            s += "    <subtype>" + da.getAddressSuggestList().get(i).getSubtype() + "</subtype>\n";
            s += "    <adr>\n";
            s += "      <iladi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrIlAdi()) + "</iladi>\n";
            s += "      <ilceadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrIlceAdi()) + "</ilceadi>\n";
            s += "      <mahalleadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrMahalleAdi()) + "</mahalleadi>\n";
            s += "      <koyadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrKoyAdi()) + "</koyadi>\n";
            s += "      <semtadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrSemtAdi()) + "</semtadi>\n";
            s += "      <caddeadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrCaddeAdi()) + "</caddeadi>\n";
            s += "      <sokakadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrSokakAdi()) + "</sokakadi>\n";
            s += "      <siteadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrSite()) + "</siteadi>\n";
            s += "      <kapino>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrKapiNo()) + "</kapino>\n";
            s += "      <kapiadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrKapiAdi()) + "</kapiadi>\n";
            s += "      <kurumadi>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrKurum()) + "</kurumadi>\n";
            s += "      <kat>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrKat()) + "</kat>\n";
            s += "      <daireno>" + Utils.convStr2Xml(da.getAddressSuggestList().get(i).getAdrDaireNo()) + "</daireno>\n";
            s += "      <postakodu>" + da.getAddressSuggestList().get(i).getAdrPostaKodu() + "</postakodu>\n";
            s += "    </adr>\n";
            s += "    </suggestion>\n";
          }
          
        }
        s += "    </suggestions>\n";
        s = s.replaceAll(">null<", "><");
        return s;
    } // toXml()

    //----------------------------------------------------------------------------


    public void setDa(DataAddress da) {
        this.da = da;
    }

    public DataAddress getDa() {
        return da;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
    
    public void setWhat3words(String what3words) {
      this.what3words = what3words;
    }

    public String getWhat3words() {
      return what3words;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public String getResAddress() {
        return resAddress;
    }
    
    public void setAdrResAddress(String adrResAddress) {
        this.adrResAddress = adrResAddress;
    }

    public String getAdrResAddress() {
        return adrResAddress;
    }

    public void setGeolocLevel(int geolocLevel) {
        this.geolocLevel = geolocLevel;
    }

    public int getGeolocLevel() {
        return geolocLevel;
    }

    public void setVerifyLevel(int verifyLevel) {
        this.verifyLevel = verifyLevel;
    }

    public int getVerifyLevel() {
        return verifyLevel;
    }

    public void setResolveLevel(int resolveLevel) {
        this.resolveLevel = resolveLevel;
    }

    public int getResolveLevel() {
        return resolveLevel;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public int getZone() {
        return zone;
    }


    public void setDgSuggests(ArrayList<DataGeocode> dgSuggests) {
      this.dgSuggests = dgSuggests;
    }
  
    public ArrayList<DataGeocode> getDgSuggests() {
      return dgSuggests;
    }
    
    public void addSuggest(DataGeocode dg) {
      this.dgSuggests.add(dg);
    }

    public void setFrc(String frc) {
        this.frc = frc;
    }

    public String getFrc() {
        return frc;
    }
}
