
ILayer.MAP_BOLGE="MAP_BOLGE";
ILayer.MAP_IL="MAP_IL";
ILayer.MAP_ILCE="MAP_ILCE";
ILayer.MAP_MAHALLE="MAP_MAHALLE";
ILayer.USER_POINT="USER_POINT";
ILayer.USER_REGION="USER_REGION";
ILayer.CAMP_CATEGORY="CAMP_CATEGORY";
ILayer.CAMP_CAMPAIGN="CAMP_CAMPAIGN";
ILayer.DEAL_CATEGORY="DEAL_CATEGORY";
ILayer.DEAL_DEAL="DEAL_DEAL";
ILayer.PREDEFINED_THEME="PREDEFINED THEME";
ILayer.MAP_ROUTE="MAP_ROUTE";
ILayer.MAP_YOL="MAP_YOL";
ILayer.HEATMAP_ULKE="HEATMAP_ULKE";
ILayer.HEATMAP_IL="HEATMAP_IL";
ILayer.HEATMAP_ILCE="HEATMAP_ILCE";
ILayer.HEATMAP_MAHALLE="HEATMAP_MAHALLE";

ILayer.DEMOGRAFIK_YAS_DURUMU="YAS_DURUMU";
ILayer.DEMOGRAFIK_NUFUS="NUFUS";
ILayer.DEMOGRAFIK_EGITIM="EGITIM";
ILayer.DEMOGRAFIK_KONUT_SAYISI="KONUT_SAYISI";
ILayer.DEMOGRAFIK_ARAC_SAYISI="ARAC_SAYISI";

function ILayer(mapper) {
  if( !mapper ) {
    this.mapview = mapviewGlobal;
  }
  else {
    this.mapview = mapper.mapview;
  }
  this.typ = null;
  this.thmtyp = null;
  this.id = null;
  this.keyColumn = null;
  this.styleColumn = null;
  this.labelColumn = null;
  this.labelStyle = null;
  this.tableName = null;
  this.where = null;
  this.thmLayer = null;
}

//----------------------------------------------------------------------------

