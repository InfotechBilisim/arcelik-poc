
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

var iJdbcSrid = 3857;



function ILayer(mapper) {
	
  if( OM.isNull(mapper) ) {
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
  this.thmUserPoints = null;
}

ILayer.prototype.createLayer=function(typ, id, where, symc, sym, visible, mouseclick, lblc, lsym, hcols, hoverStyle, textStyle, selectStyle, setToolTip, infowindow) {
  
  this.typ = typ;
  this.id = id;
  this.where = where;
  this.labelColumn = lblc;
  this.labelStyle = lsym;
  if( OM.isNull(this.labelStyle) ) this.labelStyle = "T.LABEL";
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;
  
  switch( typ ) {
	  
    case ILayer.MAP_BOLGE :
      this.keyColumn = "BOLGE_ID";
	  this.styleColumn = "BOLGE_ID";
	  if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "BOLGE";
      this.hiddenColumn = "BOLGE_ADI";
      break;
    
	case ILayer.MAP_IL :
      this.keyColumn = "IL_ID";
      this.tableName = "IL";
	  this.styleColumn = "IL_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "IL_ID <> 100";
      else this.where = "IL_ID <> 100 AND " + this.where;
      
	  this.hiddenColumn = "IL_ADI";
      break;
    
	case ILayer.MAP_ILCE :
      this.keyColumn = "ILCE_ID";
	  this.styleColumn = "ILCE_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "ILCE";
      this.hiddenColumn = "ILCE_ADI";
      break;
  
    case ILayer.MAP_MAHALLE :
      this.keyColumn = "MAHALLE_ID";
	  this.styleColumn = "MAHALLE_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "MAHALLE";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "TYPE=3";
      else this.where = this.where + " AND TYPE=3";
	
      this.hiddenColumn = "MAHALLE_ADI";
      break;
  
    case ILayer.USER_POINT :
      this.keyColumn = "POINT_ID";
      this.tableName = "LBS_USER_POINT";
	  this.styleColumn = "TYP";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
      
	  //if( OM.isNull(sym) ) sym = "M.RED_PIN";
	  if ( OM.isNull(sym) ) sym = new OM.style.Color({fill:"#ff2244", fillOpacity:0.5, stroke:"#ff2244" }); 
	  this.hiddenColumn = "POINT_NAME";
      
	  if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;
	  
    case ILayer.USER_REGION :
      this.keyColumn = "REGION_ID";
      this.styleColumn = "REGION_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "LBS_USER_REGION";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
      
	  this.hiddenColumn = "REGION_NAME";
      if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;
  
    case ILayer.USER_LINE :
      this.keyColumn = "LINE_ID";
	  this.styleColumn = "LINE_ID";
	  if( OM.notNull(symc) ) this.styleColumn = symc;
	  if( OM.isNull(sym) ) sym = "L.UM_ROTA_1";
      this.tableName = "LBS_USER_LINE";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
      
	  this.hiddenColumn = "LINE_NAME";
      if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;	
  
    case ILayer.CAMP_CATEGORY :
      this.keyColumn = "ID";
	  this.styleColumn = "BRAND_SEMBOL_NO";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
      this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CG." + this.where : "");
      this.hiddenColumn = "STANDARD_NAME";
      break;
  
    case ILayer.CAMP_CAMPAIGN :
      this.keyColumn = "ID";
	  this.styleColumn = "BRAND_SEMBOL_NO";
	  if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
      this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CC." + this.where : "");
      this.hiddenColumn = "STANDARD_NAME";
      break;
    case ILayer.DEAL_CATEGORY :
      this.keyColumn = "DP.DEAL_ID";
	  this.styleColumn = null;
	  if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "DEAL_PROVIDER DP, DEAL_CATEGORY DC";
      this.where = "(DP.DEAL_ID = DC.DEAL_ID)" + (this.where && this.where.length > 0 ? " AND DC." + this.where : "");
      this.hiddenColumn = "NAME";
      break;
  
    case ILayer.DEAL_DEAL :
      this.keyColumn = "DEAL_ID";
	  this.styleColumn = null;
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "DEAL_PROVIDER";
      this.where = (this.where && this.where.length > 0 ? this.where : "DEAL_ID = 0");
      this.hiddenColumn = "NAME";
      break;
    
	case ILayer.MAP_ROUTE :
      this.keyColumn = "PATH_ID";
	  this.styleColumn = "PATH_ID";
	  if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "NET_PATHS";
      this.where = (this.where && this.where.length > 0 ? this.where : "PATH_ID = 0");
      this.hiddenColumn = "";
     break;
  
    case ILayer.MAP_YOL :
      this.keyColumn = "YOL_ID";
      this.styleColumn = "YOL_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "YOL";
      this.where = (this.where && this.where.length > 0 ? this.where : "YOL_ID = 0");
      this.hiddenColumn = "YOL_ADI";
      break;
    
	default:
    return;
  } // switch()

  if( OM.notNull(this.thmLayer) ) {
    this.mapview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  

  
  if( OM.notNull(textStyle) ) textStyle = new OM.style.Text({styleName:"txt", fill:"#ff0000", fontStyle: OM.style.Text.FONTSTYLE_ITALIC, fontSize:50, sizeUnit:"kilometer"});

  if( Math.abs((new Date()).getTime() - gtm) < 86403236 ) {
    
    var baseQuery;
    if( this.typ == ILayer.CAMP_CATEGORY || this.typ == ILayer.CAMP_CAMPAIGN )
      baseQuery = "SELECT GEOLOC," + this.keyColumn + " AS ID " + "," + this.styleColumn + " STY" + (this.labelColumn ? "," + this.labelColumn + " AS LABEL" : "") + (this.hiddenColumn ? "," + this.hiddenColumn : "") + " FROM POI WHERE ID IN (SELECT DISTINCT ID FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where) + ")";
    else
      baseQuery = "SELECT GEOLOC, " + this.keyColumn + " AS ID " + "," + this.styleColumn + " STY" + (this.labelColumn ? "," + this.labelColumn + " AS LABEL" : "") + (this.hiddenColumn ? "," + this.hiddenColumn : "") + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where);

    if (typeof(infowindow) == "undefined") infowindow=false;
    var thm = new OM.layer.VectorLayer(this.id, {def:{type:OM.layer.VectorLayer.TYPE_JDBC, dataSource:datasrcApp, sql:baseQuery, url: urlBase, labelColumn:this.labelColumn, geometryColumn:"GEOLOC", loadOnDemand:false,infoWindow:infowindow}});
    
    if( OM.isNull(sym) ) {
      if( this.styleColumn == "BRAND_SEMBOL_NO" ) thm.setRenderingStyle({dataSource:datasrcApp, name: "V.MAP_POI_BRAND"});
    } 
	else {
      if( typeof sym  === "string" ) thm.setRenderingStyle({dataSource: datasrcApp, name: sym});
      else thm.setRenderingStyle(sym, [this.styleColumn]);
    }
	 
	if( !OM.isNull(hoverStyle) ) { 
	  //hoverStyle = new OM.style.Color({stroke:"#FFFFFF", strokeThickness:2});
	  thm.setHoverStyle(hoverStyle);
	  thm.enableFeatureHover(true);
	}
    thm.enableFeatureSelection(false);
    thm.setBringToTopOnMouseOver(true);
    thm.setToolTipCustomizer(this.hiddenColumn);
    thm.setLabelsVisible(true);
    thm.setLabelingStyle(textStyle);
    thm.setVisible(visible);
    thm.enableInfoWindow(infowindow);

   
    if ( setToolTip ) thm.setToolTipCustomizer(vectorLayerTooltip);
    if( OM.notNull(selectStyle) ) thm.setSelectStyle(selectStyle);
	
    if( OM.notNull(mouseclick) ) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, function(obj){
      var pos = mview.getCursorLocation();
      pos = pos.transform(8307);
      var p = { "x": pos.getX(), "y": pos.getY() };
      var x = { data: [] };
      var lyrAttr = obj.feature.attributes;	
      for (var key in lyrAttr) {
        if (lyrAttr.hasOwnProperty(key)) {
          var data = { name: key, value: lyrAttr[key] };
          x.data.push(data);
        }
     }
     return mouseclick(p, x);
	});
  
    mview.addLayer(thm);
    this.thmLayer = thm;
  }
  
  return;

}//createLayer()

