package com.infotech.locationbox.servlet;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CampCampaign {
    private long id = 0;
    private String name = null;
    private String description = null;
    private String category = null;
    private boolean hot = false;
    private String startDate = null;
    private String endDate = null;
    private long brandId = 0;
    private String brandName = null;
    private String productName = null;
    private String productTitle = null;
    private String productDetails = null;
    private String productPayment = null;
    private double productListPrice = 0.00;
    private double productPrice = 0.00;
    private double productDiscountPercent = 0.00;
    private int stockAmount = 0;
    private String[] imageUrls = null;
    private int count = -1;

    private DataPoi poi = null;

    public CampCampaign() {
    }

    //-----------------------------------------------------------------------------

    public static CampCampaign getInstance(ResultSet rset) {
        CampCampaign cc = new CampCampaign();
        try {
            cc.id = rset.getLong("CAMPAIGN_ID");
            cc.name = rset.getString("NAME");
            cc.description = rset.getString("DESCRIPTION");
            cc.category = rset.getString("CATEGORY");
            cc.hot = rset.getInt("HOT") != 0;
            cc.startDate = rset.getString("START_DATE");
            cc.endDate = rset.getString("END_DATE");
            cc.brandId = rset.getLong("BRAND_ID");
            cc.brandName = rset.getString("BRAND_NAME");
            cc.productName = rset.getString("PRODUCT_NAME");
            cc.productTitle = rset.getString("PRODUCT_TITLE");
            cc.productDetails = rset.getString("PRODUCT_DETAILS");
            cc.productPayment = rset.getString("PRODUCT_PAYMENT");
            cc.productListPrice = rset.getDouble("PRODUCT_LIST_PRICE");
            cc.productPrice = rset.getDouble("PRODUCT_PRICE");
            cc.productDiscountPercent = rset.getDouble("PRODUCT_DISCOUNT_PERCENT");
            cc.stockAmount = rset.getInt("STOCK_AMOUNT");
            cc.imageUrls = getCampaignImages(cc.id);
            try {
                cc.poi = DataPoi.getInstance(rset);
            } catch (Exception e) {
                ;
            }
            try {
                cc.poi.distance = rset.getDouble("DISTANCE");
            } catch (Exception e) {
                ;
            }
            return cc;
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
        s += indent + "  \"name\": \"" + Utils.convStr2Json(name) + "\",\n";
        s += indent + "  \"description\": \"" + Utils.convStr2Json(description) + "\",\n";
        s += indent + "  \"category\": \"" + Utils.convStr2Json(category) + "\",\n";
        s += indent + "  \"hot\": \"" + hot + "\",\n";
        s += indent + "  \"startdate\": \"" + (startDate == null ? "" : startDate) + "\",\n";
        s += indent + "  \"enddate\": \"" + (endDate == null ? "" : endDate) + "\",\n";
        s += indent + "  \"brandid\": " + brandId + ",\n";
        s += indent + "  \"brandname\": \"" + Utils.convStr2Json(brandName) + "\",\n";
        s += indent + "  \"productname\": \"" + Utils.convStr2Json(productName) + "\",\n";
        s += indent + "  \"producttitle\": \"" + Utils.convStr2Json(productTitle) + "\",\n";
        s += indent + "  \"productdetails\": \"" + Utils.convStr2Json(productDetails) + "\",\n";
        s += indent + "  \"productpayment\": \"" + Utils.convStr2Json(productPayment) + "\",\n";
        s += indent + "  \"productlistprice\": \"" + productListPrice + "\",\n";
        s += indent + "  \"productprice\": \"" + productPrice + "\",\n";
        s += indent + "  \"discountpercent\": \"" + productDiscountPercent + "\",\n";
        s += indent + "  \"stockamount\": \"" + stockAmount + "\",\n";
        for (int i = 0; i < 5; i++) {
            s +=
                indent + "  \"imageurl" + (i + 1) + "\": \"" +
                (imageUrls != null && i < imageUrls.length ? imageUrls[i] : "") + "\",\n";
        } // for()
        s += indent + "  \"count\": " + count;
        if (poi != null) {
            s += ",\n";
            s += indent + "  \"poi\": " + poi.toJson(indent + "    ");
        }
        s += "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <campaign>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <name>" + Utils.convStr2Xml(name) + "</name>\n";
        s += "      <description>" + Utils.convStr2Xml(description) + "</description>\n";
        s += "      <category>" + Utils.convStr2Xml(category) + "</category>\n";
        s += "      <hot>" + (hot ? 1 : 0) + "</hot>\n";
        s += "      <startdate>" + (startDate == null ? "" : startDate) + "</startdate>\n";
        s += "      <enddate>" + (endDate == null ? "" : endDate) + "</enddate>\n";
        s += "      <brandid>" + brandId + "</brandid>\n";
        s += "      <brandname>" + Utils.convStr2Xml(brandName) + "</brandname>\n";
        s += "      <productname>" + Utils.convStr2Xml(productName) + "</productname>\n";
        s += "      <producttitle>" + Utils.convStr2Xml(productTitle) + "</producttitle>\n";
        s += "      <productdetails>" + Utils.convStr2Xml(productDetails) + "</productdetails>\n";
        s += "      <productpayment>" + Utils.convStr2Xml(productPayment) + "</productpayment>\n";
        s += "      <productlistprice>" + productListPrice + "</productlistprice>\n";
        s += "      <productprice>" + productPrice + "</productprice>\n";
        s += "      <discountpercent>" + productDiscountPercent + "</discountpercent>\n";
        s += "      <stockamount>" + stockAmount + "</stockamount>\n";
        for (int i = 0; i < 5; i++) {
            s +=
                "      <imageurl" + (i + 1) + ">" + (imageUrls != null && i < imageUrls.length ? imageUrls[i] : "") +
                "</imageurl" + (i + 1) + ">\n";
        } // for()
        s += "      <count>" + count + "</count>\n";
        if (poi != null) {
            s += poi.toXml();
        }
        s += "    </campaign>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    private static String[] getCampaignImages(long id) {
        ArrayList array = new ArrayList();

        String imagesPath = Utils.getParameter("campaign_img_path");
        String urlPrefix = Utils.getParameter("campaign_url_prefix");
        String namePrefix = "camp_" + id;

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
    } // getCampaignImages()

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public boolean isHot() {
        return hot;
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

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductPayment(String productPayment) {
        this.productPayment = productPayment;
    }

    public String getProductPayment() {
        return productPayment;
    }

    public void setProductListPrice(double productListPrice) {
        this.productListPrice = productListPrice;
    }

    public double getProductListPrice() {
        return productListPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductDiscountPercent(double productDiscountPercent) {
        this.productDiscountPercent = productDiscountPercent;
    }

    public double getProductDiscountPercent() {
        return productDiscountPercent;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }

    public int getStockAmount() {
        return stockAmount;
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

    public void setPoi(DataPoi poi) {
        this.poi = poi;
    }

    public DataPoi getPoi() {
        return poi;
    }

}
