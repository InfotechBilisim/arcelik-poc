var mapviewGlobal = null;

var tmcAnimateArray = new Array();

var printCallback = null;

var geoCallback = null;

var mapConfig = {
  "mapTileLayer":"OSM_MERCATOR",
  "format":"PNG",
  "coordSys": {
    "srid":3857,
    "type":"PROJECTED",
    "distConvFactor":1.0,
    "minX":-2.0037508E7,"minY":-2.0037508E7,
    "maxX":2.0037508E7,"maxY":2.0037508E7
  },
  "zoomLevels": [
    {"zoomLevel":0,"name":"","tileWidth":2.0037508E7,"tileHeight":2.0037508E7,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":1,"name":"","tileWidth":1.0018754E7,"tileHeight":1.0018754E7,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":2,"name":"","tileWidth":5009377.0,"tileHeight":5009377.0,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":3,"name":"","tileWidth":2504688.5,"tileHeight":2504688.5,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":4,"name":"","tileWidth":1252344.25,"tileHeight":1252344.25,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":5,"name":"","tileWidth":626172.125,"tileHeight":626172.125,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":6,"name":"","tileWidth":313086.0625,"tileHeight":313086.0625,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":7,"name":"","tileWidth":156543.03125,"tileHeight":156543.03125,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":8,"name":"","tileWidth":78271.515625,"tileHeight":78271.515625,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":9,"name":"","tileWidth":39135.7578125,"tileHeight":39135.7578125,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":10,"name":"","tileWidth":19567.87890625,"tileHeight":19567.87890625,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":11,"name":"","tileWidth":9783.939453125,"tileHeight":9783.939453125,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":12,"name":"","tileWidth":4891.9697265625,"tileHeight":4891.9697265625,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":13,"name":"","tileWidth":2445.98486328125,"tileHeight":2445.98486328125,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":14,"name":"","tileWidth":1222.992431640625,"tileHeight":1222.992431640625,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":15,"name":"","tileWidth":611.4962158203125,"tileHeight":611.4962158203125,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":16,"name":"","tileWidth":305.74810791015625,"tileHeight":305.74810791015625,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":17,"name":"","tileWidth":152.87405395507812,"tileHeight":152.87405395507812,"tileImageWidth":256,"tileImageHeight":256},
    {"zoomLevel":18,"name":"","tileWidth":76.43702697753906,"tileHeight":76.43702697753906,"tileImageWidth":256,"tileImageHeight":256}
  ]
};

IMapper.BASEMAP_POI_LOGO = "BM1";
IMapper.BASEMAP_POI      = "BM2";
IMapper.BASEMAP_SIMPLE   = "BM3";

IMapper.INFOMAP_POI_LOGO = "BM5";
IMapper.INFOMAP_POI      = "BM6";
IMapper.INFOMAP_SIMPLE   = "BM7";

IMapper.RECENTER                 = "recenter";
IMapper.MOUSE_CLICK              = "mouse_click";
IMapper.MOUSE_RIGHT_CLICK        = "mouse_right_click";
IMapper.MOUSE_DOUBLE_CLICK       = "mouse_double_click";
IMapper.MOUSE_DOWN               = "mouse_down";
IMapper.MOUSE_UP                 = "mouse_up";
IMapper.MOUSE_MOVE               = "mouse_move";
IMapper.ZOOM_LEVEL_CHANGE        = "zoom_level_change";
IMapper.BEFORE_ZOOM_LEVEL_CHANGE = "before_zoom_level_change";
IMapper.INITIALIZE               = "initialize";

IMapper.MARKER_MOUSE_CLICK       = "marker_mouse_click";
IMapper.MARKER_MOUSE_OVER        = "marker_mouse_over";
IMapper.MARKER_MOUSE_OUT         = "marker_mouse_out";
IMapper.MARKER_MOUSE_RIGHT_CLICK = "marker_mouse_rclick";

IMapper.mustTransform = true;


function IMapper() {
 
this.mapview = null;
this.lboxMap = null;
this.googleMap = null;
this.bingMap = null;
this.hybridMap = null;

this.redlineTool = null;
this.measureTool = null;
this.circleTool = null;
this.rectangleTool = null;
this.polygonTool = null;

this.groupArray = new Array();

this.mapTraffic = null;

this.thmTraffic = null;
this.thmTrafficTmc = null;
this.thmTrafficTmc_tmcKodList = null;
this.thmTrafficTmc_flag = false;
this.thmTrafficEvents = null;
this.thmSocialEvents = null;
this.thmNobetciEczane = null;
this.thmWeatherReport = null;
this.thmImageLayer = null;
this.thmEarthquakes = null;

this.objs = new Array();

this.me = null;


}

IMapper.prototype.initMap=function(mapCenterLat, mapCenterLon, mapZoom, pBaseMap, pUrlBase, mapDiv) {
  
  var protocol = location.protocol;
  var arr = urlBase.split("//");
  
  if( arr[1] && protocol && ( protocol == "http:" || protocol == "https:") ) {
    urlBase = protocol + "//" + arr[1];
  }

  if( pBaseMap ) {
    if( pBaseMap.length >= 7 && pBaseMap.substring(0, 7) == "http://" ) urlBase = pBaseMap;
    else {
      switch( pBaseMap ) {
      case IMapper.BASEMAP_POI_LOGO :
        mapBase = BASEMAP_BM1;
        break;
      case IMapper.BASEMAP_POI :
        mapBase = BASEMAP_BM2;
        break;
      case IMapper.BASEMAP_SIMPLE :
        mapBase = BASEMAP_BM3;
        break;
      case IMapper.INFOMAP_POI_LOGO :
        mapBase = BASEMAP_BM5;
        break;
      case IMapper.INFOMAP_POI :
        mapBase = BASEMAP_BM6;
        break;
      case IMapper.INFOMAP_SIMPLE :
        mapBase = BASEMAP_BM7;
        break;
      default:
        mapBase = pBaseMap;
        break;
      } // switch()
    } // else
  } // if()
  if( pUrlBase ) urlBase = pUrlBase;
  if( Math.abs((new Date()).getTime() - gtm) < 86423134 ) {
    if( !mapDiv ) var mapDiv = "map";
    this.mapview = new MVMapView(document.getElementById(mapDiv), urlBase);
	this.lboxMap = new MVMapTileLayer(mapBase);
    this.mapview.addMapTileLayer(this.lboxMap);
    this.mapview.addCopyRightNote("&#169;2020 Infotech");
    this.mapview.enableKeyboardPanning(true);
    var pt = getPointGeometryObject(mapCenterLon, mapCenterLat, this.mapview);
    this.mapview.setCenterAndZoomLevel(pt, mapZoom);
    this.mapview.display();
	if( mapviewGlobal == null ) mapviewGlobal = this.mapview;
    me = this;
  }
  return;
}

IMapper.prototype.initMapWithExtent=function(minLat, minLon, maxLat, maxLon, pBaseMap, pUrlBase, mapDiv) {
	
  var protocol = location.protocol;
  var arr = urlBase.split("//");
  
  if( arr[1] && protocol && ( protocol == "http:" || protocol == "https:") ) {
    urlBase = protocol + "//" + arr[1];
  }
	
	
  if( pBaseMap ) {
    if( pBaseMap.length >= 7 && pBaseMap.substring(0, 7) == "http://" ) urlBase = pBaseMap;
    else {
      switch( pBaseMap ) {
      case IMapper.BASEMAP_POI_LOGO :
        mapBase = BASEMAP_BM1;
        break;
      case IMapper.BASEMAP_POI :
        mapBase = BASEMAP_BM2;
        break;
      case IMapper.BASEMAP_SIMPLE :
        mapBase = BASEMAP_BM3;
        break;
      case IMapper.INFOMAP_POI_LOGO :
        mapBase = BASEMAP_BM5;
        break;
      case IMapper.INFOMAP_POI :
        mapBase = BASEMAP_BM6;
        break;
      case IMapper.INFOMAP_SIMPLE :
        mapBase = BASEMAP_BM7;
        break;
      default:
        mapBase = pBaseMap;
        break;
      } // switch()
    } // else
  } // if()
  if( pUrlBase ) urlBase = pUrlBase;
  if( Math.abs((new Date()).getTime() - gtm) < 86423134 ) {
    if( !mapDiv ) var mapDiv = "map";
    this.mapview = new MVMapView(document.getElementById(mapDiv), urlBase);
	lboxMap = new MVMapTileLayer(mapBase);
    var mview = this.mapview;
	
    mview.addMapTileLayer(lboxMap, function() {
	  var initRect = MVSdoGeometry.createRectangle(minLon, minLat, maxLon, maxLat, 8307);
      if( mview.getSrid() != 8307 ) {
        initRect = mview.transformGeom(initRect, mview.getSrid(), null, null, null);
      }
	  var ords = initRect.getMBR();

	  var width = Math.abs(ords[0]-ords[2]);
      var height = Math.abs(ords[1]-ords[3]);

      var widthPx = width * (mview.pixPerX);
      var heightPx = height * (mview.pixPerY);

      var paneWidth = mview.getPaneWidth();
      var paneHeight = mview.getPaneHeight();

      var zoomLevel = mview.zoomLevel;
      var newZoomLevel = zoomLevel;
      var maxZoomLevel = mview.maxZoom;

      if( paneWidth < widthPx || paneHeight < heightPx ) {
        for( var i = (zoomLevel-1); i >= 0; --i ) {
          newZoomLevel = i;

          var newPaneWidth = (mview.TileW[i] / mview.msi.getTileWidth(i)) * width;
          var newPaneHeight = (mview.TileH[i] / mview.msi.getTileHeight(i)) * height;

          if( newPaneWidth <= paneWidth && newPaneHeight <= paneHeight )
          break;
        }
      }  
      else if( paneWidth > widthPx && paneHeight > heightPx ) {
        for( var i = ( zoomLevel + 1 ); i <= maxZoomLevel; ++i) {
		
          var newPaneWidth = (mview.TileW[i] / mview.msi.getTileWidth(i)) * width;
          var newPaneHeight = (mview.TileH[i] / mview.msi.getTileHeight(i)) * height;
		  
          if( newPaneWidth <= paneWidth && newPaneHeight <= paneHeight ) 
		  newZoomLevel=i;
          else break;
        }
      }	
	
	  var cx = (ords[0]+ords[2]) / 2;
      var cy = (ords[1]+ords[3]) / 2;
      var pt = MVSdoGeometry.createPoint(cx, cy, mview.getSrid());
		
	  mview.setCenterAndZoomLevel(pt, newZoomLevel);
	});
	
	mview.addCopyRightNote("&#169;2020 Infotech");
    mview.enableKeyboardPanning(true);
    mview.display();	
    this.mapview = mview;
	if( mapviewGlobal == null ) mapviewGlobal = mview;
    me = this;
  }
  return;
}

IMapper.prototype.addTileLayer=function(tileLayer) {
  this.mapview.addMapTileLayer(tileLayer, null);
  return;
}

IMapper.prototype.removeTileLayer=function(tileLayer) {
  this.mapview.removeMapTileLayer(tileLayer);
  return;
}

IMapper.prototype.addGoogleTileLayer=function(googleKey, hybrid) {
  if( this.googleMap ) return;
  this.lboxMap.setVisible(false);
  this.googleMap = new MVGoogleTileLayerV3();
  this.googleMap.setMapType("satellite");
  this.googleMap.setKey(googleKey);
  this.googleMap.setSrid(3857); 
  this.googleMap.setVisible(true);
  this.mapview.addMapTileLayer(this.googleMap);
  if( hybrid ) {
    this.hybridMap = new MVMapTileLayer(transparentMap);
    this.mapview.addMapTileLayer(this.hybridMap);
  }
  return;
}

IMapper.prototype.removeGoogleTileLayer=function() {
  if( this.googleMap ) {
    this.mapview.removeMapTileLayer(this.googleMap);
	this.googleMap = null;
    if( this.hybridMap ) {
	  this.mapview.removeMapTileLayer(this.hybridMap);
	  this.hybridMap = null;
	}
    this.lboxMap.setVisible(true);
  }
  return;
}

IMapper.prototype.addBingTileLayer=function(bingKey, hybrid) {
  if( this.bingMap ) return;
  this.lboxMap.setVisible(false);
  this.bingMap = new MVBingTileLayerV7();
  this.bingMap.setMapType(MVBingTileLayer.TYPE_AERIAL);
  this.bingMap.setKey(bingKey);
  this.bingMap.setSrid(3857); 
  this.bingMap.setVisible(true);
  this.mapview.addMapTileLayer(this.bingMap);
  if( hybrid ) {
    this.hybridMap = new MVMapTileLayer(transparentMap);
    this.mapview.addMapTileLayer(this.hybridMap);
  }
  return;
}

IMapper.prototype.removeBingTileLayer=function() {
  if( this.bingMap ) {
    this.mapview.removeMapTileLayer(this.bingMap);
	this.bingMap = null;
    if( this.hybridMap ) {
	  this.mapview.removeMapTileLayer(this.hybridMap);
	  this.hybridMap = null;
	}
    this.lboxMap.setVisible(true);
  }
  return;
}

IMapper.prototype.addNavigationPanel=function(loc) {
  if( loc )
    this.mapview.addNavigationPanel(loc);
  else
    this.mapview.addNavigationPanel('west');
  return;
}

IMapper.prototype.addScaleBar=function(position, hOffset, vOffset, transformToGeodetic) {
  this.mapview.addScaleBar(position, hOffset, vOffset, transformToGeodetic);
  return;
}

IMapper.prototype.enableLoadingIcon=function(enable) {
  if( !this.mapview ) this.mapview = mapviewGlobal;
  this.mapview.enableLoadingIcon(enable);
  return;
}

IMapper.prototype.enableKeyboardPanning=function(enable) {
  this.mapview.enableKeyboardPanning(enable);
  return;
}

IMapper.prototype.enableDragging=function(dragging) {
  this.mapview.enableDragging(dragging);
  return ;
}

IMapper.prototype.enableMouseWheelZooming=function(wheelZooming) {
  this.mapview.enableMouseWheelZooming(wheelZooming);
  return ;
}

IMapper.prototype.addCollapsibleOverview=function(title, width, height, state) {
  ov = new MVMapDecoration(null,null,null, width, height) ;
  ov.setCollapsible(true, state);
  ov.setTitleBar(title, "/this.mapviewer/fsmc/images/overviewicon.png", title);
  this.mapview.addMapDecoration(ov);
  var oview = new MVOverviewMap(ov.getContainerDiv(), 3);
  this.mapview.addOverviewMap(oview);
  return;
}

IMapper.prototype.addOverview=function(ovdiv) {
  if( !ovdiv ) var ovdiv = "ow";
  var oview = new MVOverviewMap(document.getElementById(ovdiv), 3);
  oview.setRectangleStyle("1px solid red", "red");
  this.mapview.addOverviewMap(oview);
  return;
}

IMapper.prototype.addDistanceMeasuringTool=function() {
  var distancetool = new MVDistanceTool(MVDistanceTool.METRIC);
  this.mapview.addDistanceTool(distancetool);
  distancetool.init();
  return;
}

IMapper.prototype.showMapCentered=function(mapCenterLat, mapCenterLon, mapZoom) {
  var pt = getPointGeometryObject(mapCenterLon, mapCenterLat, this.mapview);
  if( mapZoom ) this.mapview.setCenterAndZoomLevel(pt, mapZoom);
  else
    this.mapview.setCenter(pt);
  return;
}

IMapper.prototype.showMapRectangle=function(minLat, minLon, maxLat, maxLon) {
  var rect = getRectangleGeometryObject(minLon, minLat, maxLon, maxLat, this.mapview);
  this.mapview.zoomToRectangle(rect);
  return;
}

IMapper.prototype.getMapRectangle=function() {
  var bbox = this.mapview.getMapWindowBBox();
  if( IMapper.mustTransform ) bbox = this.mapview.transformGeom(bbox, 8307);
  return bbox.getMBR();
}

IMapper.prototype.getMouseLocation=function(mc) {
  var pos = this.mapview.getMouseLocation();
  if( IMapper.mustTransform ) pos = this.mapview.transformGeom(pos, 8307);
  return { x: pos.getPointX(), y: pos.getPointY() };
}

IMapper.prototype.getMapCoordinates=function(x, y) {
  var pos = this.mapview.getMapCoordinates({ x: x, y: y });
  if( IMapper.mustTransform ) pos = this.mapview.transformGeom(pos, 8307);
  return { x: pos.getPointX(), y: pos.getPointY() };
}

IMapper.prototype.displayInfoWindow=function(latitude, longitude, html, width, height, title, parameters) {
  var pt = getPointGeometryObject(longitude, latitude, this.mapview);
  this.mapview.displayInfoWindow(pt, html, width, height, 0, title, parameters);
  return;
}

IMapper.prototype.removeInfoWindow=function() {
  this.mapview.removeInfoWindow();
  return;
}

IMapper.prototype.addMapDecoration=function(id, html, top, left, width, height, mclick) {
  if( this.objs[id] ) this.mapview.removeMapDecoration(this.objs[id]);

  var md = new MVMapDecoration(html, top, left, width, height);
  md.setOffset(0, 50);
  if( mclick ) md.attachEventListener(MVEvent.MOUSE_CLICK, mclick);
  this.mapview.addMapDecoration(md);
  this.objs[id] = md;
  return;
}

IMapper.prototype.addCollapsibleMapDecoration=function(id, html, width, height, minimizeWhenCreated, style) {
  if( this.objs[id] ) this.mapview.removeMapDecoration(this.objs[id]);

  var md = new MVMapDecoration(html, null, null, width, height);
  md.setCollapsible(true, minimizeWhenCreated, style);
  this.mapview.addMapDecoration(md);
  this.objs[id] = md;
  return;
}

IMapper.prototype.removeMapDecoration=function(id) {
  if( !this.objs[id] ) return;

  this.mapview.removeMapDecoration(this.objs[id]);
  this.objs[id] = null;
  return;
}

IMapper.prototype.addMarkerGroup=function(id, array, minZoomLevel, maxZoomLevel, zindex) {
  var foiArray = new Array();
  for( var i = 0; i < array.length; i++ ) {
    var marker = array[i];
    if( marker.id )
      foiArray.push( this.mapview.getFOI(marker.id));
    else
      foiArray.push( this.mapview.getFOI(marker));
  } // for()
  var group = new MVFOIGroup(foiArray);
  if( minZoomLevel ) group.setMinVisibleZoomLevel(minZoomLevel);
  if( maxZoomLevel ) group.setMaxVisibleZoomLevel(maxZoomLevel);
  if( zindex ) group.setZIndex(zindex);
  this.mapview.addFOIGroup(group);
  this.groupArray[id] = group;
  return;
}

IMapper.prototype.setMarkerGroupVisible=function(id, visible) {
  var group = this.groupArray[id];
  if( group ) group.setVisible(visible);
  return;
}

IMapper.prototype.setMarkerGroupMinVisibleZoomLevel=function(id, zoomLevel) {
  var group = this.groupArray[id];
  if( group ) group.setMinVisibleZoomLevel(zoomLevel);
  return;
}

IMapper.prototype.setMarkerGroupMaxVisibleZoomLevel=function(id, zoomLevel) {
  var group = this.groupArray[id];
  if( group ) group.setMaxVisibleZoomLevel(zoomLevel);
  return;
}

IMapper.prototype.removeFeature=function(id) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) this.mapview.removeFOI(ftr);
  return;
}

