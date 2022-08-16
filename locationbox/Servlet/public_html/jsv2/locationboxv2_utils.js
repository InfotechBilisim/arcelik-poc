
//-----------------------------------------------------------------------------

  function isFunction(obj) { return Object.prototype.toString.call(obj) == '[object Function]'; }

//  function isFunction(obj) { return !!(obj && obj.constructor && obj.call && obj.apply); };

//-----------------------------------------------------------------------------

function convCategory2Where(category) {
  for( var i = 0; i < categoryList.length; i++ ) {
    if( category == categoryList[i].id ) return categoryList[i].wc;

  } // for()

  return null;
} // convCategory2Where()

//-----------------------------------------------------------------------------

function convCategory2Table(category) {
  for( var i = 0; i < categoryList.length; i++ ) {
    if( category == categoryList[i].id ) return categoryList[i].tn;

  } // for()

  return null;
} // convCategory2Table()

//-----------------------------------------------------------------------------

function convBrand2Where(brand) {
  for( var i = 0; i < brandList.length; i++ ) {
    if( brand == brandList[i].id ) return brandList[i].wc;

  } // for()

  return null;
} // convBrand2Where()

//-----------------------------------------------------------------------------

function isMyPackage(pkg) {
  for( var i = 0; i < packages.length; i++ ) {
    if( packages[i] == pkg ) return true;

  } // for()

  return false;
} // MyPackages()

//-----------------------------------------------------------------------------

function trans2ucase(txt) {
  var s = "";
  for( var i = 0; i < txt.length; i++ ) {
    var ch = txt.charAt(i);
    switch( ch ) {
      case 'ç' : s += 'Ç'; break;
      case 'þ' : s += "\xDE"; break;
      case 'Þ' : s += "\xDE"; break;
      case 'ð' : s += "\xD0"; break;
      case 'Ð' : s += "\xD0"; break;
      case 'i' : s += "\xDD"; break;
      case 'Ý' : s += "\xDD"; break;
      case 'ý' : s += 'I'; break;
      case 'ö' : s += 'Ö'; break;
      case 'ü' : s += 'Ü'; break;
      default :
        s += ch.toUpperCase();
        break;
    }
  } // for()
  return s;
} // trans2ucase()

//-----------------------------------------------------------------------------

function trans2getstr(txt) {
  var s = "";
  for( var i = 0; i < txt.length; i++ ) {
    var ch = txt.charAt(i);
    switch( ch ) {
      case 'Þ' : s += "%DE"; break;
      case 'þ' : s += "%EE"; break;
      case 'Ð' : s += "%D0"; break;
      case 'ð' : s += "%F0"; break;
      case 'Ý' : s += "%DD"; break;
      case 'ý' : s += "%FD"; break;
      default :
        s += ch;
        break;
    }
  } // for()
  return s;
}
//-----------------------------------------------------------------------------

function trans2xmlstr(txt) {
  var s = "";
  for( var i = 0; i < txt.length; i++ ) {
    var ch = txt.charAt(i);
    switch( ch ) {
      case  '<' : s += "&lt;"; break;
      case  '>' : s += "&gt;"; break;
      case  '&' : s += "&amp;"; break;
      case '\'' : s += "&apos;"; break;
      case  '"' : s += "&quot;"; break;
      default :
        s += ch;
        break;
    }
  } // for()
  return s;
}

//-----------------------------------------------------------------------------

function trim(txt) {
  var s = "";
  for( var i = 0; i < txt.length; i++ ) {
    var ch = txt.charAt(i);
    switch( ch ) {
      case ' ' : break;
      case '\r' : break;
      case '\n' : break;
      default :
        s += ch;
        break;
    } // switch()
  } // for()
  return s;
} // trim()

//-----------------------------------------------------------------------------

function formatCoordinate(num, typ) {
  var s = "" + num;
  if( s.length > 9 ) s = s.substring(0, 9);
  return s;
} // formatCoordinate()

//-----------------------------------------------------------------------------