ILayer.prototype.createVectorLayer=function(typ, id, where, symc, sym, visible, mouseclick, lblc, lsym, hcols, hoverStyle, textStyle, selectStyle, setToolTip) {
  
  this.typ = typ;
  this.id = id;
  this.where = where;
  this.labelColumn = lblc;
  this.labelStyle = lsym;
  if( OM.isNull(this.labelStyle) ) this.labelStyle = "T.LABEL";
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;
  
  switch( typ ) {
	  
    case ILayer.MAP_BOLGE :
      this.keyColumn = "BOLGE_ID";
	  this.styleColumn = "BOLGE_ID";
	  if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "BOLGE";
      this.hiddenColumn = "BOLGE_ADI";
      break;
    
	case ILayer.MAP_IL :
      this.keyColumn = "IL_ID";
      this.tableName = "IL";
	  this.styleColumn = "IL_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "IL_ID <> 100";
      else this.where = "IL_ID <> 100 AND " + this.where;
      
	  this.hiddenColumn = "IL_ADI";
      break;
    
	case ILayer.MAP_ILCE :
      this.keyColumn = "ILCE_ID";
	  this.styleColumn = "ILCE_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "ILCE";
      this.hiddenColumn = "ILCE_ADI";
      break;
  
    case ILayer.MAP_MAHALLE :
      this.keyColumn = "MAHALLE_ID";
	  this.styleColumn = "MAHALLE_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "MAHALLE";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "TYPE=3";
      else this.where = this.where + " AND TYPE=3";
	
      this.hiddenColumn = "MAHALLE_ADI";
      break;
  
    case ILayer.USER_POINT :
      this.keyColumn = "POINT_ID";
      this.tableName = "LBS_USER_POINT";
	  this.styleColumn = "TYP";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
      
	  //if( OM.isNull(sym) ) sym = "M.RED_PIN";
	  if ( OM.isNull(sym) ) sym = new OM.style.Color({fill:"#ff2244", fillOpacity:0.5, stroke:"#ff2244" }); 
	  this.hiddenColumn = "POINT_NAME";
      
	  if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;
	  
    case ILayer.USER_REGION :
      this.keyColumn = "REGION_ID";
      this.styleColumn = "REGION_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "LBS_USER_REGION";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
      
	  this.hiddenColumn = "REGION_NAME";
      if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;
  
    case ILayer.USER_LINE :
      this.keyColumn = "LINE_ID";
	  this.styleColumn = "LINE_ID";
	  if( OM.notNull(symc) ) this.styleColumn = symc;
	  if( OM.isNull(sym) ) sym = "L.UM_ROTA_1";
      this.tableName = "LBS_USER_LINE";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
      
	  this.hiddenColumn = "LINE_NAME";
      if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;	
  
    case ILayer.CAMP_CATEGORY :
      this.keyColumn = "ID";
	  this.styleColumn = "BRAND_SEMBOL_NO";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
      this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CG." + this.where : "");
      this.hiddenColumn = "STANDARD_NAME";
      break;
  
    case ILayer.CAMP_CAMPAIGN :
      this.keyColumn = "ID";
	  this.styleColumn = "BRAND_SEMBOL_NO";
	  if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
      this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CC." + this.where : "");
      this.hiddenColumn = "STANDARD_NAME";
      break;
    case ILayer.DEAL_CATEGORY :
      this.keyColumn = "DP.DEAL_ID";
	  this.styleColumn = null;
	  if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "DEAL_PROVIDER DP, DEAL_CATEGORY DC";
      this.where = "(DP.DEAL_ID = DC.DEAL_ID)" + (this.where && this.where.length > 0 ? " AND DC." + this.where : "");
      this.hiddenColumn = "NAME";
      break;
  
    case ILayer.DEAL_DEAL :
      this.keyColumn = "DEAL_ID";
	  this.styleColumn = null;
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "DEAL_PROVIDER";
      this.where = (this.where && this.where.length > 0 ? this.where : "DEAL_ID = 0");
      this.hiddenColumn = "NAME";
      break;
    
	case ILayer.MAP_ROUTE :
      this.keyColumn = "PATH_ID";
	  this.styleColumn = "PATH_ID";
	  if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "NET_PATHS";
      this.where = (this.where && this.where.length > 0 ? this.where : "PATH_ID = 0");
      this.hiddenColumn = "";
     break;
  
    case ILayer.MAP_YOL :
      this.keyColumn = "YOL_ID";
      this.styleColumn = "YOL_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "YOL";
      this.where = (this.where && this.where.length > 0 ? this.where : "YOL_ID = 0");
      this.hiddenColumn = "YOL_ADI";
      break;
    
	default:
    return;
  } // switch()

  if( OM.notNull(this.thmLayer) ) {
    this.mapview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  

  
  if( OM.notNull(textStyle) ) textStyle = new OM.style.Text({styleName:"txt", fill:"#ff0000", fontStyle: OM.style.Text.FONTSTYLE_ITALIC, fontSize:50, sizeUnit:"kilometer"});

  if( Math.abs((new Date()).getTime() - gtm) < 86403236 ) {
    
    var baseQuery;
    if( this.typ == ILayer.CAMP_CATEGORY || this.typ == ILayer.CAMP_CAMPAIGN )
      baseQuery = "SELECT GEOLOC," + this.keyColumn + " AS ID " + "," + this.styleColumn + " STY" + (this.labelColumn ? "," + this.labelColumn + " AS LABEL" : "") + (this.hiddenColumn ? "," + this.hiddenColumn : "") + " FROM POI WHERE ID IN (SELECT DISTINCT ID FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where) + ")";
    else
      baseQuery = "SELECT GEOLOC, " + this.keyColumn + " AS ID " + "," + this.styleColumn + " STY" + (this.labelColumn ? "," + this.labelColumn + " AS LABEL" : "") + (this.hiddenColumn ? "," + this.hiddenColumn : "") + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where);

 
    var thm = new OM.layer.VectorLayer(this.id, {def:{type:OM.layer.VectorLayer.TYPE_JDBC, dataSource:datasrcApp, sql:baseQuery, url: urlBase, labelColumn:this.labelColumn, geometryColumn:"GEOLOC", loadOnDemand:false}});
    
    if( OM.isNull(sym) ) {
      if( this.styleColumn == "BRAND_SEMBOL_NO" ) thm.setRenderingStyle({dataSource:datasrcApp, name: "V.MAP_POI_BRAND"});
    } 
	else {
      if( typeof sym  === "string" ) thm.setRenderingStyle({dataSource: datasrcApp, name: sym});
      else thm.setRenderingStyle(sym, [this.styleColumn]);
    }
	 
	if( !OM.isNull(hoverStyle) ) { 
	  //hoverStyle = new OM.style.Color({stroke:"#FFFFFF", strokeThickness:2});
	  thm.setHoverStyle(hoverStyle);
	  thm.enableFeatureHover(true);
	}
    thm.enableFeatureSelection(false);
    thm.setBringToTopOnMouseOver(true);
    thm.setToolTipCustomizer(this.hiddenColumn);
    thm.setLabelsVisible(true);
    thm.setLabelingStyle(textStyle);
	thm.setVisible(visible);
   
    if ( setToolTip ) thm.setToolTipCustomizer(vectorLayerTooltip);
    if( OM.notNull(selectStyle) ) thm.setSelectStyle(selectStyle);
	
    if( OM.notNull(mouseclick) ) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, function(obj){
      var pos = mview.getCursorLocation();
      pos = pos.transform(8307);
      var p = { "x": pos.getX(), "y": pos.getY() };
      var x = { data: [] };
      var lyrAttr = obj.feature.attributes;	
      for (var key in lyrAttr) {
        if (lyrAttr.hasOwnProperty(key)) {
          var data = { name: key, value: lyrAttr[key] };
          x.data.push(data);
        }
      }
      return mouseclick(p, x);
	});
  
    mview.addLayer(thm);
    this.thmLayer = thm;
  }
  
  return;

}//createVectorLayer()


