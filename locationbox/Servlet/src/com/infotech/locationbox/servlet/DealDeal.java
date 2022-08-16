package com.infotech.locationbox.servlet;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DealDeal {
    private long id = 0;
    private String name = null;
    private String category = null;
    private String termsOfUse = null;
    private String url = null;
    private String code = null;
    private String dealOwner = null;
    private long dealOwnerId = 0;
    private String shortDesc = null;
    private String longDesc = null;
    private String details = null;
    private double price = 0.00;
    private double discountPrice = 0.00;
    private double discountPercent = 0.00;
    private String currency = null;
    private String startDate = null;
    private String endDate = null;
    private String[] imageUrls = null;
    private int count = -1;

    public DealDeal() {
    }

    //-----------------------------------------------------------------------------

    public static DealDeal getInstance(ResultSet rset) {
        DealDeal dd = new DealDeal();
        try {
            dd.id = rset.getLong("ID");
            dd.name = rset.getString("NAME");
            if (dd.name != null)
                dd.name =
                    dd.name.trim().replaceAll("\"", "").replaceAll("\t", " ").replaceAll("\r", "").replaceAll("\n",
                                                                                                              " ");
            dd.category = rset.getString("CAT_NAME");
            dd.termsOfUse = rset.getString("TERMS_OF_USE");
            if (dd.termsOfUse != null)
                dd.termsOfUse =
                    dd.termsOfUse.trim().replaceAll("\"", "").replaceAll("\t", " ").replaceAll("\r",
                                                                                               "").replaceAll("\n",
                                                                                                              " ");
            dd.url = rset.getString("URL");
            dd.code = rset.getString("CODE");
            dd.dealOwner = rset.getString("DEAL_OWNER");
            dd.dealOwnerId = rset.getLong("DEAL_OWNER_ID");
            dd.shortDesc = rset.getString("SHORT_DESC");
            if (dd.shortDesc != null)
                dd.shortDesc =
                    dd.shortDesc.trim().replaceAll("\"", "").replaceAll("\t", " ").replaceAll("\r", "").replaceAll("\n",
                                                                                                                   " ");
            dd.longDesc = rset.getString("LONG_DESC");
            if (dd.longDesc != null)
                dd.longDesc =
                    dd.longDesc.trim().replaceAll("\"", "").replaceAll("\t", " ").replaceAll("\r", "").replaceAll("\n",
                                                                                                                  " ");
            dd.details = rset.getString("DETAILS");
            if (dd.details != null)
                dd.details =
                    dd.details.trim().replaceAll("\"", "").replaceAll("\t", " ").replaceAll("\r", "").replaceAll("\n",
                                                                                                                 " ");
            dd.price = rset.getDouble("PRICE");
            dd.discountPrice = rset.getDouble("DISCOUNT_PRICE");
            dd.discountPercent = rset.getDouble("DISCOUNT_PERCENTAGE");
            dd.currency = rset.getString("CURRENCY");
            dd.startDate = rset.getString("START_DATE");
            dd.endDate = rset.getString("END_DATE");
            dd.imageUrls = getDealImages(dd.id);
            return dd;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"id\": " + id + ",\n";
        s += indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n";
        s += indent + "  \"category\": \"" + (category == null ? "" : Utils.convStr2Json(category)) + "\",\n";
        s += indent + "  \"termsofuse\": \"" + (termsOfUse == null ? "" : Utils.convStr2Json(termsOfUse)) + "\",\n";
        s += indent + "  \"url\": \"" + (url == null ? "" : Utils.convStr2Json(url)) + "\",\n";
        s += indent + "  \"code\": \"" + (code == null ? "" : Utils.convStr2Json(code)) + "\",\n";
        s += indent + "  \"dealowner\": \"" + (dealOwner == null ? "" : Utils.convStr2Json(dealOwner)) + "\",\n";
        s += indent + "  \"dealownerid\": " + dealOwnerId + ",\n";
        s += indent + "  \"shortdesc\": \"" + (shortDesc == null ? "" : Utils.convStr2Json(shortDesc)) + "\",\n";
        s += indent + "  \"longdesc\": \"" + (longDesc == null ? "" : Utils.convStr2Json(longDesc)) + "\",\n";
        s += indent + "  \"details\": \"" + (details == null ? "" : Utils.convStr2Json(details)) + "\",\n";
        s += indent + "  \"price\": \"" + price + "\",\n";
        s += indent + "  \"discountprice\": \"" + discountPrice + "\",\n";
        s += indent + "  \"discountpercent\": \"" + discountPercent + "\",\n";
        s += indent + "  \"currency\": \"" + (currency == null ? "" : Utils.convStr2Json(currency)) + "\",\n";
        s += indent + "  \"startdate\": \"" + (startDate == null ? "" : startDate) + "\",\n";
        s += indent + "  \"enddate\": \"" + (endDate == null ? "" : endDate) + "\",\n";
        for (int i = 0; i < 5; i++) {
            s +=
                indent + "  \"imageurl" + (i + 1) + "\": \"" +
                (imageUrls != null && i < imageUrls.length ? imageUrls[i] : "") + "\",\n";
        } // for()
        s += indent + "  \"count\": \"" + count + "\"\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <campaign>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
        s += "      <category>" + (category == null ? "" : Utils.convStr2Xml(category)) + "</category>\n";
        s += "      <termsofuse>" + (termsOfUse == null ? "" : Utils.convStr2Xml(termsOfUse)) + "</termsofuse>\n";
        s += "      <url>" + (url == null ? "" : Utils.convStr2Xml(url)) + "</url>\n";
        s += "      <code>" + (code == null ? "" : Utils.convStr2Xml(code)) + "</code>\n";
        s += "      <dealowner>" + (dealOwner == null ? "" : Utils.convStr2Xml(dealOwner)) + "</dealowner>\n";
        s += "      <dealownerid>" + dealOwnerId + "</dealownerid>\n";
        s += "      <shortdesc>" + (shortDesc == null ? "" : Utils.convStr2Xml(shortDesc)) + "</shortdesc>\n";
        s += "      <longdesc>" + (longDesc == null ? "" : Utils.convStr2Xml(longDesc)) + "</longdesc>\n";
        s += "      <details>" + (details == null ? "" : Utils.convStr2Xml(details)) + "</details>\n";
        s += "      <price>" + price + "</price>\n";
        s += "      <discountprice>" + discountPrice + "</discountprice>\n";
        s += "      <discountpercent>" + discountPercent + "</discountpercent>\n";
        s += "      <currency>" + (currency == null ? "" : Utils.convStr2Xml(currency)) + "</currency>\n";
        s += "      <startdate>" + (startDate == null ? "" : startDate) + "</startdate>\n";
        s += "      <enddate>" + (endDate == null ? "" : endDate) + "</enddate>\n";
        for (int i = 0; i < 5; i++) {
            s +=
                "      <imageurl" + (i + 1) + ">" + (imageUrls != null && i < imageUrls.length ? imageUrls[i] : "") +
                "</imageurl" + (i + 1) + ">\n";
        } // for()
        s += "      <count>" + count + "</count>\n";
        s += "    </campaign>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    private static String[] getDealImages(long id) {
        ArrayList array = new ArrayList();

        String imagesPath = Utils.getParameter("deal_img_path");
        String urlPrefix = Utils.getParameter("deal_url_prefix");
        String namePrefix = "deal_" + id;

        File p = new File(imagesPath);
        File fs[] = p.listFiles();
        if (fs == null)
            return null;

        for (int i = 0; i < fs.length; i++) {
            String name = fs[i].getName();
            if (!name.startsWith(namePrefix))
                continue;

            array.add(name);
        } // for()

        if (array.size() <= 0)
            return null;

        String[] imgurls = new String[array.size()];
        for (int i = 0; i < imgurls.length; i++)
            imgurls[i] = urlPrefix + "/" + (String) array.get(i);
        return imgurls;
    } // getDealImages()

    //-----------------------------------------------------------------------------

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setTermsOfUse(String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    public String getTermsOfUse() {
        return termsOfUse;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDealOwner(String dealOwner) {
        this.dealOwner = dealOwner;
    }

    public String getDealOwner() {
        return dealOwner;
    }

    public void setDealOwnerId(long dealOwnerId) {
        this.dealOwnerId = dealOwnerId;
    }

    public long getDealOwnerId() {
        return dealOwnerId;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

}