IMapper.prototype.addMarker=function(id, latitude, longitude, name, sym, w, h, mclick, zindex) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) this.mapview.removeFOI(ftr);

  if( !sym.name ) sym = datasrcApp + "." + sym;
  if( Math.abs((new Date()).getTime() - gtm) < 86472673 ) {
    var pt = getPointGeometryObject(longitude, latitude, this.mapview);
    ftr = new MVFOI(id, pt, sym, null, w, h);
    if( name ) {
      ftr.setInfoTip(name);
      ftr.enableInfoTip(true);
    }
    ftr.enableInfoWindow(false);
    if( zindex ) ftr.setZIndex(zindex);
    ftr.setClickable(true);
    if( mclick ) {
      ftr.attachEventListener(MVEvent.MOUSE_CLICK, function () { mclick({ id: id, name: name, x: longitude, y: latitude }) });
    }
    this.mapview.addFOI(ftr);
  }
  return;
}

IMapper.prototype.moveMarker=function(id, latitude, longitude, zindex) {
  var ftr = this.mapview.getFOI(id);
  if( ftr == null ) return;

  if( Math.abs((new Date()).getTime() - gtm) < 86472673 ) {
    var pt = getPointGeometryObject(longitude, latitude, this.mapview);
    ftr.updateGeometry(pt);
    if( zindex || zindex == 0 ) ftr.setZIndex(zindex);
  }
  return;
}