ILayer.prototype.createLayer=function(typ, id, where, symc, sym, visible, mouseclick, lblc, lsym, hcols) {
  this.typ = typ;
  this.id = id;
  this.where = where;
  this.labelColumn = lblc;
  this.labelStyle = lsym;
  if( !this.labelStyle ) this.labelStyle = "T.LABEL";
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;
  
  switch( typ ) {
  case ILayer.MAP_BOLGE :
    this.keyColumn = "BOLGE_ID";
    this.styleColumn = "BOLGE_ID";
    if( symc ) this.styleColumn = symc;
    this.tableName = "BOLGE";
    this.hiddenColumn = "BOLGE_ADI";
    this.hiddenInfo = "<field column='BOLGE_ADI' name='BOLGE' />";
    break;
  case ILayer.MAP_IL :
    this.keyColumn = "IL_ID";
    this.styleColumn = "IL_ID";
    if( symc ) this.styleColumn = symc;
    this.tableName = "IL";
    if( !this.where || this.where.length <= 0 ) this.where = "IL_ID <> 100";
    else
      this.where = "IL_ID <> 100 AND " + this.where;
    this.hiddenColumn = "IL_ADI";
    this.hiddenInfo = "<field column='IL_ADI' name='IL' />";
    break;
  case ILayer.MAP_ILCE :
    this.keyColumn = "ILCE_ID";
    this.styleColumn = "ILCE_ID";
    if( symc ) this.styleColumn = symc;
    this.tableName = "ILCE";
    this.hiddenColumn = "ILCE_ADI";
    this.hiddenInfo = "<field column='ILCE_ADI' name='ILCE' />";
    break;
  case ILayer.MAP_MAHALLE :
    this.keyColumn = "MAHALLE_ID";
    this.styleColumn = "MAHALLE_ID";
    if( symc ) this.styleColumn = symc;
    this.tableName = "MAHALLE";
    if( !this.where || this.where.length <= 0 ) this.where = "TYPE=3";
    else
      this.where = this.where + " AND TYPE=3";
    this.hiddenColumn = "MAHALLE_ADI";
    this.hiddenInfo = "<field column='MAHALLE_ADI' name='MAHALLE' />";
    break;
  case ILayer.USER_POINT :
    this.keyColumn = "POINT_ID";
    this.styleColumn = "TYP";
    if( symc ) this.styleColumn = symc;
    this.tableName = "LBS_USER_POINT";
    if( !this.where || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
    else
      this.where = "KEY='" + lboxKey + "' AND " + this.where;
    if( !sym ) sym = "M.RED_PIN";
    this.hiddenColumn = "POINT_NAME";
    this.hiddenInfo = "<field column='POINT_NAME' name='NAME' />";
    if( hcols ) {
      for( var i = 0; i < hcols.length; i++ ) {
        this.hiddenColumn += "," + hcols[i];
        this.hiddenInfo += "<field column='" + hcols[i] + "' name='" + hcols[i] + "' />";
      } // for()
    }
    break;
  case ILayer.USER_REGION :
    this.keyColumn = "REGION_ID";
    this.styleColumn = "REGION_ID";
    if( symc ) this.styleColumn = symc;
    this.tableName = "LBS_USER_REGION";
    if( !this.where || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
    else
      this.where = "KEY='" + lboxKey + "' AND " + this.where;
    this.hiddenColumn = "REGION_NAME";
    this.hiddenInfo = "<field column='REGION_NAME' name='NAME' />";
    if( hcols ) {
      for( var i = 0; i < hcols.length; i++ ) {
        this.hiddenColumn += "," + hcols[i];
        this.hiddenInfo += "<field column='" + hcols[i] + "' name='" + hcols[i] + "' />";
      } // for()
    }
    break;
  case ILayer.USER_LINE :
    this.keyColumn = "LINE_ID";
    this.styleColumn = "LINE_ID";
    if( symc ) this.styleColumn = symc;
	if( !sym ) sym = "L.UM_ROTA_1";
    this.tableName = "LBS_USER_LINE";
    if( !this.where || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
    else
      this.where = "KEY='" + lboxKey + "' AND " + this.where;
    this.hiddenColumn = "LINE_NAME";
    this.hiddenInfo = "<field column='LINE_NAME' name='NAME' />";
    if( hcols ) {
      for( var i = 0; i < hcols.length; i++ ) {
        this.hiddenColumn += "," + hcols[i];
        this.hiddenInfo += "<field column='" + hcols[i] + "' name='" + hcols[i] + "' />";
      } // for()
    }
    break;	
  case ILayer.CAMP_CATEGORY :
    this.keyColumn = "ID";
    this.styleColumn = "BRAND_SEMBOL_NO";
    if( symc ) this.styleColumn = symc;
    this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
    this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CG." + this.where : "");
    this.hiddenColumn = "STANDARD_NAME";
    this.hiddenInfo = "<field column='STANDARD_NAME' name='NAME' />";
    break;
  case ILayer.CAMP_CAMPAIGN :
    this.keyColumn = "ID";
    this.styleColumn = "BRAND_SEMBOL_NO";
    if( symc ) this.styleColumn = symc;
    this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
    this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CC." + this.where : "");
    this.hiddenColumn = "STANDARD_NAME";
    this.hiddenInfo = "<field column='STANDARD_NAME' name='NAME' />";
    break;
  case ILayer.DEAL_CATEGORY :
    this.keyColumn = "DP.DEAL_ID";
    this.styleColumn = null;
    if( symc ) this.styleColumn = symc;
    this.tableName = "DEAL_PROVIDER DP, DEAL_CATEGORY DC";
    this.where = "(DP.DEAL_ID = DC.DEAL_ID)" + (this.where && this.where.length > 0 ? " AND DC." + this.where : "");
    this.hiddenColumn = "NAME";
    this.hiddenInfo = "<field column='NAME' name='NAME' />";
    break;
  case ILayer.DEAL_DEAL :
    this.keyColumn = "DEAL_ID";
    this.styleColumn = null;
    if( symc ) this.styleColumn = symc;
    this.tableName = "DEAL_PROVIDER";
    this.where = (this.where && this.where.length > 0 ? this.where : "DEAL_ID = 0");
    this.hiddenColumn = "NAME";
    this.hiddenInfo = "<field column='NAME' name='NAME' />";
    break;
  case ILayer.MAP_ROUTE :
    this.keyColumn = "PATH_ID";
    this.styleColumn = "PATH_ID";
    if( symc ) this.styleColumn = symc;
    this.tableName = "NET_PATHS";
    this.where = (this.where && this.where.length > 0 ? this.where : "PATH_ID = 0");
    this.hiddenColumn = "";
    this.hiddenInfo = "<field column='PATH_ID' name='ID' />";
    break;
  case ILayer.MAP_YOL :
    this.keyColumn = "YOL_ID";
    this.styleColumn = "YOL_ID";
    if( symc ) this.styleColumn = symc;
    this.tableName = "YOL";
    this.where = (this.where && this.where.length > 0 ? this.where : "YOL_ID = 0");
    this.hiddenColumn = "YOL_ADI";
    this.hiddenInfo = "<field column='YOL_ADI' name='NAME' />";
    break;
  default:
    return;
  } // switch()

  if( this.thmLayer ) {
    this.mapview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86403236 ) {
    var baseQuery;
    if( this.typ == ILayer.CAMP_CATEGORY || this.typ == ILayer.CAMP_CAMPAIGN )
      baseQuery = "SELECT GEOLOC," + this.keyColumn + "," + this.styleColumn + " STY" + (this.labelColumn ? "," + this.labelColumn + " AS LABEL" : "") + (this.hiddenColumn ? "," + this.hiddenColumn : "") + " FROM POI WHERE ID IN (SELECT DISTINCT ID FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where)) + ")";
    else
      baseQuery = "SELECT GEOLOC," + this.keyColumn + "," + this.styleColumn + " STY" + (this.labelColumn ? "," + this.labelColumn + " AS LABEL" : "") + (this.hiddenColumn ? "," + this.hiddenColumn : "") + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));
    var themeXml  = "<themes><theme name='" + this.typ + "_" + this.id + "_THEME' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.keyColumn + "' " + (this.labelColumn ? "label_column='LABEL' label_style='" + this.labelStyle + "' " : "") +
                    "render_style='" + (sym ? (sym.name ? sym.name : (sym.sym ? (sym.sym.name ? sym.sym.name : sym.sym) : sym)) : "C.DUMMY") + "' datasource='" + datasrcApp + "'>" + baseQuery +
                    "<hidden_info>" + this.hiddenInfo + "</hidden_info></jdbc_query></theme></themes>";
    var thm = new MVThemeBasedFOI(this.id, themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableImageCaching(false);
    if( sym ) {
      if( sym.name ) thm.addStyle(sym);
      else
      if( sym.sym ) {
        for( var i = 0; i < sym.symlist.length; i++ ) {
          var data = sym.symlist[i];
          if( data.sym.name ) thm.addStyle(data.sym);
        }
        thm.addStyle(sym.sym);
      }
    }

    thm.setVisible(visible);
    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            if( foi.attrnames ) {
              for( var i = 0; i < foi.attrnames.length; i++ ) {
                var data = { name: foi.attrnames[i], value: foi.attrs[i] };
                x.data.push(data);
              } // for()
            }
            mouseclick(p, x);
      });
    }
    if( !sym ) {
      if( this.styleColumn == "BRAND_SEMBOL_NO" ) thm.setRenderingStyle("V.MAP_POI_BRAND");
    }
    else {
      if( sym.name ) thm.setRenderingStyle(sym.name);
      else
      if( sym.sym ) thm.setRenderingStyle(sym.sym.name);
    }
    if( this.labelColumn ) thm.enableLabels(true);
    thm.enableAutoWholeImage(true, 60, 6000);
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
  return;
} // createLayer()

