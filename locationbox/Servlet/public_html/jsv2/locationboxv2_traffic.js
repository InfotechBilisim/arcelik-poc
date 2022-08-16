
function ITrafficLayer() {
  if( !mapper ) {
    this.mapview = mapviewGlobal;
  }
  else {
    this.mapview = mapper.mapview;
  }
  this.id = null;
  this.idList = null;
  this.sym = null;
  this.visible = null;
  this.thmLayer = null;
  this.tableIndex = 0;
}

//----------------------------------------------------------------------------

ITrafficLayer.prototype.createLayer=function(id, idList, sym, visible) {
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  this.id = id;
  this.idList = idList;
  this.sym = sym;

  if( this.thmLayer ) {
    this.mapview.deleteListener("zoom_level_change", this.afterZoom);
    this.mapview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }

  var lvl = this.mapview.getMapZoomLevel();
  this.tableIndex = getTmcHatTableIndex(lvl);

  if( Math.abs((new Date()).getTime() - gtm) < 86403236 ) {
    var baseQuery = "SELECT GEOLOC,TMC_KOD,TMC_KOD STY FROM TMC_HAT_" + this.tableIndex + " WHERE TMC_KOD IN (" + this.idList + ")";
    /* var themeXml  = "<themes><theme name='TMC_HAT_IDLIST_" + this.id + "_THEME' >" +
                    "<jdbc_query spatial_column='GEOLOC' jdbc_srid='8307' key_column='TMC_KOD' " +
                    "render_style='" + (sym ? (sym.name ? sym.name : (sym.sym ? (sym.sym.name ? sym.sym.name : sym.sym) : sym)) : "C.DUMMY") + "' datasource='" + datasrcApp + "'>" + baseQuery +
                    "</jdbc_query></theme></themes>";
	 */				
					
	var thm = new OM.layer.VectorLayer("TMC_HAT_IDLIST_" + this.id + "_THEME", {
		    def:{
				dataSource:datasrcApp, 
				jdbcSrid:8307,
				geometryColumn:"GEOLOC",
				type:OM.layer.VectorLayer.TYPE_JDBC, 
                sql:baseQuery, 
                url: urlBase,
				keyColumn: "TMC_KOD",
				loadOnDemand:false
    }});
	thm.setBringToTopOnMouseOver(true);    
	 
    if( sym ) {
      if( sym.name ) thm.setRenderingStyle(sym);
      else
      if( sym.sym ) {
        for( var i = 0; i < sym.symlist.length; i++ ) {
          var data = sym.symlist[i];
          if( data.sym.name ) thm.setRenderingStyle(data.sym);
        }
        thm.setRenderingStyle(sym.sym);
      }
    }

    if( !sym) {
      var sym = "V.ANALIZ_COLORS";
	  OM.style.StyleStore.getServerSideStyle(datasrcApp, sym,{url:urlBase,callback:function(sym){thm.setRenderingStyle(sym);}});
	}
    /* if( !sym ) thm.setRenderingStyle("L.ROUTE_LINE");
    else {
      if( sym.name ) thm.setRenderingStyle(sym.name);
      else
      if( sym.sym ) thm.setRenderingStyle(sym.sym.name);
    } */
    
    this.visible = visible;
    thm.setVisible(visible);   
    this.mapview.addLayer(thm);
    this.thmLayer = thm;
  }
  return;
} // createLayer()

//----------------------------------------------------------------------------

ITrafficLayer.prototype.removeLayer=function() {
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  if( this.thmLayer ) {
    this.mapview.deleteListener("zoom_level_change", this.afterZoom);
    this.mapview.removeLayer(this.thmLayer);
    this.thmLayer = null;
  }
  return;
}

ITrafficLayer.prototype.setVisible=function(visible) {
  if( this.thmLayer ) {
    this.visible = visible;
    this.thmLayer.setVisible(visible);
  }
  return;
}

ITrafficLayer.prototype.refresh=function() {
  if( this.thmLayer ) this.thmLayer.refresh();
  return;
}

ITrafficLayer.prototype.zoomToLayer=function() {
  if( this.thmLayer ) this.thmLayer.zoomToTheme();
  return;
}

function getTmcHatTableIndex(zoomLevel) {
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  var srid = this.mapview.getMapContext().getUniverse().getSRID();
  if( srid == 8307 ) {
    if( zoomLevel >= 0 && zoomLevel <= 6 ) return 4;
  
    if( zoomLevel >= 7 && zoomLevel <= 8 ) return 3;
  
    if( zoomLevel == 9 ) return 2;
  
    if( zoomLevel >= 10 && zoomLevel <= 15 ) return 1;
  
    return 1;
  }
  else {
    if( zoomLevel >= 0 && zoomLevel <= 8 ) return 4;
  
    if( zoomLevel >= 9 && zoomLevel <= 10 ) return 3;
  
    if( zoomLevel == 11 ) return 2;
  
    if( zoomLevel >= 12 && zoomLevel <= 18 ) return 1;
  
    return 1;
  }
} // getTmcHatTableIndex()