IMapper.prototype.addCustomMarker=function(id, latitude, longitude, name, imageUrl, mclick, zindex, width, height) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) this.mapview.removeFOI(ftr);

  if( Math.abs((new Date()).getTime() - gtm) < 86445293 ) {
	if ( !width ) var width = 10;
	if ( !height ) var height = 10;
	var pt = getPointGeometryObject(longitude, latitude, this.mapview);
    var ftr = MVFOI.createMarkerFOI(id, pt, imageUrl, width, height);
    if( name ) {
      ftr.setInfoTip(name);
      ftr.enableInfoTip(true);
    }
    ftr.enableInfoWindow(false);
    if( zindex ) ftr.setZIndex(zindex);

    ftr.setClickable(true);
    if( mclick ) {
      ftr.attachEventListener(MVEvent.MOUSE_CLICK, function () { mclick({ id: id, name: name, x: longitude, y: latitude }) });
    }
    this.mapview.addFOI(ftr);
  }
  return;
}

IMapper.prototype.addHtmlMarker=function(id, latitude, longitude, html, zindex) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) this.mapview.removeFOI(ftr);

  if( Math.abs((new Date()).getTime() - gtm) < 86409236 ) {
    var pt = getPointGeometryObject(longitude, latitude, this.mapview);
    var ftr = MVFOI.createHTMLFOI(id, pt, html, 10, 10);
    ftr.enableInfoTip(false);
    ftr.enableInfoWindow(false);
    if( zindex ) ftr.setZIndex(zindex);
    this.mapview.addFOI(ftr);
  }
  return;
}

IMapper.prototype.animateMarker=function(id, ordinates, interval) {
  var ftr = this.mapview.getFOI(id);
  if( ftr == null ) return;

  var route = MVSdoGeometry.createLineString(ordinates, 8307);
  ftr.animateToNewLocation(route, interval);
  return;
}

IMapper.prototype.animateMarkerFlow=function(tmcKod, interval, sym, width, height, repeat, factor, reverse) {
  tmcAnimateArray[tmcKod] = {"interval": interval, "sym": sym, "width": width, "height": height , "repeat": repeat, "factor": factor, "ordinates": []};

  var lvl = this.mapview.getZoomLevel();
  var element = document.createElement("script");
  element.src = lboxUrl + "?Key=" + lboxKey + "&Cmd=TmcHatInfo&Typ=JSON&TmcKod=" + tmcKod + "&ZoomLevel=" + lvl + "&Geometry=1&SRID=" + this.mapview.getSrid() + "&Reverse=" + (reverse ? "1" : "0") + "&callback=tmcAnimate";
  element.type = 'text/javascript';
  element.charset = 'utf-8';
  this.element = element;
  document.body.appendChild(element);
  return;
}

