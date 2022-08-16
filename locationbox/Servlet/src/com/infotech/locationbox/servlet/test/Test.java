package com.infotech.locationbox.servlet.test;

import com.infotech.locationbox.servlet.DataDirection;
import com.infotech.locationbox.servlet.DataGlobalDirection;
import com.infotech.locationbox.servlet.DataGlobalGeocode;
import com.infotech.locationbox.servlet.DataReverseGlobalGeocode;
import com.infotech.locationbox.servlet.DataRota;
import com.infotech.locationbox.servlet.DataTspPoint;
import com.infotech.locationbox.servlet.Utils;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test {
    public static void main(String[] args) {
        String res = "{\"routes\":[{\"id\":\"cb7ac1f1-9401-4791-8cd0-496c6b517231\",\"sections\":[{\"id\":\"eeeef4d4-4eb0-494b-96b3-012aafb03e98\",\"type\":\"vehicle\",\"departure\":{\"time\":\"2022-08-05T14:16:50+02:00\",\"place\":{\"type\":\"place\",\"location\":{\"lat\":52.5137479,\"lng\":13.4246242},\"originalLocation\":{\"lat\":52.51375,\"lng\":13.42462}}},\"arrival\":{\"time\":\"2022-08-05T14:20:51+02:00\",\"place\":{\"type\":\"place\",\"location\":{\"lat\":52.5178916,\"lng\":13.4341805},\"originalLocation\":{\"lat\":52.5178709,\"lng\":13.4341749},\"waypoint\":0}},\"summary\":{\"duration\":241,\"length\":1885,\"baseDuration\":194,\"typicalDuration\":204},\"polyline\":\"BGo9llkDg_rzZkF0G8LoQ4NoQgP8QoLgKgyB0tB4XoVoLsJ0K4IwWoQsOsJ8LwHoLoGkS0KkN8GsJ0F8GgF4D4DsE0FwHoLA8GUoGoB0PoB0PU8LAgKTwMTkInB0P7BkSjDwbvCsYjD4c7BgU7BkSvC4X7B8QrEwqB_EgyBjDwbnBgPvC8VnBkNvCgZjDoanBsOvC4XvC8VjDoajD8f7BkS7BsTjDge7BgU_EsxBnBwM4I8BoB7LkDnf4D7kB4D3hBwC7a8B3SwC3XkDvboB3NoBnL8BrToB7LwC_YsEzoBkDjhBwC7V0C_Y\",\"transport\":{\"mode\":\"car\"}},{\"id\":\"b7515283-08ed-4d29-a6c3-9fc9c04c06b2\",\"type\":\"vehicle\",\"departure\":{\"time\":\"2022-08-05T14:20:51+02:00\",\"place\":{\"type\":\"place\",\"location\":{\"lat\":52.5178916,\"lng\":13.4341805},\"originalLocation\":{\"lat\":52.5178709,\"lng\":13.4341749},\"waypoint\":0}},\"arrival\":{\"time\":\"2022-08-05T14:23:18+02:00\",\"place\":{\"type\":\"place\",\"location\":{\"lat\":52.5242323,\"lng\":13.4301462},\"originalLocation\":{\"lat\":52.52426,\"lng\":13.43},\"waypoint\":1}},\"summary\":{\"duration\":147,\"length\":1010,\"baseDuration\":114,\"typicalDuration\":119},\"polyline\":\"BGogulkDo0-zZQ_EwCnawC3XwC7V8BjSkDvgB4D3hB4DriBwC_YkDjcwCrT8BrJkD7LwCjIsEzKwH_OsErJwCjDkD7GoLUgFoB0F8B8Q0FsE8BkmBkSgtBwWkS8GwH8B8GoBosCgKkhBsE0FU0FUgZjD0KgF8GkDsE8BoGkD4F8C\",\"transport\":{\"mode\":\"car\"}},{\"id\":\"7068a925-c8ad-4be4-9709-132ef7f9a01e\",\"type\":\"vehicle\",\"departure\":{\"time\":\"2022-08-05T14:23:18+02:00\",\"place\":{\"type\":\"place\",\"location\":{\"lat\":52.5242323,\"lng\":13.4301462},\"originalLocation\":{\"lat\":52.52426,\"lng\":13.43},\"waypoint\":1}},\"arrival\":{\"time\":\"2022-08-05T14:23:58+02:00\",\"place\":{\"type\":\"place\",\"location\":{\"lat\":52.5232228,\"lng\":13.4280044},\"originalLocation\":{\"lat\":52.5233199,\"lng\":13.428}}},\"summary\":{\"duration\":40,\"length\":222,\"baseDuration\":30,\"typicalDuration\":31},\"polyline\":\"BGws6lkDk42zZ3F7CnGjDrE7B7GjDzK_ErEzF3DjDzK_EvHrE7BjhBTzoBN_Z\",\"transport\":{\"mode\":\"car\"}}]}]}";
      
       
       DataRota drgg =getGlobalRota( res,  true,  true,  true);
       DataDirection[] dataDirections = drgg.getDirections();
       String coors = "";
      
     /* for(int j=0;j<dataDirections.length; j++){
           //dataDirections[j].
           List<LatLngZ> line = PolylineEncoderDecoder.decode("BGws6lkDk42zZ3F7CnGjDrE7B7GjDzK_ErEzF3DjDzK_EvHrE7BjhBTzoBN_Z");             
           for( int i = 0; i < line.size(); i++ ) {
               if( i > 0 ) coors += ",";
                coors += line.get(i).lng +"," + line.get(i).lat;
            }
       }*/
       // System.out.println(drgg);
    }
    
    public static DataRota getGlobalRota(String res, boolean dirProduce, boolean geometry, boolean encode) {
        DataRota dataRota=new DataRota();
        try {
            JSONObject jsonres = new JSONObject(res);
            JSONArray routes = Utils.getJSONArrayValueFromJSONObject(jsonres, "routes", false);
            if (routes != null && routes.length() > 0) {
                JSONObject routesRes = routes.getJSONObject(0);
                
                if(routesRes!=null){
                    List<Double> coors=new ArrayList();
                    JSONArray sections = Utils.getJSONArrayValueFromJSONObject(routesRes, "sections", false);
                    double pathDistance = 0, pathDuration = 0, pathDurationWithTmcFlow =0;
                    if (sections != null && sections.length() > 0) {
                        List<DataGlobalDirection> dataDirections=new ArrayList();

                        for(int i=0; i<sections.length(); i++){
                            JSONObject contentObject=sections.getJSONObject(i);
                               if(contentObject!=null){
                                DataGlobalDirection dataDirection=new DataGlobalDirection();  
                                JSONObject summary = Utils.getJSONObjectValueFromJSONObject(contentObject, "summary", false);
                                String id = Utils.getStringValueFromJSONObject(contentObject, "id", false);
                                dataDirection.setId(id);
                                String polyline = Utils.getStringValueFromJSONObject(contentObject, "polyline", false);
                                dataDirection.setPolyline(polyline);
                                if(summary!=null){
                                     dataDirection.setLinkId(i);
                                   // dataDirection.setDirection(instruction);
                                    double length = Utils.getDoubleValueFromJSONObject(summary, "length", false);
                                    pathDistance += length;
                                    dataDirection.setDistance(length);
                                    double duration = Utils.getDoubleValueFromJSONObject(summary, "duration", false);
                                    dataDirection.setDuration(duration);
                                    pathDuration += duration;
                                    double typicalDuration = Utils.getDoubleValueFromJSONObject(summary, "typicalDuration", false);
                                    pathDurationWithTmcFlow += typicalDuration;    
                                    dataDirections.add(dataDirection);
                                    if(dirProduce)
                                    dataRota.setDirections(dataDirections.toArray(new DataGlobalDirection[dataDirections.size()]));
                                  }   
                                
                                if(geometry){
                                    JSONObject departure = Utils.getJSONObjectValueFromJSONObject(contentObject, "departure", true);
                                    if(departure!=null){
                                      JSONObject place = Utils.getJSONObjectValueFromJSONObject(departure, "place", true);
                                       if(place!=null){
                                           JSONObject originalLocation = Utils.getJSONObjectValueFromJSONObject(place, "originalLocation", true);
                                           if(originalLocation!=null)
                                           coors.add(Utils.getDoubleValueFromJSONObject(originalLocation, "lng", true));
                                           coors.add(Utils.getDoubleValueFromJSONObject(originalLocation, "lat", true));
                                       }
                                    }
                                }
                                
                            }
                        }
                }
                double[] target = new double[coors.size()];
                 for (int i = 0; i < target.length; i++) {
                    target[i] = coors.get(i);
                 }  
                dataRota.setCoors(target);
                  /* JSONArray jsonWayPoint=eachJsonObject.getJSONArray("waypoint");
                   if(jsonWayPoint!=null){
                       List<DataTspPoint> dataTspPointList=new ArrayList();
                       for(int k=0;k<jsonWayPoint.length();k++){
                           DataTspPoint dataTspPoint=new DataTspPoint();
                           JSONObject objec =  jsonWayPoint.getJSONObject(k);
                     if(objec!=null){
                           String tspName=objec.getString("mappedRoadName");
                           dataTspPoint.setName(tspName!=null?tspName:"");
                           dataTspPointList.add(dataTspPoint);
                           }
                       }
                       dataRota.setTspPoints(dataTspPointList.toArray(new DataTspPoint[dataTspPointList.size()]));
                   }*/
                    
                dataRota.setPathDistance(pathDistance);
                dataRota.setPathDuration(pathDuration);
                dataRota.setPathDurationWithTmcFlow(pathDurationWithTmcFlow);
                }

            }
        } catch (Exception ex) {
            Utils.showError("getGlobalRoute "+ ex.getMessage());
        }  
    return dataRota;
    } 
    
    public static DataReverseGlobalGeocode getReverseGlobalGeocode(String res) {
        double latitude = 0.00, longitude = 0.00;
        String city = null, country = null, postCode = null, formattedAddress = null, municipality = null, countryCode = null, countrySubdivision = null, countrySecondarySubdivision =null, countryCodeISO3 =null;

        try {
            JSONObject jsonres = new JSONObject(res);
            JSONArray geoResult = Utils.getJSONArrayValueFromJSONObject(jsonres, "addresses", false);
            if (geoResult != null && geoResult.length() > 0) {
                JSONObject geoRes = geoResult.getJSONObject(0);

                String position = Utils.getStringValueFromJSONObject(geoRes, "position", false);
                if(position != null){
                    String [] coors = Utils.splitString(position, ",");
                    if(coors!=null && coors.length==2){
                        longitude = Utils.convertStringToDoubleValue("Split Coors", coors[0]);
                        latitude = Utils.convertStringToDoubleValue("Split Coors", coors[1]);
                    }
                }

                JSONObject address = Utils.getJSONObjectValueFromJSONObject(geoRes, "address", true);
                if(address != null){
                    countryCode = Utils.getStringValueFromJSONObject(address, "countryCode", false);
                    countrySubdivision = Utils.getStringValueFromJSONObject(address, "countrySubdivision", false);
                    countrySecondarySubdivision = Utils.getStringValueFromJSONObject(address, "countrySecondarySubdivision", false);
                    municipality = Utils.getStringValueFromJSONObject(address, "municipality", false);
                    postCode = Utils.getStringValueFromJSONObject(address, "postalCode", false);
                    country = Utils.getStringValueFromJSONObject(address, "country", false);
                    countryCodeISO3 = Utils.getStringValueFromJSONObject(address, "countryCodeISO3", false);
                    formattedAddress = Utils.getStringValueFromJSONObject(address, "freeformAddress", true);
                    city = Utils.getStringValueFromJSONObject(address, "localName", false);
                }
                   
            } else {
                return null;
            }
        } catch (Exception ex) {
            Utils.showError("getGlobalGeocode "+ ex.getMessage());
        }

        DataReverseGlobalGeocode drgg = new  DataReverseGlobalGeocode("","",countryCode, municipality, countryCodeISO3, countrySubdivision, countrySecondarySubdivision,  country, formattedAddress, postCode, latitude, longitude);
        return drgg;
    } 
}
