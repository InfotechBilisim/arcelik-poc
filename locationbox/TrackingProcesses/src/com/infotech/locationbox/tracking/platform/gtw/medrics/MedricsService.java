package com.infotech.locationbox.tracking.platform.gtw.medrics;

import com.infotech.locationbox.tracking.platform.base.*;

import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

public class MedricsService {
  public static String endPoint = null;
  
  public MedricsService() {
  }

//-----------------------------------------------------------------------------
  
  public static boolean sendAlarm(long rowNo, String username, String msg) {
    String req = "";
    req += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    req += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n";
    req += "  <soap:Body>\n";
    req += "    <SendAlarm xmlns=\"http://tempuri.org/\">\n";
    req += "      <RowNo>" + rowNo + "</RowNo>\n";
    req += "      <Destination>" + username + "</Destination>\n";
    req += "      <Data>" + msg + "</Data>\n";
    req += "    </SendAlarm>\n";
    req += "  </soap:Body>\n";
    req += "</soap:Envelope>\n";
    Log.logToFile(req);
    String soapAction = "http://tempuri.org/SendAlarm";
    String res = sendRequest(endPoint, soapAction, req);
    Log.logToFile(res);
    if( res == null || isResultFaulty(res) ) {
      Log.showError("SendAlarm failed !");
      return false;
    }

    return true;
  } // sendAlarm()

//-----------------------------------------------------------------------------

  private static boolean isResultFaulty(String result) {
    String status = getNodeData(result, "SendAlarmResult");
    if( status.equalsIgnoreCase("OK") ) return false;
    
    return true;      
  } // isResultFaulty()

//-----------------------------------------------------------------------------

  private static String sendRequest(String endPoint, String soapAction, String query) {
    URL url = null;
    HttpURLConnection http = null;
    OutputStream out = null;
    InputStream inp = null;

    Log.logToFile("ENDPOINT: " + endPoint);
    Log.logToFile("SOAPACTION: " + soapAction);

    try {
      url = new URL(endPoint);
      http = (HttpURLConnection)url.openConnection();
      http.setRequestMethod("POST");
      http.setRequestProperty("Content-type", "text/xml");
      http.setRequestProperty("SOAPAction", "\"" + soapAction + "\"");
      http.setDoOutput(true);
      http.setDoInput(true);
      http.connect();

      out = http.getOutputStream();
      out.write(query.getBytes());

      inp = http.getInputStream();
      int code = http.getResponseCode();
      if( code != 200 ) Log.showError("Response Code: " + code);

      String result = "";
      byte b[] = new byte[10240];
      while( true ) {
        int length = inp.read(b, 0, b.length);
        if( length < 0 ) break;

        result += new String(b, 0, length);
      } // while()
      result = result.trim();
      return result;
    }
    catch (Exception ex) {
      Log.showError("Exception : " + ex.getMessage());
      ex.printStackTrace();
    }
    finally {
      try { if( inp != null ) inp.close(); } catch (Exception e) {;}
      try { if( out != null ) out.close(); } catch (Exception e) {;}
      try { if( http != null ) http.disconnect(); } catch (Exception e) {;}
    }

    return null;
  } // sendRequest()

//-----------------------------------------------------------------------------

  private static String getNodeData(String data, String nodeName) {
    int posBeg = data.indexOf("<" + nodeName + ">");
    if( posBeg < 0 ) return null;

    posBeg += (1 + nodeName.length() + 1);
    int posEnd = data.indexOf("</" + nodeName + ">", posBeg);
    if( posEnd < 0 ) return null;

    return( data.substring(posBeg, posEnd) );
  } // getNodeData()

}