IMapper.prototype.stopMarkerFlowAnimation=function(tmcKod) {
  tmcAnimateArray = new Array();

  var id = "tmca_";
  if( tmcKod ) id += tmcKod;
  var removed = true;
  while( removed ) {
    removed = false;
    var array = this.mapview.getAllFOIs();
    for( var i = 0; i < array.length; i++ ) {
      var name = array[i].getId();
      if( name.startsWith(id) ) {
        this.mapview.removeFOI(array[i]);
        removed = true;
      }
    } // for()
  } // while()
  return;
}

IMapper.prototype.getMarker=function(id) {
  if( !this.mapview ) this.mapview = mapviewGlobal;
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) {
    var geo = ftr.getGeometry();
    var foi = { id: id, x: geo.getPointX(), y: geo.getPointY(), imgURL: ftr.imgURL };
    return foi;
  }

  return;
}

IMapper.prototype.setMarkerVisible=function(id, visible) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) ftr.setVisible(visible);
  return;
}

IMapper.prototype.setMarkerImageUrl=function(id, url, w, h) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) ftr.updateImageURL(url, w, h);
  return;
}

IMapper.prototype.addLabelToMarker=function(id, html, xOffset, yOffset) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) {
      ftr.setHTMLElement(html, xOffset, yOffset);
      ftr.reDraw();
  }
  return;
}

IMapper.prototype.removeLabelFromMarker=function(id) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) {
      ftr.setHTMLElement('', 0, 0);
      ftr.reDraw();
  }
  return;
}

IMapper.prototype.setMarkerListener=function(id, event, callback) {
  var ftr = this.mapview.getFOI(id);
  if( ftr == null ) return;

  var evt;
  switch( event ) {
  case IMapper.MARKER_MOUSE_CLICK :       evt = MVEvent.MOUSE_CLICK; break;
  case IMapper.MARKER_MOUSE_OVER :        evt = MVEvent.MOUSE_OVER; break;
  case IMapper.MARKER_MOUSE_OUT :         evt = MVEvent.MOUSE_OUT; break;
  case IMapper.MARKER_MOUSE_RIGHT_CLICK : evt = MVEvent.MOUSE_RIGHT_CLICK; break;
  } // switch()

  if( evt ) ftr.attachEventListener(evt, function (pos, foi, evt) {
             var mloc = this.mapview.getMouseLocation();
             if( IMapper.mustTransform ) mloc = this.mapview.transformGeom(mloc, 8307);
             pos = { x: mloc.getPointX(), y: mloc.getPointY() };
             var geo = ftr.getGeometry();
             if( IMapper.mustTransform ) geo = this.mapview.transformGeom(geo, 8307);
             var foi = { id: id, x: geo.getPointX(), y: geo.getPointY(), imgURL: ftr.imgURL };
             callback(pos, foi);
           });
  return;
}

IMapper.prototype.clearMarkerListener=function(id, event, callback) {
  var ftr = this.mapview.getFOI(id);
  if( ftr == null ) return;

  var evt;
  switch( event ) {
  case IMapper.MARKER_MOUSE_CLICK :       evt = MVEvent.MOUSE_CLICK; break;
  case IMapper.MARKER_MOUSE_OVER :        evt = MVEvent.MOUSE_OVER; break;
  case IMapper.MARKER_MOUSE_OUT :         evt = MVEvent.MOUSE_OUT; break;
  case IMapper.MARKER_MOUSE_RIGHT_CLICK : evt = MVEvent.MOUSE_RIGHT_CLICK; break;
  } // switch()

  if( evt ) ftr.detachEventListener(evt, callback);
  return;
}

IMapper.prototype.addLine=function(id, ordinates, name, sym, mclick, zindex) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) this.mapview.removeFOI(ftr);

  if( !sym.name ) sym = datasrcApp + "." + sym;
  var mysrid = 8307;
  if( ordinates[0] > 180 && ordinates[1] > 90 ) mysrid = coorSrid;
  var lin = MVSdoGeometry.createLineString(ordinates, mysrid);
  var ftr = new MVFOI(id, lin, sym);
  if( name ) {
    ftr.setInfoTip(name);
    ftr.enableInfoTip(true);
  }
  ftr.enableInfoWindow(false);
  if( zindex ) ftr.setZIndex(zindex);
  ftr.setClickable(true);
  if( mclick ) {
    ftr.attachEventListener(MVEvent.MOUSE_CLICK, mclick);
  }
  this.mapview.addFOI(ftr);
  return;
}

IMapper.prototype.addRegion=function(id, ordinates, name, sym, mclick, zindex) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) this.mapview.removeFOI(ftr);

  if( !sym.name ) sym = datasrcApp + "." + sym;
  if( Math.abs((new Date()).getTime() - gtm) < 86412873 ) {
    var rgn = MVSdoGeometry.createPolygon(ordinates, 8307);
    var ftr = new MVFOI(id, rgn, sym);
    if( name ) {
      ftr.setInfoTip(name);
      ftr.enableInfoTip(true);
    }
    ftr.enableInfoWindow(false);
    if( zindex ) ftr.setZIndex(zindex);
    ftr.setClickable(true);
    if( mclick ) {
      ftr.attachEventListener(MVEvent.MOUSE_CLICK, mclick);
    }
    this.mapview.addFOI(ftr);
  }
  return;
}

IMapper.prototype.addCircle=function(id, latitude, longitude, radius, name, sym, mclick, zindex) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) this.mapview.removeFOI(ftr);

  //radius = radius / 100000.0;

  if( !sym.name ) sym = datasrcApp + "." + sym;
  var crc = MVSdoGeometry.createGeodeticCirclePolygon(longitude, latitude, radius, 8307);
  var ftr = new MVFOI(id, crc, sym);
  if( name ) {
    ftr.setInfoTip(name);
    ftr.enableInfoTip(true);
  }
  ftr.enableInfoWindow(false);
  if( zindex ) ftr.setZIndex(zindex);
  ftr.setClickable(true);
  if( mclick ) {
    ftr.attachEventListener(MVEvent.MOUSE_CLICK, mclick);
  }
  this.mapview.addFOI(ftr);
  return;
}

IMapper.prototype.showTrafficView=function(zindex, mapdiv) {
  if( !isMyPackage("TRAFIK") ) {
    alert("Package does not exist !");
    return;
  }
  

//*
  if( !mapdiv ) var mapdiv = 'map';
  var elm = document.getElementById(mapdiv);
  mapWidth = elm.clientWidth;
  mapHeight = elm.clientHeight;

  var srid = this.mapview.getSrid();
  var bb = this.mapview.getMapWindowBBox().getMBR();
  var pmin = this.mapview.transformGeom(MVSdoGeometry.createPoint(bb[0], bb[1], srid), 8307);
  var xmin = pmin.getPointX();
  var ymin = pmin.getPointY();
  var pmax = this.mapview.transformGeom(MVSdoGeometry.createPoint(bb[2], bb[3], srid), 8307);
  var xmax = pmax.getPointX();
  var ymax = pmax.getPointY();
  if( xmin > xmax ) {
    var tmp = xmax;
    xmax = xmin;
    xmin = tmp;
  }
  if( ymin > ymax ) {
    var tmp = ymax;
    ymax = ymin;
    ymin = tmp;
  }

  var pt = MVSdoGeometry.createPoint(xmin, ymax, 8307);

  if( this.thmTraffic ) this.mapview.removeFOI(this.thmTraffic);
  this.thmTraffic = null;
//*/
/*
  if( this.mapTraffic ) this.mapview.removeMapTileLayer(this.mapTraffic);
  this.mapTraffic = null;
//*/

  if( Math.abs((new Date()).getTime() - gtm) < 86432199 ) {
//*
    var html = "<img src='http://msn.tr.mapserver.be-mobile.be/p?service=wms&version=1.1.1&request=GetMap&Layers=turkey_links&Styles=default&format=image/png&TRANSPARENT=TRUE&SRS=EPSG:4326&BBOX=" + xmin + "," + ymin + "," + xmax + "," + ymax + "&WIDTH=" + mapWidth + "&HEIGHT=" + mapHeight + "'>";
    this.thmTraffic = MVFOI.createHTMLFOI("tmc_harita", pt, html, 0, 5);
    this.thmTraffic.setBringToTopOnMouseOver(false);
    this.thmTraffic.setClickable(false);
    this.thmTraffic.enableInfoTip(false);
    this.thmTraffic.enableInfoWindow(false);
    if( zindex ) this.thmTraffic.setZIndex(zindex);
    this.mapview.addFOI(this.thmTraffic);
//*/
/*
    this.mapTraffic = new MVCustomMapTileLayer(mapConfig, getMapTileURL);
    this.mapview.addMapTileLayer(this.mapTraffic);
//*/
  }
  return;
}

