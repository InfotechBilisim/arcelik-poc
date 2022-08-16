package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataDemographic {
    private int nufus = 0;

    private int konut = 0;
    private int yazlik = 0;

    private int oyBilenErkek = 0;
    private int oyBilenKadin = 0;
    private int oyBilmeyenErkek = 0;
    private int oyBilmeyenKadin = 0;
    private int oyBilinmeyenErkek = 0;
    private int oyBilinmeyenKadin = 0;

    private int egitimBilinmeyenErkek = 0;
    private int egitimBilinmeyenKadin = 0;
    private int egitimIlkokulErkek = 0;
    private int egitimIlkokulKadin = 0;
    private int egitimIlkOgretimErkek = 0;
    private int egitimIlkOgretimKadin = 0;
    private int egitimLiseErkek = 0;
    private int egitimLiseKadin = 0;
    private int egitimOrtaokulErkek = 0;
    private int egitimOrtaokulKadin = 0;
    private int egitimYuksekOgrenimErkek = 0;
    private int egitimYuksekOgrenimKadin = 0;

    private int yasGrup_0_20 = 0;
    private int yasGrup_20_30 = 0;
    private int yasGrup_30_50 = 0;
    private int yasGrup_50_65 = 0;
    private int yasGrup_65 = 0;

    private int kamyon = 0;
    private int kamyonet = 0;
    private int minibus = 0;
    private int motorsiklet = 0;
    private int otobus = 0;
    private int otomobil = 0;
    private int ozelAmacli = 0;
    private int traktor = 0;

    private String infoType = null;

    public static final String DEMOGRAPHIC_NUFUS = "NUFUS";
    public static final String DEMOGRAPHIC_OKUMAYAZMA = "OKUMAYAZMA";
    public static final String DEMOGRAPHIC_KONUT = "KONUT";
    public static final String DEMOGRAPHIC_EGITIM = "EGITIM";
    public static final String DEMOGRAPHIC_YASGRUP = "YASGRUPLARI";
    public static final String DEMOGRAPHIC_ARAC = "ARAC";

    public DataDemographic() {
    }

    //-----------------------------------------------------------------------------

    public static DataDemographic getInstance(ResultSet rset, String infoType) {
        DataDemographic dd = new DataDemographic();
        try {
            dd.infoType = infoType;
            if (infoType.equalsIgnoreCase(DEMOGRAPHIC_NUFUS)) {
                dd.nufus = rset.getInt(1);
            } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_OKUMAYAZMA)) {
                dd.oyBilenErkek = rset.getInt(1);
                dd.oyBilenKadin = rset.getInt(2);
                dd.oyBilmeyenErkek = rset.getInt(3);
                dd.oyBilmeyenKadin = rset.getInt(4);
                dd.oyBilinmeyenErkek = rset.getInt(5);
                dd.oyBilinmeyenKadin = rset.getInt(6);
            } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_KONUT)) {
                dd.konut = rset.getInt(1);
                dd.yazlik = rset.getInt(2);
            } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_EGITIM)) {
                dd.egitimBilinmeyenErkek = rset.getInt(1);
                dd.egitimBilinmeyenKadin = rset.getInt(2);
                dd.egitimIlkokulErkek = rset.getInt(3);
                dd.egitimIlkokulKadin = rset.getInt(4);
                dd.egitimIlkOgretimErkek = rset.getInt(5);
                dd.egitimIlkOgretimKadin = rset.getInt(6);
                dd.egitimLiseErkek = rset.getInt(7);
                dd.egitimLiseKadin = rset.getInt(8);
                dd.egitimOrtaokulErkek = rset.getInt(9);
                dd.egitimOrtaokulKadin = rset.getInt(10);
                dd.egitimYuksekOgrenimErkek = rset.getInt(11);
                dd.egitimYuksekOgrenimKadin = rset.getInt(12);
            } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_YASGRUP)) {
                dd.yasGrup_0_20 = rset.getInt(1);
                dd.yasGrup_20_30 = rset.getInt(2);
                dd.yasGrup_30_50 = rset.getInt(3);
                dd.yasGrup_50_65 = rset.getInt(4);
                dd.yasGrup_65 = rset.getInt(5);
            } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_ARAC)) {
                dd.kamyon = rset.getInt(1);
                dd.kamyonet = rset.getInt(2);
                dd.minibus = rset.getInt(3);
                dd.motorsiklet = rset.getInt(4);
                dd.otobus = rset.getInt(5);
                dd.otomobil = rset.getInt(6);
                dd.ozelAmacli = rset.getInt(7);
                dd.traktor = rset.getInt(8);
            }

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
        if (infoType.equalsIgnoreCase(DEMOGRAPHIC_NUFUS)) {
            s += indent + "  \"nufus\": " + nufus + "\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_OKUMAYAZMA)) {
            s +=
                indent + "  \"okumayazma_bilen\": { \"erkek\": " + oyBilenErkek + ", \"kadin\": " + oyBilenKadin +
                " },\n";
            s +=
                indent + "  \"okumayazma_bilmeyen\": { \"erkek\": " + oyBilmeyenErkek + ", \"kadin\": " +
                oyBilmeyenKadin + " },\n";
            s +=
                indent + "  \"okumayazma_bilinmeyen\": { \"erkek\": " + oyBilinmeyenErkek + ", \"kadin\": " +
                oyBilinmeyenKadin + " }\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_KONUT)) {
            s += indent + "  \"konut\": " + konut + ",\n";
            s += indent + "  \"yazlik\": " + yazlik + "\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_EGITIM)) {
            s +=
                indent + "  \"egitim_bilinmeyen\": { \"erkek\": " + egitimBilinmeyenErkek + ", \"kadin\": " +
                egitimBilinmeyenKadin + " },\n";
            s +=
                indent + "  \"egitim_ilkokul\": { \"erkek\": " + egitimIlkokulErkek + ", \"kadin\": " +
                egitimIlkokulKadin + " },\n";
            s +=
                indent + "  \"egitim_ilkogretim\": { \"erkek\": " + egitimIlkOgretimErkek + ", \"kadin\": " +
                egitimIlkOgretimKadin + " },\n";
            s +=
                indent + "  \"egitim_lise\": { \"erkek\": " + egitimLiseErkek + ", \"kadin\": " + egitimLiseKadin +
                " },\n";
            s +=
                indent + "  \"egitim_ortaokul\": { \"erkek\": " + egitimOrtaokulErkek + ", \"kadin\": " +
                egitimOrtaokulKadin + " },\n";
            s +=
                indent + "  \"egitim_yuksekogrenim\": { \"erkek\": " + egitimYuksekOgrenimErkek + ", \"kadin\": " +
                egitimYuksekOgrenimKadin + " }\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_YASGRUP)) {
            s += indent + "  \"yasgrup_0_30\": " + yasGrup_0_20 + ",\n";
            s += indent + "  \"yasgrup_20_30\": " + yasGrup_20_30 + ",\n";
            s += indent + "  \"yasgrup_30_50\": " + yasGrup_30_50 + ",\n";
            s += indent + "  \"yasgrup_50_65\": " + yasGrup_50_65 + ",\n";
            s += indent + "  \"yasgrup_65_x\": " + yasGrup_65 + "\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_ARAC)) {
            s += indent + "  \"kamyon\": " + kamyon + ",\n";
            s += indent + "  \"kamyonet\": " + kamyonet + ",\n";
            s += indent + "  \"minibus\": " + minibus + ",\n";
            s += indent + "  \"motorsiklet\": " + motorsiklet + ",\n";
            s += indent + "  \"otobus\": " + otobus + ",\n";
            s += indent + "  \"otomobil\": " + otomobil + ",\n";
            s += indent + "  \"ozelamacli\": " + ozelAmacli + ",\n";
            s += indent + "  \"traktor\": " + traktor + "\n";
        }
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <demographic>\n";
        if (infoType.equalsIgnoreCase(DEMOGRAPHIC_NUFUS)) {
            s += "      <nufus>" + nufus + "</nufus>\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_OKUMAYAZMA)) {
            s +=
                "      <okumayazma_bilen><erkek>" + oyBilenErkek + "</erkek><kadin>" + oyBilenKadin +
                "</kadin></okumayazma_bilen>\n";
            s +=
                "      <okumayazma_bilmeyen><erkek>" + oyBilmeyenErkek + "</erkek><kadin>" + oyBilmeyenKadin +
                "</kadin></okumayazma_bilmeyen>\n";
            s +=
                "      <okumayazma_bilinmeyen><erkek>" + oyBilinmeyenErkek + "</erkek><kadin>" + oyBilinmeyenKadin +
                "</kadin></okumayazma_bilinmeyen>\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_KONUT)) {
            s += "      <konut>" + konut + "</konut>\n";
            s += "      <yazlik>" + yazlik + "</yazlik>\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_EGITIM)) {
            s +=
                "      <egitim_bilinmeyen><erkek>" + egitimBilinmeyenErkek + "</erkek><kadin>" + egitimBilinmeyenKadin +
                "</kadin></egitim_bilinmeyen>\n";
            s +=
                "      <egitim_ilkokul><erkek>" + egitimIlkokulErkek + "</erkek><kadin>" + egitimIlkokulKadin +
                "</kadin></egitim_ilkokul>\n";
            s +=
                "      <egitim_ilkogretim><erkek>" + egitimIlkOgretimErkek + "</erkek><kadin>" + egitimIlkOgretimKadin +
                "</kadin></egitim_ilkogretim>\n";
            s +=
                "      <egitim_lise><erkek>" + egitimLiseErkek + "</erkek><kadin>" + egitimLiseKadin +
                "</kadin></egitim_lise>\n";
            s +=
                "      <egitim_ortaokul><erkek>" + egitimOrtaokulErkek + "</erkek><kadin>" + egitimOrtaokulKadin +
                "</kadin></egitim_ortaokul>\n";
            s +=
                "      <egitim_yuksekogrenim><erkek>" + egitimYuksekOgrenimErkek + "</erkek><kadin>" +
                egitimYuksekOgrenimKadin + "</kadin></egitim_yuksekogrenim>\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_YASGRUP)) {
            s += "      <yasgrup_0_20>" + yasGrup_0_20 + "</yasgrup_0_20>\n";
            s += "      <yasgrup_20_30>" + yasGrup_20_30 + "</yasgrup_20_30>\n";
            s += "      <yasgrup_30_50>" + yasGrup_30_50 + "</yasgrup_30_50>\n";
            s += "      <yasgrup_50_65>" + yasGrup_50_65 + "</yasgrup_50_65>\n";
            s += "      <yasgrup_65_x>" + yasGrup_65 + "</yasgrup_65_x>\n";
        } else if (infoType.equalsIgnoreCase(DEMOGRAPHIC_ARAC)) {
            s += "      <kamyon>" + kamyon + "</kamyon>\n";
            s += "      <kamyonet>" + kamyonet + "</kamyonet>\n";
            s += "      <minibus>" + minibus + "</minibus>\n";
            s += "      <motorsiklet>" + motorsiklet + "</motorsiklet>\n";
            s += "      <otobus>" + otobus + "</otobus>\n";
            s += "      <otomobil>" + otomobil + "</otomobil>\n";
            s += "      <ozelamacli>" + ozelAmacli + "</ozelamacli>\n";
            s += "      <traktor>" + traktor + "</traktor>\n";
        }
        s += "    </demographic>\n";
        return s;
    } // toXml()

}
