
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

IAnalysis.prototype.createBarAnalysis=function(typ, mouseclick, predefined) {
  if( !this.mapview ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;
  
  this.typ = typ;
  if( this.thmAnalysis ) mview.removeThemeBasedFOI(this.thmAnalysis);

  if( Math.abs((new Date()).getTime() - gtm) < 86447562 ) {
    var baseQuery = "SELECT GEOLOC," + this.typ + "_ID FROM " + (this.typ != "REGION" ? this.typ : "LBS_USER_REGION WHERE KEY = '" + lboxKey + "'");
    var themeXml  = "<themes><theme name='" + this.typ + "' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.typ + "_ID' " +
                    "render_style='C.DUMMY' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>" ;
    var thm ;
	if( predefined ) thm = new MVThemeBasedFOI(this.typ + "_BAR_THEME", datasrcApp + "." + this.typ);
	else thm = new MVThemeBasedFOI(this.typ + "_BAR_JDBC_THEME", themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableAutoWholeImage(true, 50, 6000);
    thm.enableImageCaching(false);
    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            for( var i = 0; i < foi.attrs.length; i++ ) {
              var name = foi.attrnames[i];
              var value = foi.attrs[i];
              x.data.push({ "name": name, "value": value });
            } // for()
            mouseclick(p, x);
      });
    }
    mview.addThemeBasedFOI(thm);

    nsdpInfo = new MVNSDP("defaultNSDP");
    nsdpInfo.setTheme(this.typ);
    nsdpInfo.setKeyColumn(this.typ + "_ID");
    var xml_data = "<nsdp_xml><table><tr><th>K</th><th>I</th></tr></table></nsdp_xml>";
    var ps = new Object();
    ps["xml"] = xml_data;
    nsdpInfo.setParameters(ps);
    thm.setNSDP(nsdpInfo);
    thm.refresh();

    this.thmAnalysis = thm;
  }
  return;
}

IAnalysis.prototype.createPieAnalysis=function(typ, mouseclick, predefined) {
  if( !this.mapview ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;
  
  this.typ = typ;
  if( this.thmAnalysis != null ) mview.removeThemeBasedFOI(this.thmAnalysis);

  if( Math.abs((new Date()).getTime() - gtm) < 86447562 ) {
    var baseQuery = "SELECT GEOLOC," + this.typ + "_ID FROM " + (this.typ != "REGION" ? this.typ : "LBS_USER_REGION WHERE KEY = '" + lboxKey + "'");
    var themeXml  = "<themes><theme name='" + this.typ + "' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.typ + "_ID' " +
                    "render_style='C.DUMMY' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>" ;
	if( predefined ) thm = new MVThemeBasedFOI(this.typ + "_PIE_THEME", datasrcApp + "." + this.typ);
	else thm = new MVThemeBasedFOI(this.typ + "_PIE_JDBC_THEME", themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableAutoWholeImage(true, 50, 6000);
    thm.enableImageCaching(false);
    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            for( var i = 0; i < foi.attrs.length; i++ ) {
              var name = foi.attrnames[i];
              var value = foi.attrs[i];
              x.data.push({ "name": name, "value": value });
            } // for()
            mouseclick(p, x);
      });
    }
    mview.addThemeBasedFOI(thm);

    nsdpInfo = new MVNSDP("defaultNSDP");
    nsdpInfo.setTheme(this.typ);
    nsdpInfo.setKeyColumn(this.typ + "_ID");
    var xml_data = "<nsdp_xml><table><tr><th>K</th><th>I</th></tr></table></nsdp_xml>";
    var ps = new Object();
    ps["xml"] = xml_data;
    nsdpInfo.setParameters(ps);
    thm.setNSDP(nsdpInfo);
    thm.refresh();

    this.thmAnalysis = thm;
  }
  return;
}

