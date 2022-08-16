/**
 * @fileOverview
 * @author <a href="http://www.infotech.com.tr/">Infotech</a>
 * @version 1.0
 */

var mapviewGlobal = null;

var tmcAnimateArray = new Array();

var printCallback = null;

var geoCallback = null;

var navigationPanelBar = null;

var imagesPath = "images";

var mapConfig = {
    "mapTileLayer" : "OSM_MERCATOR", "format" : "PNG", "coordSys" :  {
        "srid" : 3857, "type" : "PROJECTED", "distConvFactor" : 1.0, "minX" :  - 2.0037508E7, "minY" :  - 2.0037508E7, "maxX" : 2.0037508E7, "maxY" : 2.0037508E7
    },
    "zoomLevels" : [{"zoomLevel" : 0, "name" : "", "tileWidth" : 2.0037508E7, "tileHeight" : 2.0037508E7, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 1, "name" : "", "tileWidth" : 1.0018754E7, "tileHeight" : 1.0018754E7, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 2, "name" : "", "tileWidth" : 5009377.0, "tileHeight" : 5009377.0, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 3, "name" : "", "tileWidth" : 2504688.5, "tileHeight" : 2504688.5, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 4, "name" : "", "tileWidth" : 1252344.25, "tileHeight" : 1252344.25, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 5, "name" : "", "tileWidth" : 626172.125, "tileHeight" : 626172.125, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 6, "name" : "", "tileWidth" : 313086.0625, "tileHeight" : 313086.0625, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 7, "name" : "", "tileWidth" : 156543.03125, "tileHeight" : 156543.03125, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 8, "name" : "", "tileWidth" : 78271.515625, "tileHeight" : 78271.515625, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 9, "name" : "", "tileWidth" : 39135.7578125, "tileHeight" : 39135.7578125, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 10, "name" : "", "tileWidth" : 19567.87890625, "tileHeight" : 19567.87890625, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 11, "name" : "", "tileWidth" : 9783.939453125, "tileHeight" : 9783.939453125, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 12, "name" : "", "tileWidth" : 4891.9697265625, "tileHeight" : 4891.9697265625, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 13, "name" : "", "tileWidth" : 2445.98486328125, "tileHeight" : 2445.98486328125, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 14, "name" : "", "tileWidth" : 1222.992431640625, "tileHeight" : 1222.992431640625, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 15, "name" : "", "tileWidth" : 611.4962158203125, "tileHeight" : 611.4962158203125, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 16, "name" : "", "tileWidth" : 305.74810791015625, "tileHeight" : 305.74810791015625, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 17, "name" : "", "tileWidth" : 152.87405395507812, "tileHeight" : 152.87405395507812, "tileImageWidth" : 256, "tileImageHeight" : 256},{"zoomLevel" : 18, "name" : "", "tileWidth" : 76.43702697753906, "tileHeight" : 76.43702697753906, "tileImageWidth" : 256, "tileImageHeight" : 256}]
};

IMapper.mustTransform = true;
IMapper.BASEMAP_TILELAYER = "BM1";
IMapper.BASEMAP_TILELAYER_NOLOGO = "BM2";

IMapper.RECENTER = "recenter";
IMapper.MOUSE_CLICK = "mouse_click";
IMapper.MOUSE_RIGHT_CLICK = "mouse_right_click";
IMapper.MOUSE_DOUBLE_CLICK = "mouse_double_click";
IMapper.MOUSE_DOWN = "mouse_down";
IMapper.MOUSE_UP = "mouse_up";
IMapper.MOUSE_MOVE = "mouse_move";
IMapper.ZOOM_LEVEL_CHANGE = "zoom_level_change";
IMapper.BEFORE_ZOOM_LEVEL_CHANGE = "before_zoom_level_change";
IMapper.INITIALIZE = "initialize";

IMapper.MARKER_MOUSE_CLICK = "marker_mouse_click";
IMapper.MARKER_MOUSE_OVER = "marker_mouse_over";
IMapper.MARKER_MOUSE_OUT = "marker_mouse_out";
IMapper.MARKER_MOUSE_RIGHT_CLICK = "marker_mouse_rclick";

IMapper.FILTER_EQUALS = "equals";
IMapper.FILTER_NOTEQUALS = "notEquals";
IMapper.FILTER_GREATER = "greater";
IMapper.FILTER_GREATEREQUALS = "greaterEquals";
IMapper.FILTER_LESS = "less";
IMapper.FILTER_LESSEQUALS = "lessEquals";
IMapper.FILTER_ISNULL = "isnull";
IMapper.FILTER_ISNOTNULL = "isnotnull";
IMapper.FILTER_IN = "in";

/**
 * Create a new IMapper instance.
 * @constructor
 */
function IMapper() {
    this.mapview = null;
    this.lboxMap = null;
    this.googleMap = null;
    this.bingTileLayer = null;
    this.transTileLayer = null;
    this.hybridMap = null;
    this.baseTileLayer = null;
    this.redlineTool = null;
    this.measureTool = null;
    this.circleTool = null;
    this.rectangleTool = null;
    this.polygonTool = null;
    this.toolLayer = null;
    this.distancetool = null;
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
    this.thmAnalysis = null;

    this.marqueeZoomTool = null;

    this.typ = null;

    this.objs = new Array();

    this.me = null;
}

mapper = new IMapper();

IMapper.prototype.initMap = function (mapCenterLat, mapCenterLon, mapZoom, pBaseMap, pUrlBase, mapDiv) {
    mapper.keyControl(lboxKey,function(a){
        if(a==true){
            console.log("a->"+a);
        }else{
            var url=lboxUrl.substring(0,lboxUrl.length-8);
            window.location=url+"index.html";
        }
    });
    var protocol = location.protocol;
    var arr = urlBase.split("//");

    if (arr[1] && protocol && (protocol == "http:" || protocol == "https:")) {
        urlBase = protocol + "//" + arr[1];
    }

    if (pBaseMap || pUrlBase) {
        if (pUrlBase)
            urlBase = pUrlBase;
        if (pBaseMap) {
            switch (pBaseMap) {
                case IMapper.BASEMAP_TILELAYER:
                    mapBase = "INFOMAP.INFOTECH_TILELAYER";
                    break;
                case IMapper.BASEMAP_TILELAYER_NOLOGO:
                    mapBase = "INFOMAP.INFOTECH_TILELAYER_NOLOGO";
                    break;
                default :
                    mapBase = pBaseMap;
                    break;
            }
            // switch()
        }
        // if()
    }
    // if()
    var mapDataSource;
    var mapTileLayer;

    var pos = mapBase.indexOf('.');

    if (pos > 0) {
        mapDataSource = mapBase.substring(0, pos);
        mapTileLayer = mapBase.substring(pos + 1);
    }
    // else()
    if (!mapDiv)
        var mapDiv = "map";

    
    if (Math.abs((new Date()).getTime() - gtm) < 86423134) {
        OM.gv.setResourcePath(urlBase + "/jslib/v2/");

        //Create a map instance from url
        this.mapview = new OM.Map($("#" + mapDiv), 
        {
            universe : new OM.universe.LatLonUniverse(), mapviewerURL : urlBase
        });

        //Create a point 
        var pt = getPointGeometryObject(mapCenterLon, mapCenterLat, this.mapview);

        // create a tile layer from this.mapviewer   
        this.baseTileLayer = new OM.layer.TileLayer("baseMap", 
        {
            dataSource : mapDataSource, tileLayer : mapTileLayer, tileServerURL : urlBase + "/mcserver"
        });

        // Add tile layer to map
        this.mapview.addLayer(this.baseTileLayer);

        //Create a copyright insance
        var copyright = new OM.control.CopyRight( {
            anchorPosition : 6, textValue : '&#169;2020 Infotech', fontSize : 12, fontFamily : 'Arial', fontColor : 'black'
        });

        //this.mapview.enableMouseWheelZooming(false);
        this.mapview.setMapCenter(pt)

        // The zoom level to set for the map. The map will zoom in or out to this level.
        this.mapview.setMapZoomLevel(mapZoom);

        this.mapview.addMapDecoration(copyright);

        if (mapviewGlobal == null)
            mapviewGlobal = this.mapview;

        this.mapview.init();
    }

    return this.mapview;

}// initMap()

//----------------------------------------
IMapper.prototype.keyControl = function (key,callback) {
    if (key && lboxUrl) {
        var xmlHttpReq = false;

        if (window.XMLHttpRequest) {
            xmlHttpReq = new XMLHttpRequest();
        }
        else if (window.ActiveXObject) {
            xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
        }
        var strURL = lboxUrl + "?Cmd=KEY&Key=" + key;
        console.log(strURL);
        xmlHttpReq.open('POST', strURL, true);
        xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xmlHttpReq.onreadystatechange = function () {
            if (xmlHttpReq.readyState == 4) {
                if(xmlHttpReq && xmlHttpReq.responseText){
                var obj=JSON.parse( xmlHttpReq.responseText);
                    if(obj && obj.status==1){
                        callback(false);
                    }
                }
            }
        }
        xmlHttpReq.send();
    }

}

IMapper.prototype.initMapWithExtent = function (minLat, minLon, maxLat, maxLon, pBaseMap, pUrlBase, mapDiv) {

    var protocol = location.protocol;
    var arr = urlBase.split("//");

    if (arr[1] && protocol && (protocol == "http:" || protocol == "https:")) {
        urlBase = protocol + "//" + arr[1];
    }

    if (pBaseMap || pUrlBase) {
        if (pUrlBase)
            urlBase = pUrlBase;
        if (pBaseMap) {
            switch (pBaseMap) {
                case IMapper.BASEMAP_TILELAYER:
                    mapBase = "INFOMAP.INFOTECH_TILELAYER";
                    break;
                case IMapper.BASEMAP_TILELAYER_NOLOGO:
                    mapBase = "INFOMAP.INFOTECH_TILELAYER_NOLOGO";
                    break;
                default :
                    mapBase = pBaseMap;
                    break;
            }
            // switch()
        }
        // if()
    }
    // if()
    var mapDataSource;
    var mapTileLayer;

    var pos = mapBase.indexOf('.');

    if (pos > 0) {
        mapDataSource = mapBase.substring(0, pos);
        mapTileLayer = mapBase.substring(pos + 1);
    }
    // else()
    if (!mapDiv)
        var mapDiv = "map";

    if (Math.abs((new Date()).getTime() - gtm) < 86423134) {
        OM.gv.setResourcePath(urlBase + "/jslib/v2/");

        //Create a map instance from url
        this.mapview = new OM.Map($("#" + mapDiv), 
        {
            universe : new OM.universe.LatLonUniverse(), mapviewerURL : urlBase, disableOverviewMap : false
        });
        // create a tile layer from this.mapviewer
        this.baseTileLayer = new OM.layer.TileLayer("baseMap", 
        {
            dataSource : mapDataSource, tileLayer : mapTileLayer, tileServerURL : urlBase + "/mcserver"
        });
        var mview = this.mapview;
        var initRect = new OM.geometry.Rectangle(minLon, minLat, maxLon, maxLat, 8307);

        if (mview.getMapContext().getUniverse().getSRID() != 8307)
            initRect = rectangleGeo.transform(mview.getMapContext.getUniverse().getSRID());

        // Add tile layer to map
        this.mapview.addLayer(this.baseTileLayer, function () {
            mview.zoomToExtent(initRect);
        });

        this.mapview = mview;

        var pt = getPointGeometryObject(initRect.getCenter().getX(), initRect.getCenter().getY(), this.mapview);

        //Create a copyright insance
        var copyright = new OM.control.CopyRight( {
            anchorPosition : 6, textValue : '&#169;2020 Infotech', fontSize : 12, fontFamily : 'Arial', fontColor : 'black'
        });

        this.mapview.setMapCenter(pt);
        this.mapview.addMapDecoration(copyright);
        this.mapview.init();

    }

    return this.mapview;

}
//initMapWithExtent()
IMapper.prototype.addTileLayer = function (basemap) {

    var dataSource = null;
    var tileLayer = null;
    var pos = basemap.indexOf('.');
    if (pos > 0) {
        dataSource = basemap.substring(0, pos);
        tileLayer = basemap.substring(pos + 1);
    }
    var tlyr = new OM.layer.TileLayer("baseMap", 
    {
        dataSource : dataSource, tileLayer : tileLayer, tileServerURL : urlBase + "/mcserver"
    });
    this.mapview.addLayer(tlyr);

    return;
}
//addTileLayer()
IMapper.prototype.removeTileLayer = function (tlyr) {
    this.mapview.removeLayer(tlyr);
    return;
}
//removeTileLayer()
IMapper.prototype.createLayerControl = function (anchorPosition, offsetX, offsetY, contentStyle, titleStyle, layerDeleteEnable) {

    if (OM.isNull(anchorPosition))
        anchorPosition = 1;
    if (OM.isNull(offsetX))
        offsetX = 50;
    if (OM.isNull(offsetY))
        offsetY = 50;
    if (OM.isNull(contentStyle))
        contentStyle = {
            "minWidth" : "200", "maxHeight" : "300", "font_family" : "Tahoma", "font_size" : "11px"
        };
    if (OM.isNull(titleStyle))
        titleStyle = {
            "font_size" : "11px", "font_family" : "Tahoma"
        };
    if (OM.isNull(layerDeleteEnable))
        layerDeleteEnable = false;

    var layerControl = new OM.control.LayerControl( {
        offsetX : offsetX, offsetY : offsetY, anchorPosition : anchorPosition, layerDeleteEnable : layerDeleteEnable, contentStyle : contentStyle, titleStyle : titleStyle
    });

    return layerControl;
}
//createLayerControl()