ILayer.prototype.createDynamicLayer=function(typ, id, where, symc, sym, visible, mouseclick, lblc, lsym, hcols) {
  
  this.typ = typ;
  this.id = id;
  this.where = where;
  this.labelColumn = lblc;
  this.labelStyle = lsym;
  
  if( OM.isNull(this.labelStyle) ) this.labelStyle = "T.LABEL";
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
 
  var mview = this.mapview;
  
  switch( typ ) {
	  
    case ILayer.MAP_BOLGE :
      this.keyColumn = "BOLGE_ID";
      this.styleColumn = "BOLGE_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "BOLGE";
      this.hiddenColumn = "BOLGE_ADI";
      break;
	
    case ILayer.MAP_IL :
      this.keyColumn = "IL_ID";
      this.styleColumn = "IL_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "IL";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "IL_ID <> 100";
      else this.where = "IL_ID <> 100 AND " + this.where;
   
	  this.hiddenColumn = "IL_ADI";
      break;
	
    case ILayer.MAP_ILCE :
      this.keyColumn = "ILCE_ID";
      this.styleColumn = "ILCE_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "ILCE";
      this.hiddenColumn = "ILCE_ADI";
      break;
	
    case ILayer.MAP_MAHALLE :
      this.keyColumn = "MAHALLE_ID";
      this.styleColumn = "MAHALLE_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "MAHALLE";
	  
      if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "TYPE=3";
      else this.where = this.where + " AND TYPE=3";
    
	  this.hiddenColumn = "MAHALLE_ADI";
      break;
	
    case ILayer.USER_POINT :
      this.keyColumn = "POINT_ID";
      this.styleColumn = "TYP";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "LBS_USER_POINT";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
    
	  if( OM.isNull(sym) ) sym = "M.RED_PIN";
      this.hiddenColumn = "POINT_NAME";
      
	  if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;
	
    case ILayer.USER_REGION :
      this.keyColumn = "REGION_ID";
      this.styleColumn = "REGION_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "LBS_USER_REGION";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
    
	  this.hiddenColumn = "REGION_NAME";
      
	  if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;
	
    case ILayer.USER_LINE :
      this.keyColumn = "LINE_ID";
      this.styleColumn = "LINE_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
	  if( OM.isNull(sym) ) sym = "L.UM_ROTA_1";
      this.tableName = "LBS_USER_LINE";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) this.where = "KEY='" + lboxKey + "'";
      else this.where = "KEY='" + lboxKey + "' AND " + this.where;
    
	  this.hiddenColumn = "LINE_NAME";
      
	  if( OM.notNull(hcols) ) {
        for( var i = 0; i < hcols.length; i++ ) {
          this.hiddenColumn += "," + hcols[i];
        } // for()
      }
      break;	
	
    case ILayer.CAMP_CATEGORY :
      this.keyColumn = "ID";
      this.styleColumn = "BRAND_SEMBOL_NO";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
      this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CG." + this.where : "");
      this.hiddenColumn = "STANDARD_NAME";
      break;
	
    case ILayer.CAMP_CAMPAIGN :
      this.keyColumn = "ID";
      this.styleColumn = "BRAND_SEMBOL_NO";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";
      this.where = "(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='" + lboxKey + "'" + (this.where && this.where.length > 0 ? " AND CC." + this.where : "");
      this.hiddenColumn = "STANDARD_NAME";
      break;
	
    case ILayer.DEAL_CATEGORY :
      this.keyColumn = "DP.DEAL_ID";
      this.styleColumn = null;
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "DEAL_PROVIDER DP, DEAL_CATEGORY DC";
      this.where = "(DP.DEAL_ID = DC.DEAL_ID)" + (this.where && this.where.length > 0 ? " AND DC." + this.where : "");
      this.hiddenColumn = "NAME";
      break;
	
    case ILayer.DEAL_DEAL :
      this.keyColumn = "DEAL_ID";
      this.styleColumn = null;
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "DEAL_PROVIDER";
      this.where = (this.where && this.where.length > 0 ? this.where : "DEAL_ID = 0");
      this.hiddenColumn = "NAME";
      break;
	
    case ILayer.MAP_ROUTE :
      this.keyColumn = "PATH_ID";
      this.styleColumn = "PATH_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "NET_PATHS";
      this.where = (this.where && this.where.length > 0 ? this.where : "PATH_ID = 0");
      this.hiddenColumn = "";
      break;
	
    case ILayer.MAP_YOL :
      this.keyColumn = "YOL_ID";
      this.styleColumn = "YOL_ID";
      if( OM.notNull(symc) ) this.styleColumn = symc;
      this.tableName = "YOL";
      this.where = (this.where && this.where.length > 0 ? this.where : "YOL_ID = 0");
      this.hiddenColumn = "YOL_ADI";
      break;
    
	default:
    
	return;
  } // switch()

  if( OM.notNull(this.thmLayer) ) {
    mview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  
  var myuniv = mview.getUniverse();
  var myconfig = new OM.layer.TileLayerConfig({tileImageWidth: 256, tileImageHeight: 256});	
  
  if( Math.abs((new Date()).getTime() - gtm) < 86403236 ) {

    var baseQuery;
	
    if( this.typ == ILayer.CAMP_CATEGORY || this.typ == ILayer.CAMP_CAMPAIGN )
      baseQuery = "SELECT GEOLOC," + this.keyColumn + "," + this.styleColumn + " STY" + (this.labelColumn ? "," + this.labelColumn + " AS LABEL" : "") + (this.hiddenColumn ? "," + this.hiddenColumn : "") + " FROM POI WHERE ID IN (SELECT DISTINCT ID FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where)) + ")";
    else
      baseQuery = "SELECT GEOLOC, " + this.keyColumn + "," + this.styleColumn + " STY" + (this.labelColumn ? "," + this.labelColumn + " AS LABEL" : "") + (this.hiddenColumn ? "," + this.hiddenColumn : "") + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));

	var themeJDBC = new OM.server.ServerJDBCTheme(id);
	
	if( OM.isNull(sym) ) {
      if( this.styleColumn == "BRAND_SEMBOL_NO" ) sym = "V.MAP_POI_BRAND" ;
    }	
   
    themeJDBC.setQuery(baseQuery);
    themeJDBC.setSRID('8307');
    themeJDBC.setGeometryColumnName("GEOLOC");
	
    if( OM.notNull(sym) && OM.notNull(sym.sym))	themeJDBC.setRenderingStyleName(sym.name);
	else if( sym instanceof Object ) themeJDBC.setRenderingStyleName(sym.getStyleName());
	else themeJDBC.setRenderingStyleName(sym);
	
    themeJDBC.setDataSourceName(datasrcApp);
	themeJDBC.setRenderingStyleValueColumns(this.keyColumn);
   	themeJDBC.addInfoColumn({column: this.keyColumn, name:this.keyColumn});
	themeJDBC.addInfoColumn({column: this.hiddenColumn, name:this.hiddenColumn});
	
	var req = new OM.server.ServerMapRequest(urlBase);

    req.setProperties({dataSource:datasrcApp, transparent:true, antialiase:"false"});

    req.addTheme(themeJDBC);
	
	if( OM.notNull(sym) && OM.notNull(sym.sym) ) req.addStyle(sym.sym);
	else if( sym instanceof Object ) req.addStyle(sym);
	
    var dtl_props = {  // dtl specific properties
        dataSource: datasrcApp, // if not provided, it comes from ServerMapRequest;
        universe: myuniv,    
        tileLayerConfig: myconfig,
        tileServerURL: urlBase + "/omserver", //default is ServerMapRequest's baseURL + '/omserver'
        enableUTFGrid: true,
        enableUTFGridInfoWindow: true,
        utfGridResolution: 4
    };
	
    var thm = new OM.layer.DynamicTileLayer(id, dtl_props, req);
	
	if( OM.notNull(mouseclick) ) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, function(obj){
      var pos = mview.getCursorLocation();
      pos = pos.transform(8307);
      var p = { "x": pos.getX(), "y": pos.getY() };
      var x = { data: [] };
      var lyrAttr = thm.getFeatureInfo();
      var attrValues = lyrAttr[Object.keys(lyrAttr)[0]];	 				 
      for (var key in attrValues) {
        if (attrValues.hasOwnProperty(key)) {
          var data = {name: key, value: attrValues[key]};
          x.data.push(data);
        }
      }		 
      return mouseclick(p,x);
    });
  
    mview.addLayer(thm);
    this.thmLayer = thm;
  }
  return;
} // createDynamicLayer()

