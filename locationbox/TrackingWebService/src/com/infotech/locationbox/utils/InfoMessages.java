package com.infotech.locationbox.utils;


import java.io.PrintWriter;

public enum InfoMessages {
    KEY,
    MOBILE_ID,
    TIME_STAMP;


    public static void getMessageInfo(InfoMessages enumVar, String message, PrintWriter out, long rowNo) {
        String res = null;
        switch (enumVar) {
        case KEY:
            res = "{ \"status\":\" Invalid KEY\", \"message\": \"" + message + "\" }";
            break;
        case MOBILE_ID:
            res = "{ \"status\":\" Invalid MOBILE_ID\", \"message\": \"" + message + "\" }";
            break;
        case TIME_STAMP:
            res = "{ \"status\":\" Invalid TIME_STAMP\", \"message\": \"" + message + "\" }";
            break;
        }
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    }

    public static void sendMessage(PrintWriter out, long rowNo, int status, String message) {
        String res = "{ \"status\":" + status + ", \"message\": \"" + message + "\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    }
}
