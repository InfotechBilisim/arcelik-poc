package com.infotech.locationbox.trackingws;


import com.infotech.locationbox.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import javax.servlet.*;
import javax.servlet.http.*;

public class TrackingServlet extends HttpServlet {
  private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
  private static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";
  private static final String appVersion = "v1.14";

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String method = request.getMethod();
    String remoteIp = getClientIpAddr(request);
    String uri = request.getRequestURI().trim();
    String token = request.getParameter("token");
        Utils.showText("METHOD: " + method + ", IP: " + remoteIp + ", URI: " + uri + ", TOKEN: " + token);

    if( method.equals("OPTIONS") ) {
      response.setHeader("Allow", "GET,POST,PUT,DELETE");
      return;
    }
    
    String[] info = uri.split("/");
    if( info.length <= 2 ) {
      response.setContentType(CONTENT_TYPE_HTML);
      PrintWriter out = response.getWriter();
      sendFunctionList(out);
      return;
    }
    
    String resource = info[2];
    String[] items = new String[info.length - 3];
    for( int i = 0; i < items.length; i++ ) items[i] = info[3 + i];

    ServletInputStream inp = request.getInputStream();
    byte[] bytes = new byte[8192];
    String postData = "";
    while( true ) {
      int len = inp.read(bytes);
      if( len <= 0 ) break;

      postData += Utils.getStringFromBytes(bytes, len);
    } // while()
        Utils.showText("POST DATA: " + postData);

    response.setContentType(CONTENT_TYPE_JSON);
    PrintWriter out = response.getWriter();

    JsonObject data = null;
    if( postData != null && postData.length() > 0 ) {
      bytes = Utils.getJsonStringBytes(postData);
      if( bytes == null ) {
        sendInputDataFailure(out);
        out.close();
        return;
      }

      InputStream stream = new ByteArrayInputStream(bytes);
      JsonReader jsonReader = Json.createReader(stream);
      try { data = jsonReader.readObject(); } catch (Exception ex) { ex.printStackTrace(); }
    }

    if( token == null ) token = findToken(items);
    DataRegister dr = DataRegister.getInstance(token, remoteIp);
    if( dr == null ) dr = new DataRegister(remoteIp);
    
    long rowNo = Utils.logWebServiceRequest(dr, uri, postData);
    if( resource.equals("register") ) Operations.doRegister(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("mobile") ) Operations.doMobile(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("mobiles") ) Operations.doMobiles(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("schedule") ) Operations.doSchedule(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("schedules") ) Operations.doSchedules(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("location") ) Operations.doLocation(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("locations") ) Operations.doLocations(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("alarms") ) Operations.doAlarms(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("loctypes") ) Operations.doLocTypes(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("eventcodes") ) Operations.doEventCodes(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("events") ) Operations.doEvents(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("region") ) Operations.doRegion(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("regions") ) Operations.doRegions(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("campus") ) Operations.doCampus(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("campuses") ) Operations.doCampuses(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("venue") ) Operations.doVenue(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("venues") ) Operations.doVenues(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("floor") ) Operations.doFloor(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("floors") ) Operations.doFloors(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("area") ) Operations.doArea(rowNo, dr, out, method, data, items);
    else
    if( resource.equals("areas") ) Operations.doAreas(rowNo, dr, out, method, data, items);
    else {
      sendInvalidResource(out, rowNo, resource);
      out.close();
      return;
    }

    out.close();
    return;
  }

//-----------------------------------------------------------------------------

  private String getClientIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
    }
    return ip;
  } // getClientIpAddr()

//-----------------------------------------------------------------------------

  private String findToken(String[] items) {
    for( int i = 0; i < items.length; i++ ) {
      if( items[i].equals("token") && (i + 1) < items.length ) return items[i + 1];
      
    } // for()
    return null;
  } // findToken()
  
//-----------------------------------------------------------------------------
  
  private void sendFunctionList(PrintWriter out) {
    String res = "";
    res += "<html>";
    res += "<head><title>TrackingWebService Function List (" + appVersion +  ")</title></head>";
    res += "<body>";
    res += "<p>";
    res += "<h2>Function List(" + appVersion + ")</h2>";
    res += "<ul>";
    res += "<li>register</li>";
    res += "<li>mobile</li>";
    res += "<li>mobiles</li>";
    res += "<li>schedule</li>";
    res += "<li>schedules</li>";
    res += "<li>location</li>";
    res += "<li>locations</li>";
    res += "<li>alarms</li>";
    res += "<li>loctypes</li>";
    res += "<li>eventcodes</li>";
    res += "<li>events</li>";
    res += "<li>region</li>";
    res += "<li>regions</li>";
    res += "<li>campus</li>";
    res += "<li>campuses</li>";
    res += "<li>venue</li>";
    res += "<li>venues</li>";
    res += "<li>floor</li>";
    res += "<li>floors</li>";
    res += "<li>area</li>";
    res += "<li>areas</li>";
    res += "</ul>";
    res += "</p>";
    res += "</body>";
    res += "</html>";
        Utils.showText("RESPONSE: " + res);
    out.println(res);
    return;
  } // sendInputDataFailure()

//-----------------------------------------------------------------------------
  
  private void sendInputDataFailure(PrintWriter out) {
    String res = "{ \"status\": -4, \"message\": \"Input data error\" }";
        Utils.showText("RESPONSE: " + res);
    out.println(res);
    return;
  } // sendInputDataFailure()

//-----------------------------------------------------------------------------
  
  private void sendInvalidResource(PrintWriter out, long rowNo, String resource) {
    String res = "{ \"status\": -5, \"message\": \"Invalid resource: " + resource + "\" }";
    out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
    return;
  } // sendInvalidResource()


}
