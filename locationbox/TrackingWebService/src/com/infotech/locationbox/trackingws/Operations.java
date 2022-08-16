package com.infotech.locationbox.trackingws;

import com.infotech.locationbox.utils.InfoMessages;

import com.infotech.locationbox.utils.Utils;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

public class Operations {

    public Operations() {
    }

    //-----------------------------------------------------------------------------

    public static void doRegister(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (method.equals("POST")) {
            String username = params.getString("username");
            String password = params.getString("password");
            String key = params.getString("key");
            dr = Utils.getUserDetails(username, password, key);
            if (dr == null)
                /* @SAL
                 * sendUserFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            else {
                String res = "{ \"status\": 0, \"message\": \"Success\", \"token\": \"" + dr.getToken() + "\"}";
                out.println(res);
                Utils.logWebServiceResponse(rowNo, "0", "Success", res);
            }
            return;
        }

        sendMethodFailure(out, rowNo, "POST");
        return;
    } // doRegister()

    //-----------------------------------------------------------------------------

    public static void doMobile(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        if (method.equals("GET")) {
            long mobileId = getItemId(items, 0);
            if (mobileId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataMobile dm = DataMobile.getInstance(dr, mobileId);
            if (dm == null)
                /*@sal
                 *
                 *  sendNotFound(out, rowNo);*/
                InfoMessages.sendMessage(out, rowNo, -2, "Not Found");

            else
                sendItemData(out, rowNo, dm);
            return;
        }

        if (method.equals("POST")) {
            DataMobile dm = DataMobile.getInstance(params);
            if (dm == null) {
                /*@SAL
                 * sendJsonObjectFailure(out, rowNo);
                */
                InfoMessages.sendMessage(out, rowNo, -6, "Illegal JSON Object");
                return;
            }

            long mobileId = dm.insert(dr);
            if (mobileId > 0)
                sendSuccess(out, rowNo, mobileId);
            else
                sendFailure(out, rowNo, mobileId);
            return;
        }

        if (method.equals("PUT")) {
            DataMobile dm = DataMobile.getInstance(params);
            if (dm == null) {

                /*@SAL
                 * sendJsonObjectFailure(out, rowNo);
                */
                InfoMessages.sendMessage(out, rowNo, -6, "Illegal JSON Object");
                return;
            }

            boolean ok = dm.update(dr);
            if (ok)
                //@SAL
                InfoMessages.sendMessage(out, rowNo, 0, "Success");
            //  sendSuccess(out, rowNo);
            else
                //@SAL
                InfoMessages.sendMessage(out, rowNo, -1, "Failure");
            // sendFailure(out, rowNo);
            return;
        }

        if (method.equals("DELETE")) {
            long thingId = getItemId(items, 0);
            if (thingId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataMobile dm = new DataMobile(thingId);
            int res = dm.delete(dr);
            if (res == 0)
                //@SAL
                InfoMessages.sendMessage(out, rowNo, 0, "Success");
            //  sendSuccess(out, rowNo);
            else
                //@SAL
                InfoMessages.sendMessage(out, rowNo, -1, "Failure");
            // sendFailure(out, rowNo);

            return;
        }
        /*@SAL
         * sendMethodFailure(out,rowno);
         */
        InfoMessages.sendMessage(out, rowNo, -7, "Illegal Method other than (GET,POST,PUT,DELETE) !");

        return;
    } // doMobile()

    //-----------------------------------------------------------------------------

    public static void doMobiles(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        DataMobile[] mobiles = DataMobile.getMobiles(dr);
        if (mobiles == null) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendItemData(out, rowNo, mobiles);
        return;
    } // doMobiles()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doSchedule(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        if (method.equals("GET")) {
            long scheduleId = getItemId(items, 0);
            if (scheduleId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataSchedule schedule = DataSchedule.getInstance(dr, scheduleId);
            if (schedule == null)
                /*@sal
                 *
                 *  sendNotFound(out, rowNo);*/
                InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            else
                sendItemData(out, rowNo, schedule);
            return;
        }

        if (method.equals("POST")) {
            DataSchedule schedule = DataSchedule.getInstance(params);
            if (schedule == null) {

                /*@SAL
                 * sendJsonObjectFailure(out, rowNo);
                */
                InfoMessages.sendMessage(out, rowNo, -6, "Illegal JSON Object");
                return;
            }

            long scheduleId = schedule.insert(dr);
            if (scheduleId > 0)
                sendSuccess(out, rowNo, scheduleId);
            else
                //@SAL
                InfoMessages.sendMessage(out, rowNo, -1, "Failure");
            // sendFailure(out, rowNo);

            return;
        }

        if (method.equals("PUT")) {
            DataSchedule schedule = DataSchedule.getInstance(params);
            if (schedule == null) {

                /*@SAL
                 * sendJsonObjectFailure(out, rowNo);
                */
                InfoMessages.sendMessage(out, rowNo, -6, "Illegal JSON Object");
                return;
            }

            boolean ok = schedule.update(dr);
            if (ok)
                //@SAL
                InfoMessages.sendMessage(out, rowNo, 0, "Success");
            //  sendSuccess(out, rowNo);
            else
                //@SAL
                InfoMessages.sendMessage(out, rowNo, -1, "Failure");
            // sendFailure(out, rowNo);

            return;
        }

        if (method.equals("DELETE")) {
            String attr = getItemAttribute(items, 1);
            if (attr != null) {
                if (attr.equals("coors")) {
                    /* @SAL
                     * sendItemAttributeFailure(out, rowNo);
                     */
                    InfoMessages.sendMessage(out, rowNo, -3, "Item attribute is incorrect");
                    return;
                }
            }

            long scheduleId = getItemId(items, 0);
            if (scheduleId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataSchedule schedule = new DataSchedule(scheduleId);
            int res = schedule.delete(dr);
            if (res == 0)
                //@SAL
                InfoMessages.sendMessage(out, rowNo, 0, "Success");
            //  sendSuccess(out, rowNo);
            else
                //@SAL
                InfoMessages.sendMessage(out, rowNo, -1, "Failure");
            // sendFailure(out, rowNo);

            return;
        }

        /*@SAL
         * sendMethodFailure(out,rowno);
         */
        InfoMessages.sendMessage(out, rowNo, -7, "Illegal Method other than (GET,POST,PUT,DELETE) !");
        return;
    } // doSchedule()

    //-----------------------------------------------------------------------------

    public static void doSchedules(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        DataSchedule[] schedules = DataSchedule.getSchedules(dr);
        if (schedules == null) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendItemData(out, rowNo, schedules);
        return;
    } // doSchedules()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doLocation(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        int inx = 0;

        if (method.equals("GET")) {
            String attr = getItemAttribute(items, inx++);
            if (attr == null) {
                /* @SAL
                 * sendItemAttributeFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item attribute is incorrect");
                return;
            }

            if (attr.equals("last")) {
                long mobileId = getItemId(items, inx++);
                if (mobileId < 0) {
                    /*
                     * @SAL
                     *sendItemNumberFailure(out, rowNo);
                     */
                    InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                    return;
                }

                DataLocation location = DataLocation.getInstance(dr, mobileId);
                if (location == null)
                    /*@sal
                     *
                     *  sendNotFound(out, rowNo);*/
                    InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                else
                    sendItemData(out, rowNo, location);
                return;
            }

            /* @SAL
             * sendItemAttributeFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -3, "Item attribute is incorrect");
            return;
        }


        if (method.equals("POST")) {
            DataLocation dl = DataLocation.getInstance(params);

            if (dl == null) {

                /*@SAL
                 * sendJsonObjectFailure(out, rowNo);
                */
                InfoMessages.sendMessage(out, rowNo, -6, "Illegal JSON Object");
                return;
            }
            if (dl.timeStamp == null) {
                InfoMessages.getMessageInfo(InfoMessages.TIME_STAMP, "Invalid Time Stamp Query is failed", out, rowNo);
                return;
            }
            long rowno = dl.insert(dr);
            if (rowno > 0)
                sendSuccess(out, rowNo, rowno);
            else
                sendFailure(out, rowNo, rowno);
            return;
        }

        sendMethodFailure(out, rowNo, "POST");
        return;
    } // doLocation()

    //-----------------------------------------------------------------------------

    public static void doLocations(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        int inx = 0;

        DataLocation[] location = null;

        if (method.equals("GET")) {
            String attr = getItemAttribute(items, inx++);
            if (attr == null) {
                /* @SAL
                 * sendItemAttributeFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item attribute is incorrect");
                return;
            }

            if (attr.equals("last")) {
                List<Long> mobileList = new ArrayList<>();

                while (getItemId(items, inx) > 0)
                    mobileList.add(getItemId(items, inx++));

                if (mobileList.size() > 0)
                    location = DataLocation.getInstance(dr, mobileList);
                else
                    location = DataLocation.getInstance(dr);

                if (location == null)
                    /*@sal
                     *
                     *  sendNotFound(out, rowNo);*/
                    InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                else
                    sendItemData(out, rowNo, location);
                return;
            }

            /* @SAL
             * sendItemAttributeFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -3, "Item attribute is incorrect");
            return;
        }

        if (method.equals("POST")) {
            DataLocation dl = DataLocation.getInstance(params);
            if (dl == null) {

                /*@SAL
                 * sendJsonObjectFailure(out, rowNo);
                */
                InfoMessages.sendMessage(out, rowNo, -6, "Illegal JSON Object");
                return;
            }

            long rowno = dl.insert(dr);
            if (rowno > 0)
                sendSuccess(out, rowNo, rowno);
            else
                sendFailure(out, rowNo, rowno);
            return;
        }

        sendMethodFailure(out, rowNo, "POST");
        return;
    } // doLocations()

    //-----------------------------------------------------------------------------

    public static void doAlarms(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        int hoursBefore = 24;

        String attr = getItemAttribute(items, 0);
        if (attr != null && attr.equals("hours")) {
            int hb = (int) getItemId(items, 1);
            if (hb > 0)
                hoursBefore = hb;
        }

        DataAlarm[] alarms = DataAlarm.getAlarms(dr, hoursBefore);
        if (alarms == null) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendItemData(out, rowNo, alarms);
        return;
    } // doAlarms()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doLocTypes(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        DataLocType[] locTypes = DataLocType.getLocTypes(dr);
        if (locTypes == null) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendItemData(out, rowNo, locTypes);
        return;
    } // doLocTypes()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doEventCodes(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        DataEventCode[] eventCodes = DataEventCode.getEventCodes(dr);
        if (eventCodes == null) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendItemData(out, rowNo, eventCodes);
        return;
    } // doEventCodes()

    //-----------------------------------------------------------------------------

    public static void doEvents(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        long mobileId = 0;
        String eventCode = null;
        String startDate = null;
        String endDate = null;

        int inx = 0;
        for (int i = 0; i < 4; i++) {
            String attr = getItemAttribute(items, inx++);
            if (attr == null)
                break;

            if (attr.equals("mobile"))
                mobileId = getItemId(items, inx++);
            if (attr.equals("event"))
                eventCode = getItemAttribute(items, inx++);
            if (attr.equals("startdate"))
                startDate = Utils.decodeEscape(getItemAttribute(items, inx++));
            if (attr.equals("enddate"))
                endDate = Utils.decodeEscape(getItemAttribute(items, inx++));
        } // for()

        long timeDiff = 0;

        try {
            timeDiff = Utils.toTimestamp(endDate).getTime() - Utils.toTimestamp(startDate).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        Utils.showText("MOBILE: " + mobileId + ", EVENT: " + eventCode + ", START: " + startDate + ", END: " + endDate + " TIMEDIFF: " + timeDiff);

        if (timeDiff > 86400000) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        DataEvent[] events = DataEvent.getEvents(dr, mobileId, eventCode, startDate, endDate);
        if (events == null) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendItemData(out, rowNo, events);
        return;
    } // doEvents()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doRegion(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        if (method.equals("GET")) {
            long regionId = getItemId(items, 0);
            if (regionId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataRegion campus = DataRegion.getInstance(dr, regionId);
            if (campus == null) {
                /*@sal
                 *
                 *  sendNotFound(out, rowNo);*/
                InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                return;
            }

            sendItemData(out, rowNo, campus);
            return;
        }

        if (method.equals("POST")) {
            DataRegion dm = DataRegion.getInstance(params);
            if (dm == null) {

                /*@SAL
                 * sendJsonObjectFailure(out, rowNo);
                */
                InfoMessages.sendMessage(out, rowNo, -6, "Illegal JSON Object");
                return;
            }

            long regionId = dm.insert(dr);
            if (regionId > 0)
                sendSuccess(out, rowNo, regionId);
            else
                //@SAL
                InfoMessages.sendMessage(out, rowNo, -1, "Failure");
            // sendFailure(out, rowNo);

            return;
        }

        if (method.equals("DELETE")) {
            long regionId = getItemId(items, 0);
            if (regionId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataRegion rgn = new DataRegion(regionId);
            int res = rgn.delete(dr);
            if (res == 0)
                //@SAL
                InfoMessages.sendMessage(out, rowNo, 0, "Success");
            //  sendSuccess(out, rowNo);
            else
                //@SAL
                InfoMessages.sendMessage(out, rowNo, -1, "Failure");
            // sendFailure(out, rowNo);

            return;
        }

        sendMethodFailure(out, rowNo, "GET,POST,DELETE");
        return;
    } // doRegion()

    //-----------------------------------------------------------------------------

    public static void doRegions(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        DataRegion[] regions = DataRegion.getRegions(dr);
        if (regions == null) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendItemData(out, rowNo, regions);
        return;
    } // doRegions()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doCampus(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        int inx = 0;

        if (method.equals("GET")) {
            long campusId = getItemId(items, inx++);
            if (campusId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            boolean detailed = false;
            String attr = getItemAttribute(items, inx++);
            if (attr != null && attr.equals("detailed"))
                detailed = true;

            DataCampus campus = DataCampus.getInstance(dr, campusId, detailed);
            if (campus == null) {
                /*@sal
                 *
                 *  sendNotFound(out, rowNo);*/
                InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                return;
            }

            sendItemData(out, rowNo, campus);
            return;
        }

        sendMethodFailure(out, rowNo, "GET");
        return;
    } // doCampus()

    //-----------------------------------------------------------------------------

    public static void doCampuses(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        DataCampus[] campuses = DataCampus.getCampuses(dr);
        if (campuses == null) {
            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendItemData(out, rowNo, campuses);
        return;
    } // doCampuses()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doVenue(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        int inx = 0;

        if (method.equals("GET")) {
            long venueId = getItemId(items, inx++);
            if (venueId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            boolean detailed = false;
            String attr = getItemAttribute(items, inx++);
            if (attr != null && attr.equals("detailed"))
                detailed = true;

            DataVenue venue = DataVenue.getInstance(dr, venueId, detailed);
            if (venue == null) {
                /*@sal
                 *
                 *  sendNotFound(out, rowNo);*/
                InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                return;
            }

            sendItemData(out, rowNo, venue);
            return;
        }

        sendMethodFailure(out, rowNo, "GET");
        return;
    } // doVenue()

    //-----------------------------------------------------------------------------

    public static void doVenues(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        String attr = getItemAttribute(items, 0);
        if (attr != null && attr.equals("campus")) {
            long campusId = getItemId(items, 1);
            if (campusId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataVenue[] venues = DataVenue.getVenues(dr, campusId);
            if (venues == null) {
                /*@sal
                 *
                 *  sendNotFound(out, rowNo);*/
                InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                return;
            }

            sendItemData(out, rowNo, venues);
            return;
        }

        /*@sal
         *
         *  sendNotFound(out, rowNo);*/
        InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
        return;
    } // doVenues()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doFloor(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        int inx = 0;

        if (method.equals("GET")) {
            int floor = (int) getItemId(items, inx++);
            if (floor < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            String attr = getItemAttribute(items, inx++);
            if (attr != null && attr.equals("venue")) {
                long venueId = getItemId(items, inx++);
                if (venueId < 0) {
                    /*
                     * @SAL
                     *sendItemNumberFailure(out, rowNo);
                     */
                    InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                    return;
                }

                boolean detailed = false;
                attr = getItemAttribute(items, inx++);
                if (attr != null && attr.equals("detailed"))
                    detailed = true;

                DataFloor df = DataFloor.getInstance(dr, venueId, floor, detailed);
                if (df == null) {
                    /*@sal
                     *
                     *  sendNotFound(out, rowNo);*/
                    InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                    return;
                }

                sendItemData(out, rowNo, df);
                return;
            }

            /*@sal
             *
             *  sendNotFound(out, rowNo);*/
            InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            return;
        }

        sendMethodFailure(out, rowNo, "GET");
        return;
    } // doFloor()

    //-----------------------------------------------------------------------------

    public static void doFloors(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        String attr = getItemAttribute(items, 0);
        if (attr != null && attr.equals("venue")) {
            long venueId = getItemId(items, 1);
            if (venueId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataFloor[] floors = DataFloor.getFloors(dr, venueId);
            if (floors == null) {
                /*@sal
                 *
                 *  sendNotFound(out, rowNo);*/
                InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                return;
            }

            sendItemData(out, rowNo, floors);
            return;
        }

        /*@sal
         *
         *  sendNotFound(out, rowNo);*/
        InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
        return;
    } // doFloors()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    public static void doArea(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        if (method.equals("GET")) {
            long areaId = getItemId(items, 0);
            if (areaId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            DataArea area = DataArea.getInstance(dr, areaId);
            if (area == null)
                /*@sal
                 *
                 *  sendNotFound(out, rowNo);*/
                InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
            else
                sendItemData(out, rowNo, area);
            return;
        }

        sendMethodFailure(out, rowNo, "GET");
        return;
    } // doArea()

    //-----------------------------------------------------------------------------

    public static void doAreas(long rowNo, DataRegister dr, PrintWriter out, String method, JsonObject params, String[] items) {
        if (dr.username == null) {
            /* @SAL
             * sendUserFailure(out, rowNo);
             */
            InfoMessages.sendMessage(out, rowNo, -9, "User Fail");
            return;
        }

        String attr = getItemAttribute(items, 0);
        if (attr != null && attr.equals("venue")) {
            long venueId = getItemId(items, 1);
            if (venueId < 0) {
                /*
                 * @SAL
                 *sendItemNumberFailure(out, rowNo);
                 */
                InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                return;
            }

            attr = getItemAttribute(items, 2);
            if (attr != null && attr.equals("floor")) {
                int floor = (int) getItemId(items, 3);
                if (floor < 0) {
                    /*
                     * @SAL
                     *sendItemNumberFailure(out, rowNo);
                     */
                    InfoMessages.sendMessage(out, rowNo, -3, "Item number is incorrect");
                    return;
                }

                DataArea[] areas = DataArea.getAreas(dr, venueId, floor);
                if (areas == null) {
                    /*@sal
                     *
                     *  sendNotFound(out, rowNo);*/
                    InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
                    return;
                }

                sendItemData(out, rowNo, areas);
                return;
            }
        }

        /*@sal
         *
         *  sendNotFound(out, rowNo);*/
        InfoMessages.sendMessage(out, rowNo, -2, "Not Found");
        return;
    } // doAreas()

    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------
    //-----------------------------------------------------------------------------

    private static long getItemId(String[] items, int inx) {
        if (items.length <= inx)
            return -1;
        try {
            return Long.parseLong(items[inx]);
        } catch (Exception e) {
            ;
        }
        return -2;
    } // getItemId()

    //-----------------------------------------------------------------------------

    private static String getItemAttribute(String[] items, int inx) {
        if (items.length <= inx)
            return null;
        return items[inx];
    } // getItemAttribute()

    //-----------------------------------------------------------------------------

    private static int getItemIntValue(String[] items, int inx) {
        if (items.length <= inx)
            return -1;
        try {
            return Integer.parseInt(items[inx]);
        } catch (Exception e) {
            ;
        }
        return -2;
    } // getItemIntValue()

    //-----------------------------------------------------------------------------

    private static double getItemDoubleValue(String[] items, int inx) {
        if (items.length <= inx)
            return -1;
        try {
            return Double.parseDouble(items[inx]);
        } catch (Exception e) {
            ;
        }
        return -2;
    } // getItemDoubleValue()

    //-----------------------------------------------------------------------------

    /*
     *  @SAL
     * */

    /*private static void sendSuccess(PrintWriter out, long rowNo) {
        String res = "{ \"status\": 0, \"message\": \"Success\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendSuccess()
    */
    //-----------------------------------------------------------------------------

    private static void sendSuccess(PrintWriter out, long rowNo, long id) {
        String res = "{ \"status\": 0, \"message\": \"Success\", \"id\": " + id + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendSuccess()

    //-----------------------------------------------------------------------------


    /*      @SAL
        private static void sendFailure(PrintWriter out, long rowNo) {
        String res = "{ \"status\": -1, \"message\": \"Failure\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } // sendFailure()
*/
    //-----------------------------------------------------------------------------

    // sendFailure()
    private static void sendFailure(PrintWriter out, long rowNo, long result) {
        String res = "{ \"status\": " + result + ", \"message\": \"Failure with result\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } 

    //-----------------------------------------------------------------------------
    /*
 * @SAL
    private static void sendNotFound(PrintWriter out, long rowNo) {
        String res = "{ \"status\": -2, \"message\": \"Not found\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } // sendNotFound()
*/
    //-----------------------------------------------------------------------------

    /*
 * @SAL
 *
    private static void sendItemNumberFailure(PrintWriter out, long rowNo) {
        String res = "{ \"status\": -3, \"message\": \"Item number is incorrect\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } // sendItemNumberFailure()
*/
    //-----------------------------------------------------------------------------

    /*
 * @SAL
 *
    private static void sendItemAttributeFailure(PrintWriter out, long rowNo) {
        String res = "{ \"status\": -3, \"message\": \"Item attribute is incorrect\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } // sendItemAttributeFailure()
*/
    //-----------------------------------------------------------------------------

    /*@SAL
 *
 */
    /*  private static void sendJsonObjectFailure(PrintWriter out, long rowNo) {
        String res = "{ \"status\": -6, \"message\": \"Illegal Json Object !\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } // sendJsonObjectFailure()
*/
    //-----------------------------------------------------------------------------


    private static void sendMethodFailure(PrintWriter out, long rowNo, String methodList) {
        String res = "{ \"status\": -7, \"message\": \"Illegal Method other than (" + methodList + ") !\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } // sendMethodFailure()

    //-----------------------------------------------------------------------------

    /*@SAL
     *

    private static void sendMethodFailure(PrintWriter out, long rowNo) {
        String res = "{ \"status\": -7, \"message\": \"Illegal Method other than (GET,POST,PUT,DELETE) !\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } // sendMethodFailure()
    */
    //-----------------------------------------------------------------------------

    /*
 * @SAL
    private static void sendUserFailure(PrintWriter out, long rowNo) {
        String res = "{ \"status\": -9, \"message\": \"User failure\" }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "1", "Failure", res);
        return;
    } // sendUserFailure()
*/
    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataMobile mobile) {
        String res = "{ \"status\": 0, \"data\": " + mobile + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataMobile[] mobiles) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < mobiles.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + mobiles[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataSchedule schedule) {
        String res = "{ \"status\": 0, \"data\": " + schedule + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataSchedule[] schedules) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < schedules.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + schedules[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataLocation location) {
        String res = "{ \"status\": 0, \"data\": " + location + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataLocation[] locations) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < locations.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + locations[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataAlarm[] alarms) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < alarms.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + alarms[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataLocType[] locTypes) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < locTypes.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + locTypes[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataEventCode[] eventCodes) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < eventCodes.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + eventCodes[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataEvent[] events) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < events.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + events[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataRegion region) {
        String res = "{ \"status\": 0, \"data\": " + region + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataRegion[] regions) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < regions.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + regions[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataCampus campus) {
        String res = "{ \"status\": 0, \"data\": " + campus + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataCampus[] campuses) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < campuses.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + campuses[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataVenue venue) {
        String res = "{ \"status\": 0, \"data\": " + venue + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataVenue[] venues) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < venues.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + venues[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataFloor floor) {
        String res = "{ \"status\": 0, \"data\": " + floor + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataFloor[] floors) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < floors.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + floors[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataArea areas) {
        String res = "{ \"status\": 0, \"data\": " + areas + " }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

    //-----------------------------------------------------------------------------

    private static void sendItemData(PrintWriter out, long rowNo, DataArea[] areas) {
        String res = "{ \"status\": 0, \"data\": [\n";
        for (int i = 0; i < areas.length; i++) {
            if (i > 0)
                res += ",\n";
            res += "  " + areas[i];
        } // for()
        res += "] }";
        out.println(res);
        Utils.logWebServiceResponse(rowNo, "0", "Success", res);
        return;
    } // sendItemData()

}
