var unitymapper;
var slat;
var slon;

function IMapper() {
}

IMapper.prototype.initMap=function(lat, lon, wdth, hght, divid) {
  
  if( Math.abs((new Date()).getTime() - gtm) < 86423134 ) {
  
    var config = {params: { width: wdth, height: hght, enableDebugging:"0" , logoimage:"http://www.locationbox.com.tr/web/css/images/logo.png", disableContextMenu:true } };

    unitymapper = new UnityObject2(config);
			
    if( !divid ) var divid = "map";
	
	slat = lat;
	slon = lon;

	jQuery(function() {

	  var $missingScreen = jQuery("#" + divid).find(".missing");
	  var $brokenScreen = jQuery("#" + divid).find(".broken");
	  $missingScreen.hide();
	  $brokenScreen.hide();
				
	  unitymapper.observeProgress(function (progress) {
	  switch(progress.pluginStatus) {
	    case "broken":
		  $brokenScreen.find("a").click(function (e) {
			  e.stopPropagation();
			  e.preventDefault();
			  unitymapper.installPlugin();
			  return false;
				});
			  $brokenScreen.show();
			  break;
			  case "missing":
			    $missingScreen.find("a").click(function (e) {
			    e.stopPropagation();
				e.preventDefault();
				unitymapper.installPlugin();
				return false;
				});
				$missingScreen.show();
				break;
				case "installed":
				$missingScreen.remove();
				break;
				case "first":
				break;
				}
				});
		unitymapper.initPlugin(jQuery("#" + divid)[0], unity3dUrl);
				
	 });
  }		        
  return;
}


IMapper.prototype.flyToCoordinate=function(lat, lon) {

  if( Math.abs((new Date()).getTime() - gtm) < 86423134 ) {
    unitymapper.getUnity().SendMessage("GisApi", "ExecuteCommand", "GisApiCommandGoToLonLat " + lon + "," + lat + "#0#Geographic");
  }
			
  return;
}

IMapper.prototype.addCustomMarker=function(id, lat, lon, imgUrl) {
  
  if( Math.abs((new Date()).getTime() - gtm) < 86423134 ) {
    unitymapper.getUnity().SendMessage("GisApi", "ExecuteCommand", "GisApiCommandAddBillboard pm" + id + "#" + imgUrl + "#" + lon + "," + lat + "#Geographic");    
  }  
  
  return;
}

IMapper.prototype.removeCustomMarker=function(id) {

  if( Math.abs((new Date()).getTime() - gtm) < 86423134 ) {
    unitymapper.getUnity().SendMessage("GisApi", "ExecuteCommand", "GisApiCommandRemoveResource pm" + id);
  }
  
  return;
}

function GisApiOnBoundariesDefined() {
  if( Math.abs((new Date()).getTime() - gtm) < 86423134 ) {
    unitymapper.getUnity().SendMessage("GisApi", "ExecuteCommand", "GisApiCommandGoToLonLat " + slon + "," + slat + "#1#Geographic"); 
  }
}