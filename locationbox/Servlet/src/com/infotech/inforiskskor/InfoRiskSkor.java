package com.infotech.inforiskskor;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.infotech.locationbox.servlet.DbConn;

public class InfoRiskSkor {
  private static final int RESULT_SUCCESS = 0;
  private static final int RESULT_DB_NOT_CONNECTED = -1;
  private static final int RESULT_BEYAN_VALUE_GT_ZERO = -3;
  private static final int RESULT_NOTFOUND_PRMDETAYID = -5;
  private static final int RESULT_NOTFOUND_GEO_RECORD = -6;
  private static final int RESULT_NOTFOUND_PRMDETAYSINIF = -7;
  private static final int RESULT_NOTFOUND_DHO_SIDDET = -9;
  private static final int RESULT_NOTDEFINED_DEPREM_SINIF = -10;
  private static final int RESULT_NOTFOUND_DEPREM_HASAR_ORAN = -11;
  private static final int RESULT_NOTFOUND_DEPREM_DATA = -12;
  private static final int RESULT_INVALID_COORDINATE = -101;
  private static final int RESULT_INVALID_ADDR_LEVEL = -102;
  private static final int RESULT_INVALID_TEMINAT = -103;
  private static final int RESULT_INVALID_RISK_SCORE = -104;
  private int gAdresSeviye = 0;

  public InfoRiskSkor() {
  }


  public SkorResultInfo getSkor(String key, int teminatTuru, double xCoor, double yCoor, int adresSeviye, int[] beyanList) {

    SkorRequestInfo skorRequst = new SkorRequestInfo(key, teminatTuru, xCoor, yCoor, adresSeviye, beyanList);
    SkorResultInfo skorResult = new SkorResultInfo();

    if (xCoor <= 0 || yCoor <= 0) {
      skorResult.setResultCode(RESULT_INVALID_COORDINATE);
      return skorResult;
    }

    if (adresSeviye <= 0) {
      skorResult.setResultCode(RESULT_INVALID_ADDR_LEVEL);
      return skorResult;
    }

    if (adresSeviye < 3) {
      adresSeviye = 3;
    }

    if (adresSeviye == 7 || adresSeviye == 8)
      adresSeviye = 6;

    gAdresSeviye = adresSeviye;

    //il,ilce seviyesinden buyuk yani mahalle seviyesinden itibaren skor hesaplanmalidir.
    if (adresSeviye > 8) {
      skorResult.setResultCode(RESULT_INVALID_ADDR_LEVEL);
      return skorResult;
    }

    switch (teminatTuru) {
      case 1:
        skorResult = getYanginSkor(skorRequst);
        break;
      case 2:
        skorResult = getSelSkor(skorRequst);
        break;
      case 3:
        skorResult = getHirsizlikSkor(skorRequst);
        break;
      case 4:
        skorResult = getTerorSkor(skorRequst);
        break;
      case 5:
        skorResult = getDepremSkor(skorRequst);
        break;
      case 6:
        skorResult = getZeminSkor(skorRequst);
        break;
      case 7:
        skorResult = getFirtinaSkor(skorRequst);
        break;
      case 8:
        skorResult = getKarSkor(skorRequst);
        break;
      case 9:
        skorResult = getDoluSkor(skorRequst);
        break;
      default:
        skorResult.setResultCode(RESULT_INVALID_TEMINAT);
        break;
    }

    return skorResult;
  } //getSkor


