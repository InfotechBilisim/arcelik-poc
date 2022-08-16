package com.infotech.locationbox.servlet;

import com.infotech.inforiskskor.InfoRiskSkor;
import com.infotech.inforiskskor.SkorResultInfo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.imageio.ImageIO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.sql.BLOB;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.json.JSONArray;
import org.json.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;


public class LocationBoxServlet extends HttpServlet {

    private static final String PROGRAM_VERSION = "8.0.0";
    private static final String PROGRAM_DATETIME = "2022-08-08";

    private static final String CONTENT_TYPE_XML = "text/xml; charset=utf-8";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    private static final String CONTENT_TYPE_JS = "text/javascript; charset=utf-8";
    private static final String CONTENT_TYPE_GIF = "image/gif;";
    private static final String CONTENT_TYPE_JPG = "image/jpeg;";
    private static final String CONTENT_TYPE_PNG = "image/png;";

    protected static String mapviewerSym = null;
    protected static String symDirPrefix = null;
    protected static String symUrlPrefix = null;
    protected static String mapUrlPrefix = null;

    protected static String dataSrc = null, baseMap = null, mvURL = null;

    protected static String locationSymbolName = null;
    protected static String defaultSymbolName = null;

    private static final String PACKAGE_TRAFIK = "TRAFIK";
    private static final String PACKAGE_NOBETCI_ECZANE = "NOBETCI_ECZANE";
    private static final String PACKAGE_AKARYAKIT_FIYATLARI = "AKARYAKIT_FIYATLARI";
    private static final String PACKAGE_OTOPARK_FIYATLARI = "OTOPARK_FIYATLARI";
    private static final String PACKAGE_ROTA_GEOMETRISI = "ROTA_GEOMETRISI";
    private static final String PACKAGE_TMCHAT_GEOMETRISI = "TMCHAT_GEOMETRISI";
    private static final String PACKAGE_YOL2SHAPE = "YOL2SHAPE";
    private static final String PACKAGE_KAMPANYA = "KAMPANYA";
    private static final String PACKAGE_FIRSATLAR = "FIRSATLAR";
    private static final String PACKAGE_DEMOGRAFIK = "DEMOGRAFIK";
    private static final String PACKAGE_SPATIAL_ANALYSIS = "SPATIAL_ANALYSIS";
    private static final String PACKAGE_RAW_POI_COOR = "RAW_POI_COOR";
    private static final String PACKAGE_RISK = "RISK";
    private static final String PACKAGE_KAPI_GEOMETRISI = "KAPI_GEOMETRISI";
    private static final String PACKAGE_YOL_GEOMETRISI = "YOL_GEOMETRISI";
    private static final String PACKAGE_MAHALLE_GEOMETRISI = "MAHALLE_GEOMETRISI";
    private static final String PACKAGE_ILCE_GEOMETRISI = "ILCE_GEOMETRISI";
    private static final String PACKAGE_IL_GEOMETRISI = "IL_GEOMETRISI";
    private static final String PACKAGE_GLOBAL_GEOCODE = "GLOBAL_GEOCODE";
    private static final String PACKAGE_WEATHER_REPORT = "WEATHER_REPORT";
    private static final String PACKAGE_SUB_KEY = "SUB_KEY";
    private static final String PACKAGE_ROUTE_OPTIMIZATION = "ROUTE_OPTIMIZATION";
    private static final String PACKAGE_CLUSTER = "CLUSTER";
    private static final String PACKAGE_PHONEBOOK = "PHONEBOOK";
    private static final String PACKAGE_IMAGE_INDEX = "IMAGE_INDEX";
    private static final String PACKAGE_UAVT_CODE = "UAVT";
    private static final String PACKAGE_EARTHQUAKE = "EARTHQUAKE";
    private static final String PACKAGE_INDOOR = "INDOOR";
    private static final String PACKAGE_WHAT3WORDS = "WHAT3WORDS";
    private static final String PACKAGE_APIV2 = "APIV2";
    private static final String PACKAGE_NOBETCI_NOTER = "NOBETCI_NOTER";
    private static final String PACKAGE_FEEDBACK = "FEEDBACK";

    private static final String EXTYPE_NOBETCI_ECZANE = "PHARMACYONDUTY";
    private static final String EXTYPE_AKARYAKIT_FIYATLARI = "FUELPRICES";
    private static final String EXTYPE_OTOPARK_FIYATLARI = "CARPARKPRICES";
    private static final String EXTYPE_NOBETCI_NOTER = "NOTARYONDUTY";

    private static boolean isFakeCoorNeeded = true;

    private static char[] jsBuf = new char[901429];
    private static char[] jsv2Buf = new char[1901429];
    private static char[] js3dLbBuf = new char[764905];

    private static int jsBufLength = 0;
    private static int jsv2BufLength = 0;
    private static int js3dBufLength = 0;

    private static String sqlReservedWords[] = {
        "ACCESS", "ADD", "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AUDIT", "BETWEEN", "BY", "CHAR", "CHECK", "CLUSTER", "COLUMN", "COMMENT", "COMPRESS", "CONNECT", "CREATE", "CURRENT", "DATE", "DECIMAL", "DEFAULT",
        "DELETE", "DESC", "DISTINCT", "DROP", "ELSE", "EXCLUSIVE", "EXISTS", "FILE", "FLOAT", "FOR", "FROM", "GRANT", "GROUP", "HAVING", "IDENTIFIED", "IMMEDIATE", "IN", "INCREMENT", "INDEX", "INITIAL", "INSERT",
        "INTEGER", "INTERSECT", "INTO", "IS", "LEVEL", "LIKE", "LOCK", "LONG", "MAXEXTENTS", "MINUS", "MLSLABEL", "MODE", "MODIFY", "NOAUDIT", "NOCOMPRESS", "NOT", "NOWAIT", "NULL", "NUMBER", "OF", "OFFLINE", "ON",
        "ONLINE", "OPTION", "OR", "ORDER", "PCTFREE", "PRIOR", "PRIVILEGES", "PUBLIC", "RAW", "RENAME", "RESOURCE", "REVOKE", "ROW", "ROWID", "ROWNUM", "ROWS", "SELECT", "SESSION", "SET", "SHARE", "SIZE", "SMALLINT",
        "START", "SUCCESSFUL", "SYNONYM", "SYSDATE", "TABLE", "THEN", "TO", "TRIGGER", "UID", "UNION", "UNIQUE", "UPDATE", "USER", "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2", "VIEW", "WHENEVER", "WHERE", "WITH"
    };
    private static String poiReservedWords[] = {
        "ID", "STANDARD_NAME", "ALTERNATIVE_NAME", "ENGLISH_NAME", "BRAND_NAME1", "BRAND_NAME2", "BRAND_NAME3", "BRAND_NAME4", "TYPE", "SUB_TYPE", "PLACE_NAME", "STREET_NAME", "STREET_TYPE", "HSN", "ROAD_NAME",
        "POSTAL_CODE", "COUNTRY_CODE", "AREA_CODE", "TELEPHONE", "FAX", "EMAIL", "INTERNET_ADDRESS", "OPENING_PERIOD", "PA", "TUR", "IL_ID", "IL_ADI", "ILCE_ID", "ILCE_ADI", "MAHALLE_ID", "MAHALLE_ADI", "XCOOR", "YCOOR",
        "NEREDE", "ACIKLAMA", " ZONE", " BRAND_ID", "BRAND_STYLE", "SUB_TYPE_STYLE", "NEREDE1", "GEOCODE_DATE", "UPDATE_DATE", "FLAG", "ALTLIK", "GEOCODE", "OPTIONS", "YOL_ID", "BRAND_SEMBOL_NO", "MI_STYLE", "MI_PRINX",
        "GEOLOC", "KEYWORD"
    };

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
       