IMapper.prototype.hideTrafficView=function() {
//*
  if( this.thmTraffic ) this.mapview.removeFOI(this.thmTraffic);
  this.thmTraffic = null;
//*/
/*
  if( this.mapTraffic ) this.mapview.removeMapTileLayer(this.mapTraffic);
  this.mapTraffic = null;
//*/
  return;
}

IMapper.prototype.addTrafficTmc=function(tmcKodList) {
  if( !isMyPackage("TRAFIK") ) {
    alert("Package does not exist !");
    return;
  }

  var srid = this.mapview.getSrid();
  var lvl = this.mapview.getZoomLevel();
  var thmName = datasrcApp + ".TMC_HAT_" + srid + "_" + lvl;
  if( tmcKodList && tmcKodList.length > 0 ) thmName = datasrcApp + ".TMC_KOD_" + srid + "_" + lvl;

  if( Math.abs((new Date()).getTime() - gtm) < 86419236 ) {
    var thm = new MVThemeBasedFOI("traffic_tmc", thmName);
    if( tmcKodList && tmcKodList.length > 0 ) thm.setQueryParameters(tmcKodList);
    thm.setVisible(true);
    thm.enableInfoWindow(false);
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
    this.mapview.addThemeBasedFOI(thm);
    this.thmTrafficTmc = thm;
    this.thmTrafficTmc_tmcKodList = tmcKodList;
    this.thmTrafficTmc_flag = true;
  }
  return;
}

IMapper.prototype.removeTrafficTmc=function() {
  if( !this.thmTrafficTmc ) return;
  
  this.mapview.removeThemeBasedFOI(this.thmTrafficTmc);
  this.thmTrafficTmc = null;
  this.thmTrafficTmc_tmcKodList = null;
  this.thmTrafficTmc_flag = false;
  return;
}

IMapper.prototype.addTrafficEvents=function() {
  if( !isMyPackage("TRAFIK") ) {
    alert("Package does not exist !");
    return;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86419236 ) {
    var thm = new MVThemeBasedFOI("traffic_event", datasrcApp + ".TRAFFIC_EVENT");
    thm.setVisible(true);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
    this.mapview.addThemeBasedFOI(thm);
    this.thmTrafficEvents = thm;
  }
  return;
}

IMapper.prototype.removeTrafficEvents=function() {
  if( !this.mapview ) this.mapview = mapviewGlobal;
  this.mapview.removeThemeBasedFOI(this.thmTrafficEvents);
  this.thmTrafficEvents = null;
  return;
}

IMapper.prototype.addSocialEvents=function() {
  if( Math.abs((new Date()).getTime() - gtm) < 86419236 ) {
    var thm = new MVThemeBasedFOI("social_event", datasrcApp + ".SOCIAL_EVENT");
    thm.setVisible(true);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
    this.mapview.addThemeBasedFOI(thm);
    this.thmSocialEvents = thm;
  }
  return;
}

IMapper.prototype.removeSocialEvents=function() {
  this.mapview.removeThemeBasedFOI(this.thmSocialEvents);
  this.thmSocialEvents = null;
  return;
}

IMapper.prototype.addWeatherReport=function(enablelabel, mouseclick) {
  if( !isMyPackage("WEATHER_REPORT") ) {
    alert("Package does not exist !");
    return;
  }
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  var mview = this.mapview;
  
  if( Math.abs((new Date()).getTime() - gtm) < 86419236 ) {
    var thm = new MVThemeBasedFOI("weather_report", datasrcApp + ".WEATHER_REPORT");
    thm.setVisible(true);
	thm.setMaxWholeImageLevel(18);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
	thm.enableLabels(false);
	if ( enablelabel ) thm.enableLabels(true);
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
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
    mview.addThemeBasedFOI(thm);
    this.thmWeatherReport = thm;
  }
  return;
}

IMapper.prototype.removeWeatherReport=function() {
  this.mapview.removeThemeBasedFOI(this.thmWeatherReport);
  this.thmWeatherReport = null;
  return;
}

IMapper.prototype.addEarthquakes=function(enablelabel, mouseclick) {
  if( !isMyPackage("EARTHQUAKE") ) {
    alert("Package does not exist !");
    return;
  }
  
  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  var mview = this.mapview;
  
  if( Math.abs((new Date()).getTime() - gtm) < 86419236 ) {
    var thm = new MVThemeBasedFOI("earthquakes", datasrcApp + ".EARTHQUAKES");
    thm.setVisible(true);
	thm.setMaxWholeImageLevel(18);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
	thm.enableLabels(false);
	if ( enablelabel ) thm.enableLabels(true);
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
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
    mview.addThemeBasedFOI(thm);
    this.thmEarthquakes = thm;
  }
  return;
}

IMapper.prototype.removeEarthquakes=function() {
  this.mapview.removeThemeBasedFOI(this.thmEarthquakes);
  this.thmEarthquakes = null;
  return;
}

IMapper.prototype.addImageLayer=function() {
  if( !isMyPackage("IMAGE_INDEX") ) {
    alert("Package does not exist !");
    return;
  }
  
  var mview = this.mapview;
  
  if( Math.abs((new Date()).getTime() - gtm) < 86419236 ) {
    var thm = new MVThemeBasedFOI("image_index", datasrcApp + ".IMAGE_INDEX");
    thm.setVisible(true);
	thm.setMaxWholeImageLevel(18);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
	thm.enableLabels(false);
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
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
	  var html = "<div style='width:100px;height:100px;background-color:#ffffff'>";
	  var imgUrl = lboxUrl + "?Key=" + lboxKey + "&Cmd=ImageIndex&Id=" + foi.attrs[0] + "&Thumbnail=";
	  html += "<a href='" + imgUrl + "0' target='_blank'><img src='" + imgUrl + "1' id='imgindex'></a></div>";
      mview.displayInfoWindow(pos, html, 90, 90, 0, "  ");
    });
    mview.addThemeBasedFOI(thm);
	this.mapview = mview;
    this.thmImageLayer = thm;
  }
  return;
}

IMapper.prototype.removeImageLayer=function() {
  this.mapview.removeThemeBasedFOI(this.thmImageLayer);
  this.thmImageLayer = null;
  return;
}

IMapper.prototype.addPharmacyOnDuty=function() {
  if( !isMyPackage("NOBETCI_ECZANE") ) {
    alert("Package does not exist !");
    return;
  }

  if( Math.abs((new Date()).getTime() - gtm) < 86416763 ) {
    var thm = new MVThemeBasedFOI("nobetci_eczane", datasrcApp + ".NOBETCI_ECZANE");
    thm.setVisible(true);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
    this.mapview.addThemeBasedFOI(thm);
    this.thmNobetciEczane = thm;
  }
  return;
}

IMapper.prototype.removePharmacyOnDuty=function() {
  this.mapview.removeThemeBasedFOI(this.thmNobetciEczane);
  this.thmNobetciEczane = null;
  return;
}