  public SkorResultInfo getYanginSkor(SkorRequestInfo skorRequest) {
    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();

    Connection cnn = DbConn.getPooledConnection();
    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));

    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    long tempIlceId = 0;

    try {
      String yanginCikma = "Veri Yok";
      String yanginYayilma = "Veri Yok";
      String yanginUlasim = "Veri Yok";
      String yanginItfaiye = "Veri Yok";
      String yanginCivar = "Veri Yok";
      String yanginArazi = "Veri Yok";
      String yanginDogalgaz = "Veri Yok";
      String yanginLpg = "Veri Yok";

      //koordinata ait ilce var mi ?
      tempIlceId = getIlceId(cnn, skorRequest);
      if (tempIlceId == 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
        //return skorResult;
      }

      if (gAdresSeviye == 6 && tempIlceId > 0) {
        //bulunan ilceye ait kapi var mi ?
        sql = "Select COUNT(*) as CNT From RISK_KAPI Where ILCE_ID=" + tempIlceId;
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
        rset.next();
        //KAPI tablosunda ilceye ait data var ise kullanilir, eger yok ise IDARI_SINIR_YOL tablosundaki risk donulur.
        if (rset.getLong("CNT") > 0) {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select YANGIN_CIKMA, YANGIN_YAYILMA, YANGIN_ULASIM, YANGIN_ITFAIYE, YANGIN_CIVAR, YANGIN_ARAZI, YANGIN_DOGALGAZ, YANGIN_LPG ";
          sql += getSqlPart(skorRequest);
          sql += " AND ILCE_ID = " + tempIlceId;
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
        } else {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select YANGIN_CIKMA, YANGIN_YAYILMA, YANGIN_ULASIM, YANGIN_ITFAIYE, YANGIN_CIVAR, YANGIN_ARAZI, YANGIN_DOGALGAZ, YANGIN_LPG ";
          sql += getSqlPart2(skorRequest);
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
          //                    if (!rset.next()) {
          //                        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
          //                        return skorResult;
          //                    }
        }
      } else if (tempIlceId > 0) {
        DbConn.closeDBConnection(pstmt, rset);
        sql = "Select YANGIN_CIKMA, YANGIN_YAYILMA, YANGIN_ULASIM, YANGIN_ITFAIYE, YANGIN_CIVAR, YANGIN_ARAZI, YANGIN_DOGALGAZ, YANGIN_LPG ";
        sql += getSqlPart(skorRequest);
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
      }

      if (rset != null && rset.next()) {
        yanginCikma = rset.getString("YANGIN_CIKMA");
        yanginYayilma = rset.getString("YANGIN_YAYILMA");
        yanginUlasim = rset.getString("YANGIN_ULASIM");
        yanginItfaiye = rset.getString("YANGIN_ITFAIYE");
        yanginCivar = rset.getString("YANGIN_CIVAR");
        yanginArazi = rset.getString("YANGIN_ARAZI");
        yanginDogalgaz = rset.getString("YANGIN_DOGALGAZ");
        yanginLpg = rset.getString("YANGIN_LPG");
      }

      //Risk Agirliklari
      double[] agirlikArray = new double[31];
      agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());

      //Yangin Cikma
      prmValue[1] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 1, yanginCikma, skorRequest.getKey());
      if (prmValue[1] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[1] = prmValue[1] * agirlikArray[1];
      skorResult.setSkorCog(prmValue[1], 1);

      //Yangin Yayilma
      prmValue[2] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 2, yanginYayilma, skorRequest.getKey());
      if (prmValue[2] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[2] = prmValue[2] * agirlikArray[2];
      skorResult.setSkorCog(prmValue[2], 2);

      //Ulasim Durumu
      prmValue[3] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 3, yanginUlasim, skorRequest.getKey());
      if (prmValue[3] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[3] = prmValue[3] * agirlikArray[3];
      skorResult.setSkorCog(prmValue[3], 3);


      //Itfaiye Mesafesi
      prmValue[4] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 4, yanginItfaiye, skorRequest.getKey());
      if (prmValue[4] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[4] = prmValue[4] * agirlikArray[4];
      skorResult.setSkorCog(prmValue[4], 4);


      //Civar Yangin Tehlikesi
      prmValue[5] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 5, yanginCivar, skorRequest.getKey());
      if (prmValue[5] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[5] = prmValue[5] * agirlikArray[5];
      skorResult.setSkorCog(prmValue[5], 5);


      //Arazi Kullanim
      prmValue[6] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 6, yanginArazi, skorRequest.getKey());
      if (prmValue[6] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[6] = prmValue[6] * agirlikArray[6];
      skorResult.setSkorCog(prmValue[6], 6);


      //Dogalgaz Servis Kutusu Mesafesi
      prmValue[7] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 7, yanginDogalgaz, skorRequest.getKey());
      if (prmValue[7] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[7] = prmValue[7] * agirlikArray[7];
      skorResult.setSkorCog(prmValue[7], 7);

      //LPG Dokme Gaz
      prmValue[8] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 8, yanginLpg, skorRequest.getKey());
      if (prmValue[8] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[8] = prmValue[8] * agirlikArray[8];
      skorResult.setSkorCog(prmValue[8], 8);

      //Hasar Gecmisi -- yapiya gore entegre edilecek
      prmValue[9] = 0;
      if (prmValue[9] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[9] = prmValue[9] * agirlikArray[9];
      skorResult.setSkorCog(prmValue[9], 9);

      for (int ii = 1; ii <= 20; ii++) {

        prmValue[ii + 9] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 9, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
        if (prmValue[ii + 9] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
          return skorResult;
        }
        prmValue[ii + 9] = prmValue[ii + 9] * agirlikArray[ii + 9];
        skorResult.setSkorBeyan(prmValue[ii + 9], ii);

      }

      double yanginSkor = 0.0;
      for (int ii = 1; ii <= 29; ii++) {
        yanginSkor += prmValue[ii];
      }
      
      skorResult.setSkorValue(yanginSkor);

      if (tempIlceId > 0) {
        skorResult.setResultCode(RESULT_SUCCESS);
        skorResult = checkRiskScore(skorResult);
      }

      //return skorResult;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
       DbConn.closeDBConnection(pstmt, rset);
       DbConn.closeConnection(cnn);
    }

    return skorResult;
  } //getYanginSkor

  public SkorResultInfo getSelSkor(SkorRequestInfo skorRequest) {
    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();

    Connection cnn = DbConn.getPooledConnection();
    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));
    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }
    /*
        if (skorRequest.getBeyan1() == 0 || skorRequest.getBeyan2() == 0) {
            skorResult.setResultCode(RESULT_BEYAN_VALUE_GT_ZERO);
            return skorResult;
        }*/

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    long tempIlceId = 0;

    try {

      String selCTarsimSelSu = "Veri Yok";
      String selSuMesafe = "Veri Yok";
      String selSuYukseklik = "Veri Yok";
      String selDenizMesafe = "Veri Yok";
      String selDenizYukseklik = "Veri Yok";
      String selAltYapi = "Veri Yok";

      //koordinata ait ilce var mi ?
      tempIlceId = getIlceId(cnn, skorRequest);
      if (tempIlceId == 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
        //return skorResult;
      }

      if (gAdresSeviye == 6 && tempIlceId > 0) {
        //bulunan ilceye ait kapi var mi ?
        sql = "Select COUNT(*) as CNT From RISK_KAPI Where ILCE_ID=" + tempIlceId;
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
        rset.next();
        //KAPI tablosunda ilceye ait data var ise kullanilir, eger yok ise IDARI_SINIR_YOL tablosundaki risk donulur.
        if (rset.getLong("CNT") > 0) {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select C_TARSIM_SEL_SU, SEL_SU_MESAFE, SEL_SU_YUKSEKLIK, SEL_DENIZ_MESAFE, SEL_DENIZ_YUKSEKLIK, SEL_ALTYAPI ";
          sql += getSqlPart(skorRequest);
          sql += " AND ILCE_ID = " + tempIlceId;
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
        } else {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select C_TARSIM_SEL_SU, SEL_SU_MESAFE, SEL_SU_YUKSEKLIK, SEL_DENIZ_MESAFE, SEL_DENIZ_YUKSEKLIK, SEL_ALTYAPI ";
          sql += getSqlPart2(skorRequest);
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
          //                    if (!rset.next()) {
          //                        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
          //                        return skorResult;
          //                    }
        }
      } else if (tempIlceId > 0) {
        DbConn.closeDBConnection(pstmt, rset);
        sql = "Select C_TARSIM_SEL_SU, SEL_SU_MESAFE, SEL_SU_YUKSEKLIK, SEL_DENIZ_MESAFE, SEL_DENIZ_YUKSEKLIK, SEL_ALTYAPI ";
        sql += getSqlPart(skorRequest);
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
      }
      if (rset != null && rset.next()) {
        selCTarsimSelSu = rset.getString("C_TARSIM_SEL_SU");
        selSuMesafe = rset.getString("SEL_SU_MESAFE");
        selSuYukseklik = rset.getString("SEL_SU_YUKSEKLIK");
        selDenizMesafe = rset.getString("SEL_DENIZ_MESAFE");
        selDenizYukseklik = rset.getString("SEL_DENIZ_YUKSEKLIK");
        selAltYapi = rset.getString("SEL_ALTYAPI");
      }
      //Risk Agirliklari
      double[] agirlikArray = new double[31];
      agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());


      //c tarsim sel su
      prmValue[1] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 1, selCTarsimSelSu, skorRequest.getKey());
      if (prmValue[1] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[1] = prmValue[1] * agirlikArray[1];
      skorResult.setSkorCog(prmValue[1], 1);

      //Riziko adresinin su kaynagina mesafesi
      prmValue[2] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 2, selSuMesafe, skorRequest.getKey());
      if (prmValue[2] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[2] = prmValue[2] * agirlikArray[2];
      skorResult.setSkorCog(prmValue[2], 2);

      //Riziko adresinin su kaynagina yuksekligi
      prmValue[3] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 3, selSuYukseklik, skorRequest.getKey());
      if (prmValue[3] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[3] = prmValue[3] * agirlikArray[3];
      skorResult.setSkorCog(prmValue[3], 3);

      //Riziko adresinin denize mesafesi
      prmValue[4] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 4, selDenizMesafe, skorRequest.getKey());
      if (prmValue[4] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[4] = prmValue[4] * agirlikArray[4];
      skorResult.setSkorCog(prmValue[4], 4);

      //Riziko adresinin denizden yuksekligi
      prmValue[5] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 5, selDenizYukseklik, skorRequest.getKey());
      if (prmValue[5] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[5] = prmValue[5] * agirlikArray[5];
      skorResult.setSkorCog(prmValue[5], 5);

      //Alt Yapi varligi
      prmValue[6] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 6, selAltYapi, skorRequest.getKey());
      if (prmValue[6] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[6] = prmValue[6] * agirlikArray[6];
      skorResult.setSkorCog(prmValue[6], 6);

      //Hasar Ge�mi�i
      prmValue[7] = 0;
      if (prmValue[7] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[7] = prmValue[7] * agirlikArray[7];
      skorResult.setSkorCog(prmValue[7], 7);

      for (int ii = 1; ii <= 20; ii++) {
        prmValue[ii + 7] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 7, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
        if (prmValue[ii + 7] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
          return skorResult;
        }
        prmValue[ii + 7] = prmValue[ii + 7] * agirlikArray[ii + 7];
        skorResult.setSkorBeyan(prmValue[ii + 7], ii);
      }

      double selSkor = 0.0;
      for (int ii = 1; ii <= 27; ii++) {
        selSkor += prmValue[ii];
      }
      
      skorResult.setSkorValue(selSkor);

      if (tempIlceId > 0) {
        skorResult.setResultCode(RESULT_SUCCESS);
        skorResult = checkRiskScore(skorResult);
      }

      //return skorResult;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        DbConn.closeDBConnection(pstmt, rset);
        DbConn.closeConnection(cnn);
    }

    return skorResult;
  } //getSelSkor

  public SkorResultInfo getHirsizlikSkor(SkorRequestInfo skorRequest) {
    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();
    skorResult.setCogParamLength(4);

    Connection cnn = DbConn.getPooledConnection();
    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));
    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }
    /*
        if (skorRequest.getBeyan1() == 0 || skorRequest.getBeyan2() == 0 || skorRequest.getBeyan3() == 0 ||
            skorRequest.getBeyan4() == 0 || skorRequest.getBeyan5() == 0 || skorRequest.getBeyan6() == 0 ||
            skorRequest.getBeyan7() == 0) {
            skorResult.setResultCode(RESULT_BEYAN_VALUE_GT_ZERO);
            return skorResult;
        }*/

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    long tempIlceId = 0;

    try {

      String hirsizKarakolMesafe = "Veri Yok";
      String hirsizIsMerkezi = "Veri Yok";
      String hirsizSanayiSitesi = "Veri Yok";
      String hirsizGuvenlikKontrol = "Veri Yok";

      //koordinata ait ilce var mi ?
      tempIlceId = getIlceId(cnn, skorRequest);
      if (tempIlceId == 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
        //return skorResult;
      }

      if (gAdresSeviye == 6 && tempIlceId > 0) {
        //bulunan ilceye ait kapi var mi ?
        sql = "SELECT COUNT(*) AS CNT FROM RISK_KAPI WHERE ILCE_ID=" + tempIlceId;
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
        rset.next();
        //KAPI tablosunda ilceye ait data var ise kullanilir, eger yok ise IDARI_SINIR_YOL tablosundaki risk donulur.
        if (rset.getLong("CNT") > 0) {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "SELECT HIRSIZ_KARAKOL_MESAFE, HIRSIZ_IS_MERKEZI, HIRSIZ_SANAYI_SITESI, HIRSIZ_GUVENLIK_KONTROL ";
          sql += getSqlPart(skorRequest);
          sql += " AND ILCE_ID = " + tempIlceId;
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
        } else {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "SELECT HIRSIZ_KARAKOL_MESAFE, HIRSIZ_IS_MERKEZI, HIRSIZ_SANAYI_SITESI, HIRSIZ_GUVENLIK_KONTROL ";
          sql += getSqlPart2(skorRequest);
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
          //                    if (!rset.next()) {
          //                        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
          //                        return skorResult;
          //                    }
        }
      } else if (tempIlceId > 0 ) {
        DbConn.closeDBConnection(pstmt, rset);
        sql = "SELECT HIRSIZ_KARAKOL_MESAFE, HIRSIZ_IS_MERKEZI, HIRSIZ_SANAYI_SITESI, HIRSIZ_GUVENLIK_KONTROL ";
        sql += getSqlPart(skorRequest);
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
      }
      if (rset != null && rset.next()) {
        hirsizKarakolMesafe = rset.getString("HIRSIZ_KARAKOL_MESAFE");
        hirsizIsMerkezi = rset.getString("HIRSIZ_IS_MERKEZI");
        hirsizSanayiSitesi = rset.getString("HIRSIZ_SANAYI_SITESI");
        hirsizGuvenlikKontrol = rset.getString("HIRSIZ_GUVENLIK_KONTROL");
      }

      //Risk Agirliklari
      double[] agirlikArray = new double[31];
      agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());


      //Riziko adresinin karakola mesafesi
      prmValue[1] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 1, hirsizKarakolMesafe, skorRequest.getKey());
      if (prmValue[1] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[1] = prmValue[1] * agirlikArray[1];
      skorResult.setSkorCog(prmValue[1], 1);

      //Riziko adresinin is hani veya is merkezi icinde olmasi
      prmValue[2] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 2, hirsizIsMerkezi, skorRequest.getKey());
      if (prmValue[2] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[2] = prmValue[2] * agirlikArray[2];
      skorResult.setSkorCog(prmValue[2], 2);

      //Riziko adresinin sanayi sitesi icinde olmasi
      prmValue[3] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 3, hirsizSanayiSitesi, skorRequest.getKey());
      if (prmValue[3] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[3] = prmValue[3] * agirlikArray[3];
      skorResult.setSkorCog(prmValue[3], 3);

      //Riziko adresinin guvenlik kontrolune yakin olmasi
      prmValue[4] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 4, hirsizGuvenlikKontrol, skorRequest.getKey());
      if (prmValue[4] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[4] = prmValue[4] * agirlikArray[4];
      skorResult.setSkorCog(prmValue[4], 4);

      //Hasar Ge�mi�i
      prmValue[5] = 0;
      if (prmValue[5] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[5] = prmValue[5] * agirlikArray[5];
      skorResult.setSkorCog(prmValue[5], 5);

      for (int ii = 1; ii <= 20; ii++) {
        prmValue[ii + 5] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 5, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
        if (prmValue[ii + 5] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
          return skorResult;
        }
        prmValue[ii + 5] = prmValue[ii + 5] * agirlikArray[ii + 5];
        skorResult.setSkorBeyan(prmValue[ii + 5], ii);
      }

      double hirsizSkor = 0.0;
      for (int ii = 1; ii <= 25; ii++) {
        hirsizSkor += prmValue[ii];
      }
      
      skorResult.setSkorValue(hirsizSkor);

      if (tempIlceId > 0) {
        skorResult.setResultCode(RESULT_SUCCESS);
        skorResult = checkRiskScore(skorResult);
      }

      //return skorResult;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        DbConn.closeDBConnection(pstmt, rset);
        DbConn.closeConnection(cnn);
    }

    return skorResult;
  } //getHirsizlikSkor


  public SkorResultInfo getTerorSkor(SkorRequestInfo skorRequest) {
    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();

    Connection cnn = DbConn.getPooledConnection();
    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));
    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    
    long tempIlceId = 0;

    try {
      String terorRiskNokta = "Veri Yok";
      String terorHalkNokta = "Veri Yok";
      
      //koordinata ait ilce var mi ?
      tempIlceId = getIlceId(cnn, skorRequest);
      if( tempIlceId == 0 ) {
        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
        //return skorResult;
      }
      
      if (gAdresSeviye == 6 && tempIlceId > 0) {
        //bulunan ilceye ait kapi var mi ?
        sql = "Select COUNT(*) as CNT From RISK_KAPI Where ILCE_ID=" + tempIlceId;
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
        rset.next();
        //KAPI tablosunda ilceye ait data var ise kullanilir, eger yok ise IDARI_SINIR_YOL tablosundaki risk donulur.
        if (rset.getLong("CNT") > 0) {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select TEROR_RISK_NOKTA, TEROR_HALK_NOKTA ";
          sql += getSqlPart(skorRequest);
          sql += " AND ILCE_ID = " + tempIlceId;
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
        } else {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select TEROR_RISK_NOKTA, TEROR_HALK_NOKTA ";
          sql += getSqlPart2(skorRequest);
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
          /*if (!rset.next()) {
                        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
                        return skorResult;
                    }*/
        }
      } else if( tempIlceId > 0 ) {
        DbConn.closeDBConnection(pstmt, rset);
        sql = "Select TEROR_RISK_NOKTA, TEROR_HALK_NOKTA ";
        sql += getSqlPart(skorRequest);
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
      }
      if (rset != null && rset.next()) {
        terorRiskNokta = rset.getString("TEROR_RISK_NOKTA");
        terorHalkNokta = rset.getString("TEROR_HALK_NOKTA");
      }

        //Risk Agirliklari
        double[] agirlikArray = new double[31];
        agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());

        //Teror riski yuksek noktalar
        prmValue[1] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 1, terorRiskNokta, skorRequest.getKey());
        if (prmValue[1] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
          return skorResult;
        }
        prmValue[1] = prmValue[1] * agirlikArray[1];
        skorResult.setSkorCog(prmValue[1], 1);

        //Halk hareketi yuksek noktalar
        prmValue[2] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 2, terorHalkNokta, skorRequest.getKey());
        if (prmValue[2] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
          return skorResult;
        }
        prmValue[2] = prmValue[2] * agirlikArray[2];
        skorResult.setSkorCog(prmValue[2], 2);

        //Hasar Ge�mi�i
        prmValue[3] = 0;
        if (prmValue[3] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
          return skorResult;
        }
        prmValue[3] = prmValue[3] * agirlikArray[3];
        skorResult.setSkorCog(prmValue[3], 3);

        for (int ii = 1; ii <= 20; ii++) {
          prmValue[ii + 3] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 3, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
          if (prmValue[ii + 3] < 0) {
            skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
            return skorResult;
          }
          prmValue[ii + 3] = prmValue[ii + 3] * agirlikArray[ii + 3];
          skorResult.setSkorBeyan(prmValue[ii + 3], ii);
        }

        double terorSkor = 0.0;
        for (int ii = 1; ii <= 23; ii++) {
          terorSkor += prmValue[ii];
        }
      
        skorResult.setSkorValue(terorSkor);
      
        skorResult = checkRiskScore(skorResult);
      
        //return skorResult;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        DbConn.closeDBConnection(pstmt, rset);
        DbConn.closeConnection(cnn);
    }

    return skorResult;
  } //getTerorSkor

  public SkorResultInfo getDepremSkor(SkorRequestInfo skorRequest) {
    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();

    Connection cnn = DbConn.getPooledConnection();

    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));
    
    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }
    
    String depremRiskSinif = "";

    if (skorRequest.getBeyanValue(1) == 0 || skorRequest.getBeyanValue(2) == 0 || skorRequest.getBeyanValue(3) == 0) {
      skorResult.setResultCode(RESULT_BEYAN_VALUE_GT_ZERO);
      depremRiskSinif = "A";
    }
    
    //koordinata ait ilce var mi ?
    long tempIlceId = getIlceId(cnn, skorRequest);
    if( tempIlceId == 0 ) {
      skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
      //return skorResult;
    }

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    try {
      sql = "Select Count(1) as CNT ";
      sql += getSqlPart(skorRequest);
      pstmt = cnn.prepareStatement(sql);
      pstmt.setQueryTimeout(360);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      rset.next();
      long cnt = rset.getLong("CNT");
      DbConn.closeDBConnection(pstmt, rset);
      if (cnt > 0) {
        sql = "Select BOLGE_ID, DEPREM_TEHLIKE ";
        sql += getSqlPart(skorRequest);
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
      } else {
        sql = "Select DEPREM_SISMIK , -1  From RISK_SPECTRAL_GRID WHERE SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL)) = 'TRUE' AND rownum=1";
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
      }
      if (rset.next()) {

        //Risk Agirliklari
        double[] agirlikArray = new double[31];
        agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());

        int bolgeId = rset.getInt("BOLGE_ID");
        String depremTehlike = rset.getString("DEPREM_TEHLIKE");

        //41:Kocaeli ve 59:Tekirdag icin Istanbul sinirlarinda grid oldugundan istanbul gibi islem yapilir.
        if (bolgeId == 41 || bolgeId == 59)
          bolgeId = 34;

        /*Betonarme matris yapisi
           --------------- 1-3 Kat ---- 4-7 Kat ---  >7 Kat
           <= 1975           B           A            A
           1976-1999         B           A            B
           >= 2000           C           C            C  */
        String[][] mtxBetonarme = { { "B", "A", "A" }, { "B", "A", "B" }, { "C", "C", "C" } };


        /*Betonarme disindakilerin matris yapisi
           ---------------<2000 ---- >=2000 ---
           Ahsap             B           C
           Yigma(Kagir)      B           C
           Adi Kagir         A           A
           P.fabrik kiris    A           B
           P.fabrik a.panel  B           C
           Celik             B           C    */
        String[][] mtxDiger = { { "B", "C" }, { "B", "C" }, { "A", "A" }, { "A", "B" }, { "B", "C" }, { "B", "C" } };

        //Bina Tipi
        prmValue[1] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), 1, skorRequest.getBeyanValue(1), skorRequest.getKey());
        if (prmValue[1] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
          return skorResult;
        }
        int prmValue1 = (int) skorRequest.getBeyanValue(1);
        skorResult.setSkorBeyan(prmValue[1] * agirlikArray[1], 1);


        //Risk parametre detay tablosu beyan edilen parametrelerin varligi icin okunur sadece. Agirlik degerleri kullanilmaz.
        //Bina Yapim Yili
        prmValue[2] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), 2, (int) skorRequest.getBeyanValue(2), skorRequest.getKey());
        if (prmValue[2] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
          return skorResult;
        }
        int prmValue2 = (int) skorRequest.getBeyanValue(2);
        skorResult.setSkorBeyan(prmValue[2] * agirlikArray[2], 2);


        //Bina Kat Adedi
        prmValue[3] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), 3, (int) skorRequest.getBeyanValue(3), skorRequest.getKey());
        if (prmValue[3] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
          return skorResult;
        }
        int prmValue3 = (int) skorRequest.getBeyanValue(3);
        skorResult.setSkorBeyan(prmValue[3] * agirlikArray[3], 3);


        //deprem risk sinifi matrislerden bulunur
        if (depremRiskSinif == null || depremRiskSinif.length() == 0 ) { 
          if (prmValue1 == 0)
            depremRiskSinif = "A"; //belirsiz
          else if (prmValue1 == 1)
            depremRiskSinif = mtxBetonarme[prmValue2 - 1][prmValue3 - 1]; //betonarme

          if (prmValue1 > 1) {
            if (prmValue3 > 1) {
              prmValue3--;
            }
            depremRiskSinif = mtxDiger[(prmValue1 - 1) - 1][prmValue3 - 1]; //Bina yapim yili icin betonarmede 3, digerlerinde 2 adet yil parametresi oldugu icin < 2000 olan 1 ve 2 1'e 3 te 2'ye esitlenmis oluyor.
          }
        }

        //Deprem sinifi saptanamadi
        if (!(depremRiskSinif.equals("A") || depremRiskSinif.equals("B") || depremRiskSinif.equals("C"))) {
          skorResult.setResultCode(RESULT_NOTDEFINED_DEPREM_SINIF);
          depremRiskSinif = "A";
          //return skorResult;
        }

        if (bolgeId != 34 && bolgeId != 35)
          bolgeId = -1;
        DbConn.closeDBConnection(pstmt, rset);
        sql = "Select * From RISK_DEPREM_HASAR_ORAN where GURUP_KODU = ? AND BOLGE_ID = ?";
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        pstmt.setString(1, depremRiskSinif);
        pstmt.setInt(2, bolgeId);
        rset = pstmt.executeQuery();
        if (!rset.next()) {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select * From RISK_DEPREM_HASAR_ORAN where GURUP_KODU = ? AND BOLGE_ID = -1";
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          pstmt.setString(1, depremRiskSinif);
          if (!rset.next()) {
            skorResult.setResultCode(RESULT_NOTFOUND_DEPREM_HASAR_ORAN); //DEPREM_HASAR_ORAN tablosunda kayit bulunamdi
            return skorResult;
          }
        }

        //Hasar Ge�mi�i
        prmValue[5] = 0;
        if (prmValue[5] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
          return skorResult;
        }
        prmValue[5] = prmValue[5] * agirlikArray[5];
        skorResult.setSkorCog(prmValue[5], 2);

        for (int ii = 4; ii <= 20; ii++) {
          prmValue[ii + 2] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 2, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
          if (prmValue[ii + 2] < 0) {
            skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
            return skorResult;
          }
          prmValue[ii + 2] = prmValue[ii + 2] * agirlikArray[ii + 2];
          skorResult.setSkorBeyan(prmValue[ii + 2], ii);
        }

        double depremSkor = 0.0;
        if (depremTehlike.equals("5") || depremTehlike.equals("5.5") || depremTehlike.equals("6"))
          depremSkor = rset.getDouble("DHO_ALTI");
        else if (depremTehlike.equals("6.5"))
          depremSkor = rset.getDouble("DHO_ALTIBUCUK");
        else if (depremTehlike.equals("7"))
          depremSkor = rset.getDouble("DHO_YEDI");
        else if (depremTehlike.equals("7.5"))
          depremSkor = rset.getDouble("DHO_YEDIBUCUK");
        else if (depremTehlike.equals("8"))
          depremSkor = rset.getDouble("DHO_SEKIZ");
        else if (depremTehlike.equals("8.5"))
          depremSkor = rset.getDouble("DHO_SEKIZBUCUK");
        else if (depremTehlike.equals("9"))
          depremSkor = rset.getDouble("DHO_DOKUZ");
        else if (depremTehlike.equals("9.5"))
          depremSkor = rset.getDouble("DHO_DOKUZBUCUK");
        else if (depremTehlike.equals("10"))
          depremSkor = rset.getDouble("DHO_ON");
        else if (depremTehlike.equals("10.5"))
          depremSkor = rset.getDouble("DHO_ONBUCUK");
        else {
          skorResult.setResultCode(RESULT_NOTFOUND_DEPREM_DATA); //DEPREM skor hesabi icin veri olmayan bir lokasyon secilmis. Hesaplama yapilamaz.
          return skorResult;
        }

        if (depremSkor <= 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_DHO_SIDDET);
          return skorResult;
        }

        depremSkor = depremSkor * agirlikArray[4];
        skorResult.setSkorCog(depremSkor, 1);

        for (int ii = 5; ii <= 22; ii++) {
          depremSkor += prmValue[ii];
        }
        
        skorResult.setSkorValue(depremSkor);
  
        skorResult = checkRiskScore(skorResult);
        
        //return skorResult;

      } else {
        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
        return skorResult;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        DbConn.closeDBConnection(pstmt, rset);
        DbConn.closeConnection(cnn);
    }

    return skorResult;
  } //getDepremSkor

  public SkorResultInfo getZeminSkor(SkorRequestInfo skorRequest) {
    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();

    Connection cnn = DbConn.getPooledConnection();
    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));

    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    
    long tempIlceId = 0;

    try {
      String zeminAktifHeyelan = "Veri Yok";
      
      //koordinata ait ilce var mi ?
      tempIlceId = getIlceId(cnn, skorRequest);
      if (tempIlceId == 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
        //return skorResult;
      }
      
      if (gAdresSeviye == 6 && tempIlceId > 0) {
        //bulunan ilceye ait kapi var mi ?
        sql = "Select COUNT(*) as CNT From RISK_KAPI Where ILCE_ID=" + tempIlceId;
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
        rset.next();
        //KAPI tablosunda ilceye ait data var ise kullanilir, eger yok ise IDARI_SINIR_YOL tablosundaki risk donulur.
        if (rset.getLong("CNT") > 0) {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select ZEMIN_AKTIF_HEYELAN ";
          sql += getSqlPart(skorRequest);
          sql += " AND ILCE_ID = " + tempIlceId;
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
        } else {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select ZEMIN_AKTIF_HEYELAN ";
          sql += getSqlPart2(skorRequest);
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
          /*if (!rset.next()) {
                        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
                        return skorResult;
                    } */
        }
      } else if ( tempIlceId > 0 ) {
        DbConn.closeDBConnection(pstmt, rset);
        sql = "Select ZEMIN_AKTIF_HEYELAN ";
        sql += getSqlPart(skorRequest);
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
      }
      if (rset != null && rset.next()) {
        zeminAktifHeyelan = rset.getString("ZEMIN_AKTIF_HEYELAN");
      }

        //Risk Agirliklari
        double[] agirlikArray = new double[31];
        agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());

        //Zemin
        prmValue[1] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 1, zeminAktifHeyelan, skorRequest.getKey());
        if (prmValue[1] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
          return skorResult;
        }
        prmValue[1] = prmValue[1] * agirlikArray[1];
        skorResult.setSkorCog(prmValue[1], 1);

        //Hasar Ge�mi�i
        prmValue[2] = 0;
        if (prmValue[2] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
          return skorResult;
        }
        prmValue[2] = prmValue[2] * agirlikArray[2];
        skorResult.setSkorCog(prmValue[2], 2);


        for (int ii = 1; ii <= 20; ii++) {
          prmValue[ii + 2] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 2, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
          if (prmValue[ii + 2] < 0) {
            skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
            return skorResult;
          }
          prmValue[ii + 2] = prmValue[ii + 2] * agirlikArray[ii + 2];
          skorResult.setSkorBeyan(prmValue[ii + 2], ii);
        }

        double zeminSkor = 0;
        for (int ii = 1; ii <= 22; ii++) {
          zeminSkor += prmValue[ii];
        }
      
        skorResult.setSkorValue(zeminSkor);

        //result_desc yazilmasi..
        skorResult = checkRiskScore(skorResult);
        //return skorResult;

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        DbConn.closeDBConnection(pstmt, rset);
        DbConn.closeConnection(cnn);
    }

    return skorResult;
  } //getZeminSkor

  public SkorResultInfo getFirtinaSkor(SkorRequestInfo skorRequest) {
    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();
    
    Connection cnn = DbConn.getPooledConnection();
    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));
    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }
    /*
        if (skorRequest.getBeyan1() == 0) {
            skorResult.setResultCode(RESULT_BEYAN_VALUE_GT_ZERO);
            return skorResult;
        }*/

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    
    long tempIlceId = 0;

    try {
      
      String firtinaCTarsimFirtina = "Veri Yok";
      
      //koordinata ait ilce var mi ?
      tempIlceId = getIlceId(cnn, skorRequest);
      if (tempIlceId == 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
        //return skorResult;
      }
      
      if (gAdresSeviye == 6 && tempIlceId > 0) {
        //bulunan ilceye ait kapi var mi ?
        sql = "Select COUNT(*) as CNT From RISK_KAPI Where ILCE_ID=" + tempIlceId;
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
        rset.next();
        //KAPI tablosunda ilceye ait data var ise kullanilir, eger yok ise IDARI_SINIR_YOL tablosundaki risk donulur.
        if (rset.getLong("CNT") > 0) {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select C_TARSIM_FIRTINA ";
          sql += getSqlPart(skorRequest);
          sql += " AND ILCE_ID = " + tempIlceId;
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
//          if (!rset.next()) {
//            skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
//            return skorResult;
//          }
        } else {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select C_TARSIM_FIRTINA ";
          sql += getSqlPart2(skorRequest);
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
//          if (!rset.next()) {
//            skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
//            return skorResult;
//          }
        }
      } else if (tempIlceId > 0) {
        DbConn.closeDBConnection(pstmt, rset);
        sql = "Select C_TARSIM_FIRTINA ";
        sql += getSqlPart(skorRequest);
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
//        if (!rset.next()) {
//          skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
//          return skorResult;
//        }
      }
      if( rset != null && rset.next() ) {
        firtinaCTarsimFirtina = rset.getString("C_TARSIM_FIRTINA");
      }

      //Risk Agirliklari
      double[] agirlikArray = new double[31];
      agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());


      //Riziko adresinin karakola mesafesi
      prmValue[1] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 1, firtinaCTarsimFirtina, skorRequest.getKey());
      if (prmValue[1] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[1] = prmValue[1] * agirlikArray[1];
      skorResult.setSkorCog(prmValue[1], 1);

      //Hasar Ge�mi�i
      prmValue[2] = 0;
      if (prmValue[2] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[2] = prmValue[2] * agirlikArray[2];
      skorResult.setSkorCog(prmValue[2], 2);


      for (int ii = 1; ii <= 20; ii++) {
        prmValue[ii + 2] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 2, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
        if (prmValue[ii + 2] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
          return skorResult;
        }
        prmValue[ii + 2] = prmValue[ii + 2] * agirlikArray[ii + 2];
        skorResult.setSkorBeyan(prmValue[ii + 2], ii);
      }

      double hirsizSkor = 0.0;
      for (int ii = 1; ii <= 22; ii++) {
        hirsizSkor += prmValue[ii];
      }
      
      skorResult.setSkorValue(hirsizSkor);
      
      // result_desc yazilmasi..
      skorResult = checkRiskScore(skorResult);
      
      //return skorResult;

      //} else {
      //     skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
      //     return skorResult;
      //}
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        DbConn.closeDBConnection(pstmt, rset);
        DbConn.closeConnection(cnn);
    }

    return skorResult;
  } //getFirtinaSkor


  public SkorResultInfo getKarSkor(SkorRequestInfo skorRequest) {
    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();

    long tempIlceId = 0;

    Connection cnn = DbConn.getPooledConnection();

    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));
    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }
    /*
        if (skorRequest.getBeyan1() == 0) {
            skorResult.setResultCode(RESULT_BEYAN_VALUE_GT_ZERO);
            return skorResult;
        }*/

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    try {
      
      String firtinaCTarsimFirtina = "Veri Yok";
      
      //koordinata ait ilce var mi ?
      tempIlceId = getIlceId(cnn, skorRequest);
      if (tempIlceId == 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
        //return skorResult;
      }
      
      if (gAdresSeviye == 6 && tempIlceId > 0) {
        //bulunan ilceye ait kapi var mi ?
        sql = "Select COUNT(*) as CNT From RISK_KAPI Where ILCE_ID=" + tempIlceId;
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
        rset.next();
        //KAPI tablosunda ilceye ait data var ise kullanilir, eger yok ise IDARI_SINIR_YOL tablosundaki risk donulur.
        if (rset.getLong("CNT") > 0) {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select C_TARSIM_FIRTINA ";
          sql += getSqlPart(skorRequest);
          sql += " AND ILCE_ID = " + tempIlceId;
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
//          if (!rset.next()) {
//            skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
//            return skorResult;
//          }
        } else {
          DbConn.closeDBConnection(pstmt, rset);
          sql = "Select C_TARSIM_FIRTINA ";
          sql += getSqlPart2(skorRequest);
          pstmt = cnn.prepareStatement(sql);
          pstmt.setQueryTimeout(360);
          pstmt.clearParameters();
          rset = pstmt.executeQuery();
//          if (!rset.next()) {
//            skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
//            return skorResult;
//          }
        }
      } else if ( tempIlceId > 0 ) {
        DbConn.closeDBConnection(pstmt, rset);
        sql = "Select C_TARSIM_FIRTINA ";
        sql += getSqlPart(skorRequest);
        pstmt = cnn.prepareStatement(sql);
        pstmt.setQueryTimeout(360);
        pstmt.clearParameters();
        rset = pstmt.executeQuery();
//        if (!rset.next()) {
//          skorResult.setResultCode(RESULT_NOTFOUND_GEO_RECORD);
//          return skorResult;
//        }
      }
      if( rset.next() ) {
        firtinaCTarsimFirtina = rset.getString("C_TARSIM_FIRTINA");
      }

      //Risk Agirliklari
      double[] agirlikArray = new double[31];
      agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());

      prmValue[1] = getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 1, firtinaCTarsimFirtina, skorRequest.getKey());
      if (prmValue[1] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[1] = prmValue[1] * agirlikArray[1];
      skorResult.setSkorCog(prmValue[1], 1);

      //Hasar Ge�mi�i
      prmValue[2] = 0;
      if (prmValue[2] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[2] = prmValue[2] * agirlikArray[2];
      skorResult.setSkorCog(prmValue[2], 2);

      for (int ii = 1; ii <= 20; ii++) {
        prmValue[ii + 2] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 2, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
        if (prmValue[ii + 2] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
          return skorResult;
        }
        prmValue[ii + 2] = prmValue[ii + 2] * agirlikArray[ii + 2];
        skorResult.setSkorBeyan(prmValue[ii + 2], ii);
      }

      double karSkor = 0.0;
      for (int ii = 1; ii <= 22; ii++) {
        karSkor += prmValue[ii];
      }
      
      skorResult.setSkorValue(karSkor);

      // result_desc yazilmasi..
      skorResult = checkRiskScore(skorResult);

      //return skorResult;

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        DbConn.closeDBConnection(pstmt, rset);
        DbConn.closeConnection(cnn);
    }
    return skorResult;
  } //getKarSkor

  public SkorResultInfo getDoluSkor(SkorRequestInfo skorRequest) {

    double prmValue[] = new double[31];
    SkorResultInfo skorResult = new SkorResultInfo();

    long tempIlceId = 0;

    Connection cnn = DbConn.getPooledConnection();

    skorResult.setCogParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 0));
    skorResult.setBeyanParamLength(getParamCount(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey(), 1));
    if (cnn == null) {
      skorResult.setResultCode(RESULT_DB_NOT_CONNECTED);
      return skorResult;
    }
    /*
        if (skorRequest.getBeyan1() == 0) {
            skorResult.setResultCode(RESULT_BEYAN_VALUE_GT_ZERO);
            return skorResult;
        }*/

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    try {

      //Risk Agirliklari
      double[] agirlikArray = new double[31];
      agirlikArray = getRiskAgirlik(cnn, skorRequest.getTeminatTuru(), skorRequest.getKey());

      //Hasar Ge�mi�i
      prmValue[1] = 0;
      if (prmValue[1] < 0) {
        skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYSINIF);
        return skorResult;
      }
      prmValue[1] = prmValue[1] * agirlikArray[1];
      skorResult.setSkorCog(prmValue[1], 1);

      for (int ii = 1; ii <= 20; ii++) {
        prmValue[ii + 1] = getParametreDetay(cnn, skorRequest.getTeminatTuru(), ii + 1, (int) skorRequest.getBeyanValue(ii), skorRequest.getKey());
        if (prmValue[ii + 1] < 0) {
          skorResult.setResultCode(RESULT_NOTFOUND_PRMDETAYID);
          return skorResult;
        }
        prmValue[ii + 1] = prmValue[ii + 1] * agirlikArray[ii + 1];
        skorResult.setSkorBeyan(prmValue[ii + 1], ii);
      }


      double doluSkor = 0.0;
      for (int ii = 1; ii <= 21; ii++) {
        doluSkor += prmValue[ii];
      }
      
      skorResult.setSkorValue(doluSkor);

      // result_desc yazilmasi..
      skorResult = checkRiskScore(skorResult);

      //return skorResult;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        DbConn.closeDBConnection(pstmt, rset);
        DbConn.closeConnection(cnn);
    }

    return skorResult;
  } //getDoluSkor


  //                     getParametreDetay(cnn, skorRequest.getTeminatTuru(), 9, (int) skorRequest.getBeyan1(),skorRequest.getKey());


  //                     getParametreDetay(cnn, skorRequest.getTeminatTuru(), 1, (int) skorRequest.getBeyan1(),skorRequest.getKey());

  //RiskDetayId ile parametre okuma ------------------------------------------------------------
  public double getParametreDetay(Connection cnn, int teminatId, int riskId, int riskDetayId, String key) {
    //double retValue = -5.00;
    double retValue = 0.00;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    try {
      sql = "Select VALUE from RISK_PARAMETRE_DETAY Where TEMINAT_ID = ? And RISK_ID = ? And RISK_DETAY_ID = ? And KEY = ?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.setQueryTimeout(360);
      pstmt.clearParameters();
      pstmt.setInt(1, teminatId);
      pstmt.setInt(2, riskId);
      pstmt.setInt(3, riskDetayId);
      pstmt.setString(4, key);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        retValue = rset.getDouble("VALUE");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DbConn.closeDBConnection(pstmt, rset);
    }
    return retValue;
  } //getParametreDetay

  //getParametreDetaySinif(cnn, skorRequest.getTeminatTuru(), 1, yanginCikma, skorRequest.getKey());


  //RiskDetay Sinifi ile parametre okuma ------------------------------------------------------------
  public double getParametreDetaySinif(Connection cnn, int teminatId, int riskId, String sinif, String key) {
    //double retValue = -7.00;
    double retValue = 0.00;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    try {
      sql = "Select VALUE from RISK_PARAMETRE_DETAY Where TEMINAT_ID = ? And RISK_ID = ? And SINIF = ? And KEY = ?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.setQueryTimeout(360);
      pstmt.clearParameters();
      pstmt.setInt(1, teminatId);
      pstmt.setInt(2, riskId);
      pstmt.setString(3, sinif);
      pstmt.setString(4, key);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        retValue = rset.getDouble("VALUE");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
       DbConn.closeDBConnection(pstmt, rset);
    }

    return retValue;
  } //getParametreDetaySinif

  //Risk parametre aagirlik oranlari ------------------------------------------------------------
  public double[] getRiskAgirlik(Connection cnn, int teminatId, String key) {
    double[] retArrayValue = new double[31];

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    try {
      sql = "Select RISK_ID,AGIRLIK from RISK_PARAMETRE Where TEMINAT_ID = ? AND KEY = ? ORDER BY RISK_ID";
      pstmt = cnn.prepareStatement(sql);
      pstmt.setQueryTimeout(360);
      pstmt.clearParameters();
      pstmt.setInt(1, teminatId);
      pstmt.setString(2, key);
      rset = pstmt.executeQuery();
      while (rset.next()) {
        int riskId = rset.getInt("RISK_ID");
        double agirlik = rset.getDouble("AGIRLIK");
        retArrayValue[riskId] = agirlik;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DbConn.closeDBConnection(pstmt, rset);
    }

    return retArrayValue;
  } // getRiskAgirlik

  //Risk sqlleri adres seviyesine gore olusturulan sql parcalari ------------------------------------------------------------
  public String getSqlPart(SkorRequestInfo skorRequest) {
    String sqlPart = null;
    switch (skorRequest.getAdresSeviye()) {
      case 3:
        sqlPart =
          //                "From RISK_MAHALLE WHERE SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" +
          //                skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL)) = 'TRUE' AND rownum=1";
          "From RISK_MAHALLE WHERE SDO_NN(geoloc, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
      case 4:
        sqlPart = "From RISK_KOY Where SDO_NN(geoloc, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
      case 5:
        sqlPart = "From RISK_IDARI_SINIR_YOL Where SDO_NN(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
      case 6:
        sqlPart = "From RISK_KAPI Where SDO_NN(geoloc, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
      case 7:
      case 8:
        sqlPart = "From RISK_POI_RISK_KARNE Where SDO_NN(geoloc, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
    }

    return sqlPart;
  } //getSqlPart

  public String getSqlPart2(SkorRequestInfo skorRequest) {
    String sqlPart = null;
    switch (skorRequest.getAdresSeviye()) {
      case 3:
        sqlPart = "From RISK_MAHALLE WHERE SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL)) = 'TRUE' AND rownum=1";
        break;
      case 4:
        sqlPart = "From RISK_KOY Where SDO_NN(geoloc, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
      case 5:
        sqlPart = "From RISK_IDARI_SINIR_YOL Where SDO_NN(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
      case 6:
        sqlPart = "From RISK_IDARI_SINIR_YOL Where SDO_NN(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
      case 7:
      case 8:
        sqlPart = "From RISK_POI_RISK_KARNE Where SDO_NN(geoloc, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE' AND rownum=1";
        break;
    }
    return sqlPart;
  } //getSqlPart2

  public int getParamCount(Connection cnn, int teminatTuru, String key, int beyan) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int count = 0;
    try {
      sql = "select count(RISK_ID) as cnt from RISK_PARAMETRE where teminat_id=? and key=? and beyan = ?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.setQueryTimeout(360);
      pstmt.clearParameters();
      pstmt.setInt(1, teminatTuru);
      pstmt.setString(2, key);
      pstmt.setInt(3, beyan);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        count = rset.getInt("cnt");
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DbConn.closeDBConnection(pstmt, rset);
    }
    return count;
  } // getParamCount

  public long getIlceId(Connection cnn, SkorRequestInfo skorRequest) {

    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    long ilceId = 0;

    try {
      sql = "SELECT ILCE_ID From RISK_ILCE WHERE SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(" + skorRequest.getXCoor() + "," + skorRequest.getYCoor() + ",NULL), NULL, NULL)) = 'TRUE' AND rownum=1";
      pstmt = cnn.prepareStatement(sql);
      pstmt.setQueryTimeout(360);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      if (rset.next()) {
        ilceId = rset.getLong("ILCE_ID");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DbConn.closeDBConnection(pstmt, rset);
    }

    return ilceId;
  } //getIlceId
  
  public SkorResultInfo checkRiskScore(SkorResultInfo res) {
    if( res.getSkorValue() > 100 || res.getSkorValue() < 0 )  res.setResultCode(RESULT_INVALID_RISK_SCORE);
    
    return res;
  } // checkRiskScore
  
  public static String getResponseDesc(int responseCode) {
    String responseDesc = "";
    
    switch (responseCode) {
      case RESULT_SUCCESS:
        responseDesc = "";
        break;
      case RESULT_BEYAN_VALUE_GT_ZERO:
        responseDesc = "Must be defined required Beyan parameter(s).";
        break;
      case RESULT_NOTFOUND_GEO_RECORD:
        responseDesc = "Given coordinates does not seem appropriate.(Out of boundary)";
        break;
      case RESULT_NOTFOUND_DHO_SIDDET:
        responseDesc = "Earthquake of magnitude could not be determined.";
        break;
      case RESULT_NOTDEFINED_DEPREM_SINIF:
        responseDesc = "Earthquake class could not be determined.";
        break;
      case RESULT_NOTFOUND_DEPREM_HASAR_ORAN:
        responseDesc = "Earthquake damage rate could not be determined.";
        break;
      case RESULT_NOTFOUND_DEPREM_DATA:
        responseDesc = "Given coordinates does not seem appropriate for earthquake score. Calculations can not be done.";
        break;
      case RESULT_INVALID_COORDINATE:
        responseDesc = "Given incorrect coordinates.";
        break;
      case RESULT_INVALID_ADDR_LEVEL:
        responseDesc = "Given incorrect address level.";
        break;
      case RESULT_INVALID_TEMINAT:
        responseDesc = "Given incorrect teminat id.";
        break;
      case RESULT_INVALID_RISK_SCORE:
        responseDesc = "Calculated risk score outside the upper or lower limit. Check Risk Report.";
        break;
      default:
        responseDesc = "";
        break;
    }
    
    return responseDesc;
  } // getResponseDesc
  
}