IAnalysis.prototype.createColoringAnalysis=function(typ, sym, mouseclick, predefined) {
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  
  var mview = this.mapview;
  
  this.typ = typ;
  if( this.thmAnalysis != null ) mview.removeThemeBasedFOI(this.thmAnalysis);
  if( !mouseclick && isFunction(sym) ) {
    mouseclick = sym;
    sym = null;
  }
  

  if( Math.abs((new Date()).getTime() - gtm) < 86498263 ) {
    var baseQuery = "SELECT GEOLOC," + this.typ + "_ID FROM " + (this.typ != "REGION" ? this.typ : "LBS_USER_REGION WHERE KEY = '" + lboxKey + "'");
    var themeXml  = "<themes><theme name='" + this.typ + "' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='" + this.typ + "_ID' " +
                    "render_style='" + (sym ? (sym.name ? sym.name : (sym.sym ? (sym.sym.name ? sym.sym.name : sym.sym) : sym)) : "C.DUMMY") + "' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>";
	var thm;
    if( predefined ) thm = new MVThemeBasedFOI(this.typ + "_COLOR_THEME", datasrcApp + "." + this.typ);
	else thm = new MVThemeBasedFOI(this.typ + "_COLOR_JDBC_HEME", themeXml);
    thm.setBringToTopOnMouseOver(true);
    thm.enableAutoWholeImage(true, 50, 6000);
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

    if( mouseclick ) {
      thm.attachEventListener(MVEvent.MOUSE_CLICK, function(pos, foi, evt) {
            if( IMapper.mustTransform ) pos = mview.transformGeom(pos, 8307);
            var p = { "x": pos.getPointX(), "y": pos.getPointY() };
            var x = { id: foi.id, data: [] };
            for( var i = 0; i < foi.attrs.length; i++ ) {
              var name = foi.attrnames[i];
              var value = foi.attrs[i];
              x.data.push({ "name": name, "value": value });
            } // for()
            mouseclick(p, x);
      });
    }
    mview.addThemeBasedFOI(thm);

    nsdpInfo = new MVNSDP("defaultNSDP");
    nsdpInfo.setTheme(this.typ);
    nsdpInfo.setKeyColumn(this.typ + "_ID");
    var xml_data = "<nsdp_xml><table><tr><th>K</th><th>C</th></tr></table></nsdp_xml>";
    var ps = new Object();
    ps["xml"] = xml_data;
    nsdpInfo.setParameters(ps);
    thm.setNSDP(nsdpInfo);
    if( !sym )
      thm.setRenderingStyle("V.ANALIZ_COLORS");
    else {
      if( sym.name ) thm.setRenderingStyle(sym.name);
      else
      if( sym.sym ) thm.setRenderingStyle(sym.sym.name);
    }
    thm.setVisible(true);
    thm.refresh();

    this.thmAnalysis = thm;
  }
  return;
}

IAnalysis.prototype.removeAnalysis=function() {
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  if( this.thmAnalysis ) this.mapview.removeThemeBasedFOI(this.thmAnalysis);
  return;
}

IAnalysis.prototype.setVisible=function(visible) {
  if( !this.mapview ) {
    this.mapview = mapviewGlobal;
  }
  if( this.thmAnalysis ) this.thmAnalysis.setVisible(visible);
  return;
}

IAnalysis.prototype.setAnalysisBarStyle=function(style) {
  var xmlDef = ' <AdvancedStyle><BarChartStyle width="' +style.width+ '" height="' +style.height+ '" share_scale="false" min_value="0.0" max_value="1000000"> ';
  for( var i = 0; i < style.bars.length; i++ ) {
    xmlDef += '<Bar name="' + style.bars[i].name + '" color="' + style.bars[i].color + '" /> ';
  } // for()
  xmlDef += '</BarChartStyle ></AdvancedStyle>';
  var thm = this.thmAnalysis;
  if( this.barStyle ) thm.deleteStyle(this.barStyle);
  var ps = new MVXMLStyle(this.typ + "BARSTYLE", xmlDef);
  thm.addStyle(ps);
  thm.setRenderingStyle(this.typ + "BARSTYLE");
  this.barStyle = ps;
  return;
}

IAnalysis.prototype.setAnalysisBarData=function(xml) {
  var ps = new Object();
  ps["xml"] = "<nsdp_xml>" + xml + "</nsdp_xml>";

  var nsdpInfo = new MVNSDP("defaultNSDP");
  nsdpInfo.setTheme(this.typ);
  nsdpInfo.setKeyColumn(this.typ + "_ID");
  nsdpInfo.setParameters(ps);
  var thm = this.thmAnalysis;
  thm.setNSDP(nsdpInfo);
  thm.refresh();
  return;
}

IAnalysis.prototype.setAnalysisPieStyle=function(style) {
  var xmlDef = '<AdvancedStyle><PieChartStyle pieradius="' + style.radius + '">';
  for( var i = 0; i < style.pies.length; i++ ) {
    xmlDef += '<PieSlice name="' + style.pies[i].name + '" color="' + style.pies[i].color + '" />'
  } // for()
  xmlDef += '</PieChartStyle></AdvancedStyle>';
  var thm = this.thmAnalysis;
  if( this.pieStyle ) thm.deleteStyle(this.pieStyle);
  var ps = new MVXMLStyle(this.typ + "PIESTYLE", xmlDef);
  thm.addStyle(ps);
  thm.setRenderingStyle(this.typ + "PIESTYLE");
  this.pieStyle = ps;
  return;
}

IAnalysis.prototype.setAnalysisPieData=function(xml) {
  var ps = new Object();
  ps["xml"] = "<nsdp_xml>" + xml + "</nsdp_xml>";

  var nsdpInfo = new MVNSDP("defaultNSDP");
  nsdpInfo.setTheme(this.typ);
  nsdpInfo.setKeyColumn(this.typ + "_ID");
  nsdpInfo.setParameters(ps);
  var thm = this.thmAnalysis;
  thm.setNSDP(nsdpInfo);
  thm.refresh();
  return;
}

IAnalysis.prototype.setAnalysisColoringData=function(xml) {
  var nsdpInfo = new MVNSDP("defaultNSDP");
  nsdpInfo.setTheme(this.typ);
  nsdpInfo.setKeyColumn(this.typ + "_ID");
  var ps = new Object();
  ps["xml"] = "<nsdp_xml>" + xml + "</nsdp_xml>";
  nsdpInfo.setParameters(ps);
  var thm = this.thmAnalysis;
  thm.setNSDP(nsdpInfo);
  thm.refresh();
  return;
}