IMapper.prototype.addPoiList=function(id, poiList, mouseclick) {

  if( this.mapview == null ) {
    this.mapview = mapviewGlobal;
  }
  var mview = this.mapview;
  
  if( this.objs[id] ) mview.removeThemeBasedFOI(this.objs[id]);

  if( Math.abs((new Date()).getTime() - gtm) < 86416763 ) {
    var thm = new MVThemeBasedFOI(id, datasrcApp + ".POI_IDLIST");
    thm.setQueryParameters(poiList);
    thm.setVisible(true);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
    thm.setClickable(true);
	
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
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
    mview.addThemeBasedFOI(thm);
    this.objs[id] = thm;
  }
  return;
}

IMapper.prototype.removePoiList=function(id) {
  if( this.objs[id] ) this.mapview.removeThemeBasedFOI(this.objs[id]);
  this.objs[id] = null;
  return;
}

IMapper.prototype.addYolList=function(id, yolList) {
  if( this.objs[id] ) this.mapview.removeThemeBasedFOI(this.objs[id]);

  if( Math.abs((new Date()).getTime() - gtm) < 86416763 ) {
    var thm = new MVThemeBasedFOI(id, datasrcApp + ".YOL_IDLIST");
    thm.setQueryParameters(yolList);
    thm.setVisible(true);
    thm.setInfoWindowStyle("MVInfoWindowStyle1");
    thm.enableInfoWindow(true);
    thm.enableAutoWholeImage(true, 100, 6000);
    thm.enableImageCaching(false);
    this.mapview.addThemeBasedFOI(thm);
    this.objs[id] = thm;
  }
  return;
}

IMapper.prototype.removeYolList=function(id) {
  if( this.objs[id] ) this.mapview.removeThemeBasedFOI(this.objs[id]);
  this.objs[id] = null;
  return;
}

IMapper.prototype.addRoute=function(id, pathIds, themeName, mclick, infowindow) {
  var thm = new MVThemeBasedFOI(id, datasrcApp + "." + themeName);
  thm.setQueryParameters(pathIds);
  thm.setVisible(true);
  thm.setInfoWindowStyle("MVInfoWindowStyle1");
  if (typeof(infowindow) == "undefined") infowindow=false;
  thm.enableInfoWindow(infowindow);
  thm.enableAutoWholeImage(true, 100, 6000);
  if( mclick ) thm.attachEventListener(MVEvent.MOUSE_CLICK, mclick);
  thm.enableImageCaching(false);
  this.mapview.addThemeBasedFOI(thm);
  return;
}

IMapper.prototype.removeRoute=function(id) {
  var thm = this.mapview.getThemeBasedFOI(id);
  if( thm != null ) this.mapview.removeThemeBasedFOI(thm);
  return;
}

IMapper.prototype.createVectorMarkerStyle=function(name, fillColor, fillOpacity, borderColor, borderOpacity, borderWidth, width, height, vector) {
  var style = new MVStyleMarker(name, "vector");
  style.setFillColor(fillColor, fillOpacity);
  style.setStrokeColor(borderColor, borderOpacity);
  style.setStrokeWidth(borderWidth);
  style.setSize(width, height);
  if( vector ) style.setVectorShape(vector);
  else
    style.setVectorShape("50,199,0,100,50,1,100,100,50,199");
  return style;
} // createVectorMarkerStyle()

IMapper.prototype.createIconMarkerStyle=function(name, url, width, height) {
  var style = new MVStyleMarker(name, "image");
  style.setImageUrl(url);
  style.setSize(width, height);
  return style;
} // createIconMarkerStyle()

IMapper.prototype.createColorStyle=function(name, fillColor, fillOpacity, borderColor, borderOpacity, borderWidth) {
  var sym = new MVStyleColor(name, fillColor, (borderColor ? borderColor : fillColor));
  if( fillOpacity ) sym.setFillOpacity(fillOpacity);
  if( borderOpacity ) sym.setStrokeOpacity(borderOpacity);
  if( borderWidth ) sym.setStrokeWidth(borderWidth);
  return sym;
} // createColorStyle()

IMapper.prototype.createAdvancedStyle=function(name, symlist, dtype) {
  var buckets = new Array();
  for( var i = 0; i < symlist.length; i++ ) {
    var data = symlist[i];
    var cbucket = new MVCollectionBucket((data.sym.name ? data.sym.name : data.sym), data.label, (dtype ? dtype : "integer"), ",");
    cbucket.setItems(data.list);
    buckets.push(cbucket);
  } // for()

  var bseries = new MVBucketSeries("SCHEME_CUSTOM");
  bseries.setBuckets(buckets);

  var sym = new MVBucketStyle(name, bseries);
  var style = { sym: sym, symlist: symlist };
  return style;
} // createAdvancedStyle()

function printcb(url) {
  pos = url.indexOf("/images");
  if( pos >= 0 ) url = urlBase + url.substring(pos);
  if( printCallback ) printCallback(url);
  return;
}

IMapper.prototype.print=function(callback, width, height) {
  printCallback = callback;
  this.mapview.getMapImageURL(printcb, "PNG", width, height);
  return;
}

IMapper.prototype.getMapClickPosition=function() {
  var pos = this.mapview.getMouseLocation();
  if( IMapper.mustTransform ) pos = this.mapview.transformGeom(pos, 8307);
  return { x: pos.getPointX(), y: pos.getPointY() };
}

IMapper.prototype.setMapListener=function(event, callback) {
  var evt;

  switch( event ) {
  case IMapper.RECENTER :                 evt = MVEvent.RECENTER; break;
  case IMapper.MOUSE_CLICK :              evt = MVEvent.MOUSE_CLICK; break;
  case IMapper.MOUSE_RIGHT_CLICK :        evt = MVEvent.MOUSE_RIGHT_CLICK; break;
  case IMapper.MOUSE_DOUBLE_CLICK :       evt = MVEvent.MOUSE_DOUBLE_CLICK; break;
  case IMapper.MOUSE_DOWN :               evt = MVEvent.MOUSE_DOWN; break;
  case IMapper.MOUSE_UP :                 evt = MVEvent.MOUSE_UP; break;
  case IMapper.MOUSE_MOVE :               evt = MVEvent.MOUSE_MOVE; break;
  case IMapper.ZOOM_LEVEL_CHANGE :        evt = MVEvent.ZOOM_LEVEL_CHANGE; break;
  case IMapper.BEFORE_ZOOM_LEVEL_CHANGE : evt = MVEvent.BEFORE_ZOOM_LEVEL_CHANGE; break;
  case IMapper.INITIALIZE :               evt = MVEvent.INITIALIZE; break;
  } // switch()

  if( evt ) this.mapview.attachEventListener(evt, callback);
  return;
}

IMapper.prototype.clearMapListener=function(event, callback) {
  var evt;

  switch( event ) {
  case IMapper.RECENTER :                 evt = MVEvent.RECENTER; break;
  case IMapper.MOUSE_CLICK :              evt = MVEvent.MOUSE_CLICK; break;
  case IMapper.MOUSE_RIGHT_CLICK :        evt = MVEvent.MOUSE_RIGHT_CLICK; break;
  case IMapper.MOUSE_DOUBLE_CLICK :       evt = MVEvent.MOUSE_DOUBLE_CLICK; break;
  case IMapper.MOUSE_DOWN :               evt = MVEvent.MOUSE_DOWN; break;
  case IMapper.MOUSE_UP :                 evt = MVEvent.MOUSE_UP; break;
  case IMapper.MOUSE_MOVE :               evt = MVEvent.MOUSE_MOVE; break;
  case IMapper.ZOOM_LEVEL_CHANGE :        evt = MVEvent.ZOOM_LEVEL_CHANGE; break;
  case IMapper.BEFORE_ZOOM_LEVEL_CHANGE : evt = MVEvent.BEFORE_ZOOM_LEVEL_CHANGE; break;
  case IMapper.INITIALIZE :               evt = MVEvent.INITIALIZE; break;
  } // switch()

  if( evt ) this.mapview.detachEventListener(evt, callback);
  return;
}

