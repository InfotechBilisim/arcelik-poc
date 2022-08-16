package com.infotech.locationbox.servlet;

import com.infotech.address.cleaner.AppMain;
import com.infotech.address.cleaner.DataAddress;
public class GeocodeOperations {
  private static int maxAddressLength = 0;
  public static String dataVersion;

  public GeocodeOperations() {
  }

  //-----------------------------------------------------------------------------

  public static void initAdresCleaner() {
    String _searchUnique = com.infotech.address.cleaner.Utils.getParameter("search_unique");
    if(_searchUnique != null)
      AppMain.searchUnique = (_searchUnique.equalsIgnoreCase("YES")) || (_searchUnique.equalsIgnoreCase("on")) || (_searchUnique.equalsIgnoreCase("TRUE"));

    String _useKapi = com.infotech.address.cleaner.Utils.getParameter("result_usekapi");
    if(_useKapi != null)
      AppMain.useKapi = (_useKapi.equalsIgnoreCase("YES")) || (_useKapi.equalsIgnoreCase("on")) || (_useKapi.equalsIgnoreCase("TRUE"));

    String _rnd = null;
    _rnd = com.infotech.address.cleaner.Utils.getParameter("randomize_il");
    if(_rnd != null)
      AppMain.randIl = (_rnd.equalsIgnoreCase("YES") || _rnd.equalsIgnoreCase("on") || _rnd.equalsIgnoreCase("TRUE"));
    _rnd = com.infotech.address.cleaner.Utils.getParameter("randomize_ilce");
    if(_rnd != null)
      AppMain.randIlce = (_rnd.equalsIgnoreCase("YES") || _rnd.equalsIgnoreCase("on") || _rnd.equalsIgnoreCase("TRUE"));
    _rnd = com.infotech.address.cleaner.Utils.getParameter("randomize_mahalle");
    if(_rnd != null)
      AppMain.randMahalle = (_rnd.equalsIgnoreCase("YES") || _rnd.equalsIgnoreCase("on") || _rnd.equalsIgnoreCase("TRUE"));
    _rnd = com.infotech.address.cleaner.Utils.getParameter("randomize_yol");
    if(_rnd != null)
      AppMain.randYol = (_rnd.equalsIgnoreCase("YES") || _rnd.equalsIgnoreCase("on") || _rnd.equalsIgnoreCase("TRUE"));

    String _lev = com.infotech.address.cleaner.Utils.getParameter("use_levenshtein");
    if(_lev != null) {
      AppMain.useLevenshtein = (_lev.equalsIgnoreCase("YES") || _lev.equalsIgnoreCase("on") || _lev.equalsIgnoreCase("TRUE"));
      if(AppMain.useLevenshtein) {
        _lev = com.infotech.address.cleaner.Utils.getParameter("use_levenshtein_for_il");
        if(_lev != null)
          AppMain.useLevenshtein_ForIl = (_lev.equalsIgnoreCase("YES") || _lev.equalsIgnoreCase("on") || _lev.equalsIgnoreCase("TRUE"));
        _lev = com.infotech.address.cleaner.Utils.getParameter("use_levenshtein_for_ilce");
        if(_lev != null)
          AppMain.useLevenshtein_ForIlce = (_lev.equalsIgnoreCase("YES") || _lev.equalsIgnoreCase("on") || _lev.equalsIgnoreCase("TRUE"));
        _lev = com.infotech.address.cleaner.Utils.getParameter("use_levenshtein_for_mahalle");
        if(_lev != null)
          AppMain.useLevenshtein_ForMahalle = (_lev.equalsIgnoreCase("YES") || _lev.equalsIgnoreCase("on") || _lev.equalsIgnoreCase("TRUE"));
        _lev = com.infotech.address.cleaner.Utils.getParameter("use_levenshtein_for_koy");
        if(_lev != null)
          AppMain.useLevenshtein_ForKoy = (_lev.equalsIgnoreCase("YES") || _lev.equalsIgnoreCase("on") || _lev.equalsIgnoreCase("TRUE"));
        _lev = com.infotech.address.cleaner.Utils.getParameter("use_levenshtein_for_yol");
        if(_lev != null)
          AppMain.useLevenshtein_ForYol = (_lev.equalsIgnoreCase("YES") || _lev.equalsIgnoreCase("on") || _lev.equalsIgnoreCase("TRUE"));
        _lev = com.infotech.address.cleaner.Utils.getParameter("use_levenshtein_for_kapi");
        if(_lev != null)
          AppMain.useLevenshtein_ForKapi = (_lev.equalsIgnoreCase("YES") || _lev.equalsIgnoreCase("on") || _lev.equalsIgnoreCase("TRUE"));
        _lev = com.infotech.address.cleaner.Utils.getParameter("use_levenshtein_for_poi");
        if(_lev != null)
          AppMain.useLevenshtein_ForPoi = (_lev.equalsIgnoreCase("YES") || _lev.equalsIgnoreCase("on") || _lev.equalsIgnoreCase("TRUE"));
      }
    }

    String _mal = com.infotech.address.cleaner.Utils.getParameter("max_address_length");
    if(_mal != null)
      try {
        maxAddressLength = Integer.parseInt(_mal);
      }
      catch(Exception e) {
        ;
      }

    try {
      AppMain.fillParameterValues();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    try {
      dataVersion = AppMain.getDataVersion();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    return;
  }  // initAdresCleaner()

  //-----------------------------------------------------------------------------

  public static int checkGeocodeAddress(String address) {
    if(address == null || address.length() <= 0)
      return -1;

    if(maxAddressLength > 0) {
      int length = address.length();
      if(length > maxAddressLength)
        return -2;
    }

    return 0;
  }  // checkGeocodeAddress()

  //-----------------------------------------------------------------------------

  public static DataGeocode getGeocode(String address, double latitude, double longitude, int suggestion) {
    try {
      Utils.logInfo("ADDRESS: " + address);
      String addr = Utils.toUpperCase(address);
      addr = DataAddress.preprocess(addr);
      Utils.logInfo("PROCESS: " + addr);
      DataAddress da = DataAddress.parseAddress(addr);
      if(da == null || (da.getDogrulamaSeviyesi() <= 0 && da.getCozumlemeSeviyesi() <= 0)) {
        Utils.logInfo("Could not parse address.");
        if(latitude == 0.00 || longitude == 0.00)
          return null;

        DataAddress daArea = Utils.getIlIlceMahalle(latitude, longitude);
        if(daArea == null)
          return null;

        Utils.logInfo("Getting help from given coordinates.");
        Utils.logInfo("NEW PROCESS: " + addr + " " + daArea.getIlAdi());
        da = DataAddress.parseAddress(addr + " " + daArea.getIlAdi());
        if(da == null)
          return null;

      }

      Utils.logInfo("Parsed successfully.");
      DataGeocode dg = DataGeocode.getInstance(da, address, suggestion);
      
      for (DataAddress dag : da.getAddressSuggestList())  {  
          DataGeocode dgs = DataGeocode.getInstance(dag, address, suggestion);
          dg.addSuggest(dgs);         
        }
        
      return dg;
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }  // getGeocode()

  //-----------------------------------------------------------------------------

  public static DataGeocode getReverseGeocode(double latitude, double longitude, double tolKapi, double tolYol, int buffer) {
    DataAddress da = Utils.getIlIlceMahalle(latitude, longitude);
    if(da == null)
      return null;
    
     double ycoor = latitude;
     double xcoor = longitude;
      
    DataIl di = Operations.getIlInfo(da.getIlId(), false, false, 0, null);
    if(di != null){
      da.setIlUavt(di.getAdresKodu());
      xcoor = di.getLongitude();
      ycoor = di.getLatitude();
    }
    DataIlce dic = Operations.getIlceInfo(da.getIlceId(), false, false, 0, null);
    if(dic != null){
      da.setIlceUavt(dic.getAdresKodu());
      xcoor = dic.getLongitude();
      ycoor = dic.getLatitude();
    }
    DataMahalle dim = Operations.getMahalleInfo(da.getMahalleId(), false, false, 0, null);
    if(dim != null) {
      da.setMahalleUavt(dim.getAdresKodu());
      da.setAdrPostaKodu(dim.getPostaKodu());
      da.setPostaKodu(dim.getPostaKodu());
      xcoor = dim.getLongitude();
      ycoor = dim.getLatitude();
    }

   // DataMahalle dm = null;

    DataYol dy = null;
    DataKapi dk = null;

//    ADB17Q2 degisen tablo yapisi geregi kaldirildi. Mevcut durumdaki koyleri getMahalleInfo() metodu sorgulamaktadir.
    
//    if((da.getIlceAdi() != null && !da.getIlceAdi().isEmpty()) && ((da.getMahalleAdi() == null) || (!da.getMahalleAdi().isEmpty() && da.getMahalleAdi().contains("Mücavir")))) {
//      dm = Utils.getKoyInfo(latitude, longitude, 5000);
//    }

   /* if(dm != null) {
      da.setKoyAdi(dm.getName());
      da.setAdrKoyAdi(dm.getName());
      da.setKoyId(dm.getId());
      da.setAdrPostaKodu(dm.getPostaKodu());
      da.setPostaKodu(dm.getPostaKodu());
    }*/

    String address = ((da.getMahalleAdi() == null || da.getMahalleAdi().equals("")) ? "" : da.getMahalleAdi() + " MAH. ");

    address += ((da.getKoyAdi() == null || da.getKoyAdi().equals("")) ? "" : da.getKoyAdi() + " KÖYÜ");

    int typ = 0;
    int geolevel = -1;


    dk = Utils.getKapiInfo(latitude, longitude, tolKapi);

    if(dk != null) {
      address += (dk.getYolAdi() == null ? "" : dk.getYolAdi()) + (dk.getName() == null ? "" : " " + dk.getName()) + (dk.getNo() == null ? "" : " NO: " + dk.getNo());
      address = address.trim();
      typ = 1;
      
      if( Utils.isStringDataNull(da.getPostaKodu()) ){
        da.setAdrPostaKodu(dk.getPostaKodu());
        da.setPostaKodu(dk.getPostaKodu());
      }
      da.setKapiId(dk.getId());
      da.setAdrKapiAdi(dk.getName());
      da.setKapiAdi(dk.getName());
      da.setAdrKapiNo(dk.getNo());
      da.setKapiNo(dk.getNo());
      da.setKapiUavt(dk.getAdresKodu());
      xcoor = dk.getLongitude();
      ycoor = dk.getLatitude();

      dy = Utils.getYolInfo(latitude, longitude, tolYol);
      if(dy != null) {
        if(dy.getYolSinifi() == 2 || dy.getYolSinifi() == 8 || dy.getYolSinifi() == 9 || dy.getYolSinifi() == 12) {
          da.setSokakId(dk.getYolId());
          da.setAdrSokakAdi(dk.getYolAdi());
          da.setSokakAdi(dk.getYolAdi());
          da.setSokakUavt(dy.getAdresKodu());
          xcoor = dy.getLongitude();
          ycoor = dy.getLatitude();
        }
        else {
          da.setCaddeId(dk.getYolId());
          da.setAdrCaddeAdi(dk.getYolAdi());
          da.setCaddeAdi(dk.getYolAdi());
          da.setCaddeUavt(dy.getAdresKodu());
          xcoor = dy.getLongitude();
          ycoor = dy.getLatitude();
        }
      }
    }
    else {
      dy = Utils.getYolInfo(latitude, longitude, tolYol);
      if(dy != null) {
        address += (dy.getName() == null ? "" : " " + dy.getName());
        address = address.trim();
        typ = 2;

        if(dy.getYolSinifi() == 2 || dy.getYolSinifi() == 8 || dy.getYolSinifi() == 9 || dy.getYolSinifi() == 12) {
          da.setSokakId(dy.getId());
          da.setAdrSokakAdi(dy.getName());
          da.setSokakAdi(dy.getName());
          da.setSokakUavt(dy.getAdresKodu());
          xcoor = dy.getLongitude();
          ycoor = dy.getLatitude();
        }
        else {
          da.setCaddeId(dy.getId());
          da.setAdrCaddeAdi(dy.getName());
          da.setCaddeAdi(dy.getName());
          da.setCaddeUavt(dy.getAdresKodu());
          xcoor = dy.getLongitude();
          ycoor = dy.getLatitude();
        }
        if( Utils.isStringDataNull(da.getPostaKodu()) ){
            da.setAdrPostaKodu(dy.getPostaKodu());
            da.setPostaKodu(dy.getPostaKodu());
        }
      }
        
    }
    if(dy != null)
       da.setYolTuru(dy.getYolTuru());
    else
       da.setYolTuru("");
      
    // geolevel belirlenmesi..
    if(da.getKapiId() > 0)
      geolevel = 6;
    else if(da.getCaddeId() > 0 || da.getSokakId() > 0)
      geolevel = 5;
    else if(da.getKoyId() > 0)
      geolevel = 4;
    else if(da.getMahalleId() > 0)
      geolevel = 3;
    else if(da.getIlceId() > 0)
      geolevel = 2;
    else if(da.getIlId() > 0)
      geolevel = 1;

    address += " " + (da.getPostaKodu() == null || da.getPostaKodu().length() < 3 ? "" : " " + da.getPostaKodu());
    address += (da.getIlceAdi() == null ? "" : " " + da.getIlceAdi()) + (da.getIlAdi() == null ? "" : " " + da.getIlAdi());
    address = address.trim();

    address = Utils.toUpperCase(address);

    DataGeocode dg = new DataGeocode(da);
    if(xcoor == 0.0 || ycoor == 0.0){
        dg.setLatitude(latitude);
        dg.setLongitude(longitude);
    } else{
        dg.setLatitude(ycoor);
        dg.setLongitude(xcoor);  
    }
    dg.setGeolocLevel(geolevel);
    dg.setResolveLevel(typ);
    dg.setResAddress(address);
    dg.setZone(da.getZone());
    String frc = null;
    if( buffer == 10 || buffer == 50 || buffer == 100 ){
        frc = Utils.getYolFRCInfo(latitude, longitude, buffer);
        dg.setFrc(frc);
    }
    return dg;
  }  // getReverseGeocode()

}
