package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

import java.util.List;

public class DataBBolumFeedback {
    private long id = 0L;
    private String ts = null;
    private String  company = null;
    private long uavtId = 0;
    private String address = null;
    private int processStatus = 0;
    private String processTimestamp = null;
    private String adbVersion = null;
    private int sequenceNo = 0;
    private List feedbackResultList = null;
    
    private DataBBolumFeedback() {
        super();
    }
    public DataBBolumFeedback(long id) {
        this.id = id;
    }
    //-----------------------------------------------------------------------------
    public static DataBBolumFeedback getInstance(ResultSet rset ){
        DataBBolumFeedback di = new DataBBolumFeedback();
        int colno = 1;
        try {
            di.id = rset.getLong(colno++);
            di.ts = rset.getString(colno++);
            di.company = rset.getString(colno++);
            di.uavtId = rset.getLong(colno++);
            di.address = rset.getString(colno++);
            di.processStatus = rset.getInt(colno++);
            di.processTimestamp = rset.getString(colno++);  
            di.adbVersion = rset.getString(colno++);  
        } catch (Exception ex) {
          Utils.showError("DataBBolumFeedback getInstance1 EXCEPTION: " + ex.getMessage());
          di = null;
        }
        return di;
    }
    
    public static DataBBolumFeedback getInstanceList(ResultSet rset ){
        DataBBolumFeedback di = new DataBBolumFeedback();
        int colno = 1;
        try {
            di.id = rset.getLong(colno++);
            di.uavtId = rset.getLong(colno++);
            di.processStatus = rset.getInt(colno++);
        } catch (Exception ex) {
          ex.printStackTrace();
          di = null;
        }
        return di;
    }
//-----------------------------------------------------------------------------   
    public String toJson() {
        StringBuffer s = new StringBuffer();
        s.append("{\n");
        s.append("\"id\":" + id + ",\n");
        s.append("\"uavtId\":" + uavtId + ",\n");
        s.append("\"processStatus\":" + processStatus + "\n");
        s.append("}");
        return  s.toString();
    } // toJason()
 //-----------------------------------------------------------------------------   
    public String toJson(int responseType) {
        StringBuffer s = new StringBuffer();
        s.append("{\n");
        s.append("\"id\":" + id + "\n");
       
        if(responseType == 1){
            s.append(",");
            s.append("\"ts\": \"" + Utils.convStr2Json(ts)+ "\",\n");
            s.append("\"company\": \"" + Utils.convStr2Json(company) + "\",\n");
            s.append("\"uavtId\":" + uavtId + ",\n");
            s.append("\"address\": \"" + Utils.convStr2Json(address) + "\",\n");
            s.append("\"adbVersion\":\"" + Utils.convStr2Json(adbVersion)  + "\",\n");
            s.append("\"processStatus\":" + processStatus + ",\n");
            s.append("\"scripts\": [\n");
            if( feedbackResultList !=null ){
                for (int i = 0; i < feedbackResultList.size(); i++) {
                    if (i > 0)
                       s.append(",\n");
                    s.append("\"" +feedbackResultList.get(i).toString()+ "\"");
                } // for()
            }
            s.append("\n");
            s.append("  ]\n");
           // s.append("\"scripts\": \"" + Utils.convStr2Json(processTimestamp) + "\"");
        }
        s.append("\n");
        s.append("}");
        return  s.toString();
    } // toJason()

//----------------------------------------------------------------------------- 
    
    public String toXml() {
        StringBuffer s = new StringBuffer();
        s.append("<feedback>\n");
        s.append("<id>" + id + "</id>\n");
        s.append("<uavtId>" + uavtId+ "</uavtId>\n");
        s.append("<processStatus>" + processStatus+ "</processStatus>\n");
        s.append("</feedback>\n");
        return s.toString();
    } // toXml()
    
 //-----------------------------------------------------------------------------

    public String toXml(int responseType) {
        StringBuffer s = new StringBuffer();
        s.append("<feedback>\n");
        s.append("<id>" + id + "</id>\n");
        if(responseType == 1){
            s.append("<ts>" + Utils.convStr2Xml(ts) + "</ts>\n"); 
            s.append("<company>" + Utils.convStr2Xml(company) + "</company>\n");
            s.append("<uavtId>" + uavtId+ "</uavtId>\n");
            s.append("<address>" +  Utils.convStr2Xml(address)+ "</address>\n");
            s.append("<adbVersion>" +  Utils.convStr2Xml(adbVersion) + "</adbVersion>\n");
            s.append("<processStatus>" + processStatus+ "</processStatus>\n");
            s.append("<scripts>");
            if(feedbackResultList != null ){
                for (int i = 0; i < feedbackResultList.size(); i++) {
                    s.append("<script>\n");
                    s.append(Utils.convStr2Xml(feedbackResultList.get(i).toString()));
                    s.append("</script>\n");
                } // for()
            }
           s.append("</scripts>\n");
        }
        s.append("</feedback>\n");
        return s.toString();
    } //toXml(int responseType)


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getTs() {
        return ts;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setUavtId(long uavtId) {
        this.uavtId = uavtId;
    }

    public long getUavtId() {
        return uavtId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessTimestamp(String processTimestamp) {
        this.processTimestamp = processTimestamp;
    }

    public String getProcessTimestamp() {
        return processTimestamp;
    }


    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setFeedbackResultList(List feedbackResultList) {
        this.feedbackResultList = feedbackResultList;
    }

    public List getFeedbackResultList() {
        return feedbackResultList;
    }

    public void setAdbVersion(String adbVersion) {
        this.adbVersion = adbVersion;
    }

    public String getAdbVersion() {
        return adbVersion;
    }
}