IMapper.prototype.createVariableMarkerStyle = function (classification, color, stroke, type, textStyle, startSize, increment, numClasses, cx, cy, width, height) {

    if (OM.isNull(classification))
        classification = "logarithmic";
    if (OM.isNull(color))
        color = "#ff0000";
    if (OM.isNull(stroke))
        stroke = "#ffffff";
    if (OM.isNull(type))
        type = "circle";
    if (OM.isNull(startSize))
        startSize = 20;
    if (OM.isNull(increment))
        increment = 5;
    if (OM.isNull(numClasses))
        numClasses = 10;
    if (OM.isNull(cx))
        cx = 5;
    if (OM.isNull(cy))
        cy = 5;
    if (OM.isNull(width))
        width = 10;
    if (OM.isNull(height))
        height = 10;

    if (OM.isNull(textStyle)) {
        textStyle = new OM.style.Text( {
            styleName : "txt", fill : "#ffffff", fontStyle : OM.Text.FONTSTYLE_NORMAL, fontFamily : "Tahoma", fontSize : 12
        });
    }

    var variableMarker = new OM.style.VariableMarker( {
        styleName : "vMrk", classification : classification, marker : new OM.style.Marker( {
            textStyle : textStyle, vectorDef : [{shape :  {type : type, cx : cx, cy : cy, width : width, height : height},style :  {fill : color, stroke : stroke}}]
        }), startSize : startSize, increment : increment, numClasses : numClasses
    });

    return variableMarker;

}

//createVariableMarkerStyle()
IMapper.prototype.createTextStyle = function (name, color, fontStyle, fontFamily, fontSize, sizeUnit, fontWeight) {

    if (OM.isNull(name))
        name = "txt";
    if (OM.isNull(color))
        color = "#ff0000";
    if (OM.isNull(fontStyle))
        fontStyle = OM.Text.FONTSTYLE_NORMAL;
    if (OM.isNull(fontFamily))
        fontFamily = OM.Text.FONTSTYLE_NORMAL;
    if (OM.isNull(fontSize))
        fontSize = 50;
    if (OM.isNull(sizeUnit))
        name = "pixel";
    if (OM.isNull(fontWeight))
        fontWeight = OM.Text.FONTWEIGHT_NORMAL;

    var textStyle = new OM.style.Text( {
        styleName : name, fill : color, fontStyle : fontStyle, fontFamily : fontFamily, fontSize : fontSize, sizeUnit : sizeUnit, fontWeight : fontWeight
    });
    return textStyle;
}
//createTextStyle()
IMapper.prototype.createAnimationColorStyle = function (colorFirst, colorSecond, duration, enableMovingDashLine) {

    if (OM.isNull(colorFirst))
        colorFirst = new OM.style.Color( {
            fill : "#ff0000", strokeThickness : 5, stroke : "#ff0000"
        });
    if (OM.isNull(colorSecond))
        colorSecond = new OM.style.Color( {
            fill : "#ffff00", strokeThickness : 5, stroke : "#ffff00"
        });
    if (OM.isNull(duration))
        duration = 5;
    if (OM.isNull(enableMovingDashLine))
        enableMovingDashLine = false;

    var acStyle = new OM.style.AnimationColor( {
        beginColor : colorFirst, endColor : colorSecond, duration : duration, enableMovingDashLine : enableMovingDashLine
    });

    return acStyle;
}
// createAnimationColorStyle()
IMapper.prototype.createGrowStyle = function (opacity, color, offset, radius) {

    if (OM.isNull(opacity))
        opacity = 0.5;
    if (OM.isNull(color))
        color = "#000000";
    if (OM.isNull(offset))
        offset = 6;
    if (OM.isNull(radius))
        radius = 12;

    var glowFilter = new OM.visualfilter.Glow( {
        opacity : opacity, color : color, offset : offset, radius : radius
    });

    return glowFilter;
}
//createGrowStyle()
IMapper.prototype.createDropShadowStyle = function (opacity, color, offset, radius) {

    if (OM.isNull(opacity))
        opacity = 0.5;
    if (OM.isNull(color))
        color = "#000000";
    if (OM.isNull(offset))
        offset = 6;
    if (OM.isNull(radius))
        radius = 10;

    var shadowFilter = new OM.visualfilter.DropShadow( {
        opacity : opacity, color : color, offset : offset, radius : radius
    });

    return shadowFilter;
}
//createDropShadowStyle()
IMapper.prototype.createLinearGradient = function (stops, x1, y1, x2, y2, stroke) {

    if (OM.isNull(stops))
        stops = [{offset : 0, color : "#d93624ff"},{offset : 1, color : "#782200ff"}]
    if (OM.isNull(x1))
        x1 = 1;
    if (OM.isNull(y1))
        y1 = 1;
    if (OM.isNull(x2))
        x2 = 0;
    if (OM.isNull(y2))
        y2 = 0;
    if (OM.isNull(stroke))
        stroke = "#ffff00";

    var linearGradient = new OM.style.LinearGradient( {
        stops : stops, x1 : x1, y1 : y1, x2 : x2, y2 : y2, stroke : stroke
    });

    return linearGradient;
}
//createLinearGradient()
IMapper.prototype.createRadialGradient = function (stops, cy, cx, radius, stroke) {

    if (OM.isNull(stops))
        stops = [{offset : 0, color : "#d93624ff"},{offset : 1, color : "#782200ff"}]
    if (OM.isNull(cy))
        cy = 0.5;
    if (OM.isNull(cx))
        cx = 0.5;
    if (OM.isNull(radius))
        radius = 0.5;
    if (OM.isNull(stroke))
        stroke = "#ffff00";

    var radialGradient = new OM.style.RadialGradient( {
        stops : stops, cx : cx, cy : cy, radius : radius, stroke : stroke
    });

    return radialGradient;
}
//createRadialGradient()

IMapper.prototype.createBar = function (data, color) {

    var bar = new OM.style.Bar(data, color);

    return bar;
}
//createBar()
IMapper.prototype.createPieSlice = function (data, color) {

    var pieSlice = new OM.style.PieSlice(data, color);

    return pieSlice;
}
//createPieSlice()
IMapper.prototype.createPieChartStyle = function (name, slices, enableHighlight, startingAngle, direction, radius, lengthUnit, radiusPixelRange) {

    if (OM.isNull(name))
        name = "pie";
    if (OM.isNull(startingAngle))
        startingAngle = 90;
    if (OM.isNull(direction))
        direction = "CW";
    if (OM.isNull(radius))
        radius = 80;
    if (OM.isNull(lengthUnit))
        lengthUnit = "kilometer";
    if (OM.isNull(radiusPixelRange))
        radiusPixelRange = {
            max : 200, min : 10
        };

    var piechart = new OM.style.PieChart( {
        styleName : name, pieSlices : slices, enableHighlight : enableHighlight, startingAngle : startingAngle, direction : direction, radius : radius, lengthUnit : lengthUnit, radiusPixelRange : radiusPixelRange
    });
    return piechart;
}
//createPieChartStyle()
IMapper.prototype.createBarChartStyle = function (name, bars, width, height, lengthUnit, enableHighlight, lengthPixelRange) {

    if (OM.isNull(name))
        name = "bar";
    if (OM.isNull(width))
        width = 180;
    if (OM.isNull(height))
        height = 210;
    if (OM.isNull(lengthUnit))
        lengthUnit = "kilometer";
    if (OM.isNull(lengthPixelRange))
        lengthPixelRange = {
            min : 0, max : 120
        };

    var barchart = new OM.style.BarChart( {
        styleName : name, bars : bars, width : width, height : height, lengthUnit : lengthUnit, enableHighlight : enableHighlight, lengthPixelRange : lengthPixelRange
    });
    return barchart;
}
//createBarChartStyle()

IMapper.prototype.createBucketStyle = function (name, styles, buckets, classification, numClasses, gradient) {

    if (OM.isNull(name))
        name = "bucketStyle";
    if (OM.isNull(classification))
        classification = 'equal';

    var bucketStyle = new OM.style.BucketStyle( {
        styleName : name, styles : styles, classification : classification, //since we are supplying all the buckets
        buckets : buckets, numClasses : numClasses, gradient : gradient

    });

    return bucketStyle;
}
//createBucketStyle()
IMapper.prototype.createPulseAnimation = function (stroke, duration, beginSize, endSize) {

    if (OM.isNull(stroke))
        stroke = "#336699";
    if (OM.isNull(duration))
        duration = 1;
    if (OM.isNull(beginSize))
        beginSize = 8;
    if (OM.isNull(endSize))
        endSize = 50;

    var pulse = new OM.style.PulseAnimation( {
        stroke : stroke, duration : duration, beginSize : beginSize, endSize : endSize
    });
    return pulse;
}
// createPulseAnimation()
IMapper.prototype.addBingTileLayer = function (bingKey, hybrid) {
    if (this.bingTileLayer)
        return;

    if (transparentMap.indexOf(".") >  - 1) {
        var transparentMapInfo = transparentMap.split(".");
        var transpDataSource = transparentMapInfo[0];
        var transpTileLayer = transparentMapInfo[1];
        this.baseTileLayer.setVisible(false);
        this.bingTileLayer = new OM.layer.BingTileLayer("bingLayer", 
        {
            key : bingKey, mapType : OM.layer.BingTileLayer.TYPE_AERIAL, mapTypeVisible : true
        });
        this.mapview.addLayer(this.bingTileLayer);
        if (hybrid) {
            this.hybridMap = new OM.layer.TileLayer("transTile", 
            {
                dataSource : transpDataSource, tileLayer : transpTileLayer, tileServerURL : urlBase + "/mcserver"
            });
            this.mapview.addLayer(this.hybridMap);
        }
    }

    return;
}
//addBingTileLayer()
IMapper.prototype.removeBingTileLayer = function () {
    if (this.bingTileLayer) {
        this.mapview.removeLayer(this.bingTileLayer);
        this.bingTileLayer = null;
        if (this.hybridMap) {
            this.mapview.removeLayer(this.hybridMap);
            this.hybridMap = null;
        }
        this.baseTileLayer.setVisible(true);
    }
    return;
}
//removeBingTileLayer()
IMapper.prototype.addGoogleTileLayer = function (googleKey, hybrid) {
    if (this.googleMap)
        return;

    if (transparentMap.indexOf(".") >  - 1) {
        var transparentMapInfo = transparentMap.split(".");
        var transpDataSource = transparentMapInfo[0];
        var transpTileLayer = transparentMapInfo[1];
        this.baseTileLayer.setVisible(false);
        var thm = new OM.layer.GoogleTileLayer("googleLayer", 
        {
            key : googleKey, mapType : OM.layer.GoogleTileLayer.TYPE_SATELLITE, mapTypeVisible : true
        });
        thm.setVisible(true);
        this.mapview.addLayer(thm);
        if (hybrid) {
            this.hybridMap = new OM.layer.TileLayer("transparentTile", 
            {
                dataSource : transpDataSource, tileLayer : transpTileLayer, tileServerURL : urlBase + "/mcserver"
            });
            this.mapview.addLayer(this.hybridMap);
        }
        this.googleMap = thm;
    }

    return;
}//addGoogleTileLayer()