function formatNumber(num, len) {
  var s = "" + num;
  if( s.length < len ) s = "0000000".substring(0, len - s.length) + s;
  return s;
} // formatNumber()

//-----------------------------------------------------------------------------

function formatDuration(duration) {
  if( !duration && duration != 0 ) return '?dk';

  if( duration < 60 ) return duration + ' dk.';

  return parseInt(duration / 60) + ' sa. ' + parseInt(duration % 60) + ' dk.';
} // formatDuration()

//-----------------------------------------------------------------------------

function formatDistance(dist) {
  if( !dist && dist != 0 ) return "?m";

  var n = 0;
  if( dist > 1000 ) {
      dist /= 1000;
      n = 1;
  }

  var distStr = "" + dist;
  var pos = distStr.indexOf(".");
  if( pos < 0 ) pos = distStr.indexOf(",");
  if( pos < 0 ) {
    pos = distStr.length;
    distStr += ".00000";
  }

  if( n &&  n > 0 ) pos += (n + 1);
  return distStr.substring(0, pos) + (n == 0 ? "m" : "km");
} // formatDistance()

//-----------------------------------------------------------------------------

var hourglassElement;

function setHourglass(func) {
  var elmMap = document.getElementById("map");
  if( hourglassElement == null ) {
    hourglassElement = document.createElement("img");
    hourglassElement.src = "./images/hourglass.gif";
    hourglassElement.style.zIndex = 2001;
    hourglassElement.style.position = "absolute";
    var width = 0;
    if( hourglassElement.width ) width = hourglassElement.width;
    var height = 0;
    if( hourglassElement.height ) height = hourglassElement.height;
    hourglassElement.style.left = parseInt((elmMap.offsetWidth - width) / 2) + "px";
    hourglassElement.style.top = parseInt((elmMap.offsetHeight - height) / 2) + "px";
    hourglassElement.style.visibility = 'visible';
    hourglassElement.onLoad = function() {
                         hourglassElement.style.left = parseInt((elmMap.offsetWidth - hourglassElement.width) / 2) + "px";
                         hourglassElement.style.top = parseInt((elmMap.offsetHeight - hourglassElement.height) / 2) + "px";
                         hourglassElement.onload = null;
                       };
  }
  elmMap.appendChild(hourglassElement);
  if( func ) self.setTimeout(func, 100);
  return;
} // setHourglass()

function clearHourglass() {
  if( hourglassElement != null ) {
    document.getElementById("map").removeChild(hourglassElement);
    hourglassElement = null;
  }
  return;
} // clearHourglass()

//-----------------------------------------------------------------------------

function getPointGeometryObject(longitude, latitude, mview) {
  var pt;
  if( latitude < 90 && longitude < 180 )
      pt = new OM.geometry.Point(longitude, latitude, 8307, 0, 0);	
  else {
    var srid = mview.getUniverse().getSRID();
    if( IMapper.mustTransform ) srid = coorSrid;
    pt = new OM.geometry.Point(longitude, latitude, srid, 0, 0);
  }
  return pt;
} // getPointGeometryObject()

//-----------------------------------------------------------------------------
function getPointGeometryObjectCustom(longitude, latitude, mview) {
  var pt;
  if( latitude < 90 && longitude < 180 )
          pt = new OM.geometry.Point(longitude, latitude, 8307);	
      else {
        var srid = mview.getUniverse().getSRID();
        if( IMapper.mustTransform ) srid = coorSrid;
        pt = new OM.geometry.Point(longitude, latitude, srid);
  }
  return pt;
} // getPointGeometryObject()
//-----------------------------------------------------------------------------

function getRectangleGeometryObject(minLongitude, minLatitude, maxLongitude, maxLatitude, mview) {
  var rect;
  if( minLatitude < 90 && minLongitude < 180 && maxLatitude < 90 && maxLongitude < 180 )
    rect = new OM.geometry.Rectangle(minLongitude, minLatitude, maxLongitude, maxLatitude, 8307);
  else {
    var srid = mview.getUniverse().getSRID();
    rect = new OM.geometry.Rectangle(minLongitude, minLatitude, maxLongitude, maxLatitude, srid);
  }
  return rect;
} // getRectangleGeometryObject()

