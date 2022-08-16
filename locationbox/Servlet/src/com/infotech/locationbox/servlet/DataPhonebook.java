package com.infotech.locationbox.servlet;


public class DataPhonebook implements Comparable<DataPhonebook> {

    private long id = 0;
    private String firstName = null;
    private String lastName = null;
    private String telNo = null;
    private String operator = null;
    private long cityCode = 0;
    private String adrIl = null;
    private String adrIlce = null;
    private int score = 0;
    private String customer = null;
    private String address = null;

    public DataPhonebook() {

    }


    public DataPhonebook(long id, String firstName, String lastName, String customer, String telNo, String operator,
                         long cityCode, String address, String adrIl, String adrIlce, int score) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.customer = customer;
        this.telNo = telNo;
        this.operator = operator;
        this.cityCode = cityCode;
        this.address = address;
        this.adrIl = adrIl;
        this.adrIlce = adrIlce;
        this.score = score;
    }


    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"id\": " + id + ",\n";
        s += indent + "  \"firstname\": \"" + Utils.convStr2Json(firstName) + "\",\n";
        s += indent + "  \"lastname\": \"" + Utils.convStr2Json(lastName) + "\",\n";
        s += indent + "  \"telno\": \"" + telNo + "\",\n";
        s += indent + "  \"operator\": \"" + operator + "\",\n";
        s += indent + "  \"il\": \"" + Utils.convStr2Json(adrIl) + "\",\n";
        s += indent + "  \"ilce\": \"" + Utils.convStr2Json(adrIlce) + "\",\n";
        s += indent + "  \"score\": \"" + score + "\" \n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <result>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <firstname>" + Utils.convStr2Xml(firstName) + "</firstname>\n";
        s += "      <lastname>" + Utils.convStr2Xml(lastName) + "</lastname>\n";
        s += "      <telno>" + telNo + "</telno>\n";
        s += "      <operator>" + operator + "</operator>\n";
        s += "      <il>" + Utils.convStr2Xml(adrIl) + "</il>\n";
        s += "      <ilce>" + Utils.convStr2Xml(adrIlce) + "</ilce>\n";
        s += "      <score>" + score + "</score>\n";
        s += "    </result>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }

    public void getTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setCityCode(long cityCode) {
        this.cityCode = cityCode;
    }

    public long getCityCode() {
        return cityCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAdrIl(String adrIl) {
        this.adrIl = adrIl;
    }

    public String getAdrIl() {
        return adrIl;
    }

    public void setAdrIlce(String adrIlce) {
        this.adrIlce = adrIlce;
    }

    public String getAdrIlce() {
        return adrIlce;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(DataPhonebook dp) {

        int c;
        c = new Long(this.id).compareTo(new Long(dp.id));
        if (c == 0)
            c = new Integer(this.score).compareTo(new Integer(dp.score));
        return c;
    }
}