ILayer.prototype.createVectorCategoryLayer=function(typ, id, sym, visible, mouseclick, hoverStyle, setToolTip) {
  
  this.typ = typ;
  this.id = id;
  this.tableName = convCategory2Table(typ);
  
  if( OM.isNull(this.tableName) ) this.tableName = "POI";
  
  this.keyColumn = "ID";
  this.where = convCategory2Where(typ);
  this.hiddenColumn = "STANDARD_NAME";

  if( OM.isNull(this.where) ) {
    alert("Category not found !");
    return;
  }
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;

  if( OM.notNull(this.thmLayer) ) {
    mview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  
  this.styleColumn = null;
  
  if( this.where.indexOf("TUR") >= 0 ) this.styleColumn = "TUR";
  else
  if( this.where.indexOf("BRAND_ID") >= 0 || this.where.indexOf("BRAND_NAME") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
  if( this.where.indexOf("SUB_TYPE") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
    ;
  
  if ( OM.isNull(sym) ) sym = new OM.style.Color({fill:"#ff2244", fillOpacity:0.5, stroke:"#ff2244" }); 
  
  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
	  
    var baseQuery = "SELECT GEOLOC," + this.hiddenColumn + "," +this.keyColumn + (this.styleColumn ? "," + this.styleColumn : "")  + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where);
    
	var thm = new OM.layer.VectorLayer(this.id , {renderingStyle: sym, def:{type:OM.layer.VectorLayer.TYPE_JDBC, dataSource:datasrcApp, sql:baseQuery, url: urlBase, geometryColumn:"GEOLOC", loadOnDemand:true}});
	
	if( OM.isNull(hoverStyle) ) hoverStyle = new OM.style.Marker({vectorDef : [{shape: {type:"circle", cx:5, cy:5, width:10, height:10}, style: {fill:"#ff2244", stroke:"#ff2244", fillOpacity:0.5}}], width: 30, height:30});
	
	thm.setHoverStyle(hoverStyle);

    if( setToolTip ) thm.setToolTipCustomizer(vectorLayerTooltip);
	
	thm.setVisible(visible);
	
    mview.addLayer(thm);
	
	if( OM.notNull(mouseclick) ) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, function(obj){
	var pos = mview.getCursorLocation();
	pos = pos.transform(8307);
	var p = { "x": pos.getX(), "y": pos.getY() };
	var x = { data: [] };
	var lyrAttr = obj.feature.attributes;	
		for (var key in lyrAttr) {
			if (lyrAttr.hasOwnProperty(key)) {
				var data = { name: key, value: lyrAttr[key] };
				x.data.push(data);
			}
		}
		return mouseclick(p, x);
	});
    
    this.thmLayer = thm;
  }
 
 return;
} // createVectorCategoryLayer()

ILayer.prototype.setJDBCRenderingStyle = function (id, sym ) {
	
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;	    
  
  var thm = this.mapview.getLayerByName(id);
  
  thm.setRenderingStyle(sym, ["ID"]);
  thm.redraw();
	
  return;	
} //setJDBCRenderingStyle()


ILayer.prototype.addSecondaryStyle = function (id, sym ) {
	
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;	    
  
  var thm = this.mapview.getLayerByName(id);
  
  thm.addSecondaryStyle(sym);
  thm.redraw();
	
  return;	
}//addSecondaryStyle()