//----------------------------------------------------------------------------

ILayer.prototype.createHeatmapLayer=function(typ, id, thmtyp, where, visible) {
  this.typ = typ;
  this.thmtyp = thmtyp;
  this.id = id;
  this.where = where;
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;

  switch( thmtyp ) {
  case ILayer.USER_POINT :
    this.keyColumn = "POINT_ID";
    this.tableName = "LBS_USER_POINT";
    if( !this.where || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
    else
      this.where = "KEY='" + lboxKey + "' AND " + this.where;
    if( !sym ) sym = "M.RED_PIN";
    break;
  case ILayer.CAMP_CATEGORY :
    this.keyColumn = "ID";
    this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
    this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CG." + this.where : "");
    break;
  case ILayer.CAMP_CAMPAIGN :
    this.keyColumn = "ID";
    this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
    this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CC." + this.where : "");
    break;
  case ILayer.DEAL_CATEGORY :
    this.keyColumn = "DP.DEAL_ID";
    this.tableName = "DEAL_PROVIDER DP, DEAL_CATEGORY DC";
    this.where = "(DP.DEAL_ID = DC.DEAL_ID)" + (this.where && this.where.length > 0 ? " AND DC." + this.where : "");
    break;
  case ILayer.DEAL_DEAL :
    this.keyColumn = "DEAL_ID";
    this.tableName = "DEAL_PROVIDER";
    this.where = (this.where && this.where.length > 0 ? this.where : "DEAL_ID = 0");
    break;
  default:
    return;
  } // switch()

  if( this.thmLayer ) {
    mview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  var sym;
  switch( typ ) {
  case ILayer.HEATMAP_ULKE :    sym = "V.HEAT_ULKE"; break;
  case ILayer.HEATMAP_IL :      sym = "V.HEAT_IL"; break;
  case ILayer.HEATMAP_ILCE :    sym = "V.HEAT_ILCE"; break;
  case ILayer.HEATMAP_MAHALLE : sym = "V.HEAT_MAHALLE"; break;
  default:
    return;
  } // switch()

  if( Math.abs((new Date()).getTime() - gtm) < 86403236 ) {
    var baseQuery;
    if( this.typ == ILayer.CAMP_CATEGORY || this.typ == ILayer.CAMP_CAMPAIGN )
      baseQuery = "SELECT GEOLOC," + this.keyColumn + " FROM POI WHERE ID IN (SELECT DISTINCT ID FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where)) + ")";
    else
      baseQuery = "SELECT GEOLOC," + this.keyColumn + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));

    var themeXml  = "<themes><theme name='" + this.typ + "_" + this.id + "_THEME' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.keyColumn + "' " +
                    "render_style='" + sym + "' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>";
    var thm = new MVThemeBasedFOI(this.id, themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableImageCaching(false);
    thm.setVisible(visible);
    thm.setRenderingStyle(sym);
    thm.enableAutoWholeImage(true, 60, 6000);
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
  return;
} // createHeatmapLayer()

//----------------------------------------------------------------------------

ILayer.prototype.createCategoryLayer=function(typ, id, sym, visible, mouseclick) {
  this.typ = typ;
  this.id = id;
  this.tableName = convCategory2Table(typ);
  if( !this.tableName ) this.tableName = "POI";
  this.keyColumn = "ID";
  this.where = convCategory2Where(typ);
  if( !this.where ) {
    alert("Category not found !");
    return;
  }
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;

  this.styleColumn = null;
  if( this.where.indexOf("TUR") >= 0 ) this.styleColumn = "TUR";
  else
  if( this.where.indexOf("BRAND_ID") >= 0 || this.where.indexOf("BRAND_NAME") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
  if( this.where.indexOf("SUB_TYPE") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
    ;
	

  if( this.thmLayer ) {
    mview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
    var baseQuery = "SELECT GEOLOC," + this.keyColumn + (this.styleColumn ? "," + this.styleColumn : "")  + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));
    var themeXml  = "<themes><theme name='" + this.typ + "_" + this.id + "_THEME' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.keyColumn + "' " +
                    "render_style='" + (sym ? (sym.name ? sym.name : (sym.sym ? (sym.sym.name ? sym.sym.name : sym.sym) : sym)) : "M.RED_PIN") + "' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>";
    var thm = new MVThemeBasedFOI(this.id, themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableImageCaching(false);
    if( sym ) {
      if( sym.name ) thm.addStyle(sym);
      else
      if( sym.sym ) {
        for( var i = 0; i < sym.symlist.length; i++ ) {
          var data = sym.symlist[i];
          if( data.sym.name ) thm.addStyle(data.sym);
        }
        thm.addStyle(sym.sym);
      }
    }

    thm.setVisible(visible);
    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            if( foi.attrnames ) {
              for( var i = 0; i < foi.attrnames.length; i++ ) {
                var data = { name: foi.attrnames[i], value: foi.attrs[i] };
                x.data.push(data);
              } // for()
            }
            mouseclick(p, x);
      });
    }
    if( !sym ) {
      if( this.styleColumn == "TUR" ) thm.setRenderingStyle("V.MAP_POI_CATEGORY");
      else
      if( this.styleColumn == "BRAND_SEMBOL_NO" ) thm.setRenderingStyle("V.MAP_POI_BRAND");
      else
        ;
    }
    else {
      if( sym.name ) thm.setRenderingStyle(sym.name);
      else
      if( sym.sym ) thm.setRenderingStyle(sym.sym.name);
    }
    thm.enableAutoWholeImage(true, 60, 6000);
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
  return;
} // createCategoryLayer()

