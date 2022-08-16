package com.infotech.locationbox.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.infotech.address.cleaner.AppMain;
import com.infotech.inforiskskor.InfoRiskSkor;
import com.infotech.inforiskskor.SkorResultInfo;
import java.io.PrintWriter;
import java.util.List;


public class DataResponse {
    private static final String XML_HEADER_STRING = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static final int STATUS_SUCCESS = 0;
    private static final int STATUS_FAIL = 1;
    private static final int STATUS_WAIT = 2;

    private static Gson gson = null;
    private static JsonParser jp = null;
    private static JsonElement je = null;
    private static String addressCleanerVersion = null;
    private static String dataVersion = null;

    public static void initDataResponseVariables() {
        try {
            jp = new JsonParser();
            gson = new GsonBuilder().setPrettyPrinting().create();
            addressCleanerVersion = AppMain.appVersion;
            dataVersion = AppMain.getDataVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, String name, String txt) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <" + name + ">" + Operations.escapeInvalidXml(txt) + "</" + name + ">\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"" + name + "\": \"" + txt + "\"\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, boolean result) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <result>" + (result ? 1 : 0) + "</result>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"result\": " + result + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, String name, int value) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <" + name + ">" + value + "</" + name + ">\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"" + name + "\": " + value + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataApiInfo dai) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"apiinfo\": " + dai.toJson("    ") + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, String name1, String txt1, String name2, String txt2) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <" + name1 + ">" + Operations.escapeInvalidXml(txt1) + "</" + name1 + ">\n";
            s += "  <" + name2 + ">" + Operations.escapeInvalidXml(txt2) + "</" + name2 + ">\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"" + name1 + "\": \"" + txt1 + "\",\n";
            s += "  \"" + name2 + "\": \"" + txt2 + "\"\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataPackage[] dps) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <packages>\n";
            for (int i = 0; i < dps.length; i++)
                s += "    " + Operations.escapeInvalidXml(dps[i].toXml());
            s += "  </packages>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"packages\": [\n";
            for (int i = 0; i < dps.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += "    " + dps[i].toJson();
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataIl[] dis) {
        String s = "";
        long totalCount = 0;
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <illist>\n";
            for (int i = 0; dis.length > i; i++) {
                s += "    " + Operations.escapeInvalidXml(dis[i].toXml());
                totalCount += dis[i].getCount();
            }
            s += "  </illist>\n";
            if (totalCount > 0) {
                s += "<totalcount>" + totalCount + "</totalcount>";
            }
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"illist\": [\n";

            for (int i = 0; i < dis.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += "    " + dis[i].toJson();
                totalCount += dis[i].getCount();
            } // for()
            s += "\n";
            s += "  ]";
            if (totalCount > 0) {
                s += ",\n";
                s += "  \"totalcount\": " + totalCount;
            }
            s += "\n}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataIl di) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "    " + Operations.escapeInvalidXml(di.toXml(""));
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  " + di.toJson("");
            s += "\n";
            //s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataIlce[] dis) {
        String s = "";
        long totalCount = 0;
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <ilcelist>\n";
            for (int i = 0; dis.length > i; i++) {
                s += "    " + Operations.escapeInvalidXml(dis[i].toXml());
                totalCount += dis[i].getCount();
            }
            s += "  </ilcelist>\n";
            if (totalCount > 0) {
                s += "<totalcount>" + totalCount + "</totalcount>";
            }
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"ilcelist\": [\n";
            for (int i = 0; i < dis.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += "    " + dis[i].toJson();
                totalCount += dis[i].getCount();
            } // for()
            s += "\n";
            s += "  ]";
            if (totalCount > 0) {
                s += ",\n";
                s += "  \"totalcount\": " + totalCount;
            }
            s += "\n}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataIlce di) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "    " + Operations.escapeInvalidXml(di.toXml(""));
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  " + di.toJson("");
            s += "\n";
            //s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataMahalle[] dms) {
        StringBuilder s = new StringBuilder();
        long totalCount = 0;
        if (typ.equals("XML")) {
            s.append(XML_HEADER_STRING + "\n");
            s.append("<response>\n");
            s.append("  <transactionid>" + transactionId + "</transactionid>\n");
            s.append("  <status>" + STATUS_SUCCESS + "</status>\n");
            s.append("  <errno></errno>\n");
            s.append("  <errdesc></errdesc>\n");
            s.append("  <mahallelist>\n");
            for (int i = 0; dms.length > i; i++) {
              s.append("    " + Operations.escapeInvalidXml(dms[i].toXml()));
                totalCount += dms[i].getCount();
            }
            s.append("  </mahallelist>\n");
            if (totalCount > 0) {
                s.append("<totalcount>" + totalCount + "</totalcount>");
            }
            s.append("</response>\n");
        } else {
            s.append("{\n");
            s.append("  \"transactionid\": \"" + transactionId + "\",\n");
            s.append("  \"status\": " + STATUS_SUCCESS + ",\n");
            s.append("  \"mahallelist\": [\n");
            for (int i = 0; i < dms.length; i++) {
                if (i > 0)
                  s.append( ",\n");
                s.append("    " + dms[i].toJson());
                totalCount += dms[i].getCount();
            } // for()
            s.append("\n");
            s.append("  ]");
            if (totalCount > 0) {
                s.append(",\n");
                s.append("  \"totalcount\": " + totalCount);
            }
           s.append("\n}\n");

            try {
                je = jp.parse(s.toString());
                s.setLength(0);
                s.append(gson.toJson(je));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0){
                StringBuilder tempS = new StringBuilder();
                tempS.append(callback + "(" + s.toString() + ");");
                s.setLength(0);
                s.append(tempS.toString());
                tempS=null;
            }
        }
        if(  s.length() == 0 )s.append("");
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s.toString());

        out.println(s.toString());
        s = null;
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataMahalle dm) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "    " + Operations.escapeInvalidXml(dm.toXml(""));
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  " + dm.toJson("");
            s += "\n";
            //s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataYol[] dys) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <yollist>\n";
            for (int i = 0; i < dys.length; i++)
                s += "    " + Operations.escapeInvalidXml(dys[i].toXml());
            s += "  </yollist>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"yollist\": [\n";
            for (int i = 0; i < dys.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dys[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataYol dy) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dy.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"yol\": \n";
            s += dy.toJson("    ");
            s += "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataKapi dk) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dk.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"kapi\": \n";
            s += dk.toJson("    ");
            s += "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------
    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataKapi[] dks) {
        StringBuilder s = new StringBuilder();
        if (typ.equals("XML")) {
            s.append( XML_HEADER_STRING + "\n");
            s.append("<response>\n");
            s.append("  <transactionid>" + transactionId + "</transactionid>\n");
            s.append("  <status>" + STATUS_SUCCESS + "</status>\n");
            s.append("  <errno></errno>\n");
            s.append("  <errdesc></errdesc>\n");
            s.append("  <kapilist>\n");
            for (int i = 0; i < dks.length; i++)
               s.append("    " + Operations.escapeInvalidXml(dks[i].toXml()));
            s.append(" </kapilist>\n");
            s.append("</response>\n");
        } else {
            s.append( "{\n");
            s.append("  \"transactionid\": \"" + transactionId + "\",\n");
            s.append( "  \"status\": " + STATUS_SUCCESS + ",\n");
            s.append( "  \"kapilist\": [\n");
            for (int i = 0; i < dks.length; i++) {
                if (i > 0)
                   s.append(",\n");
                s.append(dks[i].toJson("    "));
            } // for()
            s.append( "\n");
            s.append( "  ]\n");
            s.append( "}\n");

            try {
                je = jp.parse(s.toString());
                s.setLength(0);
                s.append(gson.toJson(je));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0){
                StringBuilder tempS = new StringBuilder();
                tempS.append(callback + "(" + s.toString() + ");");
                s.setLength(0);
                s.append(tempS.toString());
                tempS = null;
            }
        }
        if(  s.length() == 0 )s.append("");
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s.toString());
        out.println(s.toString());
        s = null;
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataUserRegion[] durs) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <regionlist>\n";
            for (int i = 0; i < durs.length; i++)
                s += "    " + Operations.escapeInvalidXml(durs[i].toXml());
            s += "  </regionlist>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"regionlist\": [\n";
            for (int i = 0; i < durs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += "    " + durs[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, String[] dcs, String names, String name) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <" + names + ">\n";
            for (int i = 0; i < dcs.length; i++)
                s += "    <" + name + ">" + Operations.escapeInvalidXml(dcs[i]) + "</" + name + ">\n";
            s += "  </" + names + ">\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"" + names + "\": [\n";
            for (int i = 0; i < dcs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += "    \"" + dcs[i] + "\"";
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataCategory[] dbs) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <brands>\n";
            for (int i = 0; i < dbs.length; i++)
                s += "    <brand>" + Operations.escapeInvalidXml(dbs[i].toXml()) + "</brand>\n";
            s += "  </brands>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"brands\": [\n";
            for (int i = 0; i < dbs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dbs[i].toJson();
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()
    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataPoi[] dps) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <poilist>\n";
            for (int i = 0; i < dps.length; i++)
                s += Operations.escapeInvalidXml(dps[i].toXml());
            s += "  </poilist>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"poilist\": [\n";
            for (int i = 0; i < dps.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dps[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()
    //-----------------------------------------------------------------------------
    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataPoi[] dps, int brandGroup) {
        StringBuffer s = new StringBuffer();
        if (typ.equals("XML")) {
            s.append(XML_HEADER_STRING + "\n");
            s.append("<response>\n");
            s.append("  <transactionid>" + transactionId + "</transactionid>\n");
            s.append("  <status>" + STATUS_SUCCESS + "</status>\n");
            s.append("  <errno></errno>\n");
            s.append("  <errdesc></errdesc>\n");
            s.append("  <poilist>\n");
            for (int i = 0; i < dps.length; i++){
                s.append(Operations.escapeInvalidXml(dps[i].toXml(brandGroup)));
            }
                
            s.append("  </poilist>\n");
            s.append("</response>\n");
        } else {
            s.append("{\n");
            s.append("  \"transactionid\": \"" + transactionId + "\",\n");
            s.append("  \"status\": " + STATUS_SUCCESS + ",\n");
            s.append("  \"poilist\": [\n");
            for (int i = 0; i < dps.length; i++) {
                if (i > 0)
                   s.append(",\n");
                s.append(dps[i].toJson("    ", brandGroup));
            } // for()
            s.append("\n");
            s.append("  ]\n");
            s.append("}\n");

            try {
                je = jp.parse(s.toString());
                s.setLength(0);
                s.append(gson.toJson(je));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0){
                StringBuffer temp = new StringBuffer();
                temp.append(callback + "(" + s.toString() + ");");
                s.setLength(0);
                s.append(temp.toString());
                temp = null;
            }
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s.toString());
        out.println(s);
        return;
    } // sendSuccessResponse()
    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataPoi dp) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dp.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"poi\": \n";
            s += dp.toJson("    ");
            s += "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, double[] coors) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            if (coors.length <= 3) {
                s += "  <latitude>" + Utils.makeCoorFormat(coors[1]) + "</latitude>\n";
                s += "  <longitude>" + Utils.makeCoorFormat(coors[0]) + "</longitude>\n";
                if (coors.length >= 3) {
                    s += "  <elevation>" + coors[2] + "</elevation>\n";
                }
            } else {
                s += "  <coors>\n";
                for (int i = 0; i < coors.length / 2; i++) {
                    s += "    <latitude>" + Utils.makeCoorFormat(coors[2 * i + 1]) + "</latitude>\n";
                    s += "    <longitude>" + Utils.makeCoorFormat(coors[2 * i]) + "</longitude>\n";
                } // for()
                s += "  </coors>\n";
            }
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            if (coors.length <= 3) {
                s += "  \"latitude\": " + Utils.makeCoorFormat(coors[1]) + ",\n";
                if (coors.length < 3) {
                    s += "  \"longitude\": " + Utils.makeCoorFormat(coors[0]) + "\n";
                } else {
                    s += "  \"longitude\": " + Utils.makeCoorFormat(coors[0]) + ",\n";
                    s += "  \"elevation\": " + coors[2] + "\n";
                }
            } else {
                s += "  \"coors\": [\n";
                for (int i = 0; i < coors.length / 2; i++) {
                    if (i > 0)
                        s += ",\n";
                    s += "    { \"latitude\": " + Utils.makeCoorFormat(coors[2 * i + 1]) + ", \"longitude\": " + Utils.makeCoorFormat(coors[2 * i]) + " }";
                } // for()
                s += "\n";
                s += "  ]\n";
            }
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataSuggestion[] dsgs) {
        StringBuilder s = new StringBuilder();
        if (typ.equals("XML")) {
           s.append(XML_HEADER_STRING + "\n");
           s.append("<response>\n");
           s.append("  <transactionid>" + transactionId + "</transactionid>\n");
           s.append("  <status>" + STATUS_SUCCESS + "</status>\n");
           s.append("  <errno></errno>\n");
           s.append("  <errdesc></errdesc>\n");
           s.append("  <suggestions>\n");
            for (int i = 0; i < dsgs.length; i++)
               s.append(Operations.escapeInvalidXml(dsgs[i].toXml()));
           s.append( "  </suggestions>\n");
           s.append( "</response>\n");
        } else {
           s.append( "{\n");
           s.append("  \"transactionid\": \"" + transactionId + "\",\n");
           s.append("  \"status\": " + STATUS_SUCCESS + ",\n");
           s.append("  \"suggestions\": [\n");
            for (int i = 0; i < dsgs.length; i++) {
                if (i > 0)
                   s.append(",\n");
               s.append( dsgs[i].toJson("    "));
            } // for()
           s.append("\n");
            s.append("  ]\n");
            s.append("}\n");
          

            try {
                je = jp.parse(s.toString());
                s.setLength(0);
                s.append(gson.toJson(je));
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            if (typ.equals("JS")) {
                StringBuilder tempS = new StringBuilder();
                tempS.append("resultData = " + s.toString().replaceAll("\n", "") + ";");
                s.setLength(0);
                s.append(tempS.toString());
                tempS = null;
            }
           
            if (callback != null && callback.length() > 0){
                StringBuilder tempS = new StringBuilder();
                tempS.append(callback + "(" + s.toString() + ");");
                s.setLength(0);
                s.append(tempS.toString());
                tempS = null;
            }
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s.toString());
        out.println(s.toString());
        s = null;
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataPoint[] dps) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <pointlist>\n";
            for (int i = 0; i < dps.length; i++)
                s += Operations.escapeInvalidXml(dps[i].toXml());
            s += "  </pointlist>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"pointlist\": [\n";
            for (int i = 0; i < dps.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dps[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataDistance[] dms) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <distances>\n";
            for (int i = 0; i < dms.length; i++)
                s += Operations.escapeInvalidXml(dms[i].toXml());
            s += "  </distances>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"distances\": [\n";
            for (int i = 0; i < dms.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dms[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, long id) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <id>" + id + "</id>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"id\": " + id + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, double distance) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <distance>" + distance + "</distance>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"distance\": " + distance + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";

        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataMap dm) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <mapdata>\n";
            s += Operations.escapeInvalidXml(dm.toXml());
            s += "  </mapdata>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"mapdata\": " + dm.toJson("    ") + "\n";
            s += "}\n";

            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataUserPoint[] dups) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <userpoints>\n";
            for (int i = 0; i < dups.length; i++)
                s += Operations.escapeInvalidXml(dups[i].toXml());
            s += "  </userpoints>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"userpoints\": [\n";
            for (int i = 0; i < dups.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dups[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataUserLine[] dull) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <userlines>\n";
            for (int i = 0; i < dull.length; i++)
                s += Operations.escapeInvalidXml(dull[i].toXml());
            s += "  </userlines>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"userlines\": [\n";
            for (int i = 0; i < dull.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dull[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataList[] dints) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < dints.length; i++)
                s += Operations.escapeInvalidXml(dints[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < dints.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dints[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";

        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataGeocode dg) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <adrcleanver>" + addressCleanerVersion + "</adrcleanver>\n";
            s += "  <dataversion>" + dataVersion + "</dataversion>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <geocode>\n";
            s += Operations.escapeInvalidXml(dg.toXml());
            s += "  </geocode>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"adrcleanver\": \"" + addressCleanerVersion + "\",\n";
            s += "  \"dataversion\": \"" + dataVersion + "\",\n";
            s += "  \"geocode\": " + dg.toJson() + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------
    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataBagimsizBirim[] dks) {
        StringBuilder s = new StringBuilder();
        if (typ.equals("XML")) {
            s.append( XML_HEADER_STRING + "\n");
            s.append("<response>\n");
            s.append("  <transactionid>" + transactionId + "</transactionid>\n");
            s.append("  <status>" + STATUS_SUCCESS + "</status>\n");
            s.append("  <errno></errno>\n");
            s.append("  <errdesc></errdesc>\n");
            s.append("  <ickapilist>\n");
            for (int i = 0; i < dks.length; i++)
               s.append("    " + Operations.escapeInvalidXml(dks[i].toXml()));
            s.append(" </ickapilist>\n");
            s.append("</response>\n");
        } else {
            s.append( "{\n");
            s.append("  \"transactionid\": \"" + transactionId + "\",\n");
            s.append( "  \"status\": " + STATUS_SUCCESS + ",\n");
            s.append( "  \"ickapilist\": [\n");
            for (int i = 0; i < dks.length; i++) {
                if (i > 0)
                   s.append(",\n");
                s.append(dks[i].toJson("    "));
            } // for()
            s.append( "\n");
            s.append( "  ]\n");
            s.append( "}\n");

            try {
                je = jp.parse(s.toString());
                s.setLength(0);
                s.append(gson.toJson(je));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0){
                StringBuilder tempS = new StringBuilder();
                tempS.append(callback + "(" + s.toString() + ");");
                s.setLength(0);
                s.append(tempS.toString());
                tempS = null;
            }
        }
        if(  s.length() == 0 )s.append("");
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s.toString());
        out.println(s.toString());
        s = null;
        return;
    } 
    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataAdresCadde dg) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <adrcleanver>" + addressCleanerVersion + "</adrcleanver>\n";
            s += "  <dataversion>" + dataVersion + "</dataversion>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <geocode>\n";
            s += Operations.escapeInvalidXml(dg.toXml());
            s += "  </geocode>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"adrcleanver\": \"" + addressCleanerVersion + "\",\n";
            s += "  \"dataversion\": \"" + dataVersion + "\",\n";
            s += "  \"geocode\": " + dg.toJson() + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    }
    
    //-----------------------------------------------------------------------------
    
    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataAdresMahalle dg) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <adrcleanver>" + addressCleanerVersion + "</adrcleanver>\n";
            s += "  <dataversion>" + dataVersion + "</dataversion>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <geocode>\n";
            s += Operations.escapeInvalidXml(dg.toXml());
            s += "  </geocode>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"adrcleanver\": \"" + addressCleanerVersion + "\",\n";
            s += "  \"dataversion\": \"" + dataVersion + "\",\n";
            s += "  \"geocode\": " + dg.toJson() + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()
    
    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataGlobalGeocode dg) {
        StringBuilder s = new StringBuilder();
        if (typ.equals("XML")) {
             s.append(XML_HEADER_STRING + "\n");
             s.append("<response>\n");
             s.append("  <transactionid>" + transactionId + "</transactionid>\n");
             s.append("  <status>" + STATUS_SUCCESS + "</status>\n");
             s.append("  <errno></errno>\n");
             s.append("  <errdesc></errdesc>\n");
             s.append("  <geocode>\n");
             s.append(Operations.escapeInvalidXml(dg.toXml()));
             s.append("  </geocode>\n");
             s.append("</response>\n");
        } else {
            s.append("{\n");
            s.append("  \"transactionid\": \"" + transactionId + "\",\n");
            s.append("  \"status\": " + STATUS_SUCCESS + ",\n");
            s.append("  \"geocode\": " + dg.toJson() + "\n");
            s.append("}\n");

            try {
                je = jp.parse(s.toString());
                s.setLength(0);
                s.append(gson.toJson(je));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0){
                StringBuilder temp = new StringBuilder();
                temp.append(callback + "(" + s.toString() + ");");
                s.setLength(0);
                s.append(temp.toString());
                temp = null;
            }
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s.toString());
        out.println(s);
        return;
    } 
    
    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataReverseGlobalGeocode dg) {
        StringBuilder s = new StringBuilder();
        if (typ.equals("XML")) {
             s.append(XML_HEADER_STRING + "\n");
             s.append("<response>\n");
             s.append("  <transactionid>" + transactionId + "</transactionid>\n");
             s.append("  <status>" + STATUS_SUCCESS + "</status>\n");
             s.append("  <errno></errno>\n");
             s.append("  <errdesc></errdesc>\n");
             s.append("  <geocode>\n");
             s.append(Operations.escapeInvalidXml(dg.toXml()));
             s.append("  </geocode>\n");
             s.append("</response>\n");
        } else {
            s.append("{\n");
            s.append("  \"transactionid\": \"" + transactionId + "\",\n");
            s.append("  \"status\": " + STATUS_SUCCESS + ",\n");
            s.append("  \"geocode\": " + dg.toJson() + "\n");
            s.append("}\n");

            try {
                je = jp.parse(s.toString());
                s.setLength(0);
                s.append(gson.toJson(je));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0){
                StringBuilder temp = new StringBuilder();
                temp.append(callback + "(" + s.toString() + ");");
                s.setLength(0);
                s.append(temp.toString());
                temp = null;
            }
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s.toString());
        out.println(s);
        return;
    } // sendSuccessResponse()