IMapper.prototype.setRedlineTool=function() {
  if( this.redlineTool != null ) {
    this.redlineTool.init();
    return;
  }

  this.redlineTool = new MVRedlineTool(datasrcApp + "." + "L.REDLINE", datasrcApp + "." + "C.RED");
  this.mapview.addRedLineTool(this.redlineTool);
  this.redlineTool.init();
  return;
}

IMapper.prototype.getRedlineOrdinates=function() {
  if( !this.redlineTool ) return null;

  var ord = this.redlineTool.getOrdinates();
  if( IMapper.mustTransform ) ord = transformOrdinatesGeom(ord, this.mapview);
  return ord;
}

IMapper.prototype.clearRedlineTool=function() {
  if( this.redlineTool != null ) this.redlineTool.clear();
  return;
}

IMapper.prototype.setMeasureTool=function(getNewPoint, getNewLine) {
  if( this.measureTool != null ) {
    this.measureTool.init();
    return;
  }

  this.measureTool = new MVRedlineTool(datasrcApp + ".L.REDLINE");
  this.measureTool.setMarkerImage("./images/distanceMarker.png", 15, 29);
  this.measureTool.setControlPanelVisible(false);
  this.measureTool.setAutoClose(false);
  this.mapview.addRedLineTool(this.measureTool);
  if( getNewPoint ) this.measureTool.attachEventListener(MVEvent.NEW_SHAPE_POINT, getNewPoint);
  if( getNewLine ) this.measureTool.attachEventListener(MVEvent.FINISH, getNewLine);
  this.measureTool.init();
  return;
}

IMapper.prototype.clearMeasureTool=function() {
  if( this.measureTool != null ) {
    this.measureTool.clear();
    this.measureTool = null;
  }
  return;
}

IMapper.prototype.setCircleTool=function(getCircle, getDragEvent) {
  if( this.circleTool != null ) {
    this.circleTool.init();
    return;
  }

  this.circleTool = new MVCircleTool(datasrcApp + ".L.REDLINE");
  if( getCircle ) this.circleTool.attachEventListener(MVEvent.FINISH, getCircle);
  if( getDragEvent ) this.circleTool.attachEventListener(MVEvent.DRAG, function() { getDragEvent(); });
  this.mapview.addCircleTool(this.circleTool);
  this.circleTool.init();
  return;
}

IMapper.prototype.clearCircleTool=function() {
  if( this.circleTool != null ) this.circleTool.clear();
  return;
}

IMapper.prototype.getCircleData=function() {
  var circleCenter = this.circleTool.getCenter();
  if( IMapper.mustTransform ) circleCenter = this.mapview.transformGeom(circleCenter, 8307);
  return { x: circleCenter.getPointX(), y: circleCenter.getPointY(), radius: this.circleTool.getRadius('meter') };
}

IMapper.prototype.getCirclePolygonOrdinates=function() {
  if( !this.circleTool ) return null;

  var circle = this.circleTool.getCirclePolygon();
  if( !circle ) return null;

  var ord = circle.getOrdinates();
  if( IMapper.mustTransform ) ord = transformOrdinatesGeom(ord, this.mapview);
  return ord;
}

IMapper.prototype.setRectangleTool=function(getRectangle) {
  if( this.rectangleTool != null ) {
    this.rectangleTool.init();
    return;
  }

  this.rectangleTool = new MVRectangleTool(datasrcApp + ".L.REDLINE");
  if( getRectangle ) this.rectangleTool.attachEventListener(MVEvent.FINISH, getRectangle);
  this.mapview.addRectangleTool(this.rectangleTool);
  this.rectangleTool.init();
  return;
}

IMapper.prototype.clearRectangleTool=function() {
  if( this.rectangleTool != null ) this.rectangleTool.clear();
  return;
}

IMapper.prototype.getRectangleOrdinates=function() {
  if( !this.rectangleTool ) return null;

  var rect = this.rectangleTool.getRectangle();
  if( !rect ) return null;

  var ord = rect.getOrdinates();
  if( IMapper.mustTransform ) ord = transformOrdinatesGeom(ord, this.mapview);
  return ord;
}

IMapper.prototype.setPolygonTool=function(getPolygon) {
  if( this.polygonTool != null ) {
    this.polygonTool.init();
    return;
  }

  this.polygonTool = new MVRedlineTool(datasrcApp + ".L.REDLINE");
  this.polygonTool.setControlPanelVisible(true);
  this.polygonTool.setGeneratePolygonTop(true);
  this.polygonTool.setAutoClose(true);
  this.mapview.addRedLineTool(this.polygonTool);
  if( getPolygon ) this.polygonTool.attachEventListener(MVEvent.FINISH, getPolygon);
  this.polygonTool.init();
  return;
}

IMapper.prototype.clearPolygonTool=function() {
  if( this.polygonTool ) {
    this.polygonTool.clear();
    this.polygonTool = null;
  }
  return;
}

IMapper.prototype.setPolygonOrdinates=function(ordinates) {
  var coors = ordinates.split(",");
  var srid = this.mapview.getSrid();
  var pos;
  var inx = 0;
  for( var i = 0; i < coors.length / 2; i++ ) {
    var x = parseFloat(coors[inx++]);
    var y = parseFloat(coors[inx++]);
	
	if ( x < 180 && y < 180 ) {
      pos = MVSdoGeometry.createPoint(x, y, 8307);
      pos = this.mapview.transformGeom(pos, srid);
	}
	
    this.polygonTool.addVertex(i, pos.getPointX(), pos.getPointY());	  
  } // for()
  this.polygonTool.setControlPanelVisible(true);
  this.polygonTool.setGeneratePolygonTop(true);
  this.polygonTool.setAutoClose(true);
  return;
}

IMapper.prototype.getPolygonOrdinates=function() {
  if( !this.polygonTool ) return null;

  var ord = this.polygonTool.getOrdinates();
  if( IMapper.mustTransform ) ord = transformOrdinatesGeom(ord, this.mapview);
  return ord;
}

IMapper.prototype.startMarqueeZoom=function() {
  this.mapview.stopMarqueeZoom();
  this.mapview.startMarqueeZoom('continuous', new Object());
  return;
}

IMapper.prototype.stopMarqueeZoom=function() {
  this.mapview.stopMarqueeZoom();
  return;
}

IMapper.prototype.pan=function(typ) {
  var srid = this.mapview.getSrid();
  var bbox = this.mapview.getMapWindowBBox();
  var ords = bbox.getOrdinates();
  var minX = ords[0];
  var minY = ords[1];
  var maxX = ords[4];
  var maxY = ords[5];
  var cenX = (minX + maxX) / 2.0;
  var cenY = (minY + maxY) / 2.0;
  var offX = (maxX - minX) / 2.0;
  var offY = (maxY - minY) / 2.0;

  switch( typ ) {
  case 1 : // NORTH
    this.mapview.setCenter(MVSdoGeometry.createPoint(cenX, cenY + offY, srid));
    break;
  case 2 : // NORTH-EAST
    this.mapview.setCenter(MVSdoGeometry.createPoint(cenX + offX, cenY + offY, srid));
    break;
  case 3 : // EAST
    this.mapview.setCenter(MVSdoGeometry.createPoint(cenX + offX, cenY, srid));
    break;
  case 4 : // SOUTH-EAST
    this.mapview.setCenter(MVSdoGeometry.createPoint(cenX + offX, cenY - offY, srid));
    break;
  case 5 : // SOUTH
    this.mapview.setCenter(MVSdoGeometry.createPoint(cenX, cenY - offY, srid));
    break;
  case 6 : // SOUTH-WEST
    this.mapview.setCenter(MVSdoGeometry.createPoint(cenX - offX, cenY - offY, srid));
    break;
  case 7 : // WEST
    this.mapview.setCenter(MVSdoGeometry.createPoint(cenX - offX, cenY, srid));
    break;
  case 8 : // NORTH-WEST
    this.mapview.setCenter(MVSdoGeometry.createPoint(cenX - offX, cenY + offY, srid));
    break;
  } // switch()
  return;
} // pan()