ILayer.prototype.createBrandLayer=function(typ, id, sym, visible, mouseclick) {
  
  this.typ = typ;
  this.id = id;
  this.tableName = "POI";
  this.keyColumn = "ID";
  this.hiddenColumn = "STANDARD_NAME";
  
  this.where = convBrand2Where(typ);
  
  if( OM.isNull(this.where) ) {
    alert("Brand not found !");
    return;
  }
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;

  this.styleColumn = null;
 
  if( this.where.indexOf("TUR") >= 0 ) this.styleColumn = "TUR";
  else
  if( this.where.indexOf("BRAND_ID") >= 0 || this.where.indexOf("BRAND_NAME") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
  if( this.where.indexOf("SUB_TYPE") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
    ;

  var myconfig = new OM.layer.TileLayerConfig({tileImageWidth: 256, tileImageHeight: 256});
  
  var myuniv = this.mapview.getUniverse();
  
  if( OM.notNull(this.thmLayer) ) {
    this.mapview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  
  if( OM.isNull(sym) ) {
    if( this.styleColumn == "TUR" ) sym = "V.MAP_POI_CATEGORY";
    else
    if( this.styleColumn == "BRAND_SEMBOL_NO" ) sym =  "V.MAP_POI_BRAND";
    else
      ;
  }
  
  if( Math.abs((new Date()).getTime() - gtm) < 86413436 ) {
    
	var baseQuery = "SELECT GEOLOC," + this.hiddenColumn + "," + this.keyColumn + (this.styleColumn ? "," + this.styleColumn : "")  + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));
					
    var themeJDBC = new OM.server.ServerJDBCTheme(id);
		
    themeJDBC.setQuery(baseQuery);
    themeJDBC.setSRID('8307');
    themeJDBC.setGeometryColumnName("GEOLOC");
	var render_style = sym ? (sym.name ? sym.name : (sym.sym ? (sym.sym.name ? sym.sym.name : sym.sym) : sym)) : "M.PIN_1";
    themeJDBC.setRenderingStyleName(render_style);
    themeJDBC.setDataSourceName(datasrcApp);
	themeJDBC.setRenderingStyleValueColumns(this.styleColumn);

    themeJDBC.addInfoColumn({column: this.keyColumn, name:this.keyColumn});
	themeJDBC.addInfoColumn({column: this.hiddenColumn, name:this.hiddenColumn});

	var req = new OM.server.ServerMapRequest(urlBase);

    req.setProperties({dataSource:datasrcApp, transparent:true, antialiase:"false"});
    
    req.addTheme(themeJDBC);
		
    var dtl_props = {  // dtl specific properties
        dataSource:datasrcApp, // if not provided, it comes from ServerMapRequest;
        universe: myuniv,    
        tileLayerConfig: myconfig,
        tileServerURL: urlBase + "/omserver", //default is ServerMapRequest's baseURL + '/omserver'
        enableUTFGrid: true,
        enableUTFGridInfoWindow: true,
        utfGridResolution: 12
    };
	
    var thm = new OM.layer.DynamicTileLayer(id +"_LayerBrand" , dtl_props, req);
				
				
	if( OM.notNull(mouseclick) ) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, function(obj){
	  var pos = mview.getCursorLocation();
	  pos = pos.transform(8307);
	  var p = { "x": pos.getX(), "y": pos.getY() };
	  var x = { data: [] };
	  var lyrAttr = thm.getFeatureInfo();
	  var attrValues = lyrAttr[Object.keys(lyrAttr)[0]];	 				 
      for (var key in attrValues) {
        if (attrValues.hasOwnProperty(key)) {
          var data = {name: key, value: attrValues[key]};
          x.data.push(data);
        }
      }		 
      return mouseclick(p,x);
	});   
	
	thm.setVisible(visible);
	
    this.mapview.addLayer(thm);
    this.thmLayer = thm;
  }
  return;
} // createBrandLayer()

ILayer.prototype.createClusterBrandLayer=function(typ, id, sym, visible, variableMarker, minPointCount, maxClusteringLevel, threshold, mouseclick) {
  
  this.typ = typ;
  this.id = id;
  this.tableName = "POI";
  this.keyColumn = "ID";
  
  this.where = convBrand2Where(typ);
  if( OM.isNull(this.where) ) {
    alert("Brand not found !");
    return;
  }
 
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  this.styleColumn = null;
  
  if( this.where.indexOf("TUR") >= 0 ) this.styleColumn = "TUR";
  else
  if( this.where.indexOf("BRAND_ID") >= 0 || this.where.indexOf("BRAND_NAME") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
  if( this.where.indexOf("SUB_TYPE") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
    ;
 
  if( OM.notNull(this.thmLayer) ) {
    this.mapview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  
  var style = new OM.style.Color({fill:"#ff2244", fillOpacity:0.5, stroke:"#ff2244" }); 
  
  if( Math.abs((new Date()).getTime() - gtm) < 86413436 ) {
    
	var baseQuery = "SELECT GEOLOC," + this.keyColumn + (this.styleColumn ? "," + this.styleColumn : "")  + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + trans2xmlstr(this.where));
					
    var thm = new OM.layer.VectorLayer(this.id, {renderingStyle: style,def:{type:OM.layer.VectorLayer.TYPE_JDBC, dataSource:datasrcApp, sql:baseQuery, url: urlBase, geometryColumn:"GEOLOC", loadOnDemand:true, keyColumn: this.keyColumn}});
	 
	var  hoverStyle = new OM.style.Marker({vectorDef : [{shape: {type:"circle", cx:5, cy:5, width:10, height:10},style: {fill:"#ff2244", stroke:"#ff2244", fillOpacity:0.5}}], width: 30, height:30});
	
	thm.setHoverStyle(hoverStyle);
	thm.setVisible(visible);
	
	var tStyle  = new OM.style.Text({styleName:"textStyle",fill:"#ffffff"});
	
    if(OM.isNull(variableMarker)) variableMarker = new OM.style.VariableMarker({classification:"logarithmic", marker:new OM.style.Marker({textStyle:tStyle,vectorDef: [{shape: {type: "circle", cx: 5, cy: 5, width: 10, height: 10}, style: {fill: "#ff0000 ", stroke: "#ffffff"}}]}), startSize:20, increment:5, numClasses:10});	  
    
    this.mapview.addLayer(thm);
    
	var layer = this.mapview.getLayerByName(this.id);
  
	if( OM.isNull(minPointCount) ) minPointCount = 3;
    if( OM.isNull(maxClusteringLevel) ) maxClusteringLevel = 12;
    if( OM.isNull(threshold) ) threshold = 40;

	layer.enableClustering(true,{clusterStyle:variableMarker,minPointCount:minPointCount, maxClusteringLevel:maxClusteringLevel, threshold:threshold});			
				
	if(OM.notNull(mouseclick)) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, mouseclick);
	
    this.thmLayer = thm;
  }
  
  return;
} // createClusterBrandLayer()

ILayer.prototype.createCategoryLayer=function(typ, id, sym, visible, mouseclick) {
	
  this.typ = typ;
  this.id = id;
  this.tableName = convCategory2Table(typ);
  
  if( OM.isNull(this.tableName) ) this.tableName = "POI";
  
  this.keyColumn = "ID";
  this.hiddenColumn = "STANDARD_NAME";
  this.where = convCategory2Where(typ);
  
  if( OM.isNull(this.where) ) {
    alert("Category not found !");
    return;
  }
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;
  
  var myconfig = new OM.layer.TileLayerConfig({tileImageWidth: 256, tileImageHeight: 256});
    
  var myuniv = this.mapview.getUniverse();
  
  this.styleColumn = null;
  
  if( this.where.indexOf("TUR") >= 0 ) this.styleColumn = "TUR";
  else
  if( this.where.indexOf("BRAND_ID") >= 0 || this.where.indexOf("BRAND_NAME") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
  if( this.where.indexOf("SUB_TYPE") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
    ;
	
  if( OM.notNull(this.thmLayer) ) {
    mview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }

  if( OM.isNull(sym)) {
    if( this.styleColumn == "TUR" ) sym = "V.MAP_POI_CATEGORY" ;
    else
    if( this.styleColumn == "BRAND_SEMBOL_NO" ) sym = "V.MAP_POI_BRAND" ;
    else
      ;
  }  

  
  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
    var baseQuery = "SELECT GEOLOC," + this.hiddenColumn + "," + this.keyColumn + (this.styleColumn ? "," + this.styleColumn : "")  + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where);
    
	var themeJDBC = new OM.server.ServerJDBCTheme(id + "_THEME");
		
    themeJDBC.setQuery(baseQuery);
    themeJDBC.setSRID('8307');
    themeJDBC.setGeometryColumnName("GEOLOC");
	var render_style = sym ? (sym.name ? sym.name : (sym.sym ? (sym.sym.name ? sym.sym.name : sym.sym) : sym)) : "M.RED_PIN";
    themeJDBC.setRenderingStyleName(render_style);
    themeJDBC.setDataSourceName(datasrcApp);
	themeJDBC.setRenderingStyleValueColumns(this.styleColumn);
	themeJDBC.addInfoColumn({column: this.keyColumn, name:this.keyColumn});
	themeJDBC.addInfoColumn({column: this.hiddenColumn, name:this.hiddenColumn});
 
	var req = new OM.server.ServerMapRequest(urlBase);

    req.setProperties({dataSource:datasrcApp, transparent:true, antialiase:"false"});
    req.addTheme(themeJDBC);
	req.addStyle(sym);
	
    var dtl_props = {  // dtl specific properties
        dataSource:datasrcApp, // if not provided, it comes from ServerMapRequest;
        universe: myuniv,    
        tileLayerConfig: myconfig,
        tileServerURL: urlBase + "/omserver", //default is ServerMapRequest's baseURL + '/omserver'
        enableUTFGrid: true,
        enableUTFGridInfoWindow: true,
        utfGridResolution: 4
    };
	
    var thm = new OM.layer.DynamicTileLayer(id + "_LyerCategory", dtl_props, req);
			
	if( OM.notNull(mouseclick) ) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, function(obj){
      var pos = mview.getCursorLocation();
      pos = pos.transform(8307);
      var p = { "x": pos.getX(), "y": pos.getY() };
      var x = { data: [] };
      var lyrAttr = thm.getFeatureInfo();
      var attrValues = lyrAttr[Object.keys(lyrAttr)[0]];	 				 
      for (var key in attrValues) {
        if (attrValues.hasOwnProperty(key)) {
          var data = {name: key, value: attrValues[key]};
          x.data.push(data);
        }
      }		 
      return mouseclick(p,x);
    });
	thm.setVisible(visible);
	
    mview.addLayer(thm);
    this.thmLayer = thm;
  }
  
  return;
} // createCategoryLayer()