//----------------------------------------------------------------------------

ILayer.prototype.createCategoryHeatmapLayer=function(typ, id, category, visible) {
  this.typ = typ;
  this.id = id;
  this.tableName = "POI";
  this.keyColumn = "ID";
  this.where = convCategory2Where(category);
  if( !this.where ) {
    alert("Category not found !");
    return;
  }
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;

  if( this.thmLayer ) {
    mview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  var sym;
  switch( typ ) {
  case ILayer.HEATMAP_ULKE :    sym = "V.HEAT_ULKE"; break;
  case ILayer.HEATMAP_IL :      sym = "V.HEAT_IL"; break;
  case ILayer.HEATMAP_ILCE :    sym = "V.HEAT_ILCE"; break;
  case ILayer.HEATMAP_MAHALLE : sym = "V.HEAT_MAHALLE"; break;
  default:
    return;
  } // switch()

  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
    var baseQuery = "SELECT GEOLOC," + this.keyColumn + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));
    var themeXml  = "<themes><theme name='" + this.typ + "_" + this.id + "_THEME' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.keyColumn + "' " +
                    "render_style='" + sym + "' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>";
    var thm = new MVThemeBasedFOI(this.id, themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableImageCaching(false);
    thm.setVisible(visible);
    thm.setRenderingStyle(sym);
    thm.enableAutoWholeImage(true, 60, 6000);
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
  return;
} // createCategoryHeatmapLayer()

