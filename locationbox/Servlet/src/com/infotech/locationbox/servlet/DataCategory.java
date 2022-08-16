package com.infotech.locationbox.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DataCategory {
    private String category = null;
    private String description = null;
    private long brandId = 0;

    public DataCategory() {
        super();
    }

    public DataCategory(String category, String description, long brandId) {
        this.category = category;
        this.description = description;
        this.brandId = brandId;
    }

    //-----------------------------------------------------------------------------

    public static String getCategorySqlQuery(String key, String category, String brand) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        if ((category == null || category.length() <= 0) && (brand == null || brand.length() <= 0))
            return null;

        if (category != null && category.length() > 0) {
            if (brand != null && brand.length() > 0) {
                brand = brand + "_" + category;
                category = null;
            }
        }

        try {
            cnn = DbConn.getPooledConnection();
            //--- LBS_POI_CATEGORY_KEY --------------------------------------------
            if (category != null && category.length() > 0)
                sql = "SELECT SQL_QUERY FROM LBS_POI_CATEGORY_KEY WHERE KEY=? AND CATEGORY=? AND BRAND_ID = 0";
            else
                sql = "SELECT SQL_QUERY FROM LBS_POI_CATEGORY_KEY WHERE KEY=? AND CATEGORY=? AND BRAND_ID <> 0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            if (category != null && category.length() > 0)
                pstmt.setString(2, Utils.convToUpperEnglishChars(category));
            else
                pstmt.setString(2, Utils.convToUpperEnglishChars(brand));
            rset = pstmt.executeQuery();
            if (rset.next()) {
                String sqlQuery = rset.getString(1);
                return sqlQuery;
            }
            
            DbConn.closeDBConnection(pstmt, rset);
            
            //--- LBS_POI_CATEGORY ------------------------------------------------
            if (category != null && category.length() > 0)
                sql = "SELECT SQL_QUERY FROM LBS_POI_CATEGORY WHERE CATEGORY=? AND BRAND_ID = 0";
            else
                sql = "SELECT SQL_QUERY FROM LBS_POI_CATEGORY WHERE CATEGORY=? AND BRAND_ID <> 0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            if (category != null && category.length() > 0)
                pstmt.setString(1, Utils.convToUpperEnglishChars(category));
            else
                pstmt.setString(1, Utils.convToUpperEnglishChars(brand));
            rset = pstmt.executeQuery();
            if (rset.next()) {
                String sqlQuery = rset.getString(1);
                return sqlQuery;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getCategorySqlQuery()
    
    public static String getCategoriesSqlQuery(String key, String category, String brand) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String sqlQuery = null;
        if ((category == null || category.length() <= 0) && (brand == null || brand.length() <= 0))
            return sqlQuery;

        if (category != null && category.length() > 0) {
            if (brand != null && brand.length() > 0) {
                String [] categories = category.split(",");
                
                for(int i = 0; i<categories.length;i++){
                    String temp = brand + "_" + categories[i];
                    if( i == 0 )
                       brand = temp;
                    else {
                        brand = brand + "," + temp;
                    }
                }
                
                category = null;
            }
        }

        try {
            cnn = DbConn.getPooledConnection();
            //--- LBS_POI_CATEGORY_KEY --------------------------------------------
            if (category != null && category.length() > 0)
                sql = "SELECT SQL_QUERY FROM LBS_POI_CATEGORY_KEY WHERE KEY=? AND CATEGORY IN(SELECT * FROM TABLE(CAST(IN_STRING_LIST(?) AS STRING_TABLE))) AND BRAND_ID = 0";
            else
                sql = "SELECT SQL_QUERY FROM LBS_POI_CATEGORY_KEY WHERE KEY=? AND CATEGORY IN(SELECT * FROM TABLE(CAST(IN_STRING_LIST(?) AS STRING_TABLE))) AND BRAND_ID <> 0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            if (category != null && category.length() > 0)
                pstmt.setString(2, Utils.toUpperCase(category));
            else
                pstmt.setString(2, Utils.toUpperCase(brand));
            rset = pstmt.executeQuery();
            int count = 0;
            String whereQuery = null;
            String selectQuery = null;
            while (rset.next()) {
                 String str = rset.getString(1);
                 int posOfInx = str.toUpperCase().indexOf("WHERE");
                 int orderposOfInx = str.toUpperCase().indexOf("ORDER");
                
                 String selectQTmp = null;
                 String whereQueryQTmp = null;
                
                if(posOfInx>0){
                   selectQTmp = str.substring(0,posOfInx);
                   whereQueryQTmp = (orderposOfInx>0) ? str.substring(posOfInx + 5, orderposOfInx) : str.substring(posOfInx + 5, str.length());
                   whereQueryQTmp = " ( " + whereQueryQTmp + " ) ";
                }
                
                if(count == 0){
                    selectQuery = selectQTmp;
                    whereQuery = whereQueryQTmp;
                }else{
                    whereQuery = whereQuery + " OR " +whereQueryQTmp; 
                }
                
               count++;
            }
           
            if( count == 0 ){
            
                DbConn.closeDBConnection(pstmt, rset);
                
                //--- LBS_POI_CATEGORY ------------------------------------------------
                if (category != null && category.length() > 0)
                    sql = "SELECT SQL_QUERY FROM LBS_POI_CATEGORY WHERE CATEGORY IN(SELECT * FROM TABLE(CAST(IN_STRING_LIST(?) AS STRING_TABLE))) AND BRAND_ID = 0";
                else
                    sql = "SELECT SQL_QUERY FROM LBS_POI_CATEGORY WHERE CATEGORY IN(SELECT * FROM TABLE(CAST(IN_STRING_LIST(?) AS STRING_TABLE))) AND BRAND_ID <> 0";
                pstmt = cnn.prepareStatement(sql);
                pstmt.clearParameters();
                if (category != null && category.length() > 0)
                    pstmt.setString(1, Utils.toUpperCase(category));
                else
                    pstmt.setString(1, Utils.toUpperCase(brand));
                rset = pstmt.executeQuery();
                while(rset.next()) {
                    String str = rset.getString(1);
                    int posOfInx = str.toUpperCase().indexOf("WHERE");
                    int orderposOfInx = str.toUpperCase().indexOf("ORDER");
                    
                    String selectQTmp = null;
                    String whereQueryQTmp = null;
                    
                    if(posOfInx>0){
                      selectQTmp = str.substring(0,posOfInx);
                      whereQueryQTmp = (orderposOfInx>0) ? str.substring(posOfInx + 5, orderposOfInx) : str.substring(posOfInx + 5, str.length());
                      whereQueryQTmp = " ( " + whereQueryQTmp + " ) ";
                    }
                    
                    if(count == 0){
                       selectQuery = selectQTmp;
                       whereQuery = whereQueryQTmp;
                    }else{
                       whereQuery = whereQuery + " OR " + whereQueryQTmp; 
                    }
                    
                    count++;
                }
            }
           if( count > 0 )
            sqlQuery = selectQuery + " WHERE " + whereQuery; 
            
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return sqlQuery;
    } // getCategoriesSqlQuery()
    
    //-----------------------------------------------------------------------------

    public static boolean isInArray(ArrayList array, String category) {
        for (int i = 0; i < array.size(); i++) {
            DataCategory dc = (DataCategory) array.get(i);
            if (dc.category.equals(category))
                return true;

        } // for()
        return false;
    } // isInArray()

    //-----------------------------------------------------------------------------

    public String toJson() {
        return "{ \"brand\": \"" + Utils.convStr2Json(category) + "\", \"description\": \"" +
               Utils.convStr2Json(description) + "\", \"brandid\": " + brandId + " }";
    } // toJason()

    //-----------------------------------------------------------------------------

    public String toXml() {
        return "<brand>" + Utils.convStr2Xml(category) + "</brand><description>" + Utils.convStr2Xml(description) +
               "</description><brandid>" + brandId + "</brandid>\n";
    } // toXml()

    //-----------------------------------------------------------------------------

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

}