ILayer.prototype.createClusterCategoryLayer=function(typ, id, sym, visible, variableMarker, minPointCount, maxClusteringLevel, threshold, mouseclick) {
 
  this.typ = typ;
  this.id = id;
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;
  
  this.tableName = convCategory2Table(typ);
  
  if( OM.isNull(this.tableName) ) this.tableName = "POI";
  
  this.keyColumn = "ID";
  this.where = convCategory2Where(typ);
  
  if( OM.isNull(this.where) ) {
    alert("Category not found !");
    return;
  }
  
  this.styleColumn = null;
  
  if( this.where.indexOf("TUR") >= 0 ) this.styleColumn = "TUR";
  else
  if( this.where.indexOf("BRAND_ID") >= 0 || this.where.indexOf("BRAND_NAME") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
  if( this.where.indexOf("SUB_TYPE") >= 0 ) this.styleColumn = "BRAND_SEMBOL_NO";
  else
    ;
	
  if( OM.notNull(this.thmLayer) ) {
    mview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  
  var style = new OM.style.Color({fill:"#ff2244", fillOpacity:0.5, stroke:"#ff2244" }); 
  
  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
   
    var baseQuery = "SELECT GEOLOC," + this.keyColumn + (this.styleColumn ? "," + this.styleColumn : "")  + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where);
    
	var thm = new OM.layer.VectorLayer(this.id, {renderingStyle: style, def:{type:OM.layer.VectorLayer.TYPE_JDBC, dataSource:datasrcApp, sql:baseQuery, url: urlBase, geometryColumn:"GEOLOC", loadOnDemand:true, keyColumn: this.keyColumn}});
	
	var  hoverStyle = new OM.style.Marker({vectorDef : [{shape: {type:"circle", cx:5, cy:5, width:10, height:10}, style: {fill:"#ff2244", stroke:"#ff2244", fillOpacity:0.5}}], width: 30, height:30});
	
	thm.setHoverStyle(hoverStyle);
	
    if(OM.isNull(variableMarker)) variableMarker = new OM.style.VariableMarker({classification:"logarithmic", marker:new OM.style.Marker({vectorDef: [{shape: {type: "circle", cx: 5, cy: 5, width: 10, height: 10}, style: {fill: "#ff0000 ", stroke: "#ffffff"}}]}), textStyle:"#ffffff", startSize:20, increment:5, numClasses:10});	  
	
	thm.setVisible(visible);
 
    this.mapview.addLayer(thm);
    var layer = this.mapview.getLayerByName(this.id);
   
    if( OM.isNull(minPointCount) ) minPointCount = 3;
    if( OM.isNull(maxClusteringLevel) ) maxClusteringLevel = 19;
    if( OM.isNull(threshold) ) threshold = 40;
	
	layer.enableClustering(true,{clusterStyle:variableMarker,minPointCount:minPointCount, maxClusteringLevel:maxClusteringLevel, threshold:threshold});		

	if(OM.notNull(mouseclick)) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, mouseclick);
    
    this.thmLayer = thm;
  }
  return;
} // createClusterCategoryLayer()

ILayer.prototype.createHeatmapLayer=function(typ, id, thmtyp, where, visible, colorStops) {
	
  this.typ = typ;
  this.thmtyp = thmtyp;
  this.id = id;
  this.where = where;
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;

  switch( thmtyp ) {
	  
    case ILayer.USER_POINT :
      this.keyColumn = "POINT_ID";
      this.tableName = "LBS_USER_POINT";
      
	  if( OM.isNull(this.where) || this.where.length <= 0 ) 
	    this.where = "KEY='" + lboxKey + "'";
      else
        this.where = "KEY='" + lboxKey + "' AND " + this.where;
    
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

  if( OM.notNull(this.thmLayer) ) {
    mview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86403236 ) {
    
	var baseQuery;
    
	if( this.typ == ILayer.CAMP_CATEGORY || this.typ == ILayer.CAMP_CAMPAIGN )
      baseQuery = "SELECT GEOLOC," + this.keyColumn + " FROM POI WHERE ID IN (SELECT DISTINCT ID FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where) + ")";
    else
      baseQuery = "SELECT GEOLOC," + this.keyColumn + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where);

    var thm = new OM.layer.VectorLayer(this.id + "_THEME", {def:{type:OM.layer.VectorLayer.TYPE_JDBC, dataSource:datasrcApp, sql:baseQuery, url: urlBase, geometryColumn:"GEOLOC", loadOnDemand:false, keyColumn: this.keyColumn}});
	
	if ( OM.isNull(colorStops) ) colorStops = ['#009900','#FFFFCC','#FFFF66','#FFFF33','#FF9933','#FF6600','#FF3333','#FF0000'] ;
	
	var heatMap = new OM.style.HeatMap({styleName: this.id, spotlightRadius:10, colorStops:colorStops, opacity:0.5, lengthUnit:"kilometer", sampleFactor:1,
//	containerVectorLayer: new OM.layer.VectorLayer(this.id + "heatmap container", {def: {type:OM.layer.VectorLayer.TYPE_JDBC, dataSource: datasrcApp, sql: "SELECT GEOLOC FROM IL", }})
	loadOnDemand:true });	 
	
    thm.setRenderingStyle(heatMap);	

    thm.setVisible(visible);
    thm.setLabelsVisible(true);
    mview.addLayer(thm);	
	
	this.thmLayer = thm;	
  }
  return;
} // createHeatmapLayer()
  