//----------------------------------------------------------------------------

ILayer.prototype.createBrandLayer=function(typ, id, sym, visible, mouseclick) {
  this.typ = typ;
  this.id = id;
  this.tableName = "POI";
  this.keyColumn = "ID";
  this.where = convBrand2Where(typ);
  if( !this.where ) {
    alert("Brand not found !");
    return;
  }
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;

  this.styleColumn = null;
  if( this.where.indexOf("TUR") >= 0 ) this.styleColumn = "TUR";
  else
  if( this.where.indexOf("BRAND_ID") >= 0 || this.where.indexOf("BRAND_NAME") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
  if( this.where.indexOf("SUB_TYPE") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
    ;

  if( this.thmLayer ) {
    mview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86413436 ) {
    var baseQuery = "SELECT GEOLOC," + this.keyColumn + (this.styleColumn ? "," + this.styleColumn : "")  + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));
    var themeXml  = "<themes><theme name='" + this.typ + "_" + this.id + "_THEME' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.keyColumn + "' " +
                    "render_style='" + (sym ? (sym.name ? sym.name : (sym.sym ? (sym.sym.name ? sym.sym.name : sym.sym) : sym)) : "M.RED_PIN") + "' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>";
    var thm = new MVThemeBasedFOI(this.id, themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableImageCaching(false);
    if( sym ) {
      if( sym.name ) thm.addStyle(sym);
      else
      if( sym.sym ) {
        for( var i = 0; i < sym.symlist.length; i++ ) {
          var data = sym.symlist[i];
          if( data.sym.name ) thm.addStyle(data.sym);
        }
        thm.addStyle(sym.sym);
      }
    }

    thm.setVisible(visible);
    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            if( foi.attrnames ) {
              for( var i = 0; i < foi.attrnames.length; i++ ) {
                var data = { name: foi.attrnames[i], value: foi.attrs[i] };
                x.data.push(data);
              } // for()
            }
            mouseclick(p, x);
      });
    }
    if( !sym ) {
      if( this.styleColumn == "TUR" ) thm.setRenderingStyle("V.MAP_POI_CATEGORY");
      else
      if( this.styleColumn == "BRAND_SEMBOL_NO" ) thm.setRenderingStyle("V.MAP_POI_BRAND");
      else
        ;
    }
    else {
      if( sym.name ) thm.setRenderingStyle(sym.name);
      else
      if( sym.sym ) thm.setRenderingStyle(sym.sym.name);
    }
    thm.enableAutoWholeImage(true, 60, 6000);
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
  return;
} // createBrandLayer()

