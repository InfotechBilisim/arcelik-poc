var unitymapper;var slat;var slon;function IMapper(){}
IMapper.prototype.initMap=function(ab,bb,cb,db,eb){if(Math.abs((new Date()).getTime()-gtm)<86423134){var config={params:{width:cb,height:db,enableDebugging:"0",logoimage:"http://www.locationbox.com.tr/web/css/images/logo.png",fb:true}
}
;unitymapper=new UnityObject2(config);if(!eb)var eb="map";slat=ab;slon=bb;jQuery(function(){var gb=jQuery("#"+eb).find(".missing");var hb=jQuery("#"+eb).find(".broken");gb.hide();hb.hide();unitymapper.observeProgress(function(ib){switch(ib.pluginStatus){case"broken":hb.find("a").click(function(e){e.stopPropagation();e.preventDefault();unitymapper.installPlugin();return false;}
);hb.show();break;case"missing":gb.find("a").click(function(e){e.stopPropagation();e.preventDefault();unitymapper.installPlugin();return false;}
);gb.show();break;case"installed":gb.remove();break;case"first":break;}
}
);unitymapper.initPlugin(jQuery("#"+eb)[0],unity3dUrl);}
);}
return;}
IMapper.prototype.flyToCoordinate=function(ab,bb){if(Math.abs((new Date()).getTime()-gtm)<86423134){unitymapper.getUnity().SendMessage("GisApi","ExecuteCommand","GisApiCommandGoToLonLat "+bb+","+ab+"#0#Geographic");}
return;}
IMapper.prototype.addCustomMarker=function(jb,ab,bb,kb){if(Math.abs((new Date()).getTime()-gtm)<86423134){unitymapper.getUnity().SendMessage("GisApi","ExecuteCommand","GisApiCommandAddBillboard pm"+jb+"#"+kb+"#"+bb+","+ab+"#Geographic");}
return;}
IMapper.prototype.removeCustomMarker=function(jb){if(Math.abs((new Date()).getTime()-gtm)<86423134){unitymapper.getUnity().SendMessage("GisApi","ExecuteCommand","GisApiCommandRemoveResource pm"+jb);}
return;}
function GisApiOnBoundariesDefined(){if(Math.abs((new Date()).getTime()-gtm)<86423134){unitymapper.getUnity().SendMessage("GisApi","ExecuteCommand","GisApiCommandGoToLonLat "+slon+","+slat+"#1#Geographic");}
}