        GeocodeOperations.initAdresCleaner();
        Operations.fakeCoorSrid = 81989006;
        try {
            DataResponse.initDataResponseVariables();
            Operations.fakeCoorSrid = Integer.parseInt(Utils.getParameter("fakecoor_srid"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String _fcfp = Utils.getParameter("fakecoor_for_poi");
        if (_fcfp != null)
            isFakeCoorNeeded = (_fcfp.equalsIgnoreCase("YES")) || (_fcfp.equalsIgnoreCase("on")) || (_fcfp.equalsIgnoreCase("TRUE"));

        DataNetwork.networks = new DataNetwork[3];
        DataNetwork.networks[0] = new DataNetwork("yaya");
        DataNetwork.networks[1] = new DataNetwork("short");
        DataNetwork.networks[2] = new DataNetwork("fast");
        return;
    } // init()

    //-----------------------------------------------------------------------------

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(response);

        Utils.showText("GET: " + request.getQueryString());
        String typ = request.getParameter("Typ");
        if (typ == null || typ.length() <= 0)
            typ = "JSON";
        else {
            if (typ.equalsIgnoreCase("XML"))
                typ = "XML";
            else if (typ.equalsIgnoreCase("JS"))
                typ = "JS";
            else
                typ = "JSON";
        }

        String key = request.getParameter("Key");
        String cmd = request.getParameter("Cmd");
        
        if (cmd!=null && cmd.equalsIgnoreCase("GETTILEDEFINITION")) {
            doGetTileDefinition(key, request, response, typ);
            return;
        }

        if (cmd!=null && cmd.equalsIgnoreCase("GETTILE")) {
            doGetTile(key, request, response);
            return;
        }

        if (cmd!=null && cmd.equalsIgnoreCase("GETUSERIMAGE")) {
            doGetUserImage(key, request, response);
            return;
        }

        if (cmd!=null && cmd.equalsIgnoreCase("MAPIMAGE")) {
            doMapImage(key, request, response);
            return;
        }

        if (cmd!=null && cmd.equalsIgnoreCase("MAPTRAFFICIMAGE")) {
            doMapTrafficImage(key, request, response);
            return;
        }
        
        if (cmd!=null && cmd.equalsIgnoreCase("MAPTHEMEIMAGE")) {
           doMapThemeImage(key, request, response);
           return;
        }

        if (cmd!=null && cmd.equalsIgnoreCase("IMAGEINDEX")) {
            doImageIndex(key, request, response);
            return;
        }          
           
        String callback = request.getParameter("callback");
        if (callback == null || callback.length() <= 0)
            callback = request.getParameter("Callback");
        if (callback == null || callback.length() <= 0)
            callback = request.getParameter("CallBack");

        if (typ.equals("XML"))
            response.setContentType(CONTENT_TYPE_XML);
        else if (typ.equals("JS"))
            response.setContentType(CONTENT_TYPE_JS);
        else
            response.setContentType(CONTENT_TYPE_JSON);

        PrintWriter out = null;
        try{
           out = response.getWriter();
        if (key == null || key.length() <= 0) {
            String transactionId = getTransactionId();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: " + cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10012", "Error occured while processing. Please check parameter 'Key'.");
            return;
        }

        if (!Utils.isKeyValid(key)) {
            String transactionId = getTransactionId();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: " + cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            return;
        }
        String referer = request.getHeader("Referer");
        Utils.logInfo("REFERRER: " + referer);
        Utils.logInfo("KEY: " + key);

        String domain = Utils.getDomainFromReferer(referer);
        if (!Utils.isKeyDomainValid(key, domain)) {
            String transactionId = getTransactionId();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: " + cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10014", "The key used is not active for used domain " + domain + " ! Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            return;
        }

        if (cmd == null || cmd.length() <= 0) {
            String transactionId = getTransactionId();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: " + cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10011", "Error occured while processing. Please check parameter 'Cmd'.");
            return;
        }
        doOperation(request, out, cmd, typ, key, callback);
      }catch(Exception ex){
          Utils.showError("doGet " + ex.getMessage());       
      }finally{
        DbConn.closeHttpConn(null,null, out, null);
     }
        return;
    } // doGet()

    //-----------------------------------------------------------------------------

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        setAccessControlHeaders(response);
        Utils.showText("POST: " + request.getQueryString());

        String typ = request.getParameter("Typ");
        if (typ == null || typ.length() <= 0)
            typ = "JSON";
        else {
            if (typ.equalsIgnoreCase("XML"))
                typ = "XML";
            else if (typ.equalsIgnoreCase("JS"))
                typ = "JS";
            else
                typ = "JSON";
        }

        String key = request.getParameter("Key");
        String cmd = request.getParameter("Cmd");

        String callback = request.getParameter("callback");
        if (callback == null || callback.length() <= 0)
            callback = request.getParameter("Callback");
        if (callback == null || callback.length() <= 0)
            callback = request.getParameter("CallBack");

        if (typ.equals("XML"))
            response.setContentType(CONTENT_TYPE_XML);
        else if (typ.equals("JS"))
            response.setContentType(CONTENT_TYPE_JS);
        else
            response.setContentType(CONTENT_TYPE_JSON);

        PrintWriter out = null;
        try {
          out = response.getWriter();
        if (key == null || key.length() <= 0) {
            String transactionId = getTransactionId();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: " + cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10012", "Error occured while processing. Please check parameter 'Key'.");
            return;
        }

        if (!Utils.isKeyValid(key)) {
            String transactionId = getTransactionId();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: " + cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            return;
        }

        String referer = request.getHeader("Referer");
        Utils.logInfo("REFERRER: " + referer);
        Utils.logInfo("KEY: " + key);

        String domain = Utils.getDomainFromReferer(referer);
        if (!Utils.isKeyDomainValid(key, domain)) {
            String transactionId = getTransactionId();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: " + cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10014", "The key used is not active for used domain " + domain + " ! Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            return;
        }

        if (cmd == null || cmd.length() <= 0) {
            String transactionId = getTransactionId();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: " + cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10011", "Error occured while processing. Please check parameter 'Cmd'.");
            return;
        }
        doOperation(request, out, cmd, typ, key, callback);
       }catch (Exception ex){
          Utils.showError("doPost " + ex.getMessage());
       }finally{
         DbConn.closeHttpConn(null,null, out, null);
      }
        return;
    } // doPost()

    //-----------------------------------------------------------------------------

    private void doOperation(HttpServletRequest request, PrintWriter out, String cmd, String typ, String key, String callback) {
        String transactionId = getTransactionId();

        if (cmd.equalsIgnoreCase("API"))
            doApi(request, out, typ, key, transactionId);
        else if(cmd.equalsIgnoreCase("KEY")){
            doKeyControl(request,out,key,transactionId);}
        else if(cmd.equalsIgnoreCase("KEYCREATE")){
            doKeyCreate(request,out,key,transactionId);}
        else if(cmd.equalsIgnoreCase("KEYUPDATE")){
            doKeyUpdate(request,out,key,transactionId);}
        else if (cmd.equalsIgnoreCase("APIV2"))
            doApiV2(request, out, typ, key, transactionId);
        else if (cmd.equalsIgnoreCase("API3D"))
            doApi3d(request, out, typ, key, transactionId);
        else if (cmd.equalsIgnoreCase("APIINFO"))
            doApiInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETVERSION"))
            doGetVersion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETMYPACKAGES"))
            doGetMyPackages(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ILLIST"))
            doIlList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ILLISTWITHEXTENT"))
            doIlListWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ILCELIST"))
            doIlceList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ILCELISTWITHEXTENT"))
            doIlceListWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("MAHALLELIST"))
            doMahalleList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("MAHALLELISTWITHEXTENT"))
            doMahalleListWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("YOLLIST"))
            doYolList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("YOLLISTWITHEXTENT"))
            doYolListWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("YOL2SHAPEWITHEXTENT"))
            doYol2ShapeWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETNEARESTYOL"))
            doGetNearestYol(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("KAPILIST"))
            doKapiList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("KAPILISTWITHEXTENT"))
            doKapiListWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETILINFO"))
            doGetIlInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETILCEINFO"))
            doGetIlceInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETMAHALLEINFO"))
            doGetMahalleInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETYOLINFO"))
            doGetYolInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETKAPIINFO"))
            doGetKapiInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETBAGIMSIZBIRIM"))
            doGetBagimsizBirim(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETBAGIMSIZBIRIMLIST"))
            doGetBagimsizBirimList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETCADDEADRESKODU"))
            doGetCaddeAdresKodu(request, out, typ, key, transactionId, callback);     
        else if (cmd.equalsIgnoreCase("GETMAHALLEADRESKODU"))
            doGetMahalleAdresKodu(request, out, typ, key, transactionId, callback);     
        else if (cmd.equalsIgnoreCase("CATEGORYLIST"))
            doCategoryList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("BRANDLIST"))
            doBrandList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("POICOUNT"))
            doPoiCount(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("POICOUNTWITHEXTENT"))
            doPoiCountWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETPOI"))
            doGetPoi(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETPOIGROUP"))
            doGetPoiGroup(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("POILIST"))
            doPoiList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("POILISTEX"))
            doPoiListEx(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("POISEARCH"))
            doPoiSearch(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("POISEARCHWITHEXTENT"))
            doPoiSearchWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("POISEARCHEX"))
            doPoiSearchEx(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("POISEARCHEXWITHEXTENT"))
            doPoiSearchExWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETCOORDINATE"))
            doGetCoordinate(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETELEVATION"))
            doGetElevation(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETSUGGESTIONS"))
            doGetSuggestions(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ORDERBYDISTANCE"))
            doOrderByDistance(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("DISTANCEMATRIX"))
            doDistanceMatrix(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("DISTANCE"))
            doDistance(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GEOCODE"))
            doGeocode(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GLOBALGEOCODE"))
            doGlobalGeocode(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GLOBALROUTE"))
            doGlobalRoute(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ROTA"))
            doRota(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ROUTE"))
            doRota(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ONROUTE"))
            doOnRoute(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("MAP"))
            doMap(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDUSERPOINT"))
            doAddUserPoint(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDUSERPOINTS"))
            doAddUserPoints(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETUSERPOINT"))
            doGetUserPoint(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("REMOVEUSERPOINT"))
            doRemoveUserPoint(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("USERPOINTLIST"))
            doUserPointList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("USERPOINTCOUNTLIST"))
            doUserPointListCount(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("USERPOINTCOUNTSEARCH"))
            doUserPointCountSearch(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("USERPOINTLISTWITHEXTENT"))
            doUserPointListWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("USERPOINTCOUNTLISTWITHEXTENT"))
            doUserPointListCountWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDUSERREGIONBYUNION"))
            doAddUserRegionByUnion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDUSERREGION"))
            doAddUserRegion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDUSERREGIONS"))
            doAddUserRegions(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDBUFFEREDLINETOUSERREGION"))
            doBufferedLineToUserRegion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDBUFFEREDROUTETOUSERREGION"))
            doBufferedRouteToUserRegion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETUSERREGION"))
            doGetUserRegion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("USERREGIONSEARCH"))
            doUserRegionSearch(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("REMOVEUSERREGION"))
            doRemoveUserRegion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("USERREGIONLIST"))
            doUserRegionList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("UPLOADREGION"))
            doUploadRegion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDUSERLINE"))
            doAddUserLine(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDUSERLINES"))
            doAddUserLines(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETUSERLINE"))
            doGetUserLine(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("REMOVEUSERLINE"))
            doRemoveUserLine(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("USERLINELIST"))
            doUserLineList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDROUTETOUSERLINE"))
            doAddRouteToUserLine(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("CAMPCATEGORYLIST"))
            doCampCategoryList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("CAMPCAMPAIGNLIST"))
            doCampCampaignList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("CAMPNEARCAMPAIGNS"))
            doCampNearCampaigns(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("DEALCATEGORYLIST"))
            doDealCategoryList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("DEALDEALLIST"))
            doDealDealList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("DEALNEARDEALS"))
            doDealNearDeals(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TRAFFICEVENTSEARCH"))
            doTrafficEventSearch(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TRAFFICINFO"))
            doTrafficInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TRAFFICTMCFLOWDATA"))
            doTrafficTmcFlowData(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TRAFFICSTARTLIST"))
            doTrafficStartList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TRAFFICENDLIST"))
            doTrafficEndList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TRAFFICROUTE"))
            doTrafficRoute(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TRAFFICROUTELIST"))
            doTrafficRouteList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETDEMOGRAPHICINFO"))
            doGetDemographicInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDUSERIMAGE"))
            doAddUserImage(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("REMOVEUSERIMAGE"))
            doRemoveUserImage(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETRISKSKOR"))
            doGetRiskSkor(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ADDSERVICEAREATOUSERREGION"))
            doAddServiceAreaToUserRegion(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("SOCIALEVENTLIST"))
            doSocialEventList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("SOCIALEVENTSEARCH"))
            doSocialEventSearch(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TMCHATINFO"))
            doTmcHatInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TMCHATLISTWITHEXTENT"))
            doTmcHatListWithExtent(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("TMCHATSEARCH"))
            doTmcHatSearch(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("OPTMATRIX"))
            doOptMatrix(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("OPTREQUEST"))
            doOptRequest(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("OPTRESULT"))
            doOptResult(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("SPATIALANALYSIS"))
            doSpatialAnalysis(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ENCODEGEOMETRY"))
            doEncodeGeometry(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("DECODEGEOMETRY"))
            doDecodeGeometry(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("ENCODELEVEL"))
            doEncodeLevel(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("DECODELEVEL"))
            doDecodeLevel(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("WEATHERREPORT"))
            doWeatherReport(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("SETPOIATTRIBUTE"))
            doSetPoiAttribute(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("REMOVEPOIATTRIBUTE"))
            doRemovePoiAttribute(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("INCREMENTPOIATTRIBUTE"))
            doIncrementPoiAttribute(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("DECREMENTPOIATTRIBUTE"))
            doDecrementPoiAttribute(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("CLUSTERPOINTS"))
            doClusterPoints(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETPHONEADDRESSSCORE"))
            doGetPhoneAddressScore(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETNEARESTPOINTONYOL"))
            doGetNearestPointOnYol(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETKEYSTATUS"))
            doGetKeyStatus(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETMAXYOLSPEED"))
            doGetMaxYolSpeed(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("GETEARTHQUAKEINFO"))
            doGetEarthQuakeInfo(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("INDOORVENUELIST"))
            doIndoorVenueList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("INDOORPOILIST"))
            doIndoorPoiList(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("INDOORPOISEARCH"))
            doIndoorPoiSearch(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("WHAT3WORDS"))
            doWhat3Words(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("BBOLUMFEEDBACK"))
            doBBolumFeedback(request, out, typ, key, transactionId, callback);
        else if (cmd.equalsIgnoreCase("LISTFEEDBACK"))
            doListFeedback(request, out, typ, key, transactionId, callback);
        else {
            Utils.logLbsServiceRequest(key, transactionId, cmd, request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10001", "Error occured while processing. Please try again later.");
        }

        return;
    } // doOperation()

    //-----------------------------------------------------------------------------

    private void doApi(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId) {
        Utils.logLbsServiceRequest(key, transactionId, "doApi(): ", request.getQueryString(), request.getRemoteAddr());

        if (!typ.equals("JS")) {
            DataResponse.sendErrorResponse(out, typ, transactionId, null, "20099", "Parameter 'Typ' must be given JS (no other option available yet).");
            return;
        }

        String fname = null;
        URL url = null;
        InputStreamReader inp = null;
        String urlString = null;

        char[] cbuf = new char[8192];

        out.println("var gtm = " + (new java.util.Date()).getTime() + ";");
        out.println("var coorSrid = " + Utils.getParameter("fakecoor_srid") + ";");
        out.println("var lboxUrl = '" + request.getRequestURL().toString() + "';");
        out.println("var lboxKey = '" + key + "';");
        String datasrc = Utils.getParameter("mapviewer_map_datasource");
        out.println("var datasrc = '" + datasrc + "';");
        out.println("var datasrcApp = '" + Utils.getParameter("mapviewer_app_datasource") + "';");
        String mapviewerUrl = Utils.getParameter("mapviewer_url");
        if (mapviewerUrl != null) {
            if (request.isSecure() || (request.getRequestURL().toString().startsWith("https"))) {
                mapviewerUrl = (mapviewerUrl.startsWith("https") ? mapviewerUrl : mapviewerUrl.replace("http", "https"));
            }
        }
        out.println("var urlBase = '" + mapviewerUrl + "';");
        out.println("var mapBase = '" + Utils.getParameter("mapviewer_basemap_0") + "';");
        out.println("var transparentMap = '" + Utils.getParameter("transparent_basemap") + "';");
        out.println("var categoryList = [" + Utils.getCategoryList(key) + "];");
        out.println("var brandList = [" + Utils.getBrandList(key) + "];");
        out.println("var packages = [" + Utils.getPackages(key) + "];");
        int inx = 1;

        while (true) {
            String basemap = Utils.getParameter("mapviewer_basemap_" + inx);
            if (basemap == null || basemap.length() <= 0)
                break;

            int posPoint = basemap.indexOf('.');
            if (posPoint < 0)
                basemap = datasrc + "." + basemap;
            out.println("var BASEMAP_BM" + inx + " = '" + basemap + "'");
            inx++;
        } // while()

        if (jsBuf[0] != '\u0000') {
            out.write(jsBuf, 0, jsBufLength);
        }

        else {
            BufferedReader br = null;
            FileReader fr = null;
            try {
                fname = Utils.getParameter("js_combined");
                urlString = request.getRequestURL().toString();
                int pos = urlString.lastIndexOf('/');
                urlString = urlString.substring(0, pos) + "/js/" + fname;
                String jsPath = this.getServletContext().getRealPath("/js/" + fname);

                try {
                    fr = new FileReader(jsPath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Utils.showText("JS Path: " + this.getServletContext().getRealPath("/js/" + fname));
                    Utils.showError(ex.getMessage());
                }
            
                if (fr != null) {
                    try {
                        String sCurrentLine;
                        br = new BufferedReader(fr);

                        while ((sCurrentLine = br.readLine()) != null) {
                            out.write(sCurrentLine + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utils.showText("JS Path: " + this.getServletContext().getRealPath("/js/" + fname));
                        Utils.showError(e.getMessage());
                    }

                } else {
                    try {
                        url = new URL(urlString);
                        inp = new InputStreamReader(url.openStream());
                        while (true) {
                            int count = inp.read(cbuf, 0, cbuf.length);
                            for (int i = 0; i < count; i++) {
                                jsBuf[jsBufLength + i] = cbuf[i];
                            }

                            jsBufLength += count;
                            if (count <= 0)
                                break;
                            out.write(cbuf, 0, count);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                Utils.logInfo("Exception: " + ex.getMessage());
                ex.printStackTrace();
                DataResponse.sendErrorResponse(out, typ, transactionId, null, "20098", "Error occured while processing. Please try again later");
                return;
            } finally {
                DbConn.closeFileConn(fr, null);
                DbConn.closeHttpConn(null, inp, null, br);
            }
        }

        Utils.incrementAccessCount(key,transactionId);
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "API");
        return;
    } // doApi()

    //-----------------------------------------------------------------------------

    private void doApiV2(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId) {
        Utils.logLbsServiceRequest(key, transactionId, "doApiV2(): ", request.getQueryString(), request.getRemoteAddr());

        if (!typ.equals("JS")) {
            DataResponse.sendErrorResponse(out, typ, transactionId, null, "20099", "Parameter 'Typ' must be given JS (no other option available yet).");
            return;
        }

        int apiV2Enabled = Operations.getPackageCounter(key, PACKAGE_APIV2);

        if (apiV2Enabled <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, null, "30011", "APIV2 package support is missing.");
            return;
        }

        String tmp = null;
        int includeJQuery = 1;
        tmp = request.getParameter("IncludeJQuery");
        if (tmp != null && tmp.length() > 0) {
            try {
                includeJQuery = Integer.parseInt(tmp);
            } catch (Exception e) {
                includeJQuery = 1;
                e.printStackTrace();
                Utils.logInfo(e.getMessage());
            }
        }

        String fname = null;
        URL url = null;
        InputStreamReader inp = null;
        BufferedReader br = null;
        String urlString = null;
        FileReader fr = null;

        char[] cbuf = new char[8192];

        out.println("var gtm = " + (new java.util.Date()).getTime() + ";");
        out.println("var coorSrid = " + Utils.getParameter("fakecoor_srid") + ";");
        out.println("var lboxUrl = '" + request.getRequestURL().toString() + "';");
        out.println("var lboxKey = '" + key + "';");
        String datasrc = Utils.getParameter("mapviewer_map_datasource");
        out.println("var datasrc = '" + datasrc + "';");
        out.println("var datasrcApp = '" + Utils.getParameter("mapviewer_app_datasource") + "';");
        String mapviewerUrl = Utils.getParameter("mapviewer_url");
        if (mapviewerUrl != null) {
            if (request.isSecure() || (request.getRequestURL().toString().startsWith("https"))) {
                mapviewerUrl = (mapviewerUrl.startsWith("https") ? mapviewerUrl : mapviewerUrl.replace("http", "https"));
            }
        }        
        out.println("var urlBase = '" + mapviewerUrl + "';");
        out.println("var mapBase = '" + Utils.getParameter("mapviewer_basemap_0") + "';");
        out.println("var transparentMap = '" + Utils.getParameter("transparent_basemap") + "';");
        out.println("var categoryList = [" + Utils.getCategoryList(key) + "];");
        out.println("var brandList = [" + Utils.getBrandList(key) + "];");
        out.println("var packages = [" + Utils.getPackages(key) + "];");
        int inx = 1;

        while (true) {
            String basemap = Utils.getParameter("mapviewer_basemap_" + inx);
            if (basemap == null || basemap.length() <= 0)
                break;

            int posPoint = basemap.indexOf('.');
            if (posPoint < 0)
                basemap = datasrc + "." + basemap;
            out.println("var BASEMAP_BM" + inx + " = '" + basemap + "';");
            inx++;
        } // while()

        if (jsv2Buf[0] != '\u0000' && includeJQuery > 0) {
            out.write(jsv2Buf, 0, jsv2BufLength);
        }

        else {
            try {
                if (includeJQuery > 0)
                    fname = Utils.getParameter("js_combined_v2");
                else
                    fname = Utils.getParameter("js_combined_v2_nojquery");
                urlString = request.getRequestURL().toString();
                int pos = urlString.lastIndexOf('/');
                urlString = urlString.substring(0, pos) + "/jsv2/" + fname;
                String jsPath = this.getServletContext().getRealPath("/jsv2/" + fname);

                try {
                    fr = new FileReader(jsPath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Utils.showError(ex.getMessage());
                    Utils.showText("JS Path: " + jsPath);
                }

                if (fr != null) {
                    try {
                        String sCurrentLine;
                        br = new BufferedReader(fr);

                        while ((sCurrentLine = br.readLine()) != null) {
                            out.write(sCurrentLine + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utils.showError(e.getMessage());
                        Utils.showText("JS Path: " + jsPath);
                    }

                } else {
                    try {
                        url = new URL(urlString);
                        inp = new InputStreamReader(url.openStream());
                        while (true) {
                            int count = inp.read(cbuf, 0, cbuf.length);
                            for (int i = 0; i < count; i++) {
                                jsv2Buf[jsv2BufLength + i] = cbuf[i];
                            }

                            jsv2BufLength += count;
                            if (count <= 0)
                                break;
                            out.write(cbuf, 0, count);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                Utils.logInfo("Exception: " + ex.getMessage());
                ex.printStackTrace();
                DataResponse.sendErrorResponse(out, typ, transactionId, null, "20098", "Error occured while processing. Please try again later");
                return;
            } finally {
                DbConn.closeHttpConn(null, inp, null, br);
                DbConn.closeFileConn(fr, null);
            }
        }

        Utils.incrementAccessCount(key,transactionId);
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "APIV2");
        return;
    } // doApiV2()

    //-----------------------------------------------------------------------------
    
    private void doKeyControl(HttpServletRequest request, PrintWriter out, String key, String transactionId){

        boolean keyControl = Operations.getKeyControl(key);

        if (keyControl) {
            out.println(true);
            return;
        }else{
            out.println(false);
            return; 
        }
               
    }
    
    //------------------------------------------------------------------------------------
    private void doKeyCreate(HttpServletRequest request, PrintWriter out, String key, String transactionId){
        Utils.logLbsServiceRequest(key, transactionId, "doKeyCreate():", request.getQueryString(), request.getRemoteAddr());
        
        Timestamp expireDate=null;
        String fName = (request.getParameter("fName") != null ? request.getParameter("fName"):"");
        String lName = (request.getParameter("lName") != null ? request.getParameter("lName"):"");
        String description = (request.getParameter("description") != null ? request.getParameter("description"):"");
        if(description.equals("-")) {
             description = null;
        }
        String email = (request.getParameter("email") != null ? request.getParameter("email"):"");
        String city = (request.getParameter("city") != null ? request.getParameter("city"):"");
        String company = (request.getParameter("company") != null ? request.getParameter("company"):"");
        
        PreparedStatement pstmt=null;
        ResultSet rset = null;
        Connection conn = null;

        if(fName.length() == 0 || lName.length() == 0 ||  email.length() == 0 || city.length() == 0) {
            DataResponse.sendErrorResponse(out, "JSON", transactionId, null, "1111", "Create key parameters must be entered");
            return;
        } else {    
            
          try {  
            conn = DbConn.getPooledConnection();
            String emailControlSql="SELECT COUNT(*) FROM LBS_KEYS WHERE EMAIL = '"+email+"'";
            pstmt = conn.prepareStatement(emailControlSql);          
            rset=pstmt.executeQuery();
            rset.next();
            long cnt = rset.getLong("COUNT(*)");
            if( cnt > 0 ) {
              try {
                  DbConn.closeDBConnection(pstmt, rset);
                  DbConn.closeConnection(conn);
                  DataResponse.sendErrorResponse(out, "JSON", transactionId, null, "1111", "You got a key with this e-mail address before");

                 return;
              } catch(Exception ex){
                ex.printStackTrace();
                Utils.showError("Exception on checking e-mail address " + email);
                DataResponse.sendErrorResponse(out, "JSON", transactionId, null, "1111", "Create Key fail");
                return;

              }
            } else {
              String couponId = "X010061053";
              Random ran = new Random();
              String customerId = String.valueOf(ran.nextInt(999999999 - 111111111 + 1) + 111111111);
              String letter = couponId.substring(0, 1); // Letter of coupon
              String hiNibble = couponId.substring(1, 5); // First nibble of coupon (four numeric value)
              String loNibble = couponId.substring(5, 10); // Last nibble of coupon (five numeric value)
              hiNibble = padLeft(Integer.parseInt(hiNibble) * 193, 7);
              loNibble = padLeft(Integer.parseInt(loNibble) * 37, 7);
              customerId = padLeft(customerId, 32);
              Calendar now = Calendar.getInstance();
              String dt = "" + padLeft(now.get(Calendar.MONTH) + 1, 2) + padLeft(now.get(Calendar.MINUTE), 2) + padLeft(now.get(Calendar.SECOND), 2);
              dt = padLeft(Integer.parseInt(dt) * 29, 8);
              int[] inx = {
                9, 11, 20, 30, 38, 41, 37, 1, 44, 15, 31, 32, 45, 6, 14, 48, 25, 34, 52, 49,
                8,  23, 40, 3, 29, 13, 36, 50, 43, 54, 17, 7, 46, 33, 51, 47, 18, 26, 4, 24,
                22, 10, 39, 28, 2, 21, 0, 12, 42, 16, 35, 53, 27, 5, 19
              };
              String idAll = loNibble + letter + dt + hiNibble + customerId;
              StringBuffer str = new StringBuffer();
              for( int i = 0; i < 7 + 1 + 7 + 8  + 32; i++ ) str.append(idAll.charAt(inx[i]));
              String newKey = str.toString();
              Utils.showText("Key: " + newKey);
            
              Timestamp currentTime = new Timestamp(now.getTime().getTime());
              now.add(Calendar.YEAR,1);
              Timestamp expireTime = new Timestamp(now.getTime().getTime());
              expireDate=expireTime;
              String sql = "INSERT INTO LBS_KEYS (KEY, TIME_STAMP, SURNAME, NAME, EMAIL, REQUEST_LIMIT, ACTIVE, DESCRIPTION, CITY, COMPANY , ";
              sql += "PRIVILEGE, EXPIRE_DATE, REQUEST_COUNT, FLAGS ) ";
              sql += "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
              PreparedStatement prsServ = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
              prsServ.setString(1,newKey);
              prsServ.setTimestamp(2,currentTime);
              prsServ.setString(3,fName);
              prsServ.setString(4,lName);
              prsServ.setString(5,email);
              prsServ.setLong(6,5000);
              prsServ.setLong(7,1);
              prsServ.setString(8,description);
              prsServ.setString(9,city);
              prsServ.setString(10,company);
              prsServ.setLong(11,7);
              prsServ.setTimestamp(12,expireTime);
              prsServ.setLong(13,0);
              prsServ.setLong(14,0);
              prsServ.executeUpdate();
              key=newKey;
                
             DbConn.closeDBConnection(prsServ, null);
                
             sql="INSERT INTO lbs_key_package(package_name,EXPIRE_DATE,KEY,ACTIVE) " + 
             "select distinct(package_name),?,?,1 from lbs_key_package";   
             System.out.println(sql);
             PreparedStatement prsPackage = conn.prepareStatement(sql);
             prsPackage.setTimestamp(1,expireDate);
             prsPackage.setString(2,key);
             prsPackage.executeUpdate();
             
            DbConn.closeDBConnection(prsPackage, null);
                
            }
          } catch(Exception ex) {
            ex.printStackTrace();        
            out.println("{res:1, message: 'exception'}");
            Utils.showError("Exception " + ex.getMessage() + "\n Exception Generation key for =  " + fName + " " + lName + " " + email);
            return;
          } finally {
              DbConn.closeDBConnection(pstmt, rset);
              DbConn.closeConnection(conn);     
          }
          
        }
        
        DataKeyStatus dks = Operations.getKeyStatus(key);
        DataPackage[] dps = Operations.getMyPackages(key);

        if (dks == null) {
            DataResponse.sendErrorResponse(out, "JSON", transactionId, null, "20901", "Key cannot be found.");
            return;
        }

        DataResponse.sendSuccessResponse(out, "JSON", transactionId, null, dks, dps);
        return;
        
    }

    //-----------------------------------------------------------------------------
    private void doKeyUpdate(HttpServletRequest request, PrintWriter out, String key, String transactionId){
        
        Utils.logLbsServiceRequest(key, transactionId, "doKeyUpdate():", request.getQueryString(), request.getRemoteAddr());
        String updateKey = (request.getParameter("updateKey") != null ? request.getParameter("updateKey"):"");
        String expireDate = (request.getParameter("expireDate") != null ? request.getParameter("expireDate"):"");
        String limit = (request.getParameter("limit") != null ? request.getParameter("limit"):"");
        String active = (request.getParameter("active") != null ? request.getParameter("active"):"");

        if(updateKey.length() == 0 ) {
            DataResponse.sendErrorResponse(out, "JSON", transactionId, null, "1111", "Update Key parameter must be entered");

            return;
        }
        
        if(expireDate.length() == 0 && limit.length() == 0 && active.length() == 0 ) {
            DataResponse.sendErrorResponse(out, "JSON", transactionId, null, "1111", "parameters must be entered");

            return;
        }
        
        PreparedStatement pstmt = null;
        Connection conn = null;

        boolean flag=false;
        String sql="UPDATE LBS_KEYS SET ";
        if(expireDate!=""){
            sql+= " EXPIRE_DATE=TO_DATE('"+expireDate+" 00:00:00','DD-MM-YYYY HH24:MI:SS') ";
            flag=true;
        }
        
        if(limit!=""){
            if(flag==true){
                sql+=" , ";
            }
            sql+= "REQUEST_LIMIT="+limit+" ";
            flag=true;
        }
        
        if(active!=""){
            if(flag==true){
                sql+=" , ";
            }
            sql+= "ACTIVE="+active+" ";
        }
        
        sql+=" WHERE KEY='"+updateKey+"'";
        try {
            conn = DbConn.getPooledConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            DbConn.closeDBConnection(pstmt, null);
            if(expireDate!=""){
                sql="UPDATE LBS_KEY_PACKAGE SET EXPIRE_DATE=TO_DATE('"+expireDate+" 00:00:00','DD-MM-YYYY HH24:MI:SS') WHERE KEY='"+updateKey+"'";
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();        
            DataResponse.sendErrorResponse(out, "JSON", transactionId, null, "1111", "Update Key exception");
            return;
        }finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(conn);  
        }
        
        DataKeyStatus dks = Operations.getKeyStatus(updateKey);
        DataPackage[] dps = Operations.getMyPackages(updateKey);

        if (dks == null) {
            DataResponse.sendErrorResponse(out, "JSON", transactionId, null, "20901", "Key cannot be found.");
            return;
        }

        DataResponse.sendSuccessResponse(out, "JSON", transactionId, null, dks, dps);
        return;       
    }
    
    //--------------------------------------------------------------------------------
    private void doApi3d(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId) {
        Utils.logLbsServiceRequest(key, transactionId, "doApi3d(): ", request.getQueryString(), request.getRemoteAddr());

        if (!typ.equals("JS")) {
            DataResponse.sendErrorResponse(out, typ, transactionId, null, "20099", "Parameter 'Typ' must be given JS (no other option available yet).");
            return;
        }

        String fname = null;
        URL url = null;
        InputStreamReader inp = null;
        String urlString = null;

        char[] cbuf = new char[8192];

        try {
            out.println("var gtm = " + (new java.util.Date()).getTime() + ";");
            out.println("var unity3dUrl = '" + Utils.getParameter("unity3d_url") + "'; ");
            if (js3dLbBuf[0] == '\u0000') {
                fname = Utils.getParameter("js_3d");
                urlString = request.getRequestURL().toString();
                int pos = urlString.lastIndexOf('/');
                urlString = urlString.substring(0, pos) + "/js3d/" + fname;
                url = new URL(urlString);
                inp = new InputStreamReader(url.openStream());

                while (true) {
                    int count = inp.read(cbuf, 0, cbuf.length);
                    for (int i = 0; i < count; i++) {
                        js3dLbBuf[js3dBufLength + i] = cbuf[i];
                    }

                    js3dBufLength += count;
                    if (count <= 0)
                        break;
                    out.write(cbuf, 0, count);
                }
            } else {
                out.write(js3dLbBuf, 0, js3dBufLength);
            }

        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
            DataResponse.sendErrorResponse(out, typ, transactionId, null, "20098", "Error occured while processing. Please try again later");
            return;
        } finally {
            DbConn.closeHttpConn(null, inp, null, null);
        }

        Utils.incrementAccessCount(key, transactionId);
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "API3D");
        return;
    } // doApi3d()

    //-----------------------------------------------------------------------------

    private void doApiInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doApiInfo()", request.getQueryString(), request.getRemoteAddr());
        String lboxUrl = request.getRequestURL().toString();
        DataApiInfo dai = DataApiInfo.getInstance(lboxUrl, key, dataSrc);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dai);
        return;
    } // doApiInfo()

    //-----------------------------------------------------------------------------

    private void doGetVersion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetVersion()", request.getQueryString(), request.getRemoteAddr());
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "version", PROGRAM_VERSION, "datetime", PROGRAM_DATETIME);
        return;
    } // doGetVersion()

    //-----------------------------------------------------------------------------

    private void doGetMyPackages(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetMyPackages()", request.getQueryString(), request.getRemoteAddr());

        DataPackage[] dps = Operations.getMyPackages(key);
        if (dps == null || dps.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20022", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);
        return;
    } // doGetMyPackages()

    //-----------------------------------------------------------------------------

    private void doIlList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doIlList()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int majorCities = 0;
        tmp = request.getParameter("MajorCities");
        if (tmp != null && tmp.length() > 0)
            try {
                majorCities = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }

        long countryId = 0;
        tmp = request.getParameter("CountryId");
        if (tmp != null && tmp.length() > 0)
            try {
                countryId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
            keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
            ;
        }

        DataIl[] dis = Operations.getIlList(majorCities, countryId, keyword);

        if (dis == null || dis.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20022", "IL not found.");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            for (int i = 0; dis.length > i; i++)
                dis[i].setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dis);
        return;
    } // doIlList()

    //-----------------------------------------------------------------------------

    private void doIlListWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doIlListWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
            keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
            ;
        }

        if (minLatitude == 0.00 || minLongitude == 0.00 || maxLatitude == 0.00 || maxLongitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant extent coordinates.");
            return;
        }

        Extent ext = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        DataIl[] dis = Operations.getIlListWithExtent(ext, keyword);

        if (dis == null || dis.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "IL not found.");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            for (int i = 0; dis.length > i; i++)
                dis[i].setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dis);
        return;
    } // doIlListWithExtent()

    //-----------------------------------------------------------------------------

    private void doIlceList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doIlceList()", request.getQueryString(), request.getRemoteAddr());

        long ilId = 0;
        String tmp = request.getParameter("IlId");
        if (tmp != null && tmp.length() > 0)
            try {
                ilId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }

        String ilAdi = null;
        tmp = request.getParameter("IlAdi");
        if (tmp != null && tmp.length() > 0)
            try {
                ilAdi = Utils.convUtf8ToTurkish(tmp);
            } catch (Exception e) {
                ;
            }
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
           keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
            ;
        }
        
        DataIlce[] dis = Operations.getIlceList(ilId, ilAdi, keyword);

        if (dis == null || dis.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20032", "ILCE not found. (IL ID: " + ilId + ")");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            for (int i = 0; dis.length > i; i++)
                dis[i].setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dis);
        return;
    } // doIlceList()

    //-----------------------------------------------------------------------------

    private void doIlceListWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doIlceListWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (minLatitude == 0.00 || minLongitude == 0.00 || maxLatitude == 0.00 || maxLongitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant extent coordinates.");
            return;
        }
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
            keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
            ;
        }
        
        Extent ext = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        DataIlce[] dis = Operations.getIlceListWithExtent(ext, keyword);

        if (dis == null || dis.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "ILCE not found.");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            for (int i = 0; dis.length > i; i++)
                dis[i].setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dis);
        return;
    } // doIlceListWithExtent()

    //-----------------------------------------------------------------------------

    private void doMahalleList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doMahalleList()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long ilceId = 0;
        tmp = request.getParameter("IlceId");
        if (tmp != null && tmp.length() > 0)
            try {
                ilceId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        boolean isAllMahalle = false;
        tmp = request.getParameter("MahalleList");
        if( tmp == null )  tmp = request.getParameter("mahalleList");
        try {
            isAllMahalle = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        String ilAdi = null;
        tmp = request.getParameter("IlAdi");
        if (tmp != null && tmp.length() > 0)
            try {
                ilAdi = Utils.convUtf8ToTurkish(tmp);
            } catch (Exception e) {
                ;
            }
        String ilceAdi = null;
        tmp = request.getParameter("IlceAdi");
        if (tmp != null && tmp.length() > 0)
            try {
                ilceAdi = Utils.convUtf8ToTurkish(tmp);
            } catch (Exception e) {
                ;
            }
        int type = 0;
        tmp = request.getParameter("Type");
        if (tmp != null && tmp.length() > 0)
            try {
                type = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
           keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
            ;
        }
        
        DataMahalle[] dms = null;
        if( isAllMahalle ){
          dms = Operations.getMahalleList(type, keyword);
        }
        else {
           dms = Operations.getMahalleList(ilceId, ilAdi, ilceAdi, type, keyword);   
        }
        if (dms == null || dms.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "MAHALLE not found. (ILCE ID: " + ilceId + ")");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            for (int i = 0; dms.length > i; i++)
                dms[i].setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dms);
        return;
    } // doMahalleList()

    //-----------------------------------------------------------------------------

    private void doMahalleListWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doMahalleListWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int type = 0;
        tmp = request.getParameter("Type");
        if (tmp != null && tmp.length() > 0)
            try {
                type = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (minLatitude == 0.00 || minLongitude == 0.00 || maxLatitude == 0.00 || maxLongitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant extent coordinates.");
            return;
        }
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
            keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
            ;
        }
        
        Extent ext = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        DataMahalle[] dms = Operations.getMahalleListWithExtent(ext, type, keyword);
        if (dms == null || dms.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "YOL not found.");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            for (int i = 0; dms.length > i; i++)
                dms[i].setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dms);
        return;
    } // doMahalleListWithExtent()

    //-----------------------------------------------------------------------------

    private void doYolList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doYolList()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        if (tmp != null && tmp.length() > 0)
            try {
                ilId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        long ilceId = 0;
        tmp = request.getParameter("IlceId");
        if (tmp != null && tmp.length() > 0)
            try {
                ilceId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        long mahalleId = 0;
        tmp = request.getParameter("MahalleId");
        if (tmp != null && tmp.length() > 0)
            try {
                mahalleId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        String yolAdi = "";
        tmp = request.getParameter("YolAdi");
        if (tmp != null)
            yolAdi = Utils.toUpperCase(Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp)));

        String ilAdi = null;
        tmp = request.getParameter("IlAdi");
        if (tmp != null && tmp.length() > 0)
            try {
                ilAdi = Utils.convUtf8ToTurkish(tmp);
            } catch (Exception e) {
                ;
            }
        String ilceAdi = null;
        tmp = request.getParameter("IlceAdi");
        if (tmp != null && tmp.length() > 0)
            try {
                ilceAdi = Utils.convUtf8ToTurkish(tmp);
            } catch (Exception e) {
                ;
            }
        String mahalleAdi = null;
        tmp = request.getParameter("MahalleAdi");
        if (tmp != null && tmp.length() > 0)
            try {
                mahalleAdi = Utils.convUtf8ToTurkish(tmp);
            } catch (Exception e) {
                ;
            }

        if (ilId == 0 && ilceId == 0 && mahalleId == 0) {
            if (ilAdi == null || ilceAdi == null || mahalleAdi == null) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "('IlId' or 'IlceId' or 'MahalleId') or ('IlAdi' and 'IlceAdi' and 'MahalleAdi') must be supplied.");
                return;
            }
        }

        DataYol[] dys = Operations.getYolList(ilId, ilceId, mahalleId, ilAdi, ilceAdi, mahalleAdi, yolAdi);
        if (dys == null || dys.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "YOL not found. (MAHALLE ID: " + mahalleId + ")");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0)
            for (int i = 0; dys.length > i; i++)
                dys[i].setAdresKodu(0L);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dys);
        return;
    } // doYolList()

    //-----------------------------------------------------------------------------

    private void doYolListWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doYolListWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (minLatitude == 0.00 || minLongitude == 0.00 || maxLatitude == 0.00 || maxLongitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant extent coordinates.");
            return;
        }
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
            keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
            ;
        }

        Extent ext = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        DataYol[] dys = Operations.getYolListWithExtent(ext, keyword);
        if (dys == null || dys.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "YOL not found.");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0)
            for (int i = 0; dys.length > i; i++)
                dys[i].setAdresKodu(0L);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dys);
        return;
    } // doYolListWithExtent()

    //-----------------------------------------------------------------------------

    private void doYol2ShapeWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doYol2ShapeWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (minLatitude == 0.00 || minLongitude == 0.00 || maxLatitude == 0.00 || maxLongitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant extent coordinates.");
            return;
        }

        int count = Operations.getPackageCounter(key, PACKAGE_YOL2SHAPE);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "YOL2SHAPE package support is missing.");
            return;
        }

        double maxArea = Double.parseDouble(Utils.getParameter("yol2shape_maxarea"));

        Extent ext = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        double area = Utils.getExtentArea(ext);
        if (area < 0.00 || area > maxArea) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20044", "Illegal or max extent area exceeded.");
            return;
        }

        String fname = "" + (new Date()).getTime();
        String path = Utils.getParameter("savefile_path") + "/" + fname;
        String zipFileName = Operations.getYol2ShapeWithExtent(path, ext);
        if (zipFileName == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "No data found.");
            return;
        }

        String urlPath = Utils.getParameter("savefileurl_prefix") + "/" + fname + ".zip";

        Utils.incrementAccessCount(key, transactionId, count > 0 ? count : 1);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "url", urlPath);
        return;
    } // doYol2ShapeWithExtent()

    //-----------------------------------------------------------------------------

    private void doKapiList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doKapiList()", request.getQueryString(), request.getRemoteAddr());

        long yolId = 0;
        String tmp = request.getParameter("YolId");
        if (tmp != null && tmp.length() > 0)
            try {
                yolId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        
        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        
       boolean geometry = false;
       tmp = request.getParameter("Geometry");
       try {
           geometry = (Integer.parseInt(tmp) != 0);
       } catch (Exception e) {
           ;
       }
       int count = 0;
       if (geometry)
           count = Operations.getPackageCounter(key, PACKAGE_KAPI_GEOMETRISI);
       
        DataKapi[] dks = Operations.getKapiList(yolId, (count > 0), encode);
        if (dks == null || dks.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "KAPI not found. (YOL ID: " + yolId + ")");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0)
            for (int i = 0; dks.length > i; i++)
                dks[i].setAdresKodu(0L);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dks);
        return;
    } // doKapiList()

    //-----------------------------------------------------------------------------

    private void doKapiListWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doKapiListWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (minLatitude == 0.00 || minLongitude == 0.00 || maxLatitude == 0.00 || maxLongitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant extent coordinates.");
            return;
        }

        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        
        boolean geometry = false;
        tmp = request.getParameter("Geometry");
        try {
           geometry = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
           ;
        }
        int count = 0;
        if (geometry)
           count = Operations.getPackageCounter(key, PACKAGE_KAPI_GEOMETRISI);

        Extent ext = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        DataKapi[] dks = Operations.getKapiListWithExtent(ext, (count > 0), encode);
        if (dks == null || dks.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "YOL not found.");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0)
            for (int i = 0; dks.length > i; i++)
                dks[i].setAdresKodu(0L);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dks);
        return;
    } // doKapiListWithExtent()

    //-----------------------------------------------------------------------------

    private void doGetNearestYol(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetNearestYol()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            latitude = 0.00;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            longitude = 0.00;
        }

        if (latitude == 0.00 || longitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20014", "Error occured while processing. Please check parameters 'Radius, Latitude and Longitude'.");
            return;
        }

        DataYol dy = Operations.getNearestYol(latitude, longitude);
        if (dy == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20015", "Yol cannot be found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dy);
        return;
    } // doGetNearestYol()

    //-----------------------------------------------------------------------------

    private void doGetIlInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetIlInfo()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        if (tmp != null && tmp.length() > 0)
            try {
                ilId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        boolean geometry = false;
        tmp = request.getParameter("Geometry");
        try {
            geometry = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        long adresKodu = 0;
        tmp = request.getParameter("AdresKodu");
        if (tmp != null && tmp.length() > 0)
            try {
                adresKodu = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }

        int count = 0;
        if (geometry)
            count = Operations.getPackageCounter(key, PACKAGE_IL_GEOMETRISI);
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
           keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
           ;
        }

        DataIl di = null;
        di = Operations.getIlInfo(ilId, (count > 0), encode, adresKodu, keyword);

        if (di == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "IL not found. (Check IlId or AdresKodu parameters.)");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            di.setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId, count > 0 ? count : 1);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, di);
        return;
    } // doGetIlInfo()

    //-----------------------------------------------------------------------------

    private void doGetIlceInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetIlceInfo()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long ilceId = 0;
        tmp = request.getParameter("IlceId");
        if (tmp != null && tmp.length() > 0)
            try {
                ilceId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        boolean geometry = false;
        tmp = request.getParameter("Geometry");
        try {
            geometry = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }

        long adresKodu = 0;
        tmp = request.getParameter("AdresKodu");
        if (tmp != null && tmp.length() > 0)
            try {
                adresKodu = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
           keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
           ;
        }
        int count = 0;
        if (geometry)
            count = Operations.getPackageCounter(key, PACKAGE_ILCE_GEOMETRISI);
        DataIlce di = Operations.getIlceInfo(ilceId, (count > 0), encode, adresKodu, keyword);
        if (di == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "ILCE not found. (Check AdresKodu or IlceId parameters.)");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            di.setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId, count > 0 ? count : 1);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, di);
        return;
    } // doGetIlceInfo()

    //-----------------------------------------------------------------------------

    private void doGetMahalleInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetMahalleInfo()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long mahalleId = 0;
        tmp = request.getParameter("MahalleId");
        if (tmp != null && tmp.length() > 0)
            try {
                mahalleId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        boolean geometry = false;
        tmp = request.getParameter("Geometry");
        try {
            geometry = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        long adresKodu = 0;
        tmp = request.getParameter("AdresKodu");
        if (tmp != null && tmp.length() > 0)
            try {
                adresKodu = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
           keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
           ;
        }

        int count = 0;
        if (geometry)
            count = Operations.getPackageCounter(key, PACKAGE_MAHALLE_GEOMETRISI);
        DataMahalle dm = Operations.getMahalleInfo(mahalleId, (count > 0), encode, adresKodu, keyword);
        if (dm == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "MAHALLE not found. (Check AdresKodu or MahalleId parameters.)");
            return;
        }
        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            dm.setAdresKodu(0L);
        }

        Utils.incrementAccessCount(key, transactionId, count > 0 ? count : 1);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dm);
        return;
    } // doGetMahalleInfo()

    //-----------------------------------------------------------------------------

    private void doGetYolInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetYolInfo()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long yolId = 0;
        tmp = request.getParameter("YolId");
        if (tmp != null && tmp.length() > 0)
            try {
                yolId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        boolean geometry = false;
        tmp = request.getParameter("Geometry");
        try {
            geometry = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        long adresKodu = 0;
        tmp = request.getParameter("AdresKodu");
        if (tmp != null && tmp.length() > 0)
            try {
                adresKodu = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null && tmp.length() > 0)
        try {
           keyword = Utils.convToUpperEnglishChars(Utils.decodeEscape(tmp));
        } catch (Exception e) {
           ;
        }

        int count = 0;
        if (geometry)
            count = Operations.getPackageCounter(key, PACKAGE_YOL_GEOMETRISI);
        DataYol[] dy = Operations.getYolInfo(yolId, (count > 0), encode, adresKodu, keyword);
        if (dy == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "YOL not found. (Check AdresKodu or YolId parameters.)");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0)
            for (int i = 0; i < dy.length; i++)
                dy[i].setAdresKodu(0L);

        Utils.incrementAccessCount(key, transactionId, count > 0 ? count : 1);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dy);
        return;
    } // doGetYolInfo()

    //-----------------------------------------------------------------------------

    private void doGetKapiInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetKapiInfo()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long yolId = 0;
        tmp = request.getParameter("KapiId");
        if (tmp != null && tmp.length() > 0)
            try {
                yolId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }

        long adresKodu = 0;
        tmp = request.getParameter("AdresKodu");
        if (tmp != null && tmp.length() > 0)
            try {
                adresKodu = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }

        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
                
        boolean geometry = false;
        tmp = request.getParameter("Geometry");
        try {
           geometry = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
           ;
        }
        int count = 0;
        if (geometry)
          count = Operations.getPackageCounter(key, PACKAGE_KAPI_GEOMETRISI);
                         
        DataKapi dk = Operations.getKapiInfo(yolId, adresKodu, (count > 0), encode);
        if (dk == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "KAPI not found. (Check AdresKodu or KapiId parameters.)");
            return;
        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0)
            dk.setAdresKodu(0L);

        Utils.incrementAccessCount(key, transactionId, count > 0 ? count : 1);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dk);
        return;
    } // doGetKapiInfo()


    //-----------------------------------------------------------------------------

    private void doCategoryList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doCategoryList()", request.getQueryString(), request.getRemoteAddr());

        String[] dcs = Operations.getCategoryList(key);
        if (dcs == null || dcs.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Category not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dcs, "categories", "category");
        return;
    } // doCategoryList()

    //-----------------------------------------------------------------------------

    private void doBrandList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doBrandList()", request.getQueryString(), request.getRemoteAddr());

        DataCategory[] dbs = Operations.getBrandList(key);
        if (dbs == null || dbs.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Brand not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dbs);
        return;
    } // doBrandList()

    //-----------------------------------------------------------------------------

    private void doPoiCount(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doPoiCount()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long ilceId = 0;
        tmp = request.getParameter("IlceId");
        try {
            ilceId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long mahalleId = 0;
        tmp = request.getParameter("MahalleId");
        try {
            mahalleId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        String regionId = null;
        tmp = request.getParameter("RegionId");
        try {
            regionId = tmp;
        } catch (Exception e) {
            ;
        }
        String category = null;
        tmp = request.getParameter("Category");
        if (tmp != null)
            category = Utils.toUpperCase(tmp);
        String brand = null;
        tmp = request.getParameter("Brand");
        if (tmp != null)
            brand = Utils.toUpperCase(tmp);

        if (ilId == 0 && ilceId == 0 && mahalleId == 0 && regionId == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'IlId' or 'IlceId' or 'MahalleId' or 'RegionId' must be supplied.");
            return;
        }

        if (category == null && brand == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Category' or 'Brand' must be supplied.");
            return;
        }

        int count = 0;
        count = Operations.getPoiCount(key, ilId, ilceId, mahalleId, regionId, category, brand);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "count", count);
        return;
    } // doPoiCount()

    //-----------------------------------------------------------------------------

    private void doPoiCountWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doPoiCountWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        String category = null;
        tmp = request.getParameter("Category");
        if (tmp != null)
            category = Utils.toUpperCase(tmp);
        String brand = null;
        tmp = request.getParameter("Brand");
        if (tmp != null)
            brand = Utils.toUpperCase(tmp);

        if (minLatitude == 0.0 || minLongitude == 0.0 && maxLatitude == 0.0 && maxLongitude == 0.0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'MinLatitude' and 'MinLongitude' and 'MaxLatitude' and 'MaxLongitude' must be supplied.");
            return;
        }

        if (category == null && brand == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Category' or 'Brand' must be supplied.");
            return;
        }

        int count = 0;
        count = Operations.getPoiCountWithExtent(key, minLatitude, minLongitude, maxLatitude, maxLongitude, category, brand);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "count", count);
        return;
    } // doPoiCount()

    //-----------------------------------------------------------------------------

    private void doGetPoi(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetPoi()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long id = 0;
        tmp = request.getParameter("Id");
        try {
            id = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        String category = null;
        tmp = request.getParameter("Category");
        try {
            category = tmp;
        } catch (Exception e) {
            ;
        }

        String brand = null;
        tmp = request.getParameter("Brand");
        try {
            brand = tmp;
        } catch (Exception e) {
            ;
        }

        long[] idList = null;
        tmp = request.getParameter("IdList");

        if (tmp != null) {
            String[] info = tmp.split(",");
            if (info.length > 1) {
                idList = new long[info.length];
                for (int i = 0; i < info.length; i++)
                    idList[i] = Long.parseLong(info[i]);
            } else {
                idList = new long[1];
                idList[0] = Long.parseLong(tmp);
            }

            int poiCount = Integer.parseInt(Utils.getParameter("poi_count"));
            if (idList.length > poiCount) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "IdList length cannot be more than " + poiCount);
                return;
            }
        }

        if (id == 0 && idList == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error occured while processing. Please check parameter 'Id' or 'IdList'.");
            return;
        }

        DataPoi[] dps = null;
        DataPoi dp = null;

        int count = Operations.getPackageCounter(key, PACKAGE_RAW_POI_COOR);

        if (idList != null) {
            dps = Operations.getPoi(key, idList, category, brand, (count > 0 ? false : isFakeCoorNeeded));
            if (dps == null) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "POI not found.");
                return;
            }
            Utils.incrementAccessCount(key, transactionId, dps.length);
            DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);
        }

        else {
            dp = Operations.getPoi(key, id, category, brand, (count > 0 ? false : isFakeCoorNeeded));
            if (dp == null) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "POI not found.");
                return;
            }
            Utils.incrementAccessCount(key, transactionId);
            DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dp);
        }

        return;
    } // doGetPoi()

    //-----------------------------------------------------------------------------

    private void doGetPoiGroup(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetPoiGroup()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long id = 0;
        tmp = request.getParameter("Id");
        try {
            id = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        if (id == 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error occured while processing. Please check parameter 'Id'.");
            return;
        }

        DataPoi[] dps = null;

        int count = Operations.getPackageCounter(key, PACKAGE_RAW_POI_COOR);

        dps = Operations.getPoiGroup(key, id, (count > 0 ? false : isFakeCoorNeeded));
        if (dps == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "POI not found.");
            return;
        }
        Utils.incrementAccessCount(key, transactionId, dps.length);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);

        return;
    } // doGetPoiGroup()

    //-----------------------------------------------------------------------------

    private void doPoiList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doPoiList()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long ilceId = 0;
        tmp = request.getParameter("IlceId");
        try {
            ilceId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long mahalleId = 0;
        tmp = request.getParameter("MahalleId");
        try {
            mahalleId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        
        double lat = 0.00;
        tmp = request.getParameter("Lat");
        if(tmp == null) tmp = request.getParameter("LAT");
        if(tmp == null) tmp = request.getParameter("Latitude");
        try {
            lat = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double lon = 0.00;
        tmp = request.getParameter("Lon");
        if(tmp == null) tmp = request.getParameter("LON");
        if(tmp == null) tmp = request.getParameter("Longitude");
        try {
            lon = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        
        int radius = 0;
        tmp = request.getParameter("Radius");
        if(tmp == null) tmp = request.getParameter("radius");
        if (tmp != null)
            try {
                radius = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }

        double[] coors = Utils.getTransformedCoors(lat, lon);
        lon = coors[0];
        lat = coors[1];
        
        if( lon>0 && lat>0 && radius<=0)radius = Integer.parseInt(Utils.getParameter("radius"));
        
        String regionId = null;
        tmp = request.getParameter("RegionId");
        if (tmp != null)
            regionId = tmp;

        String poiAdi = null;
        tmp = request.getParameter("PoiAdi");
        if (tmp != null)
            poiAdi = Utils.toUpperCase(Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp)));

        String category = null;
        tmp = request.getParameter("Category");
        if (tmp != null)
            category = Utils.toUpperCase(tmp);
        String brand = null;
        tmp = request.getParameter("Brand");
        if (tmp != null)
            brand = Utils.toUpperCase(tmp);

        String orderName = null;
        tmp = request.getParameter("OrderName");
        if (tmp != null)
            orderName = tmp;
        String orderType = null;
        tmp = request.getParameter("OrderType");
        if (tmp != null)
            orderType = tmp;

        String attName = null;
        tmp = request.getParameter("AttributeOrderName");
        if (tmp != null)
            attName = tmp;
        String attOrder = null;
        tmp = request.getParameter("AttributeOrderType");
        if (tmp != null)
            attOrder = tmp;
        String attWhereClause = "";
        tmp = request.getParameter("AttributeWhereClause");
        if (tmp != null)
            attWhereClause = tmp;
        
        String upperAttWhereClause = attWhereClause.toUpperCase();
        if (upperAttWhereClause.indexOf("SELECT ") >= 0 || upperAttWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        if (ilId == 0 && ilceId == 0 && mahalleId == 0 && regionId == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'IlId' or 'IlceId' or 'MahalleId' or 'RegionId' must be supplied.");
            return;
        }

        if (poiAdi == null && category == null && brand == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'PoiAdi' or 'Category' or 'Brand' must be supplied.");
            return;
        }

        int count = Operations.getPackageCounter(key, PACKAGE_RAW_POI_COOR);
        DataPoi[] dps = Operations.getPoiList(key, ilId, ilceId, mahalleId, poiAdi, category, brand, orderName, orderType, attName, attOrder, attWhereClause, (count > 0 ? false : isFakeCoorNeeded), regionId, lat, lon, radius);
        if (dps == null || dps.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "POI not found.");
            return;
        }

        if (count < dps.length)
            count = dps.length;

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);
        return;
    } // doPoiList()

    //-----------------------------------------------------------------------------

    private void doPoiListEx(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doPoiListEx()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long ilceId = 0;
        tmp = request.getParameter("IlceId");
        try {
            ilceId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long mahalleId = 0;
        tmp = request.getParameter("MahalleId");
        try {
            mahalleId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        String regionId = null;
        tmp = request.getParameter("RegionId");
        if (tmp != null)
            regionId = tmp;
        String poiAdi = null;

        tmp = request.getParameter("PoiAdi");
        if (tmp != null)
            poiAdi = Utils.toUpperCase(Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp)));
        String exType = null;

        tmp = request.getParameter("ExType");
        if (tmp != null)
            exType = Utils.toUpperCase(tmp);

        String orderName = null;
        tmp = request.getParameter("OrderName");
        if (tmp != null)
            orderName = tmp;
        String orderType = null;
        tmp = request.getParameter("OrderType");
        if (tmp != null)
            orderType = tmp;

        String attOrderName = null;
        tmp = request.getParameter("AttributeOrderName");
        if (tmp != null)
            attOrderName = tmp;
        String attOrderType = null;
        tmp = request.getParameter("AttributeOrderType");
        if (tmp != null)
            attOrderType = tmp;
        String attWhereClause = null;
        tmp = request.getParameter("AttributeWhereClause");
        if (tmp != null)
            attWhereClause = tmp;


        if (ilId == 0 && ilceId == 0 && mahalleId == 0 && regionId == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'IlId' or 'IlceId' or 'MahalleId' or 'RegionId' must be supplied.");
            return;
        }

        if (exType == null || exType.length() <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'ExType' parameter must be supplied.");
            return;
        }

        int count = 0;
        String category = null;
        if (exType.equals(EXTYPE_NOBETCI_ECZANE)) {
            count = Operations.getPackageCounter(key, PACKAGE_NOBETCI_ECZANE);
            if (count > 0)
                category = "EX_" + PACKAGE_NOBETCI_ECZANE;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Nobetci Eczane package support is missing.");
                return;
            }
        } else if (exType.equals(EXTYPE_AKARYAKIT_FIYATLARI)) {
            count = Operations.getPackageCounter(key, PACKAGE_AKARYAKIT_FIYATLARI);
            if (count > 0)
                category = "EX_" + PACKAGE_AKARYAKIT_FIYATLARI;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Akaryakit Fiyatlari package support is missing.");
                return;
            }
        } else if (exType.equals(EXTYPE_OTOPARK_FIYATLARI)) {
            count = Operations.getPackageCounter(key, PACKAGE_OTOPARK_FIYATLARI);
            if (count > 0)
                category = "EX_" + PACKAGE_OTOPARK_FIYATLARI;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Otopark Fiyatlari package support is missing.");
                return;
            }
        } else if (exType.equals(EXTYPE_NOBETCI_NOTER)) {
            count = Operations.getPackageCounter(key, PACKAGE_NOBETCI_NOTER);
            if (count > 0)
                category = "EX_" + PACKAGE_NOBETCI_NOTER;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Nobetci Noter package support is missing.");
                return;
            }             
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30021", "Illegal Extended info type.");
            return;
        }

        int countRawPoiCoor = Operations.getPackageCounter(key, PACKAGE_RAW_POI_COOR);
        DataPoi[] dps =
            Operations.getPoiListEx(key, ilId, ilceId, mahalleId, poiAdi, category, orderName, orderType, attOrderName, attOrderType, attWhereClause, (countRawPoiCoor > 0 ? false : isFakeCoorNeeded), regionId);
        if (dps == null || dps.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20014", "Extended POI not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);
        return;
    } // doPoiListEx()

    //-----------------------------------------------------------------------------

    private void doPoiSearch(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doPoiSearch()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        
        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        int radius = 0;
        tmp = request.getParameter("Radius");
        int maxRadius = Integer.parseInt(Utils.getParameter("radius"));
        if (tmp != null) {
            try {
                radius = Integer.parseInt(tmp);
                if ((radius > maxRadius) || (radius == 0))
                    radius = maxRadius;
            } catch (Exception e) {
                ;
            }
        } else {
            radius = maxRadius;
        }
        int poiCount = 100;
        tmp = request.getParameter("Count");
        if(tmp == null) tmp = request.getParameter("count");
        if (tmp != null){
            try {
             poiCount = Integer.parseInt(tmp);
            } catch (Exception e) {;}
        }else{
          poiCount = Integer.parseInt(Utils.getParameter("poi_count"));
        }
        int markerName = 0;
        tmp = request.getParameter("markerName");
        try {
            if( tmp == null) tmp = request.getParameter("MarkerName"); 
            markerName = Integer.parseInt(tmp);
        } catch (Exception e) { ; }
        
        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null)
            keyword = Utils.convUtf8ToTurkish(tmp);
        String category = null;
        tmp = request.getParameter("Category");
        if (tmp != null)
            category = tmp;
        String brand = null;
        tmp = request.getParameter("Brand");
        if (tmp != null)
            brand = tmp;
        
        int brandGroup = 0;
        tmp = request.getParameter("BrandGroup");
        if(tmp == null) tmp = request.getParameter("brandGroup");
        if (tmp != null){
            try {
              brandGroup = Integer.parseInt(tmp);
            } catch (Exception e) {;}
        }

        String attOrderName = null;
        tmp = request.getParameter("AttributeOrderName");
        if (tmp != null)
            attOrderName = tmp;
        String attOrderType = null;
        tmp = request.getParameter("AttributeOrderType");
        if (tmp != null)
            attOrderType = tmp;
        String attWhereClause = null;
        tmp = request.getParameter("AttributeWhereClause");
        if (tmp != null)
            attWhereClause = tmp;

        if (category == null && brand == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Category' or 'Brand' must be supplied.");
            return;
        }
        
        int count = Operations.getPackageCounter(key, PACKAGE_RAW_POI_COOR);
        DataPoi[] dps = Operations.getPoiSearch(key, latitude, longitude, radius, keyword, category, brand, attOrderName, attOrderType, attWhereClause, (count > 0 ? false : isFakeCoorNeeded),markerName, poiCount, brandGroup);
        if (dps == null || dps.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Poi not found.");
            return;
        }
        
        if(brandGroup==1){
           LinkedHashSet <String> hs = new LinkedHashSet <String>();
           ArrayList<DataPoi> list = new ArrayList<DataPoi>();
           for(DataPoi elm: dps) {
               if(hs.add(elm.getBrandName())==true) {
                   list.add(elm);
               }   
           }
           dps = new DataPoi[list.size()];
           list.toArray(dps); 
        }
        
        if (count < dps.length)
            count = dps.length;
        
        Utils.incrementAccessCount(key, transactionId, count);
        if(brandGroup==1){
          DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps, brandGroup);   
        }else{
          DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);   
        }
        return;
    } // doPoiSearch()
   
    //-----------------------------------------------------------------------------

    private void doPoiSearchEx(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doPoiSearchEx()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        int radius = 0;
        tmp = request.getParameter("Radius");
        if (tmp != null)
            try {
                radius = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        else
            radius = Integer.parseInt(Utils.getParameter("radius"));

        int maxRadius = Integer.parseInt(Utils.getParameter("radius"));
        if ((radius > maxRadius) || (radius == 0))
            radius = maxRadius;

        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null)
            keyword = Utils.convUtf8ToTurkish(tmp);
        String exType = null;

        tmp = request.getParameter("ExType");
        if (tmp != null)
            exType = Utils.toUpperCase(tmp);

        String attOrderName = null;
        tmp = request.getParameter("AttributeOrderName");
        if (tmp != null)
            attOrderName = tmp;
        String attOrderType = null;
        tmp = request.getParameter("AttributeOrderType");
        if (tmp != null)
            attOrderType = tmp;
        String attWhereClause = null;
        tmp = request.getParameter("AttributeWhereClause");
        if (tmp != null)
            attWhereClause = tmp;

        if (exType == null || exType.length() <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'ExType' parameter must be supplied.");
            return;
        }

        int count = 0;
        String category = null;
        if (exType.equals(EXTYPE_NOBETCI_ECZANE)) {
            count = Operations.getPackageCounter(key, PACKAGE_NOBETCI_ECZANE);
            if (count > 0)
                category = "EX_" + PACKAGE_NOBETCI_ECZANE;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Nobetci Eczane package support is missing.");
                return;
            }
        } else if (exType.equals(EXTYPE_AKARYAKIT_FIYATLARI)) {
            count = Operations.getPackageCounter(key, PACKAGE_AKARYAKIT_FIYATLARI);
            if (count > 0)
                category = "EX_" + PACKAGE_AKARYAKIT_FIYATLARI;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Akaryakit Fiyatlari package support is missing.");
                return;
            }
        } else if (exType.equals(EXTYPE_OTOPARK_FIYATLARI)) {
            count = Operations.getPackageCounter(key, PACKAGE_OTOPARK_FIYATLARI);
            if (count > 0)
                category = "EX_" + PACKAGE_OTOPARK_FIYATLARI;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Otopark Fiyatlari package support is missing.");
                return;
            }
        } else if (exType.equals(EXTYPE_NOBETCI_NOTER)) {
            count = Operations.getPackageCounter(key, PACKAGE_NOBETCI_NOTER);
            if (count > 0)
                category = "EX_" + PACKAGE_NOBETCI_NOTER;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Nobetci Noter package support is missing.");
                return;
            }            
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30021", "Illegal Extended info type.");
            return;
        }

        int zone = DataAdminArea.getZone(latitude, longitude);

        int countRawPoiCoor = Operations.getPackageCounter(key, PACKAGE_RAW_POI_COOR);
        DataPoi[] dps = Operations.getPoiSearchEx(key, latitude, longitude, radius, zone, keyword, category, attOrderName, attOrderType, attWhereClause, (countRawPoiCoor > 0 ? false : isFakeCoorNeeded));
        if (dps == null || dps.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Poi not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);
        return;
    } // doPoiSearchEx()

    //-----------------------------------------------------------------------------

    private void doPoiSearchWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doPoiSearchExWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null)
            keyword = Utils.convUtf8ToTurkish(tmp);

        String category = null;
        tmp = request.getParameter("Category");
        if (tmp != null)
            category = tmp;
        String brand = null;
        tmp = request.getParameter("Brand");
        if (tmp != null)
            brand = tmp;

        String attOrderName = null;
        tmp = request.getParameter("AttributeOrderName");
        if (tmp != null)
            attOrderName = tmp;
        String attOrderType = null;
        tmp = request.getParameter("AttributeOrderType");
        if (tmp != null)
            attOrderType = tmp;
        String attWhereClause = null;
        tmp = request.getParameter("AttributeWhereClause");
        if (tmp != null)
            attWhereClause = tmp;

        if (category == null && brand == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Category' or 'Brand' must be supplied.");
            return;
        }

        int count = Operations.getPackageCounter(key, PACKAGE_RAW_POI_COOR);
        DataPoi[] dps =
            Operations.getPoiSearchWithExtent(key, minLatitude, minLongitude, maxLatitude, maxLongitude, keyword, category, brand, attOrderName, attOrderType, attWhereClause, (count > 0 ? false : isFakeCoorNeeded));
        if (dps == null || dps.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Poi not found.");
            return;
        }

        if (count < dps.length)
            count = dps.length;

        Utils.incrementAccessCount(key, transactionId,  count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);
        return;
    } // doPoiSearchWithExtent()

    //-----------------------------------------------------------------------------

    private void doPoiSearchExWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doPoiSearchWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        String keyword = null;
        tmp = request.getParameter("Keyword");
        if (tmp != null)
            keyword = Utils.convUtf8ToTurkish(tmp);
        String exType = null;
        tmp = request.getParameter("ExType");
        if (tmp != null)
            exType = Utils.toUpperCase(tmp);

        String attOrderName = null;
        tmp = request.getParameter("AttributeOrderName");
        if (tmp != null)
            attOrderName = tmp;
        String attOrderType = null;
        tmp = request.getParameter("AttributeOrderType");
        if (tmp != null)
            attOrderType = tmp;
        String attWhereClause = null;
        tmp = request.getParameter("AttributeWhereClause");
        if (tmp != null)
            attWhereClause = tmp;

        if (exType == null || exType.length() <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'ExType' parameter must be supplied.");
            return;
        }

        int count = 0;
        String category = null;
        if (exType.equals(EXTYPE_NOBETCI_ECZANE)) {
            count = Operations.getPackageCounter(key, PACKAGE_NOBETCI_ECZANE);
            if (count > 0)
                category = "EX_" + PACKAGE_NOBETCI_ECZANE;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Nobetci Eczane package support is missing.");
                return;
            }
        } else if (exType.equals(EXTYPE_AKARYAKIT_FIYATLARI)) {
            count = Operations.getPackageCounter(key, PACKAGE_AKARYAKIT_FIYATLARI);
            if (count > 0)
                category = "EX_" + PACKAGE_AKARYAKIT_FIYATLARI;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Akaryakit Fiyatlari package support is missing.");
                return;
            }
        } else if (exType.equals(EXTYPE_OTOPARK_FIYATLARI)) {
            count = Operations.getPackageCounter(key, PACKAGE_OTOPARK_FIYATLARI);
            if (count > 0)
                category = "EX_" + PACKAGE_OTOPARK_FIYATLARI;
            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Otopark Fiyatlari package support is missing.");
                return;
            }
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30021", "Illegal Extended info type.");
            return;
        }

        int countRawPoiCoor = Operations.getPackageCounter(key, PACKAGE_RAW_POI_COOR);
        DataPoi[] dps =
            Operations.getPoiSearchExWithExtent(key, minLatitude, minLongitude, maxLatitude, maxLongitude, keyword, category, attOrderName, attOrderType, attWhereClause, (countRawPoiCoor > 0 ? false : isFakeCoorNeeded));
        if (dps == null || dps.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Poi not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId,  count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);
        return;
    } // doPoiSearchExWithExtent()

    //-----------------------------------------------------------------------------

    private void doGetCoordinate(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetCoordinate()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long ilceId = 0;
        tmp = request.getParameter("IlceId");
        try {
            ilceId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long mahalleId = 0;
        tmp = request.getParameter("MahalleId");
        try {
            mahalleId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long yolId = 0;
        tmp = request.getParameter("YolId");
        try {
            yolId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long kapiId = 0;
        tmp = request.getParameter("KapiId");
        try {
            kapiId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        long poiId = 0;
        tmp = request.getParameter("PoiId");
        try {
            poiId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Operations.getCoordinate(ilId, ilceId, mahalleId, yolId, kapiId, poiId);
        if (coors == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Coordinate not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, coors);
        return;
    } // doGetCoordinate()

    //-----------------------------------------------------------------------------

    private void doGetElevation(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetElevation()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        double altitude = Operations.getElevation(latitude, longitude);
        coors = new double[3];
        coors[0] = longitude;
        coors[1] = latitude;
        coors[2] = altitude;

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, coors);
        return;
    } // doGetElevation()

    //-----------------------------------------------------------------------------

    private void doGetSuggestions(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetSuggestions()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null, keyword = null;

        tmp = request.getParameter("Keyword");
        try {
            keyword = tmp;
        } catch (Exception e) {
            Utils.showError("keyword: " + keyword + " " + e.getMessage());
        }
        

        if (keyword == null || keyword.length() <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant keyword.");
            return;
        }

        int count = 0, sxt = 0;
        double latitude = 0.00, longitude = 0.00;
        String suggestionServletUrl = null, suggestionNopoiServletUrl = null, suggestionYolServletUrl = null, dataset = null;
        
        tmp = request.getParameter("Count");
        if (tmp == null){
            tmp = Utils.getParameter("suggestion_count");
        }
        try {
            if(!Utils.isStringDataNull(tmp)){
              count = Integer.parseInt(tmp);
            }
        } catch (Exception e) {
            Utils.showError("count: " + tmp + " " + e.getMessage());
        }
     
        tmp = request.getParameter("Latitude");
        try {
            if(!Utils.isStringDataNull(tmp)){
              latitude = Double.parseDouble(tmp);
            }
        } catch (Exception e) {
            Utils.showError("latitude: " + tmp + " " + e.getMessage());
        }

        tmp = request.getParameter("Longitude");
        try {
            if(!Utils.isStringDataNull(tmp)){
              longitude = Double.parseDouble(tmp);
            }
        } catch (Exception e) {
           Utils.showError("longitude: " + tmp + " " + e.getMessage());
        }
    
        tmp = request.getParameter("SXT");
        try {
            if(!Utils.isStringDataNull(tmp)){
              sxt = Integer.parseInt(tmp);
            }
        } catch (Exception e) {
           Utils.showError("sxt: " + tmp + " " + e.getMessage());
        }
        
        try {
            tmp =  request.getParameter("DataSet");
            if(!Utils.isStringDataNull(tmp)){
                dataset = tmp.trim();
            }
        } catch (Exception e) {
            Utils.showError("dataset: " + tmp + " " + e.getMessage());
            dataset = null;
        }
        
    
        if(!Utils.isStringDataNull(dataset)){
            if( dataset.equalsIgnoreCase("NOPOI") ){
                try {
                   tmp = Utils.getParameter("suggestion_nopoi_servlet_url");
                    if(tmp != null && tmp.length()>5){
                        suggestionNopoiServletUrl= tmp;
                    }
                } catch (Exception e) {
                   Utils.showError("suggestion_nopoi_servlet_url: " + tmp + " " + e.getMessage());
                   suggestionNopoiServletUrl = null;
                }  
            }else if( dataset.equalsIgnoreCase("YOL") && ( sxt < 1 && latitude == 0.00 && longitude == 0.00 ) ){
                try {
                   tmp = Utils.getParameter("suggestion_yol_servlet_url");
                    if(tmp != null && tmp.length()>5){
                        suggestionYolServletUrl= tmp;
                    }
                } catch (Exception e) {
                    Utils.showError("suggestionYolServletUrl: " + tmp + " " + e.getMessage());
                    suggestionYolServletUrl = null;
                }  
            }
            
        }else{
            try {
               tmp = Utils.getParameter("suggestion_servlet_url");
                if(tmp != null && tmp.length()>5){
                    suggestionServletUrl= tmp;
                }
            } catch (Exception e) {
                Utils.showError("suggestionServletUrl: " + tmp + " " + e.getMessage());
                suggestionServletUrl = null;
            }
        }
          
        DataSuggestion[] dsgs = null;
        
        if(!Utils.isStringDataNull(suggestionYolServletUrl)){
            dsgs = Operations.getSuggestionsServlet(suggestionYolServletUrl, keyword,  count, true);      
        }else if(!Utils.isStringDataNull(suggestionNopoiServletUrl)){
            dsgs = Operations.getSuggestionsServlet(suggestionNopoiServletUrl, keyword,  count, false);      
        }else if(!Utils.isStringDataNull(suggestionServletUrl) && ( sxt < 1 &&  dataset == null && latitude == 0.00 && longitude == 0.00 )){
            dsgs = Operations.getSuggestionsServlet(suggestionServletUrl, keyword,  count, false);     
        }else{
            keyword = Utils.toSuggestKeyword(keyword);
            dsgs = Operations.getSuggestions(keyword, count, latitude, longitude, sxt, dataset);
        }
        
        if (dsgs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback);
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dsgs);
        return;
    } // doGetSuggestions()

    //-----------------------------------------------------------------------------

    private void doOrderByDistance(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doOrderByDistance()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        String points = "";
        tmp = request.getParameter("Positions");
        if (tmp != null)
            points = tmp;
        String[] info = Utils.splitString(points, ",");
        DataPoint[] dps = new DataPoint[info.length];
        for (int i = 0; i < info.length; i++) {
            String[] data = Utils.splitString(info[i], "/");
            String name = data[0];
            double lat = Double.parseDouble(data[1]);
            double lon = Double.parseDouble(data[2]);
            dps[i] = new DataPoint(name, 0, lat, lon, 0.00);
        } // for()

        boolean routeAnalysis = false;
        tmp = request.getParameter("Route");
        try {
            routeAnalysis = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        String consList = "";
        tmp = request.getParameter("Cons");
        if (tmp != null)
            consList = tmp;

        String criteria = "fast";
        tmp = request.getParameter("Criteria");
        if (tmp != null)
            criteria = tmp.toLowerCase();

        dps = Operations.getOrderByDistance(latitude, longitude, dps, routeAnalysis, consList, criteria);
        if (dps == null || dps.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Calculation problem.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, dps.length);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dps);
        return;
    } // doOrderByDistance()

    //-----------------------------------------------------------------------------

    private void doDistanceMatrix(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doDistanceMatrix()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String points = "";
        tmp = request.getParameter("Positions");
        if (tmp != null)
            points = tmp;
        String[] info = Utils.splitString(points, ",");
        DataPoint[] dps = new DataPoint[info.length];
        for (int i = 0; i < info.length; i++) {
            String[] data = Utils.splitString(info[i], "/");
            String name = data[0];
            double lat = Double.parseDouble(data[1]);
            double lon = Double.parseDouble(data[2]);
            dps[i] = new DataPoint(name, 0, lat, lon, 0.00);
        } // for()

        boolean routeAnalysis = false;
        tmp = request.getParameter("Route");
        try {
            routeAnalysis = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        String consList = "";
        tmp = request.getParameter("Cons");
        if (tmp != null)
            consList = tmp;

        String criteria = "fast";
        tmp = request.getParameter("Criteria");
        if (tmp != null && tmp.length() > 0)
            criteria = tmp.toLowerCase();

        final DataPoint[] fdps = dps;
        final boolean frouteAnalysis = routeAnalysis;
        final String fconsList = consList;
        final String fcriteria = criteria;
        ExecutorService executor = Executors.newCachedThreadPool();
        DataDistance[] dms = null;
        Callable<DataDistance[]> task = new Callable<DataDistance[]>() {
            public DataDistance[] call() {
                return Operations.getDistanceMatrix(fdps, frouteAnalysis, fconsList, fcriteria);
            }
        };

        Future<DataDistance[]> future = executor.submit(task);
        try {
            dms = future.get(180, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Timeout problem.");
            ex.printStackTrace();
            return;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } finally {
            future.cancel(true);
        }

        if (dms == null || dms.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Calculation problem.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, dps.length);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dms);
        return;
    } // doDistanceMatrix()

    //-----------------------------------------------------------------------------

    private void doDistance(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doDistance()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double fromLatitude = 0.00;
        tmp = request.getParameter("FromLatitude");
        try {
            fromLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double fromLongitude = 0.00;
        tmp = request.getParameter("FromLongitude");
        try {
            fromLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(fromLatitude, fromLongitude);
        fromLongitude = coors[0];
        fromLatitude = coors[1];

        double toLatitude = 0.00;
        tmp = request.getParameter("ToLatitude");
        try {
            toLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double toLongitude = 0.00;
        tmp = request.getParameter("ToLongitude");
        try {
            toLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        coors = Utils.getTransformedCoors(toLatitude, toLongitude);
        toLongitude = coors[0];
        toLatitude = coors[1];

        double distance = Operations.getDistance(fromLatitude, fromLongitude, toLatitude, toLongitude);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, distance);
        return;
    } // doDistance()

    //-----------------------------------------------------------------------------

    private void doGeocode(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {

        String postedAddressParam = null;
        try {
            postedAddressParam = request.getParameter("Address");
        } catch (Exception e) {
            ;
        }

        if (postedAddressParam == null || (postedAddressParam != null && postedAddressParam.trim().length() < 1))
            Utils.logLbsServiceRequest(key, transactionId, "doReverseGeocode()", request.getQueryString(), request.getRemoteAddr());
        else
            Utils.logLbsServiceRequest(key, transactionId, "doGeocode()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        String address = null;
        tmp = request.getParameter("Address");
        if (tmp != null)
            address = Utils.convUtf8ToTurkish(tmp);

        int suggestion = 0;
        tmp = request.getParameter("Suggestion");
        if (tmp != null)
            suggestion = Integer.parseInt(tmp);
        
        int buffer = 0;
        tmp = request.getParameter("Buffer");
        if (tmp != null)
            buffer = Integer.parseInt(tmp);

        String w3wStr = null;
        tmp = request.getParameter("What3Words");
        if (tmp != null)
            w3wStr = Utils.convUtf8ToTurkish(tmp);

        double tolKapi = Double.parseDouble(Utils.getParameter("tolerance_kapi"));
        double tolYol = Double.parseDouble(Utils.getParameter("tolerance_yol"));

        DataGeocode dg = null;
        What3Words w3w = null;

        int what3wordsEnabled = Operations.getPackageCounter(key, PACKAGE_WHAT3WORDS);

        if (address == null) { //REVERSE GEOCODE && WHAT3WORDS
            if (latitude == 0.00 && longitude == 0.00) {

                // WHAT3WORDS package'i varsa words ile koordinat cevrimi yapilir..
                if (w3wStr != null) {
                    if (what3wordsEnabled <= 0) {
                        DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "What3Words package support is missing.");
                        return;
                    }
                    String res = doWhat3WordsRequest("tr", null, w3wStr);
                    if (res != null)
                        w3w = Operations.getWhat3Words(res);
                    if (w3w != null) {
                        latitude = w3w.getLatitude();
                        longitude = w3w.getLongitude();
                    }
                }
            }

            if (latitude != 0.00 && longitude != 0.00) {
                dg = GeocodeOperations.getReverseGeocode(latitude, longitude, tolKapi, tolYol, buffer);
                if (dg != null && w3w != null)
                    dg.setWhat3words(w3w.getWords());
            }

            else {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20018", "Error: check parameters.");
                return;
            }

            if (dg == null) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Data not found.");
                return;
            }

        } else { //GEOCODE
            int res = GeocodeOperations.checkGeocodeAddress(address);
            if (res != 0) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "Address is no good.");
                return;
            }

            dg = GeocodeOperations.getGeocode(address, latitude, longitude, suggestion);
            if (dg == null) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20014", "Geocode operation failed.");
                return;
            }

        }

        int adresKoduEnabled = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (adresKoduEnabled <= 0) {
            dg.getDa().setIlUavt(0);
            dg.getDa().setIlceUavt(0);
            dg.getDa().setMahalleUavt(0);
            dg.getDa().setKoyUavt(0);
            dg.getDa().setCaddeUavt(0);
            dg.getDa().setSokakUavt(0);
            dg.getDa().setKapiUavt(0);
            dg.getDa().setDaireUavt(0);
        }

        if (what3wordsEnabled > 0 && w3w == null) {
            String position = dg.getLatitude() + "," + dg.getLongitude();
            String res = doWhat3WordsRequest("tr", position, null);
            if (res != null)
                w3w = Operations.getWhat3Words(res);
            if (w3w != null)
                dg.setWhat3words(w3w.getWords());
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dg);
        return;
    } // doGeocode()

    //-----------------------------------------------------------------------------

    private void doGlobalGeocode(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Connection cnn = null;
        try {
            cnn = DbConn.getPooledConnection();
            Utils.logLbsServiceRequest(cnn, key, transactionId, "doGlobalGeocode()", request.getQueryString(), request.getRemoteAddr());
            int count = Operations.getPackageCounter(cnn, key, PACKAGE_GLOBAL_GEOCODE);
            if (count <= 0) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "GlobalGeocode package support is missing.");
                return;
            }
            
            LbsGlobalGeocode lgg = Operations.getGlobalGeocodeKeyDetails(cnn, key); 
            if(lgg == null ){
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "GlobalGeocode package support is missing.");
                return; 
            }
            
            String tmp = null, address = null;
            tmp = request.getParameter("Address");
            if (tmp != null){
                address = Utils.convUtf8ToEnglish(tmp);
            }
            
            double latitude = 0.00, longitude = 0.00;
            tmp = request.getParameter("Latitude");
            try {
                latitude = Utils.makeDegree(tmp);
            } catch (Exception e) {
                if(Utils.isStringDataNull(address)){
                  Utils.showError("doGlobalGeocode Latitude: " + tmp + " " +e.getMessage());
                }
            }
            tmp = request.getParameter("Longitude");
            try {
                longitude = Utils.makeDegree(tmp);
            } catch (Exception e) {
                if(Utils.isStringDataNull(address)){
                  Utils.showError("doGlobalGeocode Longitude: " + tmp + " " +e.getMessage());
                }
            }
            
            String addressUrl = null;
            boolean isReverseGlobalGeocode = false;
            if(Utils.isStringDataNull(address) ){
                isReverseGlobalGeocode = latitude != 0.00 && longitude != 0.00;
                if (isReverseGlobalGeocode) {
                   addressUrl = Utils.replaceParameter(lgg.getReverseUrl(), lgg.getApiKey(), latitude+"", longitude+"");
                } else {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20018", "Error: check parameters.");
                    return;
                }
            }else{
                int resCode = GeocodeOperations.checkGeocodeAddress(address);
                if (resCode != 0) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "Address is no good.");
                    return;
                }
                addressUrl = Utils.replaceParameter(lgg.getUrl(), lgg.getApiKey(), address.replace(" ", "%20"));
            }
           
            String resp = Utils.commonGetRequest(addressUrl);
            
            if(Utils.isStringDataNull(resp) ){
              DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20014", "Geocode operation failed.");
            }
            
            DataGlobalGeocode dgg = null;
            DataReverseGlobalGeocode drgg = null;
            if (isReverseGlobalGeocode) {
                drgg = Operations.getReverseGlobalGeocode(resp);
                if (drgg == null) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Data not found.");
                    return;
                }
            }else {
                dgg = Operations.getGlobalGeocode(resp);
                if (dgg == null) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Data not found.");
                    return;
                }
                
                dgg.setAddress(address); 
            }
            
            Utils.incrementAccessCount(cnn, key, transactionId);
            
            if(isReverseGlobalGeocode){
               DataResponse.sendSuccessResponse(out, typ, transactionId, callback, drgg);  
            }else{
               DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dgg);  
            }   
     } catch (Exception ex) {
        Utils.showError("doGlobalGeocode: " + ex.getMessage());
        ex.printStackTrace();
    } finally {
        DbConn.closeConnection(cnn);
    }
        return;
    } 
    
    private void doGlobalRoute(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Connection cnn = null;
        try {
            cnn = DbConn.getPooledConnection();
            Utils.logLbsServiceRequest(cnn, key, transactionId, "doGlobalRoute()", request.getQueryString(), request.getRemoteAddr());
            int count = Operations.getPackageCounter(cnn, key, PACKAGE_ROTA_GEOMETRISI);
            if (count <= 0) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "GlobalRoute package support is missing.");
                return;
            }
            
            LbsGlobalRoute lgr = Operations.getGlobalRouteKeyDetails(cnn, key); 
            if(lgr == null ){
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "GlobalRoute package support is missing.");
                return; 
            }
            
            String tmp = null;
            Utils.incrementAccessCount(cnn, key, transactionId);

            String criteria = "fast";
            tmp = request.getParameter("Criteria");
            if (!Utils.isStringDataNull(tmp)){
                if(tmp.equalsIgnoreCase("short")){
                    criteria = "short";
                }else{
                    criteria = "fast";
                }
            }
            boolean dirProduce = false;
            tmp = request.getParameter("Dir");
            if (!Utils.isStringDataNull(tmp)){
               dirProduce = Utils.convertStringToIntValue("doGlobalRoute Dir",tmp)!= 0;
            }
            
            boolean geometry = false;
            tmp = request.getParameter("Geometry");
            if (!Utils.isStringDataNull(tmp)){
                geometry = Utils.convertStringToIntValue("doGlobalRoute Geometry",tmp)!= 0;
            }
           
            boolean encode = false;
            tmp = request.getParameter("Encode");
            if (!Utils.isStringDataNull(tmp)){
                encode = Utils.convertStringToIntValue("doGlobalRoute Encode",tmp)!= 0;
            }
            
            String points = "";
            tmp = request.getParameter("Points");
            if (!Utils.isStringDataNull(tmp)){
                points = tmp;
            }         

            String[] info = Utils.splitString(points, ",");
            DataPoint[] dps = new DataPoint[info.length];
            for (int i = 0; i < info.length; i++) {
                String[] data = Utils.splitString(info[i], "/");
                if (data.length == 2) {
                    double lat = Utils.convertStringToDoubleValue("doRoute splitString data[0]", data[0]);
                    double lon = Utils.convertStringToDoubleValue("doRoute splitString data[1]", data[1]);
                    dps[i] = new DataPoint(lat, lon);
                } else if (data.length == 3) {
                    int sym = Utils.convertStringToIntValue("doRoute splitString data[0]", data[0]);
                    double lat = Utils.convertStringToDoubleValue("doRoute splitString data[1]", data[1]);
                    double lon = Utils.convertStringToDoubleValue("doRoute splitString data[2]", data[2]);
                    dps[i] = new DataPoint(sym, lat, lon);
                }
            }
            
           if(dps==null || dps.length<2){
             DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error: check points parameters.");
             return;
           }
             
           String  addressUrl = Utils.replaceParameter(lgr.getUrl(), lgr.getApiKey(), criteria);
            
           DataGlobalRoute dr = Operations.getGlobalRoute(addressUrl, lgr.getTyp(), dps, dirProduce, geometry, encode) ;

           if (dr == null) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Route could not be calculated.");
                return;
           }
           DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dr,true);
        } catch (Exception ex) {
           Utils.showError("doGlobalRoute: " + ex.getMessage());
           ex.printStackTrace();
        } finally {
           DbConn.closeConnection(cnn);
        }
        return;
    }
    //-----------------------------------------------------------------------------

    private void doGetBagimsizBirim(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetBagimsizBirim()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "UAVT_CODE package support is missing.");
            return;
        }

        String tmp = null;
        DataGeocode dg = null;

        long adresKodu = 0;
        tmp = request.getParameter("AdresKodu");
        try {
            adresKodu = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        dg = Operations.getBagimsizBirim(adresKodu);
        if (dg == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Data not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dg);
        return;
    } // doGetBagimsizBirim()

    //-----------------------------------------------------------------------------

    private void doGetBagimsizBirimList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetBagimsizBirimList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "UAVT_CODE package support is missing.");
            return;
        }

        String tmp = null;
        long kapiId = 0;
        
        tmp = request.getParameter("KapiId");
        if (tmp != null && tmp.length() > 0)
            try {
                kapiId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
      
        DataBagimsizBirim [] dbb = Operations.getBagimsizBirimList(kapiId);
        if (dbb == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Data not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dbb);
        return;
    } // doGetBagimsizBirimList()

    //-----------------------------------------------------------------------------
    
    private void doGetCaddeAdresKodu(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetCaddeAdresKodu()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "UAVT_CODE package support is missing.");
            return;
        }

        String tmp = null;
        DataAdresCadde dateAdresCadde = null;

        long caddeAdresKodu = 0;
        tmp = request.getParameter("CaddeAdresKodu");
        try {
            caddeAdresKodu = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        dateAdresCadde = Operations.getCaddeAdresKodu(caddeAdresKodu);
        if (dateAdresCadde == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Data not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dateAdresCadde);
        return;
    } // doGetCaddeAdresKodu()
    
    //------------------------------------------------------------------------
    private void doGetMahalleAdresKodu(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetMahalleAdresKodu()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_UAVT_CODE);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "UAVT_CODE package support is missing.");
            return;
        }

        String tmp = null;
        DataAdresMahalle dateAdresMahalle = null;

        long mahalleAdresKodu = 0;
        tmp = request.getParameter("MahalleKodu");
        try {
            mahalleAdresKodu = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        dateAdresMahalle = Operations.getMahalleAdresKodu(mahalleAdresKodu);
        if (dateAdresMahalle == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Data not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dateAdresMahalle);
        return;
    }
   
    //----------------------------------------------------------------------------

    private void doRota(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doRota()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        tmp=request.getParameter("RotaType");
        if(tmp!=null && tmp.equals("2")){
            Utils.incrementAccessCount(key, transactionId);
            String app_id = Utils.getParameter("here_app_id");
            String app_code= Utils.getParameter("here_app_code");

            String criteria = "fastest";
            tmp = request.getParameter("Criteria");
            if (tmp != null){
                if(tmp.equalsIgnoreCase("short")){
                    criteria = "shortest";
                }else{
                    criteria = "fastest";
                }
            }
            
            boolean dirProduce = false;
            tmp = request.getParameter("Dir");
            if (tmp != null)
                try {
                    dirProduce = (Integer.parseInt(tmp) != 0);
                } catch (Exception e) {
                    ;
                }
            
            boolean geometry = false;
            tmp = request.getParameter("Geometry");
            try {
                geometry = (Integer.parseInt(tmp) != 0);
            } catch (Exception e) {
                ;
            }
            boolean encode = false;
            tmp = request.getParameter("Encode");
            try {
                encode = (Integer.parseInt(tmp) != 0);
            } catch (Exception e) {
                ;
            }
            
            String points = "";
            tmp = request.getParameter("Points");
            if (tmp != null)
                points = tmp;
                       
            // KKB de Points parametresinde ikili koordinatlarÄ± arasÄ±nda , yerine - kullaniliyor
            points = (points.contains("-") ? points.replaceAll("-", ",") : points );
            String[] info = Utils.splitString(points, ",");
            DataPoint[] dps = new DataPoint[info.length];
            for (int i = 0; i < info.length; i++) {
                String[] data = Utils.splitString(info[i], "/");
                if (data.length == 2) {
                    double lat = Utils.convertStringToDoubleValue("doRota splitString data[0]", data[0]);
                    double lon = Utils.convertStringToDoubleValue("doRota splitString data[1]", data[1]);
                    dps[i] = new DataPoint(lat, lon);
                } else if (data.length == 3) {
                    int sym = Utils.convertStringToIntValue("doRota splitString data[0]", data[0]);
                    double lat = Utils.convertStringToDoubleValue("doRota splitString data[1]", data[1]);
                    double lon = Utils.convertStringToDoubleValue("doRota splitString data[2]", data[2]);
                    dps[i] = new DataPoint(sym, lat, lon);
                }
            }
           DataRota dr = Operations.getRotaWithHere(app_id, app_code, dps,criteria, dirProduce, geometry, encode) ;

           if (dr == null) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Route could not be calculated.");
                return;
           }
           DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dr,true);

        }else{
        
        String consList = "";
        tmp = request.getParameter("Cons");
        if (tmp != null)
            consList = tmp;
        String criteria = "fast";
        tmp = request.getParameter("Criteria");
        if (tmp != null)
            criteria = tmp.toLowerCase();

        boolean dirProduce = false;
        tmp = request.getParameter("Dir");
        if (tmp != null)
            try {
                dirProduce = (Integer.parseInt(tmp) != 0);
            } catch (Exception e) {
                ;
            }
        boolean mapProduce = false;
        tmp = request.getParameter("Map");
        if (tmp != null)
            try {
                mapProduce = (Integer.parseInt(tmp) != 0);
            } catch (Exception e) {
                ;
            }
        boolean ordered = false;
        tmp = request.getParameter("Ordered");
        if (tmp != null)
            try {
                ordered = (Integer.parseInt(tmp) != 0);
            } catch (Exception e) {
                ;
            }

        int tspType = 0;
        String tspName = "F";
        tmp = request.getParameter("TspType");
        if (tmp != null)
            try {
                tspType = Integer.parseInt(tmp);
                switch (tspType) {
                case 1:
                    tspName = "F";
                    break;
                case 2:
                    tspName = "O";
                    break;
                case 3:
                    tspName = "E";
                    break;
                case 4:
                    tspName = "R";
                    break;
                case 5:
                    tspName = "C";
                    break;
                default:
                    tspName = "F";
                    break;
                }
            } catch (Exception e) {
                ;
            }
        
        int ilList = 0;
        tmp = request.getParameter("ilList");
        try {
            if( tmp == null) tmp = request.getParameter("IlList"); 
            ilList = Integer.parseInt(tmp);
        } catch (Exception e) { ; }
        
        int width = 600;
        tmp = request.getParameter("Width");
        try {
            width = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int height = 400;
        tmp = request.getParameter("Height");
        try {
            height = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        boolean geometry = false;
        tmp = request.getParameter("Geometry");
        try {
            geometry = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }

        String points = "";
        tmp = request.getParameter("Points");
        if (tmp != null)
            points = tmp;

        points = (points.contains("-") ? points.replaceAll("-", ",") : points );
        boolean allLinks = false;

        tmp = request.getParameter("AllLinks");
        try {
            allLinks = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }

        String[] info = Utils.splitString(points, ",");
        DataPoint[] dps = new DataPoint[info.length];
        for (int i = 0; i < info.length; i++) {
            String[] data = Utils.splitString(info[i], "/");
            if (data.length == 2) {
                double lat = Double.parseDouble(data[0]);
                double lon = Double.parseDouble(data[1]);
                dps[i] = new DataPoint(lat, lon);
            } else if (data.length == 3) {
                int sym = Integer.parseInt(data[0]);
                double lat = Double.parseDouble(data[1]);
                double lon = Double.parseDouble(data[2]);
                dps[i] = new DataPoint(sym, lat, lon);
            }
        } // for()
   
        int count = 0;
        if (geometry)
            count = Operations.getPackageCounter(key, PACKAGE_ROTA_GEOMETRISI);

        DataRota dr = null;
      
        final String fConsList = consList;
        final String fCriteria = criteria;
        final DataPoint[] fDps = dps;
        final boolean fOrdered = ordered;
        final String fTspName = tspName;
        final boolean fDirProduce = dirProduce;
        final boolean fMapProduce = mapProduce;
        final int fWidth = width;
        final int fHeight = height;
        final int fCount = count;
        final boolean fEncode = encode;
        final int fIlList = ilList;
    if( !Operations.isSameCoors(dps) ){
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<DataRota> task = new Callable<DataRota>() {
            public DataRota call() {
                return Operations.getRota(fDps, fCriteria, fConsList, fOrdered, fTspName, fDirProduce, fMapProduce, fWidth, fHeight, (fCount > 0), fEncode, fIlList);
            }
        };

        Future<DataRota> future = executor.submit(task);
        try {
            dr = future.get(220, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } finally {
            future.cancel(true);
        }

        if (allLinks && dr != null && 0 < dr.getPathId()) {
            long[] pathLinks = null;
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            String sql = null;

            Connection cnn = null;

            ArrayList<Long> array = new ArrayList<Long>();

            try {
                cnn = DbConn.getPooledConnection();
                sql = "SELECT LINK_ID FROM NET_PATHLINKS WHERE PATH_ID = ? ORDER BY SEQ_NO ASC";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setLong(1, dr.getPathId());
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    array.add(rset.getLong(1));
                }
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }

            pathLinks = new long[array.size()];
            for (int j = 0; j < pathLinks.length; j++) {
                pathLinks[j] = array.get(j);
            }

            dr.setPathLinks(pathLinks);
        }

    }else{
       dr = new DataRota(0, 0, 0.0, 0.0, 0, "", null, null, null, false, false);
    }
        //DataRota dr = Operations.getRota(dps, criteria, consList, ordered, dirProduce, mapProduce, width, height, (count > 0), encode);
        if (dr == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Route could not be calculated.");
            return;
        }
        DataIl[] ilListData =null;
        if (ilList == 1 && dr != null && 0 < dr.getPathId())
            ilListData = Operations.getIlList(dr.getPathId());
        Utils.incrementAccessCount(key, transactionId);
        if(ilList== 1)
          DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dr,ilListData);
        else
           DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dr,false);
       }
        return;
    } // doRota()    
    
    //-----------------------------------------------------------------------------

    private void doOnRoute(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doOnRoute()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        long pathId = 0;
        tmp = request.getParameter("PathId");
        try {
            pathId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        int distance = 0;
        tmp = request.getParameter("Distance");
        try {
            distance = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        boolean onRoute = Operations.isOnRoute(latitude, longitude, pathId, distance);
        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, onRoute);
        return;
    } // doOnRoute()

    //-----------------------------------------------------------------------------

    private void doMap(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doMap()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        if (latitude != 0.00 && longitude != 0.00) {
            double[] coors = Utils.getTransformedCoors(latitude, longitude);
            longitude = coors[0];
            latitude = coors[1];
        }

        int zoomLevel = 0;
        tmp = request.getParameter("ZoomLevel");
        try {
            zoomLevel = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (minLatitude != 0.00 && minLongitude != 0.00) {
            double[] coors = Utils.getTransformedCoors(minLatitude, minLongitude);
            minLongitude = coors[0];
            minLatitude = coors[1];
        }

        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (maxLatitude != 0.00 && maxLongitude != 0.00) {
            double[] coors = Utils.getTransformedCoors(maxLatitude, maxLongitude);
            maxLongitude = coors[0];
            maxLatitude = coors[1];
        }

        int pan = 0;
        tmp = request.getParameter("Pan");
        try {
            pan = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        int width = 600;
        tmp = request.getParameter("Width");
        try {
            width = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int height = 400;
        tmp = request.getParameter("Height");
        try {
            height = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        int basemap = 0;
        tmp = request.getParameter("Basemap");
        try {
            basemap = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        String points = "";
        tmp = request.getParameter("Points");
        if (tmp != null)
            points = tmp;

        String[] info = Utils.splitString(points, ",");
        DataPoint[] dps = new DataPoint[info.length];
        for (int i = 0; i < info.length; i++) {
            String[] data = Utils.splitString(info[i], "/");
            if (data.length == 2) {
                double lat = Double.parseDouble(data[0]);
                double lon = Double.parseDouble(data[1]);
                double[] coors = Utils.getTransformedCoors(lat, lon);
                dps[i] = new DataPoint(coors[1], coors[0]);
            } else if (data.length == 3) {
                int sym = Integer.parseInt(data[0]);
                double lat = Double.parseDouble(data[1]);
                double lon = Double.parseDouble(data[2]);
                double[] coors = Utils.getTransformedCoors(lat, lon);
                dps[i] = new DataPoint(sym, coors[1], coors[0]);
            }
        } // for()
        String userData = "";
        tmp = request.getParameter("UserData");
        if (tmp != null)
            userData = tmp;

        String userDataId = null;
        tmp = request.getParameter("UserDataId");
        if (tmp != null)
            userDataId = tmp;

        int userDataStyle = 0;
        tmp = request.getParameter("UserDataStyle");
        if (tmp != null)
            userDataStyle = Integer.parseInt(tmp);

        DataUserLine[] dul = new DataUserLine[1];
        DataUserRegion[] dur = new DataUserRegion[1];
        DataMap dm = new DataMap();
        dm.setUserDataStyle(userDataStyle);
        dm.setZoomLevel(zoomLevel);
        dm.setPan(pan);
        dm.setWidth(width);
        dm.setHeight(height);
        dm.setPoints(dps);
        dm.setBaseMap(basemap);
        if ("LINE".equalsIgnoreCase(userData) && userDataId != null) {
            dm.setUserData(userData);
            dul = Operations.getUserLine(key, userDataId, true);
            if (dul != null && dul[0] != null)
                dm.setUserCoors(dul[0].getOarray());
        }

        else if ("REGION".equalsIgnoreCase(userData) && userDataId != null) {
            dm.setUserData(userData);
            dur = Operations.getUserRegion(key, userDataId, true);
            if (dur != null && dur[0] != null)
                dm.setGeo(dur[0].getGeo());
        }

        if (latitude > 0 && longitude > 0) {
            dm.setLatitude(latitude);
            dm.setLongitude(longitude);

            dm = Operations.getDataMap(dm, true);
        } else {
            dm.setMinLatitude(minLatitude);
            dm.setMinLongitude(minLongitude);
            dm.setMaxLatitude(maxLatitude);
            dm.setMaxLongitude(maxLongitude);

            dm = Operations.getDataMap(dm, true, true);
        }
        if (dm == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dm);
        return;
    } // doMap()

    //-----------------------------------------------------------------------------

    private void doAddUserPoint(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddUserPoint()", request.getQueryString(), request.getRemoteAddr());

        DataUserPoint dup = new DataUserPoint();

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            dup.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dup.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Type");
        try {
            dup.type = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Address");
        if (tmp != null)
            dup.address = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        dup.telNo = request.getParameter("TelNo");
        dup.faxNo = request.getParameter("FaxNo");
        tmp = request.getParameter("String1");
        if (tmp != null)
            dup.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dup.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dup.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dup.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dup.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dup.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dup.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dup.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dup.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Number1");
        try {
            dup.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dup.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dup.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dup.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dup.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dup.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dup.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dup.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dup.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Latitude");
        try {
            dup.latitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Longitude");
        try {
            dup.longitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Angle");
        try {
            dup.angle = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(dup.latitude, dup.longitude);
        dup.longitude = coors[0];
        dup.latitude = coors[1];

        int res = Operations.addUserPoint(key, dup);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "id", dup.id);
        return;
    } // doAddUserPoint()

    //-----------------------------------------------------------------------------

    private void doAddUserPoints(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddUserPoints()", request.getQueryString(), request.getRemoteAddr());

        DataUserPoint dup = new DataUserPoint();
        DataUserPoint[] dupl = null;

        String tmp = null;
        String dataType = null;
        String data = null;
        int count = 1;

        tmp = request.getParameter("Data");
        if (tmp != null)
            data = tmp;

        tmp = request.getParameter("DataType");
        if (tmp != null)
            dataType = tmp;

        if (dataType.equalsIgnoreCase("JSON")) {

            try {
                JSONObject jsonres = new JSONObject(data);
                JSONArray userPointsList = jsonres.getJSONArray("userpoints");

                if (userPointsList.length() <= 0) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
                    return;
                }

                count = userPointsList.length();

                dupl = new DataUserPoint[userPointsList.length()];

                String iterKey = null;

                for (int i = 0; i < userPointsList.length(); i++) {
                    JSONObject userList = userPointsList.getJSONObject(i);
                    Iterator iter = userList.keys();

                    dup = new DataUserPoint();

                    while (iter.hasNext()) {
                        iterKey = (String) iter.next();
                        if (iterKey.equals("id"))
                            dup.id = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("name"))
                            dup.name = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("type"))
                            dup.type = userList.getInt(iterKey);
                        if (iterKey.equals("address"))
                            dup.address = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("telno"))
                            dup.telNo = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("faxno"))
                            dup.faxNo = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string1"))
                            dup.string_1 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string2"))
                            dup.string_2 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string3"))
                            dup.string_3 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string4"))
                            dup.string_4 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string5"))
                            dup.string_5 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string6"))
                            dup.string_6 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string7"))
                            dup.string_7 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string8"))
                            dup.string_8 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string9"))
                            dup.string_9 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("number1"))
                            dup.number_1 = userList.getDouble(iterKey);
                        if (iterKey.equals("number2"))
                            dup.number_2 = userList.getDouble(iterKey);
                        if (iterKey.equals("number3"))
                            dup.number_3 = userList.getDouble(iterKey);
                        if (iterKey.equals("number4"))
                            dup.number_4 = userList.getDouble(iterKey);
                        if (iterKey.equals("number5"))
                            dup.number_5 = userList.getDouble(iterKey);
                        if (iterKey.equals("number6"))
                            dup.number_6 = userList.getDouble(iterKey);
                        if (iterKey.equals("number7"))
                            dup.number_7 = userList.getDouble(iterKey);
                        if (iterKey.equals("number8"))
                            dup.number_8 = userList.getDouble(iterKey);
                        if (iterKey.equals("number9"))
                            dup.number_9 = userList.getDouble(iterKey);
                        if (iterKey.equals("latitude"))
                            dup.latitude = userList.getDouble(iterKey);
                        if (iterKey.equals("longitude"))
                            dup.longitude = userList.getDouble(iterKey);
                        if (iterKey.equals("angle"))
                            dup.angle = userList.getDouble(iterKey);
                    }

                    double[] coors = Utils.getTransformedCoors(dup.latitude, dup.longitude);
                    dup.longitude = coors[0];
                    dup.latitude = coors[1];
                    dupl[i] = dup;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Utils.showError("doAddUserPoints EXCEPTION: " + ex.getMessage());
            }
        } else if (dataType.equalsIgnoreCase("XML")) {

            try {
                DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbfactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(data));
                Document doc = db.parse(is);
                doc.getDocumentElement().normalize();
                NodeList nodes = doc.getElementsByTagName("userpoint");

                if (nodes.getLength() <= 0) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
                    return;
                }

                dupl = new DataUserPoint[nodes.getLength()];

                count = nodes.getLength();

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node nNode = nodes.item(i);
                    dup = new DataUserPoint();

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) nNode;
                        dup.id = Utils.convUtf8ToTurkish(element.getElementsByTagName("id").item(0).getTextContent());
                        dup.name = Utils.convUtf8ToTurkish(element.getElementsByTagName("name").item(0).getTextContent());
                        dup.type = Integer.parseInt(element.getElementsByTagName("type").item(0).getTextContent());
                        dup.address = Utils.convUtf8ToTurkish(element.getElementsByTagName("address").item(0).getTextContent());
                        dup.telNo = Utils.convUtf8ToTurkish(element.getElementsByTagName("telno").item(0).getTextContent());
                        dup.faxNo = Utils.convUtf8ToTurkish(element.getElementsByTagName("faxno").item(0).getTextContent());
                        dup.string_1 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string1").item(0).getTextContent());
                        dup.string_2 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string2").item(0).getTextContent());
                        dup.string_3 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string3").item(0).getTextContent());
                        dup.string_4 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string4").item(0).getTextContent());
                        dup.string_5 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string5").item(0).getTextContent());
                        dup.string_6 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string6").item(0).getTextContent());
                        dup.string_7 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string7").item(0).getTextContent());
                        dup.string_8 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string8").item(0).getTextContent());
                        dup.string_9 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string9").item(0).getTextContent());
                        dup.number_1 = Double.parseDouble(element.getElementsByTagName("number1").item(0).getTextContent());
                        dup.number_2 = Double.parseDouble(element.getElementsByTagName("number2").item(0).getTextContent());
                        dup.number_3 = Double.parseDouble(element.getElementsByTagName("number3").item(0).getTextContent());
                        dup.number_4 = Double.parseDouble(element.getElementsByTagName("number4").item(0).getTextContent());
                        dup.number_5 = Double.parseDouble(element.getElementsByTagName("number5").item(0).getTextContent());
                        dup.number_6 = Double.parseDouble(element.getElementsByTagName("number6").item(0).getTextContent());
                        dup.number_7 = Double.parseDouble(element.getElementsByTagName("number7").item(0).getTextContent());
                        dup.number_8 = Double.parseDouble(element.getElementsByTagName("number8").item(0).getTextContent());
                        dup.number_9 = Double.parseDouble(element.getElementsByTagName("number9").item(0).getTextContent());
                        dup.latitude = Double.parseDouble(element.getElementsByTagName("latitude").item(0).getTextContent());
                        dup.longitude = Double.parseDouble(element.getElementsByTagName("longitude").item(0).getTextContent());
                        dup.angle = Double.parseDouble(element.getElementsByTagName("angle").item(0).getTextContent());
                    }

                    double[] coors = Utils.getTransformedCoors(dup.latitude, dup.longitude);
                    dup.longitude = coors[0];
                    dup.latitude = coors[1];
                    dupl[i] = dup;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Utils.showError("doAddUserPoints EXCEPTION: " + ex.getMessage());
            }
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "DataType must be XML or JSON");
            return;
        }

        int res = Operations.addUserPoints(key, dupl);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doAddUserPoints()

    //-----------------------------------------------------------------------------

    private void doGetUserPoint(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetUserPoint()", request.getQueryString(), request.getRemoteAddr());

        String id = null;

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        if (id == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error occured while processing. Please check parameter 'Id'.");
            return;
        }

        DataUserPoint[] dups = Operations.getUserPoint(key, id);
        if (dups == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "USER POINT not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dups);
        return;
    } // doGetUserPoint()

    //-----------------------------------------------------------------------------

    private void doRemoveUserPoint(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doRemoveUserPoint()", request.getQueryString(), request.getRemoteAddr());

        String id = null;

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        if (id == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error occured while processing. Please check parameter 'Id'.");
            return;
        }

        int res = Operations.removeUserPoint(key, id);
        if (res != 0) {
            if (res == -1)
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "USER POINT not found.");
            else
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doRemoveUserPoint()

    //-----------------------------------------------------------------------------

    private void doUserPointList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUserPointList()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int count = 0;
        tmp = request.getParameter("Count");
        try {
            count = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int maxCount = Integer.parseInt(Utils.getParameter("userpoint_count"));
        if (count == 0 || count > maxCount)
            count = maxCount;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        int radius = 0;
        tmp = request.getParameter("Radius");
        try {
            radius = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        int maxRadius = Integer.parseInt(Utils.getParameter("radius"));
        if ((radius > maxRadius) || (radius == 0))
            radius = maxRadius;

        String regionId = null;
        tmp = request.getParameter("RegionId");
        if (tmp != null)
            regionId = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        String subKeys = null;
        tmp = request.getParameter("SubKeys");
        if (tmp != null)
            subKeys = tmp;

        String whereClause = "";
        tmp = request.getParameter("WhereClause");
        if (tmp != null)
            whereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperWhereClause = whereClause.toUpperCase();
        if (upperWhereClause.indexOf("SELECT ") >= 0 || upperWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        String idList = null;
        tmp = request.getParameter("IdList");
        if (tmp != null)
            idList = tmp;

        int countPackage = Operations.getPackageCounter(key, PACKAGE_SUB_KEY);
        if (countPackage <= 0) {
            subKeys = null;
        }

        int detailed = 0;
        tmp = request.getParameter("Detailed");
        try {
            detailed = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        DataList[] dints = null;
        DataUserPoint[] durs = null;
        if (detailed > 0) {
            if (regionId == null || regionId.length() <= 0)
                durs = Operations.getUserPointListDetailed(key, count, latitude, longitude, radius, whereClause, subKeys, idList);
            else
                durs = Operations.userPointListDetailed(key, count, regionId, whereClause, idList);

        } else {

            if (regionId == null || regionId.length() <= 0)
                dints = Operations.getUserPointList(key, count, latitude, longitude, radius, whereClause, subKeys, idList);
            else
                dints = Operations.userPointList(key, count, regionId, whereClause, idList);
        }
        if (dints == null && durs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }
        Utils.incrementAccessCount(key, transactionId);
        if (detailed > 0)
            DataResponse.sendSuccessResponse(out, typ, transactionId, callback, durs);
        else
            DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dints);
        return;
    } // doUserPointList()

    //-----------------------------------------------------------------------------

    private void doUserPointListCount(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUserPointListCount()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String[] pointIdList = null;
        int type = 0;
        long id = 0;
        tmp = request.getParameter("Type");
        if (tmp.equalsIgnoreCase("Il")) {
            type = 1;
            id = 99;
        } else if (tmp.equalsIgnoreCase("Ilce")) {
            type = 2;
        } else if (tmp.equalsIgnoreCase("Mahalle")) {
            type = 3;
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Please check TYPE parameter.");
            return;
        }

        String whereClause = "";
        tmp = request.getParameter("WhereClause");
        if (tmp != null)
            whereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperWhereClause = whereClause.toUpperCase();
        if (upperWhereClause.indexOf("SELECT") >= 0 || upperWhereClause.indexOf("FROM") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        String groupByCol = "";
        tmp = request.getParameter("Group");
        if (tmp != null) {
            groupByCol = tmp.toUpperCase();
            groupByCol = groupByCol.substring(0, groupByCol.length() - 1) + "_" + groupByCol.substring(groupByCol.length() - 1, groupByCol.length());
        }

        tmp = request.getParameter("IdList");
        if (tmp != null) {
            pointIdList = tmp.split(",");
        }

        tmp = request.getParameter("Id");
        try {
            id = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        if (id == 0 && pointIdList == null && type > 1) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10090", "Please make sure you enter Id or Point parameter.");
            return;
        } else {
            ArrayList array = null;
            if (groupByCol.length() > 0) {
                array = Operations.getUserPointListCount(key, type, id, pointIdList, whereClause, groupByCol);
            } else {
                array = Operations.getUserPointListCount(key, type, id, pointIdList, whereClause);
            }
            // il
            if (type == 1) {
                DataIl[] cupl = null;
                cupl = new DataIl[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    cupl[i] = (DataIl) array.get(i);
                }
                if (cupl.length < 1) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                    return;
                } else {
                    Utils.incrementAccessCount(key, transactionId);
                    DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
                }
            } else if (type == 2) { // ilce
                DataIlce[] cupl = null;
                cupl = new DataIlce[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    cupl[i] = (DataIlce) array.get(i);
                }
                if (cupl.length < 1) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                    return;
                } else {
                    Utils.incrementAccessCount(key, transactionId);
                    DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
                }
            }  else if (type == 3) {  // mahalle
                DataMahalle[] cupl = null;
                cupl = new DataMahalle[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    cupl[i] = (DataMahalle) array.get(i);
                }

                if (cupl.length < 1) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                    return;
                } else {
                    Utils.incrementAccessCount(key, transactionId);
                    DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
                }
            }
        }
        return;
    } // doUserPointListCount()

    //-----------------------------------------------------------------------------

    private void doUserPointListWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUserPointListWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int count = 0;
        tmp = request.getParameter("Count");
        try {
            count = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int maxCount = Integer.parseInt(Utils.getParameter("userpoint_count"));
        if (count == 0 || count > maxCount)
            count = maxCount;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        String subKeys = null;
        tmp = request.getParameter("SubKeys");
        if (tmp != null)
            subKeys = tmp;

        String whereClause = "";
        tmp = request.getParameter("WhereClause");
        if (tmp != null)
            whereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperWhereClause = whereClause.toUpperCase();
        if (upperWhereClause.indexOf("SELECT ") >= 0 || upperWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        int countPackage = Operations.getPackageCounter(key, PACKAGE_SUB_KEY);
        if (countPackage <= 0) {
            subKeys = null;
        }

        String[] idList = null;
        tmp = request.getParameter("IdList");
        if (tmp != null)
            idList = tmp.split(",");

        DataList[] dints = null;
        dints = Operations.getUserPointListWithExtent(key, count, minLatitude, minLongitude, maxLatitude, maxLongitude, whereClause, subKeys, idList);

        if (dints == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dints);
        return;
    } // doUserPointListWithExtent()

    //-----------------------------------------------------------------------------

    private void doUserPointListCountWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUserPointListCountWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int type = 0;
        long id = 0;
        String[] pointIdList = null;

        tmp = request.getParameter("Type");
        if (tmp.equalsIgnoreCase("Il")) {
            type = 1;
            id = 90;
        } else if (tmp.equalsIgnoreCase("Ilce")) {
            type = 2;
        } else if (tmp.equalsIgnoreCase("Mahalle")) {
            type = 3;
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Please check TYPE parameter.");
            return;
        }
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (minLatitude == 0.00 || minLongitude == 0.00 || maxLatitude == 0.00 || maxLongitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant extent coordinates.");
            return;
        }

        String whereClause = "";
        tmp = request.getParameter("WhereClause");
        if (tmp != null)
            whereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperWhereClause = whereClause.toUpperCase();
        if (upperWhereClause.indexOf("SELECT") >= 0 || upperWhereClause.indexOf("FROM") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        String groupByCol = "";
        tmp = request.getParameter("Group");
        if (tmp != null) {
            groupByCol = tmp.toUpperCase();
            groupByCol = groupByCol.substring(0, groupByCol.length() - 1) + "_" + groupByCol.substring(groupByCol.length() - 1, groupByCol.length());
        }

        tmp = request.getParameter("IdList");
        if (tmp != null) {
            pointIdList = tmp.split(",");
        }

        Extent extent = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        ArrayList array = null;
        if (groupByCol.length() > 0) {
            array = Operations.getUserPointListCountWithExtent(key, type, extent, pointIdList, whereClause, groupByCol);
        } else {
            array = Operations.getUserPointListCountWithExtent(key, type, extent, pointIdList, whereClause);
        }

        if (type == 1) {
            DataIl[] cupl = null;
            cupl = new DataIl[array.size()];
            for (int i = 0; i < array.size(); i++) {
                cupl[i] = (DataIl) array.get(i);
            }
            if (cupl.length < 1) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                return;
            } else {
                Utils.incrementAccessCount(key, transactionId);
                DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
            }
        } else if (type == 2) {
            DataIlce[] cupl = null;
            cupl = new DataIlce[array.size()];
            for (int i = 0; i < array.size(); i++) {
                cupl[i] = (DataIlce) array.get(i);
            }
            if (cupl.length < 1) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                return;
            } else {
                Utils.incrementAccessCount(key, transactionId);
                DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
            }
        } else if (type == 3) {
            DataMahalle[] cupl = null;
            cupl = new DataMahalle[array.size()];
            for (int i = 0; i < array.size(); i++) {
                cupl[i] = (DataMahalle) array.get(i);
            }
            if (cupl.length < 1) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                return;
            } else {
                Utils.incrementAccessCount(key, transactionId);
                DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
            }
        }
        return;
    } // doUserPointListCountWithExtent()

    //-----------------------------------------------------------------------------

    private void doUserPointCountSearch(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUserPointCountSearch()", request.getQueryString(), request.getRemoteAddr());
        String pointIdList[] = null;
        String tmp = null;
        int type = 0;
        long id = 0;
        tmp = request.getParameter("Type");
        if (tmp.equalsIgnoreCase("Il")) {
            type = 1;
            id = 90;
        } else if (tmp.equalsIgnoreCase("Ilce")) {
            type = 2;
        } else if (tmp.equalsIgnoreCase("Mahalle")) {
            type = 3;
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Please check TYPE parameter.");
            return;
        }
        int radius = 0;
        tmp = request.getParameter("Radius");
        if (tmp != null)
            try {
                radius = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        else
            radius = Integer.parseInt(Utils.getParameter("radius"));

        int maxRadius = Integer.parseInt(Utils.getParameter("radius"));
        if ((radius > maxRadius) || (radius == 0))
            radius = maxRadius;

        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (longitude == 0.00 || latitude == 0.00 || radius == 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal coordinates or radius.");
            return;
        }

        String whereClause = "";
        tmp = request.getParameter("WhereClause");
        if (tmp != null)
            whereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperWhereClause = whereClause.toUpperCase();
        if (upperWhereClause.indexOf("SELECT ") >= 0 || upperWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }
        tmp = request.getParameter("IdList");
        if (tmp != null) {
            pointIdList = tmp.split(",");
        }

        String groupByCol = "";
        tmp = request.getParameter("Group");
        if (tmp != null) {
            groupByCol = tmp.toUpperCase();
            groupByCol = groupByCol.substring(0, groupByCol.length() - 1) + "_" + groupByCol.substring(groupByCol.length() - 1, groupByCol.length());
        }

        ArrayList array = null;

        if (groupByCol.length() > 0) {
            array = Operations.getUserPointCountSearch(key, type, latitude, longitude, radius, whereClause, pointIdList, groupByCol);
        } else {
            array = Operations.getUserPointCountSearch(key, type, latitude, longitude, radius, whereClause, pointIdList);
        }

        if (type == 1) {
            DataIl[] cupl = null;
            cupl = new DataIl[array.size()];
            for (int i = 0; i < array.size(); i++) {
                cupl[i] = (DataIl) array.get(i);
            }
            if (cupl.length < 1) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                return;
            } else {
                Utils.incrementAccessCount(key, transactionId);
                DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
            }
        } else if (type == 2) {
            DataIlce[] cupl = null;
            cupl = new DataIlce[array.size()];
            for (int i = 0; i < array.size(); i++) {
                cupl[i] = (DataIlce) array.get(i);
            }
            if (cupl.length < 1) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                return;
            } else {
                Utils.incrementAccessCount(key, transactionId);
                DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
            }
        } else if (type == 3) {
            DataMahalle[] cupl = null;
            cupl = new DataMahalle[array.size()];
            for (int i = 0; i < array.size(); i++) {
                cupl[i] = (DataMahalle) array.get(i);
            }

            if (cupl.length < 1) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10091", "Please make sure you have inserted your points.");
                return;
            } else {
                Utils.incrementAccessCount(key, transactionId);
                DataResponse.sendSuccessResponse(out, typ, transactionId, callback, cupl);
            }
        }
        return;
    } // doUserPointCountSearch()

    //-----------------------------------------------------------------------------

    private void doAddUserRegionByUnion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddUserRegionByUnion()", request.getQueryString(), request.getRemoteAddr());

        DataUserRegion dur = new DataUserRegion();

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            dur.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dur.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Type");
        try {
            dur.type = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("String1");
        if (tmp != null)
            dur.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dur.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dur.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dur.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dur.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dur.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dur.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dur.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dur.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Number1");
        try {
            dur.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dur.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dur.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dur.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dur.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dur.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dur.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dur.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dur.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        String fromType = "";
        tmp = request.getParameter("FromType");
        if (tmp != null)
            fromType = Utils.toUpperCase(tmp);
        String idList = "";
        tmp = request.getParameter("IdList");
        if (tmp != null)
            idList = tmp;

        int res = Operations.addUserRegionByUnion(key, dur, fromType, idList);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "id", dur.id);
        return;
    } // doAddUserRegionByUnion()

    //-----------------------------------------------------------------------------

    private void doAddUserRegion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddUserRegion()", request.getQueryString(), request.getRemoteAddr());

        DataUserRegion dur = new DataUserRegion();

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            dur.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dur.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Type");
        try {
            dur.type = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("String1");
        if (tmp != null)
            dur.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dur.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dur.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dur.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dur.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dur.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dur.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dur.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dur.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Number1");
        try {
            dur.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dur.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dur.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dur.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dur.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dur.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dur.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dur.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dur.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double[] coors = null;
        tmp = request.getParameter("Coors");
        if (tmp != null) {
            String[] info = tmp.split(",");
            if (info.length > 1) {
                coors = new double[info.length];
                for (int i = 0; i < info.length; i++)
                    coors[i] = Double.parseDouble(info[i]);
            }
        }

        int res = Operations.addUserRegion(key, dur, coors);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "id", dur.id);
        return;
    } // doAddUserRegion()

    //-----------------------------------------------------------------------------

    private void doAddUserRegions(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddUserRegions()", request.getQueryString(), request.getRemoteAddr());

        DataUserRegion dur = null;
        DataUserRegion[] durl = null;

        String tmp = null;
        String dataType = null;
        String data = null;
        int count = 1;
        
        double[][] coorList = null;

        tmp = request.getParameter("Data");
        if (tmp != null)
            data = tmp;

        tmp = request.getParameter("DataType");
        if (tmp != null)
            dataType = tmp;

        if (dataType.equalsIgnoreCase("JSON")) {

            try {
                JSONObject jsonres = new JSONObject(data);
                JSONArray userRegionsList = jsonres.getJSONArray("userregions");

                if (userRegionsList.length() <= 0) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
                    return;
                }

                durl = new DataUserRegion[userRegionsList.length()];

                count = userRegionsList.length();

                coorList = new double[userRegionsList.length()][];
                double[] coors = null;

                String iterKey = null;

                for (int i = 0; i < userRegionsList.length(); i++) {
                    JSONObject userList = userRegionsList.getJSONObject(i);
                    Iterator iter = userList.keys();

                    dur = new DataUserRegion();

                    while (iter.hasNext()) {
                        iterKey = (String) iter.next();
                        if (iterKey.equals("id"))
                            dur.id = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("name"))
                            dur.name = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("type"))
                            dur.type = userList.getInt(iterKey);
                        if (iterKey.equals("coors")) {
                            if (userList.getString(iterKey) != null) {
                                String[] info = userList.getString(iterKey).split(",");
                                if (info.length > 1) {
                                    coors = new double[info.length];
                                    coorList[i] = new double[info.length];
                                    for (int j = 0; j < info.length; j++) {
                                        coors[j] = Double.parseDouble(info[j]);
                                        coorList[i][j] = coors[j];
                                    }
                                }
                            }
                        }
                        if (iterKey.equals("string1"))
                            dur.string_1 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string2"))
                            dur.string_2 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string3"))
                            dur.string_3 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string4"))
                            dur.string_4 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string5"))
                            dur.string_5 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string6"))
                            dur.string_6 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string7"))
                            dur.string_7 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string8"))
                            dur.string_8 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string9"))
                            dur.string_9 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("number1"))
                            dur.number_1 = userList.getDouble(iterKey);
                        if (iterKey.equals("number2"))
                            dur.number_2 = userList.getDouble(iterKey);
                        if (iterKey.equals("number3"))
                            dur.number_3 = userList.getDouble(iterKey);
                        if (iterKey.equals("number4"))
                            dur.number_4 = userList.getDouble(iterKey);
                        if (iterKey.equals("number5"))
                            dur.number_5 = userList.getDouble(iterKey);
                        if (iterKey.equals("number6"))
                            dur.number_6 = userList.getDouble(iterKey);
                        if (iterKey.equals("number7"))
                            dur.number_7 = userList.getDouble(iterKey);
                        if (iterKey.equals("number8"))
                            dur.number_8 = userList.getDouble(iterKey);
                        if (iterKey.equals("number9"))
                            dur.number_9 = userList.getDouble(iterKey);
                    }

                    coors = Utils.getTransformedCoors(dur.latitude, dur.longitude);
                    dur.longitude = coors[0];
                    dur.latitude = coors[1];
                    durl[i] = dur;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Utils.showError("doAddUserRegions EXCEPTION: " + ex.getMessage());
            }
        } else if (dataType.equalsIgnoreCase("XML")) {

            try {
                DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbfactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(data));
                Document doc = db.parse(is);
                doc.getDocumentElement().normalize();
                NodeList nodes = doc.getElementsByTagName("userregion");

                if (nodes.getLength() <= 0) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
                    return;
                }

                count = nodes.getLength();

                durl = new DataUserRegion[nodes.getLength()];

                coorList = new double[nodes.getLength()][];
                double[] coors = null;

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node nNode = nodes.item(i);
                    dur = new DataUserRegion();

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) nNode;
                        dur.id = Utils.convUtf8ToTurkish(element.getElementsByTagName("id").item(0).getTextContent());
                        dur.name = Utils.convUtf8ToTurkish(element.getElementsByTagName("name").item(0).getTextContent());
                        dur.type = Integer.parseInt(element.getElementsByTagName("type").item(0).getTextContent());
                        String[] info = Utils.convUtf8ToTurkish(element.getElementsByTagName("coors").item(0).getTextContent()).split(",");

                        if (info.length > 1) {
                            coors = new double[info.length];
                            coorList[i] = new double[info.length];
                            for (int j = 0; j < info.length; j++) {
                                coors[j] = Double.parseDouble(info[j]);
                                coorList[i][j] = coors[j];
                            }
                        }

                        dur.string_1 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string1").item(0).getTextContent());
                        dur.string_2 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string2").item(0).getTextContent());
                        dur.string_3 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string3").item(0).getTextContent());
                        dur.string_4 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string4").item(0).getTextContent());
                        dur.string_5 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string5").item(0).getTextContent());
                        dur.string_6 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string6").item(0).getTextContent());
                        dur.string_7 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string7").item(0).getTextContent());
                        dur.string_8 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string8").item(0).getTextContent());
                        dur.string_9 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string9").item(0).getTextContent());
                        dur.number_1 = Double.parseDouble(element.getElementsByTagName("number1").item(0).getTextContent());
                        dur.number_2 = Double.parseDouble(element.getElementsByTagName("number2").item(0).getTextContent());
                        dur.number_3 = Double.parseDouble(element.getElementsByTagName("number3").item(0).getTextContent());
                        dur.number_4 = Double.parseDouble(element.getElementsByTagName("number4").item(0).getTextContent());
                        dur.number_5 = Double.parseDouble(element.getElementsByTagName("number5").item(0).getTextContent());
                        dur.number_6 = Double.parseDouble(element.getElementsByTagName("number6").item(0).getTextContent());
                        dur.number_7 = Double.parseDouble(element.getElementsByTagName("number7").item(0).getTextContent());
                        dur.number_8 = Double.parseDouble(element.getElementsByTagName("number8").item(0).getTextContent());
                        dur.number_9 = Double.parseDouble(element.getElementsByTagName("number9").item(0).getTextContent());
                    }

                    coors = Utils.getTransformedCoors(dur.latitude, dur.longitude);
                    dur.longitude = coors[0];
                    dur.latitude = coors[1];
                    durl[i] = dur;
                }
                int res = Operations.addUserRegions(key, durl, coorList);
                if (res != 0) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Utils.showError("EXCEPTION: " + ex.getMessage());
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing.");
                return;
            }
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "DataType must be XML or JSON");
            return;
        }

        int res = Operations.addUserRegions(key, durl, coorList);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doAddUserRegions()

    //-----------------------------------------------------------------------------

    private void doBufferedLineToUserRegion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doBufferedLineToUserRegion()", request.getQueryString(), request.getRemoteAddr());

        DataUserRegion dur = new DataUserRegion();

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            dur.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dur.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Type");
        try {
            dur.type = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("String1");
        if (tmp != null)
            dur.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dur.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dur.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dur.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dur.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dur.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dur.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dur.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dur.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Number1");
        try {
            dur.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dur.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dur.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dur.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dur.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dur.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dur.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dur.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dur.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double[] coors = null;
        tmp = request.getParameter("Coors");
        if (tmp != null) {
            String[] info = tmp.split(",");
            if (info.length > 1) {
                coors = new double[info.length];
                for (int i = 0; i < info.length; i++)
                    coors[i] = Double.parseDouble(info[i]);
            }
        }
        String lineId = null;
        tmp = request.getParameter("LineId");
        if (tmp != null)
            lineId = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        int bufferDist = 0;
        tmp = request.getParameter("BufferDist");
        try {
            bufferDist = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        if (bufferDist <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Error BufferDist parameter must be supplied and greater than zero");
            return;
        }

        int maxBufferDistance = Integer.parseInt(Utils.getParameter("buffer_max_distance").toString());

        if (maxBufferDistance < bufferDist) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Error BufferDist parameter cannot greater than " + maxBufferDistance);
            return;
        }


        int res = Operations.addBufferedLineToUserRegion(key, dur, coors, lineId, bufferDist);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "id", dur.id);
        return;
    } // doBufferedLineToUserRegion()

    //-----------------------------------------------------------------------------

    private void doBufferedRouteToUserRegion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doBufferedRouteToUserRegion()", request.getQueryString(), request.getRemoteAddr());

        DataUserRegion dur = new DataUserRegion();

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            dur.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dur.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Type");
        try {
            dur.type = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("String1");
        if (tmp != null)
            dur.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dur.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dur.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dur.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dur.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dur.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dur.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dur.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dur.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Number1");
        try {
            dur.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dur.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dur.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dur.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dur.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dur.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dur.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dur.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dur.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        long pathId = 0;
        tmp = request.getParameter("PathId");
        try {
            pathId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        if (pathId <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Error PathId parameter must be supplied and greater than zero");
            return;
        }
        int bufferDist = 0;
        tmp = request.getParameter("BufferDist");
        try {
            bufferDist = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        if (bufferDist <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Error BufferDist parameter must be supplied and greater than zero");
            return;
        }

        int maxBufferDistance = Integer.parseInt(Utils.getParameter("buffer_max_distance").toString());

        if (maxBufferDistance < bufferDist) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Error BufferDist parameter cannot greater than " + maxBufferDistance);
            return;
        }

        int res = Operations.addBufferedRouteToUserRegion(key, dur, pathId, bufferDist);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "id", dur.id);
        return;
    } // doBufferedRouteToUserRegion()

    //-----------------------------------------------------------------------------

    private void doGetUserRegion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetUserRegion()", request.getQueryString(), request.getRemoteAddr());

        String id = null;
        boolean withCoors = false;

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        if (id == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error occured while processing. Please check parameter 'Id'.");
            return;
        }

        tmp = request.getParameter("WithCoors");
        try {
            withCoors = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }

        DataUserRegion[] durs = Operations.getUserRegion(key, id, withCoors);
        if (durs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "USER REGION not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, durs);
        return;
    } // doGetUserRegion()

    //-----------------------------------------------------------------------------

    private void doUserRegionSearch(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUserRegionSearch()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int count = 0;
        tmp = request.getParameter("Count");
        try {
            count = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int maxCount = Integer.parseInt(Utils.getParameter("userregion_count"));
        if (count == 0 || count > maxCount)
            count = maxCount;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        String pointId = null;
        tmp = request.getParameter("PointId");
        if (tmp != null)
            pointId = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        String whereClause = "";
        tmp = request.getParameter("WhereClause");
        if (tmp != null)
            whereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperWhereClause = whereClause.toUpperCase();
        if (upperWhereClause.indexOf("SELECT ") >= 0 || upperWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        DataUserRegion[] durs = null;
        if (pointId == null || pointId.length() <= 0)
            durs = Operations.userRegionSearch(key, count, latitude, longitude, whereClause);
        else
            durs = Operations.userRegionSearch(key, count, pointId, whereClause);
        if (durs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "USER REGION not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, durs);
        return;
    } // doUserRegionSearch()

    //-----------------------------------------------------------------------------

    private void doRemoveUserRegion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doRemoveUserRegion()", request.getQueryString(), request.getRemoteAddr());

        String id = null;

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        if (id == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error occured while processing. Please check parameter 'Id'.");
            return;
        }

        int res = Operations.removeUserRegion(key, id);
        if (res != 0) {
            if (res == -1)
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "USER REGION not found.");
            else
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doRemoveUserRegion()

    //-----------------------------------------------------------------------------

    private void doUserRegionList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUserRegionList()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int count = 0;
        tmp = request.getParameter("Count");
        try {
            count = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int maxCount = Integer.parseInt(Utils.getParameter("userregion_count"));
        if (count == 0 || count > maxCount)
            count = maxCount;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        String pointId = null;
        tmp = request.getParameter("PointId");
        if (tmp != null)
            pointId = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        int mask=0;
        tmp=request.getParameter("Mask");
        try {
            mask = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        
        String whereClause = "";
        tmp = request.getParameter("WhereClause");
        if (tmp != null)
            whereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperWhereClause = whereClause.toUpperCase();
        if (upperWhereClause.indexOf("SELECT ") >= 0 || upperWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }
        
        System.out.println("WhereClause->"+whereClause);
        
        if(mask==1){
            whereClause=whereClause.replaceAll("\\|A\\|", " AND ");
            whereClause=whereClause.replaceAll("\\|O\\|", " OR ");
            whereClause=whereClause.replaceAll("\\|I\\|", " IN ");
            whereClause=whereClause.replaceAll("\\|NT\\|", " NOT ");
            whereClause=whereClause.replaceAll("\\|ISNL\\|"," IS NULL ");
            whereClause=whereClause.replaceAll("\\|ISNTNL\\|"," IS NOT NULL ");

        }
        
        System.out.println("WhereClause->"+whereClause);
        
        String idList = null;
        tmp = request.getParameter("IdList");
        if (tmp != null)
            idList = tmp;

        int detailed = 0;
        tmp = request.getParameter("Detailed");
        try {
            detailed = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        DataList[] dints = null;
        DataUserRegion[] durs = null;
        if (detailed > 0)
            durs = Operations.getUserRegionListDetailed(key, count, pointId, latitude, longitude, whereClause, idList);
        else
            dints = Operations.getUserRegionList(key, count, pointId, latitude, longitude, whereClause, idList);
        if (durs == null && dints == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        if (detailed > 0)
            DataResponse.sendSuccessResponse(out, typ, transactionId, callback, durs);
        else
            DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dints);
        return;
    } // doUserRegionList()

    //-----------------------------------------------------------------------------

    private void doUploadRegion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUploadRegion()", request.getQueryString(), request.getRemoteAddr());

        DataUserRegion dur = new DataUserRegion();

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            dur.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dur.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Type");
        try {
            dur.type = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("String1");
        if (tmp != null)
            dur.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dur.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dur.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dur.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dur.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dur.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dur.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dur.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dur.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Number1");
        try {
            dur.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dur.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dur.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dur.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dur.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dur.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dur.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dur.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dur.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20032", "Upload Data Format Error.");
            return;
        }

        File savedFile = null;
        try {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = upload.parseRequest(request);
            Utils.showText("Items: " + items);

            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (!item.isFormField()) {
                    try {
                        String itemName = item.getName();
                        itemName = itemName.substring((itemName.indexOf(":", 0)) + 2, itemName.length());
                        String path = Utils.getParameter("savefile_path");
                        savedFile = new File(path + "/" + (new Date()).getTime() + "_" + itemName);
                        item.write(savedFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } // if()
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (savedFile == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        int res = Operations.addUserRegionFromFile(key, dur, savedFile);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doUploadRegion()

    //-----------------------------------------------------------------------------

    private void doAddUserLine(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddUserLine()", request.getQueryString(), request.getRemoteAddr());

        DataUserLine dul = new DataUserLine();

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            dul.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dul.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Type");
        try {
            dul.type = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("String1");
        if (tmp != null)
            dul.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dul.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dul.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dul.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dul.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dul.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dul.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dul.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dul.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Number1");
        try {
            dul.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dul.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dul.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dul.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dul.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dul.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dul.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dul.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dul.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double[] coors = null;
        tmp = request.getParameter("Coors");
        if (tmp != null) {
            String[] info = tmp.split(",");
            if (info.length > 1) {
                coors = new double[info.length];
                for (int i = 0; i < info.length; i++)
                    coors[i] = Double.parseDouble(info[i]);
            }
        }

        int res = Operations.addUserLine(key, dul, coors);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "id", dul.id);
        return;
    } // doAddUserLine()

    //-----------------------------------------------------------------------------

    private void doAddUserLines(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddUserLines()", request.getQueryString(), request.getRemoteAddr());

        DataUserLine dul = null;
        DataUserLine[] dull = null;

        String tmp = null;
        String dataType = null;
        String data = null;
        int count = 1;
        
        double[][] coorList = null;

        tmp = request.getParameter("Data");
        if (tmp != null)
            data = tmp;

        tmp = request.getParameter("DataType");
        if (tmp != null)
            dataType = tmp;

        if (dataType.equalsIgnoreCase("JSON")) {

            try {
                JSONObject jsonres = new JSONObject(data);
                JSONArray userLinesList = jsonres.getJSONArray("userlines");

                if (userLinesList.length() <= 0) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
                    return;
                }

                dull = new DataUserLine[userLinesList.length()];

                count = userLinesList.length();

                coorList = new double[userLinesList.length()][];
                double[] coors = null;

                String iterKey = null;

                for (int i = 0; i < userLinesList.length(); i++) {
                    JSONObject userList = userLinesList.getJSONObject(i);
                    Iterator iter = userList.keys();

                    dul = new DataUserLine();

                    while (iter.hasNext()) {
                        iterKey = (String) iter.next();
                        if (iterKey.equals("id"))
                            dul.id = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("name"))
                            dul.name = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("type"))
                            dul.type = userList.getInt(iterKey);
                        if (iterKey.equals("coors")) {
                            if (userList.getString(iterKey) != null) {
                                String[] info = userList.getString(iterKey).split(",");
                                if (info.length > 1) {
                                    coors = new double[info.length];
                                    coorList[i] = new double[info.length];
                                    for (int j = 0; j < info.length; j++) {
                                        coors[j] = Double.parseDouble(info[j]);
                                        coorList[i][j] = coors[j];
                                    }
                                }
                            }
                        }
                        if (iterKey.equals("string1"))
                            dul.string_1 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string2"))
                            dul.string_2 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string3"))
                            dul.string_3 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string4"))
                            dul.string_4 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string5"))
                            dul.string_5 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string6"))
                            dul.string_6 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string7"))
                            dul.string_7 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string8"))
                            dul.string_8 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("string9"))
                            dul.string_9 = Utils.convUtf8ToTurkish(userList.getString(iterKey));
                        if (iterKey.equals("number1"))
                            dul.number_1 = userList.getDouble(iterKey);
                        if (iterKey.equals("number2"))
                            dul.number_2 = userList.getDouble(iterKey);
                        if (iterKey.equals("number3"))
                            dul.number_3 = userList.getDouble(iterKey);
                        if (iterKey.equals("number4"))
                            dul.number_4 = userList.getDouble(iterKey);
                        if (iterKey.equals("number5"))
                            dul.number_5 = userList.getDouble(iterKey);
                        if (iterKey.equals("number6"))
                            dul.number_6 = userList.getDouble(iterKey);
                        if (iterKey.equals("number7"))
                            dul.number_7 = userList.getDouble(iterKey);
                        if (iterKey.equals("number8"))
                            dul.number_8 = userList.getDouble(iterKey);
                        if (iterKey.equals("number9"))
                            dul.number_9 = userList.getDouble(iterKey);
                    }

                    dull[i] = dul;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                Utils.showError("EXCEPTION: " + ex.getMessage());
            }
        } else if (dataType.equalsIgnoreCase("XML")) {

            try {
                DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbfactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(data));
                Document doc = db.parse(is);
                doc.getDocumentElement().normalize();
                NodeList nodes = doc.getElementsByTagName("userline");

                if (nodes.getLength() <= 0) {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
                    return;
                }

                count = nodes.getLength();

                dull = new DataUserLine[nodes.getLength()];

                coorList = new double[nodes.getLength()][];
                double[] coors = null;

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node nNode = nodes.item(i);
                    dul = new DataUserLine();

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) nNode;
                        dul.id = Utils.convUtf8ToTurkish(element.getElementsByTagName("id").item(0).getTextContent());
                        dul.name = Utils.convUtf8ToTurkish(element.getElementsByTagName("name").item(0).getTextContent());
                        dul.type = Integer.parseInt(element.getElementsByTagName("type").item(0).getTextContent());
                        String[] info = Utils.convUtf8ToTurkish(element.getElementsByTagName("coors").item(0).getTextContent()).split(",");

                        if (info.length > 1) {
                            coors = new double[info.length];
                            coorList[i] = new double[info.length];
                            for (int j = 0; j < info.length; j++) {
                                coors[j] = Double.parseDouble(info[j]);
                                coorList[i][j] = coors[j];
                            }
                        }

                        dul.string_1 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string1").item(0).getTextContent());
                        dul.string_2 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string2").item(0).getTextContent());
                        dul.string_3 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string3").item(0).getTextContent());
                        dul.string_4 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string4").item(0).getTextContent());
                        dul.string_5 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string5").item(0).getTextContent());
                        dul.string_6 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string6").item(0).getTextContent());
                        dul.string_7 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string7").item(0).getTextContent());
                        dul.string_8 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string8").item(0).getTextContent());
                        dul.string_9 = Utils.convUtf8ToTurkish(element.getElementsByTagName("string9").item(0).getTextContent());
                        dul.number_1 = Double.parseDouble(element.getElementsByTagName("number1").item(0).getTextContent());
                        dul.number_2 = Double.parseDouble(element.getElementsByTagName("number2").item(0).getTextContent());
                        dul.number_3 = Double.parseDouble(element.getElementsByTagName("number3").item(0).getTextContent());
                        dul.number_4 = Double.parseDouble(element.getElementsByTagName("number4").item(0).getTextContent());
                        dul.number_5 = Double.parseDouble(element.getElementsByTagName("number5").item(0).getTextContent());
                        dul.number_6 = Double.parseDouble(element.getElementsByTagName("number6").item(0).getTextContent());
                        dul.number_7 = Double.parseDouble(element.getElementsByTagName("number7").item(0).getTextContent());
                        dul.number_8 = Double.parseDouble(element.getElementsByTagName("number8").item(0).getTextContent());
                        dul.number_9 = Double.parseDouble(element.getElementsByTagName("number9").item(0).getTextContent());
                    }

                    dull[i] = dul;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Utils.showError("EXCEPTION: " + ex.getMessage());
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing.");
                return;
            }
        }  else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "DataType must be XML or JSON");
            return;
        }

        int res = Operations.addUserLines(key, dull, coorList);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doAddUserLines()

    //-----------------------------------------------------------------------------

    private void doGetUserLine(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetUserLine()", request.getQueryString(), request.getRemoteAddr());

        String id = null;
        boolean withCoors = false;

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        if (id == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error occured while processing. Please check parameter 'Id'.");
            return;
        }

        tmp = request.getParameter("WithCoors");
        try {
            withCoors = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }

        DataUserLine[] dull = Operations.getUserLine(key, id, withCoors);
        if (dull == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "USER LINE not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dull);
        return;
    } // doGetUserLine()

    //-----------------------------------------------------------------------------

    private void doRemoveUserLine(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doRemoveUserLine()", request.getQueryString(), request.getRemoteAddr());

        String id = null;

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        if (id == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Error occured while processing. Please check parameter 'Id'.");
            return;
        }

        int res = Operations.removeUserLine(key, id);
        if (res != 0) {
            if (res == -1)
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "USER LINE not found.");
            else
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doRemoveUserLine()

    //-----------------------------------------------------------------------------

    private void doUserLineList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doUserLineList()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int count = 0;
        tmp = request.getParameter("Count");
        try {
            count = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int maxCount = Integer.parseInt(Utils.getParameter("userline_count"));
        if (count == 0 || count > maxCount)
            count = maxCount;


        String whereClause = "";
        tmp = request.getParameter("WhereClause");
        if (tmp != null)
            whereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperWhereClause = whereClause.toUpperCase();
        if (upperWhereClause.indexOf("SELECT ") >= 0 || upperWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        String idList = null;
        tmp = request.getParameter("IdList");
        if (tmp != null)
            idList = tmp;

        int detailed = 0;
        tmp = request.getParameter("Detailed");
        try {
            detailed = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        DataList[] dints = null;
        DataUserLine[] durs = null;
        if (detailed > 0)
            durs = Operations.getUserLineListDetailed(key, count, whereClause, idList);
        else
            dints = Operations.getUserLineList(key, count, whereClause, idList);
        if (durs == null && dints == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }
        Utils.incrementAccessCount(key, transactionId);
        if (detailed > 0)
            DataResponse.sendSuccessResponse(out, typ, transactionId, callback, durs);
        else
            DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dints);
        return;
    } // doUserLineList()

    //-----------------------------------------------------------------------------

    private void doAddRouteToUserLine(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddRouteToUserLine()", request.getQueryString(), request.getRemoteAddr());

        DataUserLine dul = new DataUserLine();

        String tmp = null;
        long pathId = 0;
        tmp = request.getParameter("PathId");
        try {
            pathId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        tmp = request.getParameter("Id");
        if (tmp != null)
            dul.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dul.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Type");
        try {
            dul.type = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("String1");
        if (tmp != null)
            dul.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dul.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dul.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dul.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dul.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dul.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dul.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dul.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dul.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Number1");
        try {
            dul.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dul.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dul.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dul.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dul.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dul.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dul.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dul.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dul.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        boolean routeGeometry = false;

        if (Operations.getPackageCounter(key, PACKAGE_ROTA_GEOMETRISI) > 0)
            routeGeometry = true;

        int res = Operations.addRouteToUserLine(key, dul, pathId, routeGeometry);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "id", dul.id);
        return;
    } // doAddRouteToUserLine()

    //-----------------------------------------------------------------------------

    private void doCampCategoryList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doCampCategoryList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_KAMPANYA);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Campaign package support is missing.");
            return;
        }

        boolean counts = false;

        String tmp = null;
        tmp = request.getParameter("Counts");
        try {
            counts = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        CampCategory[] ccs = CampOperations.getCategoryList(key, counts);
        if (ccs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, ccs);
        return;
    } // doCampCategoryList()

    //-----------------------------------------------------------------------------

    private void doCampCampaignList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doCampCampaignList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_KAMPANYA);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Campaign package support is missing.");
            return;
        }

        String category = "";
        long poiId = 0;
        boolean counts = false;
        String tmp = null;
        
        tmp = request.getParameter("Category");
        if (tmp != null)
            category = tmp;
        tmp = request.getParameter("PoiId");
        try {
            poiId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Counts");
        try {
            counts = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        CampCampaign[] ccs = CampOperations.getCampaignList(key, category, poiId, counts);
        if (ccs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, ccs);
        return;
    } // doCampCampaignList()

    //-----------------------------------------------------------------------------

    private void doCampNearCampaigns(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doCampNearCampaigns()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_KAMPANYA);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Campaign package support is missing.");
            return;
        }

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        int radius = 0;
        tmp = request.getParameter("Radius");
        if (tmp != null)
            try {
                radius = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        else
            radius = Integer.parseInt(Utils.getParameter("radius"));

        int maxRadius = Integer.parseInt(Utils.getParameter("radius"));
        if ((radius > maxRadius) || (radius == 0))
            radius = maxRadius;

        String category = null;
        tmp = request.getParameter("Category");
        if (tmp != null)
            category = tmp;

        boolean counts = false;
        tmp = request.getParameter("Counts");
        try {
            counts = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        int zone = DataAdminArea.getZone(latitude, longitude);

        CampCampaign[] ccs = CampOperations.getNearCampaigns(key, latitude, longitude, radius, category, zone, counts);
        if (ccs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, ccs);
        return;
    } // doCampNearCampaigns()

    //-----------------------------------------------------------------------------

    private void doDealCategoryList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doDealCategoryList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_FIRSATLAR);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Deal (Firsatlar) package support is missing.");
            return;
        }

        boolean counts = false;
        String tmp = null;
        
        tmp = request.getParameter("Counts");
        try {
            counts = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        DealCategory[] dcs = DealOperations.getCategoryList(counts);
        if (dcs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dcs);
        return;
    } // doDealCategoryList()

    //-----------------------------------------------------------------------------

    private void doDealDealList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doDealDealList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_FIRSATLAR);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Deal (Firsatlar) package support is missing.");
            return;
        }

        int categoryId = 0;
        boolean counts = false;
        String tmp = null;
        
        tmp = request.getParameter("Category");
        try {
            categoryId = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Counts");
        try {
            counts = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        DealDeal[] dds = DealOperations.getDealList(categoryId, counts);
        if (dds == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dds);
        return;
    } // doDealDealList()

    //-----------------------------------------------------------------------------

    private void doDealNearDeals(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doDealNearDeals()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_FIRSATLAR);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Deal (Firsatlar) package support is missing.");
            return;
        }

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];
        
        int radius = 0;
        tmp = request.getParameter("Radius");
        if (tmp != null)
            try {
                radius = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        else
            radius = Integer.parseInt(Utils.getParameter("radius"));

        int maxRadius = Integer.parseInt(Utils.getParameter("radius"));
        if ((radius > maxRadius) || (radius == 0))
            radius = maxRadius;

        int categoryId = 0;
        tmp = request.getParameter("Category");
        try {
            categoryId = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        boolean counts = false;
        tmp = request.getParameter("Counts");
        try {
            counts = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        int zone = DataAdminArea.getZone(latitude, longitude);

        DealDeal[] dds = DealOperations.getNearDeals(latitude, longitude, radius, categoryId, zone, counts);
        if (dds == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dds);
        return;
    } // doDealNearDeals()

    //-----------------------------------------------------------------------------

    private void doTrafficEventSearch(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTrafficEventSearch()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_TRAFIK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Traffic package support is missing.");
            return;
        }

        String tmp = null;
        long pathId = 0;
        tmp = request.getParameter("PathId");
        try {
            pathId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        int radius = 0;
        tmp = request.getParameter("Radius");
        if (tmp != null)
            try {
                radius = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        else
            radius = Integer.parseInt(Utils.getParameter("radius"));

        int maxRadius = Integer.parseInt(Utils.getParameter("radius"));
        if ((radius > maxRadius) || (radius == 0))
            radius = maxRadius;

        int zone = DataAdminArea.getZone(latitude, longitude);

        DataTrafficEvent[] dtes = null;
        if (pathId > 0)
            dtes = Operations.getTrafficEventSearch(pathId);
        else
            dtes = Operations.getTrafficEventSearch(latitude, longitude, radius, zone);
        if (dtes == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dtes);
        return;
    } // doTrafficEventSearch()

    //-----------------------------------------------------------------------------

    private void doTrafficInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTrafficInfo()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_TRAFIK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Traffic package support is missing.");
            return;
        }

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        if (latitude == 0.00 || longitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20018", "No parameter given.");
            return;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        DataTrafficInfo dti = Operations.getTrafficInfo(latitude, longitude);
        if (dti == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found on given coordinates.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dti);
        return;
    } // doTrafficInfo()

    //-----------------------------------------------------------------------------

    private void doTrafficTmcFlowData(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTrafficTmcFlowData()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_TRAFIK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Traffic package support is missing.");
            return;
        }

        String tmcKodList = null;
        String tmp = request.getParameter("TmcKodList");
        if (tmp != null && tmp.length() > 0)
            tmcKodList = tmp;

        if (tmcKodList == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20018", "No parameter given.");
            return;
        }

        DataTmcFlow[] dtfs = Operations.getTrafficTmcFlow(tmcKodList);
        if (dtfs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "TMC code not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dtfs);
        return;
    } // doTrafficTmcFlowData()

    //-----------------------------------------------------------------------------

    private void doTrafficStartList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTrafficStartList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_TRAFIK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Traffic package support is missing.");
            return;
        }

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        String endName = null;
        tmp = request.getParameter("EndName");
        if (tmp != null && tmp.length() > 0)
            endName = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        int detail = 0;
        tmp = request.getParameter("Detail");
        try {
            detail = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        String[] names = Operations.getTrafficStartList(key, ilId, endName, latitude, longitude, detail);
        if (names == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, names, "names", "name");
        return;
    } // doTrafficStartList()

    //-----------------------------------------------------------------------------

    private void doTrafficEndList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTrafficEndList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_TRAFIK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Traffic package support is missing.");
            return;
        }

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        String startName = null;
        tmp = request.getParameter("StartName");
        if (tmp != null && tmp.length() > 0)
            startName = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        int detail = 0;
        tmp = request.getParameter("Detail");
        try {
            detail = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        String[] names = Operations.getTrafficEndList(key, ilId, startName, latitude, longitude, detail);
        if (names == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, names, "names", "name");
        return;
    } // doTrafficEndList()

    //-----------------------------------------------------------------------------

    private void doTrafficRoute(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTrafficRoute()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_TRAFIK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Traffic package support is missing.");
            return;
        }

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        String startName = null;
        tmp = request.getParameter("StartName");
        if (tmp != null && tmp.length() > 0)
            startName = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String endName = null;
        tmp = request.getParameter("EndName");
        if (tmp != null && tmp.length() > 0)
            endName = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        DataTrafficRoute[] dtrs = Operations.getTrafficRoute(key, ilId, startName, endName);
        if (dtrs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dtrs);
        return;
    } // doTrafficRoute()

    //-----------------------------------------------------------------------------

    private void doTrafficRouteList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTrafficRouteList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_TRAFIK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Traffic package support is missing.");
            return;
        }

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        int detail = 0;
        tmp = request.getParameter("Detail");
        try {
            detail = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        DataTrafficRoute[] dtrs = Operations.getTrafficRouteList(key, ilId, detail);
        if (dtrs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dtrs);
        return;
    } // doTrafficRouteList()

    //-----------------------------------------------------------------------------

    private void doGetDemographicInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetDemographicInfo()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_DEMOGRAFIK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Demographic package support is missing.");
            return;
        }

        String tmp = null;
        long ilId = 0;
        tmp = request.getParameter("IlId");
        if (tmp != null && tmp.length() > 0)
            try {
                ilId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        long ilceId = 0;
        tmp = request.getParameter("IlceId");
        if (tmp != null && tmp.length() > 0)
            try {
                ilceId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        long mahalleId = 0;
        tmp = request.getParameter("MahalleId");
        if (tmp != null && tmp.length() > 0)
            try {
                mahalleId = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }
        String infoType = "";
        tmp = request.getParameter("InfoType");
        if (tmp != null)
            infoType = tmp;

        if (ilId == 0 && ilceId == 0 && mahalleId == 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'IlId' or 'IlceId' or 'MahalleId' must be supplied.");
            return;
        }

        DataDemographic dd = Operations.getDemographicInfo(key, ilId, ilceId, mahalleId, infoType);
        if (dd == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dd);
        return;
    } // doGetDemographicInfo()

    //-----------------------------------------------------------------------------

    private void doAddUserImage(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddUserImage()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String id = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        int seqNo = 0;
        tmp = request.getParameter("SeqNo");
        if (tmp != null && tmp.length() > 0)
            try {
                seqNo = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        int imgType = 0;
        tmp = request.getParameter("ImgType");
        if (tmp != null && tmp.length() > 0)
            try {
                imgType = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }


        int res = Operations.addUserImage(key, id, seqNo, imgType, request);
        if (res < 0) {
            if (res == -1)
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20022", "Data format error.");
            else
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20023", "Could not add user image. (res: " + res + ")");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doAddUserImage()

 //-----------------------------------------------------------------------------
    private void doMapThemeImage(String key, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          String transactionId = getTransactionId();
          Utils.logLbsServiceRequest(key, transactionId, "doMapThemeImage()", request.getQueryString(), request.getRemoteAddr());
          if (key == null || key.length() <= 0) {
              response.setContentType(CONTENT_TYPE_XML);
              PrintWriter out = response.getWriter();
              Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTHEMEIMAGE", request.getQueryString(), request.getRemoteAddr());
              DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10012", "Error occured while processing. Please check parameter 'Key'.");
              out.close();
              return;
          }

          if (!Utils.isKeyValid(key)) {
              response.setContentType(CONTENT_TYPE_XML);
              PrintWriter out = response.getWriter();
              Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTHEMEIMAGE", request.getQueryString(), request.getRemoteAddr());
              DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
              out.close();
              return;
          }
          String referer = request.getHeader("Referer");
          Utils.logInfo("REFERRER: " + referer);
          Utils.logInfo("KEY: " + key);

          String domain = Utils.getDomainFromReferer(referer);
          if (!Utils.isKeyDomainValid(key, domain)) {
              response.setContentType(CONTENT_TYPE_XML);
              PrintWriter out = response.getWriter();
              Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTHEMEIMAGE", request.getQueryString(), request.getRemoteAddr());
              DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10014", "The key used is not active for used domain " + domain + " ! Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
              out.close();
              return;
          }
          
          String tmp = null;
          double minLatitude = 0.00;
          tmp = request.getParameter("MinLatitude");
          try {
              minLatitude = Utils.makeDegree(tmp);
          } catch (Exception e) {
              ;
          }
          double minLongitude = 0.00;
          tmp = request.getParameter("MinLongitude");
          try {
              minLongitude = Utils.makeDegree(tmp);
          } catch (Exception e) {
              ;
          }
          double[] coors = Utils.getTransformedCoors(minLatitude, minLongitude);
          minLongitude = coors[0];
          minLatitude = coors[1];

          double maxLatitude = 0.00;
          tmp = request.getParameter("MaxLatitude");
          try {
              maxLatitude = Utils.makeDegree(tmp);
          } catch (Exception e) {
              ;
          }
          double maxLongitude = 0.00;
          tmp = request.getParameter("MaxLongitude");
          try {
              maxLongitude = Utils.makeDegree(tmp);
          } catch (Exception e) {
              ;
          }
          coors = Utils.getTransformedCoors(maxLatitude, maxLongitude);
          maxLongitude = coors[0];
          maxLatitude = coors[1];

          int width = 600;
          tmp = request.getParameter("Width");
          try {
              width = Integer.parseInt(tmp);
          } catch (Exception e) {
              ;
          }
          int height = 400;
          tmp = request.getParameter("Height");
          try {
              height = Integer.parseInt(tmp);
          } catch (Exception e) {
              ;
          }
          String theme = null;
          tmp = request.getParameter("Theme");
          try {
              theme = tmp;
          } catch (Exception e) {
              ;
          }
          
          String datasource=null;
          tmp = request.getParameter("Datasource");
          try {
              datasource = tmp;
          } catch (Exception e) {
              ;
          }
          
          if ((datasource == null || datasource.length() <= 0) || (theme == null || theme.length() <= 0) ) {
              response.setContentType(CONTENT_TYPE_XML);
              PrintWriter out = response.getWriter();
              Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTHEMEIMAGE", request.getQueryString(), request.getRemoteAddr());
              DataResponse.sendErrorResponse(out, "XML", transactionId, null, "20010", "'Datasource' and 'Theme' parameters must be supplied.");
              out.close();
              return;
          }

          int basemap = 0;
          tmp = request.getParameter("Basemap");
          try {
              basemap = Integer.parseInt(tmp);
          } catch (Exception e) {
              ;
          }

          DataMap dm = Operations.getMapTheme(minLatitude, minLongitude, maxLatitude, maxLongitude, width, height,theme,datasource,  basemap, false);
          if (dm == null) {
              response.setContentType(CONTENT_TYPE_XML);
              PrintWriter out = response.getWriter();
              Utils.updateLbsServiceRequest(transactionId, "10014", "Failed to generate map theme image in server", "-");
              DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10014", "Failed to generate map theme image in server");
              out.close();
              return;
          }

          response.setContentType(CONTENT_TYPE_PNG);
          OutputStream out = null;
          InputStream inp = null;
          HttpURLConnection http = null;

          try {
              out = response.getOutputStream();
              URL url = new URL(dm.getUrl());
              http = (HttpURLConnection) url.openConnection();
              http.setRequestMethod("GET");
              http.setRequestProperty("Content-type", "image/png");
              http.setDoOutput(false);
              http.setDoInput(true);
              http.connect();

              inp = http.getInputStream();
             // int code = http.getResponseCode();
              byte b[] = new byte[10240];
              while (true) {
                  int length = inp.read(b, 0, b.length);
                  if (length < 0)
                      break;

                  out.write(b, 0, length);
              } // while()
          } catch (Exception ex) {
              ex.printStackTrace();
              Utils.updateLbsServiceRequest(transactionId, "1", "Exception", "-");
              return;
          } finally {
              DbConn.closeHttpConn(http, null, null, null);
              DbConn.closeStreamConn(inp, out);
          }

          Utils.incrementAccessCount(key, transactionId);
          Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "binary-map-traffic-image");
          return;
      } // doMapThemeImage()
    
//---------------------------------------------------------------------------------------
    private void doRemoveUserImage(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doRemoveUserImage()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String id = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        int seqNo = 0;
        tmp = request.getParameter("SeqNo");
        if (tmp != null && tmp.length() > 0)
            try {
                seqNo = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        int imgType = 0;
        tmp = request.getParameter("ImgType");
        if (tmp != null && tmp.length() > 0)
            try {
                imgType = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }

        int res = Operations.removeUserImage(key, id, seqNo, imgType);
        if (res < 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20021", "Error while removing image.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doRemoveUserImage()

    //-----------------------------------------------------------------------------

    private void doGetRiskSkor(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetRiskSkor()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_RISK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Risk package support is missing.");
            return;
        }

        String tmp = null;
        int teminatTuru = 0;
        tmp = request.getParameter("TeminatTuru");
        try {
            teminatTuru = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        int adresSeviyesi = 0;
        tmp = request.getParameter("AdresSeviyesi");
        try {
            adresSeviyesi = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        int[] beyanList = new int[21];

        for (int ii = 1; ii <= 20; ii++) {
            tmp = request.getParameter("Beyan" + ii);
            try {
                beyanList[ii] = Integer.parseInt(tmp);
            } catch (Exception e) {
                beyanList[ii] = 0;
            }
        }

        InfoRiskSkor ws = new InfoRiskSkor();
        SkorResultInfo res = ws.getSkor(key, teminatTuru, longitude, latitude, adresSeviyesi, beyanList);

        if (res == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20021", "Error while calculating score.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, res);
        return;
    } // doGetRiskSkor()

    //-----------------------------------------------------------------------------

    private void doAddServiceAreaToUserRegion(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doAddServiceAreaToUserRegion()", request.getQueryString(), request.getRemoteAddr());

        DataUserRegion dur = new DataUserRegion();

        String tmp = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            dur.id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("Name");
        if (tmp != null)
            dur.name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        int networkType = 0;
        tmp = request.getParameter("NetworkType");
        try {
            networkType = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        dur.type = 99;

        tmp = request.getParameter("String1");
        if (tmp != null)
            dur.string_1 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String2");
        if (tmp != null)
            dur.string_2 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String3");
        if (tmp != null)
            dur.string_3 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String4");
        if (tmp != null)
            dur.string_4 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String5");
        if (tmp != null)
            dur.string_5 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String6");
        if (tmp != null)
            dur.string_6 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String7");
        if (tmp != null)
            dur.string_7 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String8");
        if (tmp != null)
            dur.string_8 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        tmp = request.getParameter("String9");
        if (tmp != null)
            dur.string_9 = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        tmp = request.getParameter("Number1");
        try {
            dur.number_1 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number2");
        try {
            dur.number_2 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number3");
        try {
            dur.number_3 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number4");
        try {
            dur.number_4 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number5");
        try {
            dur.number_5 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number6");
        try {
            dur.number_6 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number7");
        try {
            dur.number_7 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number8");
        try {
            dur.number_8 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        tmp = request.getParameter("Number9");
        try {
            dur.number_9 = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        double cost = 0.00;
        double maxCost = 99999.99;
        if (networkType == 1 || networkType == 3) {
            maxCost = Double.parseDouble(Utils.getParameter("servicearea_max_distance"));
            tmp = request.getParameter("Distance");
            try {
                cost = Double.parseDouble(tmp);
            } catch (Exception e) {
                ;
            }
        } else if (networkType == 2 || networkType == 4) {
            maxCost = Double.parseDouble(Utils.getParameter("servicearea_max_duration"));
            tmp = request.getParameter("Duration");
            try {
                cost = Double.parseDouble(tmp);
            } catch (Exception e) {
                ;
            }
        } else {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20061", "NetworkType parameter value is illegal.");
            return;
        }

        if (cost > maxCost) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20062", "Distance or Duration parameter value is too high. Maximum value for given NetworkType value is " + maxCost);
            return;
        }

        if (cost <= 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20063", "Distance or Duration parameter is missing depending on NetworkType value.");
            return;
        }

        int res = Operations.addServiceAreaToUserRegion(key, dur, latitude, longitude, cost, networkType);
        if (res != 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "USER REGION not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "id", dur.id);
        return;
    } // doAddServiceAreaToUserRegion()

    //-----------------------------------------------------------------------------

    private void doSocialEventList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doSocialEventList()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long id = 0;
        
        tmp = request.getParameter("Id");
        try {
            id = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        DataSocialEvent[] dses = Operations.getSocialEventList(key, id);
        if (dses == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dses);
        return;
    } // doSocialEventList()

    //-----------------------------------------------------------------------------

    private void doSocialEventSearch(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doSocialEventSearch()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        int radius = 0;
        tmp = request.getParameter("Radius");
        if (tmp != null)
            try {
                radius = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        else
            radius = Integer.parseInt(Utils.getParameter("radius"));

        int maxRadius = Integer.parseInt(Utils.getParameter("radius"));
        if ((radius > maxRadius) || (radius == 0))
            radius = maxRadius;

        int zone = DataAdminArea.getZone(latitude, longitude);

        DataSocialEvent[] dses = Operations.getSocialEventSearch(latitude, longitude, radius, zone);
        if (dses == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dses);
        return;
    } // doSocialEventSearch()

    //-----------------------------------------------------------------------------

    private void doTmcHatInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTmcHatInfo()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String tmcKod = null;
        tmp = request.getParameter("TmcKod");
        if (tmp != null && tmp.length() > 0)
            tmcKod = tmp;
        int zoomLevel = 0;
        tmp = request.getParameter("ZoomLevel");
        try {
            zoomLevel = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        boolean geometry = false;
        tmp = request.getParameter("Geometry");
        try {
            geometry = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        boolean encode = false;
        tmp = request.getParameter("Encode");
        try {
            encode = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }
        int srid = 8307;
        tmp = request.getParameter("SRID");
        try {
            srid = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        boolean reverse = false;
        tmp = request.getParameter("Reverse");
        try {
            reverse = (Integer.parseInt(tmp) != 0);
        } catch (Exception e) {
            ;
        }

        if (tmcKod == null || tmcKod.length() <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant parameters.");
            return;
        }

        int count = 0;
        if (geometry)
            count = Operations.getPackageCounter(key, PACKAGE_TMCHAT_GEOMETRISI);
        DataTmcHat dth = Operations.getTmcHatInfo(tmcKod, zoomLevel, (count > 0), srid, reverse, encode);
        if (dth == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "TMC HAT (TmcKod=" + tmcKod + ") not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dth);
        return;
    } // doTmcHatInfo()

    //-----------------------------------------------------------------------------

    private void doTmcHatListWithExtent(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTmcHatListWithExtent()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (minLatitude == 0.00 || minLongitude == 0.00 || maxLatitude == 0.00 || maxLongitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant extent coordinates.");
            return;
        }

        Extent ext = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        DataTmcHat[] dths = Operations.getTmcHatListWithExtent(ext);
        if (dths == null || dths.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "TMC HAT not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dths);
        return;
    } // doTmcHatListWithExtent()

    //-----------------------------------------------------------------------------

    private void doTmcHatSearch(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doTmcHatSearch()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Double.parseDouble(tmp);
        } catch (Exception e) {
            ;
        }

        if (latitude == 0.00 || longitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Illegal or non existant coordinates.");
            return;
        }

        DataTmcHat[] dths = Operations.getTmcHatSearch(latitude, longitude);
        if (dths == null || dths.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20042", "TMC HAT not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dths);
        return;
    } // doTmcHatSearch()

    //-----------------------------------------------------------------------------

    private void doOptMatrix(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doOptMatrix()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_ROUTE_OPTIMIZATION);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Route Optimization package support is missing.");
            return;
        }

        String tmp = null;
        DataNamedValue[] pids = null;
        tmp = request.getParameter("PointIds");
        if (tmp != null && tmp.length() > 0) {
            String[] info = Utils.splitString(tmp, ",");
            pids = new DataNamedValue[info.length];
            for (int i = 0; i < pids.length; i++) {
                String[] data = Utils.splitString(info[i], "|");
                if (data.length == 1)
                    pids[i] = new DataNamedValue("u", data[0]);
                else if (data.length == 2) {
                    if (data[0].equals("u") || data[0].equals("p"))
                        pids[i] = new DataNamedValue(data[0], data[1]);
                    else {
                        DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "`PointIds` parameter is illegally constructed.");
                        return;
                    }

                } else {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "`PointIds` parameter is garbled.");
                    return;
                }

            } // for()
        }

        if (pids == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Invalid `PointIds` parameter.");
            return;
        }

        DataPoint[] dps = OptOperations.optMatrix(key, pids);
        if (dps == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20033", "Could not write matrix data.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, dps.length);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doOptMatrix()

    //-----------------------------------------------------------------------------

    private void doOptRequest(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doOptRequest()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_ROUTE_OPTIMIZATION);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Route Optimization package support is missing.");
            return;
        }

        String tmp = null;
        String name = "";
        tmp = request.getParameter("Name");
        if (tmp != null && tmp.length() > 0)
            name = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        DataNamedValue[] pids = null;
        tmp = request.getParameter("PointIds");
        if (tmp != null && tmp.length() > 0) {
            String[] info = Utils.splitString(tmp, ",");
            pids = new DataNamedValue[info.length];
            for (int i = 0; i < pids.length; i++) {
                String[] data = Utils.splitString(info[i], "|");
                if (data.length == 1)
                    pids[i] = new DataNamedValue("u", data[0]);
                else if (data.length == 2) {
                    if (data[0].equals("u") || data[0].equals("p"))
                        pids[i] = new DataNamedValue(data[0], data[1]);
                    else {
                        DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "`PointIds` parameter is illegally constructed.");
                        return;
                    }

                } else {
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "`PointIds` parameter is garbled.");
                    return;
                }

            } // for()
        }

        if (pids == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Invalid `PointIds` parameter.");
            return;
        }

        DataVehicle[] vehicles = null;
        tmp = request.getParameter("Vehicles");
        if (tmp != null && tmp.length() > 0) {
            String[] info = Utils.splitString(tmp, ",");
            vehicles = new DataVehicle[info.length];
            for (int i = 0; i < vehicles.length; i++) {
                String[] data = Utils.splitString(info[i], "|");
                vehicles[i] = new DataVehicle(data[0], data[1], data[2]);
            } // for()
        }

        if (vehicles == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Invalid `Vehicles` parameter.");
            return;
        }

        long requestId = OptOperations.optRequest(key, name, pids, vehicles, Utils.getKeyEMail(key));
        if (requestId <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20033", "Could not write optimization request data.");
            return;
        }

        Utils.incrementAccessCount(key,transactionId, vehicles.length);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, requestId);
        return;
    } // doOptRequest()

    //-----------------------------------------------------------------------------

    private void doOptResult(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doOptResult()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_ROUTE_OPTIMIZATION);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Route Optimization package support is missing.");
            return;
        }

        String tmp = null;
        long requestId = 0;
        tmp = request.getParameter("RequestId");
        try {
            requestId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        if (requestId <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Invalid `RequestId` parameter.");
            return;
        }

        DataOptResult[] dors = OptOperations.optResult(requestId);
        if (dors == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20033", "No Optimization result.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dors);
        return;
    } // doOptResult()

    //-----------------------------------------------------------------------------

    private void doSpatialAnalysis(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doSpatialAnalysis()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_SPATIAL_ANALYSIS);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Spatial package support is missing.");
            return;
        }

        String tmp = null;
        String type = null; // Interact, Disjoint
        tmp = request.getParameter("Type");
        if (tmp != null)
            type = tmp;
        if (type == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Parameter `Type` must be supplied.");
            return;
        }

        if (!type.equalsIgnoreCase("INTERACT") && !type.equalsIgnoreCase("DISJOINT")) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Parameter `Type` can be only INTERACT or DISJOINT !");
            return;
        }

        long ilId = 0;
        tmp = request.getParameter("IlId");
        try {
            ilId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        String targetLayer = null; // UserPoint, PoiBrand, PoiCategory
        tmp = request.getParameter("TargetLayer");
        if (tmp != null)
            targetLayer = tmp;
        if (type == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Parameter `TargetLayer` must be supplied.");
            return;
        }

        String targetBrand = null;
        tmp = request.getParameter("TargetBrand");
        if (tmp != null)
            targetBrand = tmp;

        String targetCategory = null;
        tmp = request.getParameter("TargetCategory");
        if (tmp != null)
            targetCategory = tmp;

        String targetWhereClause = "";
        tmp = request.getParameter("TargetWhereClause");
        if (tmp != null)
            targetWhereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperTargetWhereClause = targetWhereClause.toUpperCase();
        if (upperTargetWhereClause.indexOf("SELECT ") >= 0 || upperTargetWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        String searchLayer = null; // UserRegion, PoiBrand, PoiCategory
        tmp = request.getParameter("SearchLayer");
        if (tmp != null)
            searchLayer = tmp;
        if (type == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Parameter `SearchLayer` must be supplied.");
            return;
        }

        String searchBrand = null;
        tmp = request.getParameter("SearchBrand");
        if (tmp != null)
            searchBrand = tmp;

        String searchCategory = null;
        tmp = request.getParameter("SearchCategory");
        if (tmp != null)
            searchCategory = tmp;

        String searchWhereClause = "";
        tmp = request.getParameter("SearchWhereClause");
        if (tmp != null)
            searchWhereClause = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        String upperSearchWhereClause = searchWhereClause.toUpperCase();
        if (upperSearchWhereClause.indexOf("SELECT ") >= 0 || upperSearchWhereClause.indexOf(" FROM ") >= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Inner SQL is not allowed in where clause.");
            return;
        }

        int searchDistance = 0;
        tmp = request.getParameter("SearchDistance");
        try {
            searchDistance = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        if (targetLayer.equalsIgnoreCase("Poi") && (ilId <= 0 || (targetBrand == null && targetCategory == null))) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Invalid (Target) Parameters. Brand or Category must be given.");
            return;
        }

        if (searchLayer.equals("Poi") && (ilId <= 0 || (searchBrand == null && searchCategory == null))) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20011", "Invalid (Search) Parameters.");
            return;
        }

        DataSpatialAnalysis dsa = Operations.spatialAnalysis(key, type, ilId, targetLayer, targetBrand, targetCategory, targetWhereClause, searchLayer, searchBrand, searchCategory, searchWhereClause, searchDistance);
        if (dsa == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20033", "No Spatial Analysis result.");
            return;
        }

        if (!dsa.errCode.equals("0")) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, dsa.errCode, dsa.errDesc);
            return;
        }

        count = 0;
        if (dsa.getPois() != null && dsa.getPois().length > 0)
            count += dsa.getPois().length;
        if (dsa.getUserPoints() != null && dsa.getUserPoints().length > 0)
            count += dsa.getPois().length;
        if (!Utils.isRequestLimitEnough(key, count)) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "11111", "Your key request limit is not enough !");
            return;
        }

        if (count <= 0)
            Utils.incrementAccessCount(key, transactionId);
        else
            Utils.incrementAccessCount(key, transactionId, count);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dsa);
        return;
    } // doSpatialAnalysis()

    //-----------------------------------------------------------------------------

    private void doEncodeGeometry(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doEncodeGeometry()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String geometry = "";
        tmp = request.getParameter("Geometry");
        if (tmp != null)
            geometry = tmp;

        String[] info = Utils.splitString(geometry, ",");
        double[] geoCoors = new double[info.length * 2];
        for (int i = 0; i < info.length; i++) {
            String[] data = Utils.splitString(info[i], "/");
            double lat = Double.parseDouble(data[0]);
            double lon = Double.parseDouble(data[1]);
            double[] coors = Utils.getTransformedCoors(lat, lon);
            geoCoors[2 * i] = coors[0];
            geoCoors[2 * i + 1] = coors[1];
        } // for()

        String pline = PolylineUtils.encodePolyline(geoCoors);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "geometry", pline);
        return;
    } // doEncodeGeometry()

    //-----------------------------------------------------------------------------

    private void doDecodeGeometry(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doDecodeGeometry()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String polyline = "";
        tmp = request.getParameter("Geometry");
        if (tmp != null)
            polyline = tmp;

        double[] geoCoors = PolylineUtils.decodePolyline(polyline);

        if (geoCoors == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "10011", "Error occured while processing. Please check parameter 'Geometry'.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, geoCoors);
        return;
    } // doDecodeGeometry()

    //-----------------------------------------------------------------------------

    private void doEncodeLevel(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doEncodeLevel()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        int level = 0;
        tmp = request.getParameter("Level");
        try {
            level = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        String encodedLevel = PolylineUtils.encodeLevel(level);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "level", encodedLevel);
        return;
    } // doEncodeLevel()

    //-----------------------------------------------------------------------------

    private void doDecodeLevel(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doDecodeLevel()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String encodedLevel = "";
        tmp = request.getParameter("Level");
        if (tmp != null)
            encodedLevel = tmp;

        int level = PolylineUtils.decodeLevel(encodedLevel);

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "level", level);
        return;
    } // doDecodeLevel()

    //-----------------------------------------------------------------------------

    private void doGetTileDefinition(String key, HttpServletRequest request, HttpServletResponse response, String typ) throws ServletException, IOException {
        String transactionId = getTransactionId();
        if (key == null || key.length() <= 0) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTRAFFICIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10012", "Error occured while processing. Please check parameter 'Key'.");
            out.close();
            return;
        }

        if (!Utils.isKeyValid(key)) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTRAFFICIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            out.close();
            return;
        }

        Utils.logLbsServiceRequest(key, transactionId, "doGetTileDefinition()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String mapType = null;
        tmp = request.getParameter("MapType");
        if (tmp != null)
            mapType = tmp.toLowerCase();

        String baseMap = Utils.getParameter("gettile_" + mapType);
        if (baseMap == null)
            baseMap = Utils.getParameter("gettile_basemap_normal");
        String httpTemplate =
            Utils.getParameter("mapviewer_url") +
            "/mcserver?xml_request=%3C%3Fxml%20version%3D%221.0%22%20standalone%3D%22yes%22%3F%3E%3Cmap_cache_admin_request%3E%3Cget_client_config%20map_cache_names%3D%22${1}%22%20format%3D%22${2}%22%2F%3E%3C%2Fmap_cache_admin_request%3E";
        String urlString = Utils.replaceParameter(httpTemplate, baseMap, typ);
        if (typ.equalsIgnoreCase("XML"))
            response.setContentType(CONTENT_TYPE_XML);
        else
            response.setContentType(CONTENT_TYPE_JSON);

        OutputStream out = null;
        InputStream inp = null;
        HttpURLConnection http = null;

        try {
            out = response.getOutputStream();
            URL url = new URL(urlString);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-type", (typ.equalsIgnoreCase("XML") ? CONTENT_TYPE_XML : CONTENT_TYPE_JSON));
            http.setDoOutput(false);
            http.setDoInput(true);
            http.connect();

            inp = http.getInputStream();
            byte b[] = new byte[10240];
            while (true) {
                int length = inp.read(b, 0, b.length);
                if (length < 0)
                    break;

                out.write(b, 0, length);
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.updateLbsServiceRequest(transactionId, "1", "Exception", "-");
            return;
        } finally {
            DbConn.closeStreamConn(inp, out);
            DbConn.closeHttpConn(http, null, null, null);
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "binary-gettiledefinition");
        return;
    } // doGetTileDefinition()

    //-----------------------------------------------------------------------------

    private void doGetTile(String key, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String transactionId = getTransactionId();

        if (key == null || key.length() <= 0) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTRAFFICIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10012", "Error occured while processing. Please check parameter 'Key'.");
            out.close();
            return;
        }

        if (!Utils.isKeyValid(key)) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTRAFFICIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            out.close();
            return;
        }

        Utils.logLbsServiceRequest(key, transactionId, "doGetTile()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String mapType = null;
        tmp = request.getParameter("MapType");
        if (tmp == null)
            tmp = request.getParameter("maptype");
        if (tmp != null)
            mapType = tmp;
        if (mapType == null)
            mapType = "normal";
        else {
            if (mapType.equalsIgnoreCase("TYPE_NORMAL"))
                mapType = "normal";
            else if (mapType.equalsIgnoreCase("TYPE_NOLOGO"))
                mapType = "nologo";
            else
                mapType = "normal";
        }

        String format = null;
        tmp = request.getParameter("Format");
        if (tmp == null)
            tmp = request.getParameter("format");
        if (tmp != null)
            format = tmp;

        String zoomLevel = null;
        tmp = request.getParameter("ZoomLevel");
        if (tmp == null)
            tmp = request.getParameter("zoomlevel");
        if (tmp != null)
            zoomLevel = tmp;

        String mx = null;
        tmp = request.getParameter("X");
        if (tmp == null)
            tmp = request.getParameter("x");
        if (tmp != null)
            mx = tmp;

        String my = null;
        tmp = request.getParameter("Y");
        if (tmp == null)
            tmp = request.getParameter("y");
        if (tmp != null)
            my = tmp;

        String baseMap = Utils.getParameter("gettile_basemap_" + mapType);
        String httpTemplate = Utils.getParameter("mapviewer_url") + "/mcserver?request=gettile&format=PNG&zoomlevel=${2}&mapcache=${1}&mx=${3}&my=${4}";
        String urlString = Utils.replaceParameter(httpTemplate, baseMap, zoomLevel, mx, my);

        OutputStream out = null;
        InputStream inp = null;
        HttpURLConnection http = null;

        try {
            out = response.getOutputStream();
            URL url = new URL(urlString);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-type", CONTENT_TYPE_PNG);
            http.setDoOutput(false);
            http.setDoInput(true);
            http.connect();

            inp = http.getInputStream();
            byte b[] = new byte[10240];
            while (true) {
                int length = inp.read(b, 0, b.length);
                if (length < 0)
                    break;

                out.write(b, 0, length);
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.updateLbsServiceRequest(transactionId, "1", "Exception", "-");
            return;
        } finally {
            DbConn.closeHttpConn(http, null, null, null);
            DbConn.closeStreamConn(inp, out);
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "binary-gettile");
        return;
    } // doGetTile()

    //-----------------------------------------------------------------------------

    private void doGetUserImage(String key, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        String transactionId = getTransactionId();

        if (key == null || key.length() <= 0) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: GETUSERIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10012", "Error occured while processing. Please check parameter 'Key'.");
            out.close();
            return;
        }

        if (!Utils.isKeyValid(key)) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: GETUSERIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            out.close();
            return;
        }

        String referer = request.getHeader("Referer");
        Utils.logInfo("REFERRER: " + referer);
        Utils.logInfo("KEY: " + key);

        String domain = Utils.getDomainFromReferer(referer);
        if (!Utils.isKeyDomainValid(key, domain)) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: GETUSERIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10015", "The key used is not active for used domain " + domain + " ! Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            out.close();
            return;
        }

        Utils.logLbsServiceRequest(key, transactionId, "doGetUserImage()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        String id = null;
        tmp = request.getParameter("Id");
        if (tmp != null)
            id = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
        int seqNo = 0;
        tmp = request.getParameter("SeqNo");
        if (tmp != null && tmp.length() > 0)
            try {
                seqNo = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }
        int imgType = 0;
        tmp = request.getParameter("ImgType");
        if (tmp != null && tmp.length() > 0)
            try {
                imgType = Integer.parseInt(tmp);
            } catch (Exception e) {
                ;
            }

        Connection cnn = null;
        InputStream inp = null;
        OutputStream out = null;
        String contentType = null;

        try {
            out = response.getOutputStream();
            cnn = DbConn.getPooledConnection();
            sql = "SELECT IMAGE FROM LBS_USER_IMAGE WHERE KEY=? AND ID=? AND SEQNO=? AND TYP=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            pstmt.setInt(colno++, seqNo);
            pstmt.setInt(colno++, imgType);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                BLOB blob = DbConn.convToBLOB(rset.getObject(1));
                //        BLOB blob = ((OracleResultSet)rset).getBLOB(1);
                inp = blob.binaryStreamValue();
                byte[] bytes = new byte[10240];
                while (true) {
                    int length = inp.read(bytes, 0, bytes.length);
                    if (length <= 0)
                        break;

                    if (contentType == null) {
                        if (bytes[0] == 'G' || bytes[1] == 'I' || bytes[2] == 'F')
                            contentType = CONTENT_TYPE_GIF;
                        else if (bytes[1] == 'P' || bytes[2] == 'N' || bytes[3] == 'G')
                            contentType = CONTENT_TYPE_PNG;
                        else if (bytes[6] == 'J' || bytes[7] == 'F' || bytes[8] == 'I' || bytes[9] == 'F')
                            contentType = CONTENT_TYPE_JPG;
                        else
                            contentType = CONTENT_TYPE_GIF;

                        response.setContentType(contentType);
                    }

                    out.write(bytes, 0, length);
                } // while()
            } // if()
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.updateLbsServiceRequest(transactionId, "1", "Failed", "binary-image");
            return;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
            DbConn.closeStreamConn(inp, out);            
        }

        Utils.incrementAccessCount(key, transactionId);
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "binary-image");
        return;
    } // doGetUserImage()

    //-----------------------------------------------------------------------------

    private void doMapImage(String key, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String transactionId = getTransactionId();

        if (key == null || key.length() <= 0) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10012", "Error occured while processing. Please check parameter 'Key'.");
            DbConn.closeHttpConn(null, null, out, null);
            return;
        }

        if (!Utils.isKeyValid(key)) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            DbConn.closeHttpConn(null, null, out, null);
            return;
        }

        String referer = request.getHeader("Referer");
        Utils.logInfo("REFERRER: " + referer);
        Utils.logInfo("KEY: " + key);

        String domain = Utils.getDomainFromReferer(referer);
        if (!Utils.isKeyDomainValid(key, domain)) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10014", "The key used is not active for used domain " + domain + " ! Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            DbConn.closeHttpConn(null, null, out, null);
            return;
        }

        Utils.logLbsServiceRequest(key, transactionId, "doMapImage()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double[] coors = Utils.getTransformedCoors(latitude, longitude);
        longitude = coors[0];
        latitude = coors[1];

        int zoomLevel = 0;
        tmp = request.getParameter("ZoomLevel");
        try {
            zoomLevel = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int pan = 0;
        tmp = request.getParameter("Pan");
        try {
            pan = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        int width = 600;
        tmp = request.getParameter("Width");
        try {
            width = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int height = 400;
        tmp = request.getParameter("Height");
        try {
            height = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        int basemap = 0;
        tmp = request.getParameter("Basemap");
        try {
            basemap = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        String points = "";
        tmp = request.getParameter("Points");
        if (tmp != null)
            points = tmp;

        String[] info = Utils.splitString(points, ",");
        DataPoint[] dps = new DataPoint[info.length];
        for (int i = 0; i < info.length; i++) {
            String[] data = Utils.splitString(info[i], "/");
            if (data.length == 2) {
                double lat = Double.parseDouble(data[0]);
                double lon = Double.parseDouble(data[1]);
                coors = Utils.getTransformedCoors(lat, lon);
                dps[i] = new DataPoint(coors[1], coors[0]);
            } else if (data.length == 3) {
                int sym = Integer.parseInt(data[0]);
                double lat = Double.parseDouble(data[1]);
                double lon = Double.parseDouble(data[2]);
                coors = Utils.getTransformedCoors(lat, lon);
                dps[i] = new DataPoint(sym, coors[1], coors[0]);
            }
        } // for()
        DataMap dm = null;
        dm.setLatitude(latitude);
        dm.setLongitude(longitude);
        dm.setZoomLevel(zoomLevel);
        dm.setPan(pan);
        dm.setWidth(width);
        dm.setHeight(height);
        dm.setPoints(dps);
        dm.setBaseMap(basemap);

        dm = Operations.getDataMap(dm, false);
        if (dm == null) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.updateLbsServiceRequest(transactionId, "10014", "Failed to generate map image in server", "-");
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10014", "Failed to generate map image in server");
            DbConn.closeHttpConn(null, null, out, null);
            return;
        }

        response.setContentType(CONTENT_TYPE_PNG);
        OutputStream out = null;
        InputStream inp = null;
        HttpURLConnection http = null;

        try {
            out = response.getOutputStream();
            URL url = new URL(dm.getUrl());
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-type", "image/gif");
            http.setDoOutput(false);
            http.setDoInput(true);
            http.connect();

            inp = http.getInputStream();
            byte b[] = new byte[10240];
            while (true) {
                int length = inp.read(b, 0, b.length);
                if (length < 0)
                    break;

                out.write(b, 0, length);
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.updateLbsServiceRequest(transactionId, "1", "Failed", "-");
            return;
        } finally {
            DbConn.closeStreamConn(inp, out);
            DbConn.closeHttpConn(http, null, null, null);
        }

        Utils.incrementAccessCount(key, transactionId);
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "binary-map-image");
        return;
    } // doMapImage()

    //-----------------------------------------------------------------------------

    private void doMapTrafficImage(String key, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String transactionId = getTransactionId();
        PrintWriter pw = null;
        try {
            
        if (key == null || key.length() <= 0) {
            response.setContentType(CONTENT_TYPE_XML);
            pw = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTRAFFICIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(pw, "XML", transactionId, null, "10012", "Error occured while processing. Please check parameter 'Key'.");
            return;
        }

        if (!Utils.isKeyValid(key)) {
            response.setContentType(CONTENT_TYPE_XML);
            pw = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTRAFFICIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(pw, "XML", transactionId, null, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            return;
        }
        String referer = request.getHeader("Referer");
        Utils.logInfo("REFERRER: " + referer);
        Utils.logInfo("KEY: " + key);

        String domain = Utils.getDomainFromReferer(referer);
        if (!Utils.isKeyDomainValid(key, domain)) {
            response.setContentType(CONTENT_TYPE_XML);
            pw = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTRAFFICIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(pw, "XML", transactionId, null, "10014", "The key used is not active for used domain " + domain + " ! Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            return;
        }

        int count = Operations.getPackageCounter(key, PACKAGE_TRAFIK);
        if (count <= 0) {
            response.setContentType(CONTENT_TYPE_XML);
            pw = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: MAPTRAFFICIMAGE", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(pw, "XML", transactionId, null, "30011", "Traffic package support is missing.");
            return;
        }
        }catch (Exception e){
           ; 
        }finally{
            DbConn.closeHttpConn(null, null, pw, null);
        }
        Utils.logLbsServiceRequest(key, transactionId, "doMapTrafficImage()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        double minLatitude = 0.00;
        tmp = request.getParameter("MinLatitude");
        try {
            minLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double minLongitude = 0.00;
        tmp = request.getParameter("MinLongitude");
        try {
            minLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double[] coors = Utils.getTransformedCoors(minLatitude, minLongitude);
        minLongitude = coors[0];
        minLatitude = coors[1];

        double maxLatitude = 0.00;
        tmp = request.getParameter("MaxLatitude");
        try {
            maxLatitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double maxLongitude = 0.00;
        tmp = request.getParameter("MaxLongitude");
        try {
            maxLongitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        coors = Utils.getTransformedCoors(maxLatitude, maxLongitude);
        maxLongitude = coors[0];
        maxLatitude = coors[1];

        int width = 600;
        tmp = request.getParameter("Width");
        try {
            width = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }
        int height = 400;
        tmp = request.getParameter("Height");
        try {
            height = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        String pathIds = null;
        tmp = request.getParameter("PathId");
        if (tmp != null)
            pathIds = tmp;

        boolean flow = false;
        tmp = request.getParameter("Flow");
        try {
            flow = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        boolean event = false;
        tmp = request.getParameter("Event");
        try {
            event = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        boolean tmc = false;
        tmp = request.getParameter("TMC");
        try {
            tmc = Integer.parseInt(tmp) != 0;
        } catch (Exception e) {
            ;
        }

        int basemap = 0;
        tmp = request.getParameter("Basemap");
        try {
            basemap = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        DataTrafficEvent[] dtes = null;
        if (event)
            dtes = Operations.getTrafficEventSearch((minLatitude + maxLatitude) / 2.0, (minLongitude + maxLongitude) / 2.0, 100000, -1);

        DataMap dm = Operations.getDataMap_Traffic(minLatitude, minLongitude, maxLatitude, maxLongitude, width, height, pathIds, flow, tmc, dtes, basemap, false);
        if (dm == null) {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter out = response.getWriter();
            Utils.updateLbsServiceRequest(transactionId, "10014", "Failed to generate map traffic image in server", "-");
            DataResponse.sendErrorResponse(out, "XML", transactionId, null, "10014", "Failed to generate map traffic image in server");
            DbConn.closeHttpConn(null, null, out, null);
            return;
        }

        response.setContentType(CONTENT_TYPE_PNG);
        OutputStream out = null;
        InputStream inp = null;
        HttpURLConnection http = null;

        try {
            out = response.getOutputStream();
            URL url = new URL(dm.getUrl());
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-type", "image/png");
            http.setDoOutput(false);
            http.setDoInput(true);
            http.connect();

            inp = http.getInputStream();
            byte b[] = new byte[10240];
            while (true) {
                int length = inp.read(b, 0, b.length);
                if (length < 0)
                    break;

                out.write(b, 0, length);
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.updateLbsServiceRequest(transactionId, "1", "Exception", "-");
            return;
        } finally {
            DbConn.closeStreamConn(inp, out);
            DbConn.closeHttpConn(http, null, null, null);
        }

        Utils.incrementAccessCount(key, transactionId);
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "binary-map-traffic-image");
        return;
    } // doMapTrafficImage()

    //-----------------------------------------------------------------------------

    private void doWeatherReport(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doWeatherReport()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_WEATHER_REPORT);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Weather Report package support is missing.");
            return;
        }

        DataWeatherReport dwr = null;

        String tmp = null;
        long ilId = 0;

        tmp = request.getParameter("IlId");
        if (tmp != null)
            ilId = Long.parseLong(tmp);

        try {
            dwr = Operations.getWeatherReport(ilId);
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.showError("EXCEPTION: " + ex.getMessage());
        }

        if (dwr == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, null, "10014", "Data not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dwr);
        return;
    } // doWeatherReport()

    //-----------------------------------------------------------------------------

    public void doSetPoiAttribute(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doSetPoiAttribute()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;

        DataPoiAttribute dpa = new DataPoiAttribute();

        tmp = request.getParameter("PoiId");
        if (tmp != null)
            dpa.poiId = Long.parseLong(tmp);
        tmp = request.getParameter("Attribute");
        if (tmp != null)
            dpa.attribute = tmp.toUpperCase();
        tmp = request.getParameter("Type");
        if (tmp != null)
            dpa.type = Integer.parseInt(tmp);
        tmp = request.getParameter("Value");
        if (tmp != null)
            dpa.value = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));

        if (dpa.value == null || dpa.poiId == 0 || dpa.type == 0 || dpa.attribute == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "'PoiId', 'Attribute', 'Type' and 'Value' parameters must be supplied.");
            return;
        }

        for (int i = 0; i < poiReservedWords.length; i++) {
            if (poiReservedWords[i].equalsIgnoreCase(dpa.attribute)) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Attribute name in reserved words. Please use another Attribute name.");
                return;
            }
        }

        for (int i = 0; i < sqlReservedWords.length; i++) {
            if (sqlReservedWords[i].equalsIgnoreCase(dpa.attribute)) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Attribute name in reserved words. Please use another Attribute name.");
                return;
            }
        }

        int attType = Operations.checkAttributeType(key, dpa.attribute);
        if (attType != 0 && attType != dpa.type) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "This attribute is defined in a different type before.");
            return;
        }

        int res = Operations.setPoiAttribute(key, dpa);
        if (res < 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doSetPoiAttribute()

    //-----------------------------------------------------------------------------

    public void doRemovePoiAttribute(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doRemovePoiAttribute()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;

        DataPoiAttribute dpa = new DataPoiAttribute();

        tmp = request.getParameter("PoiId");
        if (tmp != null)
            dpa.poiId = Long.parseLong(tmp);
        tmp = request.getParameter("Attribute");
        if (tmp != null)
            dpa.attribute = tmp.toUpperCase();

        if (dpa.poiId == 0 || dpa.attribute == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "'PoiId' and 'Attribute' parameters must be supplied.");
            return;
        }

        int res = Operations.removePoiAttribute(key, dpa);
        if (res < 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }
        if (res == 1) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Attribute not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doRemovePoiAttribute()

    //-----------------------------------------------------------------------------

    public void doIncrementPoiAttribute(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doIncrementPoiAttribute()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;

        DataPoiAttribute dpa = new DataPoiAttribute();

        tmp = request.getParameter("PoiId");
        if (tmp != null)
            dpa.poiId = Long.parseLong(tmp);
        tmp = request.getParameter("Attribute");
        if (tmp != null)
            dpa.attribute = tmp.toUpperCase();
        tmp = request.getParameter("Type");
        if (tmp != null)
            dpa.type = Integer.parseInt(tmp);
        tmp = request.getParameter("Value");
        if (tmp != null)
            dpa.value = tmp;

        if (dpa.type == 2) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "'DataType' parameter cannot equal to 2 (String type) in Increment function.");
            return;
        }

        if (dpa.value == null || dpa.poiId == 0 || dpa.type == 0 || dpa.attribute == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "'PoiId', 'Attribute', 'Type' and 'Value' parameters must be supplied.");
            return;
        }

        for (int i = 0; i < poiReservedWords.length; i++) {
            if (poiReservedWords[i].equalsIgnoreCase(dpa.attribute)) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Attribute name in reserved words. Please use another Attribute name.");
                return;
            }
        }

        for (int i = 0; i < sqlReservedWords.length; i++) {
            if (sqlReservedWords[i].equalsIgnoreCase(dpa.attribute)) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Attribute name in reserved words. Please use another Attribute name.");
                return;
            }
        }

        int res = Operations.incrementPoiAttribute(key, dpa);
        if (res < 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doIncrementPoiAttribute()

    //-----------------------------------------------------------------------------


    public void doDecrementPoiAttribute(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doDecrementPoiAttribute()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;

        DataPoiAttribute dpa = new DataPoiAttribute();

        tmp = request.getParameter("PoiId");
        if (tmp != null)
            dpa.poiId = Long.parseLong(tmp);
        tmp = request.getParameter("Attribute");
        if (tmp != null)
            dpa.attribute = tmp.toUpperCase();
        tmp = request.getParameter("Value");
        if (tmp != null)
            dpa.value = tmp;
        tmp = request.getParameter("Type");
        if (tmp != null)
            dpa.type = Integer.parseInt(tmp);


        if (dpa.type == 2) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "'DataType' parameter can not equal to 2 (String type) in Decrement function.");
            return;
        }

        if (dpa.value == null || dpa.poiId == 0 || dpa.type == 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "'PoiId', 'Attribute', 'DataType', and 'Value' parameters must be supplied.");
            return;
        }

        for (int i = 0; i < poiReservedWords.length; i++) {
            if (poiReservedWords[i].equalsIgnoreCase(dpa.attribute)) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Attribute name in reserved words. Please use another Attribute name.");
                return;
            }
        }

        for (int i = 0; i < sqlReservedWords.length; i++) {
            if (sqlReservedWords[i].equalsIgnoreCase(dpa.attribute)) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Attribute name in reserved words. Please use another Attribute name.");
                return;
            }
        }

        int res = Operations.decrementPoiAttribute(key, dpa);
        if (res < 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback);
        return;
    } // doDecrementPoiAttribute()

    //-----------------------------------------------------------------------------

    public void doClusterPoints(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doClusterPoints()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_CLUSTER);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Cluster package support is missing.");
            return;
        }

        String tmp = null;
        String points = "";
        tmp = request.getParameter("Coors");
        if (tmp != null)
            points = tmp;
        String[] info = Utils.splitString(points, ",");
        DataPoint[] dps = new DataPoint[info.length];
        for (int i = 0; i < info.length; i++) {
            String[] data = Utils.splitString(info[i], "/");
            String name = data[0];
            double lat = Float.valueOf(data[1]);
            double lon = Float.valueOf(data[2]);
            dps[i] = new DataPoint(name, 0, lat, lon, 0.00);
        } // for()

        int cluster = 0;
        tmp = request.getParameter("Cluster");
        if (tmp != null)
            cluster = Integer.parseInt(tmp);

        if (dps.length > 10000) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "Points cannot be more than 10000.");
            return;
        }

        if (cluster < 1) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Cluster' parameter must be supplied and should be equal at least 1.");
            return;
        }

        if (dps.length < cluster) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Coors' length must be bigger than Cluster number.");
            return;
        }

        if (dps == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Coors' must be supplied.");
            return;
        }

        DataClusterPoint dcp = Operations.getClusterPoints(dps, cluster);
        if (dcp == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dcp);
        return;
    } // doClusterPoints()

    //-----------------------------------------------------------------------------

    public void doGetPhoneAddressScore(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetPhoneAddressScore()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_PHONEBOOK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Phonebook package support is missing.");
            return;
        }

        String tmp = null;

        long id = 0;
        tmp = request.getParameter("Id");
        if (tmp != null && tmp.length() > 0)
            try {
                id = Long.parseLong(tmp);
            } catch (Exception e) {
                ;
            }

        String firstName = null;
        tmp = request.getParameter("FirstName");
        if (tmp != null)
            firstName = Utils.convUtf8ToTurkish(tmp);

        String lastName = null;
        tmp = request.getParameter("LastName");
        if (tmp != null)
            lastName = Utils.convUtf8ToTurkish(tmp);

        String address = null;
        tmp = request.getParameter("Address");
        if (tmp != null)
            address = Utils.convUtf8ToTurkish(tmp);

        String serviceKey = Utils.getPhonebookKey(key);
        if (serviceKey == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20072", "Phonebook Key not found.");
            return;
        }

        DataPhonebook[] dpbl = Operations.getGetPhoneAddressScore(key, serviceKey, id, firstName, lastName, address);

        if (dpbl == null || dpbl.length <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Record not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId, dpbl.length);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dpbl);
        return;
    } // doGetPhoneAddressScore()

    //-----------------------------------------------------------------------------

    private void doGetNearestPointOnYol(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetNearestPointOnYol()", request.getQueryString(), request.getRemoteAddr());
        DataPoint dp = null;
        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            latitude = 0.00;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            longitude = 0.00;
        }

        if (latitude == 0.00 || longitude == 0.00) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20900", "Error occured while processing. Please check parameters 'Latitude and Longitude'.");
            return;
        }

        dp = Operations.getNearestPointOnYol(latitude, longitude);
        if (dp == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20901", "Point cannot be found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, "Latitude", dp);
        return;
    } // doGetNearestPointOnYol()

    //-----------------------------------------------------------------------------

    private void doGetKeyStatus(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetKeyStatus()", request.getQueryString(), request.getRemoteAddr());

        DataKeyStatus dks = null;
        String tmp = null;
        tmp = request.getParameter("Key");

        dks = Operations.getKeyStatus(tmp);
        DataPackage[] dps = Operations.getMyPackages(tmp);

        if (dks == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20901", "Key cannot be found.");
            return;
        }

        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dks, dps);
        return;

    } // doGetKeyStatus()

    //-----------------------------------------------------------------------------

    private void doGetMaxYolSpeed(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetMaxYolSpeed()", request.getQueryString(), request.getRemoteAddr());

        DataMaxYolSpeed dmys = null;

        String tmp = null;
        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        dmys = Operations.getMaxYolSpeed(latitude, longitude);

        if (dmys == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20901", "Distance cannot be calculated.");
            return;
        }
        
        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dmys);
        return;
    } // doGetMaxYolSpeed()

    //-----------------------------------------------------------------------------

    private void doGetEarthQuakeInfo(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doGetEarthQuakeInfo()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_EARTHQUAKE);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Earthquake package support is missing.");
            return;
        }

        DataEarthQuake deq = null;

        String tmp = null;

        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            ;
        }

        long radius = 0;
        tmp = request.getParameter("Radius");
        try {
            radius = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        if (radius > 100000 || radius == 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20902", "Radius must be between 0 and 100000");
            return;
        }

        deq = Operations.getEarthQuakeInfo(latitude, longitude, radius);

        if (deq == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20901", "Earthquake not found.");
            return;
        }

        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, deq);
        return;
    } // doGetEarthQuakeInfo()

    //-----------------------------------------------------------------------------

    private void doIndoorVenueList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doIndoorVenueList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_INDOOR);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Indoor package support is missing.");
            return;
        }

        DataIndoorVenue[] divs = Operations.getIndoorVenueList();

        if (divs == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20901", "Venue not found.");
            return;
        }

        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, divs);
        return;
    } // doIndoorVenueList()

    //-----------------------------------------------------------------------------

    private void doIndoorPoiList(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doIndoorPoiList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_INDOOR);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Indoor package support is missing.");
            return;
        }

        String tmp = null;

        long venueId = 0;
        tmp = request.getParameter("VenueId");
        try {
            venueId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        int floorLevel = -100;
        tmp = request.getParameter("FloorLevel");
        try {
            floorLevel = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        String category = null;
        tmp = request.getParameter("Category");
        try {
            category = tmp;
        } catch (Exception e) {
            category = null;
        }

        DataIndoorPoi[] dips = Operations.getIndoorPoiList(venueId, floorLevel, category);

        if (dips == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20901", "Venue not found.");
            return;
        }

        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dips);
        return;
    } // doIndoorPoiList()

    //-----------------------------------------------------------------------------

    private void doIndoorPoiSearch(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doIndoorPoiList()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_INDOOR);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Indoor package support is missing.");
            return;
        }

        String tmp = null;

        long venueId = 0;
        tmp = request.getParameter("VenueId");
        try {
            venueId = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }

        int floorLevel = -100;
        tmp = request.getParameter("FloorLevel");
        try {
            floorLevel = Integer.parseInt(tmp);
        } catch (Exception e) {
            floorLevel = -100;
        }

        double latitude = 0.00;
        tmp = request.getParameter("Latitude");
        try {
            latitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            latitude = 0.00;
        }
        double longitude = 0.00;
        tmp = request.getParameter("Longitude");
        try {
            longitude = Utils.makeDegree(tmp);
        } catch (Exception e) {
            longitude = 0.00;
        }

        long radius = 0;
        tmp = request.getParameter("Radius");
        try {
            radius = Long.parseLong(tmp);
            if (radius > 15000)
                radius = 15000;
        } catch (Exception e) {
            radius = 0;
        }

        String category = null;
        tmp = request.getParameter("Category");
        try {
            category = tmp;
        } catch (Exception e) {
            category = null;
        }

        DataIndoorPoi[] dips = Operations.getIndoorPoiSearch(venueId, floorLevel, latitude, longitude, radius, category);

        if (dips == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20901", "Poi not found.");
            return;
        }
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dips);
        return;
    } // doIndoorPoiList()

    //-----------------------------------------------------------------------------


    private void doWhat3Words(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doWhat3Words()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_WHAT3WORDS);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "What3Words package support is missing.");
            return;
        }

        What3Words what3Words = null;

        //Default language value en
        String lang = "en";

        String tmp = null;
        String words = null;
        String position = null;

        tmp = request.getParameter("Words");
        if (tmp != null)
            words = tmp;

        tmp = null;
        tmp = request.getParameter("Position");
        if (tmp != null)
            position = tmp;

        tmp = null;
        tmp = request.getParameter("Lang");
        if (tmp != null)
            lang = tmp;

        if (words != null) {
            String[] wordsBySplit = words.split("\\.");

            if (wordsBySplit != null && (words.length() > 40 || wordsBySplit.length > 3)) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "Words is no good.");
                return;
            }

            words = Utils.convUtf8ToTurkish(words);

            String res = doWhat3WordsRequest(lang, null, words);
            if (res != null)
                what3Words = Operations.getWhat3Words(res);

        } // if()

        else if (position != null) {

            String[] info = Utils.splitString(position, ",");

            if (info != null && info.length != 2) {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20015", "Invalid coordinates.");
                return;
            } else {
                try {
                    Double.parseDouble(info[0]);
                    Double.parseDouble(info[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                    DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20015", "Invalid coordinates.");
                    return;
                }
            }

            String res = doWhat3WordsRequest(lang, position, null);
            if (res != null)
                what3Words = Operations.getWhat3Words(res);

        }

        if (what3Words == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Data not found.");
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, what3Words);
        return;
    } // doWhat3Words()

    //-----------------------------------------------------------------------------

    public String doWhat3WordsRequest(String lang, String position, String words) {
        HttpURLConnection connection = null;
        String inputLine = null;
        String what3wordsUrl = null;

        if (words != null) {
            try {
                words = URLEncoder.encode(words, "UTF-8");
                what3wordsUrl = Utils.getParameter("what3words_words_url") + "?format=json&key=" + Utils.getParameter("what3words_key") + "&display=minimal&lang=" + lang + "&addr=" + words;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (position != null) {
            what3wordsUrl = Utils.getParameter("what3words_position_url") + "?format=json&key=" + Utils.getParameter("what3words_key") + "&display=minimal&lang=" + lang + "&coords=" + position;
        } else {
            return null;
        }

        what3wordsUrl = Utils.convUtf8ToTurkish(what3wordsUrl);
        BufferedReader in = null;
        String res = "";

        try {
            URL lurl = new URL(what3wordsUrl);
            connection = (HttpURLConnection) lurl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString("".getBytes().length));
            connection.setUseCaches(false);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                res = res + inputLine;
            }
          res = Utils.convUtf8ToTurkish(res);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            DbConn.closeHttpConn(connection, null, null, in);
        }

        return res;
    }

    //-----------------------------------------------------------------------------

    private void doImageIndex(String key, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String transactionId = getTransactionId();
        PrintWriter pw = null;
        try {
        if (key == null || key.length() <= 0) {
            response.setContentType(CONTENT_TYPE_XML);
            pw = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: IMAGEINDEX", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(pw, "XML", transactionId, null, "10012", "Error occured while processing. Please check parameter 'Key'.");
            return;
        }

        if (!Utils.isKeyValid(key)) {
            response.setContentType(CONTENT_TYPE_XML);
            pw = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: IMAGEINDEX", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(pw, "XML", transactionId, null, "10013", "The key used is not registered or not active. Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            return;
        }

        String referer = request.getHeader("Referer");
        Utils.logInfo("REFERRER: " + referer);
        Utils.logInfo("KEY: " + key);

        String domain = Utils.getDomainFromReferer(referer);
        if (!Utils.isKeyDomainValid(key, domain)) {
            response.setContentType(CONTENT_TYPE_XML);
            pw = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: IMAGEINDEX", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(pw, "XML", transactionId, null, "10014", "The key used is not active for used domain " + domain + " ! Please check parameter 'Key'. (contact email is kurumsal@locationbox.com.tr)");
            return;
        }

        int count = Operations.getPackageCounter(key, PACKAGE_IMAGE_INDEX);
        if (count <= 0) {
            response.setContentType(CONTENT_TYPE_XML);
            pw = response.getWriter();
            Utils.logLbsServiceRequest(key, transactionId, "CMD: IMAGEINDEX", request.getQueryString(), request.getRemoteAddr());
            DataResponse.sendErrorResponse(pw, "XML", transactionId, null, "30011", "Image Index package support is missing.");
            return;
        }
        }catch (Exception e){
           ; 
        }finally{
            DbConn.closeHttpConn(null, null, pw, null); 
        }
        Utils.logLbsServiceRequest(key, transactionId, "doImageIndex()", request.getQueryString(), request.getRemoteAddr());

        String tmp = null;
        long id = 0;
        tmp = request.getParameter("Id");
        try {
            id = Long.parseLong(tmp);
        } catch (Exception e) {
            ;
        }
        int thumbnail = 0;
        tmp = request.getParameter("Thumbnail");
        try {
            thumbnail = Integer.parseInt(tmp);
        } catch (Exception e) {
            ;
        }

        String imagePath = null;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            cnn = DbConn.getPooledConnection();
            String sql = "SELECT FILE_PATH FROM LBS_IMAGE_INDEX WHERE ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                imagePath = rset.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }
        if (imagePath != null && 1 < imagePath.length()) {
            OutputStream outStr = null;
            response.setContentType(CONTENT_TYPE_JPG);
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            try {
                outStr = response.getOutputStream();
                BufferedImage resizedBuffImg = null;
                BufferedImage bufImg = ImageIO.read(Files.newInputStream(Paths.get(imagePath)));

                if (thumbnail <= 0) {
                    ImageIO.write(bufImg, "jpg", outStr);
                } else {
                    resizedBuffImg = new BufferedImage(100, 100, bufImg.getType());
                    Graphics2D g2d = resizedBuffImg.createGraphics();
                    g2d.drawImage(bufImg, 0, 0, 100, 100, null);
                    g2d.dispose();
                    ImageIO.write(resizedBuffImg, "jpg", outStr);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                response.setContentType(CONTENT_TYPE_XML);
                PrintWriter out = response.getWriter();
                Utils.updateLbsServiceRequest(transactionId, "100141", "Could not resize image on server", "-");
                DataResponse.sendErrorResponse(out, "XML", transactionId, null, "100141", "Could not resize image on server");
                DbConn.closeHttpConn(null, null, out, null); 
                return;
            } finally {
                DbConn.closeStreamConn(null, outStr);
            }
        } else {
            response.setContentType(CONTENT_TYPE_XML);
            PrintWriter printOut = response.getWriter();
            Utils.updateLbsServiceRequest(transactionId, "10014", "Could not find image in server", "-");
            DataResponse.sendErrorResponse(printOut, "XML", transactionId, null, "10014", "Could not find image in server");
            DbConn.closeHttpConn(null, null, printOut, null); 
            return;
        }

        Utils.incrementAccessCount(key, transactionId);
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", "binary-image-index-image");
        return;
    } // doImageIndex()

    //-----------------------------------------------------------------------------
    
    private void doBBolumFeedback(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doBBolumFeedback()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_FEEDBACK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Feedback package support is missing.");
            return;
        }

        String tmp = null;
        long id = 0L;
        tmp = request.getParameter("Id");
        if(tmp == null) tmp = request.getParameter("id");
        try {
            id = Long.parseLong(tmp);
        } catch (Exception e) { ; }
        
        long bolumUavt = 0L;
        tmp = request.getParameter("BBolumUavt");
        if(tmp == null) tmp = request.getParameter("bbolumuavt");
        try {
            bolumUavt = Long.parseLong(tmp);
        } catch (Exception e) { ; }
        
        String address = null;
        tmp = request.getParameter("Address");
        if(tmp == null) tmp = request.getParameter("address");
         address = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
         
        if ( id == 0 && (address == null || address.length()<2) && bolumUavt == 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Address' or 'BBolumUavt' must be supplied.");
            return;
        }
        
        String adbVersion = null;
        tmp = request.getParameter("ADBVERSION");
        if(tmp == null) tmp = request.getParameter("AdbVersion");
        if(tmp == null) tmp = request.getParameter("adbversion");
         adbVersion = Utils.convUtf8ToTurkish(Utils.decodeEscape(tmp));
         
        if ( id == 0 && (address == null || address.length()<2) && bolumUavt == 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20013", "'Address' or 'BBolumUavt' must be supplied.");
            return;
        }
        
        
        int responseType = 0;
        DataBBolumFeedback dbf = null;
        if( id == 0 ){
            
           String emailAddress = Utils.getParameter("feedback_email_address");

           dbf = Operations.addBBolumFeedback(key, id, bolumUavt, address, emailAddress, adbVersion);
           if (dbf == null)  {
                DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20010", "Error occured while processing. Please try again later");
                return;
            }
        }else{
            responseType = 1;
            dbf = Operations.getBBolumFeedback(key, id);
            if (dbf == null) {
                 DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Record not found.");
                 return;
             }
        }
        
        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dbf, responseType);
        
        return;
    } // doBBolumFeedBack()
    
    //-----------------------------------------------------------------------------
    
    private void doListFeedback(HttpServletRequest request, PrintWriter out, String typ, String key, String transactionId, String callback) {
        Utils.logLbsServiceRequest(key, transactionId, "doListFeedback()", request.getQueryString(), request.getRemoteAddr());

        int count = Operations.getPackageCounter(key, PACKAGE_FEEDBACK);
        if (count <= 0) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "30011", "Feedback package support is missing.");
            return;
        }

        String tmp = null;
        int processed = 0;
        tmp = request.getParameter("Processed");
        if(tmp == null) tmp = request.getParameter("processed");
        try {
            processed = Integer.parseInt(tmp);
        } catch (Exception e) { ; }

       List<DataBBolumFeedback> dbf = Operations.getListFeedback(key, processed);

      if (dbf == null) {
            DataResponse.sendErrorResponse(out, typ, transactionId, callback, "20012", "Record not found.");
            return;
        }
        Utils.incrementAccessCount(key, transactionId);
        DataResponse.sendSuccessResponse(out, typ, transactionId, callback, dbf);
        return;
    } //doListFeedback()

    //-----------------------------------------------------------------------------

    private String getTransactionId() {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        String transactionId = "LBS_";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SEQ_LBS_REFERENCE.NEXTVAL FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            if (rset.next()) {
                transactionId += rset.getString(1);
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return transactionId;
    } // getTransactionId()
    
    private void setAccessControlHeaders(HttpServletResponse resp) {
      resp.setHeader("Access-Control-Allow-Origin", "*");
      resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
    }
    
    String padLeft(String txt, int length) {
       StringBuffer str = new StringBuffer(txt);
       int strlength = str.length();
       if( length <= strlength )
         return txt;
       for( int i = 0; i < length - strlength; i++ ) 
         str.insert(0, "0");
       return str.toString();
     } // padLeft()

     String padLeft(int num, int length) {
       String txt = "" + num;
       StringBuffer str = new StringBuffer(txt);
       int strlength = str.length();
       if( length <= strlength )return txt;
       
       for( int i = 0; i < length - strlength; i++ ) str.insert(0, "0");
       return str.toString();
     } // padLeft()
    
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    
}