//----------------------------------------------------------------------------

ILayer.prototype.createBrandHeatmapLayer=function(typ, id, brand, visible) {
  this.typ = typ;
  this.id = id;
  this.tableName = "POI";
  this.keyColumn = "ID";
  this.where = convBrand2Where(brand);
  if( !this.where ) {
    alert("Brand not found !");
    return;
  }
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;

  if( this.thmLayer ) {
    this.mapview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  var sym;
  switch( typ ) {
  case ILayer.HEATMAP_ULKE :    sym = "V.HEAT_ULKE"; break;
  case ILayer.HEATMAP_IL :      sym = "V.HEAT_IL"; break;
  case ILayer.HEATMAP_ILCE :    sym = "V.HEAT_ILCE"; break;
  case ILayer.HEATMAP_MAHALLE : sym = "V.HEAT_MAHALLE"; break;
  default:
    return;
  } // switch()

  if( Math.abs((new Date()).getTime() - gtm) < 86413436 ) {
    var baseQuery = "SELECT GEOLOC," + this.keyColumn + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));
    var themeXml  = "<themes><theme name='" + this.typ + "_" + this.id + "_THEME' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.keyColumn + "' " +
                    "render_style='" + sym + "' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>";
    var thm = new MVThemeBasedFOI(this.id, themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableImageCaching(false);
    thm.setVisible(visible);
    thm.setRenderingStyle(sym);
    thm.enableAutoWholeImage(true, 60, 6000);
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
  return;
} // createBrandHeatmapLayer()

//----------------------------------------------------------------------------

ILayer.prototype.createDemographicLayer=function(typ, id, visible, mouseclick) {
  this.typ = typ;
  this.id = id;
  this.tableName = null;

  switch( typ ) {
  case ILayer.DEMOGRAFIK_YAS_DURUMU :   this.tableName = "DEMOGRAFIK_YAS_DURUMU"; break;
  case ILayer.DEMOGRAFIK_NUFUS :        this.tableName = "DEMOGRAFIK_NUFUS"; break;
  case ILayer.DEMOGRAFIK_EGITIM :       this.tableName = "DEMOGRAFIK_EGITIM"; break;
  case ILayer.DEMOGRAFIK_KONUT_SAYISI : this.tableName = "DEMOGRAFIK_KONUT_SAYISI"; break;
  case ILayer.DEMOGRAFIK_ARAC_SAYISI :  this.tableName = "DEMOGRAFIK_ARAC_SAYISI"; break;
    break;
  } // switch()
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;

  if( this.thmLayer ) {
    mview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
    var thm = new MVThemeBasedFOI(id, datasrcApp + "." + this.tableName);
    thm.setVisible(visible);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
    thm.enableLabels(false);
    thm.enableAutoWholeImage(true, 60, 8000);
    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            if( foi.attrnames ) {
              for( var i = 0; i < foi.attrnames.length; i++ ) {
                var data = { name: foi.attrnames[i], value: foi.attrs[i] };
                x.data.push(data);
              } // for()
            }
            mouseclick(p, x);
      });
    }
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
  return;
} // createDemographicLayer()

//----------------------------------------------------------------------------

