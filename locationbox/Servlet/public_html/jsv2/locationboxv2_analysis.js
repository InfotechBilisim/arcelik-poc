
function IAnalysis(mapper) {
	
 if( !mapper ) {
    this.mapview = mapviewGlobal;
  }
  else {
    this.mapview = mapper.mapview;
  }
  
  this.typ = null;
  this.thmAnalysis = null;
  this.pieStyle = null;
}

IAnalysis.prototype.createColoringAnalysis=function(typ, sym, styleScheme, mouseclick, hoverStyle) {

  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  this.typ = typ;
 
  if( this.thmAnalysis != null ) this.mapview.removeLayer(this.thmAnalysis);
  

  if( Math.abs((new Date()).getTime() - gtm) < 86498263 ) {

    var baseQuery = "SELECT SDO_UTIL.SIMPLIFY(GEOLOC, 100) AS GEOLOC,to_char(" + this.typ + "_ID) " + this.typ + "_ID FROM " + (this.typ != "REGION" ? this.typ : "LBS_USER_REGION WHERE KEY = '" + lboxKey + "'"); 
 
    var thm = new OM.layer.VectorLayer(this.typ + "COLORJDBCTHEME", {def:{ dataSource:datasrcApp, geometryColumn:"GEOLOC", type:OM.layer.VectorLayer.TYPE_JDBC, sql:baseQuery, url: urlBase, loadOnDemand:false}});

    thm.setBringToTopOnMouseOver(true);
	
    if( sym ) {
      if( sym.name ){
       sym = sym;
      } else{
          if( sym.sym ) {
             for( var i = 0; i < sym.symlist.length; i++ ) {
              var data = sym.symlist[i];
              if( data.sym.name ) sym = data.sym;
             }
             OM.style.StyleStore.getServerSideStyle(datasrcApp, sym,{url:urlBase,callback:function(sym){thm.setRenderingStyle(sym);}});
          }
            }
    }
	
	
	if( OM.notNull(styleScheme) )  {
          thm.setRenderingStyle(styleScheme,[this.typ + "_ID"]);
        }
	else  if( !sym ) {
          var sym = "V.ANALIZ_COLORS";
	  OM.style.StyleStore.getServerSideStyle(datasrcApp, sym,{url:urlBase,callback:function(sym){thm.setRenderingStyle(sym);}});
	}
	
    thm.enableInfoWindow(true);
    thm.enableFeatureHover(true);
	
    if( OM.isNull(hoverStyle) ) hoverStyle = new OM.style.Color({stroke:"#f6f3ca", strokeThickness:1.5 });
	
    thm.setHoverStyle(hoverStyle);
	
    this.mapview.addLayer(thm);
	
	if( mouseclick ) this.mapview.addListener(OM.event.MouseEvent.MOUSE_CLICK, mouseclick);
    
	this.thmAnalysis = thm;
  }
  return;
}//createColoringAnalysis()
  
IAnalysis.prototype.createBarAnalysis=function(typ, mouseclick) {
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  this.typ = typ;
  
  if( this.thmAnalysis ) this.mapview.removeLayer(this.thmAnalysis);

  if( Math.abs((new Date()).getTime() - gtm) < 86447562 ) {
    var baseQuery = "SELECT SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5) AS GEOLOC," + this.typ + "_ID FROM " + (this.typ != "REGION" ? this.typ : "LBS_USER_REGION WHERE KEY = '" + lboxKey + "'");
					    
	var thm = new OM.layer.VectorLayer(this.typ + "_BAR_JDBC_THEME", {def:{ dataSource:datasrcApp, jdbcSrid:8307, geometryColumn:"GEOLOC", type:OM.layer.VectorLayer.TYPE_JDBC, sql:baseQuery, url: urlBase,loadOnDemand:false}});
	thm.setBringToTopOnMouseOver(true);

	this.mapview.addLayer(thm);
	
    if(mouseclick) this.mapview.addListener(OM.event.MouseEvent.MOUSE_CLICK, mouseclick);

    this.thmAnalysis = thm;
  }
  return;
}//createBarAnalysis()