//-----------------------------------------------------------------------------

function transformOrdinatesGeom(oarray, mview) {
  var srid = mview.getUniverse().getSRID();
  var ordinates = "";
  for( var i = 0; i < oarray[0].length; i += 2 ) {
     var pos = new OM.geometry.Point(oarray[0][i], oarray[0][i+1],srid);
     pos = pos.transform(8307);
     if( i > 0 ) ordinates += ",";
     ordinates += pos.getX() + "," + pos.getY();
  } // for()
  return ordinates;
} // transformOrdinatesGeom()

//-----------------------------------------------------------------------------

function dump(elm) {
  s = "";
  var cnt = 0;
  for( property in elm ) {
    if( property.length >= 2 && property.substring(0, 2) == "on" ) continue;

    s += property + ": "+ elm[property] + " \n";
    cnt++;
    if( cnt >= 10 ) {
      alert(s);
      s = "";
      cnt = 0;
    }
  } // for()
  if( cnt > 0 ) alert(s);
  return;
} // dump()

//-----------------------------------------------------------------------------

function parseChilds(xmlDoc) {
  var s = '';
  var childs = xmlDoc.childNodes;
  if( childs.length == 1 && childs[0].nodeName == '#text' ) {
    s += '"' + childs[0].nodeValue + '"';
    return s;
  }

  var pname;
  var arrayOk = true;
  if( childs.length <= 1 ) arrayOk = false;
  else {
    for( var i = 0; i < childs.length; i++ ) {
      if( i == 0 ) pname = childs[i].nodeName;
      else
        if( pname != childs[i].nodeName ) {
          arrayOk = false;
          break;
        }
    } // for()
  } // else

  s += '{ ';
  var attrs = xmlDoc.attributes;
  if( attrs ) {
    if( attrs.length > 0 ) s += "attrs: { ";
    for( var i = 0; i < attrs.length; i++ ) {
      if( i > 0 ) s += ', ';
      s += attrs[i].nodeName + ': "' + attrs[i].nodeValue + '"';
    } // for()
    if( attrs.length > 0 ) s += " }";
  }
  for( var i = 0; i < childs.length; i++ ) {
    if( i > 0 || (attrs && attrs.length > 0) ) s += ', ';
    if( arrayOk ) {
      if( i == 0 ) s += childs[i].nodeName + ': [';
    }
    else {
      s += childs[i].nodeName + ': ';
    }
    s += parseChilds(childs[i]);
  } // for()
  s += "}";
  return s;
} // parseChilds()

//-----------------------------------------------------------------------------

function floor1e5(coordinate) {
  return parseInt(Math.round(coordinate * 100000.0));
} // floor1e5()

function encodeSignedNumber(num) {
  var snum = (num << 1);
  if( num < 0 ) snum = ~(snum);
  return(encodeNumber(snum));
} // encodeSignedNumber()

function encodeNumber(num) {
  var estr = "";
  while( num >= 0x20 ) {
    var nextValue = (0x20 | (num & 0x1f)) + 63;
    if( nextValue == 92 ) estr += String.fromCharCode(nextValue);
    estr += String.fromCharCode(nextValue);
    num >>= 5;
  } // while()

  num += 63;
  if( num == 92 ) estr += String.fromCharCode(num);
  estr += String.fromCharCode(num);
  return estr;
} // encodeNumber()

function encodePolyline(coors) {
  var estr = "";

  var prevLat = 0;
  var prevLon = 0;
  for( var i = 0; i < coors.length / 2; i++) {
    var lat = floor1e5(coors[2*i + 1]);
    var lon = floor1e5(coors[2*i + 0]);
    var deltaLat = lat - prevLat;
    var deltaLon = lon - prevLon;
    prevLat = lat;
    prevLon = lon;

    estr += encodeSignedNumber(deltaLat);
    estr += encodeSignedNumber(deltaLon);
  } // for()

  return estr;
} // encodePolyline()