ILayer.prototype.createThemeLayer=function(name, id, params, visible, mouseclick, mouserclick) {
  this.typ = ILayer.PREDEFINED_THEME;
  this.id = id;
  this.tableName = name;
  this.keyColumn = null;
  this.where = null;
  this.styleColumn = null;
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;

  if( this.thmLayer ) {
    mview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
    var thm = new MVThemeBasedFOI(id, datasrcApp + "." + name);
    if( params ) {
      switch( params.length ) {
      case 1 : thm.setQueryParameters(params[0]); break;
      case 2 : thm.setQueryParameters(params[0], params[1]); break;
      case 3 : thm.setQueryParameters(params[0], params[1], params[2]); break;
      case 4 : thm.setQueryParameters(params[0], params[1], params[2], params[3]); break;
      case 5 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4]); break;
      case 6 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5]); break;
      case 7 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6]); break;
      case 8 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]); break;
      case 9 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]); break;
      case 10 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9]); break;
      case 11 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10]); break;
      case 12 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11]); break;
      case 13 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12]); break;
      case 14 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13]); break;
      case 15 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14]); break;
      case 16 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15]); break;
      case 17 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15], params[16]); break;
      case 18 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15], params[16], params[17]); break;
      case 19 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15], params[16], params[17], params[18]); break;
      case 20 : thm.setQueryParameters(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15], params[16], params[17], params[18], params[19]); break;
      
      } // switch()
    }
    thm.setVisible(visible);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
    thm.enableLabels(true);
    thm.enableAutoWholeImage(true, 60, 6000);
    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            if( foi.attrnames ) {
              for( var i = 0; i < foi.attrnames.length; i++ ) {
                var data = { name: foi.attrnames[i], value: foi.attrs[i] };
                x.data.push(data);
              } // for()
            }
            mouseclick(p, x);
      });
    }
    if( mouserclick ) {
      thm.attachEventListener(MVEvent.MOUSE_RIGHT_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            if( foi.attrnames ) {
              for( var i = 0; i < foi.attrnames.length; i++ ) {
                var data = { name: foi.attrnames[i], value: foi.attrs[i] };
                x.data.push(data);
              } // for()
            }
            mouserclick(p, x);
      });
    }
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
  return;
} // createThemeLayer()

//----------------------------------------------------------------------------

ILayer.prototype.createSqlQueryLayer=function(id, dsource, query, keycolumn, symc, sym, visible, mouseclick, lblc, lsym) {
/*
  this.id = id;
  this.dSource = dsource;
  this.query = query;
  this.keyColumn = keycolumn;
  this.labelColumn = lblc;
  this.labelStyle = lsym;

  if( !this.labelStyle ) this.labelStyle = "T.LABEL";
  if( symc ) this.styleColumn = symc;
  if( !sym ) sym = "M.PIN_1";

  if( this.thmLayer ) {
    this.mapview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86403236 ) {
    var themeXml  = "<themes><theme name='JDBC_THEME_" + this.id + "' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.keyColumn + "' " + (this.labelColumn ? "label_column='LABEL' label_style='" + this.labelStyle + "' " : "") +
                    "render_style='" + (sym ? (sym.name ? sym.name : (sym.sym ? (sym.sym.name ? sym.sym.name : sym.sym) : sym)) : "M.PIN_1") + "' datasource='" + this.dSource + "'>" + this.query +
                    "</jdbc_query></theme></themes>";
    var thm = new MVThemeBasedFOI(this.id, themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableImageCaching(false);
    if( sym ) {
      if( sym.name ) thm.addStyle(sym);
      else
      if( sym.sym ) {
        for( var i = 0; i < sym.symlist.length; i++ ) {
          var data = sym.symlist[i];
          if( data.sym.name ) thm.addStyle(data.sym);
        }
        thm.addStyle(sym.sym);
      }
    }

    thm.setVisible(visible);
    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = this.mapview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            mouseclick(p, x);
      });
    }

      if( sym.name ) thm.setRenderingStyle(sym.name);
      else
      if( sym.sym ) thm.setRenderingStyle(sym.sym.name);

    if( this.labelColumn ) thm.enableLabels(true);
    thm.enableAutoWholeImage(true, 60, 6000);
    thm.enableImageCaching(false);
    this.mapview.addThemeBasedFOI(thm);
    this.thmLayer = thm;
  }
*/
  return;
} // createSqlQueryLayer()

//----------------------------------------------------------------------------

ILayer.prototype.removeLayer=function() {
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  if( this.thmLayer ) {
    this.mapview.removeThemeBasedFOI(this.thmLayer);
    this.thmLayer = null;
  }
  return;
}

ILayer.prototype.setVisible=function(visible) {  
  if( this.thmLayer ) this.thmLayer.setVisible(visible);
  return;
}

ILayer.prototype.refresh=function() {
  if( this.thmLayer ) this.thmLayer.refresh();
  return;
}

ILayer.prototype.zoomToLayer=function() {
  if( this.thmLayer ) this.thmLayer.zoomToTheme();
  return;
}