IMapper.prototype.removeGoogleTileLayer = function () {
    if (this.googleMap) {
        this.mapview.removeLayer(this.googleMap);
        this.googleMap = null;
        if (this.hybridMap) {
            this.mapview.removeLayer(this.hybridMap);
            this.hybridMap = null;
        }
        this.baseTileLayer.setVisible(true);
    }
    return;
}
//removeGoogleTileLayer()
IMapper.prototype.addNavigationPanel = function (place, backgroundColor, buttonColor, fontSize, infoTips, orientation, style) {

    if (navigationPanelBar) {
        this.mapview.removeMapDecoration(navigationPanelBar);
    }

    if (OM.isNull(backgroundColor))
        backgroundColor = "#FFFFFF";
    if (OM.isNull(buttonColor))
        buttonColor = "#5A5A5A";
    if (OM.isNull(fontSize))
        fontSize = 10;
    if (OM.isNull(infoTips))
        infoTips = {
            0 : "0", 2 : "2", 4 : "4", 6 : "6", 8 : "8", 10 : "10", 12 : "12", 14 : "14", 16 : "16", 18 : "18"
        };
    if (OM.isNull(orientation))
        orientation = 1;
    if (OM.isNull(style))
        style = 1;

    navigationPanelBar = new OM.control.NavigationPanelBar( {
        orientation : orientation, style : style, anchorPosition : place
    });
    navigationPanelBar.setZoomLevelInfoTips(infoTips);
    navigationPanelBar.setStyle( {
        backgroundColor : backgroundColor, buttonColor : buttonColor, fontSize : fontSize
    });
    this.mapview.addMapDecoration(navigationPanelBar);
    return;
}
//addNavigationPanel()
IMapper.prototype.addScaleBar = function (format, anchorPosition, fontSize, fontColor, fontWeight, scaleBarColor, boxShadow, maxLength) {

    if (OM.isNull(format))
        format = "BOTH";
    if (OM.isNull(anchorPosition))
        anchorPosition = 3;
    if (OM.isNull(fontSize))
        fontSize = 15;
    if (OM.isNull(fontColor))
        fontColor = "#rrggbb";
    if (OM.isNull(fontWeight))
        fontWeight = 15;
    if (OM.isNull(scaleBarColor))
        scaleBarColor = "#cccccc";
    if (OM.isNull(boxShadow))
        boxShadow = "1px 1px 5px #000000";
    if (OM.isNull(maxLength))
        maxLength = 100;

    var mapScaleBar = new OM.control.ScaleBar();
    mapScaleBar.setFormat(format);
    mapScaleBar.setAnchorPosition(anchorPosition);
    mapScaleBar.setStyle( {
        fontSize : fontSize, fontColor : fontColor, fontWeight : fontWeight, scaleBarColor : scaleBarColor, boxShadow : boxShadow, maxLength : maxLength
    });
    this.mapview.addMapDecoration(mapScaleBar);
    return;
}
//addScaleBar()
IMapper.prototype.zoomIn = function () {
    this.mapview.zoomIn();
}
//zoomIn()
IMapper.prototype.zoomOut = function () {
    this.mapview.zoomOut();
}
//zoomOut()
IMapper.prototype.enableLoadingIcon = function (visible) {
    if (!this.mapview)
        this.mapview = mapviewGlobal;
    //this.mapview.showLoadingIcon(visible);
    return;
}
//enableLoadingIcon()
/* IMapper.prototype.enableKeyboardPanning=function(enable) {
  this.mapview.enableKeyboardPanning(enable);
  return;
}

IMapper.prototype.enableDragging=function(dragging) {
  this.mapview.enableDragging(dragging);
  return ;
} */

/* IMapper.prototype.addCollapsibleOverview=function(title, width, height, state) {
  ov = new MVMapDecoration(null,null,null, width, height) ;
  ov.setCollapsible(true, state);
  ov.setTitleBar(title, "/this.mapviewer/fsmc/images/overviewicon.png", title);
  this.mapview.addMapDecoration(ov);
  var oview = new MVOverviewMap(ov.getContainerDiv(), 3);
  this.mapview.addOverviewMap(oview);
  return;
} */

IMapper.prototype.addOverview = function () {
    var oOptions = {
        collapse : false, display : true
    };
    this.mapview.setOverviewMapOptions(oOptions);
}
//addOverview()
IMapper.prototype.enableMouseWheelZooming = function (wheelZooming) {
    this.mapview.enableMouseWheelZooming(wheelZooming);
    return;
}
//enableMouseWheelZooming()
IMapper.prototype.addDistanceMeasuringTool = function () {
    this.distancetool = new OM.tool.DistanceTool(this.mapview, 
    {
        type : OM.tool.DistanceTool.TYPE_LINESTRING
    });
    this.distancetool.start();
    return;
}
//addDistanceMeasuringTool()
IMapper.prototype.clearDistanceMeasuringTool = function () {
    if (this.distancetool != null) {
        this.distancetool.clear();
        this.distancetool = null;
    }
    return;
}
//clearDistanceMeasuringTool()
IMapper.prototype.showMapCentered = function (mapCenterLat, mapCenterLon, mapZoom) {

    var pt = getPointGeometryObject(mapCenterLon, mapCenterLat, this.mapview);

    if (mapZoom)
        this.mapview.setMapCenterAndZoomLevel(pt, mapZoom, false);
    else 
        this.mapview.setMapCenter(pt, false);

    return;
}
//showMapCentered()
IMapper.prototype.addMarker = function (id, latitude, longitude, name, sym, w, h, mclick, zindex) {
    mapper.removeVectorLayer(id);
    var pt = getPointGeometryObject(longitude, latitude, this.mapview);
    var feature = new OM.Feature(id, pt, 
    {
        renderingStyle : null
    });
    var vectorLayer = new OM.layer.VectorLayer(id, 
    {
        def :  {
            type : OM.layer.VectorLayer.TYPE_LOCAL
        }
    });

    OM.style.StyleStore.getServerSideStyle(datasrcApp, sym, 
    {
        url : urlBase, callback : function (rangeBuckets) {
            feature.setRenderingStyle(rangeBuckets);
        }
    });

    vectorLayer.addFeature(feature);
    vectorLayer.setBringToTopOnMouseOver(true);
    if (mclick)
        vectorLayer.addListener(OM.event.MouseEvent.MOUSE_CLICK, function () {
            mclick( {
                id : id, name : name, x : longitude, y : latitude
            })
        });

    this.mapview.addLayer(vectorLayer, "Marker");

    if (zindex)
        vectorLayer.setZIndex(parseInt(zindex));

    return;
}
//addMarker()
IMapper.prototype.addCustomMarker = function (id, latitude, longitude, name, sym, w, h, mclick, zindex, direction, textMarker ) {

    if (Math.abs((new Date()).getTime() - gtm) < 86445293) {

      var pt = direction == 1 ? getPointGeometryObjectCustom(longitude, latitude, this.mapview) : getPointGeometryObject(longitude, latitude, this.mapview);

        if (!sym)
            sym = "marker.png";

        var pos = sym.indexOf(".M.");

        if (pos < 0) {
            if (sym.indexOf("/") < 0)
                sym = imagesPath + "/" + sym.toLowerCase();
        }
        else {
            sym = imagesPath + "/" + sym.substring(pos + 3).toLowerCase() + ".png";
        }
        //else()
        var markerStyle = new OM.style.Marker( {
            width : w, height : h, src : sym
        });
        var markerFeature = new OM.Feature(id, pt, 
        {
            renderingStyle : markerStyle, label : name
        });
        
        if(textMarker)
          markerFeature.setMarkerText(textMarker);
          
        mrk = new OM.layer.VectorLayer(id, 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_LOCAL, features : [markerFeature]
            },
            renderingStyle : markerStyle
        });
        mrk.setBringToTopOnMouseOver(true);
        if (mclick)
            mrk.addListener(OM.event.MouseEvent.MOUSE_CLICK, mclick);

        this.mapview.addLayer(mrk, "Marker");

        if (zindex)
            mrk.setZIndex(parseInt(zindex));
    }
    return;
}
//addCustomMarker()
IMapper.prototype.addMapMarker = function (id, latitude, longitude, markerStyle, labelText, labelStyle, labelTextStyle, draggable, mclick, zindex, mclickDragging, mclickDragStart, mclickDragEnd, showToolTip) {

    var markerLayer = new OM.layer.MarkerLayer(id);
    var mm = new OM.MapMarker();
    markerLayer.addMapMarker(mm);
    mm.setPosition(longitude, latitude);
    //mm.setMarkerText(labelText);
    mm.setLabel(labelText);
    mm.setID(id);
    mm.setDraggable(draggable);

    mm.setRenderingStyle(markerStyle);
    //mm.setMarkerTextStyle(textStyle);
    mm.setLabelingStyle(labelTextStyle);

    markerLayer.setLabelingStyle(labelStyle);
    
    markerLayer.showToolTip=(showToolTip == null || typeof showToolTip === 'undefined') ? true :showToolTip;
    
    if (labelText)
        markerLayer.setLabelsVisible(true);
    else 
        markerLayer.setLabelsVisible(false);

    if (mclick)
        markerLayer.addListener(OM.event.MouseEvent.MOUSE_CLICK, function () {
            mclick( {
                id : id, x : longitude, y : latitude
            })
        });

    if (mclickDragging)
        markerLayer.setDraggingListener(mclickDragging);
    if (mclickDragStart)
        markerLayer.setDragStartListener(mclickDragStart);
    if (mclickDragEnd)
        markerLayer.setDragEndListener(mclickDragEnd); 
        
    this.mapview.addLayer(markerLayer);

    if (zindex)
        markerLayer.setZIndex(parseInt(zindex));

    return;
}
// addMapMarker()

IMapper.prototype.moveMarker = function (id, latitude, longitude, zindex) {

    var lyr = this.mapview.getLayerByName(id);
    var feature = lyr.getFeature(id);

    if (feature == null)
        return;

    if (Math.abs((new Date()).getTime() - gtm) < 86472673) {
        var pt = getPointGeometryObject(longitude, latitude, this.mapview);
        feature.setGeometry(pt);
        if (zindex || zindex == 0)
            lyr.setZIndex(zindex);
    }
    return;
}
//moveMarker()
IMapper.prototype.animateMarker = function (id, ordinates, interval, loop, time, bounce) {

    var ftrLayer = this.mapview.getLayerByName(id);
    var ftr = ftrLayer.getFeature(id);
    if (ftr == null)
        return;

    if (OM.isNull(loop))
        loop = true;
    if (OM.isNull(time))
        time = 1000;
    if (OM.isNull(bounce))
        bounce = true;
    var line=null;
    try {
         line = new OM.geometry.LineString(ordinates, 8307);
    
        line = line.transform(this.mapview.getUniverse().getSRID());
    
        ftr.animateToNewLocation(line, 
        {
            loop : loop, time : time, bounce : bounce
        });
        
    }catch(err){console.log(err);}
    return;
}
//animateMarker()
IMapper.prototype.animateMarkerFlow = function (tmcKod, interval, sym, width, height, repeat, factor, reverse) {
    tmcAnimateArray[tmcKod] = {
        "interval" : interval, "sym" : sym, "width" : width, "height" : height, "repeat" : repeat, "factor" : factor, "ordinates" : []
    };

    var lvl = this.mapview.getMapZoomLevel();
    var element = document.createElement("script");
    element.src = lboxUrl + "?Key=" + lboxKey + "&Cmd=TmcHatInfo&Typ=JSON&TmcKod=" + tmcKod + "&ZoomLevel=" + lvl + "&Geometry=1&SRID=" + this.mapview.getUniverse().getSRID() + "&Reverse=" + (reverse ? "1" : "0") + "&callback=tmcAnimate";

    element.type = 'text/javascript';
    element.charset = 'utf-8';
    this.element = element;
    document.body.appendChild(element);
    return;
}
//animateMarkerFlow()
IMapper.prototype.stopMarkerFlowAnimation = function (tmcKod) {
    tmcAnimateArray = new Array();

    var id = "tmca_";
    if (tmcKod)
        id += tmcKod;
    var removed = true;
    while (removed) {
        removed = false;
        var array = this.mapview.getFeatureLayers();
        for (var i = 0;i < array.length;i++) {
            var name = array[i].getId();
            if (name.startsWith(id)) {
                this.mapview.removeLayer(array[i]);
                removed = true;
            }
        }
        // for()
    }
    // while()
    return;
}
//stopMarkerFlowAnimation()