//--------------------------------------------------------------------------------
    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataRota dr, DataIl[] ilList) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dr.toXml());
            s += "  <ilList>\n";
            for (int i = 0; i < ilList.length; i++){
                s += "<il>\n";
                s += "<id>" + ilList[i].getId()+ "</id><name>" + Utils.convStr2Xml(ilList[i].getName()) + "</name>";
                s += "</il>\n";
            }
            s += "  </ilList>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"rota\": " + dr.toJson() + ",\n";
            s += "  \"ilList\": [\n";
            for (int i = 0; i < ilList.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += "{\"id\": " + ilList[i].getId();
                s += ", \"name\": \"" + Utils.convStr2Json(ilList[i].getName()) + "\"}";        
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return; 
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataRota dr,boolean isHere) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            if(!isHere){
                s += Operations.escapeInvalidXml(dr.toXml());
            }else{
                s += Operations.escapeInvalidXml(dr.toXmlHere());

            }
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            if(!isHere){
                s += "  \"rota\": " + dr.toJson() + "\n";                
            }else{
                s += "  \"rota\": " + dr.toJsonHere() + "\n";                
            }
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()


    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataGlobalRoute dr, boolean isHere) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            if(isHere){
                s += Operations.escapeInvalidXml(dr.toXmlHere());
            }
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            if(isHere){
                s += "  \"route\": " + dr.toJsonHere() + "\n";                
            }
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, CampCategory[] ccs) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < ccs.length; i++)
                s += Operations.escapeInvalidXml(ccs[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < ccs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += ccs[i].toJson();
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, CampCampaign[] ccs) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < ccs.length; i++)
                s += Operations.escapeInvalidXml(ccs[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < ccs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += ccs[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DealCategory[] dcs) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < dcs.length; i++)
                s += Operations.escapeInvalidXml(dcs[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < dcs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dcs[i].toJson();
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DealDeal[] dds) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < dds.length; i++)
                s += Operations.escapeInvalidXml(dds[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < dds.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dds[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataTrafficEvent[] dtes) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < dtes.length; i++)
                s += Operations.escapeInvalidXml(dtes[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < dtes.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dtes[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataTrafficInfo dti) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dti.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"info\": " + dti.toJson("    ");
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataTmcFlow[] dtfs) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <infos>\n";
            for (int i = 0; i < dtfs.length; i++)
                s += Operations.escapeInvalidXml(dtfs[i].toXml());
            s += "  </infos>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"infos\": [\n";
            for (int i = 0; i < dtfs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dtfs[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataTrafficRoute[] dtrs) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < dtrs.length; i++)
                s += Operations.escapeInvalidXml(dtrs[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < dtrs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dtrs[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataDemographic dd) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dd.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"demographic\": " + dd.toJson("    ");
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, SkorResultInfo res) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + res.getResultCode() + "</status>\n";
            s += "  <errdesc>" + InfoRiskSkor.getResponseDesc(res.getResultCode()) + "</errdesc>\n";
            s += "  <riskskor>" + res.getSkorValue() + "</riskskor>\n";
            for (int ii = 1; ii <= res.getCogParamLength(); ii++) {
                s += "  <cografi" + ii + ">" + res.getSkorCog(ii) + "</cografi" + ii + ">\n";
            }
            for (int ii = 1; ii <= res.getBeyanParamLength(); ii++) {
                s += "  <beyan" + ii + ">" + res.getSkorBeyan(ii) + "</beyan" + ii + ">\n";
            }
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + res.getResultCode() + ",\n";
            s += "  \"errdesc\": \"" + InfoRiskSkor.getResponseDesc(res.getResultCode()) + "\",\n";
            s += "  \"riskskor\": " + res.getSkorValue() + ",\n";
            for (int ii = 1; ii <= res.getCogParamLength(); ii++) {
                if (ii == res.getCogParamLength() && res.getBeyanParamLength() <= 0)
                    s += "  \"cografi" + ii + "\": " + res.getSkorCog(ii) + "\n";
                else
                    s += "  \"cografi" + ii + "\": " + res.getSkorCog(ii) + ",\n";
            }
            for (int ii = 1; ii <= res.getBeyanParamLength(); ii++) {
                if (ii == res.getBeyanParamLength())
                    s += "  \"beyan" + ii + "\": " + res.getSkorBeyan(ii) + "\n";
                else
                    s += "  \"beyan" + ii + "\": " + res.getSkorBeyan(ii) + ",\n";
            }

            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataSocialEvent[] dses) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < dses.length; i++)
                s += Operations.escapeInvalidXml(dses[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < dses.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dses[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataTmcHat dth) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dth.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"tmchat\": " + dth.toJson("    ") + "\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataTmcHat[] dths) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <items>\n";
            for (int i = 0; i < dths.length; i++)
                s += Operations.escapeInvalidXml(dths[i].toXml());
            s += "  </items>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"items\": [\n";
            for (int i = 0; i < dths.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += dths[i].toJson("    ");
            } // for()
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";

        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataOptResult[] dors) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            if (dors.length == 1 && (dors[0].getMobile() == 0 && dors[0].getPointId() == 0)) {
                s += "  <status>" + STATUS_WAIT + "</status>\n";
                s += "  <errno></errno>\n";
                s += "  <errdesc></errdesc>\n";
            } else {
                s += "  <status>" + STATUS_SUCCESS + "</status>\n";
                s += "  <errno></errno>\n";
                s += "  <errdesc></errdesc>\n";
                s += "  <items>\n";
                for (int i = 0; i < dors.length; i++)
                    s += Operations.escapeInvalidXml(dors[i].toXml());
                s += "  </items>\n";
            }
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            if (dors.length == 1 && (dors[0].getMobile() == 0 && dors[0].getPointId() == 0)) {
                s += "  \"status\": " + STATUS_WAIT + "\n";
            } else {
                s += "  \"status\": " + STATUS_SUCCESS + ",\n";
                s += "  \"items\": [\n";
                for (int i = 0; i < dors.length; i++) {
                    if (i > 0)
                        s += ",\n";
                    s += dors[i].toJson("    ");
                } // for()
                s += "\n";
                s += "  ]\n";
            }
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataSpatialAnalysis dsa) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <analysis>\n";
            if (dsa.pois != null) {
                s += "  <poilist>\n";
                for (int i = 0; i < dsa.pois.length; i++)
                    s += Operations.escapeInvalidXml(dsa.pois[i].toXml());
                s += "  </poilist>\n";
            }
            if (dsa.userPoints != null) {
                s += "  <userpointlist>\n";
                for (int i = 0; i < dsa.userPoints.length; i++)
                    s += dsa.userPoints[i].toXml();
                s += "  </userpointlist>\n";
            }
            s += "  </analysis>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"analysis\": {\n";
            if (dsa.pois != null) {
                s += "    \"poilist\": [\n";
                for (int i = 0; i < dsa.pois.length; i++) {
                    if (i > 0)
                        s += ",\n";
                    s += dsa.pois[i].toJson("      ");
                } // for()
                s += "\n";
                s += "  ]\n";
            }
            if (dsa.userPoints != null) {
                s += "    \"userpointlist\": [\n";
                for (int i = 0; i < dsa.userPoints.length; i++) {
                    if (i > 0)
                        s += ",\n";
                    s += dsa.userPoints[i].toJson("      ");
                } // for()
                s += "\n";
                s += "  ]\n";
            }
            s += "  }\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendErrorResponse(PrintWriter out, String typ, String transactionId, String callback, String errNo, String errDesc) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_FAIL + "</status>\n";
            s += "  <errno>" + errNo + "</errno>\n";
            s += "  <errdesc>" + errDesc + "</errdesc>\n";
            s += "</response>\n";
        } else if (typ.equals("JS")) {
            s += "alert(\"Error: " + errNo + " - " + errDesc + "\");";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_FAIL + ",\n";
            s += "  \"errno\": " + errNo + ",\n";
            s += "  \"errdesc\": \"" + errDesc + "\"\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, errNo, errDesc, s);
        out.println(s);
        return;
    } // sendErrorResponse()

    //-----------------------------------------------------------------------------

    public static void sendErrorResponse(PrintWriter out, String typ, String transactionId, String callback) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_FAIL + "</status>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_FAIL + "\n";
            s += "}\n";
            if (typ.equals("JS")) {
                s = "resultData = " + s.replaceAll("\n", "") + ";";
            } else if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "", s);
        out.println(s);
        return;
    } // sendErrorResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataWeatherReport dwr) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dwr.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"weatherreport\": " + dwr.toJson("    ");
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataClusterPoint dcp) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <clusters>\n";
            s += Operations.escapeInvalidXml(dcp.toXml());
            s += "  </clusters>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"clusters\": [\n";
            s += dcp.toJson("    ");
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------


    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataPhonebook dpbl[]) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <results>\n";
            for (int i = 0; dpbl.length > i; i++) {
                s += Operations.escapeInvalidXml(dpbl[i].toXml());
            }
            s += "  </results>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"results\": [\n";
            for (int i = 0; dpbl.length > i; i++) {
                if (i > 0)
                    s += ",\n";
                s += dpbl[i].toJson("    ");
            }
            s += "\n  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, String string, DataPoint dp) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dp.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"nearestpointonyol\": " + dp.toJson("    ");
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    }

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataKeyStatus dks, DataPackage[] dps) {

        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dks.toXml());
            s += "  <packages>\n";
            if (dps != null) {
                for (int i = 0; i < dps.length; i++)
                    s += "    " + dps[i].toXml();
            }
            s += "  </packages>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "    \"transactionid\": \"" + transactionId + "\",\n";
            s += "    \"status\": " + STATUS_SUCCESS + ",\n";
            s += "    " + dks.toJson("  ");
            s += "    \"packages\": [\n";
            if (dps != null) {
                for (int i = 0; i < dps.length; i++) {
                    if (i > 0)
                        s += ",\n";
                    s += "    " + dps[i].toJson();
                } // for()
            }
            s += "\n";
            s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    }

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataMaxYolSpeed dmys) {

        String s = "";

        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dmys.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "    \"transactionid\": \"" + transactionId + "\",\n";
            s += "    \"status\": " + STATUS_SUCCESS + ",\n";
            s += "    " + dmys.toJson("  ");
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    }

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataUavtAddress dua) {

        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(dua.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"nearestpointonyol\": " + dua.toJson("    ");
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    }

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataEarthQuake deq) {

        String s = "";

        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += Operations.escapeInvalidXml(deq.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "    \"transactionid\": \"" + transactionId + "\",\n";
            s += "    \"status\": " + STATUS_SUCCESS + ",\n";
            s += "    " + deq.toJson("  ");
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    }

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataIndoorVenue[] divs) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <venuelist>\n";
            for (int i = 0; divs.length > i; i++) {
                s += "    " + Operations.escapeInvalidXml(divs[i].toXml());
            }
            s += "  </venuelist>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"venuelist\": [\n";
            for (int i = 0; i < divs.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += "    " + divs[i].toJson();
            } // for()
            s += "\n";
            s += "]";
            s += "}";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);

        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataIndoorPoi[] dips) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "  <indoorpoilist>\n";
            for (int i = 0; dips.length > i; i++) {
                s += "    " + Operations.escapeInvalidXml(dips[i].toXml());
            }
            s += "  </indoorpoilist>\n";
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"indoorpoilist\": [\n";
            for (int i = 0; i < dips.length; i++) {
                if (i > 0)
                    s += ",\n";
                s += "    " + dips[i].toJson();
            } // for()
            s += "\n";
            s += "]";
            s += "}";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);

        out.println(s);
        return;
    } // sendSuccessResponse()

    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, What3Words w3w) {
        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += "    " + Operations.escapeInvalidXml(w3w.toXml());
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  " + w3w.toJson();
            s += "\n";
            //s += "  ]\n";
            s += "}\n";

            try {
                je = jp.parse(s);
                s = gson.toJson(je);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    } // sendSuccessResponse()
    //-----------------------------------------------------------------------------

    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, DataBBolumFeedback dbf, int responseType) {

        String s = "";
        if (typ.equals("XML")) {
            s += XML_HEADER_STRING + "\n";
            s += "<response>\n";
            s += "  <transactionid>" + transactionId + "</transactionid>\n";
            s += "  <status>" + STATUS_SUCCESS + "</status>\n";
            s += "  <errno></errno>\n";
            s += "  <errdesc></errdesc>\n";
            s += dbf.toXml(responseType);
            s += "</response>\n";
        } else {
            s += "{\n";
            s += "  \"transactionid\": \"" + transactionId + "\",\n";
            s += "  \"status\": " + STATUS_SUCCESS + ",\n";
            s += "  \"feedback\": " + dbf.toJson(responseType);
            s += "}\n";
            
            if (callback != null && callback.length() > 0)
                s = callback + "(" + s + ");";
            
        }
    
        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s);
        out.println(s);
        return;
    }
    public static void sendSuccessResponse(PrintWriter out, String typ, String transactionId, String callback, List<DataBBolumFeedback> dbf) {
        StringBuffer s = new StringBuffer();
        if (typ.equals("XML")) {
            s.append(XML_HEADER_STRING + "\n");
            s.append("<response>\n");
            s.append("<transactionid>" + transactionId + "</transactionid>\n");
            s.append("<status>" + STATUS_SUCCESS + "</status>\n");
            s.append("<errno></errno>\n");
            s.append("<errdesc></errdesc>\n");
            s.append("<feedbacklist>");
            for (int i = 0; i < dbf.size(); i++) {
               s.append(Operations.escapeInvalidXml(dbf.get(i).toXml()));
            } // for()
            s.append("</feedbacklist>\n");
            s.append("</response>\n");
        } else {
            s.append("{\n");
            s.append("\"transactionid\": \"" + transactionId + "\",\n");
            s.append("\"status\": " + STATUS_SUCCESS + ",\n");
          
            s.append("\"feedbacklist\": [\n");

            for (int i = 0; i < dbf.size(); i++) {
                if (i > 0)
                   s.append(",\n");
                s.append(dbf.get(i).toJson());
            } // for()
            s.append("\n");
            s.append("  ]\n");
            s.append("}\n");
        
            try {
                je = jp.parse(s.toString());
                s.setLength(0);
                s.append(gson.toJson(je));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null && callback.length() > 0){
                StringBuffer tmp = new StringBuffer();
                tmp.append(callback + "(" + s.toString() + ");");
                s.setLength(0);
                s.append(tmp.toString());
                tmp = null;
            }
        }

        Utils.updateLbsServiceRequest(transactionId, "0", "Successfull", s.toString());
        out.println(s);
        return;
    }

}