ILayer.prototype.createBrandHeatmapLayer=function(typ, id, brand, visible, colorStops) {
  
  this.typ = typ;
  this.id = id;
  this.tableName = "POI";
  this.keyColumn = "ID";
  this.where = convBrand2Where(brand);
  
  if( OM.isNull(this.where) ) {
    alert("Brand not found !");
    return;
  }
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;
  
  if( OM.notNull(this.thmLayer) ) {
    mview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  
  if( Math.abs((new Date()).getTime() - gtm) < 86413436 ) {
	  
    var baseQuery = "SELECT GEOLOC," + this.keyColumn + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where);

	var thm = new OM.layer.VectorLayer(this.id + "_THEME", {def:{type:OM.layer.VectorLayer.TYPE_JDBC, dataSource:datasrcApp, sql:baseQuery, url: urlBase, geometryColumn:"GEOLOC", loadOnDemand:false, keyColumn: this.keyColumn}});
    
	if ( OM.isNull(colorStops) ) colorStops = ['#009900','#FFFFCC','#FFFF66','#FFFF33','#FF9933','#FF6600','#FF3333','#FF0000'] ;
    
	var heatMap = new OM.style.HeatMap({styleName: this.id, spotlightRadius:50, colorStops:colorStops, opacity:0.8, lengthUnit:"pixel", sampleFactor:4, containerVectorLayer: new OM.layer.VectorLayer(this.id + "heatmap container", {def: {type:OM.layer.VectorLayer.TYPE_JDBC, dataSource: datasrcApp, sql: "SELECT GEOLOC FROM IL", loadOnDemand:true}})});	 

    thm.setRenderingStyle(heatMap);
	thm.setVisible(visible);
    mview.addLayer(thm);
	this.thmLayer = thm;
 }
  return;
} // createBrandHeatmapLayer()

ILayer.prototype.createCategoryHeatmapLayer=function(typ, id, category, visible, colorStops) {
	
  this.typ = typ;
  this.id = id;
  this.tableName = "POI";
  this.keyColumn = "ID";
  this.where = convCategory2Where(category);
  
  if( OM.isNull(this.where) ) {
    alert("Category not found !");
    return;
  }
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var mview = this.mapview;

  if( OM.notNull(this.thmLayer) ) {
    mview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  
  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
	  
    var baseQuery = "SELECT GEOLOC," + this.keyColumn + " FROM " + this.tableName + (!this.where || this.where.length <= 0 ? "" : " WHERE " + this.where);
  
   	var thm = new OM.layer.VectorLayer(this.id + "_THEME", {def:{type:OM.layer.VectorLayer.TYPE_JDBC, dataSource:datasrcApp, sql:baseQuery, url: urlBase, geometryColumn:"GEOLOC", loadOnDemand:false, keyColumn: this.keyColumn}});
    
	if ( OM.isNull(colorStops) ) colorStops = ['#009900','#FFFFCC','#FFFF66','#FFFF33','#FF9933','#FF6600','#FF3333','#FF0000'] ;

	var heatMap = new OM.style.HeatMap({styleName: this.id, spotlightRadius:50, colorStops:colorStops, opacity:0.8, lengthUnit:"pixel", sampleFactor:4, containerVectorLayer: new OM.layer.VectorLayer(this.id + "heatmap container", {def: {type:OM.layer.VectorLayer.TYPE_JDBC, dataSource: datasrcApp, sql: "SELECT GEOLOC FROM IL", loadOnDemand:true}})});	 

    thm.setRenderingStyle(heatMap);	
	thm.setVisible(visible);
	
    mview.addLayer(thm);
    this.thmLayer = thm;  
  }
  return;
} // createCategoryHeatmapLayer()

ILayer.prototype.enableClusterVectorLayer=function(id, variableMarker, minPointCount, maxClusteringLevel, threshold){
	
  if( OM.isNull(minPointCount) ) minPointCount = 5;
  if( OM.isNull(maxClusteringLevel) ) maxClusteringLevel = 8;
  if( OM.isNull(threshold) ) threshold = 60;

  if(OM.isNull(variableMarker)) variableMarker = new OM.style.VariableMarker({classification:"logarithmic", marker:new OM.style.Marker({ vectorDef: [{shape: {type: "circle", cx: 5, cy: 5, width: 10, height: 10}, style: {fill: "#A5DF00", stroke: "#000000"}}]}), startSize:20, increment:5, numClasses:10});
 
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;

  var thm = this.mapview.getLayerByName(id);

  thm.enableClustering(true, {clusterStyle:variableMarker, minPointCount:minPointCount, maxClusteringLevel:maxClusteringLevel, threshold:threshold}); 
	
  thm.redraw(); 
 
  return;
} // enableClusterVectorLayer()


ILayer.prototype.enableClusterLayer=function(id, variableMarker, minPointCount, maxClusteringLevel, threshold){
	
  if( OM.isNull(minPointCount) ) minPointCount = 5;
  if( OM.isNull(maxClusteringLevel) ) maxClusteringLevel = 8;
  if( OM.isNull(threshold) ) threshold = 60;

  if(OM.isNull(variableMarker)) variableMarker = new OM.style.VariableMarker({classification:"logarithmic", marker:new OM.style.Marker({ vectorDef: [{shape: {type: "circle", cx: 5, cy: 5, width: 10, height: 10}, style: {fill: "#A5DF00", stroke: "#000000"}}]}), startSize:20, increment:5, numClasses:10});
 
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;

  var thm = this.mapview.getLayerByName(id);

  thm.enableClustering(true, {clusterStyle:variableMarker, minPointCount:minPointCount, maxClusteringLevel:maxClusteringLevel, threshold:threshold}); 
	
  thm.redraw(); 
 
  return;
} // enableClusterVectorLayer()

ILayer.prototype.setHeatMapStyle=function(id, spotlightRadius, colorStops, opacity, lengthUnit, sampleFactor){

  var heatStateSql = "SELECT GEOLOC FROM IL";

  if( OM.isNull(spotlightRadius) ) spotlightRadius = 100;
  if( OM.isNull(colorStops) ) colorStops = ["#FFFFFF","#ffff00", "#ff8800", "#ff0000"];
  if( OM.isNull(opacity) ) opacity = 0.5;
  if( OM.isNull(lengthUnit) ) lengthUnit = "mile";
  if( OM.isNull(sampleFactor) ) sampleFactor = 8;
  
  var heatMap = new OM.style.HeatMap({styleName: id, spotlightRadius:spotlightRadius, colorStops:colorStops, opacity:opacity, lengthUnit:lengthUnit, sampleFactor:sampleFactor, containerVectorLayer: new OM.layer.VectorLayer(this.id,{def: {type:OM.layer.VectorLayer.TYPE_JDBC, dataSource: datasrcApp, sql: heatStateSql, loadOnDemand:true}})});
 
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var thm = this.mapview.getLayerByName(this.id);
  thm.setRenderingStyle(heatMap);
  thm.redraw();
	
  return;
} // setHeatMapStyle()



ILayer.prototype.setNSDP = function (id, keyColumn, xml_data ) {
	
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;  
  
  var thm = this.mapview.getLayerByName(id);
  
  thm.setNSDP({keyColumn:keyColumn,xml:xml_data});
  thm.redraw();
	
  return;	
}//setNSDP()

ILayer.prototype.setVisualFilter = function (id, sym ) {
	
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;    
  
  var thm = this.mapview.getLayerByName(id);
  
  thm.setVisualFilter(sym);
  thm.redraw();
	
  return;	
}//setVisualFilter()

ILayer.prototype.setPreDefRenderingStyle = function (id, sym ) {
	
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var thm = this.mapview.getLayerByName(id);
  
  thm.setRenderingStyle(sym);
  thm.redraw();
	
  return;	
}//setPreDefRenderingStyle()