IMapper.prototype.addText = function (id, latitude, longitude, content, fontFamily, fontStyle, fontWeight, color, fontSize, sizeUnit) {

    mapper.removeVectorLayer(id);
    var vectorLayer = new OM.layer.VectorLayer(id, 
    {
        def :  {
            type : OM.layer.VectorLayer.TYPE_LOCAL
        }
    });

    if (OM.isNull(fontFamily))
        fontFamily = "ARIAL";
    if (OM.isNull(fontStyle))
        fontStyle = OM.Text.FONTSTYLE_NORMAL;
    if (OM.isNull(fontWeight))
        fontWeight = OM.Text.FONTWEIGHT_NORMAL;
    if (OM.isNull(color))
        color = "#000000";
    if (OM.isNull(fontSize))
        fontSize = 16;
    if (OM.isNull(sizeUnit))
        sizeUnit = "pixel";

    var point = new OM.geometry.Point(longitude, latitude, 8307);

    var textStyle = new OM.style.Text( {
        styleName : id, fill : color, fontStyle : fontStyle, fontFamily : fontFamily, fontWeight : fontWeight, fontSize : fontSize, sizeUnit : sizeUnit
    });

    var layerText = new OM.TextFeature(id, point, content, textStyle);

    vectorLayer.addFeature(layerText);
    this.mapview.addLayer(vectorLayer);

    return;
}
// addText();
IMapper.prototype.removeFeature = function (id) {

    var ftr;

    if (id) {
        var ftrLayer = this.mapview.getLayerByName(id);

        if (ftrLayer != null)
            ftr = ftrLayer.getFeature(id);

        if (ftr != null) {
            ftrLayer.removeFeature(ftr);
            this.mapview.removeLayer(ftrLayer);
        }
    }
    return;
}
//removeFeature()
IMapper.prototype.createVectorMarkerStyle = function (name, fillColor, fillOpacity, borderColor, borderOpacity, borderWidth, width, height, vector) {

    if (OM.isNull(vector))
        vector = "50,199,0,100,50,1,100,100,50,199";
    if (OM.isNull(borderWidth))
        borderWidth = 16;
    if (OM.isNull(width))
        width = 20;
    if (OM.isNull(height))
        height = 20;
    if (OM.isNull(name))
        name = 'markerVectorStyle';
    if (OM.isNull(fillColor))
        fillColor = '000000';
    if (OM.isNull(fillOpacity))
        fillOpacity = '120';
    if (OM.isNull(borderOpacity))
        borderOpacity = '255';

    var styleMarker = new OM.style.Marker( {
        styleName : name, style :  {
            fill : fillColor, stroke : borderColor, fillOpacity : fillOpacity, strokeOpacity : borderOpacity
        },
        width : width, height : height, vectorDef : [{shape :  {type : "path", coords : [[vector]]},style :  {fill : fillColor, strokeThickness : borderOpacity, fillOpacity : fillOpacity}}]
    });
    return styleMarker;
}
//createVectorMarkerStyle()
IMapper.prototype.createMarkerStyle = function (name, fillColor, fillOpacity, borderOpacity, width, height, lengthUnit, type, cx, cy, shapeWidth, shapeHeight) {

    if (OM.isNull(width))
        width = 16;
    if (OM.isNull(height))
        height = 16;
    if (OM.isNull(name))
        name = 'markerStyle';
    if (OM.isNull(fillColor))
        fillColor = '000000';
    if (OM.isNull(fillOpacity))
        fillOpacity = '120';
    if (OM.isNull(borderOpacity))
        borderOpacity = '255';
    if (OM.isNull(lengthUnit))
        lengthUnit = 'pixel';
    if (OM.isNull(type))
        type = 'circle';
    if (OM.isNull(cx))
        cx = 10;
    if (OM.isNull(cy))
        cy = 10;
    if (OM.isNull(shapeWidth))
        shapeWidth = 20;
    if (OM.isNull(shapeHeight))
        shapeHeight = 20;

    var styleMarker = new OM.style.Marker( {
        styleName : name, width : width, height : height, lengthUnit : lengthUnit, vectorDef : [{shape :  {type : type, cx : cx, cy : cy, width : shapeWidth, height : shapeHeight},style :  {fill : fillColor, strokeThickness : borderOpacity, fillOpacity : fillOpacity}}]
    });
    return styleMarker;

}
// createMarkerStyle()
IMapper.prototype.createIconMarkerStyle = function (name, url, width, height) {

    if (OM.isNull(width))
        width = 16;
    if (OM.isNull(height))
        height = 16;
    if (OM.isNull(name))
        name = 'markerIconStyle';

    var styleMarker = new OM.style.Marker( {
        styleName : name, src : url, width : width, height : height
    });

    return styleMarker;
}
// createIconMarkerStyle()
IMapper.prototype.createColorStyle = function (name, fillColor, fillOpacity, borderColor, borderOpacity, borderWidth) {

    if (OM.isNull(borderWidth))
        borderWidth = 16;
    if (OM.isNull(name))
        name = 'colorStyle';
    if (OM.isNull(fillColor))
        fillColor = '000000';
    if (OM.isNull(fillOpacity))
        fillOpacity = '120';
    if (OM.isNull(borderOpacity))
        borderOpacity = '255';

    var styleColor = new OM.style.Color( {
        styleName : name, stroke : borderColor, fill : fillColor, fillOpacity : fillOpacity, strokeOpacity : borderOpacity, strokeThickness : borderWidth
    });

    return styleColor;
}
// createColorStyle()

IMapper.prototype.createAdvancedStyle = function (name, symlist, dtype, defSym) {

    if (OM.isNull(defSym))
        defSym = mapper.createColorStyle("cs1", "FF0000", 120, "FF0000", 255, 1);

    var buckets = new Array();
    var styles = new Array();

    for (var i = 0;i < symlist.length;i++) {
        var data = symlist[i];
        var bucket = createRangedBucket(data.seq, data.low, data.high);
        buckets.push(bucket);
        styles.push(data.sym);
    }
    // for()

    var bucketStyle = new OM.style.BucketStyle( {
        styleName : name, styles : styles, buckets : bucket, numClasses : 4, defaultStyle : defSym
    });

    var style = {
        sym : bucketStyle, symlist : symlist, name : name
    };

    return style;
}
// createAdvancedStyle()
IMapper.prototype.createCollectionBucket = function (values, dataType, keepWhiteSpace) {
    var bucket = new OM.style.CollectionBucket( {
        values : [values], dataType : dataType, keepWhiteSpace : keepWhiteSpace
    });
    return bucket;
}
// createCollectionBucket()
IMapper.prototype.createRangedBucket = function (seq, low, high) {
    var bucket = new OM.style.RangedBucket( {
        seq : seq, low : low, high : high
    });
    return bucket;
}
// createRangedBucket()
IMapper.prototype.createColorScheme = function (name, numClasses, classification, defaultStyle, styles, buckets, baseColor, toColor, fromColor, stroke, baseColorOpacity, strokeOpacity) {

    var styleScheme = new OM.style.ColorScheme( {
        styleName : name, styles : styles, buckets : buckets, name : name, numClasses : numClasses, classification : classification, //'quantiles', 'custom',
        stroke : stroke, baseColorOpacity : baseColorOpacity, strokeOpacity : strokeOpacity, defaultStyle : defaultStyle, baseColor : baseColor, fromColor : fromColor, toColor : toColor
    });
    return styleScheme;
}
//createColorScheme()
/* IMapper.prototype.addHtmlMarker=function(id, latitude, longitude, html, zindex) {
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
} */

IMapper.prototype.addLabelToMarker = function (id, html, textMarker) {
    var mrk = this.mapview.getLayerByName(id);
    if (mrk != null) {
        var feature = mrk.getFeature(id);
        feature.setMarkerText(textMarker);
        feature.setInfoWindowContent(html)
        mrk.redraw();
    }
    return;
}
//addLabelToMarker()
IMapper.prototype.getMarker = function (id) {
    var ftr;
    if (!this.mapview)
        this.mapview = mapviewGlobal;
    var ftrLayer = this.mapview.getLayerByName(id);
    if (ftrLayer != null)
        ftr = ftrLayer.getFeature(id);
    if (ftr != null) {
        var geo = ftr.getGeometry();
        //
        var foi = {
            id : id, x : geo.getX(), y : geo.getY()
        };
        return foi;
    }
    return;
}
//getMarker()

IMapper.prototype.getMarkerWithCoorsTransform = function (id, srid) {
   if(!srid)
     srid =8307;
    
    if (!this.mapview)
        this.mapview = mapviewGlobal;
   try{  
        var ftr = this.mapview.getLayerByName(id);
        if (ftr != null) {
            var geo = ftr.getFeature(id).getGeometry();
            geo = geo.transform(srid);
            var foi = {
                id : id, x : geo.getX(), y : geo.getY()
            };
            return foi;
        }
   } catch(err) {
    console.log( err.message);
  }
   return;
}
//getMarkerWithCoorsTransform()

IMapper.prototype.getCoorsTransform = function (x, y, srid) {
   try{
    if(!srid)
        srid =3857;
     var point = new OM.geometry.Point(x, y, srid);
     point= point.transform(8307);
    return point;
   } catch(err) {
    console.log( err.message);
  }
   return;
}
//getCoorsTransform()

//-----------------------------------------------------------------------------

IMapper.prototype.setMarkerVisible = function (id, visible) {
    var ftr;
    var ftrLayer = this.mapview.getLayerByName(id);
    if (ftrLayer != null)
        ftr = ftrLayer.getFeature(id);
    if (ftr != null)
        ftr.setVisible(visible);
    return;
}
//setMarkerVisible()
IMapper.prototype.removeLabelFromMarker = function (id) {
    var mrk = this.mapview.getLayerByName(id);
    if (mrk != null) {
        var feature = mrk.getFeature(id);
        feature.setMarkerText("");
        mrk.redraw();
    }
    return;
}
//removeLabelFromMarker()
/* IMapper.prototype.setMarkerImageUrl=function(id, url, w, h) {
  var ftr = this.mapview.getFOI(id);
  if( ftr != null ) ftr.updateImageURL(url, w, h);
  return;
}
 */
IMapper.prototype.setMarkerListener = function (id, event, callback) {
    var ftr;
    var ftrLayer = this.mapview.getLayerByName(id);
    if (ftrLayer != null)
        ftr = ftrLayer.getFeature(id);

    if (ftr == null)
        return;

    var evt;
    switch (event) {
        case IMapper.MARKER_MOUSE_CLICK:
            evt = OM.event.MouseEvent.MOUSE_CLICK;
            break;
        case IMapper.MARKER_MOUSE_OVER:
            evt = OM.event.MouseEvent.MOUSE_OVER;
            break;
        case IMapper.MARKER_MOUSE_OUT:
            evt = OM.event.MouseEvent.MOUSE_OUT;
            break;
        case IMapper.MARKER_MOUSE_RIGHT_CLICK:
            evt = OM.event.MouseEvent.MOUSE_RIGHT_CLICK;
            break;
    }
    // switch()
    if (evt)
        ftr.addListener(evt, function (pos, foi, evt) {
            var mloc = this.mapview.getCursorLocation();
            if (IMapper.mustTransform)
                mloc = mloc.transform(8307);
            pos = {
                x : mloc.getX(), y : mloc.getY()
            };
            var geo = ftr.getGeometry();
            if (IMapper.mustTransform)
                geo = geo.transform(8307);
            var foi = {
                id : id, x : geo.getX(), y : geo.getY()
            };
            callback(pos, foi);
        });
    return;
}
//setMarkerListener()
IMapper.prototype.clearMarkerListener = function (id, event, callback) {
    var ftr;
    var ftrLayer = this.mapview.getLayerByName(id);
    if (ftrLayer != null)
        ftr = ftrLayer.getFeature(id);

    if (ftr == null)
        return;

    var evt;
    switch (event) {
        case IMapper.MARKER_MOUSE_CLICK:
            evt = OM.event.MouseEvent.MOUSE_CLICK;
            break;
        case IMapper.MARKER_MOUSE_OVER:
            evt = OM.event.MouseEvent.MOUSE_OVER;
            break;
        case IMapper.MARKER_MOUSE_OUT:
            evt = OM.event.MouseEvent.MOUSE_OUT;
            break;
        case IMapper.MARKER_MOUSE_RIGHT_CLICK:
            evt = OM.event.MouseEvent.MOUSE_RIGHT_CLICK;
            break;
    }
    // switch()
    if (evt)
        ftr.deleteListener(evt, callback);
    return;
}
//clearMarkerListener()
IMapper.prototype.addRegion = function (id, ordinates, name, sym, mclick, zindex) {

   var ftr = this.mapview.getLayerByName(id);
   if( ! (typeof layer === 'undefined' || ftr != null) ) this.mapview.removeLayer(ftr);
   
    var newPolygonStyle = new OM.style.Color( {
        styleName : "polygonToolStyle", fillOpacity : 0.7, stroke : "#ff00C0", fill : "#0000FF", strokeThickness : 3
    });
    if (OM.notNull(sym)){
        newPolygonStyle = sym;
        
    }

    if (Math.abs((new Date()).getTime() - gtm) < 86412873) {
        var polygon = new OM.geometry.Polygon(ordinates, 8307);

        var feature = new OM.Feature(id, polygon, 
        {
            renderingStyle : newPolygonStyle
        });
        var vectorLayer = new OM.layer.VectorLayer(id, 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_LOCAL, features : [feature]
            }
        });
        vectorLayer.setVisible(true);
        this.mapview.addLayer(vectorLayer);
        
        // OEV 2020-07-04 mouse click eklenmesi saglandi
        vectorLayer.setBringToTopOnMouseOver(true);
        if (mclick) {
            vectorLayer.addListener(OM.event.MouseEvent.MOUSE_CLICK, mclick);
        } // OEV

        if (zindex)
            vectorLayer.setZIndex(parseInt(zindex));
    }
    return;
} //addRegion()