IMapper.prototype.zoomIn=function() {
  this.mapview.zoomIn();
  return;
}

IMapper.prototype.zoomOut=function() {
  this.mapview.zoomOut();
  return;
}

IMapper.prototype.zoomLevel=function(mapZoom) {
  this.mapview.setZoomLevel(mapZoom);
  return;
}

IMapper.prototype.getZoomLevel=function() {
  return this.mapview.getZoomLevel();
}

IMapper.prototype.getCenterLat=function() {
  var pos = this.mapview.getCenter();
  if( IMapper.mustTransform ) pos = this.mapview.transformGeom(pos, 8307);
  return pos.getPointY();
}

IMapper.prototype.getCenterLon=function() {
  var pos = this.mapview.getCenter();
  if( IMapper.mustTransform ) pos = this.mapview.transformGeom(pos, 8307);
  return pos.getPointX();
}

IMapper.prototype.getMaxZoomLevel=function() {
  return this.mapview.getMaxZoomLevel();
}

IMapper.prototype.zoomToMarkers=function(list) {
  if( !this.mapview ) this.mapview = mapviewGlobal;
  var mview = this.mapview;
  window.setTimeout(function () { zoomToMarkers_timeout(list, mview); }, 2000);
  return;
}

function zoomToMarkers_timeout(list, mview) {
  var minLat = 1999999999.99;
  var minLon = 1999999999.99;
  var maxLat = -1999999999.99;
  var maxLon = -1999999999.99;
  var count = 0;

  if( list ) {
    var array = list.split(",");
    for( var i = 0; i < array.length; i++ ) {
      var ftr = mview.getFOI(array[i]);
      if( ftr != null ) {
        if( minLat > ftr.y ) minLat = ftr.y;
        if( minLon > ftr.x ) minLon = ftr.x;
        if( maxLat < ftr.y ) maxLat = ftr.y;
        if( maxLon < ftr.x ) maxLon = ftr.x;
        count++;
      }
    } // for()
  }
  else {
    var array = mview.getAllFOIs();
    if( array ) {
      for( var i = 0; i < array.length; i++ ) {
        var ftr = array[i];
        if( minLat > ftr.y ) minLat = ftr.y;
        if( minLon > ftr.x ) minLon = ftr.x;
        if( maxLat < ftr.y ) maxLat = ftr.y;
        if( maxLon < ftr.x ) maxLon = ftr.x;
        count++;
      } // for()
    }
  }

  if( count > 0 ) {
    var dx = (maxLon - minLon) / 40.0;
    var dy = (maxLat - minLat) / 40.0;
    var rect = getRectangleGeometryObject(minLon - dx, minLat - dy, maxLon + dx, maxLat + dy, mview);
    mview.zoomToRectangle(rect);
  }
  return;
} // zoomToMarkers_timeout()

IMapper.prototype.xml2json=function(xmlData) {
  var xmlDoc;

  if( window.DOMParser ) {
    parser = new DOMParser();
    xmlDoc = parser.parseFromString(xmlData, "text/xml");
  }
  else { // Internet Explorer
    xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
    xmlDoc.async = "false";
    xmlDoc.loadXML(xmlData);
  }

  var json = parseChilds(xmlDoc);
  return eval('(' + json + ');');
}

IMapper.prototype.getGeolocation=function(callback) {
  var gl;
  geoCallback = callback;

  try {
    if( typeof navigator.geolocation === 'undefined' )
      gl = google.gears.factory.create('beta.geolocation');
    else
      gl = navigator.geolocation;
  }
  catch(e) {
  }

  if( gl ) gl.getCurrentPosition(displayPosition, displayError);
  else {
    alert("Kullandiginiz tarayici lokasyon tabanli servisi desteklemiyor. (Your browser does not support location services.) ");
  }
  return;
}

function displayPosition(position) {
  var loc = position.coords;
  if( geoCallback ) geoCallback(loc);
  return;
} // displayPosition()

function displayError(positionError) {
  alert("Konum alinamadi. Tarayiciniz icin konum servislerinin acik oldugunu kontrol edin. (Unable to get location information. Check the location services is turned on for your browser.)");
  return;
} // displayError()

function getMapTileURL(minx, miny, width, height, level) {
  var tx = minx;
  var ty = miny;
  var tw = width;
  var th = height;

  var x = (tx-mapConfig.coordSys.minX)/mapConfig.zoomLevels[level].tileWidth ;
  var y = (mapConfig.coordSys.maxY-ty)/mapConfig.zoomLevels[level].tileHeight-1 ;
  return "http://map.be-mobile.be/customer/infotech/tr/speed/" + (level + 1) + "/" + x + "/" + y + ".png";
} // getMapTileURL()

function tmcAnimate(obj) {
  if( !obj ) {
    alert("Tmc Hat Bulunamadi !");
    return;
  }

  if( obj.status != 0 ) {
    alert(obj.errdesc);
    return;
  }

  var id = obj.tmchat.tmccode;
  if( !tmcAnimateArray[id] ) return;

  var mapSrid = mapviewGlobal.getSrid();

  var ordinates = tmcAnimateArray[id].ordinates;
  if( !ordinates || ordinates.length <= 0 ) {
    ordinates = obj.tmchat.geometry;
    tmcAnimateArray[id].ordinates = ordinates;
  }

  var lastX = ordinates[ordinates.length - 2];
  var lastY = ordinates[ordinates.length - 1];

  var interval = tmcAnimateArray[id].interval;
  var sym = tmcAnimateArray[id].sym;
  var width = tmcAnimateArray[id].width;
  var height = tmcAnimateArray[id].height;
  var repeat = tmcAnimateArray[id].repeat;
  var factor = tmcAnimateArray[id].factor;
  if( !factor || factor < 4 ) factor = 4;

  var pointlength = ordinates.length / 2;
  var timer = interval * pointlength * 2;
  var millisecondsToWait = interval * factor;
  var number = "tmca_" + id + "_" + (new Date()).getTime() + "_" + (Math.floor((Math.random()*50000)+1)).toString();
  

  var pt = MVSdoGeometry.createPoint(ordinates[0], ordinates[1], mapSrid);
  var ftr = new MVFOI(number, pt, datasrcApp + "." + sym, null, width, height);
  mapviewGlobal.addFOI(ftr);
  var route = MVSdoGeometry.createLineString(ordinates, mapSrid);
  try { ftr.animateToNewLocation(route, interval); } catch (e) { ftr.setVisible(false); }
  setTimeout('tmcStopAnimation("' + number + '",' + lastX + ',' + lastY + ')', timer);
  var tmcObj = 'tmcAnimate({"status": 0, "tmchat": { "tmccode": "' + id + '" }})';
  if( repeat ) setTimeout('tmcAnimate({"status": 0, "tmchat": { "tmccode": "' + id + '" }})', millisecondsToWait);
  //
  return;
} // tmcAnimate()

function tmcStopAnimation(id, x, y) {
  var ftr = mapviewGlobal.getFOI(id);
  if( ftr == null ) return;

  var geo = ftr.getGeometry();
  var dx = (geo.getPointX() - x);
  var dy = (geo.getPointY() - y);
  if( Math.abs(dx) < 0.000001 && Math.abs(dy) < 0.000001 ) {
    mapviewGlobal.removeFOI(ftr);
    return;
  }
  
  window.setTimeout(function () { tmcStopAnimation(id, x, y); }, 100);
  //setTimeout('tmcStopAnimation("' + id + '",' + x + ',' + y + ')', 100);
  return;
} // tmcStopAnimation()

IMapper.prototype.zoomToLayer=function(id) {
  var thm = this.mapview.getThemeBasedFOI(id);
  if( thm ) thm.zoomToTheme();
  
  return;
} 