function decodePolyline(estr) {
  var len = estr.length;
  var inx = 0;
  var lat = 0;
  var lon = 0;

  var array = new Array();

  while( inx < len ) {
    var b = 0;
    var shift = 0;
    var result = 0;
    do {
      b = estr.charCodeAt(inx++) - 63;
      result |= (b & 0x1f) << shift;
      shift += 5;
    } while (b >= 0x20);

    var dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
    lat += dlat;

    shift = 0;
    result = 0;
    do {
      b = estr.charCodeAt(inx++) - 63;
      result |= (b & 0x1f) << shift;
      shift += 5;
    } while (b >= 0x20);
    var dlon = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
    lon += dlon;

    array.push(lon / 100000.0);
    array.push(lat / 100000.0);
  } // while()

  return array;
} // decodePolyline()

function rectangleMbrToOrdinates(mbr) { 
  var ords = [mbr[0],mbr[1], mbr[2],mbr[1], mbr[2],mbr[3], mbr[0],mbr[3], mbr[0],mbr[1]]; 
  return ords; 
}


// SIMPLIFY functions

function getSqDist(p1, p2) {

  var dx = p1.x - p2.x,
      dy = p1.y - p2.y;

  return dx * dx + dy * dy;
}

// square distance from a point to a segment
function getSqSegDist(p, p1, p2) {
  var x = p1.x,
  y = p1.y,
  dx = p2.x - x,
  dy = p2.y - y;
  
  if (dx !== 0 || dy !== 0) {
	  
    var t = ((p.x - x) * dx + (p.y - y) * dy) / (dx * dx + dy * dy);

    if (t > 1) {
      x = p2.x;
      y = p2.y;
	  
    } else if (t > 0) {
	  x += dx * t;
      y += dy * t;
    }
  }

  dx = p.x - x;
  dy = p.y - y;

  return dx * dx + dy * dy;
}
// rest of the code doesn't care about point format

// basic distance-based simplification
function simplifyRadialDist(points, sqTolerance) {

  var prevPoint = points[0],
  newPoints = [prevPoint],
    point;

  for (var i = 1, len = points.length; i < len; i++) {
    point = points[i];

    if (getSqDist(point, prevPoint) > sqTolerance) {
      newPoints.push(point);
      prevPoint = point;
    }
  }

  if (prevPoint !== point) newPoints.push(point);

  return newPoints;
}

function simplifyDPStep(points, first, last, sqTolerance, simplified) {
  var maxSqDist = sqTolerance,
      index;

  for (var i = first + 1; i < last; i++) {
    var sqDist = getSqSegDist(points[i], points[first], points[last]);

    if (sqDist > maxSqDist) {
      index = i;
      maxSqDist = sqDist;
    }
  }

  if (maxSqDist > sqTolerance) {
    if (index - first > 1) simplifyDPStep(points, first, index, sqTolerance, simplified);
      simplified.push(points[index]);
        if (last - index > 1) simplifyDPStep(points, index, last, sqTolerance, simplified);
    }
}

// simplification using Ramer-Douglas-Peucker algorithm
function simplifyDouglasPeucker(points, sqTolerance) {
  var last = points.length - 1;

  var simplified = [points[0]];
  simplifyDPStep(points, 0, last, sqTolerance, simplified);
  simplified.push(points[last]);

  return simplified;
}

// both algorithms combined for awesome performance
function simplify(points, tolerance, highestQuality) {

  if (points.length <= 2) return points;

  var sqTolerance = tolerance !== undefined ? tolerance * tolerance : 1;

  points = highestQuality ? points : simplifyRadialDist(points, sqTolerance);
  points = simplifyDouglasPeucker(points, sqTolerance);

  return points;
}

// SIMPLIFY functions end