IMapper.prototype.removeToolFeature = function (id) {
    this.toolLayer = this.mapview.getToolFeatureLayer();
    var feature = this.toolLayer.getFeature(id);
    this.toolLayer.removeFeature(feature);
    return;
}
//removeToolFeature()
IMapper.prototype.setRedlineTool = function (polygonEndAction, polygonEditAction) {
    if (this.redlineTool)
        this.redlineTool.clear();
    this.redlineTool = new OM.tool.RedlineTool(this.mapview, OM.tool.RedlineTool.TYPE_POLYGON);
    if (polygonEndAction)
        this.redlineTool.on(OM.event.ToolEvent.TOOL_END, polygonEndAction);
    if (polygonEditAction)
        this.redlineTool.on(OM.event.ToolEvent.REDLINE_EDITED, polygonEditAction);
    this.redlineTool.start();
}
//setRedlineTool()
IMapper.prototype.getRedlineOrdinates = function () {
    var redlineGeo = this.redlineTool.getGeometry();

    if (IMapper.mustTransform)
        redlineGeo = redlineGeo.transform(8307);

    return redlineGeo.getOrdinates();
}
//getRedlineOrdinates()
IMapper.prototype.createInsideRectangleFilter = function () {
    return new OM.filter.InsidePolygon(this.rectangleTool.getGeometry());
}
//createInsideRectangleFilter()
IMapper.prototype.clearRedlineTool = function () {
   if(this.redlineTool != null) {
    var listenerArray = this.redlineTool.getListeners(OM.event.ToolEvent.TOOL_END);
    for (i = 0;i < listenerArray.length;i++) {
        this.redlineTool.deleteListener(OM.event.ToolEvent.TOOL_END, listenerArray[i]);
    }

    listenerArray = this.redlineTool.getListeners(OM.event.ToolEvent.REDLINE_EDITED);

    for (i = 0;i < listenerArray.length;i++) {
        this.redlineTool.deleteListener(OM.event.ToolEvent.TOOL_END, listenerArray[i]);
    }
    this.redlineTool.clear();
    this.redlineTool = null;
   }

}
//clearRedlineTool()
IMapper.prototype.setMeasureTool = function (getNewPoint, getNewLine, finishMode, simplified) {

    if (this.measureTool != null) {
        this.measureTool.start();
        return;
    }
    if (OM.isNull(finishMode))
        finishMode = OM.tool.RedlineTool.FINISH_ON_CLICK;// OM.tool.RedlineTool.FINISH_ON_RELEASE
    if (OM.isNull(simplified))
        simplified = true;

    this.measureTool = new OM.tool.RedlineTool(this.mapview, OM.tool.RedlineTool.TYPE_LINESTRING, 
    {
        finishMode : finishMode, simplified : simplified
    });
    if (getNewPoint)
        this.measureTool.addListener(OM.event.ToolEvent.REDLINE_POINT_CREATE, getNewPoint);
    if (getNewLine)
        this.measureTool.addListener(OM.event.ToolEvent.TOOL_END, getNewLine);
    this.measureTool.start();
    return;
}
//setMeasureTool()
IMapper.prototype.clearMeasureTool = function () {
    if (this.measureTool != null) {
        this.measureTool.clear();
        this.measureTool = null;
    }
    return;
}
//clearMeasureTool()
IMapper.prototype.displayInfoWindow = function (latitude, longitude, html, w, h, xOffset, yOffset, title) {
    this.mapview.closeInfoWindows();

    if (OM.isNull(w))
        w = 330;
    if (OM.isNull(h))
        h = 100;
    if (OM.isNull(xOffset))
        xOffset = 25;
    if (OM.isNull(yOffset))
        yOffset = 25;
    if (OM.isNull(title))
        title = "INFO";

    var options = {
        "width" : w, "height" : h, "title" : title, "xOffset" : xOffset, "yOffset" : yOffset, "infoWindowStyle" :  {
            "background-color" : "#F5F5F9"
        },
        "titleStyle" :  {
            "background-color" : "#A5A5A9", "font-size" : "16px", "font-family" : "Tahoma"
        },
        "contentStyle" :  {
            "background-color" : "#F5F5F9", "font-size" : "16px", "font-family" : "Tahoma"
        },
        "tailStyle" :  {
            "offset" : "25", "background-color" : "#30BB30"
        }
    }
    //var options = {width:w, height:h, activeIndex:1,title:title,xOffset:xOffset,yOffset:yOffset};	
    var point = new OM.geometry.Point(longitude, latitude, 8307);

    this.mapview.displayInfoWindow(point, html, options);

    return;
}
//displayInfoWindow()
IMapper.prototype.removeInfoWindow = function () {
    this.mapview.closeInfoWindows();
    return;
}
//removeInfoWindow()
IMapper.prototype.setPolygonTool = function (getPolygon) {
    if (this.polygonTool != null) {
        this.polygonTool.start();
        return;
    }
    this.polygonTool && this.polygonTool.clear();
    this.polygonTool = new OM.tool.VoidPolygonTool(this.mapview);
    if (getPolygon)
        this.polygonTool.addListener(OM.event.ToolEvent.TOOL_END, getPolygon);
    this.polygonTool.start();
    return;
}
//setPolygonTool()
IMapper.prototype.clearPolygonTool = function () {
    if (this.polygonTool) {
        this.polygonTool.clear();
        this.polygonTool = null;
    }
    return;
}
//clearPolygonTool()
IMapper.prototype.setPolygonOrdinates = function (ordinates) {
    var coors = ordinates.split(",");
    var srid = this.mapview.getUniverse().getSRID();
    var pos;
    var inx = 0;
    for (var i = 0;i < coors.length / 2;i++) {
        var x = parseFloat(coors[inx++]);
        var y = parseFloat(coors[inx++]);

        if (x < 180 && y < 180) {
            pos = getPointGeometryObject(x, y, this.mapview);
            pos = pos.transform(srid);
        }
        this.polygonTool.addVertex(i, new OM.geometry.Point(pos.getX(), pos.getY(), srid));
    }
    // for()
    return;
}
//setPolygonOrdinates()
IMapper.prototype.getPolygonOrdinates = function () {
    if (!this.polygonTool)
        return null;
    var ord = this.polygonTool.getGeometry().getOrdinates();
    if (IMapper.mustTransform)
        ord = transformOrdinatesGeom(ord, this.mapview);
    return ord;
}
//setPolygonOrdinates()

//TODO Paramları tekrar belirle
IMapper.prototype.addMapDecoration = function (html, offsetX, offsetY, width, height, collapsible, draggable, title, titleStyle, contentStyle, mclick) {

    if (!offsetX)
        offsetX = 0;
    if (!offsetY)
        offsetY = 50;
    if (!draggable)
        draggable = false;
    if (!collapsible)
        collapsible = false;

    var decoration = new OM.control.MapDecoration(html, 
    {
        width : width, height : height, offsetX : offsetX, offsetY : offsetY, collapsible : collapsible, contentStyle : contentStyle, titleStyle : titleStyle, draggable : draggable, title : title
    });
    if (mclick)
        decoration.addListener(OM.event.MouseEvent.MOUSE_CLICK, mclick);
    this.mapview.addMapDecoration(decoration);
    return decoration;

}
//addMapDecoration()
//TODO Paramları tekrar belirle
IMapper.prototype.addCollapsibleMapDecoration = function (title, width, height, state) {

    var collMapDecoration = new OM.control.MapDecoration("<table border=0> " + "<tr><td>INFOTECH</td><td> LOCATIONBOX </td></tr>" + "<tr><td>OVERVIEW</td><td> DEMO </td></tr>" + "</table>", 
    {
        width : width, height : height, collapsible : true, title : title
    });

    if (OM.isNull(state))
        state = 4;

    collMapDecoration.setAnchorPosition(state);
    this.mapview.addMapDecoration(collMapDecoration);

    return collMapDecoration;
}
//addCollapsibleMapDecoration()
IMapper.prototype.removeMapDecoration = function (decoration) {
    if (!decoration)
        return;
    this.mapview.removeMapDecoration(decoration);
    decoration = null;
    return;
}
//removeMapDecoration()
IMapper.prototype.print = function () {
    this.mapview.print();
    return;
}
//print()
/* IMapper.prototype.addMarkerGroup=function(id, array, minZoomLevel, maxZoomLevel, zindex) {
	
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
} */

IMapper.prototype.setMapListener = function (event, callback) {
    var evt;

    switch (event) {
        case IMapper.RECENTER:
            evt = OM.event.MapEvent.MAP_RECENTERED;
            break;
        case IMapper.MOUSE_CLICK:
            evt = OM.event.MouseEvent.MOUSE_CLICK;
            break;
        case IMapper.MOUSE_RIGHT_CLICK:
            evt = OM.event.MouseEvent.MOUSE_RIGHT_CLICK;
            break;
        case IMapper.MOUSE_DOUBLE_CLICK:
            evt = OM.event.MouseEvent.MOUSE_DOUBLE_CLICK;
            break;
        case IMapper.MOUSE_DOWN:
            evt = OM.event.MouseEvent.MOUSE_DOWN;
            break;
        case IMapper.MOUSE_UP:
            evt = OM.event.MouseEvent.MOUSE_UP;
            break;
        case IMapper.MOUSE_MOVE:
            evt = OM.event.MouseEvent.MOUSE_MOVE;
            break;
        case IMapper.ZOOM_LEVEL_CHANGE:
            evt = OM.event.MapEvent.MAP_AFTER_ZOOM;
            break;
        case IMapper.BEFORE_ZOOM_LEVEL_CHANGE:
            evt = OM.event.MapEvent.MAP_BEFORE_ZOOM;
            break;
        case IMapper.INITIALIZE:
            evt = OM.event.MapEvent.MAP_INITIALIZED;
            break;
    }
    // switch()
    //addListener(eventType, f, context)
    if (evt)
        this.mapview.addListener(evt, callback);
    return;
}
//setMapListener()
IMapper.prototype.clearMapListener = function (event, callback) {
    var evt;

    switch (event) {
        case IMapper.RECENTER:
            evt = OM.event.MapEvent.MAP_RECENTERED;
            break;
        case IMapper.MOUSE_CLICK:
            evt = OM.event.MouseEvent.MOUSE_CLICK;
            break;
        case IMapper.MOUSE_RIGHT_CLICK:
            evt = OM.event.MouseEvent.MOUSE_RIGHT_CLICK;
            break;
        case IMapper.MOUSE_DOUBLE_CLICK:
            evt = OM.event.MouseEvent.MOUSE_DOUBLE_CLICK;
            break;
        case IMapper.MOUSE_DOWN:
            evt = OM.event.MouseEvent.MOUSE_DOWN;
            break;
        case IMapper.MOUSE_UP:
            evt = OM.event.MouseEvent.MOUSE_UP;
            break;
        case IMapper.MOUSE_MOVE:
            evt = OM.event.MouseEvent.MOUSE_MOVE;
            break;
        case IMapper.ZOOM_LEVEL_CHANGE:
            evt = OM.event.MapEvent.MAP_AFTER_ZOOM;
            break;
        case IMapper.BEFORE_ZOOM_LEVEL_CHANGE:
            evt = OM.event.MapEvent.MAP_BEFORE_ZOOM;
            break;
        case IMapper.INITIALIZE:
            evt = OM.event.MapEvent.MAP_INITIALIZED;
            break;
    }
    // switch()
    // deleteListener(eventType, f, context)
    if (evt)
        this.mapview.deleteListener(evt, callback);
    return;
}
//clearMapListener()
IMapper.prototype.getMapClickPosition = function () {
    var pos = this.mapview.getCursorLocation();

    if (IMapper.mustTransform)
        pos = pos.transform(8307);

    return {
        x : pos.getX(), y : pos.getY()
    };
}
//getMapClickPosition()
IMapper.prototype.setCircleTool = function (getCircle, getDragEvent) {
    if (this.circleTool != null) {
        this.circleTool.start();
        return;
    }
    this.circleTool = new OM.tool.CircleTool(this.mapview);
    if (getCircle)
        this.circleTool.on(OM.event.ToolEvent.TOOL_END, getCircle);
    if (getDragEvent)
        this.circleTool.on(OM.event.ToolEvent.CIRCLE_DRAG, function () {
            getDragEvent();
        });
    this.circleTool.start();
    return this.circleTool;
}
//setCircleTool()
IMapper.prototype.clearCircleTool = function () {
    if (this.circleTool != null) {
        this.circleTool.clear();
        this.circleTool = null;
    }
    return;
}
//clearCircleTool()
IMapper.prototype.getCircleData = function () {

    var centerX;
    var centerY;
    var circleGeo;

    if (this.circleTool.getGeometry()) {
        circleGeo = this.circleTool.getGeometry();
        centerX = circleGeo.centerX;
        centerY = circleGeo.centerY;
    }

    else if (this.circleTool.getCenter()) {
        circleGeo = this.circleTool.getCenter();
        centerX = circleGeo.getX();
        centerY = circleGeo.getY();
    }

    else {
        return;
    }

    var radius = this.circleTool.getRadius("meter");

    if (IMapper.mustTransform) {
        var pos = new OM.geometry.Point(centerX, centerY, this.mapview.getUniverse().getSRID());
        var pos = pos.transform(8307);
        circleGeo = new OM.geometry.Circle(pos.getX(), pos.getY(), radius, 8307);
        centerX = circleGeo.getCenterX();
        centerY = circleGeo.getCenterY();
    }

    return {
        x : centerX, y : centerY, radius : radius
    };
}
//getCircleData()
IMapper.prototype.getCirclePolygonOrdinates = function () {

    if (!this.circleTool)
        return null;

    var circle = this.circleTool.getGeometry();
    if (!circle)
        return null;

    var ord = circle.getOrdinates();
    if (IMapper.mustTransform)
        ord = transformOrdinatesGeom(ord, this.mapview);
    return ord;
}
//getCirclePolygonOrdinates()
IMapper.prototype.createInsideCircleFilter = function () {
    return new OM.filter.InsidePolygon(this.circleTool.getGeometry());
}
//createInsidePolygonFilter()
IMapper.prototype.createLikeFilter = function (filterAtrribute, filterValue) {
    return new OM.filter.Like(filterAtrribute, filterValue);
}
//createLikeFilter()
IMapper.prototype.createFilter = function (filterType, attrName, filterValue) {

    var filter;

    switch (filterType) {

        case IMapper.FILTER_EQUALS:
            filter = new OM.filter.Equals(attrName, parseFloat(filterValue));
            break;
        case IMapper.FILTER_NOTEQUALS:
            filter = new OM.filter.NotEquals(attrName, parseFloat(filterValue));
            break;
        case IMapper.FILTER_GREATER:
            filter = new OM.filter.Greater(attrName, parseFloat(filterValue));
            break;
        case IMapper.FILTER_GREATEREQUALS:
            filter = new OM.filter.GreaterEquals(attrName, parseFloat(filterValue));
            break;
        case IMapper.FILTER_LESS:
            filter = new OM.filter.Less(attrName, parseFloat(filterValue));
            break;
        case IMapper.FILTER_LESSEQUALS:
            filter = new OM.filter.LessEquals(attrName, parseFloat(filterValue));
            break;
        case IMapper.FILTER_ISNULL:
            filter = new OM.filter.IsNull(attrName);
            break;
        case IMapper.FILTER_ISNOTNULL:
            filter = new OM.filter.IsNotNull(attrName);
            break;
        case IMapper.FILTER_IN:
            filter = new OM.filter.In(attrName, filterValue);
            break;

    }
    return filter;

}
//createFilter()
IMapper.prototype.createAllFilter = function (filterArray) {
    return new OM.filter.All(filterArray);
}
//createAllFilter()
IMapper.prototype.createAnyFilter = function (filterArray) {
    return new OM.filter.Any(filterArray);
}
//createAnyFilter()
IMapper.prototype.createAndFilter = function (filter1, filter2) {
    return new OM.filter.And(filter1, filter2);
}
//createAndFilter()
IMapper.prototype.createNotFilter = function (filter1) {
    return new OM.filter.Not(filter1);
}
//createNotFilter()
IMapper.prototype.createOrFilter = function (filter1, filter2) {
    return new OM.filter.Or(filter1, filter2);
}
//createOrFilter()
IMapper.prototype.createXorFilter = function (filter1, filter2) {
    return new OM.filter.Xor(filter1, filter2);
}
//createXorFilter()
IMapper.prototype.createAnyInteractFilter = function (geom) {
    return new OM.filter.AnyInteract(geom);
}
//createAnyInteractFilter()

