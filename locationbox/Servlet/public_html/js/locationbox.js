function IAnalysis(ab){if(!ab){this.mapview=mapviewGlobal;}
else{this.mapview=ab.mapview;}
this.typ=null;this.thmAnalysis=null;this.pieStyle=null;}
IAnalysis.prototype.createBarAnalysis=function(bb,cb,db){if(!this.mapview){this.mapview=mapviewGlobal;}
var eb=this.mapview;this.typ=bb;if(this.thmAnalysis)eb.removeThemeBasedFOI(this.thmAnalysis);if(Math.abs((new Date()).getTime()-gtm)<86447562){var fb="SELECT GEOLOC,"+this.typ+"_ID FROM "+(this.typ!="REGION"?this.typ:"LBS_USER_REGION WHERE KEY = '"+lboxKey+"'");var gb="<themes><theme name='"+this.typ+"' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.typ+"_ID' "+"render_style='C.DUMMY' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";var hb;if(db)hb=new MVThemeBasedFOI(this.typ+"_BAR_THEME",datasrcApp+"."+this.typ);else hb=new MVThemeBasedFOI(this.typ+"_BAR_JDBC_THEME",gb);hb.setBringToTopOnMouseOver(true);hb.enableAutoWholeImage(true,50,6000);hb.enableImageCaching(false);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;for(var i=0;i<jb.attrs.length;i++){var name=jb.attrnames[i];var value=jb.attrs[i];x.data.push({"name":name,"value":value}
);}
cb(p,x);}
);}
eb.addThemeBasedFOI(hb);lb=new MVNSDP("defaultNSDP");lb.setTheme(this.typ);lb.setKeyColumn(this.typ+"_ID");var mb="<nsdp_xml><table><tr><th>K</th><th>I</th></tr></table></nsdp_xml>";var nb=new Object();nb["xml"]=mb;lb.setParameters(nb);hb.setNSDP(lb);hb.refresh();this.thmAnalysis=hb;}
return;}
IAnalysis.prototype.createPieAnalysis=function(bb,cb,db){if(!this.mapview){this.mapview=mapviewGlobal;}
var eb=this.mapview;this.typ=bb;if(this.thmAnalysis!=null)eb.removeThemeBasedFOI(this.thmAnalysis);if(Math.abs((new Date()).getTime()-gtm)<86447562){var fb="SELECT GEOLOC,"+this.typ+"_ID FROM "+(this.typ!="REGION"?this.typ:"LBS_USER_REGION WHERE KEY = '"+lboxKey+"'");var gb="<themes><theme name='"+this.typ+"' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.typ+"_ID' "+"render_style='C.DUMMY' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";if(db)hb=new MVThemeBasedFOI(this.typ+"_PIE_THEME",datasrcApp+"."+this.typ);else hb=new MVThemeBasedFOI(this.typ+"_PIE_JDBC_THEME",gb);hb.setBringToTopOnMouseOver(true);hb.enableAutoWholeImage(true,50,6000);hb.enableImageCaching(false);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;for(var i=0;i<jb.attrs.length;i++){var name=jb.attrnames[i];var value=jb.attrs[i];x.data.push({"name":name,"value":value}
);}
cb(p,x);}
);}
eb.addThemeBasedFOI(hb);lb=new MVNSDP("defaultNSDP");lb.setTheme(this.typ);lb.setKeyColumn(this.typ+"_ID");var mb="<nsdp_xml><table><tr><th>K</th><th>I</th></tr></table></nsdp_xml>";var nb=new Object();nb["xml"]=mb;lb.setParameters(nb);hb.setNSDP(lb);hb.refresh();this.thmAnalysis=hb;}
return;}
IAnalysis.prototype.createColoringAnalysis=function(bb,sym,cb,db){if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;this.typ=bb;if(this.thmAnalysis!=null)eb.removeThemeBasedFOI(this.thmAnalysis);if(!cb&&ob(sym)){cb=sym;sym=null;}
if(Math.abs((new Date()).getTime()-gtm)<86498263){var fb="SELECT GEOLOC,"+this.typ+"_ID FROM "+(this.typ!="REGION"?this.typ:"LBS_USER_REGION WHERE KEY = '"+lboxKey+"'");var gb="<themes><theme name='"+this.typ+"' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.typ+"_ID' "+"render_style='"+(sym?(sym.name?sym.name:(sym.sym?(sym.sym.name?sym.sym.name:sym.sym):sym)):"C.DUMMY")+"' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";var hb;if(db)hb=new MVThemeBasedFOI(this.typ+"_COLOR_THEME",datasrcApp+"."+this.typ);else hb=new MVThemeBasedFOI(this.typ+"_COLOR_JDBC_HEME",gb);hb.setBringToTopOnMouseOver(true);hb.enableAutoWholeImage(true,50,6000);hb.enableImageCaching(false);if(sym){if(sym.name)hb.addStyle(sym);else
if(sym.sym){for(var i=0;i<sym.symlist.length;i++){var data=sym.symlist[i];if(data.sym.name)hb.addStyle(data.sym);}
hb.addStyle(sym.sym);}
}
if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;for(var i=0;i<jb.attrs.length;i++){var name=jb.attrnames[i];var value=jb.attrs[i];x.data.push({"name":name,"value":value}
);}
cb(p,x);}
);}
eb.addThemeBasedFOI(hb);lb=new MVNSDP("defaultNSDP");lb.setTheme(this.typ);lb.setKeyColumn(this.typ+"_ID");var mb="<nsdp_xml><table><tr><th>K</th><th>C</th></tr></table></nsdp_xml>";var nb=new Object();nb["xml"]=mb;lb.setParameters(nb);hb.setNSDP(lb);if(!sym)hb.setRenderingStyle("V.ANALIZ_COLORS");else{if(sym.name)hb.setRenderingStyle(sym.name);else
if(sym.sym)hb.setRenderingStyle(sym.sym.name);}
hb.setVisible(true);hb.refresh();this.thmAnalysis=hb;}
return;}
IAnalysis.prototype.removeAnalysis=function(){if(this.mapview==null){this.mapview=mapviewGlobal;}
if(this.thmAnalysis)this.mapview.removeThemeBasedFOI(this.thmAnalysis);return;}
IAnalysis.prototype.setVisible=function(pb){if(!this.mapview){this.mapview=mapviewGlobal;}
if(this.thmAnalysis)this.thmAnalysis.setVisible(pb);return;}
IAnalysis.prototype.setAnalysisBarStyle=function(qb){var rb=' <AdvancedStyle><BarChartStyle width="'+qb.width+'" height="'+qb.height+'" share_scale="false" min_value="0.0" max_value="1000000"> ';for(var i=0;i<qb.bars.length;i++){rb+='<Bar name="'+qb.bars[i].name+'" color="'+qb.bars[i].color+'" /> ';}
rb+='</BarChartStyle ></AdvancedStyle>';var hb=this.thmAnalysis;if(this.barStyle)hb.deleteStyle(this.barStyle);var nb=new MVXMLStyle(this.typ+"BARSTYLE",rb);hb.addStyle(nb);hb.setRenderingStyle(this.typ+"BARSTYLE");this.barStyle=nb;return;}
IAnalysis.prototype.setAnalysisBarData=function(sb){var nb=new Object();nb["xml"]="<nsdp_xml>"+sb+"</nsdp_xml>";var lb=new MVNSDP("defaultNSDP");lb.setTheme(this.typ);lb.setKeyColumn(this.typ+"_ID");lb.setParameters(nb);var hb=this.thmAnalysis;hb.setNSDP(lb);hb.refresh();return;}
IAnalysis.prototype.setAnalysisPieStyle=function(qb){var rb='<AdvancedStyle><PieChartStyle pieradius="'+qb.radius+'">';for(var i=0;i<qb.pies.length;i++){rb+='<PieSlice name="'+qb.pies[i].name+'" color="'+qb.pies[i].color+'" />'}
rb+='</PieChartStyle></AdvancedStyle>';var hb=this.thmAnalysis;if(this.pieStyle)hb.deleteStyle(this.pieStyle);var nb=new MVXMLStyle(this.typ+"PIESTYLE",rb);hb.addStyle(nb);hb.setRenderingStyle(this.typ+"PIESTYLE");this.pieStyle=nb;return;}
IAnalysis.prototype.setAnalysisPieData=function(sb){var nb=new Object();nb["xml"]="<nsdp_xml>"+sb+"</nsdp_xml>";var lb=new MVNSDP("defaultNSDP");lb.setTheme(this.typ);lb.setKeyColumn(this.typ+"_ID");lb.setParameters(nb);var hb=this.thmAnalysis;hb.setNSDP(lb);hb.refresh();return;}
IAnalysis.prototype.setAnalysisColoringData=function(sb){var lb=new MVNSDP("defaultNSDP");lb.setTheme(this.typ);lb.setKeyColumn(this.typ+"_ID");var nb=new Object();nb["xml"]="<nsdp_xml>"+sb+"</nsdp_xml>";lb.setParameters(nb);var hb=this.thmAnalysis;hb.setNSDP(lb);hb.refresh();return;}
ILayer.MAP_BOLGE="MAP_BOLGE";ILayer.MAP_IL="MAP_IL";ILayer.MAP_ILCE="MAP_ILCE";ILayer.MAP_MAHALLE="MAP_MAHALLE";ILayer.USER_POINT="USER_POINT";ILayer.USER_REGION="USER_REGION";ILayer.CAMP_CATEGORY="CAMP_CATEGORY";ILayer.CAMP_CAMPAIGN="CAMP_CAMPAIGN";ILayer.DEAL_CATEGORY="DEAL_CATEGORY";ILayer.DEAL_DEAL="DEAL_DEAL";ILayer.PREDEFINED_THEME="PREDEFINED THEME";ILayer.MAP_ROUTE="MAP_ROUTE";ILayer.MAP_YOL="MAP_YOL";ILayer.HEATMAP_ULKE="HEATMAP_ULKE";ILayer.HEATMAP_IL="HEATMAP_IL";ILayer.HEATMAP_ILCE="HEATMAP_ILCE";ILayer.HEATMAP_MAHALLE="HEATMAP_MAHALLE";ILayer.DEMOGRAFIK_YAS_DURUMU="YAS_DURUMU";ILayer.DEMOGRAFIK_NUFUS="NUFUS";ILayer.DEMOGRAFIK_EGITIM="EGITIM";ILayer.DEMOGRAFIK_KONUT_SAYISI="KONUT_SAYISI";ILayer.DEMOGRAFIK_ARAC_SAYISI="ARAC_SAYISI";function ILayer(ab){if(!ab){this.mapview=mapviewGlobal;}
else{this.mapview=ab.mapview;}
this.typ=null;this.thmtyp=null;this.id=null;this.keyColumn=null;this.styleColumn=null;this.labelColumn=null;this.labelStyle=null;this.tableName=null;this.where=null;this.thmLayer=null;}
ILayer.prototype.createLayer=function(bb,id,tb,ub,sym,pb,cb,vb,wb,xb){this.typ=bb;this.id=id;this.where=tb;this.labelColumn=vb;this.labelStyle=wb;if(!this.labelStyle)this.labelStyle="T.LABEL";if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;switch(bb){case ILayer.MAP_BOLGE:this.keyColumn="BOLGE_ID";this.styleColumn="BOLGE_ID";if(ub)this.styleColumn=ub;this.tableName="BOLGE";this.hiddenColumn="BOLGE_ADI";this.hiddenInfo="<field column='BOLGE_ADI' name='BOLGE' />";break;case ILayer.MAP_IL:this.keyColumn="IL_ID";this.styleColumn="IL_ID";if(ub)this.styleColumn=ub;this.tableName="IL";if(!this.where||this.where.length<=0)this.where="IL_ID <> 100";else
this.where="IL_ID <> 100 AND "+this.where;this.hiddenColumn="IL_ADI";this.hiddenInfo="<field column='IL_ADI' name='IL' />";break;case ILayer.MAP_ILCE:this.keyColumn="ILCE_ID";this.styleColumn="ILCE_ID";if(ub)this.styleColumn=ub;this.tableName="ILCE";this.hiddenColumn="ILCE_ADI";this.hiddenInfo="<field column='ILCE_ADI' name='ILCE' />";break;case ILayer.MAP_MAHALLE:this.keyColumn="MAHALLE_ID";this.styleColumn="MAHALLE_ID";if(ub)this.styleColumn=ub;this.tableName="MAHALLE";if(!this.where||this.where.length<=0)this.where="TYPE=3";else
this.where=this.where+" AND TYPE=3";this.hiddenColumn="MAHALLE_ADI";this.hiddenInfo="<field column='MAHALLE_ADI' name='MAHALLE' />";break;case ILayer.USER_POINT:this.keyColumn="POINT_ID";this.styleColumn="TYP";if(ub)this.styleColumn=ub;this.tableName="LBS_USER_POINT";if(!this.where||this.where.length<=0)this.where="KEY='"+lboxKey+"'";else
this.where="KEY='"+lboxKey+"' AND "+this.where;if(!sym)sym="M.RED_PIN";this.hiddenColumn="POINT_NAME";this.hiddenInfo="<field column='POINT_NAME' name='NAME' />";if(xb){for(var i=0;i<xb.length;i++){this.hiddenColumn+=","+xb[i];this.hiddenInfo+="<field column='"+xb[i]+"' name='"+xb[i]+"' />";}
}
break;case ILayer.USER_REGION:this.keyColumn="REGION_ID";this.styleColumn="REGION_ID";if(ub)this.styleColumn=ub;this.tableName="LBS_USER_REGION";if(!this.where||this.where.length<=0)this.where="KEY='"+lboxKey+"'";else
this.where="KEY='"+lboxKey+"' AND "+this.where;this.hiddenColumn="REGION_NAME";this.hiddenInfo="<field column='REGION_NAME' name='NAME' />";if(xb){for(var i=0;i<xb.length;i++){this.hiddenColumn+=","+xb[i];this.hiddenInfo+="<field column='"+xb[i]+"' name='"+xb[i]+"' />";}
}
break;case ILayer.USER_LINE:this.keyColumn="LINE_ID";this.styleColumn="LINE_ID";if(ub)this.styleColumn=ub;if(!sym)sym="L.UM_ROTA_1";this.tableName="LBS_USER_LINE";if(!this.where||this.where.length<=0)this.where="KEY='"+lboxKey+"'";else
this.where="KEY='"+lboxKey+"' AND "+this.where;this.hiddenColumn="LINE_NAME";this.hiddenInfo="<field column='LINE_NAME' name='NAME' />";if(xb){for(var i=0;i<xb.length;i++){this.hiddenColumn+=","+xb[i];this.hiddenInfo+="<field column='"+xb[i]+"' name='"+xb[i]+"' />";}
}
break;case ILayer.CAMP_CATEGORY:this.keyColumn="ID";this.styleColumn="BRAND_SEMBOL_NO";if(ub)this.styleColumn=ub;this.tableName="POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";this.where="(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='"+lboxKey+"'"+(this.where&&this.where.length>0?" AND CG."+this.where:"");this.hiddenColumn="STANDARD_NAME";this.hiddenInfo="<field column='STANDARD_NAME' name='NAME' />";break;case ILayer.CAMP_CAMPAIGN:this.keyColumn="ID";this.styleColumn="BRAND_SEMBOL_NO";if(ub)this.styleColumn=ub;this.tableName="POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";this.where="(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='"+lboxKey+"'"+(this.where&&this.where.length>0?" AND CC."+this.where:"");this.hiddenColumn="STANDARD_NAME";this.hiddenInfo="<field column='STANDARD_NAME' name='NAME' />";break;case ILayer.DEAL_CATEGORY:this.keyColumn="DP.DEAL_ID";this.styleColumn=null;if(ub)this.styleColumn=ub;this.tableName="DEAL_PROVIDER DP, DEAL_CATEGORY DC";this.where="(DP.DEAL_ID = DC.DEAL_ID)"+(this.where&&this.where.length>0?" AND DC."+this.where:"");this.hiddenColumn="NAME";this.hiddenInfo="<field column='NAME' name='NAME' />";break;case ILayer.DEAL_DEAL:this.keyColumn="DEAL_ID";this.styleColumn=null;if(ub)this.styleColumn=ub;this.tableName="DEAL_PROVIDER";this.where=(this.where&&this.where.length>0?this.where:"DEAL_ID = 0");this.hiddenColumn="NAME";this.hiddenInfo="<field column='NAME' name='NAME' />";break;case ILayer.MAP_ROUTE:this.keyColumn="PATH_ID";this.styleColumn="PATH_ID";if(ub)this.styleColumn=ub;this.tableName="NET_PATHS";this.where=(this.where&&this.where.length>0?this.where:"PATH_ID = 0");this.hiddenColumn="";this.hiddenInfo="<field column='PATH_ID' name='ID' />";break;case ILayer.MAP_YOL:this.keyColumn="YOL_ID";this.styleColumn="YOL_ID";if(ub)this.styleColumn=ub;this.tableName="YOL";this.where=(this.where&&this.where.length>0?this.where:"YOL_ID = 0");this.hiddenColumn="YOL_ADI";this.hiddenInfo="<field column='YOL_ADI' name='NAME' />";break;default:return;}
if(this.thmLayer){this.mapview.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
if(Math.abs((new Date()).getTime()-gtm)<86403236){var fb;if(this.typ==ILayer.CAMP_CATEGORY||this.typ==ILayer.CAMP_CAMPAIGN)fb="SELECT GEOLOC,"+this.keyColumn+","+this.styleColumn+" STY"+(this.labelColumn?","+this.labelColumn+" AS LABEL":"")+(this.hiddenColumn?","+this.hiddenColumn:"")+" FROM POI WHERE ID IN (SELECT DISTINCT ID FROM "+this.tableName+(!this.where||this.where.length<=0?"":" WHERE "+trans2xmlstr(this.where))+")";else
fb="SELECT GEOLOC,"+this.keyColumn+","+this.styleColumn+" STY"+(this.labelColumn?","+this.labelColumn+" AS LABEL":"")+(this.hiddenColumn?","+this.hiddenColumn:"")+" FROM "+this.tableName+(!this.where||this.where.length<=0?"":" WHERE "+trans2xmlstr(this.where));var gb="<themes><theme name='"+this.typ+"_"+this.id+"_THEME' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.keyColumn+"' "+(this.labelColumn?"label_column='LABEL' label_style='"+this.labelStyle+"' ":"")+"render_style='"+(sym?(sym.name?sym.name:(sym.sym?(sym.sym.name?sym.sym.name:sym.sym):sym)):"C.DUMMY")+"' datasource='"+datasrcApp+"'>"+fb+"<hidden_info>"+this.hiddenInfo+"</hidden_info></jdbc_query></theme></themes>";var hb=new MVThemeBasedFOI(this.id,gb);hb.setBringToTopOnMouseOver(true);hb.enableImageCaching(false);if(sym){if(sym.name)hb.addStyle(sym);else
if(sym.sym){for(var i=0;i<sym.symlist.length;i++){var data=sym.symlist[i];if(data.sym.name)hb.addStyle(data.sym);}
hb.addStyle(sym.sym);}
}
hb.setVisible(pb);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
cb(p,x);}
);}
if(!sym){if(this.styleColumn=="BRAND_SEMBOL_NO")hb.setRenderingStyle("V.MAP_POI_BRAND");}
else{if(sym.name)hb.setRenderingStyle(sym.name);else
if(sym.sym)hb.setRenderingStyle(sym.sym.name);}
if(this.labelColumn)hb.enableLabels(true);hb.enableAutoWholeImage(true,60,6000);hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ILayer.prototype.createHeatmapLayer=function(bb,id,yb,tb,pb){this.typ=bb;this.thmtyp=yb;this.id=id;this.where=tb;if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;switch(yb){case ILayer.USER_POINT:this.keyColumn="POINT_ID";this.tableName="LBS_USER_POINT";if(!this.where||this.where.length<=0)this.where="KEY='"+lboxKey+"'";else
this.where="KEY='"+lboxKey+"' AND "+this.where;if(!sym)sym="M.RED_PIN";break;case ILayer.CAMP_CATEGORY:this.keyColumn="ID";this.tableName="POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";this.where="(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='"+lboxKey+"'"+(this.where&&this.where.length>0?" AND CG."+this.where:"");break;case ILayer.CAMP_CAMPAIGN:this.keyColumn="ID";this.tableName="POI P, CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP";this.where="(CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CG.KEY='"+lboxKey+"'"+(this.where&&this.where.length>0?" AND CC."+this.where:"");break;case ILayer.DEAL_CATEGORY:this.keyColumn="DP.DEAL_ID";this.tableName="DEAL_PROVIDER DP, DEAL_CATEGORY DC";this.where="(DP.DEAL_ID = DC.DEAL_ID)"+(this.where&&this.where.length>0?" AND DC."+this.where:"");break;case ILayer.DEAL_DEAL:this.keyColumn="DEAL_ID";this.tableName="DEAL_PROVIDER";this.where=(this.where&&this.where.length>0?this.where:"DEAL_ID = 0");break;default:return;}
if(this.thmLayer){eb.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
var sym;switch(bb){case ILayer.HEATMAP_ULKE:sym="V.HEAT_ULKE";break;case ILayer.HEATMAP_IL:sym="V.HEAT_IL";break;case ILayer.HEATMAP_ILCE:sym="V.HEAT_ILCE";break;case ILayer.HEATMAP_MAHALLE:sym="V.HEAT_MAHALLE";break;default:return;}
if(Math.abs((new Date()).getTime()-gtm)<86403236){var fb;if(this.typ==ILayer.CAMP_CATEGORY||this.typ==ILayer.CAMP_CAMPAIGN)fb="SELECT GEOLOC,"+this.keyColumn+" FROM POI WHERE ID IN (SELECT DISTINCT ID FROM "+this.tableName+(!this.where||this.where.length<=0?"":" WHERE "+trans2xmlstr(this.where))+")";else
fb="SELECT GEOLOC,"+this.keyColumn+" FROM "+this.tableName+(!this.where||this.where.length<=0?"":" WHERE "+trans2xmlstr(this.where));var gb="<themes><theme name='"+this.typ+"_"+this.id+"_THEME' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.keyColumn+"' "+"render_style='"+sym+"' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";var hb=new MVThemeBasedFOI(this.id,gb);hb.setBringToTopOnMouseOver(true);hb.enableImageCaching(false);hb.setVisible(pb);hb.setRenderingStyle(sym);hb.enableAutoWholeImage(true,60,6000);hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ILayer.prototype.createCategoryLayer=function(bb,id,sym,pb,cb){this.typ=bb;this.id=id;this.tableName=zb(bb);if(!this.tableName)this.tableName="POI";this.keyColumn="ID";this.where=$b(bb);if(!this.where){alert("Category not found !");return;}
if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;this.styleColumn=null;if(this.where.indexOf("TUR")>=0)this.styleColumn="TUR";else
if(this.where.indexOf("BRAND_ID")>=0||this.where.indexOf("BRAND_NAME")>=0)this.styleColumn="BRAND_SEMBOL_NO";else
if(this.where.indexOf("SUB_TYPE")>=0)this.styleColumn="BRAND_SEMBOL_NO";else
;if(this.thmLayer){eb.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
if(Math.abs((new Date()).getTime()-gtm)<86402336){var fb="SELECT GEOLOC,"+this.keyColumn+(this.styleColumn?","+this.styleColumn:"")+" FROM "+this.tableName+(!this.where||this.where.length<=0?"":" WHERE "+trans2xmlstr(this.where));var gb="<themes><theme name='"+this.typ+"_"+this.id+"_THEME' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.keyColumn+"' "+"render_style='"+(sym?(sym.name?sym.name:(sym.sym?(sym.sym.name?sym.sym.name:sym.sym):sym)):"M.RED_PIN")+"' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";var hb=new MVThemeBasedFOI(this.id,gb);hb.setBringToTopOnMouseOver(true);hb.enableImageCaching(false);if(sym){if(sym.name)hb.addStyle(sym);else
if(sym.sym){for(var i=0;i<sym.symlist.length;i++){var data=sym.symlist[i];if(data.sym.name)hb.addStyle(data.sym);}
hb.addStyle(sym.sym);}
}
hb.setVisible(pb);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
cb(p,x);}
);}
if(!sym){if(this.styleColumn=="TUR")hb.setRenderingStyle("V.MAP_POI_CATEGORY");else
if(this.styleColumn=="BRAND_SEMBOL_NO")hb.setRenderingStyle("V.MAP_POI_BRAND");else
;}
else{if(sym.name)hb.setRenderingStyle(sym.name);else
if(sym.sym)hb.setRenderingStyle(sym.sym.name);}
hb.enableAutoWholeImage(true,60,6000);hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ILayer.prototype.createCategoryHeatmapLayer=function(bb,id,_b,pb){this.typ=bb;this.id=id;this.tableName="POI";this.keyColumn="ID";this.where=$b(_b);if(!this.where){alert("Category not found !");return;}
if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;if(this.thmLayer){eb.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
var sym;switch(bb){case ILayer.HEATMAP_ULKE:sym="V.HEAT_ULKE";break;case ILayer.HEATMAP_IL:sym="V.HEAT_IL";break;case ILayer.HEATMAP_ILCE:sym="V.HEAT_ILCE";break;case ILayer.HEATMAP_MAHALLE:sym="V.HEAT_MAHALLE";break;default:return;}
if(Math.abs((new Date()).getTime()-gtm)<86402336){var fb="SELECT GEOLOC,"+this.keyColumn+" FROM "+this.tableName+(!this.where||this.where.length<=0?"":" WHERE "+trans2xmlstr(this.where));var gb="<themes><theme name='"+this.typ+"_"+this.id+"_THEME' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.keyColumn+"' "+"render_style='"+sym+"' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";var hb=new MVThemeBasedFOI(this.id,gb);hb.setBringToTopOnMouseOver(true);hb.enableImageCaching(false);hb.setVisible(pb);hb.setRenderingStyle(sym);hb.enableAutoWholeImage(true,60,6000);hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ILayer.prototype.createBrandLayer=function(bb,id,sym,pb,cb){this.typ=bb;this.id=id;this.tableName="POI";this.keyColumn="ID";this.where=ac(bb);if(!this.where){alert("Brand not found !");return;}
if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;this.styleColumn=null;if(this.where.indexOf("TUR")>=0)this.styleColumn="TUR";else
if(this.where.indexOf("BRAND_ID")>=0||this.where.indexOf("BRAND_NAME")>=0)this.styleColumn="BRAND_SEMBOL_NO";else
if(this.where.indexOf("SUB_TYPE")>=0)this.styleColumn="BRAND_SEMBOL_NO";else
;if(this.thmLayer){eb.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
if(Math.abs((new Date()).getTime()-gtm)<86413436){var fb="SELECT GEOLOC,"+this.keyColumn+(this.styleColumn?","+this.styleColumn:"")+" FROM "+this.tableName+(!this.where||this.where.length<=0?"":" WHERE "+trans2xmlstr(this.where));var gb="<themes><theme name='"+this.typ+"_"+this.id+"_THEME' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.keyColumn+"' "+"render_style='"+(sym?(sym.name?sym.name:(sym.sym?(sym.sym.name?sym.sym.name:sym.sym):sym)):"M.RED_PIN")+"' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";var hb=new MVThemeBasedFOI(this.id,gb);hb.setBringToTopOnMouseOver(true);hb.enableImageCaching(false);if(sym){if(sym.name)hb.addStyle(sym);else
if(sym.sym){for(var i=0;i<sym.symlist.length;i++){var data=sym.symlist[i];if(data.sym.name)hb.addStyle(data.sym);}
hb.addStyle(sym.sym);}
}
hb.setVisible(pb);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
cb(p,x);}
);}
if(!sym){if(this.styleColumn=="TUR")hb.setRenderingStyle("V.MAP_POI_CATEGORY");else
if(this.styleColumn=="BRAND_SEMBOL_NO")hb.setRenderingStyle("V.MAP_POI_BRAND");else
;}
else{if(sym.name)hb.setRenderingStyle(sym.name);else
if(sym.sym)hb.setRenderingStyle(sym.sym.name);}
hb.enableAutoWholeImage(true,60,6000);hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ILayer.prototype.createBrandHeatmapLayer=function(bb,id,bc,pb){this.typ=bb;this.id=id;this.tableName="POI";this.keyColumn="ID";this.where=ac(bc);if(!this.where){alert("Brand not found !");return;}
if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;if(this.thmLayer){this.mapview.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
var sym;switch(bb){case ILayer.HEATMAP_ULKE:sym="V.HEAT_ULKE";break;case ILayer.HEATMAP_IL:sym="V.HEAT_IL";break;case ILayer.HEATMAP_ILCE:sym="V.HEAT_ILCE";break;case ILayer.HEATMAP_MAHALLE:sym="V.HEAT_MAHALLE";break;default:return;}
if(Math.abs((new Date()).getTime()-gtm)<86413436){var fb="SELECT GEOLOC,"+this.keyColumn+" FROM "+this.tableName+(!this.where||this.where.length<=0?"":" WHERE "+trans2xmlstr(this.where));var gb="<themes><theme name='"+this.typ+"_"+this.id+"_THEME' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='"+this.keyColumn+"' "+"render_style='"+sym+"' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";var hb=new MVThemeBasedFOI(this.id,gb);hb.setBringToTopOnMouseOver(true);hb.enableImageCaching(false);hb.setVisible(pb);hb.setRenderingStyle(sym);hb.enableAutoWholeImage(true,60,6000);hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ILayer.prototype.createDemographicLayer=function(bb,id,pb,cb){this.typ=bb;this.id=id;this.tableName=null;switch(bb){case ILayer.DEMOGRAFIK_YAS_DURUMU:this.tableName="DEMOGRAFIK_YAS_DURUMU";break;case ILayer.DEMOGRAFIK_NUFUS:this.tableName="DEMOGRAFIK_NUFUS";break;case ILayer.DEMOGRAFIK_EGITIM:this.tableName="DEMOGRAFIK_EGITIM";break;case ILayer.DEMOGRAFIK_KONUT_SAYISI:this.tableName="DEMOGRAFIK_KONUT_SAYISI";break;case ILayer.DEMOGRAFIK_ARAC_SAYISI:this.tableName="DEMOGRAFIK_ARAC_SAYISI";break;break;}
if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;if(this.thmLayer){eb.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
if(Math.abs((new Date()).getTime()-gtm)<86402336){var hb=new MVThemeBasedFOI(id,datasrcApp+"."+this.tableName);hb.setVisible(pb);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableLabels(false);hb.enableAutoWholeImage(true,60,8000);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
cb(p,x);}
);}
hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ILayer.prototype.createThemeLayer=function(name,id,cc,pb,cb,dc){this.typ=ILayer.PREDEFINED_THEME;this.id=id;this.tableName=name;this.keyColumn=null;this.where=null;this.styleColumn=null;if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;if(this.thmLayer){eb.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
if(Math.abs((new Date()).getTime()-gtm)<86402336){var hb=new MVThemeBasedFOI(id,datasrcApp+"."+name);if(cc){switch(cc.length){case 1:hb.setQueryParameters(cc[0]);break;case 2:hb.setQueryParameters(cc[0],cc[1]);break;case 3:hb.setQueryParameters(cc[0],cc[1],cc[2]);break;case 4:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3]);break;case 5:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4]);break;case 6:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5]);break;case 7:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6]);break;case 8:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7]);break;case 9:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8]);break;case 10:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9]);break;case 11:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10]);break;case 12:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11]);break;case 13:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12]);break;case 14:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12],cc[13]);break;case 15:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12],cc[13],cc[14]);break;case 16:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12],cc[13],cc[14],cc[15]);break;case 17:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12],cc[13],cc[14],cc[15],cc[16]);break;case 18:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12],cc[13],cc[14],cc[15],cc[16],cc[17]);break;case 19:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12],cc[13],cc[14],cc[15],cc[16],cc[17],cc[18]);break;case 20:hb.setQueryParameters(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12],cc[13],cc[14],cc[15],cc[16],cc[17],cc[18],cc[19]);break;}
}
hb.setVisible(pb);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableLabels(true);hb.enableAutoWholeImage(true,60,6000);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
cb(p,x);}
);}
if(dc){hb.attachEventListener(MVEvent.MOUSE_RIGHT_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
dc(p,x);}
);}
hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ILayer.prototype.createSqlQueryLayer=function(id,ec,fc,gc,ub,sym,pb,cb,vb,wb){return;}
ILayer.prototype.removeLayer=function(){if(this.mapview==null){this.mapview=mapviewGlobal;}
if(this.thmLayer){this.mapview.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
return;}
ILayer.prototype.setVisible=function(pb){if(this.thmLayer)this.thmLayer.setVisible(pb);return;}
ILayer.prototype.refresh=function(){if(this.thmLayer)this.thmLayer.refresh();return;}
ILayer.prototype.zoomToLayer=function(){if(this.thmLayer)this.thmLayer.zoomToTheme();return;}
var mapviewGlobal=null;var tmcAnimateArray=new Array();var hc=null;var ic=null;var jc={"mapTileLayer":"OSM_MERCATOR","format":"PNG","coordSys":{"srid":3857,"type":"PROJECTED","distConvFactor":1.0,"minX":-2.0037508E7,"minY":-2.0037508E7,"maxX":2.0037508E7,"maxY":2.0037508E7
}
,"zoomLevels":[{"zoomLevel":0,"name":"","tileWidth":2.0037508E7,"tileHeight":2.0037508E7,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":1,"name":"","tileWidth":1.0018754E7,"tileHeight":1.0018754E7,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":2,"name":"","tileWidth":5009377.0,"tileHeight":5009377.0,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":3,"name":"","tileWidth":2504688.5,"tileHeight":2504688.5,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":4,"name":"","tileWidth":1252344.25,"tileHeight":1252344.25,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":5,"name":"","tileWidth":626172.125,"tileHeight":626172.125,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":6,"name":"","tileWidth":313086.0625,"tileHeight":313086.0625,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":7,"name":"","tileWidth":156543.03125,"tileHeight":156543.03125,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":8,"name":"","tileWidth":78271.515625,"tileHeight":78271.515625,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":9,"name":"","tileWidth":39135.7578125,"tileHeight":39135.7578125,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":10,"name":"","tileWidth":19567.87890625,"tileHeight":19567.87890625,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":11,"name":"","tileWidth":9783.939453125,"tileHeight":9783.939453125,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":12,"name":"","tileWidth":4891.9697265625,"tileHeight":4891.9697265625,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":13,"name":"","tileWidth":2445.98486328125,"tileHeight":2445.98486328125,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":14,"name":"","tileWidth":1222.992431640625,"tileHeight":1222.992431640625,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":15,"name":"","tileWidth":611.4962158203125,"tileHeight":611.4962158203125,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":16,"name":"","tileWidth":305.74810791015625,"tileHeight":305.74810791015625,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":17,"name":"","tileWidth":152.87405395507812,"tileHeight":152.87405395507812,"tileImageWidth":256,"tileImageHeight":256}
,{"zoomLevel":18,"name":"","tileWidth":76.43702697753906,"tileHeight":76.43702697753906,"tileImageWidth":256,"tileImageHeight":256}
]}
;IMapper.BASEMAP_POI_LOGO="BM1";IMapper.BASEMAP_POI="BM2";IMapper.BASEMAP_SIMPLE="BM3";IMapper.INFOMAP_POI_LOGO="BM5";IMapper.INFOMAP_POI="BM6";IMapper.INFOMAP_SIMPLE="BM7";IMapper.RECENTER="recenter";IMapper.MOUSE_CLICK="mouse_click";IMapper.MOUSE_RIGHT_CLICK="mouse_right_click";IMapper.MOUSE_DOUBLE_CLICK="mouse_double_click";IMapper.MOUSE_DOWN="mouse_down";IMapper.MOUSE_UP="mouse_up";IMapper.MOUSE_MOVE="mouse_move";IMapper.ZOOM_LEVEL_CHANGE="zoom_level_change";IMapper.BEFORE_ZOOM_LEVEL_CHANGE="before_zoom_level_change";IMapper.INITIALIZE="initialize";IMapper.MARKER_MOUSE_CLICK="marker_mouse_click";IMapper.MARKER_MOUSE_OVER="marker_mouse_over";IMapper.MARKER_MOUSE_OUT="marker_mouse_out";IMapper.MARKER_MOUSE_RIGHT_CLICK="marker_mouse_rclick";IMapper.mustTransform=true;function IMapper(){this.mapview=null;this.lboxMap=null;this.googleMap=null;this.bingMap=null;this.hybridMap=null;this.redlineTool=null;this.measureTool=null;this.circleTool=null;this.rectangleTool=null;this.polygonTool=null;this.groupArray=new Array();this.mapTraffic=null;this.thmTraffic=null;this.thmTrafficTmc=null;this.thmTrafficTmc_tmcKodList=null;this.thmTrafficTmc_flag=false;this.thmTrafficEvents=null;this.thmSocialEvents=null;this.thmNobetciEczane=null;this.thmWeatherReport=null;this.thmImageLayer=null;this.thmEarthquakes=null;this.objs=new Array();this.me=null;}
IMapper.prototype.initMap=function(kc,lc,mc,nc,oc,pc){var qc=location.protocol;var rc=urlBase.split("//");if(rc[1]&&qc&&(qc=="http:"||qc=="https:")){urlBase=qc+"//"+rc[1];}
if(nc){if(nc.length>=7&&nc.substring(0,7)=="http://")urlBase=nc;else{switch(nc){case IMapper.BASEMAP_POI_LOGO:mapBase=BASEMAP_BM1;break;case IMapper.BASEMAP_POI:mapBase=BASEMAP_BM2;break;case IMapper.BASEMAP_SIMPLE:mapBase=BASEMAP_BM3;break;case IMapper.INFOMAP_POI_LOGO:mapBase=BASEMAP_BM5;break;case IMapper.INFOMAP_POI:mapBase=BASEMAP_BM6;break;case IMapper.INFOMAP_SIMPLE:mapBase=BASEMAP_BM7;break;default:mapBase=nc;break;}
}
}
if(oc)urlBase=oc;if(Math.abs((new Date()).getTime()-gtm)<86423134){if(!pc)var pc="map";this.mapview=new MVMapView(document.getElementById(pc),urlBase);this.lboxMap=new MVMapTileLayer(mapBase);this.mapview.addMapTileLayer(this.lboxMap);this.mapview.addCopyRightNote("&#169;2020 Infotech");this.mapview.enableKeyboardPanning(true);var sc=getPointGeometryObject(lc,kc,this.mapview);this.mapview.setCenterAndZoomLevel(sc,mc);this.mapview.display();if(mapviewGlobal==null)mapviewGlobal=this.mapview;tc=this;}
return;}
IMapper.prototype.initMapWithExtent=function(uc,vc,wc,xc,nc,oc,pc){var qc=location.protocol;var rc=urlBase.split("//");if(rc[1]&&qc&&(qc=="http:"||qc=="https:")){urlBase=qc+"//"+rc[1];}
if(nc){if(nc.length>=7&&nc.substring(0,7)=="http://")urlBase=nc;else{switch(nc){case IMapper.BASEMAP_POI_LOGO:mapBase=BASEMAP_BM1;break;case IMapper.BASEMAP_POI:mapBase=BASEMAP_BM2;break;case IMapper.BASEMAP_SIMPLE:mapBase=BASEMAP_BM3;break;case IMapper.INFOMAP_POI_LOGO:mapBase=BASEMAP_BM5;break;case IMapper.INFOMAP_POI:mapBase=BASEMAP_BM6;break;case IMapper.INFOMAP_SIMPLE:mapBase=BASEMAP_BM7;break;default:mapBase=nc;break;}
}
}
if(oc)urlBase=oc;if(Math.abs((new Date()).getTime()-gtm)<86423134){if(!pc)var pc="map";this.mapview=new MVMapView(document.getElementById(pc),urlBase);lboxMap=new MVMapTileLayer(mapBase);var eb=this.mapview;eb.addMapTileLayer(lboxMap,function(){var yc=MVSdoGeometry.createRectangle(vc,uc,xc,wc,8307);if(eb.getSrid()!=8307){yc=eb.transformGeom(yc,eb.getSrid(),null,null,null);}
var zc=yc.getMBR();var $c=Math.abs(zc[0]-zc[2]);var _c=Math.abs(zc[1]-zc[3]);var ad=$c*(eb.pixPerX);var bd=_c*(eb.pixPerY);var cd=eb.getPaneWidth();var dd=eb.getPaneHeight();var ed=eb.zoomLevel;var fd=ed;var gd=eb.maxZoom;if(cd<ad||dd<bd){for(var i=(ed-1);i>=0;--i){fd=i;var hd=(eb.TileW[i]/eb.msi.getTileWidth(i))*$c;var jd=(eb.TileH[i]/eb.msi.getTileHeight(i))*_c;if(hd<=cd&&jd<=dd)break;}
}
else if(cd>ad&&dd>bd){for(var i=(ed+1);i<=gd;++i){var hd=(eb.TileW[i]/eb.msi.getTileWidth(i))*$c;var jd=(eb.TileH[i]/eb.msi.getTileHeight(i))*_c;if(hd<=cd&&jd<=dd)fd=i;else break;}
}
var kd=(zc[0]+zc[2])/2;var ld=(zc[1]+zc[3])/2;var sc=MVSdoGeometry.createPoint(kd,ld,eb.getSrid());eb.setCenterAndZoomLevel(sc,fd);}
);eb.addCopyRightNote("&#169;2020 Infotech");eb.enableKeyboardPanning(true);eb.display();this.mapview=eb;if(mapviewGlobal==null)mapviewGlobal=eb;tc=this;}
return;}
IMapper.prototype.addTileLayer=function(md){this.mapview.addMapTileLayer(md,null);return;}
IMapper.prototype.removeTileLayer=function(md){this.mapview.removeMapTileLayer(md);return;}
IMapper.prototype.addGoogleTileLayer=function(nd,od){if(this.googleMap)return;this.lboxMap.setVisible(false);this.googleMap=new MVGoogleTileLayerV3();this.googleMap.setMapType("satellite");this.googleMap.setKey(nd);this.googleMap.setSrid(3857);this.googleMap.setVisible(true);this.mapview.addMapTileLayer(this.googleMap);if(od){this.hybridMap=new MVMapTileLayer(transparentMap);this.mapview.addMapTileLayer(this.hybridMap);}
return;}
IMapper.prototype.removeGoogleTileLayer=function(){if(this.googleMap){this.mapview.removeMapTileLayer(this.googleMap);this.googleMap=null;if(this.hybridMap){this.mapview.removeMapTileLayer(this.hybridMap);this.hybridMap=null;}
this.lboxMap.setVisible(true);}
return;}
IMapper.prototype.addBingTileLayer=function(pd,od){if(this.bingMap)return;this.lboxMap.setVisible(false);this.bingMap=new MVBingTileLayerV7();this.bingMap.setMapType(MVBingTileLayer.TYPE_AERIAL);this.bingMap.setKey(pd);this.bingMap.setSrid(3857);this.bingMap.setVisible(true);this.mapview.addMapTileLayer(this.bingMap);if(od){this.hybridMap=new MVMapTileLayer(transparentMap);this.mapview.addMapTileLayer(this.hybridMap);}
return;}
IMapper.prototype.removeBingTileLayer=function(){if(this.bingMap){this.mapview.removeMapTileLayer(this.bingMap);this.bingMap=null;if(this.hybridMap){this.mapview.removeMapTileLayer(this.hybridMap);this.hybridMap=null;}
this.lboxMap.setVisible(true);}
return;}
IMapper.prototype.addNavigationPanel=function(qd){if(qd)this.mapview.addNavigationPanel(qd);else
this.mapview.addNavigationPanel('west');return;}
IMapper.prototype.addScaleBar=function(rd,sd,td,ud){this.mapview.addScaleBar(rd,sd,td,ud);return;}
IMapper.prototype.enableLoadingIcon=function(vd){if(!this.mapview)this.mapview=mapviewGlobal;this.mapview.enableLoadingIcon(vd);return;}
IMapper.prototype.enableKeyboardPanning=function(vd){this.mapview.enableKeyboardPanning(vd);return;}
IMapper.prototype.enableDragging=function(wd){this.mapview.enableDragging(wd);return;}
IMapper.prototype.enableMouseWheelZooming=function(xd){this.mapview.enableMouseWheelZooming(xd);return;}
IMapper.prototype.addCollapsibleOverview=function(yd,$c,_c,zd){$d=new MVMapDecoration(null,null,null,$c,_c);$d.setCollapsible(true,zd);$d.setTitleBar(yd,"/this.mapviewer/fsmc/images/overviewicon.png",yd);this.mapview.addMapDecoration($d);var _d=new MVOverviewMap($d.getContainerDiv(),3);this.mapview.addOverviewMap(_d);return;}
IMapper.prototype.addOverview=function(ae){if(!ae)var ae="ow";var _d=new MVOverviewMap(document.getElementById(ae),3);_d.setRectangleStyle("1px solid red","red");this.mapview.addOverviewMap(_d);return;}
IMapper.prototype.addDistanceMeasuringTool=function(){var be=new MVDistanceTool(MVDistanceTool.METRIC);this.mapview.addDistanceTool(be);be.init();return;}
IMapper.prototype.showMapCentered=function(kc,lc,mc){var sc=getPointGeometryObject(lc,kc,this.mapview);if(mc)this.mapview.setCenterAndZoomLevel(sc,mc);else
this.mapview.setCenter(sc);return;}
IMapper.prototype.showMapRectangle=function(uc,vc,wc,xc){var ce=getRectangleGeometryObject(vc,uc,xc,wc,this.mapview);this.mapview.zoomToRectangle(ce);return;}
IMapper.prototype.getMapRectangle=function(){var de=this.mapview.getMapWindowBBox();if(IMapper.mustTransform)de=this.mapview.transformGeom(de,8307);return de.getMBR();}
IMapper.prototype.getMouseLocation=function(ee){var ib=this.mapview.getMouseLocation();if(IMapper.mustTransform)ib=this.mapview.transformGeom(ib,8307);return{x:ib.getPointX(),y:ib.getPointY()}
;}
IMapper.prototype.getMapCoordinates=function(x,y){var ib=this.mapview.getMapCoordinates({x:x,y:y}
);if(IMapper.mustTransform)ib=this.mapview.transformGeom(ib,8307);return{x:ib.getPointX(),y:ib.getPointY()}
;}
IMapper.prototype.displayInfoWindow=function(fe,ge,he,$c,_c,yd,ie){var sc=getPointGeometryObject(ge,fe,this.mapview);this.mapview.displayInfoWindow(sc,he,$c,_c,0,yd,ie);return;}
IMapper.prototype.removeInfoWindow=function(){this.mapview.removeInfoWindow();return;}
IMapper.prototype.addMapDecoration=function(id,he,top,je,$c,_c,ke){if(this.objs[id])this.mapview.removeMapDecoration(this.objs[id]);var le=new MVMapDecoration(he,top,je,$c,_c);le.setOffset(0,50);if(ke)le.attachEventListener(MVEvent.MOUSE_CLICK,ke);this.mapview.addMapDecoration(le);this.objs[id]=le;return;}
IMapper.prototype.addCollapsibleMapDecoration=function(id,he,$c,_c,me,qb){if(this.objs[id])this.mapview.removeMapDecoration(this.objs[id]);var le=new MVMapDecoration(he,null,null,$c,_c);le.setCollapsible(true,me,qb);this.mapview.addMapDecoration(le);this.objs[id]=le;return;}
IMapper.prototype.removeMapDecoration=function(id){if(!this.objs[id])return;this.mapview.removeMapDecoration(this.objs[id]);this.objs[id]=null;return;}
IMapper.prototype.addMarkerGroup=function(id,array,ne,gd,oe){var pe=new Array();for(var i=0;i<array.length;i++){var qe=array[i];if(qe.id)pe.push(this.mapview.getFOI(qe.id));else
pe.push(this.mapview.getFOI(qe));}
var re=new MVFOIGroup(pe);if(ne)re.setMinVisibleZoomLevel(ne);if(gd)re.setMaxVisibleZoomLevel(gd);if(oe)re.setZIndex(oe);this.mapview.addFOIGroup(re);this.groupArray[id]=re;return;}
IMapper.prototype.setMarkerGroupVisible=function(id,pb){var re=this.groupArray[id];if(re)re.setVisible(pb);return;}
IMapper.prototype.setMarkerGroupMinVisibleZoomLevel=function(id,ed){var re=this.groupArray[id];if(re)re.setMinVisibleZoomLevel(ed);return;}
IMapper.prototype.setMarkerGroupMaxVisibleZoomLevel=function(id,ed){var re=this.groupArray[id];if(re)re.setMaxVisibleZoomLevel(ed);return;}
IMapper.prototype.removeFeature=function(id){var se=this.mapview.getFOI(id);if(se!=null)this.mapview.removeFOI(se);return;}
IMapper.prototype.addMarker=function(id,fe,ge,name,sym,w,h,ke,oe){var se=this.mapview.getFOI(id);if(se!=null)this.mapview.removeFOI(se);if(!sym.name)sym=datasrcApp+"."+sym;if(Math.abs((new Date()).getTime()-gtm)<86472673){var sc=getPointGeometryObject(ge,fe,this.mapview);se=new MVFOI(id,sc,sym,null,w,h);if(name){se.setInfoTip(name);se.enableInfoTip(true);}
se.enableInfoWindow(false);if(oe)se.setZIndex(oe);se.setClickable(true);if(ke){se.attachEventListener(MVEvent.MOUSE_CLICK,function(){ke({id:id,name:name,x:ge,y:fe}
)}
);}
this.mapview.addFOI(se);}
return;}
IMapper.prototype.moveMarker=function(id,fe,ge,oe){var se=this.mapview.getFOI(id);if(se==null)return;if(Math.abs((new Date()).getTime()-gtm)<86472673){var sc=getPointGeometryObject(ge,fe,this.mapview);se.updateGeometry(sc);if(oe||oe==0)se.setZIndex(oe);}
return;}
IMapper.prototype.addCustomMarker=function(id,fe,ge,name,te,ke,oe,$c,_c){var se=this.mapview.getFOI(id);if(se!=null)this.mapview.removeFOI(se);if(Math.abs((new Date()).getTime()-gtm)<86445293){if(!$c)var $c=10;if(!_c)var _c=10;var sc=getPointGeometryObject(ge,fe,this.mapview);var se=MVFOI.createMarkerFOI(id,sc,te,$c,_c);if(name){se.setInfoTip(name);se.enableInfoTip(true);}
se.enableInfoWindow(false);if(oe)se.setZIndex(oe);se.setClickable(true);if(ke){se.attachEventListener(MVEvent.MOUSE_CLICK,function(){ke({id:id,name:name,x:ge,y:fe}
)}
);}
this.mapview.addFOI(se);}
return;}
IMapper.prototype.addHtmlMarker=function(id,fe,ge,he,oe){var se=this.mapview.getFOI(id);if(se!=null)this.mapview.removeFOI(se);if(Math.abs((new Date()).getTime()-gtm)<86409236){var sc=getPointGeometryObject(ge,fe,this.mapview);var se=MVFOI.createHTMLFOI(id,sc,he,10,10);se.enableInfoTip(false);se.enableInfoWindow(false);if(oe)se.setZIndex(oe);this.mapview.addFOI(se);}
return;}
IMapper.prototype.animateMarker=function(id,ue,ve){var se=this.mapview.getFOI(id);if(se==null)return;var we=MVSdoGeometry.createLineString(ue,8307);se.animateToNewLocation(we,ve);return;}
IMapper.prototype.animateMarkerFlow=function(xe,ve,sym,$c,_c,ye,ze,$e){tmcAnimateArray[xe]={"interval":ve,"sym":sym,"width":$c,"height":_c,"repeat":ye,"factor":ze,"ordinates":[]}
;var _e=this.mapview.getZoomLevel();var element=document.createElement("script");element.src=lboxUrl+"?Key="+lboxKey+"&Cmd=TmcHatInfo&Typ=JSON&TmcKod="+xe+"&ZoomLevel="+_e+"&Geometry=1&SRID="+this.mapview.getSrid()+"&Reverse="+($e?"1":"0")+"&callback=tmcAnimate";element.type='text/javascript';element.charset='utf-8';this.element=element;document.body.appendChild(element);return;}
IMapper.prototype.stopMarkerFlowAnimation=function(xe){tmcAnimateArray=new Array();var id="tmca_";if(xe)id+=xe;var af=true;while(af){af=false;var array=this.mapview.getAllFOIs();for(var i=0;i<array.length;i++){var name=array[i].getId();if(name.startsWith(id)){this.mapview.removeFOI(array[i]);af=true;}
}
}
return;}
IMapper.prototype.getMarker=function(id){if(!this.mapview)this.mapview=mapviewGlobal;var se=this.mapview.getFOI(id);if(se!=null){var bf=se.getGeometry();var jb={id:id,x:bf.getPointX(),y:bf.getPointY(),imgURL:se.imgURL}
;return jb;}
return;}
IMapper.prototype.setMarkerVisible=function(id,pb){var se=this.mapview.getFOI(id);if(se!=null)se.setVisible(pb);return;}
IMapper.prototype.setMarkerImageUrl=function(id,cf,w,h){var se=this.mapview.getFOI(id);if(se!=null)se.updateImageURL(cf,w,h);return;}
IMapper.prototype.addLabelToMarker=function(id,he,df,ef){var se=this.mapview.getFOI(id);if(se!=null){se.setHTMLElement(he,df,ef);se.reDraw();}
return;}
IMapper.prototype.removeLabelFromMarker=function(id){var se=this.mapview.getFOI(id);if(se!=null){se.setHTMLElement('',0,0);se.reDraw();}
return;}
IMapper.prototype.setMarkerListener=function(id,event,ff){var se=this.mapview.getFOI(id);if(se==null)return;var kb;switch(event){case IMapper.MARKER_MOUSE_CLICK:kb=MVEvent.MOUSE_CLICK;break;case IMapper.MARKER_MOUSE_OVER:kb=MVEvent.MOUSE_OVER;break;case IMapper.MARKER_MOUSE_OUT:kb=MVEvent.MOUSE_OUT;break;case IMapper.MARKER_MOUSE_RIGHT_CLICK:kb=MVEvent.MOUSE_RIGHT_CLICK;break;}
if(kb)se.attachEventListener(kb,function(ib,jb,kb){var gf=this.mapview.getMouseLocation();if(IMapper.mustTransform)gf=this.mapview.transformGeom(gf,8307);ib={x:gf.getPointX(),y:gf.getPointY()}
;var bf=se.getGeometry();if(IMapper.mustTransform)bf=this.mapview.transformGeom(bf,8307);var jb={id:id,x:bf.getPointX(),y:bf.getPointY(),imgURL:se.imgURL}
;ff(ib,jb);}
);return;}
IMapper.prototype.clearMarkerListener=function(id,event,ff){var se=this.mapview.getFOI(id);if(se==null)return;var kb;switch(event){case IMapper.MARKER_MOUSE_CLICK:kb=MVEvent.MOUSE_CLICK;break;case IMapper.MARKER_MOUSE_OVER:kb=MVEvent.MOUSE_OVER;break;case IMapper.MARKER_MOUSE_OUT:kb=MVEvent.MOUSE_OUT;break;case IMapper.MARKER_MOUSE_RIGHT_CLICK:kb=MVEvent.MOUSE_RIGHT_CLICK;break;}
if(kb)se.detachEventListener(kb,ff);return;}
IMapper.prototype.addLine=function(id,ue,name,sym,ke,oe){var se=this.mapview.getFOI(id);if(se!=null)this.mapview.removeFOI(se);if(!sym.name)sym=datasrcApp+"."+sym;var hf=8307;if(ue[0]>180&&ue[1]>90)hf=coorSrid;var jf=MVSdoGeometry.createLineString(ue,hf);var se=new MVFOI(id,jf,sym);if(name){se.setInfoTip(name);se.enableInfoTip(true);}
se.enableInfoWindow(false);if(oe)se.setZIndex(oe);se.setClickable(true);if(ke){se.attachEventListener(MVEvent.MOUSE_CLICK,ke);}
this.mapview.addFOI(se);return;}
IMapper.prototype.addRegion=function(id,ue,name,sym,ke,oe){var se=this.mapview.getFOI(id);if(se!=null)this.mapview.removeFOI(se);if(!sym.name)sym=datasrcApp+"."+sym;if(Math.abs((new Date()).getTime()-gtm)<86412873){var kf=MVSdoGeometry.createPolygon(ue,8307);var se=new MVFOI(id,kf,sym);if(name){se.setInfoTip(name);se.enableInfoTip(true);}
se.enableInfoWindow(false);if(oe)se.setZIndex(oe);se.setClickable(true);if(ke){se.attachEventListener(MVEvent.MOUSE_CLICK,ke);}
this.mapview.addFOI(se);}
return;}
IMapper.prototype.addCircle=function(id,fe,ge,radius,name,sym,ke,oe){var se=this.mapview.getFOI(id);if(se!=null)this.mapview.removeFOI(se);if(!sym.name)sym=datasrcApp+"."+sym;var lf=MVSdoGeometry.createGeodeticCirclePolygon(ge,fe,radius,8307);var se=new MVFOI(id,lf,sym);if(name){se.setInfoTip(name);se.enableInfoTip(true);}
se.enableInfoWindow(false);if(oe)se.setZIndex(oe);se.setClickable(true);if(ke){se.attachEventListener(MVEvent.MOUSE_CLICK,ke);}
this.mapview.addFOI(se);return;}
IMapper.prototype.showTrafficView=function(oe,mf){if(!nf("TRAFIK")){alert("Package does not exist !");return;}
if(!mf)var mf='map';var of=document.getElementById(mf);pf=of.clientWidth;qf=of.clientHeight;var rf=this.mapview.getSrid();var sf=this.mapview.getMapWindowBBox().getMBR();var tf=this.mapview.transformGeom(MVSdoGeometry.createPoint(sf[0],sf[1],rf),8307);var uf=tf.getPointX();var vf=tf.getPointY();var wf=this.mapview.transformGeom(MVSdoGeometry.createPoint(sf[2],sf[3],rf),8307);var xf=wf.getPointX();var yf=wf.getPointY();if(uf>xf){var zf=xf;xf=uf;uf=zf;}
if(vf>yf){var zf=yf;yf=vf;vf=zf;}
var sc=MVSdoGeometry.createPoint(uf,yf,8307);if(this.thmTraffic)this.mapview.removeFOI(this.thmTraffic);this.thmTraffic=null;if(Math.abs((new Date()).getTime()-gtm)<86432199){var he="<img src='http://msn.tr.mapserver.be-mobile.be/p?service=wms&version=1.1.1&request=GetMap&Layers=turkey_links&Styles=default&format=image/png&TRANSPARENT=TRUE&SRS=EPSG:4326&BBOX="+uf+","+vf+","+xf+","+yf+"&WIDTH="+pf+"&HEIGHT="+qf+"'>";this.thmTraffic=MVFOI.createHTMLFOI("tmc_harita",sc,he,0,5);this.thmTraffic.setBringToTopOnMouseOver(false);this.thmTraffic.setClickable(false);this.thmTraffic.enableInfoTip(false);this.thmTraffic.enableInfoWindow(false);if(oe)this.thmTraffic.setZIndex(oe);this.mapview.addFOI(this.thmTraffic);}
return;}
IMapper.prototype.hideTrafficView=function(){if(this.thmTraffic)this.mapview.removeFOI(this.thmTraffic);this.thmTraffic=null;return;}
IMapper.prototype.addTrafficTmc=function($f){if(!nf("TRAFIK")){alert("Package does not exist !");return;}
var rf=this.mapview.getSrid();var _e=this.mapview.getZoomLevel();var _f=datasrcApp+".TMC_HAT_"+rf+"_"+_e;if($f&&$f.length>0)_f=datasrcApp+".TMC_KOD_"+rf+"_"+_e;if(Math.abs((new Date()).getTime()-gtm)<86419236){var hb=new MVThemeBasedFOI("traffic_tmc",_f);if($f&&$f.length>0)hb.setQueryParameters($f);hb.setVisible(true);hb.enableInfoWindow(false);hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);this.mapview.addThemeBasedFOI(hb);this.thmTrafficTmc=hb;this.thmTrafficTmc_tmcKodList=$f;this.thmTrafficTmc_flag=true;}
return;}
IMapper.prototype.removeTrafficTmc=function(){if(!this.thmTrafficTmc)return;this.mapview.removeThemeBasedFOI(this.thmTrafficTmc);this.thmTrafficTmc=null;this.thmTrafficTmc_tmcKodList=null;this.thmTrafficTmc_flag=false;return;}
IMapper.prototype.addTrafficEvents=function(){if(!nf("TRAFIK")){alert("Package does not exist !");return;}
if(Math.abs((new Date()).getTime()-gtm)<86419236){var hb=new MVThemeBasedFOI("traffic_event",datasrcApp+".TRAFFIC_EVENT");hb.setVisible(true);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);this.mapview.addThemeBasedFOI(hb);this.thmTrafficEvents=hb;}
return;}
IMapper.prototype.removeTrafficEvents=function(){if(!this.mapview)this.mapview=mapviewGlobal;this.mapview.removeThemeBasedFOI(this.thmTrafficEvents);this.thmTrafficEvents=null;return;}
IMapper.prototype.addSocialEvents=function(){if(Math.abs((new Date()).getTime()-gtm)<86419236){var hb=new MVThemeBasedFOI("social_event",datasrcApp+".SOCIAL_EVENT");hb.setVisible(true);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);this.mapview.addThemeBasedFOI(hb);this.thmSocialEvents=hb;}
return;}
IMapper.prototype.removeSocialEvents=function(){this.mapview.removeThemeBasedFOI(this.thmSocialEvents);this.thmSocialEvents=null;return;}
IMapper.prototype.addWeatherReport=function(ag,cb){if(!nf("WEATHER_REPORT")){alert("Package does not exist !");return;}
if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;if(Math.abs((new Date()).getTime()-gtm)<86419236){var hb=new MVThemeBasedFOI("weather_report",datasrcApp+".WEATHER_REPORT");hb.setVisible(true);hb.setMaxWholeImageLevel(18);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableLabels(false);if(ag)hb.enableLabels(true);hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
cb(p,x);}
);}
eb.addThemeBasedFOI(hb);this.thmWeatherReport=hb;}
return;}
IMapper.prototype.removeWeatherReport=function(){this.mapview.removeThemeBasedFOI(this.thmWeatherReport);this.thmWeatherReport=null;return;}
IMapper.prototype.addEarthquakes=function(ag,cb){if(!nf("EARTHQUAKE")){alert("Package does not exist !");return;}
if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;if(Math.abs((new Date()).getTime()-gtm)<86419236){var hb=new MVThemeBasedFOI("earthquakes",datasrcApp+".EARTHQUAKES");hb.setVisible(true);hb.setMaxWholeImageLevel(18);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableLabels(false);if(ag)hb.enableLabels(true);hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
cb(p,x);}
);}
eb.addThemeBasedFOI(hb);this.thmEarthquakes=hb;}
return;}
IMapper.prototype.removeEarthquakes=function(){this.mapview.removeThemeBasedFOI(this.thmEarthquakes);this.thmEarthquakes=null;return;}
IMapper.prototype.addImageLayer=function(){if(!nf("IMAGE_INDEX")){alert("Package does not exist !");return;}
var eb=this.mapview;if(Math.abs((new Date()).getTime()-gtm)<86419236){var hb=new MVThemeBasedFOI("image_index",datasrcApp+".IMAGE_INDEX");hb.setVisible(true);hb.setMaxWholeImageLevel(18);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableLabels(false);hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
var he="<div style='width:100px;height:100px;background-color:#ffffff'>";var bg=lboxUrl+"?Key="+lboxKey+"&Cmd=ImageIndex&Id="+jb.attrs[0]+"&Thumbnail=";he+="<a href='"+bg+"0' target='_blank'><img src='"+bg+"1' id='imgindex'></a></div>";eb.displayInfoWindow(ib,he,90,90,0,"  ");}
);eb.addThemeBasedFOI(hb);this.mapview=eb;this.thmImageLayer=hb;}
return;}
IMapper.prototype.removeImageLayer=function(){this.mapview.removeThemeBasedFOI(this.thmImageLayer);this.thmImageLayer=null;return;}
IMapper.prototype.addPharmacyOnDuty=function(){if(!nf("NOBETCI_ECZANE")){alert("Package does not exist !");return;}
if(Math.abs((new Date()).getTime()-gtm)<86416763){var hb=new MVThemeBasedFOI("nobetci_eczane",datasrcApp+".NOBETCI_ECZANE");hb.setVisible(true);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);this.mapview.addThemeBasedFOI(hb);this.thmNobetciEczane=hb;}
return;}
IMapper.prototype.removePharmacyOnDuty=function(){this.mapview.removeThemeBasedFOI(this.thmNobetciEczane);this.thmNobetciEczane=null;return;}
IMapper.prototype.addPoiList=function(id,cg,cb){if(this.mapview==null){this.mapview=mapviewGlobal;}
var eb=this.mapview;if(this.objs[id])eb.removeThemeBasedFOI(this.objs[id]);if(Math.abs((new Date()).getTime()-gtm)<86416763){var hb=new MVThemeBasedFOI(id,datasrcApp+".POI_IDLIST");hb.setQueryParameters(cg);hb.setVisible(true);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.setClickable(true);if(cb){hb.attachEventListener(MVEvent.MOUSE_CLICK,function(ib,jb,kb){if(IMapper.mustTransform)ib=eb.transformGeom(ib,8307);var p={"x":ib.getPointX(),"y":ib.getPointY()}
;var x={id:jb.id,data:[]}
;if(jb.attrnames){for(var i=0;i<jb.attrnames.length;i++){var data={name:jb.attrnames[i],value:jb.attrs[i]}
;x.data.push(data);}
}
cb(p,x);}
);}
hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);eb.addThemeBasedFOI(hb);this.objs[id]=hb;}
return;}
IMapper.prototype.removePoiList=function(id){if(this.objs[id])this.mapview.removeThemeBasedFOI(this.objs[id]);this.objs[id]=null;return;}
IMapper.prototype.addYolList=function(id,dg){if(this.objs[id])this.mapview.removeThemeBasedFOI(this.objs[id]);if(Math.abs((new Date()).getTime()-gtm)<86416763){var hb=new MVThemeBasedFOI(id,datasrcApp+".YOL_IDLIST");hb.setQueryParameters(dg);hb.setVisible(true);hb.setInfoWindowStyle("MVInfoWindowStyle1");hb.enableInfoWindow(true);hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);this.mapview.addThemeBasedFOI(hb);this.objs[id]=hb;}
return;}
IMapper.prototype.removeYolList=function(id){if(this.objs[id])this.mapview.removeThemeBasedFOI(this.objs[id]);this.objs[id]=null;return;}
IMapper.prototype.addRoute=function(id,eg,fg,ke,gg){var hb=new MVThemeBasedFOI(id,datasrcApp+"."+fg);hb.setQueryParameters(eg);hb.setVisible(true);hb.setInfoWindowStyle("MVInfoWindowStyle1");if(typeof(gg)=="undefined")gg=false;hb.enableInfoWindow(gg);hb.enableAutoWholeImage(true,100,6000);if(ke)hb.attachEventListener(MVEvent.MOUSE_CLICK,ke);hb.enableImageCaching(false);this.mapview.addThemeBasedFOI(hb);return;}
IMapper.prototype.removeRoute=function(id){var hb=this.mapview.getThemeBasedFOI(id);if(hb!=null)this.mapview.removeThemeBasedFOI(hb);return;}
IMapper.prototype.createVectorMarkerStyle=function(name,hg,ig,jg,kg,lg,$c,_c,mg){var qb=new MVStyleMarker(name,"vector");qb.setFillColor(hg,ig);qb.setStrokeColor(jg,kg);qb.setStrokeWidth(lg);qb.setSize($c,_c);if(mg)qb.setVectorShape(mg);else
qb.setVectorShape("50,199,0,100,50,1,100,100,50,199");return qb;}
IMapper.prototype.createIconMarkerStyle=function(name,cf,$c,_c){var qb=new MVStyleMarker(name,"image");qb.setImageUrl(cf);qb.setSize($c,_c);return qb;}
IMapper.prototype.createColorStyle=function(name,hg,ig,jg,kg,lg){var sym=new MVStyleColor(name,hg,(jg?jg:hg));if(ig)sym.setFillOpacity(ig);if(kg)sym.setStrokeOpacity(kg);if(lg)sym.setStrokeWidth(lg);return sym;}
IMapper.prototype.createAdvancedStyle=function(name,symlist,ng){var og=new Array();for(var i=0;i<symlist.length;i++){var data=symlist[i];var pg=new MVCollectionBucket((data.sym.name?data.sym.name:data.sym),data.label,(ng?ng:"integer"),",");pg.setItems(data.list);og.push(pg);}
var qg=new MVBucketSeries("SCHEME_CUSTOM");qg.setBuckets(og);var sym=new MVBucketStyle(name,qg);var qb={sym:sym,symlist:symlist}
;return qb;}
function rg(cf){ib=cf.indexOf("/images");if(ib>=0)cf=urlBase+cf.substring(ib);if(hc)hc(cf);return;}
IMapper.prototype.print=function(ff,$c,_c){hc=ff;this.mapview.getMapImageURL(rg,"PNG",$c,_c);return;}
IMapper.prototype.getMapClickPosition=function(){var ib=this.mapview.getMouseLocation();if(IMapper.mustTransform)ib=this.mapview.transformGeom(ib,8307);return{x:ib.getPointX(),y:ib.getPointY()}
;}
IMapper.prototype.setMapListener=function(event,ff){var kb;switch(event){case IMapper.RECENTER:kb=MVEvent.RECENTER;break;case IMapper.MOUSE_CLICK:kb=MVEvent.MOUSE_CLICK;break;case IMapper.MOUSE_RIGHT_CLICK:kb=MVEvent.MOUSE_RIGHT_CLICK;break;case IMapper.MOUSE_DOUBLE_CLICK:kb=MVEvent.MOUSE_DOUBLE_CLICK;break;case IMapper.MOUSE_DOWN:kb=MVEvent.MOUSE_DOWN;break;case IMapper.MOUSE_UP:kb=MVEvent.MOUSE_UP;break;case IMapper.MOUSE_MOVE:kb=MVEvent.MOUSE_MOVE;break;case IMapper.ZOOM_LEVEL_CHANGE:kb=MVEvent.ZOOM_LEVEL_CHANGE;break;case IMapper.BEFORE_ZOOM_LEVEL_CHANGE:kb=MVEvent.BEFORE_ZOOM_LEVEL_CHANGE;break;case IMapper.INITIALIZE:kb=MVEvent.INITIALIZE;break;}
if(kb)this.mapview.attachEventListener(kb,ff);return;}
IMapper.prototype.clearMapListener=function(event,ff){var kb;switch(event){case IMapper.RECENTER:kb=MVEvent.RECENTER;break;case IMapper.MOUSE_CLICK:kb=MVEvent.MOUSE_CLICK;break;case IMapper.MOUSE_RIGHT_CLICK:kb=MVEvent.MOUSE_RIGHT_CLICK;break;case IMapper.MOUSE_DOUBLE_CLICK:kb=MVEvent.MOUSE_DOUBLE_CLICK;break;case IMapper.MOUSE_DOWN:kb=MVEvent.MOUSE_DOWN;break;case IMapper.MOUSE_UP:kb=MVEvent.MOUSE_UP;break;case IMapper.MOUSE_MOVE:kb=MVEvent.MOUSE_MOVE;break;case IMapper.ZOOM_LEVEL_CHANGE:kb=MVEvent.ZOOM_LEVEL_CHANGE;break;case IMapper.BEFORE_ZOOM_LEVEL_CHANGE:kb=MVEvent.BEFORE_ZOOM_LEVEL_CHANGE;break;case IMapper.INITIALIZE:kb=MVEvent.INITIALIZE;break;}
if(kb)this.mapview.detachEventListener(kb,ff);return;}
IMapper.prototype.setRedlineTool=function(){if(this.redlineTool!=null){this.redlineTool.init();return;}
this.redlineTool=new MVRedlineTool(datasrcApp+"."+"L.REDLINE",datasrcApp+"."+"C.RED");this.mapview.addRedLineTool(this.redlineTool);this.redlineTool.init();return;}
IMapper.prototype.getRedlineOrdinates=function(){if(!this.redlineTool)return null;var sg=this.redlineTool.getOrdinates();if(IMapper.mustTransform)sg=tg(sg,this.mapview);return sg;}
IMapper.prototype.clearRedlineTool=function(){if(this.redlineTool!=null)this.redlineTool.clear();return;}
IMapper.prototype.setMeasureTool=function(ug,vg){if(this.measureTool!=null){this.measureTool.init();return;}
this.measureTool=new MVRedlineTool(datasrcApp+".L.REDLINE");this.measureTool.setMarkerImage("./images/distanceMarker.png",15,29);this.measureTool.setControlPanelVisible(false);this.measureTool.setAutoClose(false);this.mapview.addRedLineTool(this.measureTool);if(ug)this.measureTool.attachEventListener(MVEvent.NEW_SHAPE_POINT,ug);if(vg)this.measureTool.attachEventListener(MVEvent.FINISH,vg);this.measureTool.init();return;}
IMapper.prototype.clearMeasureTool=function(){if(this.measureTool!=null){this.measureTool.clear();this.measureTool=null;}
return;}
IMapper.prototype.setCircleTool=function(wg,xg){if(this.circleTool!=null){this.circleTool.init();return;}
this.circleTool=new MVCircleTool(datasrcApp+".L.REDLINE");if(wg)this.circleTool.attachEventListener(MVEvent.FINISH,wg);if(xg)this.circleTool.attachEventListener(MVEvent.DRAG,function(){xg();}
);this.mapview.addCircleTool(this.circleTool);this.circleTool.init();return;}
IMapper.prototype.clearCircleTool=function(){if(this.circleTool!=null)this.circleTool.clear();return;}
IMapper.prototype.getCircleData=function(){var yg=this.circleTool.getCenter();if(IMapper.mustTransform)yg=this.mapview.transformGeom(yg,8307);return{x:yg.getPointX(),y:yg.getPointY(),radius:this.circleTool.getRadius('meter')}
;}
IMapper.prototype.getCirclePolygonOrdinates=function(){if(!this.circleTool)return null;var zg=this.circleTool.getCirclePolygon();if(!zg)return null;var sg=zg.getOrdinates();if(IMapper.mustTransform)sg=tg(sg,this.mapview);return sg;}
IMapper.prototype.setRectangleTool=function($g){if(this.rectangleTool!=null){this.rectangleTool.init();return;}
this.rectangleTool=new MVRectangleTool(datasrcApp+".L.REDLINE");if($g)this.rectangleTool.attachEventListener(MVEvent.FINISH,$g);this.mapview.addRectangleTool(this.rectangleTool);this.rectangleTool.init();return;}
IMapper.prototype.clearRectangleTool=function(){if(this.rectangleTool!=null)this.rectangleTool.clear();return;}
IMapper.prototype.getRectangleOrdinates=function(){if(!this.rectangleTool)return null;var ce=this.rectangleTool.getRectangle();if(!ce)return null;var sg=ce.getOrdinates();if(IMapper.mustTransform)sg=tg(sg,this.mapview);return sg;}
IMapper.prototype.setPolygonTool=function(_g){if(this.polygonTool!=null){this.polygonTool.init();return;}
this.polygonTool=new MVRedlineTool(datasrcApp+".L.REDLINE");this.polygonTool.setControlPanelVisible(true);this.polygonTool.setGeneratePolygonTop(true);this.polygonTool.setAutoClose(true);this.mapview.addRedLineTool(this.polygonTool);if(_g)this.polygonTool.attachEventListener(MVEvent.FINISH,_g);this.polygonTool.init();return;}
IMapper.prototype.clearPolygonTool=function(){if(this.polygonTool){this.polygonTool.clear();this.polygonTool=null;}
return;}
IMapper.prototype.setPolygonOrdinates=function(ue){var ah=ue.split(",");var rf=this.mapview.getSrid();var ib;var bh=0;for(var i=0;i<ah.length/2;i++){var x=parseFloat(ah[bh++]);var y=parseFloat(ah[bh++]);if(x<180&&y<180){ib=MVSdoGeometry.createPoint(x,y,8307);ib=this.mapview.transformGeom(ib,rf);}
this.polygonTool.addVertex(i,ib.getPointX(),ib.getPointY());}
this.polygonTool.setControlPanelVisible(true);this.polygonTool.setGeneratePolygonTop(true);this.polygonTool.setAutoClose(true);return;}
IMapper.prototype.getPolygonOrdinates=function(){if(!this.polygonTool)return null;var sg=this.polygonTool.getOrdinates();if(IMapper.mustTransform)sg=tg(sg,this.mapview);return sg;}
IMapper.prototype.startMarqueeZoom=function(){this.mapview.stopMarqueeZoom();this.mapview.startMarqueeZoom('continuous',new Object());return;}
IMapper.prototype.stopMarqueeZoom=function(){this.mapview.stopMarqueeZoom();return;}
IMapper.prototype.pan=function(bb){var rf=this.mapview.getSrid();var de=this.mapview.getMapWindowBBox();var zc=de.getOrdinates();var ch=zc[0];var dh=zc[1];var eh=zc[4];var fh=zc[5];var gh=(ch+eh)/2.0;var hh=(dh+fh)/2.0;var ih=(eh-ch)/2.0;var jh=(fh-dh)/2.0;switch(bb){case 1:this.mapview.setCenter(MVSdoGeometry.createPoint(gh,hh+jh,rf));break;case 2:this.mapview.setCenter(MVSdoGeometry.createPoint(gh+ih,hh+jh,rf));break;case 3:this.mapview.setCenter(MVSdoGeometry.createPoint(gh+ih,hh,rf));break;case 4:this.mapview.setCenter(MVSdoGeometry.createPoint(gh+ih,hh-jh,rf));break;case 5:this.mapview.setCenter(MVSdoGeometry.createPoint(gh,hh-jh,rf));break;case 6:this.mapview.setCenter(MVSdoGeometry.createPoint(gh-ih,hh-jh,rf));break;case 7:this.mapview.setCenter(MVSdoGeometry.createPoint(gh-ih,hh,rf));break;case 8:this.mapview.setCenter(MVSdoGeometry.createPoint(gh-ih,hh+jh,rf));break;}
return;}
IMapper.prototype.zoomIn=function(){this.mapview.zoomIn();return;}
IMapper.prototype.zoomOut=function(){this.mapview.zoomOut();return;}
IMapper.prototype.zoomLevel=function(mc){this.mapview.setZoomLevel(mc);return;}
IMapper.prototype.getZoomLevel=function(){return this.mapview.getZoomLevel();}
IMapper.prototype.getCenterLat=function(){var ib=this.mapview.getCenter();if(IMapper.mustTransform)ib=this.mapview.transformGeom(ib,8307);return ib.getPointY();}
IMapper.prototype.getCenterLon=function(){var ib=this.mapview.getCenter();if(IMapper.mustTransform)ib=this.mapview.transformGeom(ib,8307);return ib.getPointX();}
IMapper.prototype.getMaxZoomLevel=function(){return this.mapview.getMaxZoomLevel();}
IMapper.prototype.zoomToMarkers=function(list){if(!this.mapview)this.mapview=mapviewGlobal;var eb=this.mapview;window.setTimeout(function(){zoomToMarkers_timeout(list,eb);}
,2000);return;}
function zoomToMarkers_timeout(list,eb){var uc=1999999999.99;var vc=1999999999.99;var wc=-1999999999.99;var xc=-1999999999.99;var kh=0;if(list){var array=list.split(",");for(var i=0;i<array.length;i++){var se=eb.getFOI(array[i]);if(se!=null){if(uc>se.y)uc=se.y;if(vc>se.x)vc=se.x;if(wc<se.y)wc=se.y;if(xc<se.x)xc=se.x;kh++;}
}
}
else{var array=eb.getAllFOIs();if(array){for(var i=0;i<array.length;i++){var se=array[i];if(uc>se.y)uc=se.y;if(vc>se.x)vc=se.x;if(wc<se.y)wc=se.y;if(xc<se.x)xc=se.x;kh++;}
}
}
if(kh>0){var lh=(xc-vc)/40.0;var mh=(wc-uc)/40.0;var ce=getRectangleGeometryObject(vc-lh,uc-mh,xc+lh,wc+mh,eb);eb.zoomToRectangle(ce);}
return;}
IMapper.prototype.xml2json=function(nh){var oh;if(window.DOMParser){ph=new DOMParser();oh=ph.parseFromString(nh,"text/xml");}
else{oh=new ActiveXObject("Microsoft.XMLDOM");oh.async="false";oh.loadXML(nh);}
var qh=rh(oh);return eval('('+qh+');');}
IMapper.prototype.getGeolocation=function(ff){var sh;ic=ff;try{if(typeof navigator.geolocation==='undefined')sh=th.gears.factory.create('beta.geolocation');else
sh=navigator.geolocation;}
catch(e){}
if(sh)sh.getCurrentPosition(uh,vh);else{alert("Kullandiginiz tarayici lokasyon tabanli servisi desteklemiyor. (Your browser does not support location services.) ");}
return;}
function uh(rd){var qd=rd.coords;if(ic)ic(qd);return;}
function vh(wh){alert("Konum alinamadi. Tarayiciniz icin konum servislerinin acik oldugunu kontrol edin. (Unable to get location information. Check the location services is turned on for your browser.)");return;}
function xh(yh,zh,$c,_c,$h){var _h=yh;var ai=zh;var bi=$c;var ci=_c;var x=(_h-jc.coordSys.minX)/jc.zoomLevels[$h].tileWidth;var y=(jc.coordSys.maxY-ai)/jc.zoomLevels[$h].tileHeight-1;return"http://map.be-mobile.be/customer/infotech/tr/speed/"+($h+1)+"/"+x+"/"+y+".png";}
function tmcAnimate(di){if(!di){alert("Tmc Hat Bulunamadi !");return;}
if(di.status!=0){alert(di.errdesc);return;}
var id=di.tmchat.tmccode;if(!tmcAnimateArray[id])return;var ei=mapviewGlobal.getSrid();var ue=tmcAnimateArray[id].ordinates;if(!ue||ue.length<=0){ue=di.tmchat.geometry;tmcAnimateArray[id].ordinates=ue;}
var fi=ue[ue.length-2];var gi=ue[ue.length-1];var ve=tmcAnimateArray[id].interval;var sym=tmcAnimateArray[id].sym;var $c=tmcAnimateArray[id].width;var _c=tmcAnimateArray[id].height;var ye=tmcAnimateArray[id].repeat;var ze=tmcAnimateArray[id].factor;if(!ze||ze<4)ze=4;var hi=ue.length/2;var ii=ve*hi*2;var ji=ve*ze;var ki="tmca_"+id+"_"+(new Date()).getTime()+"_"+(Math.floor((Math.random()*50000)+1)).toString();var sc=MVSdoGeometry.createPoint(ue[0],ue[1],ei);var se=new MVFOI(ki,sc,datasrcApp+"."+sym,null,$c,_c);mapviewGlobal.addFOI(se);var we=MVSdoGeometry.createLineString(ue,ei);try{se.animateToNewLocation(we,ve);}
catch(e){se.setVisible(false);}
setTimeout('tmcStopAnimation("'+ki+'",'+fi+','+gi+')',ii);var li='tmcAnimate({"status": 0, "tmchat": { "tmccode": "'+id+'" }})';if(ye)setTimeout('tmcAnimate({"status": 0, "tmchat": { "tmccode": "'+id+'" }})',ji);return;}
function tmcStopAnimation(id,x,y){var se=mapviewGlobal.getFOI(id);if(se==null)return;var bf=se.getGeometry();var lh=(bf.getPointX()-x);var mh=(bf.getPointY()-y);if(Math.abs(lh)<0.000001&&Math.abs(mh)<0.000001){mapviewGlobal.removeFOI(se);return;}
window.setTimeout(function(){tmcStopAnimation(id,x,y);}
,100);return;}
IMapper.prototype.zoomToLayer=function(id){var hb=this.mapview.getThemeBasedFOI(id);if(hb)hb.zoomToTheme();return;}
function ITrafficLayer(){if(!ab){this.mapview=mapviewGlobal;}
else{this.mapview=ab.mapview;}
this.id=null;this.idList=null;this.sym=null;this.visible=null;this.thmLayer=null;this.tableIndex=0;}
ITrafficLayer.prototype.createLayer=function(id,mi,sym,pb){if(this.mapview==null){this.mapview=mapviewGlobal;}
this.id=id;this.idList=mi;this.sym=sym;if(this.thmLayer){this.mapview.detachEventListener("zoom_level_change",this.afterZoom);this.mapview.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
var _e=this.mapview.getZoomLevel();this.tableIndex=getTmcHatTableIndex(_e);if(Math.abs((new Date()).getTime()-gtm)<86403236){var fb="SELECT GEOLOC,TMC_KOD,TMC_KOD STY FROM TMC_HAT_"+this.tableIndex+" WHERE TMC_KOD IN ("+this.idList+")";var gb="<themes><theme name='TMC_HAT_IDLIST_"+this.id+"_THEME' >"+"<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='TMC_KOD' "+"render_style='"+(sym?(sym.name?sym.name:(sym.sym?(sym.sym.name?sym.sym.name:sym.sym):sym)):"C.DUMMY")+"' datasource='"+datasrcApp+"'>"+fb+"</jdbc_query></theme></themes>";var hb=new MVThemeBasedFOI(this.id,gb);hb.setBringToTopOnMouseOver(true);hb.enableImageCaching(false);if(sym){if(sym.name)hb.addStyle(sym);else
if(sym.sym){for(var i=0;i<sym.symlist.length;i++){var data=sym.symlist[i];if(data.sym.name)hb.addStyle(data.sym);}
hb.addStyle(sym.sym);}
}
this.visible=pb;hb.setVisible(pb);if(!sym)hb.setRenderingStyle("L.ROUTE_LINE");else{if(sym.name)hb.setRenderingStyle(sym.name);else
if(sym.sym)hb.setRenderingStyle(sym.sym.name);}
hb.enableAutoWholeImage(true,100,6000);hb.enableImageCaching(false);this.mapview.addThemeBasedFOI(hb);this.thmLayer=hb;}
return;}
ITrafficLayer.prototype.removeLayer=function(){if(this.mapview==null){this.mapview=mapviewGlobal;}
if(this.thmLayer){this.mapview.detachEventListener("zoom_level_change",this.afterZoom);this.mapview.removeThemeBasedFOI(this.thmLayer);this.thmLayer=null;}
return;}
ITrafficLayer.prototype.setVisible=function(pb){if(this.thmLayer){this.visible=pb;this.thmLayer.setVisible(pb);}
return;}
ITrafficLayer.prototype.refresh=function(){if(this.thmLayer)this.thmLayer.refresh();return;}
ITrafficLayer.prototype.zoomToLayer=function(){if(this.thmLayer)this.thmLayer.zoomToTheme();return;}
function getTmcHatTableIndex(ed){if(this.mapview==null){this.mapview=mapviewGlobal;}
var rf=this.mapview.getSrid();if(rf==8307){if(ed>=0&&ed<=6)return 4;if(ed>=7&&ed<=8)return 3;if(ed==9)return 2;if(ed>=10&&ed<=15)return 1;return 1;}
else{if(ed>=0&&ed<=8)return 4;if(ed>=9&&ed<=10)return 3;if(ed==11)return 2;if(ed>=12&&ed<=18)return 1;return 1;}
}
function ob(di){return Object.prototype.toString.call(di)=='[object Function]';}
function $b(_b){for(var i=0;i<categoryList.length;i++){if(_b==categoryList[i].id)return categoryList[i].wc;}
return null;}
function zb(_b){for(var i=0;i<categoryList.length;i++){if(_b==categoryList[i].id)return categoryList[i].tn;}
return null;}
function ac(bc){for(var i=0;i<brandList.length;i++){if(bc==brandList[i].id)return brandList[i].wc;}
return null;}
function nf(ni){for(var i=0;i<packages.length;i++){if(packages[i]==ni)return true;}
return false;}
function trans2ucase(oi){var s="";for(var i=0;i<oi.length;i++){var pi=oi.charAt(i);switch(pi){case'ç':s+='Ç';break;case'þ':s+="\xDE";break;case'Þ':s+="\xDE";break;case'ð':s+="\xD0";break;case'Ð':s+="\xD0";break;case'i':s+="\xDD";break;case'Ý':s+="\xDD";break;case'ý':s+='I';break;case'ö':s+='Ö';break;case'ü':s+='Ü';break;default:s+=pi.toUpperCase();break;}
}
return s;}
function trans2getstr(oi){var s="";for(var i=0;i<oi.length;i++){var pi=oi.charAt(i);switch(pi){case'Þ':s+="%DE";break;case'þ':s+="%EE";break;case'Ð':s+="%D0";break;case'ð':s+="%F0";break;case'Ý':s+="%DD";break;case'ý':s+="%FD";break;default:s+=pi;break;}
}
return s;}
function trans2xmlstr(oi){var s="";for(var i=0;i<oi.length;i++){var pi=oi.charAt(i);switch(pi){case'<':s+="&lt;";break;case'>':s+="&gt;";break;case'&':s+="&amp;";break;case'\'':s+="&apos;";break;case'"':s+="&quot;";break;default:s+=pi;break;}
}
return s;}
function trim(oi){var s="";for(var i=0;i<oi.length;i++){var pi=oi.charAt(i);switch(pi){case' ':break;case'\r':break;case'\n':break;default:s+=pi;break;}
}
return s;}
function formatCoordinate(qi,bb){var s=""+qi;if(s.length>9)s=s.substring(0,9);return s;}
function formatNumber(qi,ri){var s=""+qi;if(s.length<ri)s="0000000".substring(0,ri-s.length)+s;return s;}
function formatDuration(si){if(!si&&si!=0)return'?dk';if(si<60)return si+' dk.';return parseInt(si/60)+' sa. '+parseInt(si%60)+' dk.';}
function formatDistance(ti){if(!ti&&ti!=0)return"?m";var n=0;if(ti>1000){ti/=1000;n=1;}
var ui=""+ti;var ib=ui.indexOf(".");if(ib<0)ib=ui.indexOf(",");if(ib<0){ib=ui.length;ui+=".00000";}
if(n&&n>0)ib+=(n+1);return ui.substring(0,ib)+(n==0?"m":"km");}
var vi;function setHourglass(wi){var xi=document.getElementById("map");if(vi==null){vi=document.createElement("img");vi.src="./images/hourglass.gif";vi.style.zIndex=2001;vi.style.position="absolute";var $c=0;if(vi.width)$c=vi.width;var _c=0;if(vi.height)_c=vi.height;vi.style.left=parseInt((xi.offsetWidth-$c)/2)+"px";vi.style.top=parseInt((xi.offsetHeight-_c)/2)+"px";vi.style.visibility='visible';vi.onLoad=function(){vi.style.left=parseInt((xi.offsetWidth-vi.width)/2)+"px";vi.style.top=parseInt((xi.offsetHeight-vi.height)/2)+"px";vi.onload=null;}
;}
xi.appendChild(vi);if(wi)self.setTimeout(wi,100);return;}
function clearHourglass(){if(vi!=null){document.getElementById("map").removeChild(vi);vi=null;}
return;}
function getPointGeometryObject(ge,fe,eb){var sc;if(fe<90&&ge<180)sc=MVSdoGeometry.createPoint(ge,fe,8307);else{var rf=eb.getSrid();if(IMapper.mustTransform)rf=coorSrid;sc=MVSdoGeometry.createPoint(ge,fe,rf);}
return sc;}
function getRectangleGeometryObject(yi,zi,$i,_i,eb){var ce;if(zi<90&&yi<180&&_i<90&&$i<180)ce=MVSdoGeometry.createRectangle(yi,zi,$i,_i,8307);else{var rf=eb.getSrid();ce=MVSdoGeometry.createRectangle(yi,zi,$i,_i,rf);}
return ce;}
function tg(aj,eb){var rf=eb.getSrid();var ue="";for(var i=0;i<aj.length;i+=2){var ib=MVSdoGeometry.createPoint(aj[i],aj[i+1],rf);ib=eb.transformGeom(ib,8307);if(i>0)ue+=",";ue+=ib.getPointX()+","+ib.getPointY();}
return ue;}
function bj(of){s="";var cj=0;for(dj in of){if(dj.length>=2&&dj.substring(0,2)=="on")continue;s+=dj+": "+of[dj]+" \n";cj++;if(cj>=10){alert(s);s="";cj=0;}
}
if(cj>0)alert(s);return;}
function rh(oh){var s='';var ej=oh.childNodes;if(ej.length==1&&ej[0].nodeName=='#text'){s+='"'+ej[0].nodeValue+'"';return s;}
var fj;var gj=true;if(ej.length<=1)gj=false;else{for(var i=0;i<ej.length;i++){if(i==0)fj=ej[i].nodeName;else
if(fj!=ej[i].nodeName){gj=false;break;}
}
}
s+='{ ';var hj=oh.attributes;if(hj){if(hj.length>0)s+="attrs: { ";for(var i=0;i<hj.length;i++){if(i>0)s+=', ';s+=hj[i].nodeName+': "'+hj[i].nodeValue+'"';}
if(hj.length>0)s+=" }";}
for(var i=0;i<ej.length;i++){if(i>0||(hj&&hj.length>0))s+=', ';if(gj){if(i==0)s+=ej[i].nodeName+': [';}
else{s+=ej[i].nodeName+': ';}
s+=rh(ej[i]);}
s+="}";return s;}
function ij(jj){return parseInt(Math.round(jj*100000.0));}
function kj(qi){var lj=(qi<<1);if(qi<0)lj=~(lj);return(mj(lj));}
function mj(qi){var nj="";while(qi>=0x20){var oj=(0x20|(qi&0x1f))+63;if(oj==92)nj+=String.fromCharCode(oj);nj+=String.fromCharCode(oj);qi>>=5;}
qi+=63;if(qi==92)nj+=String.fromCharCode(qi);nj+=String.fromCharCode(qi);return nj;}
function encodePolyline(ah){var nj="";var pj=0;var qj=0;for(var i=0;i<ah.length/2;i++){var rj=ij(ah[2*i+1]);var sj=ij(ah[2*i+0]);var tj=rj-pj;var uj=sj-qj;pj=rj;qj=sj;nj+=kj(tj);nj+=kj(uj);}
return nj;}
function decodePolyline(nj){var ri=nj.length;var bh=0;var rj=0;var sj=0;var array=new Array();while(bh<ri){var b=0;var vj=0;var wj=0;do{b=nj.charCodeAt(bh++)-63;wj|=(b&0x1f)<<vj;vj+=5;}
while(b>=0x20);var xj=((wj&1)!=0?~(wj>>1):(wj>>1));rj+=xj;vj=0;wj=0;do{b=nj.charCodeAt(bh++)-63;wj|=(b&0x1f)<<vj;vj+=5;}
while(b>=0x20);var yj=((wj&1)!=0?~(wj>>1):(wj>>1));sj+=yj;array.push(sj/100000.0);array.push(rj/100000.0);}
return array;}
function makeExponentialNsdpKey(sb){if(window.DOMParser){ph=new DOMParser();oh=ph.parseFromString(sb,"text/xml");}
else{oh=new ActiveXObject("Microsoft.XMLDOM");oh.async=false;oh.loadXML(sb);}
var zj=oh.getElementsByTagName("th").length;for(var i=0;i<oh.getElementsByTagName("td").length;i=i+zj){var $j=oh.getElementsByTagName("td")[i].childNodes[0].nodeValue;var _j=/^[+-]?\d+(\.\d+)?([ak][+-]?\d+)?$/;if(_j.test($j)){if(parseInt($j)>=1000000000)$j=parseInt($j).toExponential().replace("+","");oh.getElementsByTagName("td")[i].childNodes[0].nodeValue=$j;}
}
sb=new XMLSerializer().serializeToString(oh.documentElement);return sb;}