ILayer.prototype.selectFeatureByFilter = function (id, filter ) {
	
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var thm = this.mapview.getLayerByName(id);
  
  thm.selectFeatureByFilter(filter);
 
	
  return;	
}//selectFeatureByFilter()

ILayer.prototype.clearSelectedFeatures = function (id) {
	
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var thm = this.mapview.getLayerByName(id);
  
  thm.clearSelectedFeatures();
  
	
  return;	
}//clearSelectedFeatures()

ILayer.prototype.setSelectStyle = function (id, style) {
	
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var thm = this.mapview.getLayerByName(id);
  
  thm.setSelectStyle(style);
  
	
  return;	
}//setSelectStyle()



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
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
 
  if( OM.notNull(this.thmLayer) ) {
    this.mapview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  var myconfig = new OM.layer.TileLayerConfig({tileImageWidth: 256, tileImageHeight: 256});
    
  var myuniv = this.mapview.getUniverse();

  if( Math.abs((new Date()).getTime() - gtm) < 86402336 ) {
	
	var themePredefined = new OM.server.ServerPredefinedTheme(this.tableName);
    var req = new OM.server.ServerMapRequest(urlBase);
	
	// 2.2 set properties
    req.setProperties({dataSource:datasrcApp, transparent:true, antialiase:"false"});

        // 2.3 add themes and styles
    req.addTheme(themePredefined);
		
	var dtl_props = {  // dtl specific properties
            //dataSource:datasrcApp, // if not provided, it comes from ServerMapRequest;
            universe: myuniv,    
            tileLayerConfig: myconfig,
            tileServerURL: urlBase + "/omserver", //default is ServerMapRequest's baseURL + '/omserver'
            enableUTFGrid: true,
            enableUTFGridInfoWindow: true,
			//infoWindowTrigger : OM.event.MouseEvent.MOUSE_OVER,
            utfGridResolution: 12
    };
    
	var thm = new OM.layer.DynamicTileLayer(this.id + "layerPredefined", dtl_props, req);

    this.mapview.addListener(OM.event.MouseEvent.MOUSE_CLICK, dynLayerClick);
	thm.setVisible(visible);
	
    this.mapview.addLayer(thm);
    this.thmLayer = thm;
  }
  return;
} // createDemographicLayer()

ILayer.prototype.createThemeLayer=function(name, id, params, visible, mouseclick, mouserclick) {

  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  var thm = new OM.layer.VectorLayer(id, { def: { type: OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource: datasrcApp, theme: name, loadOnDemand: true, jdbcSrid:iJdbcSrid } });
  
  thm.enableInfoWindow(!mouseclick);
  thm.setVisible(visible);
  
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
  thm.setLabelsVisible(true);
  if( mouseclick ) thm.addListener(OM.event.MouseEvent.MOUSE_CLICK, mouseclick);
  if( mouserclick ) thm.addListener(OM.event.MouseEvent.MOUSE_RIGHT_CLICK, mouseclick);
  
  this.mapview.addLayer(thm);
  this.thmLayer = thm;
  
  return;
}

function dynLayerClick(evt){
	
  var layers = evt.target.divLayers;

  var clickedLayer = null;
  
  if(layers){
	  
    for(var k = layers.length - 1; k > 0; k-- ){
		
      if(layers[k].visible){
        clickedLayer = layers[k];
        break;
      }
    }
  }
  
  if(clickedLayer != null){
    var json = clickedLayer.getFeatureInfo();
    if ( OM.notNull(json) && JSON.stringify(json) != "{}"){
      showInfoWindow(json);
    }
  }
  
  return;
}//dynLayerClick()

function showInfoWindow(json){
    
  var attname = "";
    
	for (var name in json) {
      
	  if (json.hasOwnProperty(name)) {
        attname = name;
        break;
      }
    }
   
  state = json[attname];

  var attrState=OM.isNull(state)?"": JSON.stringify(state);
  
  if( this.mapview == undefined || this.mapview == null ) this.mapview = mapviewGlobal;
  
    var content= "";
    var count=0;
    
	if ( OM.notNull(attrState) && attrState.length > 0) {
      
	  if (content === "") {
        content = "<table align='left' cellspacing='4' style='margin: auto;border-collapse: separate;'>";
      }
                    
      var parsed = JSON.parse(attrState);
      var value = [];
      var name = [];
    
      for(var x in parsed){
        value.push(parsed[x]);
        name.push(x);
      }
    
      for ( var i = 0; value.length > i; i++ ) {
        
	    var pVal = value[i];
        
	    if(!pVal || pVal == null || pVal == "null"){
          pVal = "-";
        } else{
            pVal = noExponents(pVal);
          }
        
	    content += "<tr bgcolor='#dddddd'>" + "<td align='left'>" + name[i] + "</td>" + "<td align='left'>" + pVal + "</td></tr>";
      }
        
		count++;
    }
    
  var options={width:330, height:40 + (count * 90), title:"INFO", "infoWindowStyle":{"background-color":"#F5F5F9"}, "titleStyle":{"background-color":"#A5A5A9","font-size":"16px","font-family":"Tahoma"}, "contentStyle":{"background-color":"#F5F5F9","font-size":"16px","font-family":"Tahoma"}, "tailStyle":{"offset":"25","background-color":"#30BB30"}}
  
  content += "</table>";
  
  var point = this.mapview.getCursorLocation();
	
  this.mapview.displayInfoWindow(point, content, options);

  return;
}//showInfoWindow()

function noExponents(data){
    
  var txData = data + "";
  
  if(txData.length <= 1){
      return data;
  }
  
  var splitData = txData.split(/[eE]/);
  
  if(splitData.length == 1 || isNaN(splitData[0])) return data;

  var  z = '', sign = this < 0 ? '-' : '',
  str = splitData[0].replace('.', ''),
  mag = Number(splitData[1])+ 1;

  if( mag < 0 ){
    z = sign + '0.';
    while ( mag++ ) z += '0';
    return z + str.replace(/^\-/,'');
  }
  
  mag -= str.length;  
  while( mag-- ) z += '0';
  return str + z;
  
}//noExponents()

 ILayer.prototype.removeLayer=function() {
  if( OM.isNull(this.mapview) ) {
    this.mapview = mapviewGlobal;
  }
  
  if(  OM.notNull(this.thmLayer) ) {
    this.mapview.removeLayer(this.thmLayer);
  }
  return;
}//removeLayer()  

ILayer.prototype.removeUserPointHeatMap = function(){
  
  if( OM.isNull(this.mapview) ) {
    this.mapview = mapviewGlobal;
  }
    
  if( OM.notNull(this.thmLayer) ) {
    this.mapview.removeLayer(this.thmLayer);
	this.mapview.removeLayer(this.thmUserPoints);
  }

}//removeUserPointHeatMap()

ILayer.prototype.setVisible=function(visible) {  
  if( this.thmLayer ) this.thmLayer.setVisible(visible);
  return;
}//setVisible()

ILayer.prototype.refresh=function() {
  if( this.thmLayer ) this.thmLayer.refresh();
  return;
}//refresh()

ILayer.prototype.zoomToLayer=function() {
  if( this.thmLayer ) this.thmLayer.zoomToTheme();
  return;
}//zoomToLayer()

function vectorLayerTooltip(feature) { 
 
  var str = '';
  
  for (var p in feature.attributes) {
    if (feature.attributes.hasOwnProperty(p)) {
      str += p + ':' + feature.attributes[p] + '\n';
    }
  }

  return str;
 
}//vectorLayerTooltip()