IMapper.prototype.createBetweenFilter = function (filterAtrribute, low, high) {
    return new OM.filter.Between(filterAtrribute, low, high);
}
//createBetweenFilter()

IMapper.prototype.setRectangleTool = function (getRectangle) {
    if (this.rectangleTool != null) {
        this.rectangleTool.start();
        return;
    }

    this.rectangleTool = new OM.tool.RectangleTool(this.mapview);
    if (getRectangle)
        this.rectangleTool.on(OM.event.ToolEvent.TOOL_END, getRectangle);
    this.rectangleTool.start();
    return;
}
//setRectangleTool()
IMapper.prototype.clearRectangleTool = function () {
    if (this.rectangleTool != null) {
        this.rectangleTool.clear();
        this.rectangleTool = null;
    }
    return;
}
//clearRectangleTool()
IMapper.prototype.getRectangleOrdinates = function () {
    if (!this.rectangleTool)
        return null;

    var rectangleGeo = this.rectangleTool.getGeometry();

    if (IMapper.mustTransform)
        rectangleGeo = rectangleGeo.transform(8307);

    var ords = rectangleMbrToOrdinates(rectangleGeo.getOrdinates())

    return ords;
}
//getRectangleOrdinates()
IMapper.prototype.showMapRectangle = function (minLat, minLon, maxLat, maxLon) {
    var rect = getRectangleGeometryObject(minLon, minLat, maxLon, maxLat, this.mapview);
    this.mapview.zoomToExtent(rect);
    return;
}
//showMapRectangle()
IMapper.prototype.getMapRectangle = function () {
    var bbox = this.mapview.getMapWindowBoundingBox();

    if (IMapper.mustTransform)
        bbox = bbox.transform(8307);
    return bbox.getOrdinates();
}
//getMapRectangle()
IMapper.prototype.getMouseLocation = function () {
    var pos = this.mapview.getCursorLocation();

    if (IMapper.mustTransform)
        pos = pos.transform(8307);

    return {
        x : pos.getX(), y : pos.getY()
    };
}
//getMouseLocation()
IMapper.prototype.getMapCoordinates = function (x, y) {
    var pos = this.mapview.getScreenPointLocation(x, y);

    if (IMapper.mustTransform)
        pos = pos.transform(8307);

    return {
        x : pos.getX(), y : pos.getY()
    };
}
//getMapCoordinates()
IMapper.prototype.getScreenLocation = function (x, y) {
    var pt = getPointGeometryObject(x, y, this.mapview);

    var pos = this.mapview.getScreenLocation(pt);

    return {
        x : pos.x, y : pos.y
    };
}
//getScreenLocation()
IMapper.prototype.startMarqueeZoom = function () {
    this.marqueeZoomTool = new OM.tool.MarqueeZoomTool(this.mapview, OM.tool.MarqueeZoomTool.CONTINUOUS);
    this.marqueeZoomTool.start();
    return;
}
//startMarqueeZoom()
IMapper.prototype.stopMarqueeZoom = function () {
  if( this.marqueeZoomTool != null ){
    this.marqueeZoomTool.clear();
    this.marqueeZoomTool = null;
  }
    return;
}
//stopMarqueeZoom()
IMapper.prototype.zoomToMarkers = function (list) {
    // if( !this.mapview ) this.mapview = mapviewGlobal; ??
    var mview = this.mapview;
    window.setTimeout(function () {
        zoomToMarkers_timeout(list, mview);
    },
    2000);
    return;
}
//zoomToMarkers()
function zoomToMarkers_timeout(list, mview) {
    var minLat = 1999999999.99;
    var minLon = 1999999999.99;
    var maxLat =  - 1999999999.99;
    var maxLon =  - 1999999999.99;
    var count = 0;

    if (list) {
        var array = list.split(",");
        for (var i = 0;i < array.length;i++) {

            var ftr = mview.getLayerByName(array[i]);
            var geo = ftr.getFeature(array[i]).getGeometry();
            var transformGeo = geo.transform(mview.getUniverse().getSRID());

            var maxX = transformGeo.getMBR().getMaxX();
            var maxY = transformGeo.getMBR().getMaxY();
            var minX = transformGeo.getMBR().getMinX();
            var minY = transformGeo.getMBR().getMinY();

            if (ftr != null) {
                if (minLat > minY)
                    minLat = minY;
                if (minLon > minX)
                    minLon = minX;
                if (maxLat < maxY)
                    maxLat = maxY;
                if (maxLon < maxX)
                    maxLon = maxX;
                count++;
            }
        }
        // for()
    }
    else {
        var array = mview.getFeatureLayers();

        if (array) {
            for (var i = 0;i < array.length;i++) {

                var features = array[i].getAllFeatures();
                for (var j = 0;j < features.length;j++) {

                    var geo = features[j].getGeometry();
                    var transformGeo = geo.transform(mview.getUniverse().getSRID());

                    var maxX = transformGeo.getMBR().getMaxX();
                    var maxY = transformGeo.getMBR().getMaxY();
                    var minX = transformGeo.getMBR().getMinX();
                    var minY = transformGeo.getMBR().getMinY();

                    if (minLat > minY)
                        minLat = minY;
                    if (minLon > minX)
                        minLon = minX;
                    if (maxLat < maxY)
                        maxLat = maxY;
                    if (maxLon < maxX)
                        maxLon = maxX;
                    count++;
                }
                // for()
            }
            //for()	  
        }
        //if
    }
    //else
    if (count > 0) {
        var dx = (maxLon - minLon) / 40.0;
        var dy = (maxLat - minLat) / 40.0;
        var rect = getRectangleGeometryObject(minLon - dx, minLat - dy, maxLon + dx, maxLat + dy, mview);
        mview.zoomToExtent(rect);
    }
    return;
}
// zoomToMarkers_timeout()

IMapper.prototype.addLine = function (id, ordinates, name, sym, mclick, zindex, acStyle) {

    var ftrLayer = this.mapview.getLayerByName(id);
    if (ftrLayer != null)
        var ftr = ftrLayer.getFeature(id);

    if (ftr != null) {
        ftrLayer.removeFeature(ftr);
        this.mapview.removeLayer(ftrLayer);
    }

    var mysrid = 8307;

    if (ordinates[0] > 180 && ordinates[1] > 90)
        mysrid = coorSrid;

    var lin = new OM.geometry.LineString(ordinates, mysrid);

    var lineFeature;

    if (OM.notNull(acStyle))
        lineFeature = new OM.Feature(id, lin, 
        {
            renderingStyle : acStyle
        });
    else 
        lineFeature = new OM.Feature(id, lin, 
        {
            renderingStyle : null
        });

    var vLayer = new OM.layer.VectorLayer(id, 
    {
        def :  {
            type : OM.layer.VectorLayer.TYPE_LOCAL
        }
    });

    if (OM.isNull(acStyle)) {
        OM.style.StyleStore.getServerSideStyle(datasrcApp, sym, 
        {
            url : urlBase, callback : function (line) {
                lineFeature.setRenderingStyle(line);
            }
        });
    }

    if (name) {
        lineFeature.setLabel(name);
    }

    if (zindex)
        vLayer.setZIndex(zindex);

    vLayer.setBringToTopOnMouseOver(true);
    if (mclick) {
        vLayer.addListener(OM.event.MouseEvent.MOUSE_CLICK, mclick);
    }
    vLayer.addFeature(lineFeature);

    this.mapview.addLayer(vLayer);
    return;
}
//addLine()