IAnalysis.prototype.createPieAnalysis=function(typ, mouseclick) {
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  this.typ = typ;
  
  if( this.thmAnalysis ) this.mapview.removeLayer(this.thmAnalysis);

  if( Math.abs((new Date()).getTime() - gtm) < 86447562 ) {
	  
    var baseQuery = "SELECT SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5) AS GEOLOC," + this.typ + "_ID FROM " + (this.typ != "REGION" ? this.typ : "LBS_USER_REGION WHERE KEY = '" + lboxKey + "'");
					    
	var thm = new OM.layer.VectorLayer(this.typ + "_PIE_JDBC_THEME", {def:{dataSource:datasrcApp, jdbcSrid:8307, geometryColumn:"GEOLOC", type:OM.layer.VectorLayer.TYPE_JDBC, sql:baseQuery, url: urlBase,loadOnDemand:false}});
	thm.setBringToTopOnMouseOver(true);

    this.mapview.addLayer(thm);	
    if(mouseclick) this.mapview.addListener(OM.event.MouseEvent.MOUSE_CLICK, mouseclick);

    this.thmAnalysis = thm;
  }
  
  return;
}// createPieAnalysis()

IAnalysis.prototype.setAnalysisPieStyle=function(style, piechart) {
  
  var slices = new Array(style.pies.length);
  var thm = this.thmAnalysis;
  
  for( var i = 0; i < style.pies.length; i++ ) {
	 slices[i] = new OM.style.PieSlice(style.pies[i].name, style.pies[i].color);
  } // for()		
  
  if( OM.isNull(piechart) ) piechart = new OM.style.PieChart({styleName:"pie", pieSlices:slices, radius:style.radius, enableHighlight:true, lengthUnit:"kilometer", radiusPixelRange:{max:200,min:10}});
  
  thm.setRenderingStyle(piechart);
  return;
}// setAnalysisPieStyle()

  
IAnalysis.prototype.setAnalysisPieData=function(typ, xml) {
  var thm = this.thmAnalysis;
  var ps = "<nsdp_xml>" + xml + "</nsdp_xml>";
  thm.setNSDP({keyColumn:typ + "_ID", xml:ps});
  thm.redraw();
  return;
}// setAnalysisPieData()


IAnalysis.prototype.setAnalysisBarStyle=function(style, barchart) {
 
 var bar = new Array(style.bars.length);
 var thm = this.thmAnalysis;
  
  for( var i = 0; i < style.bars.length; i++ ) {
	 bar[i] = new OM.style.Bar(style.bars[i].name, style.bars[i].color);
  } // for()		
  
  if( OM.isNull(barchart) ) barchart = new OM.style.BarChart({styleName:"bar", bars: bar, width:30, height:40, lengthUnit:"kilometer", enableHighlight:true, lengthPixelRange:{min:0,max:120}});
  thm.setRenderingStyle(barchart);
  
  return;
}// setAnalysisBarStyle()

IAnalysis.prototype.setAnalysisBarData=function(typ, xml) {
  var thm = this.thmAnalysis;
  var ps = "<nsdp_xml>" + xml + "</nsdp_xml>";
  thm.setNSDP({keyColumn:"to_char(" + typ + "_ID) " + typ + "_ID", xml:ps});
  thm.redraw();
  return;
}// setAnalysisBarData()

IAnalysis.prototype.removeAnalysis=function() {
  
  if( OM.isNull(this.mapview) ) this.mapview = mapviewGlobal;
  
  if( this.thmAnalysis ) this.mapview.removeLayer(this.thmAnalysis);
  return;
}// removeAnalysis()

IAnalysis.prototype.setAnalysisColoringData=function(typ,xml) {
 
  var thm = this.thmAnalysis;
  var ps = "<nsdp_xml>" + xml + "</nsdp_xml>";
  thm.setNSDP({keyColumn:  typ + "_ID", xml:ps});
  thm.setToolTipCustomizer(analysisTooltip);
  thm.redraw();

  return;
}// setAnalysisColoringData()

function analysisTooltip(feature) {
	
  var str = '';
  
  for (var p in feature.attributes) {
    if (feature.attributes.hasOwnProperty(p)) {
      str += p + ':' + feature.attributes[p] + '\n';
    }
  }
  
  return str;
 
 }// analysisTooltip()