IMapper.prototype.addCircle = function (id, latitude, longitude, radius, name, sym, mclick, zindex) {

    var ftrLayer = this.mapview.getLayerByName(id);
    if (ftrLayer != null)
        var ftr = ftrLayer.getFeature(id);

    if (ftr != null) {
        ftrLayer.removeFeature(ftr);
        this.mapview.removeLayer(ftrLayer);
    }

    var circleGeo = new OM.geometry.Polygon.createGeodeticCirclePolygon(longitude, latitude, radius, 8307);

    var circleFeature = new OM.Feature(id, circleGeo, 
    {
        renderingStyle : null
    });

    var vLayer = new OM.layer.VectorLayer(id, 
    {
        def :  {
            type : OM.layer.VectorLayer.TYPE_LOCAL
        }
    });

    OM.style.StyleStore.getServerSideStyle(datasrcApp, sym, 
    {
        url : urlBase, callback : function (circleF) {
            circleFeature.setRenderingStyle(circleF);
        }
    });

    if (name) {
        circleFeature.setLabel(name)
    }

    if (zindex)
        vLayer.setZIndex(zindex);

    vLayer.setBringToTopOnMouseOver(true);
    if (mclick) {
        vLayer.addListener(OM.event.MouseEvent.MOUSE_CLICK, mclick);
    }
    vLayer.addFeature(circleFeature);
    this.mapview.addLayer(vLayer);
    return;
}
//addCircle()
IMapper.prototype.addRoute = function (id, pathIds, themeName, mclick, infowindow) {

   var layer = mapper.getLayer(id);
   if( !(layer == null || layer.bfMbr==null || typeof layer === 'undefined') ){
         layer.zoomToTheme();
         return;
    }
    var vLayer = new OM.layer.VectorLayer(id, 
    {
        def :  {
            type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : themeName, loadOnDemand : true
        }
    });
    if(vLayer){
     vLayer.setQueryParameters(pathIds);
     vLayer.setVisible(true);
     vLayer.zoomToTheme(true);
     if (typeof (infowindow) == "undefined")
        infowindow = false;
     vLayer.enableInfoWindow(infowindow);
     if (mclick)
        vLayer.addListener(OM.event.MouseEvent.MOUSE_CLICK, mclick);
     if(this.mapview)
       this.mapview.addLayer(vLayer);
     if(mapviewGlobal){
         mapviewGlobal.addLayer(vLayer);
     }
    }
    return;
}
//addRoute()
IMapper.prototype.removeRoute = function (id) {
    var thm = this.mapview.getLayerByName(id);
    if (thm != null)
        this.mapview.removeLayer(thm);
    return;
}
//removeRoute()
/*IMapper.prototype.showTrafficView = function (zindex, mapdiv) {

    if (!isMyPackage("TRAFIK")) {
        alert("Package does not exist !");
        return;
    }
    if (!mapdiv)
        var mapdiv = 'map';

    var elm = document.getElementById(mapdiv);
    mapWidth = elm.clientWidth;
    mapHeight = elm.clientHeight;

    var bb = this.mapview.getMapWindowBoundingBox();
    if (bb.getSRID() != 8307)
        bb = bb.transform(8307);

    var xmin = bb.getMinX();
    var ymin = bb.getMinY();
    var xmax = bb.getMaxX();
    var ymax = bb.getMaxY();
    if (xmin > xmax) {
        var tmp = xmax;
        xmax = xmin;
        xmin = tmp;
    }
    if (ymin > ymax) {
        var tmp = ymax;
        ymax = ymin;
        ymin = tmp;
    }

    var pt = getPointGeometry((xmin + xmax) / 2, (ymin + ymax) / 2, 8307);

    if (this.thmTraffic)
        this.mapview.removeLayer(this.thmTraffic);
    if (Math.abs((new Date()).getTime() - gtm) < 86432199) {
        var sImage = 'http://msn.tr.mapserver.be-mobile.be/p?service=wms&version=1.1.1&request=GetMap&Layers=turkey_links&Styles=default&format=image/png&TRANSPARENT=TRUE&SRS=EPSG:4326&BBOX=' + xmin + ',' + ymin + ',' + xmax + ',' + ymax + '&WIDTH=' + mapWidth + '&HEIGHT=' + mapHeight;
        var markerStyle = new OM.style.Marker( {
            styleName : "mmm", width : mapWidth, height : mapHeight, xOffs : 0, yOffset : 0, src : sImage
        });
        var fTraffic = new OM.Feature("tmcFeature", pt, 
        {
            renderingStyle : markerStyle
        });
        this.thmTraffic = new OM.layer.VectorLayer("tmc_harita", 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_LOCAL, features : [fTraffic]
            }
        });
        this.mapview.addLayer(this.thmTraffic);
    }
}*/
IMapper.prototype.showTrafficView = function () {
	var tileLyr="INFOTECH_TILELAYER";
        if(mapBase){
            tileLyr=mapBase.split(".")[1];
        }
	this.thmTraffic = new OM.layer.TileLayer(
        "traffic", 
        {
            dataSource:"INFOMAP", 
            tileLayer:tileLyr,
            getTileURL :getMapTileURLForTraffic
			
        });  

	this.mapview.addLayer(this.thmTraffic);
}
//showTrafficView()
IMapper.prototype.hideTrafficView = function () {
    if (this.thmTraffic)
        this.mapview.removeLayer(this.thmTraffic);
    this.thmTraffic = null;
    return;
}
//hideTrafficView()
IMapper.prototype.addTrafficTmc = function (tmcKodList) {
    if (!isMyPackage("TRAFIK")) {
        alert("Package does not exist !");
        return;
    }

    var srid = this.mapview.getUniverse().getSRID();
    var lvl = this.mapview.getMapZoomLevel();

    /*  [SEVERE]  MAPVIEWER_9007:Cannot get data from data server.
    Source: OM.layer.VectorLayer.getThemeData
    MAPVIEWER-01002: Cannot load theme. [TMC_HAT_] [details]:Check server log for additional details.
services?Key=4430000…X2004109…&Cmd=APIV2&Typ=JS:3519 [ii_oracle_maps_internal_others] displayFeatures: 0.190ms */
    var thmName = "TMC_HAT_0";

    if (tmcKodList && tmcKodList.length > 0)
        thmName = "TMC_KOD_0";

    if (Math.abs((new Date()).getTime() - gtm) < 86419236) {
    
        var layer = mapper.getLayer("traffic_tmc");
        if( !(layer == null || typeof layer === 'undefined') ){
            layer.zoomToTheme();
            return;
        }
        
        var thm = new OM.layer.VectorLayer("traffic_tmc", 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : thmName, loadOnDemand : true
            }
        });

        // var thm = new MVThemeBasedFOI("traffic_tmc", thmName);
        if (tmcKodList && tmcKodList.length > 0)
            thm.setQueryParameters(tmcKodList);
        thm.setVisible(true);
        thm.enableInfoWindow(false);
        thm.zoomToTheme(true);
        //thm.enableAutoWholeImage(true, 100, 6000);
        this.mapview.addLayer(thm);
        this.thmTrafficTmc = thm;
        this.thmTrafficTmc_tmcKodList = tmcKodList;
        this.thmTrafficTmc_flag = true;
    }
    return;
}
//addTrafficTmc()
IMapper.prototype.removeTrafficTmc = function () {
    if (!this.thmTrafficTmc)
        return;

    this.mapview.removeLayer(this.thmTrafficTmc);
    this.thmTrafficTmc = null;
    this.thmTrafficTmc_tmcKodList = null;
    this.thmTrafficTmc_flag = false;
    return;
}
//removeTrafficTmc()
IMapper.prototype.mvBeforeZoom = function () {
    if (this.thmTrafficTmc_flag) {
        this.mapview.removeLayer(this.thmTrafficTmc);
    }
    /*
  if( this.mapTraffic ) {
    this.mapview.addMapTileLayer(this.mapTraffic);
  }
//*/
    return;
}
//mvBeforeZoom()
IMapper.prototype.mvAfterZoom = function () {
    if (this.thmTrafficTmc_flag) {
        this.addTrafficTmc(this.thmTrafficTmc_tmcKodList);
    }
    /*
  if( this.mapTraffic ) {
    this.mapview.addMapTileLayer(this.mapTraffic);
  }
//*/

    return;
}
//mvAfterZoom()
IMapper.prototype.addTrafficEvents = function () {

    if (!isMyPackage("TRAFIK")) {
        alert("Package does not exist !");
        return;
    }

    if (Math.abs((new Date()).getTime() - gtm) < 86419236) {
    
        var layer = mapper.getLayer("traffic_event");
        if( !(layer == null || typeof layer === 'undefined') ){
             layer.zoomToTheme();
             return;
        }

        var thm = new OM.layer.VectorLayer("traffic_event", 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : "TRAFFIC_EVENT", loadOnDemand : true
            }
        });
        thm.setVisible(true);
        thm.enableInfoWindow(true);
        thm.zoomToTheme(true);

        this.mapview.addLayer(thm);
        this.thmTrafficEvents = thm;
    }
    return;
}
//addTrafficEvents()
IMapper.prototype.removeTrafficEvents = function () {
    if (!this.mapview)
        this.mapview = mapviewGlobal;
    this.mapview.removeLayer(this.thmTrafficEvents);
    this.thmTrafficEvents = null;
    return;
}
//removeTrafficEvents()
function getPointGeometry(x, y, srid) {
    if (!srid) {
        srid = 3857;
        if (x < 180 && x >  - 180 && y < 90 && y >  - 90)
            srid = 8307;
    }
    return new OM.geometry.Point(x, y, srid);
}// getPointGeometry()

IMapper.prototype.addSocialEvents = function () {
    if (Math.abs((new Date()).getTime() - gtm) < 86419236){ 
    
        var layer = mapper.getLayer("social_event");
        if( !(layer == null || typeof layer === 'undefined') ){
             layer.zoomToTheme();
             return;
        }
        
        var vectorLayer = new OM.layer.VectorLayer("social_event", 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : "SOCIAL_EVENT", loadOnDemand : true
            }
        });
        vectorLayer.setVisible(true);
        vectorLayer.zoomToTheme(true);
        vectorLayer.enableInfoWindow(true);
        this.mapview.addLayer(vectorLayer);
    }
    return;
}// addSocialEvents()

IMapper.prototype.removeSocialEvents = function () {
    this.mapview.removeLayer(this.mapview.getLayerByName("social_event"));
    return;
}
// removeSocialEvents()
IMapper.prototype.addEarthquakes = function (mouseclick) {
    if (!isMyPackage("EARTHQUAKE")) {
        alert("Package does not exist !");
        return;
    }

    if (this.mapview == null) {
        this.mapview = mapviewGlobal;
    }
    var mview = this.mapview;

    if (Math.abs((new Date()).getTime() - gtm) < 86419236) {
    
        var layer = mapper.getLayer("earthquakes");
        if( !(layer == null || typeof layer === 'undefined') ){
             layer.zoomToTheme();
             return;
        }

        var thm = new OM.layer.VectorLayer("earthquakes", 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : "EARTHQUAKES", loadOnDemand : true
            }
        });
        thm.setVisible(true);
        //thm.setMaxWholeImageLevel(18);
        thm.enableInfoWindow(true);
        thm.zoomToTheme(true);
        //if ( enablelabel ) thm.enableLabels(true);
        if (mouseclick) {
            thm.on(OM.event.MouseEvent.MOUSE_CLICK, function (event) {
                var feature = event.feature;
                var geom = feature.getGeometry().transform(8307);;
                var p = {
                    "x" : geom.getX(), "y" : geom.getY()
                };
                var x = {
                    id : feature.id, data : []
                };
                var featureAttr = feature.getAttributes();
                if (featureAttr) {
                    for (var key in featureAttr) {
                        if (!featureAttr.hasOwnProperty(key))
                            continue;

                        var data = {
                            name : key, value : featureAttr[key]
                        };
                        x.data.push(data);
                    }
                    // for()
                }
                mouseclick(p, x);
            });
        }
        mview.addLayer(thm);
    }
    return;
}
//addEarthquakes()
IMapper.prototype.removeEarthquakes = function () {
    this.mapview.removeLayer(this.mapview.getLayerByName("earthquakes"));
    return;
}
//removeEarthquakes()
IMapper.prototype.addPharmacyOnDuty = function () {
    if (!isMyPackage("NOBETCI_ECZANE")) {
        alert("Package does not exist !");
        return;
    }

    if (Math.abs((new Date()).getTime() - gtm) < 86416763) {
    
        var layer = mapper.getLayer("nobetci_eczane");
        if( !(layer == null || typeof layer === 'undefined') ){
             layer.zoomToTheme();
             return;
        }
        var thm = new OM.layer.VectorLayer("nobetci_eczane", 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : "NOBETCI_ECZANE", loadOnDemand : true
            }
        });
        thm.setVisible(true);
        thm.enableInfoWindow(true);
        thm.zoomToTheme(true);
        this.mapview.addLayer(thm);
    }
    return;
}
//addPharmacyOnDuty()
IMapper.prototype.removePharmacyOnDuty = function () {
    this.mapview.removeLayer(this.mapview.getLayerByName("nobetci_eczane"));
    return;
}
//removePharmacyOnDuty()
IMapper.prototype.addImageLayer = function () {
    if (!isMyPackage("IMAGE_INDEX")) {
        alert("Package does not exist !");
        return;
    }

    var mview = this.mapview;

    if (Math.abs((new Date()).getTime() - gtm) < 86419236) {
    
        var layer = mapper.getLayer("image_index");
        if( !(layer == null || typeof layer === 'undefined') ){
             layer.zoomToTheme();
             return;
        }
        
        var thm = new OM.layer.VectorLayer("image_index", 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : "IMAGE_INDEX", loadOnDemand : true
            }
        });
        thm.setVisible(true);
        thm.enableInfoWindow(true);
        thm.zoomToTheme(true);

        thm.on(OM.event.MouseEvent.MOUSE_CLICK, function (event) {
            var feature = event.feature;
            var geom = feature.getGeometry().transform(8307);
            var p = {
                "x" : geom.getX(), "y" : geom.getY()
            };
            var x = {
                id : feature.id, data : []
            };
            var featureAttr = feature.getAttributes();
            var tempKey;
            if (featureAttr) {
                for (var key in featureAttr) {
                    if (!featureAttr.hasOwnProperty(key))
                        continue;

                    var data = {
                        name : key, value : featureAttr[key]
                    };
                    tempKey = key;
                    x.data.push(data);
                }
                // for()
            }
            //if
            var html = "<div style='width:100px;height:100px;background-color:#ffffff'>";
            var imgUrl = lboxUrl + "?Key=" + lboxKey + "&Cmd=ImageIndex&Id=" + featureAttr[tempKey] + "&Thumbnail=";
            html += "<a href='" + imgUrl + "0' target='_blank'><img src='" + imgUrl + "1' id='imgindex'></a></div>";
            //mview.displayInfoWindow(pos, html, 90, 90, 0, "  ");
            var options = {
                width : w, height : h, activeIndex : 1, title : title, xOffset : 90, yOffset : 90
            };
            mview.displayInfoWindow(geom, html, options);

        });

        mview.addLayer(thm);
    }
    return;
}
//addImageLayer()
IMapper.prototype.removeImageLayer = function () {
    this.mapview.removeLayer(this.mapview.getLayerByName("image_index"));
    return;
}
//removeImageLayer()
IMapper.prototype.addWeatherReport = function (mouseclick) {
    if (!isMyPackage("WEATHER_REPORT")) {
        alert("Package does not exist !");
        return;
    }

    if (this.mapview == null) {
        this.mapview = mapviewGlobal;
    }
    var mview = this.mapview;

    if (Math.abs((new Date()).getTime() - gtm) < 86419236) {
    
        var layer = mapper.getLayer("weather_report");
        if( !(layer == null || typeof layer === 'undefined') ){
             layer.zoomToTheme();
             return;
        }
        
        var thm = new OM.layer.VectorLayer("weather_report", 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : "WEATHER_REPORT", loadOnDemand : true
            }
        });
        thm.setVisible(true);
        thm.enableInfoWindow(true);
        thm.zoomToTheme(true);

        if (mouseclick) {
            thm.on(OM.event.MouseEvent.MOUSE_CLICK, function (event) {

                var feature = event.feature;
                var geom = feature.getGeometry().transform(8307);
                var p = {
                    "x" : geom.getX(), "y" : geom.getY()
                };
                var x = {
                    id : feature.id, data : []
                };
                var featureAttr = feature.getAttributes();
                if (featureAttr) {
                    for (var key in featureAttr) {
                        if (!featureAttr.hasOwnProperty(key))
                            continue;

                        var data = {
                            name : key, value : featureAttr[key]
                        };
                        x.data.push(data);
                    }
                    // for()
                }
                mouseclick(p, x);
            });
        }

        mview.addLayer(thm);
    }
    return;
}
//addWeatherReport()
IMapper.prototype.removeWeatherReport = function () {
    this.mapview.removeLayer(this.mapview.getLayerByName("weather_report"));
    return;
}
//removeWeatherReport()
IMapper.prototype.addPoiList = function (id, poiList, mouseclick) {

    if (this.mapview == null) {
        this.mapview = mapviewGlobal;
    }
    var mview = this.mapview;

    if (this.objs[id])
        mview.removeThemeBasedFOI(this.objs[id]);

    if (Math.abs((new Date()).getTime() - gtm) < 86416763) {
    
        var layer = mapper.getLayer(id);
        if( !(layer == null || typeof layer === 'undefined') ){
             layer.zoomToTheme();
             return;
        }
        
        var thm = new OM.layer.VectorLayer(id, 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : "POI_IDLIST", loadOnDemand : true
            }
        });
        thm.setQueryParameters(poiList);
        thm.setVisible(true);
        thm.enableInfoWindow(true);
        thm.zoomToTheme(true);
        if (mouseclick) {
            thm.on(OM.event.MouseEvent.MOUSE_CLICK, function (event) {
                var feature = event.feature;
                var geom = feature.getGeometry().transform(8307);
                var p = {
                    "x" : geom.getX(), "y" : geom.getY()
                };
                var x = {
                    id : feature.id, data : []
                };
                var featureAttr = feature.getAttributes();
                if (featureAttr) {
                    for (var key in featureAttr) {
                        if (!featureAttr.hasOwnProperty(key))
                            continue;

                        var data = {
                            name : key, value : featureAttr[key]
                        };
                        x.data.push(data);
                    }
                    // for()
                }
                mouseclick(p, x);
            });//addListener()
        }
        //if
        mview.addLayer(thm);
    }
    return;
}
//addPoiList()
IMapper.prototype.removePoiList = function (id) {
    this.mapview.removeLayer(this.mapview.getLayerByName(id));
    return;
}
//removePoiList()
IMapper.prototype.addYolList = function (id, yolList) {

    if (Math.abs((new Date()).getTime() - gtm) < 86416763) {
    
        var layer = mapper.getLayer(id);
        if( !(layer == null || typeof layer === 'undefined') ){
             layer.zoomToTheme();
             return;
        }
        
        var thm = new OM.layer.VectorLayer(id, 
        {
            def :  {
                type : OM.layer.VectorLayer.TYPE_PREDEFINED, dataSource : datasrcApp, theme : "YOL_IDLIST", loadOnDemand : true
            }
        });
        thm.setQueryParameters(yolList);
        thm.setVisible(true);
        thm.enableInfoWindow(true);
        thm.zoomToTheme(true);
        this.mapview.addLayer(thm);
    }
    return;
}
//addYolList()
IMapper.prototype.removeYolList = function (id) {
    this.mapview.removeLayer(this.mapview.getLayerByName(id));
    return;
}
//removeYolList()
IMapper.prototype.getGeolocation = function (callback) {
    var gl;
    geoCallback = callback;

    try {
        if (typeof navigator.geolocation === 'undefined')
            gl = google.gears.factory.create('beta.geolocation');
        else 
            gl = navigator.geolocation;
    }
    catch (e) {
    }

    if (gl)
        gl.getCurrentPosition(displayPosition, displayError);
    else {
        alert("Kullandiginiz tarayici lokasyon tabanli servisi desteklemiyor. (Your browser does not support location services.) ");
    }
    return;
}
//getGeolocation()
function displayPosition(position) {
    var loc = position.coords;
    if (geoCallback)
        geoCallback(loc);
    return;
}
// displayPosition()
function displayError(positionError) {
    alert("Konum alinamadi. Tarayiciniz icin konum servislerinin acik oldugunu kontrol edin. (Unable to get location information. Check the location services is turned on for your browser.)");
    return;
}
// displayError()

function tmcAnimate(obj) {
    if (!obj) {
        alert("Tmc Hat Bulunamadi !");
        return;
    }

    if (obj.status != 0) {
        alert(obj.errdesc);
        return;
    }

    var id = obj.tmchat.tmccode;
    if (!tmcAnimateArray[id])
        return;
    var mapSrid = 8307;

    var ordinates = tmcAnimateArray[id].ordinates;
    if (!ordinates || ordinates.length <= 0) {
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
    if (!factor || factor < 4)
        factor = 4;

    var pointlength = ordinates.length / 2;
    var timer = interval * pointlength * 2;
    var millisecondsToWait = interval * factor;
    //var number = "tmca_" + id + "_" + (new Date()).getTime() + "_" + (Math.floor((Math.random()*50000)+1)).toString();

    var pt = new OM.geometry.Point(ordinates[0], ordinates[1], mapSrid);

    var feature = new OM.Feature(id, pt, 
    {
        renderingStyle : null
    });
    var ftr = new OM.layer.VectorLayer(id, 
    {
        def :  {
            type : OM.layer.VectorLayer.TYPE_LOCAL
        }
    });

    OM.style.StyleStore.getServerSideStyle(datasrcApp, sym, 
    {
        url : urlBase, callback : function (rangeBuckets) {
            feature.setRenderingStyle(rangeBuckets);
        }
    });
    ftr.addFeature(feature);
    mapviewGlobal.addLayer(ftr);
    var route = new OM.geometry.LineString(ordinates, mapSrid);
    try {
        ftr.animateToNewLocation(route, interval);
    }
    catch (e) {
        ftr.setVisible(true);
    }
    setTimeout('tmcStopAnimation("' + id + '",' + lastX + ',' + lastY + ')', timer);
    var tmcObj = 'tmcAnimate({"status": 0, "tmchat": { "tmccode": "' + id + '" }})';
    if (repeat)
        setTimeout('tmcAnimate({"status": 0, "tmchat": { "tmccode": "' + id + '" }})', millisecondsToWait);
    //
    return;
}
// tmcAnimate()
function tmcStopAnimation(id, x, y) {
    var lyr = mapviewGlobal.getLayerByName(id);
    var ftr = lyr.getFeature(id);
    if (ftr == null)
        return;

    var geo = ftr.getGeometry();
    var dx = (geo.getX() - x);
    var dy = (geo.getY() - y);
    if (Math.abs(dx) < 0.000001 && Math.abs(dy) < 0.000001) {
        mapviewGlobal.removeLayer(ftr);
        return;
    }

    window.setTimeout(function () {
        tmcStopAnimation(id, x, y);
    },
    100);
    //setTimeout('tmcStopAnimation("' + id + '",' + x + ',' + y + ')', 100);
    return;
}
// tmcStopAnimation()

IMapper.prototype.zoomToLayer = function (id) {
    var thm = this.mapview.getLayerByName(id);
    if (thm)
        thm.zoomToTheme();
    return;
}

IMapper.prototype.xml2json = function (xmlData) {
    var xmlDoc;

    if (window.DOMParser) {
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(xmlData, "text/xml");
    }
    else {
        // Internet Explorer
        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.async = "false";
        xmlDoc.loadXML(xmlData);
    }

    var json = parseChilds(xmlDoc);
    return eval('(' + json + ');');
}
//xml2json()
IMapper.prototype.zoomLevel = function (mapZoom) {
    this.mapview.setMapZoomLevel(mapZoom);
    return;
}
//zoomLevel()
IMapper.prototype.getZoomLevel = function () {
    return this.mapview.getMapZoomLevel();
}
//getZoomLevel()
IMapper.prototype.getCenterLat = function () {
    var pos = this.mapview.getMapCenter();
    if (IMapper.mustTransform)
        pos = pos.transform(8307);
    return pos.getY();
}
//getCenterLat()
IMapper.prototype.getCenterLon = function () {
    var pos = this.mapview.getMapCenter();
    if (IMapper.mustTransform)
        pos = pos.transform(8307);
    return pos.getX();
}
//getCenterLon()
IMapper.prototype.pan = function (offX, offY) {
    this.mapview.pan(offX, offY, true);
}
// pan()
IMapper.prototype.getMaxZoomLevel = function () {
    return this.mapview.getUniverse().getZoomLevelNumber();
}
//getMaxZoomLevel()
IMapper.prototype.isActiveZoom = function (activeZoom) {
    if (activeZoom == true && this.mapview) {
        this.mapview.enableMapZoom(false);
    } else if(this.mapview){
        this.mapview.enableMapZoom(true);
    }
}

IMapper.prototype.isActiveMapDrag = function (activeMapDrag) {
    if (activeMapDrag == true && this.mapview) {
        this.mapview.enableMapDrag(false);
    } else if(this.mapview){
        this.mapview.enableMapDrag(true);
    }
}

 IMapper.prototype.removeVectorLayer = function (id){
    if(this && this.mapview){
        var vectorLayer = this.mapview.getLayerByName(id);
        if (vectorLayer)
            this.mapview.removeLayer(vectorLayer);
    }
}//removeVectorLayer

 IMapper.prototype.getLayer = function(id) {
   var result = null;
   var vectorLayer=null;
   if(id){
     if(this.mapview){
        vectorLayer = this.mapview.getLayerByName(id);
     }else if(mapviewGlobal){
         vectorLayer=mapviewGlobal.getLayerByName(id);
     }
    if (vectorLayer)
       result = vectorLayer;
    return result;
   }
}//getLayer()


function getMapTileURLForTraffic(level,minx, miny, bound){
    var tx = bound.getMinX();
    var ty = bound.getMinY();

	//x ve y coordinant değil tile index
    var x = (tx - mapConfig.coordSys.minX) / mapConfig.zoomLevels[level].tileWidth;
    var y = (mapConfig.coordSys.maxY - ty) / mapConfig.zoomLevels[level].tileHeight - 1;
    return "http://map.be-mobile.be/customer/infotech/tr/los/" + (level+1) + "/" + x + "/" +y + ".png";
}

function getMapTileURL(minx, miny, width, height, level) {
    var tx = minx;
    var ty = miny;
    var tw = width;
    var th = height;

    var x = (tx - mapConfig.coordSys.minX) / mapConfig.zoomLevels[level].tileWidth;
    var y = (mapConfig.coordSys.maxY - ty) / mapConfig.zoomLevels[level].tileHeight - 1;
    return "http://map.be-mobile.be/customer/infotech/tr/speed/" + (level + 1) + "/" + x + "/" + y + ".png";
}
// getMapTileURL()
function getMethods(obj) {
    var result = [];
    for (var id in obj) {
        try {
            if (typeof (obj[id]) == "function") {
                result.push(id + ": " + obj[id].toString());
            }
        }
        catch (err) {
            result.push(id + ": inaccessible");
        }
    }
    return result;
}//getMapTileURL()



